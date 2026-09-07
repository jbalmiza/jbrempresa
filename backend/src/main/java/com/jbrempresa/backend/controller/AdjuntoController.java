package com.jbrempresa.backend.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.jbrempresa.backend.entity.Adjunto;
import com.jbrempresa.backend.entity.Parametro;
import com.jbrempresa.backend.repository.AdjuntoRepository;
import com.jbrempresa.backend.repository.ParametroRepository;
import com.jbrempresa.backend.repository.ProductoRepository;
import com.jbrempresa.backend.repository.ServicioRepository;
import com.jbrempresa.backend.repository.TipoArticuloRepository;
import com.jbrempresa.backend.repository.EmpresaRepository;
import com.jbrempresa.backend.service.ImagenService;
import com.jbrempresa.backend.security.JwtUser;

@RestController
@RequestMapping("/adjuntos")
@CrossOrigin(origins = "http://localhost:4200")
public class AdjuntoController {
    private static final long TAMANIO_MAXIMO = 10L * 1024 * 1024;
    private static final Set<String> EXTENSIONES = Set.of("pdf", "doc", "docx", "xls", "xlsx", "jpg", "jpeg", "png");
    private static final Set<String> TIPOS = Set.of("ORIGINAL", "COPIA", "OTRO");

    private final AdjuntoRepository adjuntoRepository;
    private final ParametroRepository parametroRepository;
    private final ProductoRepository productoRepository;
    private final ServicioRepository servicioRepository;
    private final EmpresaRepository empresaRepository;
    private final TipoArticuloRepository tipoArticuloRepository;
    private final ImagenService imagenService;

    public AdjuntoController(AdjuntoRepository adjuntoRepository, ParametroRepository parametroRepository,
            ProductoRepository productoRepository, ServicioRepository servicioRepository,
            EmpresaRepository empresaRepository, TipoArticuloRepository tipoArticuloRepository, ImagenService imagenService) {
        this.adjuntoRepository = adjuntoRepository;
        this.parametroRepository = parametroRepository;
        this.productoRepository = productoRepository;
        this.servicioRepository = servicioRepository;
        this.empresaRepository = empresaRepository;
        this.tipoArticuloRepository = tipoArticuloRepository;
        this.imagenService = imagenService;
    }

    @GetMapping
    public List<Adjunto> consultar(
            @RequestParam("modulo") String modulo,
            @RequestParam("tipoRegistro") String tipoRegistro,
            @RequestParam("registroId") Long registroId) {
        return adjuntoRepository
                .findByEmpIdAndAdjModIgnoreCaseAndAdjTipRegIgnoreCaseAndAdjRegIdOrderByAdjId(
                        obtenerEmpresa(), modulo, tipoRegistro, registroId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Adjunto guardar(
            @RequestParam("modulo") String modulo,
            @RequestParam("tipoRegistro") String tipoRegistro,
            @RequestParam("registroId") Long registroId,
            @RequestParam("codigoRuta") String codigoRuta,
            @RequestParam("nombre") String nombre,
            @RequestParam("tipo") String tipo,
            @RequestPart("archivo") MultipartFile archivo) {
        validar(nombre, tipo, registroId, archivo);
        Long empId = obtenerEmpresa();
        String moduloNormalizado = modulo.trim().toUpperCase(Locale.ROOT);
        Parametro parametro = parametroRepository
                .findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(empId, moduloNormalizado, codigoRuta)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "No existe el parámetro de ruta para este módulo."));

        Path raiz = Path.of(parametro.getParVal()).toAbsolutePath().normalize();
        Path carpetaRegistro = raiz.resolve(String.valueOf(registroId)).normalize();
        if (!carpetaRegistro.startsWith(raiz)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ruta de almacenamiento no válida.");
        }

        String nombreOriginal = Path.of(archivo.getOriginalFilename() == null ? "archivo" : archivo.getOriginalFilename())
                .getFileName().toString();
        String extension = obtenerExtension(nombreOriginal);
        String nombreFisico = UUID.randomUUID() + "." + extension;
        Path destino = carpetaRegistro.resolve(nombreFisico).normalize();

        try {
            Files.createDirectories(carpetaRegistro);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException error) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo almacenar el archivo.", error);
        }

        Adjunto adjunto = new Adjunto();
        adjunto.setEmpId(empId);
        adjunto.setAdjMod(moduloNormalizado);
        adjunto.setAdjTipReg(tipoRegistro.trim().toUpperCase(Locale.ROOT));
        adjunto.setAdjRegId(registroId);
        adjunto.setAdjNom(nombre.trim());
        adjunto.setAdjTip(tipo.trim().toUpperCase(Locale.ROOT));
        adjunto.setAdjNomArc(nombreOriginal);
        adjunto.setAdjRutRel(String.valueOf(registroId) + "/" + nombreFisico);
        adjunto.setAdjMime(archivo.getContentType() == null ? "application/octet-stream" : archivo.getContentType());
        adjunto.setAdjTam(archivo.getSize());
        adjunto.setAdjUsuMov(obtenerUsuario());
        adjunto.setAdjFecMov(LocalDateTime.now());
        adjunto.setAdjAct(true);
        adjunto.setAdjPri(false);

        try {
            return adjuntoRepository.save(adjunto);
        } catch (RuntimeException error) {
            try { Files.deleteIfExists(destino); } catch (IOException ignored) {}
            throw error;
        }
    }

    @PutMapping("/{id}/principal")
    public Adjunto marcarPrincipal(
            @PathVariable("id") Long id,
            @RequestParam("codigoRuta") String codigoRuta) {
        Adjunto seleccionado = buscarDeEmpresa(id);
        if (seleccionado.getAdjMime() == null || !seleccionado.getAdjMime().toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo una imagen puede marcarse como principal.");
        }
        Path origen = resolverArchivo(seleccionado, codigoRuta);
        String modulo = seleccionado.getAdjMod().toUpperCase(Locale.ROOT);
        String tipoCarpeta;
        String nombre;
        try {
            switch (modulo) {
                case "PRODUCTOS" -> {
                    if ("TIPO_PRODUCTO".equalsIgnoreCase(seleccionado.getAdjTipReg())) {
                        var tipo = tipoArticuloRepository.findByEmpIdAndIdAndClase(obtenerEmpresa(), seleccionado.getAdjRegId(), "PRODUCTO")
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de producto no encontrado."));
                        tipo.setImagen(imagenService.guardar(obtenerEmpresa(), modulo, "tipos", "tipo-" + seleccionado.getAdjRegId(),
                                seleccionado.getAdjMime(), seleccionado.getAdjTam(), Files.newInputStream(origen)));
                        tipoArticuloRepository.save(tipo);
                        break;
                    }
                    var registro = productoRepository.findByEmpIdAndProIdAndProActTrue(obtenerEmpresa(), seleccionado.getAdjRegId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado."));
                    tipoCarpeta = "productos";
                    nombre = "producto-" + seleccionado.getAdjRegId();
                    registro.setProIma(imagenService.guardar(obtenerEmpresa(), modulo, tipoCarpeta, nombre,
                            seleccionado.getAdjMime(), seleccionado.getAdjTam(), Files.newInputStream(origen)));
                    productoRepository.save(registro);
                }
                case "SERVICIOS" -> {
                    if ("TIPO_SERVICIO".equalsIgnoreCase(seleccionado.getAdjTipReg())) {
                        var tipo = tipoArticuloRepository.findByEmpIdAndIdAndClase(obtenerEmpresa(), seleccionado.getAdjRegId(), "SERVICIO")
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de servicio no encontrado."));
                        tipo.setImagen(imagenService.guardar(obtenerEmpresa(), modulo, "tipos", "tipo-" + seleccionado.getAdjRegId(),
                                seleccionado.getAdjMime(), seleccionado.getAdjTam(), Files.newInputStream(origen)));
                        tipoArticuloRepository.save(tipo);
                        break;
                    }
                    var registro = servicioRepository.findByEmpIdAndSerIdAndSerActTrue(obtenerEmpresa(), seleccionado.getAdjRegId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Servicio no encontrado."));
                    tipoCarpeta = "servicios";
                    nombre = "servicio-" + seleccionado.getAdjRegId();
                    registro.setSerIma(imagenService.guardar(obtenerEmpresa(), modulo, tipoCarpeta, nombre,
                            seleccionado.getAdjMime(), seleccionado.getAdjTam(), Files.newInputStream(origen)));
                    servicioRepository.save(registro);
                }
                case "EMPRESAS" -> {
                    if (!seleccionado.getAdjRegId().equals(obtenerEmpresa())) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puede modificar otra empresa.");
                    }
                    var registro = empresaRepository.findByEmpId(obtenerEmpresa())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada."));
                    tipoCarpeta = "empresas";
                    nombre = "empresa-" + seleccionado.getAdjRegId();
                    registro.setEmpIma(imagenService.guardar(obtenerEmpresa(), "ADMINISTRACION", tipoCarpeta, nombre,
                            seleccionado.getAdjMime(), seleccionado.getAdjTam(), Files.newInputStream(origen)));
                    empresaRepository.save(registro);
                }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Este módulo no admite una imagen principal.");
            }
        } catch (IOException error) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo leer el adjunto.", error);
        }
        var adjuntos = adjuntoRepository.findByEmpIdAndAdjModIgnoreCaseAndAdjTipRegIgnoreCaseAndAdjRegIdOrderByAdjId(
                obtenerEmpresa(), seleccionado.getAdjMod(), seleccionado.getAdjTipReg(), seleccionado.getAdjRegId());
        adjuntos.forEach(adjunto -> adjunto.setAdjPri(adjunto.getAdjId().equals(id)));
        adjuntoRepository.saveAll(adjuntos);
        return seleccionado;
    }

    @GetMapping("/{id}/contenido")
    public ResponseEntity<Resource> obtenerContenido(
            @PathVariable("id") Long id,
            @RequestParam("codigoRuta") String codigoRuta,
            @RequestParam(name = "descargar", defaultValue = "false") boolean descargar) {
        Adjunto adjunto = buscarDeEmpresa(id);
        Path archivo = resolverArchivo(adjunto, codigoRuta);
        try {
            Resource recurso = new UrlResource(archivo.toUri());
            if (!recurso.exists() || !recurso.isReadable()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El archivo no existe.");
            }
            ContentDisposition disposicion = ContentDisposition
                    .builder(descargar ? "attachment" : "inline")
                    .filename(adjunto.getAdjNomArc(), StandardCharsets.UTF_8)
                    .build();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(adjunto.getAdjMime()))
                    .contentLength(adjunto.getAdjTam())
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposicion.toString())
                    .body(recurso);
        } catch (IOException error) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo leer el archivo.", error);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable("id") Long id,
            @RequestParam("codigoRuta") String codigoRuta) {
        Adjunto adjunto = buscarDeEmpresa(id);
        if (Boolean.TRUE.equals(adjunto.getAdjPri())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Seleccione otra imagen principal antes de eliminar este adjunto.");
        }
        Path archivo = resolverArchivo(adjunto, codigoRuta);
        try {
            Files.deleteIfExists(archivo);
            adjuntoRepository.delete(adjunto);
        } catch (IOException error) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar el archivo.", error);
        }
    }

    private Path resolverArchivo(Adjunto adjunto, String codigoRuta) {
        Parametro parametro = parametroRepository
                .findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(
                        adjunto.getEmpId(), adjunto.getAdjMod(), codigoRuta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe el parámetro de ruta."));
        Path raiz = Path.of(parametro.getParVal()).toAbsolutePath().normalize();
        Path archivo = raiz.resolve(adjunto.getAdjRutRel()).normalize();
        if (!archivo.startsWith(raiz)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ruta de archivo no válida.");
        }
        return archivo;
    }

    private void validar(String nombre, String tipo, Long registroId, MultipartFile archivo) {
        if (registroId == null || registroId <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registro no válido.");
        if (nombre == null || nombre.isBlank() || nombre.trim().length() > 150) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio.");
        if (tipo == null || !TIPOS.contains(tipo.trim().toUpperCase(Locale.ROOT))) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de adjunto no válido.");
        if (archivo == null || archivo.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un archivo.");
        if (archivo.getSize() > TAMANIO_MAXIMO) throw new ResponseStatusException(HttpStatus.CONTENT_TOO_LARGE, "El archivo supera los 10 MB.");
        obtenerExtension(archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename());
    }

    private String obtenerExtension(String nombre) {
        int posicion = nombre.lastIndexOf('.');
        String extension = posicion < 0 ? "" : nombre.substring(posicion + 1).toLowerCase(Locale.ROOT);
        if (!EXTENSIONES.contains(extension)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de archivo no permitido.");
        return extension;
    }

    private Adjunto buscarDeEmpresa(Long id) {
        return adjuntoRepository.findByEmpIdAndAdjId(obtenerEmpresa(), id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Adjunto no encontrado."));
    }

    private JwtUser obtenerJwtUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtUser) authentication.getPrincipal();
    }
    private Long obtenerEmpresa() { return obtenerJwtUser().getEmpresaId(); }
    private String obtenerUsuario() { return obtenerJwtUser().getUsername(); }
}
