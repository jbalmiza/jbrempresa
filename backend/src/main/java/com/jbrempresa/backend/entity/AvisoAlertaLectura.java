package com.jbrempresa.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="avisos_alertas_lecturas", uniqueConstraints=@UniqueConstraint(columnNames={"emp_id","avi_id","avi_id_his","usu_id"}))
public class AvisoAlertaLectura {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="emp_id",nullable=false) private Long empId;
    @Column(name="avi_id",nullable=false) private Long aviId;
    @Column(name="avi_id_his",nullable=false) private Long aviIdHis;
    @Column(name="usu_id",nullable=false) private Long usuarioId;
    @Column(name="fecha_lectura",nullable=false) private LocalDateTime fechaLectura;
    public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
    public Long getAviId(){return aviId;} public void setAviId(Long v){aviId=v;}
    public Long getAviIdHis(){return aviIdHis;} public void setAviIdHis(Long v){aviIdHis=v;}
    public Long getUsuarioId(){return usuarioId;} public void setUsuarioId(Long v){usuarioId=v;}
    public LocalDateTime getFechaLectura(){return fechaLectura;} public void setFechaLectura(LocalDateTime v){fechaLectura=v;}
}
