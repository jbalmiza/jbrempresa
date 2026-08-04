
package com.jbrempresa.backend.entity;

//Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @Column(name = "cli_usu_mov")
    private String cliUsuMov;

    // Fecha de movimiento
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "cli_fec_mov")
    private LocalDateTime cliFecMov;

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
    public String getCliUsuMov() {
        return cliUsuMov;
    }

    // Modifica el usuario de movimiento
    public void setCliUsuMov(String cliUsuMov) {
        this.cliUsuMov = cliUsuMov;
    }

    // Obtiene la fecha de movimiento
    public LocalDateTime getCliFecMov() {
        return cliFecMov;
    }

    // Modifica la fecha de movimiento
    public void setCliFecMov(LocalDateTime cliFecMov) {
        this.cliFecMov = cliFecMov;
    }

}
