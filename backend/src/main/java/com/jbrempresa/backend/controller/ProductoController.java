// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa List.
import java.util.List;

// Importa Autowired.
import org.springframework.beans.factory.annotation.Autowired;

// Importa Authentication.
import org.springframework.security.core.Authentication;

// Importa SecurityContextHolder.
import org.springframework.security.core.context.SecurityContextHolder;

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;

// Importa Producto.
import com.jbrempresa.backend.entity.Producto;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Importa ProductoService.
import com.jbrempresa.backend.service.ProductoService;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/productos")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    // Servicio de productos.
    @Autowired
    private ProductoService productoService;

    // Obtiene el cliente autenticado.
    private Long obtenerCliente() {

        // Obtiene la autenticación.
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // Obtiene el usuario.
        JwtUser usuario =
                (JwtUser) authentication.getPrincipal();

        // Devuelve el cliente.
        return usuario.getClienteId();

    }

    // Guarda un producto.
    @PostMapping
    public Producto guardar(
            @RequestBody Producto producto) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Asigna el cliente.
        producto.setCliId(cliId);

        // Si el identificador es 0, se trata de un registro nuevo.
        if (producto.getProId() != null && producto.getProId() == 0) {

            producto.setProId(null);

        }

        // Guarda el registro.
        return productoService.guardar(
                cliId,
                producto);

    }

    // Actualiza un producto.
    @PutMapping("/{id}")
    public Producto actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Asigna el identificador.
        producto.setProId(id);

        // Asigna el cliente.
        producto.setCliId(cliId);

        // Guarda el registro.
        return productoService.actualizar(
                cliId,
                producto);

    }

    // Obtiene los productos.
    @GetMapping
    public List<Producto> obtenerProductos() {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Devuelve los registros.
        return productoService.obtenerProductos(cliId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return productoService.obtenerSiguienteId();

    }

    // Elimina un producto.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Elimina el registro.
        productoService.eliminar(
                cliId,
                id);

    }

}