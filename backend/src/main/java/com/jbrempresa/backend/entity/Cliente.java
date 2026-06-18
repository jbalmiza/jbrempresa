
package com.jbrempresa.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Entidad JPA
@Entity

// Tabla clientes
@Table(name = "clientes")

// Clase Cliente
public class Cliente {

    // Constructor vacío
    public Cliente() {

    }

    // Clave primaria
    @Id

    // Genera automáticamente el ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "cli_id")
    private Long cliId;

    // Nombre
    @Column(name = "cli_nom")
    private String cliNom;

    // Activo
    @Column(name = "cli_act")
    private String cliAct;

    // Usuario de movimiento
    @Column(name = "usu_mov")
    private String usuMov;

    // Fecha de movimiento
    @Column(name = "fec_mov")
    private LocalDateTime fecMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getCliId() {
        return cliId;
    }

    // Modifica el ID
    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }

    // Obtiene el nombre
    public String getCliNom() {
        return cliNom;
    }

    // Modifica el nombre
    public void setCliNom(String cliNom) {
        this.cliNom = cliNom;
    }

    // Obtiene el estado activo
    public String getCliAct() {
        return cliAct;
    }

    // Modifica el estado activo
    public void setCliAct(String cliAct) {
        this.cliAct = cliAct;
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
