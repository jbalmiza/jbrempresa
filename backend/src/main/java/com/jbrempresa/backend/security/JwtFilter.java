// Define el paquete.
package com.jbrempresa.backend.security;

// Importa IOException.
import java.io.IOException;

// Importa Autowired.
import org.springframework.beans.factory.annotation.Autowired;

// Importa UsernamePasswordAuthenticationToken.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// Importa SecurityContextHolder.
import org.springframework.security.core.context.SecurityContextHolder;

// Importa UserDetails.
import org.springframework.security.core.userdetails.UserDetails;

// Importa WebAuthenticationDetailsSource.
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// Importa Component.
import org.springframework.stereotype.Component;

// Importa OncePerRequestFilter.
import org.springframework.web.filter.OncePerRequestFilter;

// Importa ExpiredJwtException.
import io.jsonwebtoken.ExpiredJwtException;

// Importa FilterChain.
import jakarta.servlet.FilterChain;

// Importa ServletException.
import jakarta.servlet.ServletException;

// Importa HttpServletRequest.
import jakarta.servlet.http.HttpServletRequest;

// Importa HttpServletResponse.
import jakarta.servlet.http.HttpServletResponse;

// Define el filtro JWT.
@Component
public class JwtFilter extends OncePerRequestFilter {

    // Servicio JWT.
    @Autowired
    private JwtService jwtService;

    // Servicio de usuarios.
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Filtra las peticiones.
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene la cabecera Authorization.
        String authHeader =
                request.getHeader("Authorization");

        // Token JWT.
        String token = null;

        // Usuario del token.
        String username = null;

        // Comprueba la cabecera.
        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            // Obtiene el token.
            token = authHeader.substring(7);

            try {

                // Obtiene el usuario.
                username =
                        jwtService.obtenerUsuario(token);

            }

            // El token ha expirado.
            catch (ExpiredJwtException e) {

                SecurityContextHolder.clearContext();

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);

                return;

            }

            // Error en el token.
            catch (Exception e) {

                SecurityContextHolder.clearContext();

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);

                return;

            }

        }

        // Comprueba si el usuario no está autenticado.
        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            // Carga el usuario.
            UserDetails userDetails =
                    customUserDetailsService
                            .loadUserByUsername(username);

            // Crea la autenticación.
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            // Añade los detalles.
            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            // Guarda la autenticación.
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);

        }

        // Continúa la petición.
        filterChain.doFilter(
                request,
                response);

    }

}