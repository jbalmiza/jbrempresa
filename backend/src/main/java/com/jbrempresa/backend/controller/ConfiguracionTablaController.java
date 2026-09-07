package com.jbrempresa.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.ConfiguracionTabla;
import com.jbrempresa.backend.repository.ConfiguracionTablaRepository;

@RestController
@RequestMapping("/configuraciones-tabla")
public class ConfiguracionTablaController {
    private final ConfiguracionTablaRepository repositorio;
    private final ContextoOperacion contexto;
    public ConfiguracionTablaController(ConfiguracionTablaRepository repositorio,ContextoOperacion contexto){this.repositorio=repositorio;this.contexto=contexto;}

    @GetMapping
    public ResponseEntity<ConfiguracionTabla> obtener(@RequestParam String clave){
        validarClave(clave);
        return repositorio.findByEmpIdAndUsuIdAndCotClaAndCotActTrue(contexto.empresaId(),contexto.usuarioActual().getUsuarioId(),clave.trim())
                .map(configuracion -> ResponseEntity.ok(configuracion)).orElseGet(()->ResponseEntity.noContent().build());
    }

    @PutMapping
    public ConfiguracionTabla guardar(@RequestBody ConfiguracionTabla entrada){
        validarClave(entrada.getCotCla());
        if(entrada.getCotCon()==null||entrada.getCotCon().isBlank()||entrada.getCotCon().length()>20000)throw new IllegalArgumentException("La configuración de tabla no es válida.");
        Long empId=contexto.empresaId();Long usuId=contexto.usuarioActual().getUsuarioId();String clave=entrada.getCotCla().trim();
        ConfiguracionTabla valor=repositorio.findByEmpIdAndUsuIdAndCotClaAndCotActTrue(empId,usuId,clave).orElseGet(()->new ConfiguracionTabla());
        valor.setEmpId(empId);valor.setUsuId(usuId);valor.setCotCla(clave);valor.setCotCon(entrada.getCotCon());valor.setCotUsuMov(contexto.nombreUsuario());valor.setCotFecMov(contexto.fechaActual());valor.setCotAct(true);
        return repositorio.save(valor);
    }

    private void validarClave(String clave){if(clave==null||clave.isBlank()||clave.length()>500)throw new IllegalArgumentException("La clave de configuración de tabla no es válida.");}
}
