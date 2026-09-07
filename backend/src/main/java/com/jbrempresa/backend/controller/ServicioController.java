package com.jbrempresa.backend.controller;
import com.jbrempresa.backend.entity.Servicio;
import com.jbrempresa.backend.security.JwtUser;
import com.jbrempresa.backend.service.ServicioService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;
@RestController @RequestMapping("/servicios") @CrossOrigin(origins="http://localhost:4200")
public class ServicioController {
 private final ServicioService service;
 private final com.jbrempresa.backend.service.ImagenService imagenes;
 public ServicioController(ServicioService service,com.jbrempresa.backend.service.ImagenService imagenes){this.service=service;this.imagenes=imagenes;}
 @GetMapping public List<Servicio> consultar(){return service.consultar(usuario().getEmpresaId());}
 @GetMapping("/siguiente-id") public Long siguienteId(){return service.siguienteId(usuario().getEmpresaId());}
 @PostMapping public Servicio guardar(@RequestBody Servicio s){validar(s);return service.guardar(usuario().getEmpresaId(),usuario().getUsername(),s);}
 @PutMapping("/{id}") public Servicio actualizar(@PathVariable Long id,@RequestBody Servicio s){validar(s);s.setSerId(id);return service.actualizar(usuario().getEmpresaId(),usuario().getUsername(),s);}
 @DeleteMapping("/{id}") public void eliminar(@PathVariable Long id){service.eliminar(usuario().getEmpresaId(),id);}
 @PostMapping("/{id}/baja") public Servicio baja(@PathVariable Long id){return service.baja(usuario().getEmpresaId(),id,usuario().getUsername());}
 @GetMapping("/{id}/historico") public List<Servicio> historico(@PathVariable Long id){return service.historico(usuario().getEmpresaId(),id);}
 @PostMapping("/{id}/deshacer") public Servicio deshacer(@PathVariable Long id){return service.deshacer(usuario().getEmpresaId(),id);}
 @PostMapping("/{id}/imagen") public Servicio subirImagen(@PathVariable Long id,@RequestParam("archivo") MultipartFile archivo){String ruta=imagenes.guardar(usuario().getEmpresaId(),"SERVICIOS","servicios","servicio-"+id,archivo);return service.asociarImagen(usuario().getEmpresaId(),id,ruta);}
 @GetMapping("/{id}/imagen") public ResponseEntity<Resource> obtenerImagen(@PathVariable Long id){Servicio s=service.obtener(usuario().getEmpresaId(),id);return imagen(imagenes.cargar(usuario().getEmpresaId(),"SERVICIOS","servicios",s.getSerIma()));}
 private ResponseEntity<Resource> imagen(Resource r){String n=r.getFilename()==null?"":r.getFilename().toLowerCase();MediaType t=n.endsWith(".png")?MediaType.IMAGE_PNG:n.endsWith(".webp")?MediaType.parseMediaType("image/webp"):MediaType.IMAGE_JPEG;return ResponseEntity.ok().contentType(t).cacheControl(CacheControl.noCache()).body(r);}
 private JwtUser usuario(){return (JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();}
 private void validar(Servicio s){if(vacio(s.getSerTipSer())||vacio(s.getSerNom())||vacio(s.getSerCat())||s.getSerDurMin()==null||s.getSerDurMin()<=0||s.getSerDurMin()%5!=0||s.getSerPreVen()==null||s.getSerPreIva()==null)throw new IllegalArgumentException("Tipo, nombre, categoría, duración múltiplo de 5, precio e IVA son obligatorios.");}
 private boolean vacio(String s){return s==null||s.isBlank();}
}
