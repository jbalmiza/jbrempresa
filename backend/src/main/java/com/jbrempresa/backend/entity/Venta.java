
package com.jbrempresa.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidad JPA
@Entity

// Tabla ventas
@Table(name = "ventas")

// Clase Venta
public class Venta {

    // Constructor vacío
    public Venta() {

    }

    // Clave primaria
    @Id

    // Genera automáticamente el ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "ven_id")
    private Long venId;

    // Cliente
    @Column(name = "cli_id")
    private Long cliId;

    // ID de la persona
    @Column(name = "per_id")
    private Long perId;

    // ID del producto
    @Column(name = "pro_id")
    private Long proId;

    // Activo
    @Column(name = "ven_act")
    private Boolean venAct;

    // Usuario de movimiento
    @Column(name = "usu_mov")
    private String usuMov;

    // Fecha de movimiento
    @Column(name = "fec_mov")
    private LocalDateTime fecMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getVenId() {
        return venId;
    }

    // Modifica el ID
    public void setVenId(Long venId) {
        this.venId = venId;
    }

    // Obtiene el cliente
    public Long getCliId() {
        return cliId;
    }

    // Modifica el cliente
    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }

    // Obtiene el ID de la persona
    public Long getPerId() {
        return perId;
    }

    // Modifica el ID de la persona
    public void setPerId(Long perId) {
        this.perId = perId;
    }

    // Obtiene el ID del producto
    public Long getProId() {
        return proId;
    }

    // Modifica el ID del producto
    public void setProId(Long proId) {
        this.proId = proId;
    }

    // Obtiene el estado activo
    public Boolean getVenAct() {
        return venAct;
    }

    // Modifica el estado activo
    public void setVenAct(Boolean venAct) {
        this.venAct = venAct;
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
