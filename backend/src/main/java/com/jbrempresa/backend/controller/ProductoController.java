// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa List.
import java.util.List;

// Importa Autowired.

// Importa Authentication.
import org.springframework.security.core.Authentication;

// Importa SecurityContextHolder.
import org.springframework.security.core.context.SecurityContextHolder;

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// Importa Producto.
import com.jbrempresa.backend.entity.Producto;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Importa ProductoService.
import com.jbrempresa.backend.service.ProductoService;
import com.jbrempresa.backend.service.ImagenService;
import com.jbrempresa.backend.core.context.ContextoOperacion;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/productos")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    // Servicio de productos.
    private final ProductoService productoService;
    private final ImagenService imagenService;
    private final ContextoOperacion contexto;

    public ProductoController(ProductoService productoService, ImagenService imagenService, ContextoOperacion contexto) {
        this.productoService = productoService;
        this.imagenService = imagenService;
        this.contexto = contexto;
    }

    // Obtiene el cliente autenticado.
    private Long obtenerEmpresa() {

        // Obtiene la autenticación.
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // Obtiene el usuario.
        JwtUser usuario =
                (JwtUser) authentication.getPrincipal();

        // Devuelve el cliente.
        return usuario.getEmpresaId();

    }

    // Comprueba los datos obligatorios del producto.
    private void validarProducto(
            Producto producto) {

        if (producto.getProDurMin() != null &&
                (producto.getProDurMin() < 0 || producto.getProDurMin() % 5 != 0)) {
            throw new IllegalArgumentException("La duración del producto debe ser cero o múltiplo de 5 minutos.");
        }
        if (Boolean.TRUE.equals(producto.getProConSto()) &&
                (producto.getProStoAct() == null || producto.getProStoAct() < 0)) {
            throw new IllegalArgumentException("El stock actual es obligatorio y no puede ser negativo cuando se controla el stock.");
        }
        if (Boolean.TRUE.equals(producto.getProVisCat()) && esTextoVacio(producto.getProIma())) {
            throw new IllegalArgumentException("La imagen es obligatoria para mostrar el producto en el catálogo.");
        }

        if (esTextoVacio(producto.getProTipPro()) ||
                esTextoVacio(producto.getProNom()) ||
                esTextoVacio(producto.getProCat()) ||
                esTextoVacio(producto.getProMar()) ||
                producto.getProPreCom() == null ||
                producto.getProPreVen() == null ||
                producto.getProPreIva() == null) {

            throw new IllegalArgumentException(
                    "Debe informar los campos obligatorios del producto.");

        }
        if (producto.getProIvaCom() != null &&
                (producto.getProIvaCom().signum() < 0 || producto.getProIvaCom().compareTo(new java.math.BigDecimal("100")) > 0)) {
            throw new IllegalArgumentException("El IVA de compra debe estar entre 0 y 100.");
        }
        if (producto.getProPreIva().signum() < 0 || producto.getProPreIva().compareTo(new java.math.BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("El IVA de venta debe estar entre 0 y 100.");
        }
        if (producto.getProPreDes() != null &&
                (producto.getProPreDes().signum() < 0 || producto.getProPreDes().compareTo(new java.math.BigDecimal("100")) > 0)) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        if (producto.getProDesCom() != null &&
                (producto.getProDesCom().signum() < 0 || producto.getProDesCom().compareTo(new java.math.BigDecimal("100")) > 0)) {
            throw new IllegalArgumentException("El descuento de compra debe estar entre 0 y 100.");
        }

    }

    // Comprueba si un texto esta vacio.
    private boolean esTextoVacio(
            String texto) {

        return texto == null || texto.isBlank();

    }

    // Obtiene el usuario autenticado.
    private String obtenerUsuario() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        JwtUser usuario =
                (JwtUser) authentication.getPrincipal();

        return usuario.getUsername();

    }

    // Guarda un producto.
    @PostMapping
    public Producto guardar(
            @RequestBody Producto producto) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Comprueba los campos obligatorios.
        validarProducto(producto);

        // Asigna el cliente.
        producto.setEmpId(empId);

        // Asigna el usuario de modificacion.
        producto.setProUsuMov(obtenerUsuario());

        // Si el identificador es 0, se trata de un registro nuevo.
        if (producto.getProId() != null && producto.getProId() == 0) {

            producto.setProId(null);

        }

        // Guarda el registro.
        return productoService.guardar(
                empId,
                obtenerUsuario(),
                producto);

    }

    // Actualiza un producto.
    @PutMapping("/{id}")
    public Producto actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Comprueba los campos obligatorios.
        validarProducto(producto);

        // Asigna el identificador.
        producto.setProId(id);

        // Asigna el cliente.
        producto.setEmpId(empId);

        // Asigna el usuario de modificacion.
        producto.setProUsuMov(obtenerUsuario());

        // Guarda el registro.
        return productoService.actualizar(
                empId,
                obtenerUsuario(),
                producto);

    }

    // Obtiene los productos.
    @GetMapping
    public List<Producto> obtenerProductos() {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Devuelve los registros.
        Long empresaConsulta = contexto.empresaConsulta(null);
        return empresaConsulta == null
                ? productoService.obtenerProductosGlobales()
                : productoService.obtenerProductos(empresaConsulta);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return productoService.obtenerSiguienteId(obtenerEmpresa());

    }

    // Elimina un producto.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Elimina el registro.
        productoService.eliminar(empId, id);

    }

    @PostMapping("/{id}/baja")
    public Producto baja(@PathVariable Long id) {
        return productoService.baja(obtenerEmpresa(), id, obtenerUsuario());

    }

    @GetMapping("/{id}/historico")
    public List<Producto> historico(@PathVariable Long id) {
        return productoService.historico(obtenerEmpresa(), id);
    }

    @PostMapping("/{id}/deshacer")
    public Producto deshacer(@PathVariable Long id) {
        return productoService.deshacer(obtenerEmpresa(), id);
    }

    @PostMapping("/{id}/imagen")
    public Producto subirImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) {
        String ruta = imagenService.guardar(obtenerEmpresa(), "PRODUCTOS", "productos", "producto-" + id, archivo);
        return productoService.asociarImagen(obtenerEmpresa(), id, ruta);
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable Long id) {
        Producto producto = productoService.obtener(obtenerEmpresa(), id);
        return imagen(imagenService.cargar(obtenerEmpresa(), "PRODUCTOS", "productos", producto.getProIma()));
    }

    private ResponseEntity<Resource> imagen(Resource recurso) {
        String nombre = recurso.getFilename() == null ? "" : recurso.getFilename().toLowerCase();
        MediaType tipo = nombre.endsWith(".png") ? MediaType.IMAGE_PNG : nombre.endsWith(".webp")
                ? MediaType.parseMediaType("image/webp") : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(tipo).cacheControl(CacheControl.noCache()).body(recurso);
    }

}
