package com.jbrempresa.backend.security;

// Importa la clase List para crear listas de elementos.
import java.util.List;

// Permite que Spring inyecte automáticamente objetos.

import org.springframework.beans.factory.annotation.Value;

// Permite declarar métodos que crean objetos gestionados por Spring.
import org.springframework.context.annotation.Bean;

// Marca esta clase como clase de configuración de Spring.
import org.springframework.context.annotation.Configuration;

// Gestiona la autenticación de usuarios.
import org.springframework.security.authentication.AuthenticationManager;

// Permite obtener la configuración de autenticación de Spring.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

// Permite configurar la seguridad HTTP.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// Activa Spring Security.
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// Permite definir el tipo de gestión de sesiones.
import org.springframework.security.config.http.SessionCreationPolicy;

// Codificador BCrypt para contraseñas.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Interfaz general para codificar contraseñas.
import org.springframework.security.crypto.password.PasswordEncoder;

// Cadena principal de filtros de seguridad.
import org.springframework.security.web.SecurityFilterChain;

// Filtro estándar de autenticación por usuario y contraseña.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Configuración CORS.
import org.springframework.web.cors.CorsConfiguration;

// Interfaz para proporcionar configuración CORS.
import org.springframework.web.cors.CorsConfigurationSource;

// Implementación para registrar configuraciones CORS.
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Indica que esta clase contiene configuración de Spring.
@Configuration

// Activa Spring Security.
@EnableWebSecurity
public class SecurityConfig {

    // Origen permitido por CORS, configurable sin cambiar el código.
    @Value("${CORS_ALLOWED_ORIGIN:http://localhost:4200,http://127.0.0.1:4200,http://192.168.*:4200,http://10.*:4200,http://172.*:4200}")
    private String corsAllowedOrigin;

    // Inyecta automáticamente el filtro JWT.
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Define la configuración principal de seguridad.
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http)
            throws Exception {

        // Comienza la configuración de seguridad.
        http

                // Desactiva la protección CSRF.
                .csrf(csrf -> csrf.disable())

                // Activa la configuración CORS definida más abajo.
                .cors(cors -> {
                })

                // Configura el uso sin sesiones.
                .sessionManagement(session ->

                        // Indica que cada petición será independiente.
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                // Configura permisos sobre las URLs.
                .authorizeHttpRequests(auth -> auth

                        // Permite acceder libremente al login.
                        .requestMatchers(
                                "/usuarios/login",
                                "/auth/password/**",
                                "/catalogo/publico/**",
                                "/webhooks/meta/whatsapp",
                                "/webhooks/twilio/whatsapp")
                        .permitAll()

                        // Obliga a autenticarse para el resto.
                        .anyRequest()
                        .authenticated()

                )

                // Inserta el filtro JWT antes del filtro estándar.
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        // Devuelve la configuración construida.
        return http.build();

    }

    // Define la configuración global CORS.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        // Crea una configuración CORS vacía.
        CorsConfiguration configuration =
                new CorsConfiguration();

        // Permite peticiones desde Angular.
        configuration.setAllowedOriginPatterns(
                List.of(corsAllowedOrigin.split("\\s*,\\s*")));
        
        // Permite enviar credenciales y cabeceras Authorization.
        configuration.setAllowCredentials(true);

        // Permite estos métodos HTTP.
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"));

        // Permite todas las cabeceras.
        configuration.setAllowedHeaders(
                List.of("*"));

        // Permite que Angular lea el token renovado.
        configuration.setExposedHeaders(
                List.of("X-Refresh-Token"));

        // Crea el objeto que almacenará la configuración.
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // Aplica la configuración a todas las rutas.
        source.registerCorsConfiguration(
                "/**",
                configuration);

        // Devuelve la configuración CORS.
        return source;

    }

    // Crea el AuthenticationManager de Spring.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        // Devuelve el AuthenticationManager configurado.
        return config.getAuthenticationManager();

    }

    // Define el codificador de contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {

        // Utiliza BCrypt para cifrar contraseñas.
        return new BCryptPasswordEncoder();

    }

}
