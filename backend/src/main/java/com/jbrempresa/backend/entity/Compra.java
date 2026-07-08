package com.jbrempresa.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Entidad JPA
@Entity

// Tabla compras
@Table(name = "compras")

// Clase Compra
public class Compra {

    // Constructor vacío
    public Compra() {

    }

    // Clave primaria
    @Id

    // Genera automáticamente el ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id")
    private Long comId;

    // Cliente
    @Column(name = "cli_id")
    private Long cliId;

    // ID del comprador
    @Column(name = "per_id_com")
    private Long perIdCom;

    // ID del vendedor
    @Column(name = "per_id_ven")
    private Long perIdVen;

    // ID del producto
    @Column(name = "pro_id")
    private Long proId;

    // Importe Subtotal
    @Column(name = "com_imp_sub")
    private BigDecimal comImpSub;

    // Importe Descuento
    @Column(name = "com_imp_des")
    private BigDecimal comImpDes;

    // Importe IVA
    @Column(name = "com_imp_iva")
    private BigDecimal comImpIva;

    // Importe Total
    @Column(name = "com_imp_tot")
    private BigDecimal comImpTot;

    // Importe Pagado
    @Column(name = "com_imp_pag")
    private BigDecimal comImpPag;

    // Importe Pendiente
    @Column(name = "com_imp_pen")
    private BigDecimal comImpPen;

    // Fecha Presupuesto
    @Column(name = "com_fec_pre")
    private LocalDate comFecPre;

    // Fecha Pedido
    @Column(name = "com_fec_ped")
    private LocalDate comFecPed;

    // Fecha Albarán
    @Column(name = "com_fec_alb")
    private LocalDate comFecAlb;

    // Fecha Factura
    @Column(name = "com_fec_fac")
    private LocalDate comFecFac;

    // Fecha Pago
    @Column(name = "com_fec_pag")
    private LocalDate comFecPag;

    // Activo
    @Column(name = "com_act")
    private Boolean comAct;

    // Usuario de movimiento
    @Column(name = "com_usu_mov")
    private String comUsuMov;

    // Fecha de movimiento
    @Column(name = "com_fec_mov")
    private LocalDateTime comFecMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getComId() {
        return comId;
    }

    // Modifica el ID
    public void setComId(Long comId) {
        this.comId = comId;
    }

    // Obtiene el cliente
    public Long getCliId() {
        return cliId;
    }

    // Modifica el cliente
    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }

    // Obtiene el ID del comprador
    public Long getPerIdCom() {
        return perIdCom;
    }

    // Modifica el ID del comprador
    public void setPerIdCom(Long perIdCom) {
        this.perIdCom = perIdCom;
    }

    // Obtiene el ID del vendedor
    public Long getPerIdVen() {
        return perIdVen;
    }

    // Modifica el ID del vendedor
    public void setPerIdVen(Long perIdVen) {
        this.perIdVen = perIdVen;
    }

    // Obtiene el ID del producto
    public Long getProId() {
        return proId;
    }

    // Modifica el ID del producto
    public void setProId(Long proId) {
        this.proId = proId;
    }

    // Obtiene el importe subtotal
    public BigDecimal getComImpSub() {
        return comImpSub;
    }

    // Modifica el importe subtotal
    public void setComImpSub(BigDecimal comImpSub) {
        this.comImpSub = comImpSub;
    }

    // Obtiene el importe descuento
    public BigDecimal getComImpDes() {
        return comImpDes;
    }

    // Modifica el importe descuento
    public void setComImpDes(BigDecimal comImpDes) {
        this.comImpDes = comImpDes;
    }

    // Obtiene el importe IVA
    public BigDecimal getComImpIva() {
        return comImpIva;
    }

    // Modifica el importe IVA
    public void setComImpIva(BigDecimal comImpIva) {
        this.comImpIva = comImpIva;
    }

    // Obtiene el importe total
    public BigDecimal getComImpTot() {
        return comImpTot;
    }

    // Modifica el importe total
    public void setComImpTot(BigDecimal comImpTot) {
        this.comImpTot = comImpTot;
    }

    // Obtiene el importe pagado
    public BigDecimal getComImpPag() {
        return comImpPag;
    }

    // Modifica el importe pagado
    public void setComImpPag(BigDecimal comImpPag) {
        this.comImpPag = comImpPag;
    }

    // Obtiene el importe pendiente
    public BigDecimal getComImpPen() {
        return comImpPen;
    }

    // Modifica el importe pendiente
    public void setComImpPen(BigDecimal comImpPen) {
        this.comImpPen = comImpPen;
    }

    // Obtiene la fecha presupuesto
    public LocalDate getComFecPre() {
        return comFecPre;
    }

    // Modifica la fecha presupuesto
    public void setComFecPre(LocalDate comFecPre) {
        this.comFecPre = comFecPre;
    }

    // Obtiene la fecha pedido
    public LocalDate getComFecPed() {
        return comFecPed;
    }

    // Modifica la fecha pedido
    public void setComFecPed(LocalDate comFecPed) {
        this.comFecPed = comFecPed;
    }

    // Obtiene la fecha albarán
    public LocalDate getComFecAlb() {
        return comFecAlb;
    }

    // Modifica la fecha albarán
    public void setComFecAlb(LocalDate comFecAlb) {
        this.comFecAlb = comFecAlb;
    }

    // Obtiene la fecha factura
    public LocalDate getComFecFac() {
        return comFecFac;
    }

    // Modifica la fecha factura
    public void setComFecFac(LocalDate comFecFac) {
        this.comFecFac = comFecFac;
    }

    // Obtiene la fecha pago
    public LocalDate getComFecPag() {
        return comFecPag;
    }

    // Modifica la fecha pago
    public void setComFecPag(LocalDate comFecPag) {
        this.comFecPag = comFecPag;
    }

    // Obtiene el estado activo
    public Boolean getComAct() {
        return comAct;
    }

    // Modifica el estado activo
    public void setComAct(Boolean comAct) {
        this.comAct = comAct;
    }

    // Obtiene el usuario de movimiento
    public String getComUsuMov() {
        return comUsuMov;
    }

    // Modifica el usuario de movimiento
    public void setComUsuMov(String comUsuMov) {
        this.comUsuMov = comUsuMov;
    }

    // Obtiene la fecha de movimiento
    public LocalDateTime getComFecMov() {
        return comFecMov;
    }

    // Modifica la fecha de movimiento
    public void setComFecMov(LocalDateTime comFecMov) {
        this.comFecMov = comFecMov;
    }

}