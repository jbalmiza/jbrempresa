package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity @IdClass(RecursoOperativoId.class)
@Table(name="recursos_operativos")
public class RecursoOperativo {
 @Id @Column(name="reo_id") private Long reoId;
 @Id @Column(name="reo_id_his") private Long reoIdHis;
 @Column(name="emp_id",nullable=false) private Long empId;
 @Column(name="reo_nom",nullable=false,length=100) private String reoNom;
 @Column(name="reo_tip",nullable=false,length=20) private String reoTip;
 @Column(name="per_id") private Long perId;
 @Column(name="reo_des",length=500) private String reoDes;
 @Column(name="reo_tip_mov",nullable=false,length=1) private String reoTipMov;
 @Column(name="reo_cau_mov",length=500) private String reoCauMov;
 @Column(name="reo_ope",nullable=false) private Boolean reoOpe=true;
 @Column(name="reo_act",nullable=false) private Boolean reoAct=true;
 @Column(name="reo_usu_mov",length=100) private String reoUsuMov;
 @Column(name="reo_fec_mov") private LocalDateTime reoFecMov;
 public Long getReoId(){return reoId;} public void setReoId(Long v){reoId=v;}
 public Long getReoIdHis(){return reoIdHis;} public void setReoIdHis(Long v){reoIdHis=v;}
 public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
 public String getReoNom(){return reoNom;} public void setReoNom(String v){reoNom=v;}
 public String getReoTip(){return reoTip;} public void setReoTip(String v){reoTip=v;}
 public Long getPerId(){return perId;} public void setPerId(Long v){perId=v;}
 public String getReoDes(){return reoDes;} public void setReoDes(String v){reoDes=v;}
 public String getReoTipMov(){return reoTipMov;} public void setReoTipMov(String v){reoTipMov=v;}
 public String getReoCauMov(){return reoCauMov;} public void setReoCauMov(String v){reoCauMov=v;}
 public Boolean getReoOpe(){return reoOpe;} public void setReoOpe(Boolean v){reoOpe=v;}
 public Boolean getReoAct(){return reoAct;} public void setReoAct(Boolean v){reoAct=v;}
 public String getReoUsuMov(){return reoUsuMov;} public void setReoUsuMov(String v){reoUsuMov=v;}
 public LocalDateTime getReoFecMov(){return reoFecMov;} public void setReoFecMov(LocalDateTime v){reoFecMov=v;}
}
