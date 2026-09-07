// Define el paquete.
package com.jbrempresa.backend.security;

// Importa Autowired.

// Importa UserDetails.
import org.springframework.security.core.userdetails.UserDetails;

// Importa UserDetailsService.
import org.springframework.security.core.userdetails.UserDetailsService;

// Importa UsernameNotFoundException.
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Importa Service.
import org.springframework.stereotype.Service;

// Importa Usuario.
import com.jbrempresa.backend.entity.Usuario;

// Importa UsuarioRepository.
import com.jbrempresa.backend.repository.UsuarioRepository;

// Define el servicio.
@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    // Repositorio de usuarios.
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Carga un usuario.
    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        // Busca el usuario.
        Usuario usuario =
                usuarioRepository
                        .findByUsuUsu(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuario no encontrado."));

        // Devuelve el usuario autenticado.
        return new JwtUser(

                usuario.getUsuUsu(),
                usuario.getUsuCon(),
                usuario.getUsuId(),
                usuario.getEmpId(),
                usuario.getPerId()

        );

    }

}
