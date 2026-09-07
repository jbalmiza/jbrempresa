package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "adjuntos", indexes = @Index(
        name = "idx_adjuntos_registro",
        columnList = "emp_id, adj_mod, adj_tip_reg, adj_reg_id"))
public class Adjunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adj_id")
    private Long adjId;
    @Column(name = "emp_id", nullable = false)
    private Long empId;
    @Column(name = "adj_mod", nullable = false, length = 50)
    private String adjMod;
    @Column(name = "adj_tip_reg", nullable = false, length = 50)
    private String adjTipReg;
    @Column(name = "adj_reg_id", nullable = false)
    private Long adjRegId;
    @Column(name = "adj_nom", nullable = false, length = 150)
    private String adjNom;
    @Column(name = "adj_tip", nullable = false, length = 20)
    private String adjTip;
    @Column(name = "adj_nom_arc", nullable = false, length = 255)
    private String adjNomArc;
    @Column(name = "adj_rut_rel", nullable = false, length = 500)
    private String adjRutRel;
    @Column(name = "adj_mime", nullable = false, length = 150)
    private String adjMime;
    @Column(name = "adj_tam", nullable = false)
    private Long adjTam;
    @Column(name = "adj_usu_mov", nullable = false, length = 50)
    private String adjUsuMov;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "adj_fec_mov", nullable = false)
    private LocalDateTime adjFecMov;
    @Column(name = "adj_act", nullable = false)
    private Boolean adjAct;
    @Column(name = "adj_pri", nullable = false, columnDefinition = "boolean default false")
    private Boolean adjPri = false;

    public Adjunto() {}
    public Long getAdjId() { return adjId; }
    public void setAdjId(Long adjId) { this.adjId = adjId; }
    public Long getEmpId() { return empId; }
    public void setEmpId(Long empId) { this.empId = empId; }
    public String getAdjMod() { return adjMod; }
    public void setAdjMod(String adjMod) { this.adjMod = adjMod; }
    public String getAdjTipReg() { return adjTipReg; }
    public void setAdjTipReg(String adjTipReg) { this.adjTipReg = adjTipReg; }
    public Long getAdjRegId() { return adjRegId; }
    public void setAdjRegId(Long adjRegId) { this.adjRegId = adjRegId; }
    public String getAdjNom() { return adjNom; }
    public void setAdjNom(String adjNom) { this.adjNom = adjNom; }
    public String getAdjTip() { return adjTip; }
    public void setAdjTip(String adjTip) { this.adjTip = adjTip; }
    public String getAdjNomArc() { return adjNomArc; }
    public void setAdjNomArc(String adjNomArc) { this.adjNomArc = adjNomArc; }
    public String getAdjRutRel() { return adjRutRel; }
    public void setAdjRutRel(String adjRutRel) { this.adjRutRel = adjRutRel; }
    public String getAdjMime() { return adjMime; }
    public void setAdjMime(String adjMime) { this.adjMime = adjMime; }
    public Long getAdjTam() { return adjTam; }
    public void setAdjTam(Long adjTam) { this.adjTam = adjTam; }
    public String getAdjUsuMov() { return adjUsuMov; }
    public void setAdjUsuMov(String adjUsuMov) { this.adjUsuMov = adjUsuMov; }
    public LocalDateTime getAdjFecMov() { return adjFecMov; }
    public void setAdjFecMov(LocalDateTime adjFecMov) { this.adjFecMov = adjFecMov; }
    public Boolean getAdjAct() { return adjAct; }
    public void setAdjAct(Boolean adjAct) { this.adjAct = adjAct; }
    public Boolean getAdjPri() { return adjPri; }
    public void setAdjPri(Boolean adjPri) { this.adjPri = adjPri; }
}
