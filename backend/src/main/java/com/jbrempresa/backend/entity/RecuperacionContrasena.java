package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "recuperaciones_contrasena")
public class RecuperacionContrasena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rec_id")
    private Long recId;

    @Column(name = "emp_id", nullable = false)
    private Long empId;

    @Column(name = "usu_id", nullable = false)
    private Long usuId;

    @Column(name = "rec_tok_hash", nullable = false, unique = true, length = 64)
    private String recTokHash;

    @Column(name = "rec_fec_cre", nullable = false)
    private LocalDateTime recFecCre;

    @Column(name = "rec_fec_exp", nullable = false)
    private LocalDateTime recFecExp;

    @Column(name = "rec_fec_uso")
    private LocalDateTime recFecUso;

    @Column(name = "rec_usu_mov", nullable = false, length = 100)
    private String recUsuMov;

    @Column(name = "rec_fec_mov", nullable = false)
    private LocalDateTime recFecMov;

    @Column(name = "rec_act", nullable = false)
    private Boolean recAct;

    public Long getRecId() { return recId; }
    public void setRecId(Long recId) { this.recId = recId; }
    public Long getEmpId() { return empId; }
    public void setEmpId(Long empId) { this.empId = empId; }
    public Long getUsuId() { return usuId; }
    public void setUsuId(Long usuId) { this.usuId = usuId; }
    public String getRecTokHash() { return recTokHash; }
    public void setRecTokHash(String recTokHash) { this.recTokHash = recTokHash; }
    public LocalDateTime getRecFecCre() { return recFecCre; }
    public void setRecFecCre(LocalDateTime recFecCre) { this.recFecCre = recFecCre; }
    public LocalDateTime getRecFecExp() { return recFecExp; }
    public void setRecFecExp(LocalDateTime recFecExp) { this.recFecExp = recFecExp; }
    public LocalDateTime getRecFecUso() { return recFecUso; }
    public void setRecFecUso(LocalDateTime recFecUso) { this.recFecUso = recFecUso; }
    public String getRecUsuMov() { return recUsuMov; }
    public void setRecUsuMov(String recUsuMov) { this.recUsuMov = recUsuMov; }
    public LocalDateTime getRecFecMov() { return recFecMov; }
    public void setRecFecMov(LocalDateTime recFecMov) { this.recFecMov = recFecMov; }
    public Boolean getRecAct() { return recAct; }
    public void setRecAct(Boolean recAct) { this.recAct = recAct; }
}
