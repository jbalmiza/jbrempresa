package com.jbrempresa.backend.entity;
import java.time.LocalDateTime; import com.fasterxml.jackson.annotation.JsonFormat; import jakarta.persistence.*;
@Entity @Table(name="paises")
public class Pais {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="pai_id") private Long paiId;
 @Column(name="emp_id",nullable=false) private Long empId; @Column(name="pai_cod",nullable=false,length=10) private String paiCod; @Column(name="pai_nom",nullable=false,length=100) private String paiNom;
 @Column(name="pai_usu_mov",nullable=false,length=50) private String paiUsuMov; @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") @Column(name="pai_fec_mov",nullable=false) private LocalDateTime paiFecMov; @Column(name="pai_act",nullable=false) private Boolean paiAct;
 public Long getPaiId(){return paiId;} public void setPaiId(Long v){paiId=v;} public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;} public String getPaiCod(){return paiCod;} public void setPaiCod(String v){paiCod=v;} public String getPaiNom(){return paiNom;} public void setPaiNom(String v){paiNom=v;} public String getPaiUsuMov(){return paiUsuMov;} public void setPaiUsuMov(String v){paiUsuMov=v;} public LocalDateTime getPaiFecMov(){return paiFecMov;} public void setPaiFecMov(LocalDateTime v){paiFecMov=v;} public Boolean getPaiAct(){return paiAct;} public void setPaiAct(Boolean v){paiAct=v;}
}
