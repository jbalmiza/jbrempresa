
// Define el paquete donde está ubicada esta entidad Java.
package com.jbrempresa.backend.entity;

// Importa las anotaciones JPA.
import jakarta.persistence.*;

import java.time.LocalDateTime;

// Entidad JPA
@Entity

// Tabla domicilios
@Table(name = "domicilios")

// Clase Domicilio
public class Domicilio {

    // Constructor vacío
    public Domicilio() {

    }

    // Clave primaria
    @Id

    // Genera automáticamente el ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "dom_id")
    private Long domId;

    // Cliente
    @Column(name = "cli_id")
    private Long cliId;

    // Tipo de vía
    @Column(name = "dom_tip_via")
    private String domTipVia;

    // Vía
    @Column(name = "dom_via")
    private String domVia;

    // Número
    @Column(name = "dom_num")
    private String domNum;

    // Kilómetro
    @Column(name = "dom_km")
    private String domKm;

    // Edificio
    @Column(name = "dom_edi")
    private String domEdi;

    // Bloque
    @Column(name = "dom_blo")
    private String domBlo;

    // Portal
    @Column(name = "dom_por")
    private String domPor;

    // Escalera
    @Column(name = "dom_esc")
    private String domEsc;

    // Planta
    @Column(name = "dom_pla")
    private String domPla;

    // Puerta
    @Column(name = "dom_pue")
    private String domPue;

    // Observaciones
    @Column(name = "dom_obs")
    private String domObs;

    // Dirección completa
    @Column(name = "dom_dir")
    private String domDir;

    // Activo
    @Column(name = "dom_act")
    private Boolean domAct;

    // Usuario de movimiento
    @Column(name = "usu_mov")
    private String usuMov;

    // Fecha de movimiento
    @Column(name = "fec_mov")
    private LocalDateTime fecMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getDomId() {
        return domId;
    }

    // Modifica el ID
    public void setDomId(Long domId) {
        this.domId = domId;
    }

    // Obtiene el cliente
    public Long getCliId() {
        return cliId;
    }

    // Modifica el cliente
    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }

    // Obtiene el tipo de vía
    public String getDomTipVia() {
        return domTipVia;
    }

    // Modifica el tipo de vía
    public void setDomTipVia(String domTipVia) {
        this.domTipVia = domTipVia;
    }

    // Obtiene la vía
    public String getDomVia() {
        return domVia;
    }

    // Modifica la vía
    public void setDomVia(String domVia) {
        this.domVia = domVia;
    }

    // Obtiene el número
    public String getDomNum() {
        return domNum;
    }

    // Modifica el número
    public void setDomNum(String domNum) {
        this.domNum = domNum;
    }

    // Obtiene el kilómetro
    public String getDomKm() {
        return domKm;
    }

    // Modifica el kilómetro
    public void setDomKm(String domKm) {
        this.domKm = domKm;
    }

    // Obtiene el edificio
    public String getDomEdi() {
        return domEdi;
    }

    // Modifica el edificio
    public void setDomEdi(String domEdi) {
        this.domEdi = domEdi;
    }

    // Obtiene el bloque
    public String getDomBlo() {
        return domBlo;
    }

    // Modifica el bloque
    public void setDomBlo(String domBlo) {
        this.domBlo = domBlo;
    }

    // Obtiene el portal
    public String getDomPor() {
        return domPor;
    }

    // Modifica el portal
    public void setDomPor(String domPor) {
        this.domPor = domPor;
    }

    // Obtiene la escalera
    public String getDomEsc() {
        return domEsc;
    }

    // Modifica la escalera
    public void setDomEsc(String domEsc) {
        this.domEsc = domEsc;
    }

    // Obtiene la planta
    public String getDomPla() {
        return domPla;
    }

    // Modifica la planta
    public void setDomPla(String domPla) {
        this.domPla = domPla;
    }

    // Obtiene la puerta
    public String getDomPue() {
        return domPue;
    }

    // Modifica la puerta
    public void setDomPue(String domPue) {
        this.domPue = domPue;
    }

    // Obtiene las observaciones
    public String getDomObs() {
        return domObs;
    }

    // Modifica las observaciones
    public void setDomObs(String domObs) {
        this.domObs = domObs;
    }

    // Obtiene la dirección
    public String getDomDir() {
        return domDir;
    }

    // Modifica la dirección
    public void setDomDir(String domDir) {
        this.domDir = domDir;
    }

    // Obtiene el estado activo
    public Boolean getDomAct() {
        return domAct;
    }

    // Modifica el estado activo
    public void setDomAct(Boolean domAct) {
        this.domAct = domAct;
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

