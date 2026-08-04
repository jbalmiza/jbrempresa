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

// Importa Persona.
import com.jbrempresa.backend.entity.Persona;

// Importa PersonaRepository.
import com.jbrempresa.backend.repository.PersonaRepository;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/personas")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class PersonaController {

    // Repositorio de personas.
    @Autowired
    private PersonaRepository personaRepository;

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

    // Guarda una persona.
    @PostMapping
    public Persona guardar(
            @RequestBody Persona persona) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Asigna el cliente.
        persona.setCliId(cliId);

        // Si el identificador es 0, se trata de un registro nuevo.
        if (persona.getPerId() != null && persona.getPerId() == 0) {

            persona.setPerId(null);

        }

        // Asigna la fecha.
        persona.setPerFecMov(LocalDateTime.now());

        // Guarda el registro.
        return personaRepository.save(persona);

    }

    // Actualiza una persona.
    @PutMapping("/{id}")
    public Persona actualizar(
            @PathVariable Long id,
            @RequestBody Persona persona) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Comprueba la persona.
        personaRepository.findByCliIdAndPerId(cliId, id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada."));

        // Asigna el identificador.
        persona.setPerId(id);

        // Asigna el cliente.
        persona.setCliId(cliId);

        // Asigna la fecha.
        persona.setPerFecMov(LocalDateTime.now());

        // Guarda el registro.
        return personaRepository.save(persona);

    }

    // Obtiene las personas.
    @GetMapping
    public List<Persona> obtenerPersonas() {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Devuelve los registros.
        return personaRepository.findByCliId(cliId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return personaRepository.obtenerSiguienteId();

    }

    // Elimina una persona.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Busca la persona.
        Persona persona =
                personaRepository
                        .findByCliIdAndPerId(cliId, id)
                        .orElseThrow(() -> new RuntimeException("Persona no encontrada."));

        // Elimina el registro.
        personaRepository.delete(persona);

    }

}