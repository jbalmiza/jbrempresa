
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

}
