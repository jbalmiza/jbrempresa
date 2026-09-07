package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name="configuraciones_tabla", uniqueConstraints=@UniqueConstraint(
        name="uk_configuraciones_tabla_usuario_clave", columnNames={"emp_id","usu_id","cot_cla"}))
public class ConfiguracionTabla {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="cot_id") private Long cotId;
    @Column(name="emp_id",nullable=false) private Long empId;
    @Column(name="usu_id",nullable=false) private Long usuId;
    @Column(name="cot_cla",nullable=false,length=500) private String cotCla;
    @Column(name="cot_con",nullable=false,columnDefinition="TEXT") private String cotCon;
    @Column(name="cot_usu_mov",nullable=false,length=50) private String cotUsuMov;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") @Column(name="cot_fec_mov",nullable=false) private LocalDateTime cotFecMov;
    @Column(name="cot_act",nullable=false) private Boolean cotAct;
    public Long getCotId(){return cotId;} public void setCotId(Long v){cotId=v;}
    public Long getEmpId(){return empId;} public void setEmpId(Long v){empId=v;}
    public Long getUsuId(){return usuId;} public void setUsuId(Long v){usuId=v;}
    public String getCotCla(){return cotCla;} public void setCotCla(String v){cotCla=v;}
    public String getCotCon(){return cotCon;} public void setCotCon(String v){cotCon=v;}
    public String getCotUsuMov(){return cotUsuMov;} public void setCotUsuMov(String v){cotUsuMov=v;}
    public LocalDateTime getCotFecMov(){return cotFecMov;} public void setCotFecMov(LocalDateTime v){cotFecMov=v;}
    public Boolean getCotAct(){return cotAct;} public void setCotAct(Boolean v){cotAct=v;}
}
