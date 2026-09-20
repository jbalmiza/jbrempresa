package com.jbrempresa.backend.core.context;

import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.security.JwtUser;
import com.jbrempresa.backend.repository.PerfilRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ContextoOperacion {
    private final PerfilRepository perfiles;
    private final HttpServletRequest request;
    public ContextoOperacion(PerfilRepository perfiles, HttpServletRequest request) { this.perfiles = perfiles; this.request = request; }
    public JwtUser usuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUser usuario)) {
            throw new IllegalStateException("No existe un usuario autenticado.");
        }
        return usuario;
    }

    public Long empresaId() {
        Long seleccionada = empresaSeleccionada();
        return seleccionada != null ? seleccionada : usuarioActual().getEmpresaId();
    }
    public String nombreUsuario() { return usuarioActual().getUsername(); }
    public LocalDateTime fechaActual() { return LocalDateTime.now(); }
    public boolean administradorGlobal() {
        JwtUser usuario = usuarioActual();
        return perfiles.findByEmpIdAndPerId(usuario.getEmpresaId(), usuario.getPerfilId())
                .map(p -> "Administrador".equalsIgnoreCase(p.getPerNom()))
                .orElse(false);
    }
    public boolean empleado() {
        JwtUser usuario = usuarioActual();
        return perfiles.findByEmpIdAndPerId(usuario.getEmpresaId(), usuario.getPerfilId())
                .map(p -> "Empleado".equalsIgnoreCase(p.getPerNom()))
                .orElse(false);
    }
    public Long empresaPermitida(Long solicitada) {
        if (!administradorGlobal()) return empresaId();
        return solicitada;
    }

    /**
     * Ámbito común de las consultas. Un valor nulo representa todas las empresas
     * y solo puede obtenerlo el Administrador global.
     */
    public Long empresaConsulta(Long solicitada) {
        if (!administradorGlobal()) return usuarioActual().getEmpresaId();
        if (solicitada != null && solicitada > 0) return solicitada;
        return empresaSeleccionada();
    }

    public boolean permiteEmpresa(Long empresa) {
        return administradorGlobal() || empresaId().equals(empresa);
    }

    public Long empresaSeleccionada() {
        if (!administradorGlobal()) return null;
        String valor = request.getHeader("X-Empresa-Seleccionada");
        if (valor == null || valor.isBlank() || "0".equals(valor)) return null;
        try {
            long empresa = Long.parseLong(valor);
            return empresa > 0 ? empresa : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
