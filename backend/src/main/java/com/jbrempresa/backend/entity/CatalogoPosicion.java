package com.jbrempresa.backend.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity @Table(name="catalogo_posiciones", uniqueConstraints={@UniqueConstraint(name="uk_catalogo_posicion",columnNames={"emp_id","cap_fil","cap_col"}),@UniqueConstraint(name="uk_catalogo_token",columnNames="cap_token")})
public class CatalogoPosicion {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="cap_id") private Long capId;
 @Column(name="emp_id",nullable=false) private Long empId; @Column(name="cap_fil",nullable=false) private Integer capFil; @Column(name="cap_col",nullable=false) private Integer capCol;
 @Column(name="cap_ubi",nullable=false,length=100) private String capUbi; @Column(name="cap_token",nullable=false,length=64) private String capToken; @Column(name="cap_act",nullable=false) private Boolean capAct;
 @Column(name="cap_usu_mov",nullable=false,length=100) private String capUsuMov; @Column(name="cap_fec_mov",nullable=false) private LocalDateTime capFecMov;
 public Long getCapId(){return capId;}public void setCapId(Long v){capId=v;}public Long getEmpId(){return empId;}public void setEmpId(Long v){empId=v;}public Integer getCapFil(){return capFil;}public void setCapFil(Integer v){capFil=v;}public Integer getCapCol(){return capCol;}public void setCapCol(Integer v){capCol=v;}public String getCapUbi(){return capUbi;}public void setCapUbi(String v){capUbi=v;}public String getCapToken(){return capToken;}public void setCapToken(String v){capToken=v;}public Boolean getCapAct(){return capAct;}public void setCapAct(Boolean v){capAct=v;}public String getCapUsuMov(){return capUsuMov;}public void setCapUsuMov(String v){capUsuMov=v;}public LocalDateTime getCapFecMov(){return capFecMov;}public void setCapFecMov(LocalDateTime v){capFecMov=v;}
}
