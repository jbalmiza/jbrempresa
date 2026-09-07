package com.jbrempresa.backend.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.entity.Domicilio;
import com.jbrempresa.backend.repository.DomicilioRepository;
import com.jbrempresa.backend.repository.ViaRepository;
import com.jbrempresa.backend.repository.CodigoPostalRepository;
import com.jbrempresa.backend.repository.MunicipioRepository;
import com.jbrempresa.backend.repository.ProvinciaRepository;
import com.jbrempresa.backend.security.JwtUser;

@RestController
@RequestMapping("/domicilio")
@CrossOrigin(origins = "http://localhost:4200")
public class DomicilioController {
    private final DomicilioRepository repository;
    private final ViaRepository viaRepository;
    private final CodigoPostalRepository codigoPostalRepository;
    private final MunicipioRepository municipioRepository;
    private final ProvinciaRepository provinciaRepository;

    public DomicilioController(
            DomicilioRepository repository,
            ViaRepository viaRepository,
            CodigoPostalRepository codigoPostalRepository,
            MunicipioRepository municipioRepository,
            ProvinciaRepository provinciaRepository) {
        this.repository = repository;
        this.viaRepository = viaRepository;
        this.codigoPostalRepository = codigoPostalRepository;
        this.municipioRepository = municipioRepository;
        this.provinciaRepository = provinciaRepository;
    }

    private JwtUser usuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (JwtUser) auth.getPrincipal();
    }

    @PostMapping @Transactional
    public Domicilio guardar(@RequestBody Domicilio domicilio) {
        domicilio.setEmpId(usuario().getEmpresaId());
        completarTerritorio(domicilio);
        domicilio.setDomId(repository.obtenerSiguienteId(usuario().getEmpresaId()));
        domicilio.setDomIdHis(1L);
        domicilio.setDomTipMov("A");
        if (domicilio.getDomCauMov() == null || domicilio.getDomCauMov().isBlank()) domicilio.setDomCauMov("Alta del registro");
        domicilio.setDomAct(true);
        domicilio.setDomUsuMov(usuario().getUsername());
        domicilio.setDomFecMov(LocalDateTime.now());
        return repository.save(domicilio);
    }

    @PutMapping("/{id}") @Transactional
    public Domicilio actualizar(@PathVariable Long id, @RequestBody Domicilio domicilio) {
        Long empId = usuario().getEmpresaId();
        Domicilio anterior = vigente(empId, id);
        comprobarModificable(anterior);
        anterior.setDomAct(false);
        repository.save(anterior);
        domicilio.setEmpId(empId);
        completarTerritorio(domicilio);
        domicilio.setDomId(id);
        domicilio.setDomIdHis(anterior.getDomIdHis() + 1);
        domicilio.setDomTipMov("M");
        if (domicilio.getDomCauMov() == null || domicilio.getDomCauMov().isBlank()) domicilio.setDomCauMov("Modificación del registro");
        domicilio.setDomAct(true);
        domicilio.setDomUsuMov(usuario().getUsername());
        domicilio.setDomFecMov(LocalDateTime.now());
        return repository.save(domicilio);
    }

    @GetMapping
    public List<Domicilio> obtenerDomicilios() {
        return repository.findByEmpIdAndDomActTrueOrderByDomId(usuario().getEmpresaId());
    }

    @GetMapping("/siguiente-id") public Long obtenerSiguienteId() { return repository.obtenerSiguienteId(usuario().getEmpresaId()); }

    @DeleteMapping("/{id}") @Transactional
    public void eliminar(@PathVariable Long id) {
        Long empId = usuario().getEmpresaId();
        vigente(empId, id);
        repository.deleteAll(repository.findByEmpIdAndDomIdOrderByDomFecMovDesc(empId, id));
    }

    @PostMapping("/{id}/baja") @Transactional
    public Domicilio baja(@PathVariable Long id) {
        Domicilio anterior = vigente(usuario().getEmpresaId(), id);
        comprobarModificable(anterior);
        anterior.setDomAct(false);
        repository.save(anterior);
        Domicilio baja = new Domicilio();
        BeanUtils.copyProperties(anterior, baja);
        baja.setDomIdHis(anterior.getDomIdHis() + 1);
        baja.setDomTipMov("B");
        baja.setDomCauMov("Baja del registro");
        baja.setDomAct(true);
        baja.setDomUsuMov(usuario().getUsername());
        baja.setDomFecMov(LocalDateTime.now());
        return repository.save(baja);
    }

    @GetMapping("/{id}/historico")
    public List<Domicilio> historico(@PathVariable Long id) {
        Long empId = usuario().getEmpresaId();
        vigente(empId, id);
        return repository.findByEmpIdAndDomIdOrderByDomFecMovDesc(empId, id);
    }

    @PostMapping("/{id}/deshacer") @Transactional
    public Domicilio deshacer(@PathVariable Long id) {
        Long empId = usuario().getEmpresaId();
        Domicilio actual = vigente(empId, id);
        if (actual.getDomIdHis() <= 1) conflicto("No existen movimientos anteriores para deshacer.");
        Domicilio anterior = repository.findByEmpIdAndDomIdAndDomIdHis(empId, id, actual.getDomIdHis() - 1)
                .orElseThrow(() -> new RuntimeException("Movimiento anterior no encontrado."));
        repository.delete(actual);
        anterior.setDomAct(true);
        return repository.save(anterior);
    }

    private Domicilio vigente(Long empId, Long id) {
        return repository.findByEmpIdAndDomIdAndDomActTrue(empId, id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado."));
    }
    private void comprobarModificable(Domicilio d) { if ("B".equals(d.getDomTipMov())) conflicto("El domicilio está dado de baja. Deshaga primero la baja."); }
    private void completarTerritorio(Domicilio d) {
        if (d.getDomViaId() == null) throw new IllegalArgumentException("La vía es obligatoria.");
        var via = viaRepository.findByEmpIdAndViaId(d.getEmpId(), d.getDomViaId()).filter(v -> Boolean.TRUE.equals(v.getViaAct())).orElseThrow(() -> new IllegalArgumentException("La vía no es válida para el cliente."));
        var codigo = codigoPostalRepository.findByEmpIdAndCopId(d.getEmpId(), via.getCopId()).orElseThrow(() -> new IllegalArgumentException("Código postal relacionado no encontrado."));
        var municipio = municipioRepository.findByEmpIdAndMunId(d.getEmpId(), codigo.getMunId()).orElseThrow(() -> new IllegalArgumentException("Municipio relacionado no encontrado."));
        var provincia = provinciaRepository.findByEmpIdAndPrvId(d.getEmpId(), municipio.getPrvId()).orElseThrow(() -> new IllegalArgumentException("Provincia relacionada no encontrada."));
        d.setDomTipVia(via.getViaTip()); d.setDomVia(via.getViaNom()); d.setDomCp(codigo.getCopCod()); d.setDomMun(municipio.getMunNom()); d.setDomPro(provincia.getPrvNom());
    }
    private void conflicto(String mensaje) { throw new ResponseStatusException(HttpStatus.CONFLICT, mensaje); }
}
