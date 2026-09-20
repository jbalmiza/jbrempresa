package com.jbrempresa.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="compras_detalle")
public class CompraDetalle {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="com_det_id", nullable=false) private Long comDetId;
    @Column(name="emp_id", nullable=false) private Long empId;
    @Column(name="com_id", nullable=false) private Long comId;
    @Column(name="pro_id") private Long proId;
    @Column(name="com_det_can", nullable=false) private Integer comDetCan;
    @Column(name="com_det_pre", nullable=false) private BigDecimal comDetPre;
    @Column(name="com_det_des", nullable=false) private BigDecimal comDetDes;
    @Column(name="com_det_iva", nullable=false) private BigDecimal comDetIva;
    @Column(name="com_det_imp", nullable=false) private BigDecimal comDetImp;
    @Column(name="com_det_usu_mov", nullable=false) private String comDetUsuMov;
    @Column(name="com_det_fec_mov", nullable=false) private LocalDateTime comDetFecMov;
    @Column(name="com_det_act", nullable=false) private Boolean comDetAct;
    @Column(name="ser_id") private Long serId;
    @Column(name="com_det_nom",length=200) private String nombre;
    @Column(name="com_det_obs",length=250) private String observaciones;
    public Long getSerId(){return serId;}public void setSerId(Long value){serId=value;}
    public String getNombre(){return nombre;}public void setNombre(String value){nombre=value;}
    public String getObservaciones(){return observaciones;}public void setObservaciones(String value){observaciones=value;}
    public Long getComDetId(){return comDetId;}
    public void setComDetId(Long value){comDetId=value;}
    public Long getEmpId(){return empId;}
    public void setEmpId(Long value){empId=value;}
    public Long getComId(){return comId;}
    public void setComId(Long value){comId=value;}
    public Long getProId(){return proId;}
    public void setProId(Long value){proId=value;}
    public Integer getComDetCan(){return comDetCan;}
    public void setComDetCan(Integer value){comDetCan=value;}
    public BigDecimal getComDetPre(){return comDetPre;}
    public void setComDetPre(BigDecimal value){comDetPre=value;}
    public BigDecimal getComDetDes(){return comDetDes;}
    public void setComDetDes(BigDecimal value){comDetDes=value;}
    public BigDecimal getComDetIva(){return comDetIva;}
    public void setComDetIva(BigDecimal value){comDetIva=value;}
    public BigDecimal getComDetImp(){return comDetImp;}
    public void setComDetImp(BigDecimal value){comDetImp=value;}
    public String getComDetUsuMov(){return comDetUsuMov;}
    public void setComDetUsuMov(String value){comDetUsuMov=value;}
    public LocalDateTime getComDetFecMov(){return comDetFecMov;}
    public void setComDetFecMov(LocalDateTime value){comDetFecMov=value;}
    public Boolean getComDetAct(){return comDetAct;}
    public void setComDetAct(Boolean value){comDetAct=value;}
}
