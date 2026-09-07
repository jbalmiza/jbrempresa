package com.jbrempresa.backend.dto;

public record ConfirmacionRecuperacionContrasena(
        String token,
        String contrasena,
        String confirmacion) {}
