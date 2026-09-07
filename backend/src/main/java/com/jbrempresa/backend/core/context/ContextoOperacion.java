package com.jbrempresa.backend.core.context;

import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.security.JwtUser;

@Service
public class ContextoOperacion {
    public JwtUser usuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUser usuario)) {
            throw new IllegalStateException("No existe un usuario autenticado.");
        }
        return usuario;
    }

    public Long empresaId() { return usuarioActual().getEmpresaId(); }
    public String nombreUsuario() { return usuarioActual().getUsername(); }
    public LocalDateTime fechaActual() { return LocalDateTime.now(); }
}
