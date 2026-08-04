// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa Autowired.
import org.springframework.beans.factory.annotation.Autowired;

// Importa LocalDateTime.
import java.time.LocalDateTime;

// Importa List.
import java.util.List;

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;

// Importa Compra.
import com.jbrempresa.backend.entity.Compra;

// Importa CompraRepository.
import com.jbrempresa.backend.repository.CompraRepository;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/compras")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class CompraController {

    // Repositorio de compras.
    @Autowired
    private CompraRepository compraRepository;

    // Guarda una compra.
    @PostMapping("/{cliId}")
    public Compra guardar(
            @PathVariable Long cliId,
            @RequestBody Compra compra) {
    	
        // Si el identificador es 0, se trata de un registro nuevo.
        if (compra.getCliId() != null && compra.getCliId() == 0) {

            compra.setCliId(null);

        }

        // Comprueba el cliente.
        if (!cliId.equals(compra.getCliId())) {
            throw new RuntimeException("El cliente de la compra no coincide con el cliente de la operación.");
        }

        // Asigna la fecha.
        compra.setComFecMov(LocalDateTime.now());

        // Guarda el registro.
        return compraRepository.save(compra);

    }

    // Actualiza una compra.
    @PutMapping("/{cliId}/{id}")
    public Compra actualizar(
            @PathVariable Long cliId,
            @PathVariable Long id,
            @RequestBody Compra compra) {

        // Comprueba el cliente.
        if (!cliId.equals(compra.getCliId())) {
            throw new RuntimeException("El cliente de la compra no coincide con el cliente de la operación.");
        }

        // Comprueba la compra.
        compraRepository.findByCliIdAndComId(cliId, id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada."));

        // Asigna el identificador.
        compra.setComId(id);

        // Asigna la fecha.
        compra.setComFecMov(LocalDateTime.now());

        // Guarda el registro.
        return compraRepository.save(compra);

    }

    // Obtiene las compras.
    @GetMapping("/{cliId}")
    public List<Compra> obtenerCompras(
            @PathVariable Long cliId) {

        // Devuelve los registros.
        return compraRepository.findByCliId(cliId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return compraRepository.obtenerSiguienteId();

    }

    // Elimina una compra.
    @DeleteMapping("/{cliId}/{id}")
    public void eliminar(
            @PathVariable Long cliId,
            @PathVariable Long id) {

        // Busca la compra.
        Compra compra = compraRepository
                .findByCliIdAndComId(cliId, id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada."));

        // Elimina el registro.
        compraRepository.delete(compra);

    }

}