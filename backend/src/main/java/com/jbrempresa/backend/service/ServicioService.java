package com.jbrempresa.backend.service;
import com.jbrempresa.backend.entity.Servicio;
import com.jbrempresa.backend.repository.ServicioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class ServicioService {
 private final ServicioRepository repository;
 public ServicioService(ServicioRepository repository){this.repository=repository;}
 public Servicio obtener(Long empId,Long id){return vigente(empId,id);}
 @Transactional public Servicio guardar(Long cli,String usuario,Servicio s){s.setEmpId(cli);s.setSerId(repository.obtenerSiguienteId(cli));s.setSerIdHis(1L);if(s.getSerVisCat()==null)s.setSerVisCat(true);if(s.getSerIma()==null||s.getSerIma().isBlank())s.setSerIma("servicio-predeterminado.png");movimiento(s,"A",usuario);return repository.save(s);}
 @Transactional public Servicio actualizar(Long cli,String usuario,Servicio s){Servicio anterior=vigente(cli,s.getSerId());comprobar(anterior);anterior.setSerAct(false);repository.saveAndFlush(anterior);s.setEmpId(cli);s.setSerIdHis(anterior.getSerIdHis()+1);movimiento(s,"M",usuario);return repository.save(s);}
 public List<Servicio> consultar(Long cli){return repository.findByEmpIdAndSerActTrueOrderBySerId(cli);}
 public Long siguienteId(Long cli){return repository.obtenerSiguienteId(cli);}
 public List<Servicio> historico(Long cli,Long id){vigente(cli,id);return repository.findByEmpIdAndSerIdOrderBySerFecMovDesc(cli,id);}
 @Transactional public void eliminar(Long cli,Long id){vigente(cli,id);repository.deleteAll(repository.findByEmpIdAndSerIdOrderBySerFecMovDesc(cli,id));}
 @Transactional public Servicio baja(Long cli,Long id,String usuario){Servicio anterior=vigente(cli,id);comprobar(anterior);anterior.setSerAct(false);repository.saveAndFlush(anterior);Servicio baja=new Servicio();BeanUtils.copyProperties(anterior,baja);baja.setSerIdHis(anterior.getSerIdHis()+1);movimiento(baja,"B",usuario);return repository.save(baja);}
 @Transactional public Servicio deshacer(Long cli,Long id){Servicio actual=vigente(cli,id);if(actual.getSerIdHis()<=1)conflicto("No existen movimientos anteriores para deshacer.");Servicio anterior=repository.findByEmpIdAndSerIdAndSerIdHis(cli,id,actual.getSerIdHis()-1).orElseThrow();repository.delete(actual);repository.flush();anterior.setSerAct(true);return repository.save(anterior);}
 @Transactional public Servicio asociarImagen(Long empId,Long id,String ruta){Servicio actual=vigente(empId,id);actual.setSerIma(ruta);return repository.save(actual);}
 private void movimiento(Servicio s,String tipo,String usuario){s.setSerTipMov(tipo);if("B".equals(tipo))s.setSerCauMov("Baja del registro");else if(s.getSerCauMov()==null||s.getSerCauMov().isBlank())s.setSerCauMov("A".equals(tipo)?"Alta del registro":"Modificación del registro");s.setSerAct(true);s.setSerUsuMov(usuario);s.setSerFecMov(LocalDateTime.now());}
 private Servicio vigente(Long cli,Long id){return repository.findByEmpIdAndSerIdAndSerActTrue(cli,id).orElseThrow(()->new RuntimeException("Servicio no encontrado."));}
 private void comprobar(Servicio s){if("B".equals(s.getSerTipMov()))conflicto("El servicio está dado de baja.");}
 private void conflicto(String m){throw new ResponseStatusException(HttpStatus.CONFLICT,m);}
}
