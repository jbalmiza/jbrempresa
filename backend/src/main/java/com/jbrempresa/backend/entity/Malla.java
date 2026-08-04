// Define el paquete donde está ubicada esta entidad Java.
package com.jbrempresa.backend.entity;

// Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity

@Table(name = "mallas")

public class Malla {

    // Identificador
    @Id
    @Column(name = "mal_id")
    private Long malId;

    // Cliente
    @Column(name = "cli_id")
    private Long cliId;
    
    // Entidad de malla (productos, usuarios, etc.)
    @Column(name = "mal_ent")
    private String malEnt;

    // Fila
    @Column(name = "mal_fil")
    private Integer malFil;

    // Columna
    @Column(name = "mal_col")
    private Integer malCol;

    // Tipo de registro (PRODUCTO, PASILLO, MACETA...)
    @Column(name = "mal_tip")
    private String malTip;

    // Id del elemento relacionado
    @Column(name = "mal_ref_id")
    private Long malRefId;

    // Descripción
    @Column(name = "mal_des")
    private String malDes;

    // Activo
    @Column(name = "mal_act")
    private Boolean malAct;

    // Usuario modificación
    @Column(name = "mal_usu_mov")
    private String malUsuMov;

    // Fecha modificación
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "mal_fec_mov")
    private LocalDateTime malFecMov;

    // ===== GETTERS Y SETTERS =====

    public Long getMalId() {
        return malId;
    }

    public void setMalId(Long malId) {
        this.malId = malId;
    }

    public Long getCliId() {
        return cliId;
    }

    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }
    
    public String getMalEnt() {
        return malEnt;
    }

    public void setMalEnt(String malEnt) {
        this.malEnt = malEnt;
    }

    public Integer getMalFil() {
        return malFil;
    }

    public void setMalFil(Integer malFil) {
        this.malFil = malFil;
    }

    public Integer getMalCol() {
        return malCol;
    }

    public void setMalCol(Integer malCol) {
        this.malCol = malCol;
    }

    public String getMalTip() {
        return malTip;
    }

    public void setMalTip(String malTip) {
        this.malTip = malTip;
    }

    public Long getMalRefId() {
        return malRefId;
    }

    public void setMalRefId(Long malRefId) {
        this.malRefId = malRefId;
    }

    public String getMalDes() {
        return malDes;
    }

    public void setMalDes(String malDes) {
        this.malDes = malDes;
    }

    public Boolean getMalAct() {
        return malAct;
    }

    public void setMalAct(Boolean malAct) {
        this.malAct = malAct;
    }

    public String getMalUsuMov() {
        return malUsuMov;
    }

    public void setMalUsuMov(String malUsuMov) {
        this.malUsuMov = malUsuMov;
    }

    public LocalDateTime getMalFecMov() {
        return malFecMov;
    }

    public void setMalFecMov(LocalDateTime malFecMov) {
        this.malFecMov = malFecMov;
    }

}