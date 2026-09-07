package com.jbrempresa.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="res_id") private Long resId;
 @Column(name="emp_id",nullable=false) private Long empId;
 @Column(name="per_id") private Long perId;
 @Column(name="dov_id") private Long dovId;
 @Column(name="com_id") private Long comId;
 @Column(name="res_ini",nullable=false) private LocalDateTime resIni;
 @Column(name="res_fin",nullable=false) private LocalDateTime resFin;
 @Column(name="res_est",nullable=false,length=20) private String resEst;
 @Column(name="res_dur_min",nullable=false) private Integer resDurMin;
 @Column(name="res_tit",nullable=false,length=200) private String resTit;
 @Column(name="res_obs",length=500) private String resObs;
 @Column(name="res_usu_mov",nullable=false,length=100) private String resUsuMov;
 @Column(name="res_fec_mov",nullable=false) private LocalDateTime resFecMov;
 @Column(name="res_act",nullable=false) private Boolean resAct;
 public Long getResId(){return resId;} public void setResId(Long v){resId=v;}
 public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
 public Long getPerId(){return perId;} public void setPerId(Long v){perId=v;}
 public Long getDovId(){return dovId;} public void setDovId(Long v){dovId=v;}
 public Long getComId(){return comId;} public void setComId(Long v){comId=v;}
 public LocalDateTime getResIni(){return resIni;} public void setResIni(LocalDateTime v){resIni=v;}
 public LocalDateTime getResFin(){return resFin;} public void setResFin(LocalDateTime v){resFin=v;}
 public String getResEst(){return resEst;} public void setResEst(String v){resEst=v;}
 public Integer getResDurMin(){return resDurMin;} public void setResDurMin(Integer v){resDurMin=v;}
 public String getResTit(){return resTit;} public void setResTit(String v){resTit=v;}
 public String getResObs(){return resObs;} public void setResObs(String v){resObs=v;}
 public String getResUsuMov(){return resUsuMov;} public void setResUsuMov(String v){resUsuMov=v;}
 public LocalDateTime getResFecMov(){return resFecMov;} public void setResFecMov(LocalDateTime v){resFecMov=v;}
 public Boolean getResAct(){return resAct;} public void setResAct(Boolean v){resAct=v;}
}
