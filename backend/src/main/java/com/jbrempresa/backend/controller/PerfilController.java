// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa Autowired.

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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Importa Perfil.
import com.jbrempresa.backend.entity.Perfil;

// Importa PerfilRepository.
import com.jbrempresa.backend.repository.PerfilRepository;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;
import com.jbrempresa.backend.core.context.ContextoOperacion;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/perfiles")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class PerfilController {

    // Repositorio de perfiles.
    private final PerfilRepository perfilRepository;
    private final ContextoOperacion contexto;

    public PerfilController(PerfilRepository perfilRepository, ContextoOperacion contexto) {
        this.perfilRepository = perfilRepository;
        this.contexto = contexto;
    }

    private void soloAdministrador() {
        if (!contexto.administradorGlobal()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "El Registro y la Gestión de Perfiles son exclusivos del Administrador.");
        }
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

    // Guarda un perfil.
    @PostMapping
    public Perfil guardar(
            @RequestBody Perfil perfil) {

        soloAdministrador();

        // Obtiene el cliente.
        Long empId = contexto.administradorGlobal() && perfil.getEmpId() != null ? perfil.getEmpId() : obtenerEmpresa();

        // Asigna el cliente.
        perfil.setEmpId(empId);

        // Asigna el usuario de modificacion.
        perfil.setPerUsuMov(obtenerUsuario());

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

        soloAdministrador();

        // Obtiene el cliente.
        Perfil existente = perfilRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfil no encontrado."));
        Long empId = contexto.administradorGlobal() ? existente.getEmpId() : obtenerEmpresa();

        // Comprueba el perfil.
        perfilRepository.findByEmpIdAndPerId(empId, id)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado."));

        // Asigna el identificador.
        perfil.setPerId(id);

        // Asigna el cliente.
        perfil.setEmpId(empId);

        // Asigna el usuario de modificacion.
        perfil.setPerUsuMov(obtenerUsuario());

        // Asigna la fecha.
        perfil.setPerFecMov(LocalDateTime.now());

        // Guarda el registro.
        return perfilRepository.save(perfil);

    }

    // Obtiene los perfiles.
    @GetMapping
    public List<Perfil> obtenerPerfiles() {

        soloAdministrador();

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Devuelve los registros.
        return perfilRepository.findAll();

    }

    // Consulta auxiliar para asignar perfiles al registrar usuarios.
    @GetMapping("/selector")
    public List<Perfil> obtenerPerfilesSelector() {
        return perfilRepository.findByEmpId(obtenerEmpresa());

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        soloAdministrador();

        // Devuelve el identificador.
        return perfilRepository.obtenerSiguienteId(obtenerEmpresa());

    }

    // Elimina un perfil.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        soloAdministrador();

        // Obtiene el cliente.
        Perfil existente = perfilRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfil no encontrado."));
        Long empId = contexto.administradorGlobal() ? existente.getEmpId() : obtenerEmpresa();

        // Busca el perfil.
        Perfil perfil =
                perfilRepository
                        .findByEmpIdAndPerId(empId, id)
                        .orElseThrow(() -> new RuntimeException("Perfil no encontrado."));

        // Elimina el registro.
        perfilRepository.delete(perfil);

    }

}
