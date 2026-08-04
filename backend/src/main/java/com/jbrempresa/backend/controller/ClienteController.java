// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa Autowired.
import org.springframework.beans.factory.annotation.Autowired;

// Importa LocalDateTime.
import java.time.LocalDateTime;

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;

// Importa Cliente.
import com.jbrempresa.backend.entity.Cliente;

// Importa ClienteRepository.
import com.jbrempresa.backend.repository.ClienteRepository;

// Importa List.
import java.util.List;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/clientes")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    // Repositorio de clientes.
    @Autowired
    private ClienteRepository clienteRepository;

    // Guarda un cliente.
    @PostMapping
    public Cliente guardar(
            @RequestBody Cliente cliente) {

        // Si el identificador es 0, se trata de un registro nuevo.
        if (cliente.getCliId() != null && cliente.getCliId() == 0) {

            cliente.setCliId(null);

        }

        // Asigna la fecha.
        cliente.setCliFecMov(LocalDateTime.now());

        // Guarda el registro.
        return clienteRepository.save(cliente);

    }

    // Actualiza un cliente.
    @PutMapping("/{id}")
    public Cliente actualizar(
            @PathVariable Long id,
            @RequestBody Cliente cliente) {

        // Comprueba el cliente.
        clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        // Asigna el identificador.
        cliente.setCliId(id);

        // Asigna la fecha.
        cliente.setCliFecMov(LocalDateTime.now());

        // Guarda el registro.
        return clienteRepository.save(cliente);

    }

    // Obtiene los clientes.
    @GetMapping
    public List<Cliente> obtenerClientes() {

        // Devuelve los registros.
        return clienteRepository.findAll();

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return clienteRepository.obtenerSiguienteId();

    }

    // Elimina un cliente.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Busca el cliente.
        Cliente cliente =
                clienteRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        // Elimina el registro.
        clienteRepository.delete(cliente);

    }

}