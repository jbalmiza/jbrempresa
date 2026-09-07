package com.jbrempresa.backend.dto;
import jakarta.validation.constraints.NotBlank;
public record LoginRequest(@NotBlank String usuUsu,@NotBlank String usuCon) {}
