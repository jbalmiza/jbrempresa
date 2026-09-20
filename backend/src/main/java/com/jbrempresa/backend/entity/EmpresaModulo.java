package com.jbrempresa.backend.entity;
import jakarta.persistence.*;
@Entity @Table(name="empresas_modulos",uniqueConstraints=@UniqueConstraint(columnNames={"emp_id","mod_id"}))
public class EmpresaModulo {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="emm_id") private Long id;
 @Column(name="emp_id",nullable=false) private Long empresaId; @Column(name="mod_id",nullable=false) private Long moduloId;
 @Column(name="emm_dis",nullable=false) private Boolean disponible; @Column(name="emm_pos",nullable=false) private Integer posicion;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getEmpresaId(){return empresaId;} public void setEmpresaId(Long v){empresaId=v;} public Long getModuloId(){return moduloId;} public void setModuloId(Long v){moduloId=v;} public Boolean getDisponible(){return disponible;} public void setDisponible(Boolean v){disponible=v;} public Integer getPosicion(){return posicion;} public void setPosicion(Integer v){posicion=v;}
}
