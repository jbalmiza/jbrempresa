
package com.jbrempresa.backend.entity;

//Importa las anotaciones JPA.
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

// Entidad JPA
@Entity

// Tabla empresas
@Table(name = "empresas")

// Clase Empresa
public class Empresa {

    // Constructor vacío
    public Empresa() {

    }

    // Clave primaria
    @Id

    // Genera automáticamente el ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "emp_id")
    private Long empId;

    // Nombre
    @Column(name = "emp_nom")
    private String empNom;

    @Column(name = "emp_ima", length = 500)
    private String empIma;

    @Column(name = "emp_raz_soc", length = 200)
    private String empRazSoc;
    @Column(name = "emp_nif", length = 30)
    private String empNif;
    @Column(name = "emp_act_eco", length = 200)
    private String empActEco;
    @Column(name = "emp_tel", length = 30)
    private String empTel;
    @Column(name = "emp_ema", length = 150)
    private String empEma;
    @Column(name = "emp_web", length = 250)
    private String empWeb;
    @Column(name = "dom_id")
    private Long domId;

    // Activo
    @Column(name = "emp_act")
    private String empAct;

    // Usuario de movimiento
    @Column(name = "emp_usu_mov")
    private String empUsuMov;

    // Fecha de movimiento
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "emp_fec_mov")
    private LocalDateTime empFecMov;

    @Column(name = "emp_tip_mov", length = 1)
    private String empTipMov;

    @Column(name = "emp_cau_mov", length = 500)
    private String empCauMov;

    // GETTERS Y SETTERS------------------------------------------------

    // Obtiene el ID
    public Long getEmpId() {
        return empId;
    }

    // Modifica el ID
    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    // Obtiene el nombre
    public String getEmpNom() {
        return empNom;
    }

    // Modifica el nombre
    public void setEmpNom(String empNom) {
        this.empNom = empNom;
    }
    public String getEmpIma() { return empIma; }
    public void setEmpIma(String empIma) { this.empIma = empIma; }
    public String getEmpRazSoc() { return empRazSoc; }
    public void setEmpRazSoc(String empRazSoc) { this.empRazSoc = empRazSoc; }
    public String getEmpNif() { return empNif; }
    public void setEmpNif(String empNif) { this.empNif = empNif; }
    public String getEmpActEco() { return empActEco; }
    public void setEmpActEco(String empActEco) { this.empActEco = empActEco; }
    public String getEmpTel() { return empTel; }
    public void setEmpTel(String empTel) { this.empTel = empTel; }
    public String getEmpEma() { return empEma; }
    public void setEmpEma(String empEma) { this.empEma = empEma; }
    public String getEmpWeb() { return empWeb; }
    public void setEmpWeb(String empWeb) { this.empWeb = empWeb; }
    public Long getDomId() { return domId; }
    public void setDomId(Long domId) { this.domId = domId; }

    // Obtiene el estado activo
    public String getEmpAct() {
        return empAct;
    }

    // Modifica el estado activo
    public void setEmpAct(String empAct) {
        this.empAct = empAct;
    }

    // Obtiene el usuario de movimiento
    public String getEmpUsuMov() {
        return empUsuMov;
    }

    // Modifica el usuario de movimiento
    public void setEmpUsuMov(String empUsuMov) {
        this.empUsuMov = empUsuMov;
    }

    // Obtiene la fecha de movimiento
    public LocalDateTime getEmpFecMov() {
        return empFecMov;
    }

    // Modifica la fecha de movimiento
    public void setEmpFecMov(LocalDateTime empFecMov) {
        this.empFecMov = empFecMov;
    }

    public String getEmpTipMov() { return empTipMov; }
    public void setEmpTipMov(String empTipMov) { this.empTipMov = empTipMov; }
    public String getEmpCauMov() { return empCauMov; }
    public void setEmpCauMov(String empCauMov) { this.empCauMov = empCauMov; }

}
