package com.jbrempresa.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;
import java.util.Locale;
import java.util.Set;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.repository.ParametroRepository;

@Service
public class ImagenService {
    public static final String CODIGO_RUTA = "RUTA_IMAGENES";
    private static final Set<String> TIPOS = Set.of("image/jpeg", "image/png", "image/webp");
    private final ParametroRepository parametros;
    public ImagenService(ParametroRepository parametros) { this.parametros = parametros; }

    public String guardar(Long empId, String modulo, String tipo, String nombre, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar una imagen.");
        if (archivo.getSize() > 5L * 1024 * 1024) error(HttpStatus.BAD_REQUEST, "La imagen no puede superar 5 MB.");
        String contentType = archivo.getContentType() == null ? "" : archivo.getContentType().toLowerCase(Locale.ROOT);
        if (!TIPOS.contains(contentType)) error(HttpStatus.BAD_REQUEST, "Formato de imagen no permitido.");
        String extension = contentType.equals("image/png") ? ".png" : contentType.equals("image/webp") ? ".webp" : ".jpg";
        Path directorio = directorio(empId, modulo, tipo);
        String fichero = nombre.replaceAll("[^a-zA-Z0-9_-]", "_") + extension;
        try {
            Files.createDirectories(directorio);
            try (var existentes = Files.list(directorio)) {
                existentes.filter(p -> p.getFileName().toString().startsWith(nombre + "."))
                        .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException e) { throw new IllegalStateException(e); } });
            }
            Files.copy(archivo.getInputStream(), directorio.resolve(fichero), StandardCopyOption.REPLACE_EXISTING);
            return fichero;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar la imagen.", e);
        }
    }

    public String guardar(Long empId, String modulo, String tipo, String nombre,
                          String contentType, long tamanio, InputStream contenido) {
        if (tamanio > 5L * 1024 * 1024) error(HttpStatus.BAD_REQUEST, "La imagen no puede superar 5 MB.");
        String mime = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);
        if (!TIPOS.contains(mime)) error(HttpStatus.BAD_REQUEST, "El adjunto seleccionado no es una imagen compatible.");
        String extension = mime.equals("image/png") ? ".png" : mime.equals("image/webp") ? ".webp" : ".jpg";
        Path directorio = directorio(empId, modulo, tipo);
        String fichero = nombre.replaceAll("[^a-zA-Z0-9_-]", "_") + extension;
        try {
            Files.createDirectories(directorio);
            try (var existentes = Files.list(directorio)) {
                existentes.filter(p -> p.getFileName().toString().startsWith(nombre + "."))
                        .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException e) { throw new IllegalStateException(e); } });
            }
            try (contenido) {
                Files.copy(contenido, directorio.resolve(fichero), StandardCopyOption.REPLACE_EXISTING);
            }
            return fichero;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar la imagen.", e);
        }
    }

    public Resource cargar(Long empId, String modulo, String tipo, String fichero) {
        if (fichero == null || fichero.isBlank()) error(HttpStatus.NOT_FOUND, "Imagen no disponible.");
        try {
            Path archivo = directorio(empId, modulo, tipo).resolve(Path.of(fichero).getFileName()).normalize();
            Resource recurso = new UrlResource(archivo.toUri());
            if (!recurso.exists() || !recurso.isReadable()) error(HttpStatus.NOT_FOUND, "Imagen no disponible.");
            return recurso;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no disponible.", e);
        }
    }

    private Path directorio(Long empId, String modulo, String tipo) {
        String base = parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(empId, modulo, CODIGO_RUTA)
                .filter(p -> Boolean.TRUE.equals(p.getParAct()) && !p.getParVal().isBlank())
                .map(p -> p.getParVal()).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Configure el parámetro RUTA_IMAGENES del módulo " + modulo + "."));
        Path raiz = Path.of(base).toAbsolutePath().normalize();
        Path resultado = raiz.resolve("empresa-" + empId).resolve(tipo).normalize();
        if (!resultado.startsWith(raiz)) error(HttpStatus.BAD_REQUEST, "Ruta de imágenes no válida.");
        return resultado;
    }

    private void error(HttpStatus estado, String mensaje) { throw new ResponseStatusException(estado, mensaje); }
}
