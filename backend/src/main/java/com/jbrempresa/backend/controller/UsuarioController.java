// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa Autowired.

// Importa LocalDateTime.
import java.time.LocalDateTime;

// Importa List.
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

// Importa Authentication.
import org.springframework.security.core.Authentication;

// Importa SecurityContextHolder.
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;

// Importa LoginResponse.
import com.jbrempresa.backend.dto.LoginResponse;
import com.jbrempresa.backend.dto.LoginRequest;
import com.jbrempresa.backend.dto.UsuarioEntrada;
import com.jbrempresa.backend.dto.UsuarioSalida;
import jakarta.validation.Valid;

// Importa Usuario.
import com.jbrempresa.backend.entity.Usuario;

// Importa Perfil.
import com.jbrempresa.backend.entity.Perfil;

// Importa Empresa.
import com.jbrempresa.backend.entity.Empresa;

// Importa UsuarioRepository.
import com.jbrempresa.backend.repository.UsuarioRepository;

// Importa PerfilRepository.
import com.jbrempresa.backend.repository.PerfilRepository;

// Importa EmpresaRepository.
import com.jbrempresa.backend.repository.EmpresaRepository;
import com.jbrempresa.backend.repository.PersonaRepository;
import com.jbrempresa.backend.repository.UsuarioMovimientoRepository;
import com.jbrempresa.backend.entity.UsuarioMovimiento;
import com.jbrempresa.backend.core.context.ContextoOperacion;

// Importa JwtService.
import com.jbrempresa.backend.security.JwtService;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/usuarios")

public class UsuarioController {

    // Punto ligero para mantener viva una sesión mientras existe actividad real en el cliente.
    @GetMapping("/actividad")
    public void actividad() {
        // La validación y renovación se realizan en JwtFilter.
    }

    // Repositorio de usuarios.
    private final UsuarioRepository usuarioRepository;

    // Repositorio de perfiles.
    private final PerfilRepository perfilRepository;

    // Repositorio de empresas.
    private final EmpresaRepository empresaRepository;

    private final PersonaRepository personaRepository;

    // Servicio JWT.
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;
    private final UsuarioMovimientoRepository usuarioMovimientoRepository;
    private final ContextoOperacion contexto;

    public UsuarioController(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            EmpresaRepository empresaRepository,
            PersonaRepository personaRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UsuarioMovimientoRepository usuarioMovimientoRepository,
            ContextoOperacion contexto) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.empresaRepository = empresaRepository;
        this.personaRepository = personaRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMovimientoRepository = usuarioMovimientoRepository;
        this.contexto = contexto;
    }

    // Obtiene el empresa autenticado.
    private Long obtenerEmpresa() {

        // Obtiene la autenticación.
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // Obtiene el usuario.
        JwtUser usuario =
                (JwtUser) authentication.getPrincipal();

        // Devuelve el empresa.
        return usuario.getEmpresaId();

    }

    // Comprueba los datos obligatorios del usuario.
    private void validarUsuario(
            Usuario usuario,
            boolean contrasenaObligatoria) {

        if (esTextoVacio(usuario.getUsuUsu()) ||
                esTextoVacio(usuario.getUsuNom()) ||
                esTextoVacio(usuario.getUsuEma()) ||
                usuario.getPerId() == null ||
                usuario.getPerId() == 0 ||
                usuario.getUsuPerId() == null ||
                usuario.getUsuPerId() == 0 ||
                (contrasenaObligatoria &&
                esTextoVacio(usuario.getUsuCon()))) {

            throw new IllegalArgumentException(
                    "Debe informar los campos obligatorios del usuario.");

        }

    }

    private void comprobarPersona(Long empId, Usuario usuario, Long usuId) {
        personaRepository.findByEmpIdAndPerIdAndPerActTrue(empId, usuario.getUsuPerId())
                .filter(persona -> !"B".equals(persona.getPerTipMov()))
                .orElseThrow(() -> new IllegalArgumentException("La persona no está activa o no pertenece al empresa."));
        boolean asignada = usuId == null
                ? usuarioRepository.existsByEmpIdAndUsuPerId(empId, usuario.getUsuPerId())
                : usuarioRepository.existsByEmpIdAndUsuPerIdAndUsuIdNot(empId, usuario.getUsuPerId(), usuId);
        if (asignada) throw new IllegalArgumentException("La persona ya tiene un usuario relacionado.");
    }

    // Comprueba si un texto esta vacio.
    private boolean esTextoVacio(
            String texto) {

        return texto == null || texto.isBlank();

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

    // Comprueba que el nombre de usuario es unico.
    private void comprobarUsuarioUnico(
            String usuUsu,
            Long usuId) {

        // Comprueba altas y modificaciones.
        boolean existe = usuId == null
                ? usuarioRepository.existsByUsuUsu(usuUsu)
                : usuarioRepository.existsByUsuUsuAndUsuIdNot(
                        usuUsu,
                        usuId);

        // Informa si el nombre ya existe.
        if (existe) {

            throw new RuntimeException(
                    "El nombre de usuario ya existe.");

        }

    }

    // Guarda un usuario.
    @PostMapping
    public UsuarioSalida guardar(
            @Valid @RequestBody UsuarioEntrada datos) {

        Usuario usuario = datos.entidad();

    	// Si el identificador es 0, se trata de un registro nuevo.
        if (usuario.getUsuId() != null && usuario.getUsuId() == 0) {

            usuario.setUsuId(null);

        }

        // Comprueba los campos obligatorios.
        validarUsuario(usuario, true);

        // Comprueba que el nombre de usuario no existe.
        comprobarUsuarioUnico(
                usuario.getUsuUsu(),
                null);
    	
        // Obtiene el empresa.
        Long empId = obtenerEmpresa();

        // Asigna el empresa.
        usuario.setEmpId(empId);

        comprobarPersona(empId, usuario, null);

        // Asigna el usuario de modificacion.
        usuario.setUsuUsuMov(obtenerUsuario());
        usuario.setUsuTipMov("A");
        usuario.setUsuCauMov(null);

        // Comprueba que el perfil pertenece al empresa.
        perfilRepository
                .findByEmpIdAndPerId(
                        empId,
                        usuario.getPerId())
                .orElseThrow(() -> new RuntimeException(
                        "Perfil no encontrado para el empresa."));

        // Asigna la fecha.
        usuario.setUsuFecMov(LocalDateTime.now());

        // Cifra la contraseÃ±a antes de guardarla.
        usuario.setUsuCon(
                passwordEncoder.encode(
                        usuario.getUsuCon()));

        // Guarda el registro.
        return UsuarioSalida.desde(usuarioRepository.save(usuario));

    }

    // Actualiza un usuario.
    @PutMapping("/{id}")
    public UsuarioSalida actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioEntrada datos) {

        Usuario usuario = datos.entidad();

        // Obtiene el empresa.
        Long empId = contexto.administradorGlobal() && usuario.getEmpId() != null && usuario.getEmpId() > 0 ? usuario.getEmpId() : obtenerEmpresa();

        // Comprueba los campos obligatorios.
        validarUsuario(usuario, false);

        // Comprueba el usuario.
        Usuario usuarioExistente =
                usuarioRepository.findByEmpIdAndUsuId(empId, id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        if (!contexto.administradorGlobal() && esAdministradorGlobal(usuarioExistente)) {
            throw new RuntimeException("Usuario no encontrado.");
        }

        // Comprueba que el nombre no pertenece a otro usuario.
        comprobarUsuarioUnico(
                usuario.getUsuUsu(),
                id);

        // Asigna el identificador.
        usuario.setUsuId(id);

        // Asigna el empresa.
        usuario.setEmpId(empId);

        comprobarPersona(empId, usuario, id);

        // Asigna el usuario de modificacion.
        usuario.setUsuUsuMov(obtenerUsuario());
        usuario.setUsuTipMov("M");
        usuario.setUsuCauMov(null);

        // Comprueba que el perfil pertenece al empresa.
        perfilRepository
                .findByEmpIdAndPerId(
                        empId,
                        usuario.getPerId())
                .orElseThrow(() -> new RuntimeException(
                        "Perfil no encontrado para el empresa."));

        // Asigna la fecha.
        usuario.setUsuFecMov(LocalDateTime.now());

        // Conserva la contraseÃ±a si no se ha indicado una nueva.
        if (usuario.getUsuCon() == null ||
                usuario.getUsuCon().isBlank()) {

            usuario.setUsuCon(
                    usuarioExistente.getUsuCon());

        }

        // Cifra la nueva contraseÃ±a.
        else {

            usuario.setUsuCon(
                    passwordEncoder.encode(
                            usuario.getUsuCon()));

        }

        // Guarda el registro.
        return UsuarioSalida.desde(usuarioRepository.save(usuario));

    }

    // Obtiene los usuarios.
    @GetMapping
    public List<UsuarioSalida> obtenerUsuarios() {

        // Obtiene el empresa.
        Long empId = obtenerEmpresa();

        // Devuelve los registros.
        return (contexto.administradorGlobal() ? usuarioRepository.findAll() : usuarioRepository.findByEmpId(empId)).stream()
                .filter(usuario -> contexto.administradorGlobal() || !esAdministradorGlobal(usuario))
                .map(UsuarioSalida::desde).toList();

    }

    // Consulta usuarios con filtros y paginación sin cargar toda la tabla.
    @GetMapping("/consulta")
    public Page<UsuarioSalida> consultarUsuarios(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "50") int tamanio,
            @RequestParam(required = false) String empId,
            @RequestParam(required = false) String usuId,
            @RequestParam(required = false) String usuUsu,
            @RequestParam(required = false) String perId,
            @RequestParam(required = false) String usuNom,
            @RequestParam(required = false) String usuEma,
            @RequestParam(required = false) String usuUsuMov,
            @RequestParam(required = false) String usuFecMov,
            @RequestParam(required = false) String usuAct) {

        int tamanioSeguro = Math.min(Math.max(tamanio, 1), 100);

        return usuarioRepository.buscarPorEmpresa(
                contexto.administradorGlobal() ? null : obtenerEmpresa(), !contexto.administradorGlobal(), usuAct != null && !usuAct.isBlank(), empId, usuId, usuUsu, perId, usuNom,
                usuEma, usuUsuMov, usuFecMov, usuAct,
                PageRequest.of(Math.max(pagina, 0), tamanioSeguro)).map(UsuarioSalida::desde);

    }

    public record CausaUsuario(String causa) {}

    @PutMapping("/{id}/baja")
    public UsuarioSalida baja(@PathVariable Long id, @RequestBody CausaUsuario datos) {
        if (obtenerJwtUser().getUsuarioId().equals(id)) throw new IllegalArgumentException("No puede dar de baja al usuario conectado.");
        return cambiarEstado(id, false, "B", datos);
    }

    @PutMapping("/{id}/reactivacion")
    public UsuarioSalida reactivar(@PathVariable Long id, @RequestBody CausaUsuario datos) {
        return cambiarEstado(id, true, "R", datos);
    }

    @GetMapping("/{id}/historico")
    public List<UsuarioMovimiento> historico(@PathVariable Long id) {
        Usuario usuario = usuarioPermitido(id);
        return usuarioMovimientoRepository.findByEmpresaIdAndUsuarioIdOrderByFechaDesc(usuario.getEmpId(), id);
    }

    private UsuarioSalida cambiarEstado(Long id, boolean activo, String tipo, CausaUsuario datos) {
        if (datos == null || datos.causa() == null || datos.causa().isBlank()) throw new IllegalArgumentException("Debe informar la causa del movimiento.");
        Usuario u = usuarioPermitido(id);
        u.setUsu_act(Boolean.toString(activo)); u.setUsuTipMov(tipo);u.setUsuCauMov(datos.causa().trim());u.setUsuUsuMov(obtenerUsuario()); u.setUsuFecMov(LocalDateTime.now()); usuarioRepository.save(u);
        UsuarioMovimiento m=new UsuarioMovimiento();m.setEmpresaId(u.getEmpId());m.setUsuarioId(id);m.setTipo(tipo);m.setCausa(datos.causa().trim());m.setUsuario(obtenerUsuario());m.setFecha(LocalDateTime.now());m.setActivo(activo);usuarioMovimientoRepository.save(m);
        return UsuarioSalida.desde(u);
    }

    private JwtUser obtenerJwtUser(){return (JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();}
    private Usuario usuarioPermitido(Long id){Usuario u=usuarioRepository.findById(id).orElseThrow(()->new RuntimeException("Usuario no encontrado."));if(!contexto.administradorGlobal()&&(!u.getEmpId().equals(obtenerEmpresa())||esAdministradorGlobal(u)))throw new RuntimeException("Usuario no encontrado.");return u;}
    private boolean esAdministradorGlobal(Usuario usuario){return "jackalblue".equalsIgnoreCase(usuario.getUsuUsu());}

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return usuarioRepository.obtenerSiguienteId(obtenerEmpresa());

    }

    // Comprueba el acceso.
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest usuario) {

        // Busca el usuario por su nombre.
        Usuario usuarioEncontrado =
                usuarioRepository
                        .findByUsuUsu(
                                usuario.usuUsu())
                        .orElse(null);

        // Comprueba si existe.
        if (usuarioEncontrado == null) {
            return null;
        }

        // Obtiene la contrasena almacenada.
        String contrasenaGuardada =
                usuarioEncontrado.getUsuCon();

        // Deniega el acceso si no existe contrasena almacenada.
        if (contrasenaGuardada == null) {
            return null;
        }

        // Comprueba siempre la contrasena mediante BCrypt.
        boolean accesoCorrecto = passwordEncoder.matches(
                usuario.usuCon(),
                contrasenaGuardada);

        // Deniega el acceso si la contrasena no coincide.
        if (!accesoCorrecto) {
            return null;
        }
        if (!List.of("true", "s", "a", "1").contains(String.valueOf(usuarioEncontrado.getUsuAct()).toLowerCase())) return null;

        // Obtiene el empresa.
        Empresa empresa =
                empresaRepository
                        .findById(usuarioEncontrado.getEmpId())
                        .orElseThrow();

        // Obtiene el perfil.
        Perfil perfil =
                perfilRepository
                        .findByEmpIdAndPerId(
                                usuarioEncontrado.getEmpId(),
                                usuarioEncontrado.getPerId())
                        .orElseThrow();

        // Genera el token.
        String token =
                jwtService.generarToken(
                        usuarioEncontrado.getUsuUsu(),
                        usuarioEncontrado.getUsuId(),
                        usuarioEncontrado.getEmpId(),
                        usuarioEncontrado.getPerId());

        // Devuelve la respuesta.
        return new LoginResponse(

                // Token JWT.
                token,

                // Identificador del usuario.
                usuarioEncontrado.getUsuId(),

                // Nombre del usuario.
                usuarioEncontrado.getUsuNom(),

                // Identificador del empresa.
                empresa.getEmpId(),

                // Nombre del empresa.
                empresa.getEmpNom(),

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

        Usuario usuario = usuarioPermitido(id);

        // Elimina el registro.
        usuarioRepository.delete(usuario);

    }

}
