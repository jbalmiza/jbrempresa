package com.jbrempresa.backend.entity;
import java.time.LocalDateTime; import jakarta.persistence.*;
@Entity @Table(name="empresas_relaciones_movimientos") public class EmpresaRelacionMovimiento{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="erm_id") private Long id;
 @Column(name="emp_id",nullable=false) private Long empresaId; @Column(name="ere_id",nullable=false) private Long relacionId;
 @Column(name="erm_tip",nullable=false,length=1) private String tipo; @Column(name="erm_cau",nullable=false,length=500) private String causa;
 @Column(name="erm_usu",nullable=false,length=100) private String usuario; @Column(name="erm_fec",nullable=false) private LocalDateTime fecha; @Column(name="erm_act",nullable=false) private Boolean activo;
 public Long getId(){return id;} public Long getEmpresaId(){return empresaId;} public void setEmpresaId(Long v){empresaId=v;} public Long getRelacionId(){return relacionId;} public void setRelacionId(Long v){relacionId=v;} public String getTipo(){return tipo;} public void setTipo(String v){tipo=v;} public String getCausa(){return causa;} public void setCausa(String v){causa=v;} public String getUsuario(){return usuario;} public void setUsuario(String v){usuario=v;} public LocalDateTime getFecha(){return fecha;} public void setFecha(LocalDateTime v){fecha=v;} public Boolean getActivo(){return activo;} public void setActivo(Boolean v){activo=v;}
}
