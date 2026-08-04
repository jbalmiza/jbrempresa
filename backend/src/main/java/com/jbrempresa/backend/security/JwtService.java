// Define el paquete.
package com.jbrempresa.backend.security;

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

// Define el servicio.
@Service
public class JwtService {

    // Clave secreta.
    private static final String CLAVE_SECRETA =
            "MiClaveSecretaSuperLargaParaGreenSaaS2026";

    // Obtiene la clave.
    private SecretKey obtenerClave() {

        return Keys.hmacShaKeyFor(
                CLAVE_SECRETA.getBytes());

    }

    // Genera un token.
    public String generarToken(
            String usuario,
            Long usuarioId,
            Long clienteId,
            Long perfilId) {

        return Jwts.builder()

                // Usuario.
                .subject(usuario)

                // Identificador del usuario.
                .claim("usuarioId", usuarioId)

                // Identificador del cliente.
                .claim("clienteId", clienteId)

                // Identificador del perfil.
                .claim("perfilId", perfilId)

                // Fecha de creación.
                .issuedAt(new Date())

                // Fecha de expiración.
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000 * 60 * 60 * 24))

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
    public Long obtenerCliente(
            String token) {

        return obtenerClaims(token)
                .get("clienteId", Long.class);

    }

    // Obtiene el identificador del perfil.
    public Long obtenerPerfil(
            String token) {

        return obtenerClaims(token)
                .get("perfilId", Long.class);

    }

}