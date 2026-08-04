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

// Importa LoginResponse.
import com.jbrempresa.backend.dto.LoginResponse;

// Importa Usuario.
import com.jbrempresa.backend.entity.Usuario;

// Importa Perfil.
import com.jbrempresa.backend.entity.Perfil;

// Importa Cliente.
import com.jbrempresa.backend.entity.Cliente;

// Importa UsuarioRepository.
import com.jbrempresa.backend.repository.UsuarioRepository;

// Importa PerfilRepository.
import com.jbrempresa.backend.repository.PerfilRepository;

// Importa ClienteRepository.
import com.jbrempresa.backend.repository.ClienteRepository;

// Importa JwtService.
import com.jbrempresa.backend.security.JwtService;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/usuarios")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    // Repositorio de usuarios.
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Repositorio de perfiles.
    @Autowired
    private PerfilRepository perfilRepository;

    // Repositorio de clientes.
    @Autowired
    private ClienteRepository clienteRepository;

    // Servicio JWT.
    @Autowired
    private JwtService jwtService;

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

    // Guarda un usuario.
    @PostMapping
    public Usuario guardar(
            @RequestBody Usuario usuario) {

    	// Si el identificador es 0, se trata de un registro nuevo.
    	if (usuario.getUsuId() != null && usuario.getUsuId() == 0) {

    	    usuario.setUsuId(null);

    	}
    	
        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Asigna el cliente.
        usuario.setCliId(cliId);

        // Asigna la fecha.
        usuario.setUsuFecMov(LocalDateTime.now());

        // Guarda el registro.
        return usuarioRepository.save(usuario);

    }

    // Actualiza un usuario.
    @PutMapping("/{id}")
    public Usuario actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Comprueba el usuario.
        usuarioRepository.findByCliIdAndUsuId(cliId, id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Asigna el identificador.
        usuario.setUsuId(id);

        // Asigna el cliente.
        usuario.setCliId(cliId);

        // Asigna la fecha.
        usuario.setUsuFecMov(LocalDateTime.now());

        // Guarda el registro.
        return usuarioRepository.save(usuario);

    }

    // Obtiene los usuarios.
    @GetMapping
    public List<Usuario> obtenerUsuarios() {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Devuelve los registros.
        return usuarioRepository.findByCliId(cliId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return usuarioRepository.obtenerSiguienteId();

    }

    // Comprueba el acceso.
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody Usuario usuario) {

        // Busca el usuario.
        Usuario usuarioEncontrado =
                usuarioRepository.login(
                        usuario.getUsuUsu(),
                        usuario.getUsuCon());

        // Comprueba si existe.
        if (usuarioEncontrado == null) {
            return null;
        }

        // Obtiene el cliente.
        Cliente cliente =
                clienteRepository
                        .findById(usuarioEncontrado.getCliId())
                        .orElseThrow();

        // Obtiene el perfil.
        Perfil perfil =
                perfilRepository
                        .findById(usuarioEncontrado.getPerId())
                        .orElseThrow();

        // Genera el token.
        String token =
                jwtService.generarToken(
                        usuarioEncontrado.getUsuUsu(),
                        usuarioEncontrado.getUsuId(),
                        usuarioEncontrado.getCliId(),
                        usuarioEncontrado.getPerId());

        // Devuelve la respuesta.
        return new LoginResponse(

                // Token JWT.
                token,

                // Identificador del usuario.
                usuarioEncontrado.getUsuId(),

                // Nombre del usuario.
                usuarioEncontrado.getUsuNom(),

                // Identificador del cliente.
                cliente.getCliId(),

                // Nombre del cliente.
                cliente.getCliNom(),

                // Identificador del perfil.
                perfil.getPerId(),

                // Nombre del perfil.
                perfil.getPerNom()

        );

    }
    
    // Elimina un usuario.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long cliId = obtenerCliente();

        // Busca el usuario.
        Usuario usuario =
                usuarioRepository
                        .findByCliIdAndUsuId(cliId, id)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Elimina el registro.
        usuarioRepository.delete(usuario);

    }

}