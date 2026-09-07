package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "areas_organizativas")
public class AreaOrganizativa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "are_id") private Long areId;
    @Column(name = "emp_id", nullable = false) private Long empId;
    @Column(name = "are_cod", nullable = false, length = 30) private String areCod;
    @Column(name = "are_nom", nullable = false, length = 100) private String areNom;
    @Column(name = "are_id_pad") private Long areIdPad;
    @Column(name = "are_usu_mov", nullable = false, length = 50) private String areUsuMov;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "are_fec_mov", nullable = false) private LocalDateTime areFecMov;
    @Column(name = "are_act", nullable = false) private Boolean areAct;

    public Long getAreId() { return areId; } public void setAreId(Long v) { areId=v; }
    public Long getEmpId() { return empId; } public void setEmpId(Long v) { empId=v; }
    public String getAreCod() { return areCod; } public void setAreCod(String v) { areCod=v; }
    public String getAreNom() { return areNom; } public void setAreNom(String v) { areNom=v; }
    public Long getAreIdPad() { return areIdPad; } public void setAreIdPad(Long v) { areIdPad=v; }
    public String getAreUsuMov() { return areUsuMov; } public void setAreUsuMov(String v) { areUsuMov=v; }
    public LocalDateTime getAreFecMov() { return areFecMov; } public void setAreFecMov(LocalDateTime v) { areFecMov=v; }
    public Boolean getAreAct() { return areAct; } public void setAreAct(Boolean v) { areAct=v; }
}
