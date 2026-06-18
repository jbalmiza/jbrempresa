
package com.jbrempresa.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidad JPA
@Entity

// Tabla perfiles
@Table(name = "perfiles")

// Clase Perfil
public class Perfil {

    // Constructor vacío
    public Perfil() {

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

    // Nombre
    @Column(name = "per_nom")
    private String perNom;

    // Tipo de perfil
    @Column(name = "per_tip_per")
    private String perTipPer;

    // Permiso administración
    @Column(name = "per_mod_adm")
    private String perModAdm;

    // Permiso terceros
    @Column(name = "per_mod_ter")
    private String perModTer;

    // Permiso perfiles
    @Column(name = "per_mod_per")
    private String perModPer;

    // Permiso productos
    @Column(name = "per_mod_pro")
    private String perModPro;

    // Permiso ventas
    @Column(name = "per_mod_ven")
    private String perModVen;

    // Activo
    @Column(name = "per_act")
    private String perAct;

    // Usuario de movimiento
    @Column(name = "usu_mov")
    private String usuMov;

    // Fecha de movimiento
    @Column(name = "fec_mov")
    private LocalDateTime fecMov;

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

    // Obtiene el nombre
    public String getPerNom() {
        return perNom;
    }

    // Modifica el nombre
    public void setPerNom(String perNom) {
        this.perNom = perNom;
    }

    // Obtiene el tipo de perfil
    public String getPerTipPer() {
        return perTipPer;
    }

    // Modifica el tipo de perfil
    public void setPerTipPer(String perTipPer) {
        this.perTipPer = perTipPer;
    }

    // Obtiene el permiso de administración
    public String getPerModAdm() {
        return perModAdm;
    }

    // Modifica el permiso de administración
    public void setPerModAdm(String perModAdm) {
        this.perModAdm = perModAdm;
    }

    // Obtiene el permiso de terceros
    public String getPerModTer() {
        return perModTer;
    }

    // Modifica el permiso de terceros
    public void setPerModTer(String perModTer) {
        this.perModTer = perModTer;
    }

    // Obtiene el permiso de perfiles
    public String getPerModPer() {
        return perModPer;
    }

    // Modifica el permiso de perfiles
    public void setPerModPer(String perModPer) {
        this.perModPer = perModPer;
    }

    // Obtiene el permiso de productos
    public String getPerModPro() {
        return perModPro;
    }

    // Modifica el permiso de productos
    public void setPerModPro(String perModPro) {
        this.perModPro = perModPro;
    }

    // Obtiene el permiso de ventas
    public String getPerModVen() {
        return perModVen;
    }

    // Modifica el permiso de ventas
    public void setPerModVen(String perModVen) {
        this.perModVen = perModVen;
    }

    // Obtiene el estado activo
    public String getPerAct() {
        return perAct;
    }

    // Modifica el estado activo
    public void setPerAct(String perAct) {
        this.perAct = perAct;
    }

    // Obtiene el usuario de movimiento
    public String getUsuMov() {
        return usuMov;
    }

    // Modifica el usuario de movimiento
    public void setUsuMov(String usuMov) {
        this.usuMov = usuMov;
    }

    // Obtiene la fecha de movimiento
    public LocalDateTime getFecMov() {
        return fecMov;
    }

    // Modifica la fecha de movimiento
    public void setFecMov(LocalDateTime fecMov) {
        this.fecMov = fecMov;
    }

}
