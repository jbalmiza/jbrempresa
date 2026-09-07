package com.jbrempresa.backend.core.config;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.core.security.CifradoDatosSensibles;
import com.jbrempresa.backend.repository.ParametroRepository;

@Service
public class ConfiguracionSmtpEmpresaService {

    public static final String MODULO = "ADMINISTRACION";
    public static final String CODIGO_PASSWORD = "SMTP_PASSWORD";
    public static final String VALOR_OCULTO = "••••••••";
    private static final String PREFIJO_CIFRADO = "ENC:";

    private final ParametroRepository parametroRepository;
    private final CifradoDatosSensibles cifrado;

    public ConfiguracionSmtpEmpresaService(
            ParametroRepository parametroRepository,
            CifradoDatosSensibles cifrado) {
        this.parametroRepository = parametroRepository;
        this.cifrado = cifrado;
    }

    public void enviarRecuperacion(Long empId, String destinatario, String token) {
        Map<String, String> valores = parametroRepository
                .findByEmpIdAndParModIgnoreCaseOrderByParId(empId, MODULO).stream()
                .filter(parametro -> parametro != null)
                .filter(p -> Boolean.TRUE.equals(p.getParAct()))
                .collect(Collectors.toMap(
                        p -> p.getParCod().toUpperCase(Locale.ROOT),
                        parametro -> parametro.getParVal() == null ? "" : parametro.getParVal(),
                        (primero, segundo) -> segundo));

        if (!booleano(valores, "SMTP_HABILITADO", false)) return;

        String servidor = obligatorio(valores, "SMTP_SERVIDOR");
        int puerto = entero(obligatorio(valores, "SMTP_PUERTO"));
        String usuario = obligatorio(valores, "SMTP_USUARIO");
        String remitente = obligatorio(valores, "SMTP_REMITENTE");
        String passwordCifrado = obligatorio(valores, CODIGO_PASSWORD);
        if (!passwordCifrado.startsWith(PREFIJO_CIFRADO)) {
            throw new IllegalStateException("La contraseña SMTP del cliente no está cifrada.");
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(servidor);
        mailSender.setPort(puerto);
        mailSender.setUsername(usuario);
        mailSender.setPassword(cifrado.descifrar(passwordCifrado.substring(PREFIJO_CIFRADO.length())));
        mailSender.getJavaMailProperties().put(
                "mail.smtp.auth", String.valueOf(booleano(valores, "SMTP_AUTENTICACION", true)));
        mailSender.getJavaMailProperties().put(
                "mail.smtp.starttls.enable", String.valueOf(booleano(valores, "SMTP_STARTTLS", true)));

        String urlFrontend = obligatorio(valores, "URL_FRONTEND").replaceAll("/+$", "");
        String enlace = urlFrontend + "/accesoLogin?recuperacion=" + token;
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject("Recuperación de contraseña de GreenSaaS");
        mensaje.setText("Se ha solicitado restablecer tu contraseña de GreenSaaS.\n\n"
                + "Abre este enlace, válido durante 30 minutos:\n" + enlace
                + "\n\nSi no realizaste la solicitud, ignora este mensaje.");
        mailSender.send(mensaje);
    }

    public boolean esPassword(String modulo, String codigo) {
        return MODULO.equalsIgnoreCase(texto(modulo)) && CODIGO_PASSWORD.equalsIgnoreCase(texto(codigo));
    }

    public String cifrarPassword(String password) {
        return PREFIJO_CIFRADO + cifrado.cifrar(password);
    }

    public String ocultarPassword(String valor) {
        return valor == null || valor.isBlank() ? "" : VALOR_OCULTO;
    }

    private String obligatorio(Map<String, String> valores, String codigo) {
        String valor = texto(valores.get(codigo));
        if (valor.isEmpty()) throw new IllegalStateException("Falta el parámetro " + codigo + " del cliente.");
        return valor;
    }

    private boolean booleano(Map<String, String> valores, String codigo, boolean defecto) {
        String valor = texto(valores.get(codigo));
        return valor.isEmpty() ? defecto : Boolean.parseBoolean(valor);
    }

    private int entero(String valor) {
        try { return Integer.parseInt(valor); }
        catch (NumberFormatException ex) { throw new IllegalStateException("El puerto SMTP no es válido."); }
    }

    private String texto(String valor) { return valor == null ? "" : valor.trim(); }
}
