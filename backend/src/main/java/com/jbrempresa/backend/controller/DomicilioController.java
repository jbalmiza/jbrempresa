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

// Importa Domicilio.
import com.jbrempresa.backend.entity.Domicilio;

// Importa DomicilioRepository.
import com.jbrempresa.backend.repository.DomicilioRepository;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/domicilio")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class DomicilioController {

    // Repositorio de domicilios.
    @Autowired
    private DomicilioRepository domicilioRepository;

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

    // Guarda un domicilio.
    @PostMapping
    public Domicilio guardar(
            @RequestBody Domicilio domicilio) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Asigna el cliente.
        domicilio.setCliId(cliId);

        // Si el identificador es 0, se trata de un registro nuevo.
        if (domicilio.getDomId() != null && domicilio.getDomId() == 0) {

            domicilio.setDomId(null);

        }

        // Asigna la fecha.
        domicilio.setDomFecMov(LocalDateTime.now());

        // Guarda el registro.
        return domicilioRepository.save(domicilio);

    }

    // Actualiza un domicilio.
    @PutMapping("/{id}")
    public Domicilio actualizar(
            @PathVariable Long id,
            @RequestBody Domicilio domicilio) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Comprueba el domicilio.
        domicilioRepository.findByCliIdAndDomId(cliId, id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado."));

        // Asigna el identificador.
        domicilio.setDomId(id);

        // Asigna el cliente.
        domicilio.setCliId(cliId);

        // Asigna la fecha.
        domicilio.setDomFecMov(LocalDateTime.now());

        // Guarda el registro.
        return domicilioRepository.save(domicilio);

    }

    // Obtiene los domicilios.
    @GetMapping
    public List<Domicilio> obtenerDomicilios() {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Devuelve los registros.
        return domicilioRepository.findByCliId(cliId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return domicilioRepository.obtenerSiguienteId();

    }

    // Elimina un domicilio.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Busca el domicilio.
        Domicilio domicilio =
                domicilioRepository
                        .findByCliIdAndDomId(cliId, id)
                        .orElseThrow(() -> new RuntimeException("Domicilio no encontrado."));

        // Elimina el registro.
        domicilioRepository.delete(domicilio);

    }

}