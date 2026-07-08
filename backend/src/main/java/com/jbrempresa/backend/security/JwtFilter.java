package com.jbrempresa.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filtro JWT
@Component
public class JwtFilter extends OncePerRequestFilter {

    // Servicio JWT
    @Autowired
    private JwtService jwtService;

    // Servicio de usuarios
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene la cabecera Authorization
        String authHeader =
                request.getHeader("Authorization");

        // Token JWT
        String token = null;

        // Nombre del usuario contenido en el token
        String username = null;

        // Comprueba si existe la cabecera Authorization
        // y comienza por Bearer
        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            // Extrae el token eliminando "Bearer "
            token = authHeader.substring(7);

            try {

                // Obtiene el usuario del token
                username =
                        jwtService.obtenerUsuario(token);

            }

            // Si el token ha expirado
            catch (ExpiredJwtException e) {

                // Limpia el contexto de seguridad
                SecurityContextHolder.clearContext();

                // Devuelve código 401 Unauthorized
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);

                return;

            }

            // Cualquier otro error relacionado con el token
            catch (Exception e) {

                // Limpia el contexto de seguridad
                SecurityContextHolder.clearContext();

                // Devuelve código 401 Unauthorized
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);

                return;

            }

        }

        // Si existe usuario y todavía no está autenticado
        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            // Carga los datos del usuario
            UserDetails userDetails =
                    customUserDetailsService
                            .loadUserByUsername(username);

            // Crea el objeto de autenticación
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            // Añade información de la petición
            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            // Guarda la autenticación en el contexto de seguridad
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);

        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);

    }

}