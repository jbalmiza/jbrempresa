package com.jbrempresa.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.web.bind.annotation.*;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.AreaOrganizativa;
import com.jbrempresa.backend.dto.administracion.OrganizacionDtos.*;
import jakarta.validation.Valid;
import com.jbrempresa.backend.repository.AreaOrganizativaRepository;
import com.jbrempresa.backend.repository.PersonalAreaRepository;

@RestController
@RequestMapping("/areas-organizativas")
public class AreaOrganizativaController {
    private final AreaOrganizativaRepository repository;
    private final PersonalAreaRepository personalRepository;
    private final ContextoOperacion contexto;
    public AreaOrganizativaController(AreaOrganizativaRepository r, PersonalAreaRepository p, ContextoOperacion c) { repository=r; personalRepository=p; contexto=c; }

    @GetMapping public List<AreaSalida> consultar() { return repository.findByEmpIdOrderByAreNom(contexto.empresaId()).stream().map(area -> AreaSalida.desde(area)).toList(); }
    @PostMapping public AreaSalida guardar(@Valid @RequestBody AreaEntrada datos) { AreaOrganizativa area=datos.entidad();area.setAreId(null); preparar(area); validar(area,null); return AreaSalida.desde(repository.save(area)); }
    @PutMapping("/{id}") public AreaSalida actualizar(@PathVariable Long id,@Valid @RequestBody AreaEntrada datos) { AreaOrganizativa area=datos.entidad();
        repository.findByEmpIdAndAreId(contexto.empresaId(),id).orElseThrow(()->new RuntimeException("Área no encontrada."));
        area.setAreId(id); preparar(area); validar(area,id); return AreaSalida.desde(repository.save(area));
    }
    @DeleteMapping("/{id}") public void eliminar(@PathVariable Long id) {
        Long cli=contexto.empresaId(); AreaOrganizativa area=repository.findByEmpIdAndAreId(cli,id).orElseThrow(()->new RuntimeException("Área no encontrada."));
        if(repository.existsByEmpIdAndAreIdPad(cli,id)) throw new IllegalArgumentException("No se puede eliminar un área con áreas dependientes.");
        if(personalRepository.existsByEmpIdAndAreId(cli,id)) throw new IllegalArgumentException("No se puede eliminar un área con personal asignado.");
        repository.delete(area);
    }
    private void preparar(AreaOrganizativa a){ a.setEmpId(contexto.empresaId()); a.setAreUsuMov(contexto.nombreUsuario()); a.setAreFecMov(contexto.fechaActual()); if(a.getAreAct()==null)a.setAreAct(true); }
    private void validar(AreaOrganizativa a,Long id){
        if(a.getAreCod()==null||a.getAreCod().isBlank()||a.getAreNom()==null||a.getAreNom().isBlank()) throw new IllegalArgumentException("Código y nombre del área son obligatorios.");
        boolean repetido=id==null?repository.existsByEmpIdAndAreCodIgnoreCase(a.getEmpId(),a.getAreCod()):repository.existsByEmpIdAndAreCodIgnoreCaseAndAreIdNot(a.getEmpId(),a.getAreCod(),id);
        if(repetido) throw new IllegalArgumentException("Ya existe un área con ese código.");
        if(a.getAreIdPad()!=null){ if(a.getAreIdPad().equals(id)) throw new IllegalArgumentException("Un área no puede depender de sí misma."); validarJerarquia(a.getEmpId(),id,a.getAreIdPad()); }
    }
    private void validarJerarquia(Long cli,Long id,Long padre){ Set<Long> visitados=new HashSet<>(); Long actual=padre; while(actual!=null){ if(!visitados.add(actual)||actual.equals(id)) throw new IllegalArgumentException("La jerarquía de áreas contiene un ciclo."); AreaOrganizativa a=repository.findByEmpIdAndAreId(cli,actual).orElseThrow(()->new IllegalArgumentException("El área superior no pertenece al cliente.")); actual=a.getAreIdPad(); } }
}
