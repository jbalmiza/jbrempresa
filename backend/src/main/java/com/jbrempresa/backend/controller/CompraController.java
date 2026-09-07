package com.jbrempresa.backend.controller;
import java.util.List;import org.springframework.web.bind.annotation.*;import com.jbrempresa.backend.core.context.ContextoOperacion;import com.jbrempresa.backend.dto.compras.CompraDtos;import com.jbrempresa.backend.entity.Compra;import com.jbrempresa.backend.exception.RecursoNoEncontradoException;import com.jbrempresa.backend.repository.CompraRepository;import jakarta.validation.Valid;
@RestController @RequestMapping("/compras")
public class CompraController{
 private final CompraRepository compras;private final ContextoOperacion contexto;
 public CompraController(CompraRepository compras,ContextoOperacion contexto){this.compras=compras;this.contexto=contexto;}
 @PostMapping("/{empId}") public CompraDtos.Salida guardar(@PathVariable Long empId,@Valid @RequestBody CompraDtos.Entrada entrada){validarRuta(empId);Compra v=entrada.entidad();v.setEmpId(contexto.empresaId());v.setComUsuMov(contexto.nombreUsuario());v.setComFecMov(contexto.fechaActual());v.setComAct(true);return CompraDtos.Salida.desde(compras.save(v));}
 @PutMapping("/{empId}/{id}") public CompraDtos.Salida actualizar(@PathVariable Long empId,@PathVariable Long id,@Valid @RequestBody CompraDtos.Entrada entrada){validarRuta(empId);Compra actual=obtener(id);Compra v=entrada.entidad();v.setComId(actual.getComId());v.setEmpId(contexto.empresaId());v.setComUsuMov(contexto.nombreUsuario());v.setComFecMov(contexto.fechaActual());v.setComAct(actual.getComAct());return CompraDtos.Salida.desde(compras.save(v));}
 @GetMapping("/{empId}") public List<CompraDtos.Salida> consultar(@PathVariable Long empId){validarRuta(empId);return compras.findByEmpId(contexto.empresaId()).stream().map(compra->CompraDtos.Salida.desde(compra)).toList();}
 @GetMapping("/siguiente-id") public Long siguienteId(){return compras.obtenerSiguienteId(contexto.empresaId());}
 @DeleteMapping("/{empId}/{id}") public void eliminar(@PathVariable Long empId,@PathVariable Long id){validarRuta(empId);compras.delete(obtener(id));}
 private Compra obtener(Long id){return compras.findByEmpIdAndComId(contexto.empresaId(),id).orElseThrow(()->new RecursoNoEncontradoException("Compra no encontrada."));}
 private void validarRuta(Long empresa){if(!contexto.empresaId().equals(empresa))throw new org.springframework.security.access.AccessDeniedException("Empresa no autorizado.");}
}
