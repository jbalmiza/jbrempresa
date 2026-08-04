
package com.jbrempresa.backend.entity;

//Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

// Entidad JPA
@Entity

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "per_id")
    private Long perId;

    // Cliente
    @Column(name = "cli_id")
    private Long cliId;

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

    // Obtiene el cliente
    public Long getCliId() {
        return cliId;
    }

    // Modifica el cliente
    public void setCliId(Long cliId) {
        this.cliId = cliId;
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
