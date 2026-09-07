package com.jbrempresa.backend.controller;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.twilio.security.RequestValidator;
import com.jbrempresa.backend.core.comunicaciones.RecepcionMensajeService;
import com.jbrempresa.backend.core.config.ConfiguracionWhatsappEmpresaService;
import com.jbrempresa.backend.entity.Mensaje;

@RestController
@RequestMapping("/webhooks/twilio/whatsapp")
public class TwilioWhatsappWebhookController {
    private final ConfiguracionWhatsappEmpresaService configuracion;
    private final RecepcionMensajeService recepcion;

    public TwilioWhatsappWebhookController(ConfiguracionWhatsappEmpresaService configuracion,
            RecepcionMensajeService recepcion) {
        this.configuracion = configuracion;
        this.recepcion = recepcion;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> recibir(
            @RequestHeader(name = "X-Twilio-Signature", required = false) String firma,
            @RequestParam Map<String,String> parametros) {
        String receptor = parametros.getOrDefault("To", "");
        Long empresaId = configuracion.clientePorTwilioFrom(receptor);
        validar(empresaId, firma, parametros);

        Mensaje mensaje = new Mensaje();
        String remitente = limpiarDireccion(parametros.get("From"));
        String nombre = parametros.getOrDefault("ProfileName", "");
        String contenido = parametros.getOrDefault("Body", "").trim();
        if (contenido.isBlank() && numero(parametros.get("NumMedia")) > 0) {
            contenido = "[ARCHIVO WHATSAPP] " + parametros.getOrDefault("MediaUrl0", "");
        }
        mensaje.setMenCan("WHATSAPP");
        mensaje.setMenIdExt(parametros.get("MessageSid"));
        mensaje.setMenRem(remitente);
        mensaje.setMenTelRem(remitente);
        mensaje.setMenNomRem(nombre);
        mensaje.setMenAsu("WhatsApp de " + (nombre.isBlank() ? remitente : nombre));
        mensaje.setMenCon(contenido);
        recepcion.recibir(empresaId, mensaje, "WEBHOOK_TWILIO");
        return ResponseEntity.ok("<Response/>");
    }

    private void validar(Long empresaId, String firma, Map<String,String> parametros) {
        if (firma == null || firma.isBlank()) throw new SecurityException("Firma de Twilio ausente.");
        String token = configuracion.valor(empresaId, ConfiguracionWhatsappEmpresaService.TWILIO_AUTH_TOKEN);
        String url = configuracion.valor(empresaId, ConfiguracionWhatsappEmpresaService.TWILIO_WEBHOOK_URL);
        if (!new RequestValidator(token).validate(url, parametros, firma))
            throw new SecurityException("Firma de Twilio no válida.");
    }

    private String limpiarDireccion(String valor) {
        return valor == null ? "" : valor.toLowerCase().replace("whatsapp:", "").trim();
    }

    private int numero(String valor) {
        try { return Integer.parseInt(valor == null ? "0" : valor); }
        catch (NumberFormatException ex) { return 0; }
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> firmaInvalida() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("<Response/>");
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> configuracionInvalida() {
        return ResponseEntity.badRequest().body("<Response/>");
    }
}
