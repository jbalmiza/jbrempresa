package com.jbrempresa.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RespuestaRecuperacionContrasena(String mensaje, String tokenDesarrollo) {}
