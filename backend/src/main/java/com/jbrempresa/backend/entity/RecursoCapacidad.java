package com.jbrempresa.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name="recursos_capacidades", uniqueConstraints=@UniqueConstraint(name="uk_recurso_capacidad",columnNames={"emp_id","reo_id","rec_ori","rec_tip"}))
public class RecursoCapacidad {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="rec_id") private Long recId;
 @Column(name="emp_id",nullable=false) private Long empId;
 @Column(name="reo_id",nullable=false) private Long reoId;
 @Column(name="rec_ori",nullable=false,length=20) private String recOri;
 @Column(name="rec_tip",nullable=false,length=100) private String recTip;
 @Column(name="rec_act",nullable=false) private Boolean recAct=true;
 public Long getRecId(){return recId;} public void setRecId(Long v){recId=v;}
 public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
 public Long getReoId(){return reoId;} public void setReoId(Long v){reoId=v;}
 public String getRecOri(){return recOri;} public void setRecOri(String v){recOri=v;}
 public String getRecTip(){return recTip;} public void setRecTip(String v){recTip=v;}
 public Boolean getRecAct(){return recAct;} public void setRecAct(Boolean v){recAct=v;}
}
