package com.jbrempresa.backend.exception;

import java.time.Instant;
import java.util.Map;

// Representa una respuesta de error de la API.
public record ApiError(
        Instant fecha,
        int estado,
        String codigo,
        String mensaje,
        String ruta,
        String referencia,
        Map<String, String> campos) {

}
