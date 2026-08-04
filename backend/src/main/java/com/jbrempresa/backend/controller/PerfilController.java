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

// Importa Perfil.
import com.jbrempresa.backend.entity.Perfil;

// Importa PerfilRepository.
import com.jbrempresa.backend.repository.PerfilRepository;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/perfiles")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class PerfilController {

    // Repositorio de perfiles.
    @Autowired
    private PerfilRepository perfilRepository;

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

    // Guarda un perfil.
    @PostMapping
    public Perfil guardar(
            @RequestBody Perfil perfil) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Asigna el cliente.
        perfil.setCliId(cliId);

        // Si el identificador es 0, se trata de un registro nuevo.
        if (perfil.getPerId() != null && perfil.getPerId() == 0) {

            perfil.setPerId(null);

        }

        // Asigna la fecha.
        perfil.setPerFecMov(LocalDateTime.now());

        // Guarda el registro.
        return perfilRepository.save(perfil);

    }

    // Actualiza un perfil.
    @PutMapping("/{id}")
    public Perfil actualizar(
            @PathVariable Long id,
            @RequestBody Perfil perfil) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Comprueba el perfil.
        perfilRepository.findByCliIdAndPerId(cliId, id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado."));

        // Asigna el identificador.
        perfil.setPerId(id);

        // Asigna el cliente.
        perfil.setCliId(cliId);

        // Asigna la fecha.
        perfil.setPerFecMov(LocalDateTime.now());

        // Guarda el registro.
        return perfilRepository.save(perfil);

    }

    // Obtiene los perfiles.
    @GetMapping
    public List<Perfil> obtenerPerfiles() {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Devuelve los registros.
        return perfilRepository.findByCliId(cliId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return perfilRepository.obtenerSiguienteId();

    }

    // Elimina un perfil.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Busca el perfil.
        Perfil perfil =
                perfilRepository
                        .findByCliIdAndPerId(cliId, id)
                        .orElseThrow(() -> new RuntimeException("Perfil no encontrado."));

        // Elimina el registro.
        perfilRepository.delete(perfil);

    }

}