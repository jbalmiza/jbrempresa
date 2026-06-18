
// Define el paquete donde está ubicada esta entidad Java.
package com.jbrempresa.backend.entity;

// Importa las anotaciones JPA.
import jakarta.persistence.*;

import java.time.LocalDateTime;

// Entidad JPA
@Entity

// Tabla productos
@Table(name = "productos")

// Clase Producto
public class Producto {

    // Constructor vacío
    public Producto() {

    }

    // Clave primaria
    @Id

    // Genera automáticamente el ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "pro_id")
    private Long proId;

    // Cliente
    @Column(name = "cli_id")
    private Long cliId;

    // Tipo de producto
    @Column(name = "pro_tip_pro")
    private String proTipPro;

    // Nombre
    @Column(name = "pro_nom")
    private String proNom;

    // Activo
    @Column(name = "pro_act")
    private Boolean proAct;

    // Usuario de movimiento
    @Column(name = "usu_mov")
    private String usuMov;

    // Fecha de movimiento
    @Column(name = "fec_mov")
    private LocalDateTime fecMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getProId() {
        return proId;
    }

    // Modifica el ID
    public void setProId(Long proId) {
        this.proId = proId;
    }

    // Obtiene el cliente
    public Long getCliId() {
        return cliId;
    }

    // Modifica el cliente
    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }

    // Obtiene el tipo de producto
    public String getProTipPro() {
        return proTipPro;
    }

    // Modifica el tipo de producto
    public void setProTipPro(String proTipPro) {
        this.proTipPro = proTipPro;
    }

    // Obtiene el nombre
    public String getProNom() {
        return proNom;
    }

    // Modifica el nombre
    public void setProNom(String proNom) {
        this.proNom = proNom;
    }

    // Obtiene el estado activo
    public Boolean getProAct() {
        return proAct;
    }

    // Modifica el estado activo
    public void setProAct(Boolean proAct) {
        this.proAct = proAct;
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