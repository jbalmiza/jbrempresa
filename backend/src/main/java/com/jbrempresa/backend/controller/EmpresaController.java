// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa Autowired.

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;

// Importa Authentication.
import org.springframework.security.core.Authentication;

// Importa SecurityContextHolder.
import org.springframework.security.core.context.SecurityContextHolder;

// Importa Empresa.
import com.jbrempresa.backend.entity.Empresa;

// Importa EmpresaRepository.
import com.jbrempresa.backend.repository.EmpresaRepository;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Importa List.
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;
import com.jbrempresa.backend.service.ImagenService;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/empresas")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class EmpresaController {

    // Repositorio de empresas.
    private final EmpresaRepository empresaRepository;
    private final ImagenService imagenService;

    public EmpresaController(EmpresaRepository empresaRepository, ImagenService imagenService) {
        this.empresaRepository = empresaRepository;
        this.imagenService = imagenService;
    }

    // Obtiene el empresa autenticado.
    private Long obtenerEmpresa() {

        // Obtiene la autenticacion.
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // Obtiene el usuario.
        JwtUser usuario =
                (JwtUser) authentication.getPrincipal();

        // Devuelve el empresa.
        return usuario.getEmpresaId();

    }

    // Obtiene el empresa del usuario autenticado.
    @GetMapping
    public List<Empresa> obtenerEmpresas() {

        // Obtiene el empresa.
        Long empId = obtenerEmpresa();

        // Busca el empresa autenticado.
        Empresa empresa =
                empresaRepository
                        .findByEmpId(empId)
                        .orElseThrow(() -> new RuntimeException("Empresa no encontrado."));

        // Mantiene una lista para conservar el contrato actual de Angular.
        return List.of(empresa);

    }

    @PutMapping("/{id}")
    public Empresa actualizar(@PathVariable Long id, @RequestBody Empresa entrada) {
        if (!obtenerEmpresa().equals(id)) throw new RuntimeException("Empresa no válida.");
        Empresa empresa = empresaRepository.findByEmpId(id).orElseThrow();
        empresa.setEmpNom(entrada.getEmpNom());
        empresa.setEmpUsuMov(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        empresa.setEmpFecMov(LocalDateTime.now());
        return empresaRepository.save(empresa);
    }

    @PostMapping("/{id}/imagen")
    public Empresa subirImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) {
        if (!obtenerEmpresa().equals(id)) throw new RuntimeException("Empresa no válida.");
        Empresa empresa = empresaRepository.findByEmpId(id).orElseThrow();
        empresa.setEmpIma(imagenService.guardar(id, "ADMINISTRACION", "empresas", "empresa-" + id, archivo));
        empresa.setEmpFecMov(LocalDateTime.now());
        return empresaRepository.save(empresa);
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable Long id) {
        if (!obtenerEmpresa().equals(id)) throw new RuntimeException("Empresa no válida.");
        Empresa empresa = empresaRepository.findByEmpId(id).orElseThrow();
        Resource recurso = imagenService.cargar(id, "ADMINISTRACION", "empresas", empresa.getEmpIma());
        String nombre = recurso.getFilename() == null ? "" : recurso.getFilename().toLowerCase();
        MediaType tipo = nombre.endsWith(".png") ? MediaType.IMAGE_PNG : nombre.endsWith(".webp")
                ? MediaType.parseMediaType("image/webp") : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(tipo).cacheControl(CacheControl.noCache()).body(recurso);
    }

}
