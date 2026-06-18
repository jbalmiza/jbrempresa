package com.jbrempresa.backend.security;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

// Servicio JWT
@Service
public class JwtService {

    // Clave secreta para firmar los tokens
    private static final String CLAVE_SECRETA =
            "MiClaveSecretaSuperLargaParaGreenSaaS2026";

    private SecretKey obtenerClave() {

        return Keys.hmacShaKeyFor(
                CLAVE_SECRETA.getBytes());

    }

    // Genera un token JWT
    public String generarToken(
            String usuario,
    		Long usuarioId,
            Long clienteId,
            Long perfilId) {

        return Jwts.builder()

                // Usuario del token
        		.subject(usuario)

        		// ID del usuario
        		.claim("usuarioId", usuarioId)
        		
                // ID del cliente
                .claim("clienteId", clienteId)

                // ID del perfil
                .claim("perfilId", perfilId)

                // Fecha de creación
                .issuedAt(new Date())

                // Expira en 24 horas
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000 * 60 * 60 * 24))

                // Firma del token
                .signWith(obtenerClave())

                .compact();

    }

    private Claims obtenerClaims(String token) {

        return Jwts
                .parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    // Obtiene el usuario del token
    public String obtenerUsuario(String token) {

        return obtenerClaims(token)
                .getSubject();

    }

    // Obtiene el ID del usuario del token
    public Long obtenerUsuarioId(String token) {

        return obtenerClaims(token)
                .get("usuarioId", Long.class);

    }
    
    // Obtiene el ID del cliente del token
    public Long obtenerCliente(String token) {

        return obtenerClaims(token)
                .get("clienteId", Long.class);

    }

    // Obtiene el ID del perfil del token
    public Long obtenerPerfil(String token) {

        return obtenerClaims(token)
                .get("perfilId", Long.class);

    }

}