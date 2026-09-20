package com.jbrempresa.backend.controller;

import static com.jbrempresa.backend.dto.agenda.AgendaEmpleadoDtos.*;
import org.springframework.web.bind.annotation.*;
import com.jbrempresa.backend.service.AgendaEmpleadoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/empleados/mi-agenda")
public class AgendaEmpleadoController {
    private final AgendaEmpleadoService agenda;
    public AgendaEmpleadoController(AgendaEmpleadoService agenda) { this.agenda = agenda; }
    @GetMapping public AgendaEmpleadoSalida consultar(@RequestParam(required = false) Long recursoAgendaId) {
        return agenda.consultar(recursoAgendaId);
    }
    @PutMapping("/tareas/{id}/estado")
    public TareaEmpleadoSalida estado(@PathVariable Long id, @Valid @RequestBody EstadoTareaEntrada entrada) {
        return agenda.cambiarEstado(id, entrada.estado());
    }
    @PutMapping("/tareas/{id}/pagado") public TareaEmpleadoSalida pagado(@PathVariable Long id, @Valid @RequestBody PagoTareaEntrada entrada) { return agenda.marcarPagado(id, entrada.pagado()); }
}
