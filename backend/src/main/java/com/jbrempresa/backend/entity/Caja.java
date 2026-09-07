package com.jbrempresa.backend.entity;
import java.time.LocalDateTime;import jakarta.persistence.*;
@Entity @Table(name="cajas") public class Caja{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)@Column(name="caj_id")private Long cajId;
 @Column(name="emp_id",nullable=false)private Long empId;@Column(name="caj_nom",nullable=false,length=100)private String cajNom;
 @Column(name="caj_des",length=500)private String cajDes;@Column(name="caj_act",nullable=false)private Boolean cajAct;
 @Column(name="caj_usu_mov",nullable=false,length=100)private String cajUsuMov;@Column(name="caj_fec_mov",nullable=false)private LocalDateTime cajFecMov;
 public Long getCajId(){return cajId;}public void setCajId(Long v){cajId=v;}public Long getEmpId(){return empId;}public void setEmpId(Long v){empId=v;}public String getCajNom(){return cajNom;}public void setCajNom(String v){cajNom=v;}public String getCajDes(){return cajDes;}public void setCajDes(String v){cajDes=v;}public Boolean getCajAct(){return cajAct;}public void setCajAct(Boolean v){cajAct=v;}public String getCajUsuMov(){return cajUsuMov;}public void setCajUsuMov(String v){cajUsuMov=v;}public LocalDateTime getCajFecMov(){return cajFecMov;}public void setCajFecMov(LocalDateTime v){cajFecMov=v;}
}
