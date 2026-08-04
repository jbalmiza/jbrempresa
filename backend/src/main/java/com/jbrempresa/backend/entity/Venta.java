
package com.jbrempresa.backend.entity;

//Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

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

    // ID de la persona Vendedor
    @Column(name = "per_id_ven")
    private Long perIdVen;
    
    // ID de la persona Comprador
    @Column(name = "per_id_com")
    private Long perIdCom;

    // ID del producto
    @Column(name = "pro_id")
    private Long proId;
    
    // Importe Subtotal
    @Column(name = "ven_imp_sub")
    private BigDecimal venImpSub;
    
    // Importe Descuento
    @Column(name = "ven_imp_des")
    private BigDecimal venImpDes;
    
    // Importe IVA
    @Column(name = "ven_imp_iva")
    private BigDecimal venImpIva;
    
    // Importe Total
    @Column(name = "ven_imp_tot")
    private BigDecimal venImpTot;
    
    // ImporteCobrado
    @Column(name = "ven_imp_cob")
    private BigDecimal venImpCob;
    
    // Importe Pendiente
    @Column(name = "ven_imp_pen")
    private BigDecimal venImpPen;
    
    // Fecha Presupuesto
    @Column(name = "ven_fec_pre")
    private LocalDate venFecPre;
    
    // Fecha Pedido
    @Column(name = "ven_fec_ped")
    private LocalDate venFecPed;
    
    // Fecha Albaran
    @Column(name = "ven_fec_alb")
    private LocalDate venFecAlb;
    
    // Fecha Factura
    @Column(name = "ven_fec_fac")
    private LocalDate venFecFac;
    
    // Fecha Cobro
    @Column(name = "ven_fec_cob")
    private LocalDate venFecCob;

    // Activo
    @Column(name = "ven_act")
    private Boolean venAct;

    // Usuario de movimiento
    @Column(name = "ven_usu_mov")
    private String venUsuMov;

    // Fecha de movimiento
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "ven_fec_mov")
    private LocalDateTime venFecMov;

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

    // Obtiene el ID de la persona Vendedor
    public Long getPerIdVen() {
        return perIdVen;
    }

    // Modifica el ID de la persona Vendedor
    public void setPerIdVen(Long perIdVen) {
        this.perIdVen = perIdVen;
    }
    
    // Obtiene el ID de la persona Comprador
    public Long getPerIdCom() {
        return perIdCom;
    }

    // Modifica el ID de la persona Comprador
    public void setPerIdCom(Long perIdCom) {
        this.perIdCom = perIdCom;
    }

    // Obtiene el ID del producto
    public Long getProId() {
        return proId;
    }

    // Modifica el ID del producto
    public void setProId(Long proId) {
        this.proId = proId;
    }
    
 // Importe Subtotal
    public BigDecimal getVenImpSub() {
        return venImpSub;
    }

    public void setVenImpSub(BigDecimal venImpSub) {
        this.venImpSub = venImpSub;
    }

    // Importe Descuento
    public BigDecimal getVenImpDes() {
        return venImpDes;
    }

    public void setVenImpDes(BigDecimal venImpDes) {
        this.venImpDes = venImpDes;
    }

    // Importe IVA
    public BigDecimal getVenImpIva() {
        return venImpIva;
    }

    public void setVenImpIva(BigDecimal venImpIva) {
        this.venImpIva = venImpIva;
    }

    // Importe Total
    public BigDecimal getVenImpTot() {
        return venImpTot;
    }

    public void setVenImpTot(BigDecimal venImpTot) {
        this.venImpTot = venImpTot;
    }

    // Importe Cobrado
    public BigDecimal getVenImpCob() {
        return venImpCob;
    }

    public void setVenImpCob(BigDecimal venImpCob) {
        this.venImpCob = venImpCob;
    }

    // Importe Pendiente
    public BigDecimal getVenImpPen() {
        return venImpPen;
    }

    public void setVenImpPen(BigDecimal venImpPen) {
        this.venImpPen = venImpPen;
    }

    // Fecha Presupuesto
    public LocalDate getVenFecPre() {
        return venFecPre;
    }

    public void setVenFecPre(LocalDate venFecPre) {
        this.venFecPre = venFecPre;
    }

    // Fecha Pedido
    public LocalDate getVenFecPed() {
        return venFecPed;
    }

    public void setVenFecPed(LocalDate venFecPed) {
        this.venFecPed = venFecPed;
    }

    // Fecha Albaran
    public LocalDate getVenFecAlb() {
        return venFecAlb;
    }

    public void setVenFecAlb(LocalDate venFecAlb) {
        this.venFecAlb = venFecAlb;
    }

    // Fecha Factura
    public LocalDate getVenFecFac() {
        return venFecFac;
    }

    public void setVenFecFac(LocalDate venFecFac) {
        this.venFecFac = venFecFac;
    }

    // Fecha Cobro
    public LocalDate getVenFecCob() {
        return venFecCob;
    }

    public void setVenFecCob(LocalDate venFecCob) {
        this.venFecCob = venFecCob;
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
    public String getVenUsuMov() {
        return venUsuMov;
    }

    // Modifica el usuario de movimiento
    public void setVenUsuMov(String venUsuMov) {
        this.venUsuMov = venUsuMov;
    }

    // Obtiene la fecha de movimiento
    public LocalDateTime getVenFecMov() {
        return venFecMov;
    }

    // Modifica la fecha de movimiento
    public void setVenFecMov(LocalDateTime venFecMov) {
        this.venFecMov = venFecMov;
    }

}
