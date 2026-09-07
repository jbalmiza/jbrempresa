package com.jbrempresa.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.entity.Parametro;
import com.jbrempresa.backend.core.config.ConfiguracionSmtpEmpresaService;
import com.jbrempresa.backend.core.config.ConfiguracionWhatsappEmpresaService;
import com.jbrempresa.backend.core.config.ConfiguracionRedsysBizumService;
import com.jbrempresa.backend.repository.ParametroRepository;
import com.jbrempresa.backend.security.JwtUser;

@RestController
@RequestMapping("/parametros")
@CrossOrigin(origins = "http://localhost:4200")
public class ParametroController {
    private final ParametroRepository parametroRepository;
    private final ConfiguracionSmtpEmpresaService configuracionSmtp;
    private final ConfiguracionWhatsappEmpresaService configuracionWhatsapp;
    private final ConfiguracionRedsysBizumService configuracionRedsys;

    public ParametroController(
            ParametroRepository parametroRepository,
            ConfiguracionSmtpEmpresaService configuracionSmtp,
            ConfiguracionWhatsappEmpresaService configuracionWhatsapp,
            ConfiguracionRedsysBizumService configuracionRedsys) {
        this.parametroRepository = parametroRepository;
        this.configuracionSmtp = configuracionSmtp;
        this.configuracionWhatsapp = configuracionWhatsapp;
        this.configuracionRedsys = configuracionRedsys;
    }

    @GetMapping
    public List<Parametro> consultar(
            @RequestParam(name = "modulo", required = false) String modulo) {
        Long empId = obtenerEmpresa();
        if (modulo == null || modulo.isBlank()) {
            return ocultarSensibles(parametroRepository.findByEmpIdOrderByParId(empId));
        }
        return ocultarSensibles(
                parametroRepository.findByEmpIdAndParModIgnoreCaseOrderByParId(empId, modulo.trim()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Parametro guardar(@RequestBody Parametro parametro) {
        Long empId = obtenerEmpresa();
        validar(parametro);
        if (parametroRepository.existsByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(
                empId, parametro.getParMod().trim(), parametro.getParCod().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un parámetro con ese código.");
        }
        parametro.setParId(null);
        parametro.setEmpId(empId);
        parametro.setParUsuMov(obtenerUsuario());
        parametro.setParFecMov(LocalDateTime.now());
        parametro.setParAct(true);
        normalizar(parametro);
        prepararSensible(parametro, null);
        return ocultarSensible(parametroRepository.save(parametro));
    }

    @PutMapping("/{id}")
    public Parametro actualizar(@PathVariable Long id, @RequestBody Parametro parametro) {
        Long empId = obtenerEmpresa();
        Parametro existente = buscarDelCliente(empId, id);
        validar(parametro);
        if (parametroRepository.existsByEmpIdAndParModIgnoreCaseAndParCodIgnoreCaseAndParIdNot(
                empId, parametro.getParMod().trim(), parametro.getParCod().trim(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un parámetro con ese código.");
        }
        existente.setParCod(parametro.getParCod());
        existente.setParMod(parametro.getParMod());
        existente.setParDes(parametro.getParDes());
        String valorAnterior = existente.getParVal();
        existente.setParVal(parametro.getParVal());
        existente.setParUsuMov(obtenerUsuario());
        existente.setParFecMov(LocalDateTime.now());
        normalizar(existente);
        prepararSensible(existente, valorAnterior);
        return ocultarSensible(parametroRepository.save(existente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        parametroRepository.delete(buscarDelCliente(obtenerEmpresa(), id));
    }

    private Parametro buscarDelCliente(Long empId, Long id) {
        return parametroRepository.findByEmpIdAndParId(empId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parámetro no encontrado."));
    }

    private void validar(Parametro parametro) {
        if (parametro.getParMod() == null || parametro.getParMod().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El módulo es obligatorio.");
        }
        if (parametro.getParCod() == null || parametro.getParCod().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código es obligatorio.");
        }
        if (parametro.getParDes() == null || parametro.getParDes().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria.");
        }
        boolean sensible = esSensible(parametro.getParMod(), parametro.getParCod());
        if (!sensible && (parametro.getParVal() == null || parametro.getParVal().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El valor es obligatorio.");
        }
        if (parametro.getParMod().trim().length() > 50
                || parametro.getParCod().trim().length() > 100
                || parametro.getParDes().trim().length() > 150
                || (parametro.getParVal() != null && parametro.getParVal().trim().length() > 2000)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La longitud del parámetro no es válida.");
        }
    }

    private void normalizar(Parametro parametro) {
        parametro.setParMod(parametro.getParMod().trim().toUpperCase());
        parametro.setParCod(parametro.getParCod().trim().toUpperCase());
        parametro.setParDes(parametro.getParDes().trim());
        parametro.setParVal(parametro.getParVal() == null ? "" : parametro.getParVal().trim());
    }

    private void prepararSensible(Parametro parametro, String valorAnterior) {
        boolean smtp = configuracionSmtp.esPassword(parametro.getParMod(), parametro.getParCod());
        boolean whatsapp = configuracionWhatsapp.esSensible(parametro.getParMod(), parametro.getParCod());
        boolean redsys = configuracionRedsys.esSensible(parametro.getParMod(), parametro.getParCod());
        if (!smtp && !whatsapp && !redsys) return;
        String valor = parametro.getParVal();
        if (ConfiguracionSmtpEmpresaService.VALOR_OCULTO.equals(valor)
                || ConfiguracionWhatsappEmpresaService.VALOR_OCULTO.equals(valor)) {
            parametro.setParVal(valorAnterior == null ? "" : valorAnterior);
        } else if (valor != null && !valor.isBlank()) {
            parametro.setParVal(smtp ? configuracionSmtp.cifrarPassword(valor)
                    : whatsapp ? configuracionWhatsapp.cifrar(valor) : configuracionRedsys.cifrar(valor));
        }
    }

    private List<Parametro> ocultarSensibles(List<Parametro> parametros) {
        return parametros.stream().map(this::ocultarSensible).toList();
    }

    private Parametro ocultarSensible(Parametro origen) {
        Parametro copia = new Parametro();
        copia.setParId(origen.getParId()); copia.setEmpId(origen.getEmpId());
        copia.setParCod(origen.getParCod()); copia.setParMod(origen.getParMod());
        copia.setParDes(origen.getParDes()); copia.setParVal(origen.getParVal());
        copia.setParUsuMov(origen.getParUsuMov()); copia.setParFecMov(origen.getParFecMov());
        copia.setParAct(origen.getParAct());
        if (configuracionSmtp.esPassword(copia.getParMod(), copia.getParCod())) {
            copia.setParVal(configuracionSmtp.ocultarPassword(copia.getParVal()));
        } else if (configuracionWhatsapp.esSensible(copia.getParMod(), copia.getParCod())) {
            copia.setParVal(configuracionWhatsapp.ocultar(copia.getParVal()));
        } else if (configuracionRedsys.esSensible(copia.getParMod(), copia.getParCod())) {
            copia.setParVal(configuracionRedsys.ocultar(copia.getParVal()));
        }
        return copia;
    }

    private boolean esSensible(String modulo, String codigo) {
        return configuracionSmtp.esPassword(modulo, codigo)
                || configuracionWhatsapp.esSensible(modulo, codigo)
                || configuracionRedsys.esSensible(modulo, codigo);
    }

    private Long obtenerEmpresa() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser usuario = (JwtUser) authentication.getPrincipal();
        return usuario.getEmpresaId();
    }

    private String obtenerUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser usuario = (JwtUser) authentication.getPrincipal();
        return usuario.getUsername();
    }
}
