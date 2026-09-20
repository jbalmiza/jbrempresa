package com.jbrempresa.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "empresas_movimientos")
public class EmpresaMovimiento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emm_id") private Long id;
    @Column(name = "emp_id", nullable = false) private Long empresaId;
    @Column(name = "emm_tip", nullable = false, length = 1) private String tipo;
    @Column(name = "emm_cau", nullable = false, length = 500) private String causa;
    @Column(name = "emm_usu", nullable = false, length = 100) private String usuario;
    @Column(name = "emm_fec", nullable = false) private LocalDateTime fecha;
    @Column(name = "emm_act", nullable = false) private Boolean activo;

    public Long getId() { return id; }
    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCausa() { return causa; }
    public void setCausa(String causa) { this.causa = causa; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
