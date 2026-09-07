// Define el paquete.
package com.jbrempresa.backend.security;

// Importa StandardCharsets.
import java.nio.charset.StandardCharsets;

// Importa Value.
import org.springframework.beans.factory.annotation.Value;

// Importa Service.
import org.springframework.stereotype.Service;

// Importa Claims.
import io.jsonwebtoken.Claims;

// Importa Jwts.
import io.jsonwebtoken.Jwts;

// Importa Keys.
import io.jsonwebtoken.security.Keys;

// Importa Date.
import java.util.Date;

// Importa SecretKey.
import javax.crypto.SecretKey;

// Importa PostConstruct.
import jakarta.annotation.PostConstruct;

// Define el servicio.
@Service
public class JwtService {

    // Duración del token en milisegundos.
    private static final long DURACION_TOKEN =
            1000 * 60 * 30;

    // Tiempo restante para renovar el token en milisegundos.
    private static final long VENTANA_RENOVACION =
            1000 * 60 * 5;

    // Clave secreta obtenida desde la configuracion externa.
    @Value("${JWT_SECRET}")
    private String claveSecreta;

    // Valida la clave al arrancar la aplicacion.
    @PostConstruct
    private void validarClaveSecreta() {

        obtenerClave();

    }

    // Obtiene la clave.
    private SecretKey obtenerClave() {

        // Comprueba que se ha configurado una clave segura.
        if (claveSecreta == null ||
                claveSecreta.isBlank() ||
                claveSecreta.getBytes(StandardCharsets.UTF_8).length < 32) {

            throw new IllegalStateException(
                    "JWT_SECRET debe tener al menos 32 bytes.");

        }

        return Keys.hmacShaKeyFor(
                claveSecreta.getBytes(
                        StandardCharsets.UTF_8));

    }

    // Genera un token.
    public String generarToken(
            String usuario,
            Long usuarioId,
            Long empresaId,
            Long perfilId) {

        return Jwts.builder()

                // Usuario.
                .subject(usuario)

                // Identificador del usuario.
                .claim("usuarioId", usuarioId)

                // Identificador del cliente.
                .claim("empresaId", empresaId)

                // Identificador del perfil.
                .claim("perfilId", perfilId)

                // Fecha de creación.
                .issuedAt(new Date())

                // Fecha de expiración.
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + DURACION_TOKEN))

                // Firma el token.
                .signWith(obtenerClave())

                .compact();

    }

    // Obtiene los datos del token.
    private Claims obtenerClaims(
            String token) {

        return Jwts
                .parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    // Obtiene el usuario.
    public String obtenerUsuario(
            String token) {

        return obtenerClaims(token)
                .getSubject();

    }

    // Obtiene el identificador del usuario.
    public Long obtenerUsuarioId(
            String token) {

        return obtenerClaims(token)
                .get("usuarioId", Long.class);

    }

    // Obtiene el identificador del cliente.
    public Long obtenerEmpresa(
            String token) {

        return obtenerClaims(token)
                .get("empresaId", Long.class);

    }

    // Obtiene el identificador del perfil.
    public Long obtenerPerfil(
            String token) {

        return obtenerClaims(token)
                .get("perfilId", Long.class);

    }

    // Comprueba si el token debe renovarse.
    public boolean debeRenovarse(
            String token) {

        // Obtiene la fecha de expiración.
        Date expiracion = obtenerClaims(token)
                .getExpiration();

        // Calcula el tiempo restante.
        long tiempoRestante =
                expiracion.getTime()
                - System.currentTimeMillis();

        // Renueva solo cuando quedan cinco minutos o menos.
        return tiempoRestante <= VENTANA_RENOVACION;

    }

}
