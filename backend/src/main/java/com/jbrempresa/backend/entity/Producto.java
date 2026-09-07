
// Define el paquete donde está ubicada esta entidad Java.
package com.jbrempresa.backend.entity;

// Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

// Entidad JPA
@Entity
@IdClass(ProductoId.class)

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
    @Column(name = "pro_id")
    private Long proId;

    @Id
    @Column(name = "pro_id_his")
    private Long proIdHis;

    @Column(name = "pro_tip_mov")
    private String proTipMov;

    @Column(name = "pro_cau_mov", length = 500)
    private String proCauMov;

    // Empresa
    @Column(name = "emp_id")
    private Long empId;

    // Tipo de producto
    @Column(name = "pro_tip_pro")
    private String proTipPro;

    // Nombre
    @Column(name = "pro_nom")
    private String proNom;
    
    // Descripcion
    @Column(name = "pro_des")
    private String proDes;
    
    // Categoria
    @Column(name = "pro_cat")
    private String proCat;
    
    // Subcategoria
    @Column(name = "pro_sub_cat")
    private String proSubCat;
    
    // Marca
    @Column(name = "pro_mar")
    private String proMar;
    
    // Modelo
    @Column(name = "pro_mod")
    private String proMod;
    
    // Proveedor
    @Column(name = "pro_pro")
    private String proPro;
    
    // Precio Compra
    @Column(name = "pro_pre_com")
    private BigDecimal proPreCom;
    
    // Precio Venta
    @Column(name = "pro_pre_ven")
    private BigDecimal proPreVen;
    
    // Precio IVA
    @Column(name = "pro_pre_iva")
    private BigDecimal proPreIva;
    
    // Precio Descuento
    @Column(name = "pro_pre_des")
    private BigDecimal proPreDes;
    
    // Precio Final
    @Column(name = "pro_pre_fin")
    private BigDecimal proPreFin;
    
    // Stock Actual
    @Column(name = "pro_sto_act")
    private Integer proStoAct;
    
    // Stock Minimo
    @Column(name = "pro_sto_min")
    private Integer proStoMin;
    
    // Unidad Medida
    @Column(name = "pro_uni_med")
    private String proUniMed;
    
    // Control Stock
    @Column(name = "pro_con_sto")
    private Boolean proConSto;
    
    // Observaciones
    @Column(name = "pro_obs")
    private String proObs;

    @Column(name = "pro_dur_min")
    private Integer proDurMin;

    @Column(name = "pro_vis_cat")
    private Boolean proVisCat = true;

    @Column(name = "pro_ima", length = 500)
    private String proIma = "producto-predeterminado.png";
    
    // Ubicación
    @Column(name = "pro_ubi")
    private String proUbi;
    
    // Fila Malla
    @Column(name = "pro_fil_mal")
    private Integer proFilMal;
    
    // Columna Malla
    @Column(name = "pro_col_mal")
    private Integer proColMal;

    // Activo
    @Column(name = "pro_act")
    private Boolean proAct;

    // Usuario de movimiento
    @Column(name = "pro_usu_mov")
    private String proUsuMov;

    // Fecha de movimiento
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "pro_fec_mov")
    private LocalDateTime proFecMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getProId() {
        return proId;
    }

    // Modifica el ID
    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Long getProIdHis() { return proIdHis; }
    public void setProIdHis(Long proIdHis) { this.proIdHis = proIdHis; }
    public String getProTipMov() { return proTipMov; }
    public void setProTipMov(String proTipMov) { this.proTipMov = proTipMov; }
    public String getProCauMov() { return proCauMov; }
    public void setProCauMov(String proCauMov) { this.proCauMov = proCauMov; }

    // Obtiene el empresa
    public Long getEmpId() {
        return empId;
    }

    // Modifica el empresa
    public void setEmpId(Long empId) {
        this.empId = empId;
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
    
 // Descripcion
    public String getProDes() {
        return proDes;
    }

    public void setProDes(String proDes) {
        this.proDes = proDes;
    }

    // Categoria
    public String getProCat() {
        return proCat;
    }

    public void setProCat(String proCat) {
        this.proCat = proCat;
    }

    // Subcategoria
    public String getProSubCat() {
        return proSubCat;
    }

    public void setProSubCat(String proSubCat) {
        this.proSubCat = proSubCat;
    }

    // Marca
    public String getProMar() {
        return proMar;
    }

    public void setProMar(String proMar) {
        this.proMar = proMar;
    }

    // Modelo
    public String getProMod() {
        return proMod;
    }

    public void setProMod(String proMod) {
        this.proMod = proMod;
    }

    // Proveedor
    public String getProPro() {
        return proPro;
    }

    public void setProPro(String proPro) {
        this.proPro = proPro;
    }

    // Precio Compra
    public BigDecimal getProPreCom() {
        return proPreCom;
    }

    public void setProPreCom(BigDecimal proPreCom) {
        this.proPreCom = proPreCom;
    }

    // Precio Venta
    public BigDecimal getProPreVen() {
        return proPreVen;
    }

    public void setProPreVen(BigDecimal proPreVen) {
        this.proPreVen = proPreVen;
    }

    // Precio IVA
    public BigDecimal getProPreIva() {
        return proPreIva;
    }

    public void setProPreIva(BigDecimal proPreIva) {
        this.proPreIva = proPreIva;
    }

    // Precio Descuento
    public BigDecimal getProPreDes() {
        return proPreDes;
    }

    public void setProPreDes(BigDecimal proPreDes) {
        this.proPreDes = proPreDes;
    }

    // Precio Final
    public BigDecimal getProPreFin() {
        return proPreFin;
    }

    public void setProPreFin(BigDecimal proPreFin) {
        this.proPreFin = proPreFin;
    }
    
 // Stock Actual
    public Integer getProStoAct() {
        return proStoAct;
    }

    public void setProStoAct(Integer proStoAct) {
        this.proStoAct = proStoAct;
    }

    // Stock Minimo
    public Integer getProStoMin() {
        return proStoMin;
    }

    public void setProStoMin(Integer proStoMin) {
        this.proStoMin = proStoMin;
    }

    // Unidad Medida
    public String getProUniMed() {
        return proUniMed;
    }

    public void setProUniMed(String proUniMed) {
        this.proUniMed = proUniMed;
    }

    // Control Stock
    public Boolean getProConSto() {
        return proConSto;
    }

    public void setProConSto(Boolean proConSto) {
        this.proConSto = proConSto;
    }

    // Observaciones
    public String getProObs() {
        return proObs;
    }

    public void setProObs(String proObs) {
        this.proObs = proObs;
    }

    public Integer getProDurMin() { return proDurMin; }
    public void setProDurMin(Integer proDurMin) { this.proDurMin = proDurMin; }
    public Boolean getProVisCat() { return proVisCat; }
    public void setProVisCat(Boolean proVisCat) { this.proVisCat = proVisCat; }
    public String getProIma() { return proIma; }
    public void setProIma(String proIma) { this.proIma = proIma; }
    
    public String getProUbi() {
        return proUbi;
    }

    public void setProUbi(String proUbi) {
        this.proUbi = proUbi;
    }

    public Integer getProFilMal() {
        return proFilMal;
    }

    public void setProFilMal(Integer proFilMal) {
        this.proFilMal = proFilMal;
    }

    public Integer getProColMal() {
        return proColMal;
    }

    public void setProColMal(Integer proColMal) {
        this.proColMal = proColMal;
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
    public String getProUsuMov() {
        return proUsuMov;
    }

    // Modifica el usuario de movimiento
    public void setProUsuMov(String proUsuMov) {
        this.proUsuMov = proUsuMov;
    }

    // Obtiene la fecha de movimiento
    public LocalDateTime getProFecMov() {
        return proFecMov;
    }

    // Modifica la fecha de movimiento
    public void setProFecMov(LocalDateTime proFecMov) {
        this.proFecMov = proFecMov;
    }

}
