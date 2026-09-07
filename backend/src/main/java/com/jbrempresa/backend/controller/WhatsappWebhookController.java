package com.jbrempresa.backend.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.jbrempresa.backend.core.comunicaciones.RecepcionMensajeService;
import com.jbrempresa.backend.core.config.ConfiguracionWhatsappEmpresaService;
import com.jbrempresa.backend.entity.Mensaje;

@RestController
@RequestMapping("/webhooks/meta/whatsapp")
public class WhatsappWebhookController {
    private final ConfiguracionWhatsappEmpresaService configuracion;
    private final RecepcionMensajeService recepcion;
    private final ObjectMapper json;

    public WhatsappWebhookController(ConfiguracionWhatsappEmpresaService configuracion,
            RecepcionMensajeService recepcion, ObjectMapper json) {
        this.configuracion = configuracion;
        this.recepcion = recepcion;
        this.json = json;
    }

    @GetMapping
    public ResponseEntity<String> verificar(
            @RequestParam(name = "hub.mode", required = false) String modo,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String reto) {
        if (!"subscribe".equals(modo) || token == null || reto == null) {
            return ResponseEntity.badRequest().body("Solicitud de verificación incompleta.");
        }
        try {
            configuracion.clientePorVerifyToken(token);
            return ResponseEntity.ok(reto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token no válido.");
        }
    }

    @PostMapping
    public ResponseEntity<Void> recibir(@RequestHeader(name = "X-Hub-Signature-256", required = false) String firma,
            @RequestBody String cuerpo) {
        try {
            JsonNode raiz = json.readTree(cuerpo);
            String phoneNumberId = primerPhoneNumberId(raiz);
            if (phoneNumberId == null) return ResponseEntity.ok().build();
            Long empresaId = configuracion.clientePorPhoneNumberId(phoneNumberId);
            validarFirma(cuerpo, firma, configuracion.valor(empresaId, ConfiguracionWhatsappEmpresaService.APP_SECRET));
            procesarMensajes(empresaId, raiz);
            return ResponseEntity.ok().build();
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private void procesarMensajes(Long empresaId, JsonNode raiz) {
        for (JsonNode entrada : iterable(raiz.path("entry"))) {
            for (JsonNode cambio : iterable(entrada.path("changes"))) {
                JsonNode valor = cambio.path("value");
                String nombre = texto(valor.path("contacts").path(0).path("profile").path("name"), "");
                for (JsonNode original : iterable(valor.path("messages"))) {
                    Mensaje mensaje = convertir(original, nombre);
                    recepcion.recibir(empresaId, mensaje, "WEBHOOK_META");
                }
            }
        }
    }

    private Mensaje convertir(JsonNode original, String nombre) {
        Mensaje mensaje = new Mensaje();
        String telefono = texto(original.path("from"), "");
        String tipo = texto(original.path("type"), "unknown");
        mensaje.setMenCan("WHATSAPP");
        mensaje.setMenIdExt(texto(original.path("id"), null));
        mensaje.setMenRem(telefono);
        mensaje.setMenTelRem(telefono);
        mensaje.setMenNomRem(nombre);
        mensaje.setMenAsu("WhatsApp de " + (nombre.isBlank() ? telefono : nombre));
        mensaje.setMenCon(contenido(original, tipo));
        String marcaTiempo = texto(original.path("timestamp"), "");
        if (!marcaTiempo.isBlank()) {
            mensaje.setMenFecRec(LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(marcaTiempo)), ZoneId.systemDefault()));
        }
        return mensaje;
    }

    private String contenido(JsonNode mensaje, String tipo) {
        return switch (tipo) {
            case "text" -> texto(mensaje.path("text").path("body"), "");
            case "button" -> texto(mensaje.path("button").path("text"), "[Botón]");
            case "interactive" -> contenidoInteractivo(mensaje.path("interactive"));
            case "image", "document", "audio", "video", "sticker" -> {
                JsonNode medio = mensaje.path(tipo);
                String descripcion = texto(medio.path("caption"), "");
                yield "[" + tipo.toUpperCase() + "]" + (descripcion.isBlank() ? "" : " " + descripcion);
            }
            default -> "[MENSAJE " + tipo.toUpperCase() + "]";
        };
    }

    private String contenidoInteractivo(JsonNode interactivo) {
        JsonNode respuesta = interactivo.has("button_reply") ? interactivo.path("button_reply") : interactivo.path("list_reply");
        return texto(respuesta.path("title"), texto(respuesta.path("id"), "[Respuesta interactiva]"));
    }

    private String primerPhoneNumberId(JsonNode raiz) {
        for (JsonNode entrada : iterable(raiz.path("entry"))) {
            for (JsonNode cambio : iterable(entrada.path("changes"))) {
                String id = texto(cambio.path("value").path("metadata").path("phone_number_id"), "");
                if (!id.isBlank()) return id;
            }
        }
        return null;
    }

    private void validarFirma(String cuerpo, String firma, String secreto) throws Exception {
        if (firma == null || !firma.startsWith("sha256=")) throw new SecurityException("Firma ausente.");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secreto.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        String esperada = "sha256=" + java.util.HexFormat.of().formatHex(mac.doFinal(cuerpo.getBytes(StandardCharsets.UTF_8)));
        if (!MessageDigest.isEqual(esperada.getBytes(StandardCharsets.UTF_8), firma.getBytes(StandardCharsets.UTF_8))) {
            throw new SecurityException("Firma incorrecta.");
        }
    }

    private String texto(JsonNode nodo, String defecto) {
        return nodo == null || nodo.isMissingNode() || nodo.isNull() ? defecto : nodo.asString();
    }

    private Iterable<JsonNode> iterable(JsonNode nodo) {
        if (nodo == null || !nodo.isArray()) return java.util.List.of();
        java.util.List<JsonNode> elementos = new java.util.ArrayList<>();
        for (int indice = 0; indice < nodo.size(); indice++) elementos.add(nodo.get(indice));
        return elementos;
    }
}
