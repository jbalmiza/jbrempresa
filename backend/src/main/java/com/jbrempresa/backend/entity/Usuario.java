
package com.jbrempresa.backend.entity;

//Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

// Entidad JPA
@Entity

// Tabla usuarios
@Table(name = "usuarios")

// Clase Usuario
public class Usuario {

    // Constructor vacío
    public Usuario() {

    }

    // Clave primaria
    @Id

    // Genera automáticamente el ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "usu_id")
    private Long usuId;

    // Empresa
    @Column(name = "emp_id")
    private Long empId;

    // Usuario
    @Column(name = "usu_usu", unique = true)
    private String usuUsu;

    // Contraseña
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "usu_con")
    private String usuCon;

    // ID del perfil
    @Column(name = "per_id")
    private Long perId;

    @Column(name = "usu_per_id", nullable = false)
    private Long usuPerId;

    // Nombre
    @Column(name = "usu_nom")
    private String usuNom;

    @Column(name = "usu_tel")
    private String usuTel;

    // Email
    @Column(name = "usu_ema")
    private String usuEma;

    // Activo
    @Column(name = "usu_act")
    private String usuAct;

    // Usuario de movimiento
    @Column(name = "usu_usu_mov")
    private String usuUsuMov;

    // Fecha de movimiento
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "usu_fec_mov")
    private LocalDateTime usuFecMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getUsuId() {
        return usuId;
    }

    // Modifica el ID
    public void setUsuId(Long usuId) {
        this.usuId = usuId;
    }

    // Obtiene el empresa
    public Long getEmpId() {
        return empId;
    }

    // Modifica el empresa
    public void setEmpId(Long EmpId) {
        this.empId = EmpId;
    }

    // Obtiene el usuario
    public String getUsuUsu() {
        return usuUsu;
    }

    // Modifica el usuario
    public void setUsuUsu(String usuUsu) {
        this.usuUsu = usuUsu;
    }

    // Obtiene la contraseña
    public String getUsuCon() {
        return usuCon;
    }

    // Modifica la contraseña
    public void setUsuCon(String usuCon) {
        this.usuCon = usuCon;
    }

    // Obtiene el ID del perfil
    public Long getPerId() {
        return perId;
    }

    // Modifica el ID del perfil
    public void setPerId(Long perId) {
        this.perId = perId;
    }

    public Long getUsuPerId() { return usuPerId; }
    public void setUsuPerId(Long usuPerId) { this.usuPerId = usuPerId; }

    // Obtiene el nombre
    public String getUsuNom() {
        return usuNom;
    }

    // Modifica el nombre
    public void setUsu_nom(String usuNom) {
        this.usuNom = usuNom;
    }

    public String getUsuTel() { return usuTel; }
    public void setUsuTel(String usuTel) { this.usuTel = usuTel; }

    // Obtiene el email
    public String getUsuEma() {
        return usuEma;
    }

    // Modifica el email
    public void setUsuEma(String usuEma) {
        this.usuEma = usuEma;
    }

    // Obtiene el estado activo
    public String getUsuAct() {
        return usuAct;
    }

    // Modifica el estado activo
    public void setUsu_act(String usuAct) {
        this.usuAct = usuAct;
    }

    // Obtiene el usuario de movimiento
    public String getUsuUsuMov() {
        return usuUsuMov;
    }

    // Modifica el usuario de movimiento
    public void setUsuUsuMov(String usuUsuMov) {
        this.usuUsuMov = usuUsuMov;
    }

    // Obtiene la fecha de movimiento
    public LocalDateTime getUsuFecMov() {
        return usuFecMov;
    }

    // Modifica la fecha de movimiento
    public void setUsuFecMov(LocalDateTime usuFecMov) {
        this.usuFecMov = usuFecMov;
    }

}
