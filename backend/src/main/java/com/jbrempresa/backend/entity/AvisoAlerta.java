package com.jbrempresa.backend.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @IdClass(AvisoAlertaId.class) @Table(name="avisos_alertas")
public class AvisoAlerta {
 @Id @Column(name="avi_id") private Long aviId;
 @Id @Column(name="avi_id_his") private Long aviIdHis;
 @Id @Column(name="emp_id",nullable=false) private Long empId;
 @Column(name="avi_tip_mov",nullable=false,length=1) private String aviTipMov;
 @Column(name="avi_cau_mov",length=500) private String aviCauMov;
 @Column(name="avi_tipo",nullable=false,length=10) private String aviTipo;
 @Column(name="avi_titulo",nullable=false,length=150) private String aviTitulo;
 @Column(name="avi_mensaje",nullable=false,length=1000) private String aviMensaje;
 @Column(name="avi_emisor",length=20) private String aviEmisor;
 @Column(name="avi_emisor_emp_id") private Long aviEmisorEmpId;
 @Column(name="avi_emisor_usu_id") private Long aviEmisorUsuId;
 @Column(name="avi_destinatario",length=20) private String aviDestinatario;
 @Column(name="avi_dest_emp_id") private Long aviDestEmpId;
 @Column(name="avi_ubicacion",length=30) private String aviUbicacion;
 @Column(name="avi_ventana",length=160) private String aviVentana;
 @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") @Column(name="avi_fec_ini") private LocalDateTime aviFecIni;
 @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") @Column(name="avi_fec_fin") private LocalDateTime aviFecFin;
 @Column(name="avi_usu_mov",nullable=false,length=100) private String aviUsuMov;
 @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") @Column(name="avi_fec_mov",nullable=false) private LocalDateTime aviFecMov;
 @Column(name="avi_act",nullable=false) private Boolean aviAct;
 public Long getAviId(){return aviId;} public void setAviId(Long v){aviId=v;} public Long getAviIdHis(){return aviIdHis;} public void setAviIdHis(Long v){aviIdHis=v;}
 public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;} public String getAviTipMov(){return aviTipMov;} public void setAviTipMov(String v){aviTipMov=v;}
 public String getAviCauMov(){return aviCauMov;} public void setAviCauMov(String v){aviCauMov=v;} public String getAviTipo(){return aviTipo;} public void setAviTipo(String v){aviTipo=v;}
 public String getAviTitulo(){return aviTitulo;} public void setAviTitulo(String v){aviTitulo=v;} public String getAviMensaje(){return aviMensaje;} public void setAviMensaje(String v){aviMensaje=v;}
 public String getAviEmisor(){return aviEmisor;} public void setAviEmisor(String v){aviEmisor=v;}
 public Long getAviEmisorEmpId(){return aviEmisorEmpId;} public void setAviEmisorEmpId(Long v){aviEmisorEmpId=v;}
 public Long getAviEmisorUsuId(){return aviEmisorUsuId;} public void setAviEmisorUsuId(Long v){aviEmisorUsuId=v;}
 public String getAviDestinatario(){return aviDestinatario;} public void setAviDestinatario(String v){aviDestinatario=v;}
 public Long getAviDestEmpId(){return aviDestEmpId;} public void setAviDestEmpId(Long v){aviDestEmpId=v;}
 public String getAviUbicacion(){return aviUbicacion;} public void setAviUbicacion(String v){aviUbicacion=v;}
 public String getAviVentana(){return aviVentana;} public void setAviVentana(String v){aviVentana=v;}
 public LocalDateTime getAviFecIni(){return aviFecIni;} public void setAviFecIni(LocalDateTime v){aviFecIni=v;} public LocalDateTime getAviFecFin(){return aviFecFin;} public void setAviFecFin(LocalDateTime v){aviFecFin=v;}
 public String getAviUsuMov(){return aviUsuMov;} public void setAviUsuMov(String v){aviUsuMov=v;} public LocalDateTime getAviFecMov(){return aviFecMov;} public void setAviFecMov(LocalDateTime v){aviFecMov=v;}
 public Boolean getAviAct(){return aviAct;} public void setAviAct(Boolean v){aviAct=v;}
}
