package com.jbrempresa.backend.service;
import java.time.LocalDateTime; import java.util.List;
import org.springframework.beans.BeanUtils; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import com.jbrempresa.backend.entity.AvisoAlerta; import com.jbrempresa.backend.repository.AvisoAlertaRepository;
@Service
public class AvisoAlertaService {
 private final AvisoAlertaRepository repo; private final AvisoAlertaDistribucionService distribucion;
 public AvisoAlertaService(AvisoAlertaRepository r,AvisoAlertaDistribucionService d){repo=r;distribucion=d;}
 public List<AvisoAlerta> consultar(Long empId){return distribucion.filtrarConsulta(repo.findByEmpIdAndAviActTrueOrderByAviId(empId));}
 public List<AvisoAlerta> consultarGlobal(){return repo.findByAviActTrueOrderByEmpIdAscAviIdAsc();}
 public List<AvisoAlerta> visibles(Long empId,String ubicacion){return repo.visibles(empId,ubicacion,LocalDateTime.now());}
 public Long siguienteId(Long empId){return repo.siguienteId(empId);}
 public List<AvisoAlerta> historico(Long empId,Long id){distribucion.exigirGestion(vigente(empId,id));return repo.findByEmpIdAndAviIdOrderByAviFecMovDesc(empId,id);}
 @Transactional public AvisoAlerta crear(Long empId,String usuario,AvisoAlerta a){validar(a);distribucion.preparar(a,empId,null);a.setEmpId(empId);a.setAviId(repo.siguienteId(empId));a.setAviIdHis(1L);movimiento(a,"A","Alta del registro",usuario);return repo.save(a);}
 @Transactional public AvisoAlerta actualizar(Long empId,Long id,String usuario,AvisoAlerta a){validar(a);AvisoAlerta anterior=vigente(empId,id);if("B".equals(anterior.getAviTipMov()))throw new IllegalArgumentException("El aviso o alerta está dado de baja.");distribucion.preparar(a,empId,anterior);anterior.setAviAct(false);repo.saveAndFlush(anterior);a.setEmpId(empId);a.setAviId(id);a.setAviIdHis(anterior.getAviIdHis()+1);movimiento(a,"M","Modificación del registro",usuario);return repo.save(a);}
 @Transactional public AvisoAlerta baja(Long empId,Long id,String usuario){AvisoAlerta anterior=vigente(empId,id);distribucion.exigirGestion(anterior);if("B".equals(anterior.getAviTipMov()))throw new IllegalArgumentException("El aviso o alerta ya está dado de baja.");return version(anterior,"B","Baja del registro",usuario);}
 @Transactional public AvisoAlerta reactivar(Long empId,Long id,String usuario){AvisoAlerta anterior=vigente(empId,id);distribucion.exigirGestion(anterior);if(!"B".equals(anterior.getAviTipMov()))throw new IllegalArgumentException("El aviso o alerta no está dado de baja.");return version(anterior,"R","Reactivación del registro",usuario);}
 @Transactional public void eliminar(Long empId,Long id){AvisoAlerta a=vigente(empId,id);distribucion.exigirGestion(a);distribucion.eliminarLecturas(empId,id);repo.deleteAll(repo.findByEmpIdAndAviIdOrderByAviFecMovDesc(empId,id));}
 private AvisoAlerta version(AvisoAlerta anterior,String tipo,String causa,String usuario){anterior.setAviAct(false);repo.saveAndFlush(anterior);AvisoAlerta nueva=new AvisoAlerta();BeanUtils.copyProperties(anterior,nueva);nueva.setAviIdHis(anterior.getAviIdHis()+1);movimiento(nueva,tipo,causa,usuario);return repo.save(nueva);}
 private void movimiento(AvisoAlerta a,String tipo,String causa,String usuario){a.setAviTipMov(tipo);a.setAviCauMov(causa);a.setAviUsuMov(usuario);a.setAviFecMov(LocalDateTime.now());a.setAviAct(true);}
 private AvisoAlerta vigente(Long empId,Long id){return repo.findByEmpIdAndAviIdAndAviActTrue(empId,id).orElseThrow(()->new IllegalArgumentException("Aviso o alerta no encontrado."));}
 private void validar(AvisoAlerta a){if(a.getAviTipo()==null||!(a.getAviTipo().equals("AVISO")||a.getAviTipo().equals("ALERTA")))throw new IllegalArgumentException("El tipo debe ser AVISO o ALERTA.");if(a.getAviTitulo()==null||a.getAviTitulo().isBlank()||a.getAviMensaje()==null||a.getAviMensaje().isBlank())throw new IllegalArgumentException("Debe informar título y mensaje.");if(a.getAviFecIni()!=null&&a.getAviFecFin()!=null&&a.getAviFecFin().isBefore(a.getAviFecIni()))throw new IllegalArgumentException("La fecha final no puede ser anterior a la inicial.");}
}
