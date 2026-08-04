// Define el paquete.
package com.jbrempresa.backend.security;

// Importa Collection.
import java.util.Collection;

// Importa Collections.
import java.util.Collections;

// Importa GrantedAuthority.
import org.springframework.security.core.GrantedAuthority;

// Importa UserDetails.
import org.springframework.security.core.userdetails.UserDetails;

// Define el usuario autenticado.
public class JwtUser implements UserDetails {

    // Nombre del usuario.
    private String usuario;

    // Contraseña.
    private String contrasena;

    // Identificador del usuario.
    private Long usuarioId;

    // Identificador del cliente.
    private Long clienteId;

    // Identificador del perfil.
    private Long perfilId;

    // Constructor.
    public JwtUser(
            String usuario,
            String contrasena,
            Long usuarioId,
            Long clienteId,
            Long perfilId) {

        this.usuario = usuario;
        this.contrasena = contrasena;
        this.usuarioId = usuarioId;
        this.clienteId = clienteId;
        this.perfilId = perfilId;

    }

    // Obtiene el identificador del usuario.
    public Long getUsuarioId() {

        return usuarioId;

    }

    // Obtiene el identificador del cliente.
    public Long getClienteId() {

        return clienteId;

    }

    // Obtiene el identificador del perfil.
    public Long getPerfilId() {

        return perfilId;

    }

    // Obtiene los permisos.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.emptyList();

    }

    // Obtiene la contraseña.
    @Override
    public String getPassword() {

        return contrasena;

    }

    // Obtiene el usuario.
    @Override
    public String getUsername() {

        return usuario;

    }

    // Comprueba si la cuenta no ha expirado.
    @Override
    public boolean isAccountNonExpired() {

        return true;

    }

    // Comprueba si la cuenta no está bloqueada.
    @Override
    public boolean isAccountNonLocked() {

        return true;

    }

    // Comprueba si las credenciales no han expirado.
    @Override
    public boolean isCredentialsNonExpired() {

        return true;

    }

    // Comprueba si la cuenta está activa.
    @Override
    public boolean isEnabled() {

        return true;

    }

}