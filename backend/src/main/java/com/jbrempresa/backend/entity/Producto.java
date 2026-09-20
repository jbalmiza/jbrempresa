
// Define el paquete donde está ubicada esta entidad Java.
package com.jbrempresa.backend.entity;

// Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

// Entidad JPA
@Entity
@IdClass(ProductoId.class)

// Tabla productos
@Table(name = "productos")

// Clase Producto
public class Producto {

    @Transient
    private List<ProductoComponente> componentes;

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

    @Column(name = "pro_iva_com")
    private BigDecimal proIvaCom;

    @Column(name = "pro_des_com")
    private BigDecimal proDesCom;

    @Column(name = "pro_tot_com")
    private BigDecimal proTotCom;

    @Column(name = "pro_pre_com_est")
    private Boolean proPreComEst;
    
    // Precio Venta
    @Column(name = "pro_pre_ven", precision = 14, scale = 4)
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

    @Column(name = "pro_nov")
    private Boolean proNov = false;

    @Column(name = "pro_mej_pre")
    private Boolean proMejPre = false;

    @Column(name = "pro_out")
    private Boolean proOut = false;

    @Column(name = "pro_dis_lun") private Boolean proDisLun = true;
    @Column(name = "pro_dis_mar") private Boolean proDisMar = true;
    @Column(name = "pro_dis_mie") private Boolean proDisMie = true;
    @Column(name = "pro_dis_jue") private Boolean proDisJue = true;
    @Column(name = "pro_dis_vie") private Boolean proDisVie = true;
    @Column(name = "pro_dis_sab") private Boolean proDisSab = true;
    @Column(name = "pro_dis_dom") private Boolean proDisDom = true;

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

    public List<ProductoComponente> getComponentes() { return componentes; }
    public void setComponentes(List<ProductoComponente> componentes) { this.componentes = componentes; }

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

    public BigDecimal getProIvaCom() { return proIvaCom; }
    public void setProIvaCom(BigDecimal proIvaCom) { this.proIvaCom = proIvaCom; }
    public BigDecimal getProDesCom() { return proDesCom; }
    public void setProDesCom(BigDecimal proDesCom) { this.proDesCom = proDesCom; }
    public BigDecimal getProTotCom() { return proTotCom; }
    public void setProTotCom(BigDecimal proTotCom) { this.proTotCom = proTotCom; }
    public Boolean getProPreComEst() { return proPreComEst; }
    public void setProPreComEst(Boolean proPreComEst) { this.proPreComEst = proPreComEst; }

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
    public Boolean getProNov() { return proNov; }
    public void setProNov(Boolean proNov) { this.proNov = proNov; }
    public Boolean getProMejPre() { return proMejPre; }
    public void setProMejPre(Boolean proMejPre) { this.proMejPre = proMejPre; }
    public Boolean getProOut() { return proOut; }
    public void setProOut(Boolean proOut) { this.proOut = proOut; }
    public Boolean getProDisLun() { return proDisLun; } public void setProDisLun(Boolean v) { proDisLun=v; }
    public Boolean getProDisMar() { return proDisMar; } public void setProDisMar(Boolean v) { proDisMar=v; }
    public Boolean getProDisMie() { return proDisMie; } public void setProDisMie(Boolean v) { proDisMie=v; }
    public Boolean getProDisJue() { return proDisJue; } public void setProDisJue(Boolean v) { proDisJue=v; }
    public Boolean getProDisVie() { return proDisVie; } public void setProDisVie(Boolean v) { proDisVie=v; }
    public Boolean getProDisSab() { return proDisSab; } public void setProDisSab(Boolean v) { proDisSab=v; }
    public Boolean getProDisDom() { return proDisDom; } public void setProDisDom(Boolean v) { proDisDom=v; }
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
