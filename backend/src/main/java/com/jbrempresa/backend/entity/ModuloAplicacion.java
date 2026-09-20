package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity @Table(name="modulos_aplicacion")
public class ModuloAplicacion {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="mod_id") private Long id;
 @Column(name="mod_cod",nullable=false,unique=true,length=50) private String codigo;
 @Column(name="mod_tit",nullable=false,length=100) private String titulo;
 @Column(name="mod_des",nullable=false,length=250) private String descripcion;
 @Column(name="mod_rut",nullable=false,unique=true,length=100) private String ruta;
 @Column(name="mod_pos",nullable=false) private Integer posicion;
 @Column(name="mod_usu_mov",nullable=false,length=100) private String usuario;
 @Column(name="mod_fec_mov",nullable=false) private LocalDateTime fecha;
 @Column(name="mod_act",nullable=false) private Boolean activo;
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getCodigo(){return codigo;} public void setCodigo(String v){codigo=v;} public String getTitulo(){return titulo;} public void setTitulo(String v){titulo=v;} public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;} public String getRuta(){return ruta;} public void setRuta(String v){ruta=v;} public Integer getPosicion(){return posicion;} public void setPosicion(Integer v){posicion=v;} public String getUsuario(){return usuario;} public void setUsuario(String v){usuario=v;} public LocalDateTime getFecha(){return fecha;} public void setFecha(LocalDateTime v){fecha=v;} public Boolean getActivo(){return activo;} public void setActivo(Boolean v){activo=v;}
}
