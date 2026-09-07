package com.jbrempresa.backend.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @IdClass(ServicioId.class) @Table(name="servicios")
public class Servicio {
 @Id @Column(name="ser_id") private Long serId;
 @Id @Column(name="ser_id_his") private Long serIdHis;
 @Column(name="emp_id") private Long empId;
 @Column(name="ser_tip_mov") private String serTipMov;
 @Column(name="ser_cau_mov", length=500) private String serCauMov;
 @Column(name="ser_tip_ser") private String serTipSer;
 @Column(name="ser_nom") private String serNom;
 @Column(name="ser_des") private String serDes;
 @Column(name="ser_cat") private String serCat;
 @Column(name="ser_sub_cat") private String serSubCat;
 @Column(name="ser_dur_min") private Integer serDurMin;
 @Column(name="ser_pre_ven") private BigDecimal serPreVen;
 @Column(name="ser_pre_des") private BigDecimal serPreDes;
 @Column(name="ser_pre_iva") private BigDecimal serPreIva;
 @Column(name="ser_pre_fin") private BigDecimal serPreFin;
 @Column(name="ser_obs") private String serObs;
 @Column(name="ser_vis_cat") private Boolean serVisCat = true;
 @Column(name="ser_ima", length=255) private String serIma;
 @Column(name="ser_usu_mov") private String serUsuMov;
 @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") @Column(name="ser_fec_mov") private LocalDateTime serFecMov;
 @Column(name="ser_act") private Boolean serAct;
 public Long getSerId(){return serId;} public void setSerId(Long v){serId=v;}
 public Long getSerIdHis(){return serIdHis;} public void setSerIdHis(Long v){serIdHis=v;}
 public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
 public String getSerTipMov(){return serTipMov;} public void setSerTipMov(String v){serTipMov=v;}
 public String getSerCauMov(){return serCauMov;} public void setSerCauMov(String v){serCauMov=v;}
 public String getSerTipSer(){return serTipSer;} public void setSerTipSer(String v){serTipSer=v;}
 public String getSerNom(){return serNom;} public void setSerNom(String v){serNom=v;}
 public String getSerDes(){return serDes;} public void setSerDes(String v){serDes=v;}
 public String getSerCat(){return serCat;} public void setSerCat(String v){serCat=v;}
 public String getSerSubCat(){return serSubCat;} public void setSerSubCat(String v){serSubCat=v;}
 public Integer getSerDurMin(){return serDurMin;} public void setSerDurMin(Integer v){serDurMin=v;}
 public BigDecimal getSerPreVen(){return serPreVen;} public void setSerPreVen(BigDecimal v){serPreVen=v;}
 public BigDecimal getSerPreDes(){return serPreDes;} public void setSerPreDes(BigDecimal v){serPreDes=v;}
 public BigDecimal getSerPreIva(){return serPreIva;} public void setSerPreIva(BigDecimal v){serPreIva=v;}
 public BigDecimal getSerPreFin(){return serPreFin;} public void setSerPreFin(BigDecimal v){serPreFin=v;}
 public String getSerObs(){return serObs;} public void setSerObs(String v){serObs=v;}
 public Boolean getSerVisCat(){return serVisCat;} public void setSerVisCat(Boolean v){serVisCat=v;}
 public String getSerIma(){return serIma;} public void setSerIma(String v){serIma=v;}
 public String getSerUsuMov(){return serUsuMov;} public void setSerUsuMov(String v){serUsuMov=v;}
 public LocalDateTime getSerFecMov(){return serFecMov;} public void setSerFecMov(LocalDateTime v){serFecMov=v;}
 public Boolean getSerAct(){return serAct;} public void setSerAct(Boolean v){serAct=v;}
}
