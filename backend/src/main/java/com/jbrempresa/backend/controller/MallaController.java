// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa LocalDateTime.
import java.time.LocalDateTime;

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

// Importa Malla.
import com.jbrempresa.backend.entity.Malla;

// Importa MallaRepository.
import com.jbrempresa.backend.repository.MallaRepository;

// Importa MallaService.
import com.jbrempresa.backend.service.MallaService;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/mallas")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class MallaController {

    // Repositorio de mallas.
    @Autowired
    private MallaRepository mallaRepository;

    // Servicio de mallas.
    @Autowired
    private MallaService mallaService;

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

    // Guarda una malla.
    @PostMapping
    public Malla guardar(
            @RequestBody Malla malla) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Asigna el cliente.
        malla.setCliId(cliId);

        // Si el identificador es 0, se trata de un registro nuevo.
        if (malla.getMalId() != null && malla.getMalId() == 0) {

            // Elimina el identificador recibido.
            malla.setMalId(null);

        }

        // Asigna la fecha.
        malla.setMalFecMov(LocalDateTime.now());

        // Guarda el registro.
        return mallaRepository.save(malla);

    }

    // Actualiza una malla.
    @PutMapping("/{id}")
    public Malla actualizar(
            @PathVariable Long id,
            @RequestBody Malla malla) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Comprueba que la malla pertenece al cliente.
        mallaRepository.findByMalIdAndCliId(
                id,
                cliId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Malla no encontrada."));

        // Asigna el identificador.
        malla.setMalId(id);

        // Asigna el cliente.
        malla.setCliId(cliId);

        // Asigna la fecha.
        malla.setMalFecMov(LocalDateTime.now());

        // Guarda el registro.
        return mallaRepository.save(malla);

    }

    // Obtiene las mallas.
    @GetMapping
    public List<Malla> obtenerMallas() {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Devuelve los registros del cliente.
        return mallaRepository.findByCliId(cliId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return mallaRepository.obtenerSiguienteId();

    }

    // Pinta una posición de la malla.
    @PostMapping("/pintar")
    public Malla pintar(
            @RequestBody Malla malla) {

        // Obtiene el cliente autenticado.
        Long cliId = obtenerCliente();

        // Pinta la posición utilizando el servicio.
        return mallaService.pintar(
                cliId,
                malla);

    }
    
 // Borra una posición pintada de la malla.
    @DeleteMapping("/posicion/{entidad}/{fila}/{columna}")
    public void borrarPosicion(
            @PathVariable String entidad,
            @PathVariable Integer fila,
            @PathVariable Integer columna) {

        // Obtiene el cliente autenticado.
        Long cliId = obtenerCliente();

        // Elimina la posición.
        mallaService.borrarPosicion(
                cliId,
                entidad,
                fila,
                columna);

    }

    // Elimina una malla.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Busca la malla.
        Malla malla =
                mallaRepository
                        .findByMalIdAndCliId(
                                id,
                                cliId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Malla no encontrada."));

        // Elimina el registro.
        mallaRepository.delete(malla);

    }

}