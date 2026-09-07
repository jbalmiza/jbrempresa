package com.jbrempresa.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.PersonalArea;
import com.jbrempresa.backend.dto.administracion.OrganizacionDtos.*;import jakarta.validation.Valid;
import com.jbrempresa.backend.repository.AreaOrganizativaRepository;
import com.jbrempresa.backend.repository.PersonaRepository;
import com.jbrempresa.backend.repository.PersonalAreaRepository;

@RestController
@RequestMapping("/personal-area")
public class PersonalAreaController {
 private final PersonalAreaRepository repository; private final AreaOrganizativaRepository areas; private final PersonaRepository personas; private final ContextoOperacion contexto;
 public PersonalAreaController(PersonalAreaRepository r,AreaOrganizativaRepository a,PersonaRepository p,ContextoOperacion c){repository=r;areas=a;personas=p;contexto=c;}
 @GetMapping public List<PersonalSalida> consultar(){return repository.findByEmpIdOrderByPeaId(contexto.empresaId()).stream().map(personal->PersonalSalida.desde(personal)).toList();}
 @PostMapping public PersonalSalida guardar(@Valid @RequestBody PersonalEntrada datos){PersonalArea p=datos.entidad();p.setPeaId(null);preparar(p);validar(p,null);return PersonalSalida.desde(repository.save(p));}
 @PutMapping("/{id}") public PersonalSalida actualizar(@PathVariable Long id,@Valid @RequestBody PersonalEntrada datos){PersonalArea p=datos.entidad();repository.findByEmpIdAndPeaId(contexto.empresaId(),id).orElseThrow(()->new RuntimeException("Asignación no encontrada."));p.setPeaId(id);preparar(p);validar(p,id);return PersonalSalida.desde(repository.save(p));}
 @DeleteMapping("/{id}") public void eliminar(@PathVariable Long id){PersonalArea p=repository.findByEmpIdAndPeaId(contexto.empresaId(),id).orElseThrow(()->new RuntimeException("Asignación no encontrada."));repository.delete(p);}
 private void preparar(PersonalArea p){p.setEmpId(contexto.empresaId());p.setPeaUsuMov(contexto.nombreUsuario());p.setPeaFecMov(contexto.fechaActual());if(p.getPeaAct()==null)p.setPeaAct(true);if(p.getPeaPri()==null)p.setPeaPri(false);if(p.getPeaRes()==null)p.setPeaRes(false);}
 private void validar(PersonalArea p,Long id){Long cli=p.getEmpId();areas.findByEmpIdAndAreId(cli,p.getAreId()).filter(a->Boolean.TRUE.equals(a.getAreAct())).orElseThrow(()->new IllegalArgumentException("El área no está activa o no pertenece al cliente."));personas.findByEmpIdAndPerIdAndPerActTrue(cli,p.getPerId()).filter(x->!"B".equals(x.getPerTipMov())).orElseThrow(()->new IllegalArgumentException("La persona no está activa o no pertenece al cliente."));
  boolean duplicada=id==null?repository.existsByEmpIdAndAreIdAndPerIdAndPeaActTrue(cli,p.getAreId(),p.getPerId()):repository.existsByEmpIdAndAreIdAndPerIdAndPeaActTrueAndPeaIdNot(cli,p.getAreId(),p.getPerId(),id);if(duplicada)throw new IllegalArgumentException("La persona ya pertenece a esta área.");
  boolean principal=Boolean.TRUE.equals(p.getPeaPri())&&(id==null?repository.existsByEmpIdAndPerIdAndPeaPriTrueAndPeaActTrue(cli,p.getPerId()):repository.existsByEmpIdAndPerIdAndPeaPriTrueAndPeaActTrueAndPeaIdNot(cli,p.getPerId(),id));if(principal)throw new IllegalArgumentException("La persona ya tiene un área principal.");
  if(p.getPeaFecDes()!=null&&p.getPeaFecHas()!=null&&p.getPeaFecHas().isBefore(p.getPeaFecDes()))throw new IllegalArgumentException("La fecha hasta no puede ser anterior a la fecha desde.");}
}
