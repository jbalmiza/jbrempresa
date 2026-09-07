
package com.jbrempresa.backend.entity;

//Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

// Entidad JPA
@Entity
@IdClass(PersonaId.class)

// Tabla personas
@Table(name = "personas")

// Clase Persona
public class Persona {

    // Constructor vacío
    public Persona() {

    }

    // Clave primaria
    @Id

    // Genera automáticamente el ID
    @Column(name = "per_id")
    private Long perId;

    @Id
    @Column(name = "per_id_his")
    private Long perIdHis;

    @Column(name = "per_tip_mov")
    private String perTipMov;

    @Column(name = "per_cau_mov", length = 500)
    private String perCauMov;

    // Empresa
    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "per_tip_per")
    private String perTipPer;


    @Column(name = "per_raz_soc_cor")
    private String perRazSocCor;

    @Column(name = "per_raz_soc_lar")
    private String perRazSocLar;

    // Tipo de documento
    @Column(name = "per_tip_doc")
    private String perTipDoc;

    // Documento
    @Column(name = "per_doc")
    private String perDoc;
    
    // Documento
    @Column(name = "per_nom_com")
    private String perNomCom;

    // Nombre
    @Column(name = "per_nom")
    private String perNom;

    // Primer apellido
    @Column(name = "per_ape1")
    private String perApe1;

    // Segundo apellido
    @Column(name = "per_ape2")
    private String perApe2;

    // Fecha de nacimiento
    @Column(name = "per_fec_nac")
    private LocalDate perFecNac;

    // Teléfono
    @Column(name = "per_tel")
    private String perTel;

    // Email
    @Column(name = "per_ema")
    private String perEma;

    // ID del domicilio
    @Column(name = "dom_id")
    private Long domId;

    @Column(name = "per_cox")
    private Double perCoX;

    @Column(name = "per_coy")
    private Double perCoY;

    @Column(name = "per_hus")
    private Long perHus;

    // Activo
    @Column(name = "per_act")
    private Boolean perAct;

    // Usuario de movimiento
    @Column(name = "per_usu_mov")
    private String perUsuMov;

    // Fecha de movimiento
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "per_fec_mov")
    private LocalDateTime perFecMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getPerId() {
        return perId;
    }

    // Modifica el ID
    public void setPerId(Long perId) {
        this.perId = perId;
    }

    public Long getPerIdHis() { return perIdHis; }
    public void setPerIdHis(Long perIdHis) { this.perIdHis = perIdHis; }
    public String getPerTipMov() { return perTipMov; }
    public void setPerTipMov(String perTipMov) { this.perTipMov = perTipMov; }
    public String getPerCauMov() { return perCauMov; }
    public void setPerCauMov(String perCauMov) { this.perCauMov = perCauMov; }
    public String getPerTipPer() { return perTipPer; }
    public void setPerTipPer(String perTipPer) { this.perTipPer = perTipPer; }
    public String getPerRazSocCor() { return perRazSocCor; }
    public void setPerRazSocCor(String perRazSocCor) { this.perRazSocCor = perRazSocCor; }
    public String getPerRazSocLar() { return perRazSocLar; }
    public void setPerRazSocLar(String perRazSocLar) { this.perRazSocLar = perRazSocLar; }

    // Obtiene el empresa
    public Long getEmpId() {
        return empId;
    }

    // Modifica el empresa
    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    // Obtiene el tipo de documento
    public String getPerTipDoc() {
        return perTipDoc;
    }

    // Modifica el tipo de documento
    public void setPerTipDoc(String perTipDoc) {
        this.perTipDoc = perTipDoc;
    }

    // Obtiene el documento
    public String getPerDoc() {
        return perDoc;
    }

    // Modifica el documento
    public void setPerDoc(String perDoc) {
        this.perDoc = perDoc;
    }

    // Obtiene el nombre completo
    public String getPerNomCom() {
        return perNomCom;
    }

    // Modifica el nombre completo
    public void setPerNomCom(String perNomCom) {
        this.perNomCom = perNomCom;
    }
    
    
    // Obtiene el nombre
    public String getPerNom() {
        return perNom;
    }

    // Modifica el nombre
    public void setPerNom(String perNom) {
        this.perNom = perNom;
    }

    // Obtiene el primer apellido
    public String getPerApe1() {
        return perApe1;
    }

    // Modifica el primer apellido
    public void setPerApe1(String perApe1) {
        this.perApe1 = perApe1;
    }

    // Obtiene el segundo apellido
    public String getPerApe2() {
        return perApe2;
    }

    // Modifica el segundo apellido
    public void setPerApe2(String perApe2) {
        this.perApe2 = perApe2;
    }

    // Obtiene la fecha de nacimiento
    public LocalDate getPerFecNac() {
        return perFecNac;
    }

    // Modifica la fecha de nacimiento
    public void setPerFecNac(LocalDate perFecNac) {
        this.perFecNac = perFecNac;
    }

    // Obtiene el teléfono
    public String getPerTel() {
        return perTel;
    }

    // Modifica el teléfono
    public void setPerTel(String perTel) {
        this.perTel = perTel;
    }

    // Obtiene el email
    public String getPerEma() {
        return perEma;
    }

    // Modifica el email
    public void setPerEma(String perEma) {
        this.perEma = perEma;
    }

    // Obtiene el ID del domicilio
    public Long getDomId() {
        return domId;
    }

    // Modifica el ID del domicilio
    public void setDomId(Long domId) {
        this.domId = domId;
    }

    public Double getPerCoX() { return perCoX; }
    public void setPerCoX(Double perCoX) { this.perCoX = perCoX; }
    public Double getPerCoY() { return perCoY; }
    public void setPerCoY(Double perCoY) { this.perCoY = perCoY; }
    public Long getPerHus() { return perHus; }
    public void setPerHus(Long perHus) { this.perHus = perHus; }

    // Obtiene el estado activo
    public Boolean getPerAct() {
        return perAct;
    }

    // Modifica el estado activo
    public void setPerAct(Boolean perAct) {
        this.perAct = perAct;
    }

    // Obtiene el usuario de movimiento
    public String getPerUsuMov() {
        return perUsuMov;
    }

    // Modifica el usuario de movimiento
    public void setPerUsuMov(String perUsuMov) {
        this.perUsuMov = perUsuMov;
    }

    // Obtiene la fecha de movimiento
    public LocalDateTime getPerFecMov() {
        return perFecMov;
    }

    // Modifica la fecha de movimiento
    public void setPerFecMov(LocalDateTime perFecMov) {
        this.perFecMov = perFecMov;
    }

}
