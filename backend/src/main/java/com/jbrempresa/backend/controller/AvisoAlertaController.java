package com.jbrempresa.backend.controller;
import java.util.List; import org.springframework.web.bind.annotation.*;
import com.jbrempresa.backend.core.context.ContextoOperacion; import com.jbrempresa.backend.entity.AvisoAlerta; import com.jbrempresa.backend.service.AvisoAlertaService; import com.jbrempresa.backend.service.AvisoAlertaDistribucionService;
@RestController @RequestMapping("/avisos-alertas") @CrossOrigin(origins="http://localhost:4200")
public class AvisoAlertaController {
 private final AvisoAlertaService service; private final AvisoAlertaDistribucionService distribucion; private final ContextoOperacion contexto;
 public AvisoAlertaController(AvisoAlertaService s,AvisoAlertaDistribucionService d,ContextoOperacion c){service=s;distribucion=d;contexto=c;}
 @GetMapping public List<AvisoAlerta> consultar(){Long empresa=contexto.empresaConsulta(null);return empresa==null?service.consultarGlobal():service.consultar(empresa);}
 @GetMapping("/siguiente-id") public Long siguienteId(){return service.siguienteId(contexto.empresaId());}
 @PostMapping public AvisoAlerta crear(@RequestBody AvisoAlerta a){return service.crear(contexto.empresaId(),contexto.nombreUsuario(),a);}
 @PutMapping("/{id}") public AvisoAlerta actualizar(@PathVariable Long id,@RequestBody AvisoAlerta a){return service.actualizar(contexto.empresaId(),id,contexto.nombreUsuario(),a);}
 @DeleteMapping("/{id}") public void eliminar(@PathVariable Long id){service.eliminar(contexto.empresaId(),id);}
 @PostMapping("/{id}/baja") public AvisoAlerta baja(@PathVariable Long id){return service.baja(contexto.empresaId(),id,contexto.nombreUsuario());}
 @PostMapping("/{id}/reactivar") public AvisoAlerta reactivar(@PathVariable Long id){return service.reactivar(contexto.empresaId(),id,contexto.nombreUsuario());}
 @GetMapping("/{id}/historico") public List<AvisoAlerta> historico(@PathVariable Long id){return service.historico(contexto.empresaId(),id);}
 @GetMapping("/bandeja") public List<AvisoAlertaDistribucionService.Entrega> bandeja(){return distribucion.bandeja();}
 @PostMapping("/bandeja/{empresa}/{id}/lectura") public void leer(@PathVariable Long empresa,@PathVariable Long id){distribucion.marcarLeido(empresa,id);}
 @GetMapping("/ventana") public List<AvisoAlertaDistribucionService.Entrega> ventana(@RequestParam String ruta){return distribucion.ventana(ruta);}
}
