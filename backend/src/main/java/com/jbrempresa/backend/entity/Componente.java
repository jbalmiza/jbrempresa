package com.jbrempresa.backend.entity;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.LocalDateTime;import com.fasterxml.jackson.annotation.JsonFormat;
@Entity @IdClass(ComponenteId.class) @Table(name="componentes")
public class Componente {
 @Id @Column(name="cmp_id") private Long cmpId; @Id @Column(name="cmp_id_his") private Long cmpIdHis;
 @Id @Column(name="emp_id") private Long empId; @Column(name="cmp_tip_mov") private String cmpTipMov; @Column(name="cmp_cau_mov") private String cmpCauMov; @Column(name="cmp_tip") private String cmpTip;
 @Column(name="cmp_nom") private String cmpNom; @Column(name="cmp_des") private String cmpDes;
 @Column(name="cmp_pre_adi") private BigDecimal cmpPreAdi; @Column(name="cmp_iva") private BigDecimal cmpIva;
 @Column(name="cmp_gluten") private Boolean cmpGluten; @Column(name="cmp_crustaceos") private Boolean cmpCrustaceos;
 @Column(name="cmp_huevos") private Boolean cmpHuevos; @Column(name="cmp_pescado") private Boolean cmpPescado;
 @Column(name="cmp_cacahuetes") private Boolean cmpCacahuetes; @Column(name="cmp_soja") private Boolean cmpSoja;
 @Column(name="cmp_leche") private Boolean cmpLeche; @Column(name="cmp_frutos_cascara") private Boolean cmpFrutosCascara;
 @Column(name="cmp_apio") private Boolean cmpApio; @Column(name="cmp_mostaza") private Boolean cmpMostaza;
 @Column(name="cmp_sesamo") private Boolean cmpSesamo; @Column(name="cmp_sulfitos") private Boolean cmpSulfitos;
 @Column(name="cmp_altramuces") private Boolean cmpAltramuces; @Column(name="cmp_moluscos") private Boolean cmpMoluscos;
 @Column(name="cmp_usu_mov") private String cmpUsuMov; @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") @Column(name="cmp_fec_mov") private LocalDateTime cmpFecMov; @Column(name="cmp_act") private Boolean cmpAct;
 public Long getCmpId(){return cmpId;}public void setCmpId(Long v){cmpId=v;}public Long getCmpIdHis(){return cmpIdHis;}public void setCmpIdHis(Long v){cmpIdHis=v;}public Long getEmpId(){return empId;}public void setEmpId(Long v){empId=v;}
 public String getCmpTipMov(){return cmpTipMov;}public void setCmpTipMov(String v){cmpTipMov=v;}public String getCmpCauMov(){return cmpCauMov;}public void setCmpCauMov(String v){cmpCauMov=v;}public String getCmpTip(){return cmpTip;}public void setCmpTip(String v){cmpTip=v;}public String getCmpNom(){return cmpNom;}public void setCmpNom(String v){cmpNom=v;}public String getCmpDes(){return cmpDes;}public void setCmpDes(String v){cmpDes=v;}
 public BigDecimal getCmpPreAdi(){return cmpPreAdi;}public void setCmpPreAdi(BigDecimal v){cmpPreAdi=v;}public BigDecimal getCmpIva(){return cmpIva;}public void setCmpIva(BigDecimal v){cmpIva=v;}
 public Boolean getCmpGluten(){return cmpGluten;}public void setCmpGluten(Boolean v){cmpGluten=v;}public Boolean getCmpCrustaceos(){return cmpCrustaceos;}public void setCmpCrustaceos(Boolean v){cmpCrustaceos=v;}public Boolean getCmpHuevos(){return cmpHuevos;}public void setCmpHuevos(Boolean v){cmpHuevos=v;}public Boolean getCmpPescado(){return cmpPescado;}public void setCmpPescado(Boolean v){cmpPescado=v;}public Boolean getCmpCacahuetes(){return cmpCacahuetes;}public void setCmpCacahuetes(Boolean v){cmpCacahuetes=v;}public Boolean getCmpSoja(){return cmpSoja;}public void setCmpSoja(Boolean v){cmpSoja=v;}public Boolean getCmpLeche(){return cmpLeche;}public void setCmpLeche(Boolean v){cmpLeche=v;}public Boolean getCmpFrutosCascara(){return cmpFrutosCascara;}public void setCmpFrutosCascara(Boolean v){cmpFrutosCascara=v;}public Boolean getCmpApio(){return cmpApio;}public void setCmpApio(Boolean v){cmpApio=v;}public Boolean getCmpMostaza(){return cmpMostaza;}public void setCmpMostaza(Boolean v){cmpMostaza=v;}public Boolean getCmpSesamo(){return cmpSesamo;}public void setCmpSesamo(Boolean v){cmpSesamo=v;}public Boolean getCmpSulfitos(){return cmpSulfitos;}public void setCmpSulfitos(Boolean v){cmpSulfitos=v;}public Boolean getCmpAltramuces(){return cmpAltramuces;}public void setCmpAltramuces(Boolean v){cmpAltramuces=v;}public Boolean getCmpMoluscos(){return cmpMoluscos;}public void setCmpMoluscos(Boolean v){cmpMoluscos=v;}
 public String getCmpUsuMov(){return cmpUsuMov;}public void setCmpUsuMov(String v){cmpUsuMov=v;}public LocalDateTime getCmpFecMov(){return cmpFecMov;}public void setCmpFecMov(LocalDateTime v){cmpFecMov=v;}public Boolean getCmpAct(){return cmpAct;}public void setCmpAct(Boolean v){cmpAct=v;}
}
