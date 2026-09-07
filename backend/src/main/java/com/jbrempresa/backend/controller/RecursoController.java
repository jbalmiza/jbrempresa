package com.jbrempresa.backend.controller;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.repository.*;

@RestController @RequestMapping("/recursos")
public class RecursoController {
 private final RecursoOperativoRepository recursos; private final RecursoCapacidadRepository capacidades; private final RecursoAgendableRepository recursosAgendables;
 private final PersonaRepository personas; private final ProductoRepository productos; private final ServicioRepository servicios;
 private final ContextoOperacion contexto;
 public RecursoController(RecursoOperativoRepository r,RecursoCapacidadRepository c,RecursoAgendableRepository ra,PersonaRepository p,ProductoRepository pr,ServicioRepository s,ContextoOperacion x){recursos=r;capacidades=c;recursosAgendables=ra;personas=p;productos=pr;servicios=s;contexto=x;}
 @GetMapping public List<RecursoOperativo> consultar(){return recursos.findByEmpIdAndReoActTrueOrderByReoNom(contexto.empresaId());}
 @PostMapping @Transactional public RecursoOperativo crear(@RequestBody RecursoOperativo r){r.setReoId(recursos.obtenerSiguienteId(contexto.empresaId()));r.setReoIdHis(1L);movimiento(r,"A");validar(r);return recursos.save(r);}
 @PutMapping("/{id}") @Transactional public RecursoOperativo actualizar(@PathVariable Long id,@RequestBody RecursoOperativo entrada){RecursoOperativo anterior=obtener(id);comprobarActivo(anterior);anterior.setReoAct(false);recursos.saveAndFlush(anterior);RecursoOperativo nuevo=new RecursoOperativo();BeanUtils.copyProperties(entrada,nuevo);nuevo.setReoId(id);nuevo.setReoIdHis(anterior.getReoIdHis()+1);movimiento(nuevo,"M");validar(nuevo);return recursos.save(nuevo);}
 @PostMapping("/{id}/baja") @Transactional public RecursoOperativo baja(@PathVariable Long id){RecursoOperativo anterior=obtener(id);comprobarActivo(anterior);anterior.setReoAct(false);recursos.saveAndFlush(anterior);RecursoOperativo baja=new RecursoOperativo();BeanUtils.copyProperties(anterior,baja);baja.setReoIdHis(anterior.getReoIdHis()+1);baja.setReoOpe(false);movimiento(baja,"B");return recursos.save(baja);}
 @DeleteMapping("/{id}") @Transactional public void eliminar(@PathVariable Long id){RecursoOperativo actual=obtener(id);if(actual.getReoIdHis()>1)throw conflicto("No se puede eliminar un recurso con histórico; debe gestionarse mediante baja.");if(recursosAgendables.findByEmpIdAndRagTipAndRagRefIdAndRagActTrue(contexto.empresaId(),"EMPLEADO",id).isPresent())throw conflicto("No se puede eliminar un empleado con agenda; debe gestionarse mediante baja.");capacidades.deleteByEmpIdAndReoId(contexto.empresaId(),id);recursos.delete(actual);}
 @GetMapping("/{id}/historico") public List<RecursoOperativo> historico(@PathVariable Long id){obtener(id);return recursos.findByEmpIdAndReoIdOrderByReoIdHisDesc(contexto.empresaId(),id);}
 @PostMapping("/{id}/deshacer") @Transactional public RecursoOperativo deshacer(@PathVariable Long id){RecursoOperativo actual=obtener(id);if(actual.getReoIdHis()<=1)throw conflicto("No existen movimientos anteriores para deshacer.");RecursoOperativo anterior=recursos.findByEmpIdAndReoIdAndReoIdHis(contexto.empresaId(),id,actual.getReoIdHis()-1).orElseThrow(()->errorNoEncontrado("Movimiento histórico no encontrado."));recursos.delete(actual);recursos.flush();anterior.setReoAct(true);return recursos.save(anterior);}
 @PatchMapping("/{id}/operativo") @Transactional public RecursoOperativo operativo(@PathVariable Long id,@RequestBody Estado e){RecursoOperativo anterior=obtener(id);comprobarActivo(anterior);anterior.setReoAct(false);recursos.saveAndFlush(anterior);RecursoOperativo nuevo=new RecursoOperativo();BeanUtils.copyProperties(anterior,nuevo);nuevo.setReoIdHis(anterior.getReoIdHis()+1);nuevo.setReoOpe(e.activo());nuevo.setReoCauMov("Cambio de operatividad");movimiento(nuevo,"M");return recursos.save(nuevo);}
 @GetMapping("/{id}/capacidades") public List<RecursoCapacidad> capacidades(@PathVariable Long id){obtener(id);return capacidades.findByEmpIdAndReoIdOrderByRecOriAscRecTipAsc(contexto.empresaId(),id);}
 @PutMapping("/{id}/capacidades") @Transactional public List<RecursoCapacidad> capacidades(@PathVariable Long id,@RequestBody List<CapacidadEntrada> lista){obtener(id);capacidades.deleteByEmpIdAndReoId(contexto.empresaId(),id);for(CapacidadEntrada e:lista){String origen=texto(e.origen()).toUpperCase();String tipo=texto(e.tipo());if(!Set.of("PRODUCTO","SERVICIO").contains(origen)||tipo.isBlank())throw error("Capacidad no valida.");RecursoCapacidad c=new RecursoCapacidad();c.setEmpId(contexto.empresaId());c.setReoId(id);c.setRecOri(origen);c.setRecTip(tipo);c.setRecAct(e.activa());capacidades.save(c);}return capacidades.findByEmpIdAndReoIdOrderByRecOriAscRecTipAsc(contexto.empresaId(),id);}
 @GetMapping("/tipos-capacidad") public List<CapacidadEntrada> tipos(){Set<String> vistos=new LinkedHashSet<>();List<CapacidadEntrada> out=new ArrayList<>();productos.findByEmpIdAndProActTrueOrderByProId(contexto.empresaId()).forEach(p->agregar(out,vistos,"PRODUCTO",p.getProTipPro()));servicios.findByEmpIdAndSerActTrueOrderBySerId(contexto.empresaId()).forEach(s->agregar(out,vistos,"SERVICIO",s.getSerTipSer()));return out;}
 private void agregar(List<CapacidadEntrada> out,Set<String> vistos,String o,String t){t=texto(t);if(!t.isBlank()&&vistos.add(o+"|"+t.toUpperCase()))out.add(new CapacidadEntrada(o,t,true));}
 private RecursoOperativo obtener(Long id){return recursos.findByEmpIdAndReoIdAndReoActTrue(contexto.empresaId(),id).orElseThrow(()->errorNoEncontrado("Recurso no encontrado."));}
 private void preparar(RecursoOperativo r){r.setEmpId(contexto.empresaId());r.setReoNom(texto(r.getReoNom()));r.setReoTip(texto(r.getReoTip()).toUpperCase());if(r.getReoOpe()==null)r.setReoOpe(true);r.setReoUsuMov(contexto.nombreUsuario());r.setReoFecMov(LocalDateTime.now());}
 private void movimiento(RecursoOperativo r,String tipo){preparar(r);r.setReoTipMov(tipo);if("B".equals(tipo))r.setReoCauMov("Baja del registro");else if(texto(r.getReoCauMov()).isBlank())r.setReoCauMov("A".equals(tipo)?"Alta del registro":"Modificación del registro");r.setReoAct(true);}
 private void validar(RecursoOperativo r){if(r.getReoNom().isBlank())throw error("El nombre es obligatorio.");if(!Set.of("EMPLEADO","MAQUINARIA").contains(r.getReoTip()))throw error("Tipo de recurso no permitido.");if("EMPLEADO".equals(r.getReoTip())){if(r.getPerId()==null||personas.findByEmpIdAndPerIdAndPerActTrue(contexto.empresaId(),r.getPerId()).isEmpty())throw error("Debe seleccionar una Persona activa.");}else r.setPerId(null);}
 private void comprobarActivo(RecursoOperativo r){if("B".equals(r.getReoTipMov()))throw conflicto("El recurso está dado de baja.");}
 private String texto(String s){return s==null?"":s.trim();} private ResponseStatusException error(String s){return new ResponseStatusException(HttpStatus.BAD_REQUEST,s);} private ResponseStatusException conflicto(String s){return new ResponseStatusException(HttpStatus.CONFLICT,s);} private ResponseStatusException errorNoEncontrado(String s){return new ResponseStatusException(HttpStatus.NOT_FOUND,s);}
 public record Estado(boolean activo){} public record CapacidadEntrada(String origen,String tipo,boolean activa){}
}
