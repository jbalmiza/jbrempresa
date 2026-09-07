package com.jbrempresa.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "personal_area")
public class PersonalArea {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pea_id") private Long peaId;
    @Column(name = "emp_id", nullable = false) private Long empId;
    @Column(name = "are_id", nullable = false) private Long areId;
    @Column(name = "per_id", nullable = false) private Long perId;
    @Column(name = "pea_car", length = 100) private String peaCar;
    @Column(name = "pea_res", nullable = false) private Boolean peaRes;
    @Column(name = "pea_pri", nullable = false) private Boolean peaPri;
    @Column(name = "pea_fec_des") private LocalDate peaFecDes;
    @Column(name = "pea_fec_has") private LocalDate peaFecHas;
    @Column(name = "pea_usu_mov", nullable = false, length = 50) private String peaUsuMov;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "pea_fec_mov", nullable = false) private LocalDateTime peaFecMov;
    @Column(name = "pea_act", nullable = false) private Boolean peaAct;

    public Long getPeaId() { return peaId; } public void setPeaId(Long v) { peaId=v; }
    public Long getEmpId() { return empId; } public void setEmpId(Long v) { empId=v; }
    public Long getAreId() { return areId; } public void setAreId(Long v) { areId=v; }
    public Long getPerId() { return perId; } public void setPerId(Long v) { perId=v; }
    public String getPeaCar() { return peaCar; } public void setPeaCar(String v) { peaCar=v; }
    public Boolean getPeaRes() { return peaRes; } public void setPeaRes(Boolean v) { peaRes=v; }
    public Boolean getPeaPri() { return peaPri; } public void setPeaPri(Boolean v) { peaPri=v; }
    public LocalDate getPeaFecDes() { return peaFecDes; } public void setPeaFecDes(LocalDate v) { peaFecDes=v; }
    public LocalDate getPeaFecHas() { return peaFecHas; } public void setPeaFecHas(LocalDate v) { peaFecHas=v; }
    public String getPeaUsuMov() { return peaUsuMov; } public void setPeaUsuMov(String v) { peaUsuMov=v; }
    public LocalDateTime getPeaFecMov() { return peaFecMov; } public void setPeaFecMov(LocalDateTime v) { peaFecMov=v; }
    public Boolean getPeaAct() { return peaAct; } public void setPeaAct(Boolean v) { peaAct=v; }
}
