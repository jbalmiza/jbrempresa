
package com.jbrempresa.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity

@Table(name = "venta_detalle")

public class VentaDetalle {

    // Constructor vacío
    public VentaDetalle() {

    }

    // Venta Detalle
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ven_det_id")
    private Long venDetId;

    // Venta
    @ManyToOne
    @JoinColumn(name = "ven_id")
    private Venta venId;
    
    // Producto
    @ManyToOne
    @JoinColumn(name = "pro_id")
    private Producto proId;

    // Cantidad
    @Column(name = "ven_det_can")
    private Integer venDetCan;

    // Precio Unitario
    @Column(name = "ven_det_pre")
    private BigDecimal venDetPre;
    
    // Precio Descuento
    @Column(name = "ven_det_des")
    private BigDecimal venDetDes;
    
    // Precio IVA
    @Column(name = "ven_det_iva")
    private BigDecimal venDetIva;

    // Importe Línea
    @Column(name = "ven_det_imp")
    private BigDecimal venDetImp;
    
    // GETTERS Y SETTERS------------------------------------------------
   
    // Venta Detalle
    public Long getVenDetId() {
        return venDetId;
    }

    public void setVenDetId(Long venDetId) {
        this.venDetId = venDetId;
    }
    
    // Venta
    public Venta getVenId() {
        return venId;
    }

    public void setVenId(Venta venId) {
        this.venId = venId;
    }

    // Producto
    public Producto getProId() {
        return proId;
    }

    public void setProId(Producto proId) {
        this.proId = proId;
    }

    // Cantidad
    public Integer getVenDetCan() {
        return venDetCan;
    }

    public void setVenDetCan(Integer venDetCan) {
        this.venDetCan = venDetCan;
    }

    // Precio Unitario
    public BigDecimal getVenDetPre() {
        return venDetPre;
    }

    public void setVenDetPre(BigDecimal venDetPre) {
        this.venDetPre = venDetPre;
    }
    
    // Precio Descuento
    public BigDecimal getVenDetDes() {
        return venDetDes;
    }

    public void setVenDetDes(BigDecimal venDetDes) {
        this.venDetDes = venDetDes;
    }
    
    // Precio IVA
    public BigDecimal getVenDetIva() {
        return venDetIva;
    }

    public void setVenDetIva(BigDecimal venDetIva) {
        this.venDetIva = venDetIva;
    }

    // Importe Línea
    public BigDecimal getVenDetImp() {
        return venDetImp;
    }

    public void setVenDetImp(BigDecimal venDetImp) {
        this.venDetImp = venDetImp;
    }
    
}