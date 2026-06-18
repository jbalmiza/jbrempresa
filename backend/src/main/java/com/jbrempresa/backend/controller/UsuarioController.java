package com.jbrempresa.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jbrempresa.backend.dto.LoginResponse;
import com.jbrempresa.backend.entity.Usuario;
import com.jbrempresa.backend.entity.Perfil;
import com.jbrempresa.backend.entity.Cliente;
import com.jbrempresa.backend.repository.UsuarioRepository;
import com.jbrempresa.backend.repository.PerfilRepository;
import com.jbrempresa.backend.repository.ClienteRepository;
import com.jbrempresa.backend.security.JwtService;

import java.util.List;

// Controlador REST
@RestController

// Ruta base
@RequestMapping("/usuarios")

// Permite peticiones desde Angular
@CrossOrigin(origins = "http://localhost:4200")

public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private JwtService jwtService;

    // Guarda un usuario
    @PostMapping
    public Usuario guardar(@RequestBody Usuario usuario) {

        return usuarioRepository.save(usuario);

    }

    // Obtiene los usuarios de un cliente
    @GetMapping
    public List<Usuario> obtenerUsuarios(
            @RequestParam Long cliente) {

        return usuarioRepository.findByCliId(cliente);

    }

    // Obtiene el siguiente ID
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        return usuarioRepository.obtenerSiguienteId();

    }

    // Comprueba usuario y contraseña
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody Usuario usuario) {

        System.out.println("ENTRANDO EN LOGIN");

        // Busca el usuario
        Usuario usuarioEncontrado =
                usuarioRepository.login(
                        usuario.getUsuUsu(),
                        usuario.getUsuCon());

        // Si no existe devuelve null
        if (usuarioEncontrado == null) { return null; }
        
        // Obtiene el cliente
        Cliente cliente = clienteRepository .findById(usuarioEncontrado.getCliId()) .orElseThrow();

        // Obtiene el perfil
        Perfil perfil = perfilRepository .findById(usuarioEncontrado.getPerId()) .orElseThrow();
        
        // Genera el token JWT
        String token =
                jwtService.generarToken(
                        usuarioEncontrado.getUsuUsu(),
                        usuarioEncontrado.getUsuId(),
                        usuarioEncontrado.getCliId(),
                        usuarioEncontrado.getPerId());

        // Devuelve la respuesta del login
        return new LoginResponse(

                token,

                usuarioEncontrado.getUsuId(),
                usuarioEncontrado.getUsuNom(),

                cliente.getCliId(),
                cliente.getCliNom(),

                perfil.getPerId(),
                perfil.getPerNom()

        );

    }

    // Elimina un usuario de un cliente
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id,
            @RequestParam Long cliente) {

        Usuario usuario =
                usuarioRepository
                        .findByUsuIdAndCliId(id, cliente)
                        .orElseThrow();

        usuarioRepository.delete(usuario);

    }

}