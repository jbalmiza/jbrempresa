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
import com.jbrempresa.backend.entity.EmpresaMovimiento;

// Importa EmpresaRepository.
import com.jbrempresa.backend.repository.EmpresaRepository;
import com.jbrempresa.backend.repository.EmpresaMovimientoRepository;
import com.jbrempresa.backend.repository.DomicilioRepository;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;
import com.jbrempresa.backend.security.AccesoPerfilService;
import com.jbrempresa.backend.core.config.ConfiguracionBeneficioProductosService;

// Importa List.
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;
import com.jbrempresa.backend.service.ImagenService;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

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
    private final AccesoPerfilService accesos;
    private final EmpresaMovimientoRepository movimientos;
    private final DomicilioRepository domicilios;
    private final ConfiguracionBeneficioProductosService beneficioProductos;

    public EmpresaController(EmpresaRepository empresaRepository, ImagenService imagenService, AccesoPerfilService accesos,
            EmpresaMovimientoRepository movimientos, DomicilioRepository domicilios,
            ConfiguracionBeneficioProductosService beneficioProductos) {
        this.empresaRepository = empresaRepository;
        this.imagenService = imagenService;
        this.accesos = accesos;
        this.movimientos = movimientos;
        this.domicilios = domicilios;
        this.beneficioProductos = beneficioProductos;
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

    private JwtUser usuarioActual() {
        return (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean esAdministradorGlobal() {
        return accesos.administradorGlobal(usuarioActual());
    }

    private void soloAdministrador() {
        if (!esAdministradorGlobal()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "El Registro y la Gestión de Empresas son exclusivos del Administrador.");
        }
    }

    private void comprobarEmpresaPermitida(Long id) {
        if (!esAdministradorGlobal() && !obtenerEmpresa().equals(id)) {
            throw new RuntimeException("Empresa no válida.");
        }
    }

    // Obtiene el empresa del usuario autenticado.
    @GetMapping
    public List<Empresa> obtenerEmpresas(@RequestParam(defaultValue = "false") boolean incluirBajas) {

        soloAdministrador();

        return empresaRepository.findAll().stream()
                .filter(empresa -> incluirBajas || esActiva(empresa))
                .sorted((a, b) -> a.getEmpId().compareTo(b.getEmpId()))
                .toList();

    }

    @PostMapping
    public Empresa guardar(@RequestBody Empresa entrada) {
        soloAdministrador();
        validarDomicilio(entrada.getEmpId(), entrada.getDomId());
        entrada.setEmpId(null);
        entrada.setEmpAct("true");
        entrada.setEmpTipMov("A");
        entrada.setEmpCauMov("");
        entrada.setEmpUsuMov(usuarioActual().getUsername());
        entrada.setEmpFecMov(LocalDateTime.now());
        Empresa guardada = empresaRepository.save(entrada);
        beneficioProductos.garantizar(guardada.getEmpId());
        return guardada;
    }

    @GetMapping("/siguiente-id")
    public Long siguienteId() {
        soloAdministrador();
        return empresaRepository.obtenerSiguienteId();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        soloAdministrador();
        if (obtenerEmpresa().equals(id)) {
            throw new IllegalArgumentException("No puede eliminar la empresa vinculada al Administrador.");
        }
        empresaRepository.delete(empresaRepository.findById(id).orElseThrow());
    }

    public record CausaEmpresa(String causa) {}

    @PutMapping("/{id}/baja")
    @Transactional
    public Empresa baja(@PathVariable Long id, @RequestBody CausaEmpresa datos) {
        if (obtenerEmpresa().equals(id)) {
            throw new IllegalArgumentException("No puede dar de baja la empresa vinculada al Administrador.");
        }
        return cambiarEstado(id, false, "B", datos);
    }

    @PutMapping("/{id}/reactivacion")
    @Transactional
    public Empresa reactivar(@PathVariable Long id, @RequestBody CausaEmpresa datos) {
        return cambiarEstado(id, true, "R", datos);
    }

    @GetMapping("/{id}/historico")
    public List<EmpresaMovimiento> historico(@PathVariable Long id) {
        soloAdministrador();
        comprobarEmpresaPermitida(id);
        return movimientos.findByEmpresaIdOrderByFechaDesc(id);
    }

    private Empresa cambiarEstado(Long id, boolean activa, String tipo, CausaEmpresa datos) {
        soloAdministrador();
        if (datos == null || datos.causa() == null || datos.causa().isBlank()) {
            throw new IllegalArgumentException("Debe informar la causa del movimiento.");
        }
        Empresa empresa = empresaRepository.findById(id).orElseThrow();
        LocalDateTime fecha = LocalDateTime.now();
        empresa.setEmpAct(Boolean.toString(activa));
        empresa.setEmpTipMov(tipo);
        empresa.setEmpCauMov(datos.causa().trim());
        empresa.setEmpUsuMov(usuarioActual().getUsername());
        empresa.setEmpFecMov(fecha);
        empresaRepository.save(empresa);
        EmpresaMovimiento movimiento = new EmpresaMovimiento();
        movimiento.setEmpresaId(id); movimiento.setTipo(tipo); movimiento.setCausa(datos.causa().trim());
        movimiento.setUsuario(usuarioActual().getUsername()); movimiento.setFecha(fecha); movimiento.setActivo(activa);
        movimientos.save(movimiento);
        return empresa;
    }

    private boolean esActiva(Empresa empresa) {
        return !List.of("false", "b", "0", "n").contains(String.valueOf(empresa.getEmpAct()).trim().toLowerCase());
    }

    @PutMapping("/{id}")
    public Empresa actualizar(@PathVariable Long id, @RequestBody Empresa entrada) {
        soloAdministrador();
        comprobarEmpresaPermitida(id);
        Empresa empresa = empresaRepository.findByEmpId(id).orElseThrow();
        empresa.setEmpNom(entrada.getEmpNom());
        empresa.setEmpRazSoc(entrada.getEmpRazSoc());
        empresa.setEmpNif(entrada.getEmpNif());
        empresa.setEmpActEco(entrada.getEmpActEco());
        empresa.setEmpTel(entrada.getEmpTel());
        empresa.setEmpEma(entrada.getEmpEma());
        empresa.setEmpWeb(entrada.getEmpWeb());
        validarDomicilio(id, entrada.getDomId());
        empresa.setDomId(entrada.getDomId());
        empresa.setEmpUsuMov(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        empresa.setEmpFecMov(LocalDateTime.now());
        return empresaRepository.save(empresa);
    }

    private void validarDomicilio(Long empresaId, Long domicilioId) {
        if (domicilioId == null || domicilioId <= 0 || empresaId == null || empresaId <= 0) return;
        if (domicilios.findByEmpIdAndDomIdAndDomActTrue(empresaId, domicilioId).isEmpty()) {
            throw new IllegalArgumentException("El domicilio fiscal no pertenece a la empresa.");
        }
    }

    @PostMapping("/{id}/imagen")
    public Empresa subirImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) {
        soloAdministrador();
        comprobarEmpresaPermitida(id);
        Empresa empresa = empresaRepository.findByEmpId(id).orElseThrow();
        empresa.setEmpIma(imagenService.guardar(id, "ADMINISTRACION", "empresas", "empresa-" + id, archivo));
        empresa.setEmpFecMov(LocalDateTime.now());
        return empresaRepository.save(empresa);
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable Long id) {
        comprobarEmpresaPermitida(id);
        Empresa empresa = empresaRepository.findByEmpId(id).orElseThrow();
        Resource recurso = imagenService.cargar(id, "ADMINISTRACION", "empresas", empresa.getEmpIma());
        String nombre = recurso.getFilename() == null ? "" : recurso.getFilename().toLowerCase();
        MediaType tipo = nombre.endsWith(".png") ? MediaType.IMAGE_PNG : nombre.endsWith(".webp")
                ? MediaType.parseMediaType("image/webp") : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(tipo).cacheControl(CacheControl.noCache()).body(recurso);
    }

}
