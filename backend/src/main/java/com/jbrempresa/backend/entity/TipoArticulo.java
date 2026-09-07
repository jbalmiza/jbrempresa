package com.jbrempresa.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tipos_articulo", uniqueConstraints = @UniqueConstraint(name = "uk_tipo_articulo", columnNames = {"emp_id", "tia_cla", "tia_nom"}))
public class TipoArticulo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "tia_id") private Long id;
    @Column(name = "emp_id", nullable = false) private Long empId;
    @Column(name = "tia_cla", nullable = false, length = 10) private String clase;
    @Column(name = "tia_nom", nullable = false, length = 80) private String nombre;
    @Column(name = "tia_ima", length = 500) private String imagen;
    @Column(name = "tia_usu_mov", nullable = false, length = 100) private String usuario;
    @Column(name = "tia_fec_mov", nullable = false) private LocalDateTime fecha;
    @Column(name = "tia_act", nullable = false) private Boolean activo;
    public Long getId() { return id; } public void setId(Long v) { id=v; }
    public Long getEmpId() { return empId; } public void setEmpId(Long v) { empId=v; }
    public String getClase() { return clase; } public void setClase(String v) { clase=v; }
    public String getNombre() { return nombre; } public void setNombre(String v) { nombre=v; }
    public String getImagen() { return imagen; } public void setImagen(String v) { imagen=v; }
    public String getUsuario() { return usuario; } public void setUsuario(String v) { usuario=v; }
    public LocalDateTime getFecha() { return fecha; } public void setFecha(LocalDateTime v) { fecha=v; }
    public Boolean getActivo() { return activo; } public void setActivo(Boolean v) { activo=v; }
}
