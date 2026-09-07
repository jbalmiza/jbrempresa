package com.jbrempresa.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "parametros", uniqueConstraints = @UniqueConstraint(
        name = "uk_parametros_cliente_modulo_codigo",
        columnNames = {"emp_id", "par_mod", "par_cod"}))
public class Parametro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "par_id")
    private Long parId;

    @Column(name = "emp_id", nullable = false)
    private Long empId;

    @Column(name = "par_cod", nullable = false, length = 100)
    private String parCod;

    @Column(name = "par_mod", nullable = false, length = 50)
    private String parMod;

    @Column(name = "par_des", nullable = false, length = 150)
    private String parDes;

    @Column(name = "par_val", nullable = false, length = 2000)
    private String parVal;

    @Column(name = "par_usu_mov", nullable = false, length = 50)
    private String parUsuMov;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "par_fec_mov", nullable = false)
    private LocalDateTime parFecMov;

    @Column(name = "par_act", nullable = false)
    private Boolean parAct;

    public Parametro() {}
    public Long getParId() { return parId; }
    public void setParId(Long parId) { this.parId = parId; }
    public Long getEmpId() { return empId; }
    public void setEmpId(Long empId) { this.empId = empId; }
    public String getParCod() { return parCod; }
    public void setParCod(String parCod) { this.parCod = parCod; }
    public String getParMod() { return parMod; }
    public void setParMod(String parMod) { this.parMod = parMod; }
    public String getParDes() { return parDes; }
    public void setParDes(String parDes) { this.parDes = parDes; }
    public String getParVal() { return parVal; }
    public void setParVal(String parVal) { this.parVal = parVal; }
    public String getParUsuMov() { return parUsuMov; }
    public void setParUsuMov(String parUsuMov) { this.parUsuMov = parUsuMov; }
    public LocalDateTime getParFecMov() { return parFecMov; }
    public void setParFecMov(LocalDateTime parFecMov) { this.parFecMov = parFecMov; }
    public Boolean getParAct() { return parAct; }
    public void setParAct(Boolean parAct) { this.parAct = parAct; }
}
