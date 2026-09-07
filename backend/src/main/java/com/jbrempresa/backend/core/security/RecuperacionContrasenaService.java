package com.jbrempresa.backend.core.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.dto.RespuestaRecuperacionContrasena;
import com.jbrempresa.backend.core.config.ConfiguracionSmtpEmpresaService;
import com.jbrempresa.backend.entity.RecuperacionContrasena;
import com.jbrempresa.backend.entity.Usuario;
import com.jbrempresa.backend.repository.RecuperacionContrasenaRepository;
import com.jbrempresa.backend.repository.UsuarioRepository;

@Service
public class RecuperacionContrasenaService {

    private static final Duration DURACION_TOKEN = Duration.ofMinutes(30);
    private static final Duration ESPERA_ENTRE_SOLICITUDES = Duration.ofMinutes(1);
    private static final String RESPUESTA_NEUTRA =
            "Si los datos son correctos, recibirás un enlace para restablecer la contraseña.";

    private final UsuarioRepository usuarioRepository;
    private final RecuperacionContrasenaRepository recuperacionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfiguracionSmtpEmpresaService configuracionSmtp;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.password-reset.expose-token:false}")
    private boolean exponerTokenDesarrollo;

    public RecuperacionContrasenaService(
            UsuarioRepository usuarioRepository,
            RecuperacionContrasenaRepository recuperacionRepository,
            PasswordEncoder passwordEncoder,
            ConfiguracionSmtpEmpresaService configuracionSmtp) {
        this.usuarioRepository = usuarioRepository;
        this.recuperacionRepository = recuperacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.configuracionSmtp = configuracionSmtp;
    }

    @Transactional
    public RespuestaRecuperacionContrasena solicitar(String nombreUsuario, String correo) {
        String usuarioNormalizado = texto(nombreUsuario);
        String correoNormalizado = texto(correo);
        if (usuarioNormalizado.isEmpty() || correoNormalizado.isEmpty()) {
            return new RespuestaRecuperacionContrasena(RESPUESTA_NEUTRA, null);
        }

        Usuario usuario = usuarioRepository
                .findByUsuUsuAndUsuEmaIgnoreCase(usuarioNormalizado, correoNormalizado)
                .orElse(null);
        if (usuario == null) {
            calcularHash(usuarioNormalizado + correoNormalizado);
            return new RespuestaRecuperacionContrasena(RESPUESTA_NEUTRA, null);
        }

        LocalDateTime ahora = LocalDateTime.now();
        boolean solicitudReciente = recuperacionRepository
                .findTopByUsuIdOrderByRecFecCreDesc(usuario.getUsuId())
                .map(r -> r.getRecFecCre().isAfter(ahora.minus(ESPERA_ENTRE_SOLICITUDES)))
                .orElse(false);
        if (solicitudReciente) {
            return new RespuestaRecuperacionContrasena(RESPUESTA_NEUTRA, null);
        }

        revocarTokens(usuario.getUsuId(), ahora);
        String token = generarToken();
        RecuperacionContrasena recuperacion = new RecuperacionContrasena();
        recuperacion.setEmpId(usuario.getEmpId());
        recuperacion.setUsuId(usuario.getUsuId());
        recuperacion.setRecTokHash(calcularHash(token));
        recuperacion.setRecFecCre(ahora);
        recuperacion.setRecFecExp(ahora.plus(DURACION_TOKEN));
        recuperacion.setRecUsuMov(usuario.getUsuUsu());
        recuperacion.setRecFecMov(ahora);
        recuperacion.setRecAct(true);
        recuperacionRepository.save(recuperacion);

        configuracionSmtp.enviarRecuperacion(
                usuario.getEmpId(), usuario.getUsuEma(), token);
        return new RespuestaRecuperacionContrasena(
                RESPUESTA_NEUTRA,
                exponerTokenDesarrollo ? token : null);
    }

    @Transactional
    public void confirmar(String token, String contrasena, String confirmacion) {
        validarContrasena(contrasena, confirmacion);
        LocalDateTime ahora = LocalDateTime.now();
        RecuperacionContrasena recuperacion = recuperacionRepository
                .findByRecTokHashAndRecActTrue(calcularHash(texto(token)))
                .orElseThrow(() -> tokenInvalido());
        if (recuperacion.getRecFecUso() != null || !recuperacion.getRecFecExp().isAfter(ahora)) {
            recuperacion.setRecAct(false);
            recuperacion.setRecFecMov(ahora);
            recuperacionRepository.save(recuperacion);
            throw tokenInvalido();
        }

        Usuario usuario = usuarioRepository.findById(recuperacion.getUsuId())
                .filter(u -> u.getEmpId().equals(recuperacion.getEmpId()))
                .orElseThrow(() -> tokenInvalido());
        usuario.setUsuCon(passwordEncoder.encode(contrasena));
        usuario.setUsuUsuMov(usuario.getUsuUsu());
        usuario.setUsuFecMov(ahora);
        usuarioRepository.save(usuario);

        recuperacion.setRecFecUso(ahora);
        recuperacion.setRecAct(false);
        recuperacion.setRecFecMov(ahora);
        recuperacionRepository.save(recuperacion);
        revocarTokens(usuario.getUsuId(), ahora);
    }

    private void validarContrasena(String contrasena, String confirmacion) {
        if (contrasena == null || !contrasena.equals(confirmacion)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden.");
        }
        if (contrasena.length() < 8 || !contrasena.matches(".*[a-z].*")
                || !contrasena.matches(".*[A-Z].*") || !contrasena.matches(".*\\d.*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, mayúscula, minúscula y número.");
        }
    }

    private void revocarTokens(Long usuarioId, LocalDateTime fecha) {
        var activos = recuperacionRepository.findByUsuIdAndRecActTrue(usuarioId);
        activos.forEach(r -> { r.setRecAct(false); r.setRecFecMov(fecha); });
        recuperacionRepository.saveAll(activos);
    }

    private String generarToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String calcularHash(String valor) {
        try {
            return HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-256").digest(valor.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 no está disponible.", ex);
        }
    }

    private String texto(String valor) { return valor == null ? "" : valor.trim(); }
    private ResponseStatusException tokenInvalido() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "El enlace no es válido o ha caducado.");
    }
}
