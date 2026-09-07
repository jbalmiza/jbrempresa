package com.jbrempresa.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jbrempresa.backend.core.security.RecuperacionContrasenaService;
import com.jbrempresa.backend.dto.ConfirmacionRecuperacionContrasena;
import com.jbrempresa.backend.dto.RespuestaRecuperacionContrasena;
import com.jbrempresa.backend.dto.SolicitudRecuperacionContrasena;

@RestController
@RequestMapping("/auth/password")
public class RecuperacionContrasenaController {

    private final RecuperacionContrasenaService service;

    public RecuperacionContrasenaController(RecuperacionContrasenaService service) {
        this.service = service;
    }

    @PostMapping("/solicitar")
    public RespuestaRecuperacionContrasena solicitar(
            @RequestBody SolicitudRecuperacionContrasena solicitud) {
        return service.solicitar(solicitud.usuario(), solicitud.correo());
    }

    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmar(
            @RequestBody ConfirmacionRecuperacionContrasena confirmacion) {
        service.confirmar(
                confirmacion.token(), confirmacion.contrasena(), confirmacion.confirmacion());
        return ResponseEntity.noContent().build();
    }
}
