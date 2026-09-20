package com.jbrempresa.backend.dto.agenda;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public final class AgendaEmpleadoDtos {
    private AgendaEmpleadoDtos() {}

    public record AgendaEmpleadoSalida(Long ragId, String empleado, java.util.List<TareaEmpleadoSalida> tareas) {}
    public record TareaEmpleadoSalida(Long tarId, Long resId, Long pedidoId, Long dvdId, Boolean pagado, String tipo, String habilidad,
            String titulo, Integer cantidad, Integer duracionUnitaria, Integer duracionTotal, String estado,
            LocalDateTime inicioPrevisto, LocalDateTime finPrevisto, LocalDateTime inicioReal, LocalDateTime finReal) {}
    public record EstadoTareaEntrada(@NotNull @Pattern(regexp = "PENDIENTE|EN_CURSO|FINALIZADO") String estado) {}
    public record PagoTareaEntrada(@NotNull Boolean pagado) {}
}
