// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa Autowired.
import org.springframework.beans.factory.annotation.Autowired;

// Importa LocalDateTime.
import java.time.LocalDateTime;

// Importa List.
import java.util.List;

// Importa Authentication.
import org.springframework.security.core.Authentication;

// Importa SecurityContextHolder.
import org.springframework.security.core.context.SecurityContextHolder;

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;

// Importa Venta.
import com.jbrempresa.backend.entity.Venta;

// Importa VentaRepository.
import com.jbrempresa.backend.repository.VentaRepository;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/ventas")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    // Repositorio de ventas.
    @Autowired
    private VentaRepository ventaRepository;

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

    // Guarda una venta.
    @PostMapping
    public Venta guardar(
            @RequestBody Venta venta) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Asigna el cliente.
        venta.setCliId(cliId);

        // Si el identificador es 0, se trata de un registro nuevo.
        if (venta.getVenId() != null && venta.getVenId() == 0) {

            venta.setVenId(null);

        }

        // Asigna la fecha.
        venta.setVenFecMov(LocalDateTime.now());

        // Guarda el registro.
        return ventaRepository.save(venta);

    }

    // Actualiza una venta.
    @PutMapping("/{id}")
    public Venta actualizar(
            @PathVariable Long id,
            @RequestBody Venta venta) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Comprueba la venta.
        ventaRepository.findByCliIdAndVenId(cliId, id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada."));

        // Asigna el identificador.
        venta.setVenId(id);

        // Asigna el cliente.
        venta.setCliId(cliId);

        // Asigna la fecha.
        venta.setVenFecMov(LocalDateTime.now());

        // Guarda el registro.
        return ventaRepository.save(venta);

    }

    // Obtiene las ventas.
    @GetMapping
    public List<Venta> obtenerVentas() {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Devuelve los registros.
        return ventaRepository.findByCliId(cliId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return ventaRepository.obtenerSiguienteId();

    }

    // Elimina una venta.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Busca la venta.
        Venta venta =
                ventaRepository
                        .findByCliIdAndVenId(cliId, id)
                        .orElseThrow(() -> new RuntimeException("Venta no encontrada."));

        // Elimina el registro.
        ventaRepository.delete(venta);

    }

}