package com.jbrempresa.backend.controller;
import static com.jbrempresa.backend.dto.agenda.AgendaDtos.*;import java.time.*;import java.util.List;import org.springframework.format.annotation.DateTimeFormat;import org.springframework.web.bind.annotation.*;import com.jbrempresa.backend.service.AgendaService;import jakarta.validation.Valid;
@RestController @RequestMapping("/agenda") @CrossOrigin(origins="http://localhost:4200")
public class AgendaController{
 private final AgendaService agenda;public AgendaController(AgendaService agenda){this.agenda=agenda;}
 @GetMapping("/recursos")public List<RecursoSalida> recursos(){return agenda.recursos();}
 @PostMapping("/recursos")public RecursoSalida guardarRecurso(@Valid @RequestBody RecursoEntrada entrada){return agenda.guardarRecurso(entrada);}
 @GetMapping("/recursos/{id}/horarios")public List<HorarioSalida> horarios(@PathVariable Long id){return agenda.horarios(id);}
 @PutMapping("/recursos/{id}/horarios")public List<HorarioSalida> horarios(@PathVariable Long id,@Valid @RequestBody List<HorarioEntrada> entrada){return agenda.reemplazarHorarios(id,entrada);}
 @GetMapping("/recursos/{id}/excepciones")public List<ExcepcionSalida> excepciones(@PathVariable Long id,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate desde,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate hasta){return agenda.excepciones(id,desde,hasta);}
 @PostMapping("/recursos/{id}/excepciones")public ExcepcionSalida excepcion(@PathVariable Long id,@Valid @RequestBody ExcepcionEntrada entrada){return agenda.guardarExcepcion(id,entrada);}
 @DeleteMapping("/excepciones/{id}")public void eliminarExcepcion(@PathVariable Long id){agenda.eliminarExcepcion(id);}
 @GetMapping("/reservas")public List<ReservaSalida> reservas(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)LocalDateTime desde,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)LocalDateTime hasta){return agenda.reservas(desde,hasta);}
 @PostMapping("/reservas")public ReservaSalida reservar(@Valid @RequestBody ReservaEntrada entrada){return agenda.crearReserva(entrada);}
 @PostMapping("/reservas/{id}/reprogramar")public ReservaSalida reprogramar(@PathVariable Long id,@Valid @RequestBody ReprogramacionEntrada entrada){return agenda.reprogramar(id,entrada);}
 @PutMapping("/reservas/{id}/estado")public ReservaSalida estado(@PathVariable Long id,@Valid @RequestBody EstadoEntrada entrada){return agenda.cambiarEstado(id,entrada.estado());}
 @GetMapping("/reservas/{id}/reprogramaciones")public List<ReprogramacionSalida> historial(@PathVariable Long id){return agenda.historial(id);}
}
