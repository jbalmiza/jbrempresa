package com.jbrempresa.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.jbrempresa.backend.entity.Usuario;
import com.jbrempresa.backend.repository.UsuarioRepository;

import java.util.Collections;

// Servicio de usuarios para Spring Security
@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    // Acceso a usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Busca un usuario por su nombre
    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        Usuario usuario =
                usuarioRepository
                        .findByUsuUsu(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuario no encontrado"));

        return new User(

                usuario.getUsuUsu(),
                usuario.getUsuCon(),

                Collections.emptyList()

        );

    }

}