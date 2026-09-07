package com.jbrempresa.backend.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacionDto(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String,String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return respuesta(HttpStatus.BAD_REQUEST, "VALIDACION", "Revise los campos indicados.", request, campos, null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> validacionParametros(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String,String> campos = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(e -> campos.put(e.getPropertyPath().toString(), e.getMessage()));
        return respuesta(HttpStatus.BAD_REQUEST, "VALIDACION", "Revise los campos indicados.", request, campos, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> jsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, "JSON_INVALIDO", "El formato de la solicitud no es válido.", request, Map.of(), null);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", ex.getMessage(), request, Map.of(), null);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> negocio(ReglaNegocioException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, ex.getCodigo(), ex.getMessage(), request, Map.of(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> argumento(IllegalArgumentException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, "SOLICITUD_INVALIDA", ex.getMessage(), request, Map.of(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> integridad(DataIntegrityViolationException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.CONFLICT, "INTEGRIDAD_DATOS", "La operación incumple una restricción de datos.", request, Map.of(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> prohibido(AccessDeniedException ex, HttpServletRequest request) {
        return respuesta(HttpStatus.FORBIDDEN, "ACCESO_DENEGADO", "No tiene permiso para realizar esta operación.", request, Map.of(), null);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> estado(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus estado = HttpStatus.valueOf(ex.getStatusCode().value());
        return respuesta(estado, "SOLICITUD_INVALIDA", ex.getReason(), request, Map.of(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> legado(RuntimeException ex, HttpServletRequest request) {
        String mensaje = ex.getMessage() == null ? "No se pudo completar la operación." : ex.getMessage();
        if (mensaje.toLowerCase().contains("no encontrad")) {
            return respuesta(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", mensaje, request, Map.of(), null);
        }
        if (esMensajeNegocio(ex)) {
            return respuesta(HttpStatus.BAD_REQUEST, "REGLA_NEGOCIO", mensaje, request, Map.of(), null);
        }
        return errorInterno(ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> interno(Exception ex, HttpServletRequest request) {
        return errorInterno(ex, request);
    }

    private ResponseEntity<ApiError> errorInterno(Exception ex, HttpServletRequest request) {
        String referencia = UUID.randomUUID().toString();
        log.error("Error interno. Referencia {}", referencia, ex);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO",
                "Error interno del servidor. Referencia: " + referencia, request, Map.of(), referencia);
    }

    private boolean esMensajeNegocio(RuntimeException ex) {
        // Compatibilidad temporal con servicios antiguos que todavía lanzan
        // RuntimeException de forma deliberada. Nunca se devuelve el texto de
        // excepciones de infraestructura, Hibernate o JDBC al navegador.
        return ex.getClass().equals(RuntimeException.class)
                || ex.getClass().equals(IllegalStateException.class);
    }

    private ResponseEntity<ApiError> respuesta(HttpStatus estado, String codigo, String mensaje,
            HttpServletRequest request, Map<String,String> campos, String referencia) {
        return ResponseEntity.status(estado).body(new ApiError(Instant.now(), estado.value(), codigo,
                mensaje == null ? estado.getReasonPhrase() : mensaje, request.getRequestURI(), referencia, campos));
    }
}
