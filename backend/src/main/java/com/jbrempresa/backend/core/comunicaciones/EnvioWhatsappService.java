package com.jbrempresa.backend.core.comunicaciones;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import com.jbrempresa.backend.core.config.ConfiguracionWhatsappEmpresaService;
import com.jbrempresa.backend.exception.ReglaNegocioException;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/** Adaptador de salida de WhatsApp. Mantiene Meta fuera del dominio de comunicaciones. */
@Service
public class EnvioWhatsappService {
    private final ConfiguracionWhatsappEmpresaService configuracion;
    private final RestClient http;
    private final ObjectMapper json;

    public EnvioWhatsappService(ConfiguracionWhatsappEmpresaService configuracion,
            RestClient.Builder builder, ObjectMapper json) {
        this.configuracion = configuracion;
        this.http = builder.baseUrl("https://graph.facebook.com").build();
        this.json = json;
    }

    public Resultado enviarTexto(Long empresaId, String destinatario, String contenido) {
        String telefono = soloDigitos(destinatario);
        if (telefono.isBlank()) throw new ReglaNegocioException("WHATSAPP_SIN_DESTINATARIO",
                "La conversación no tiene un teléfono de WhatsApp al que responder.");
        if ("TWILIO".equals(configuracion.proveedor(empresaId))) {
            return enviarTwilio(empresaId, telefono, contenido);
        }
        String version = configuracion.valor(empresaId, ConfiguracionWhatsappEmpresaService.GRAPH_API_VERSION).trim();
        String phoneNumberId = configuracion.valor(empresaId, ConfiguracionWhatsappEmpresaService.PHONE_NUMBER_ID).trim();
        String token = configuracion.valor(empresaId, ConfiguracionWhatsappEmpresaService.ACCESS_TOKEN).trim();
        if (!version.matches("v\\d+\\.\\d+")) throw new ReglaNegocioException("WHATSAPP_CONFIGURACION",
                "La versión configurada de WhatsApp Graph API no es válida.");
        Map<String,Object> cuerpo = Map.of(
                "messaging_product", "whatsapp",
                "recipient_type", "individual",
                "to", telefono,
                "type", "text",
                "text", Map.of("preview_url", false, "body", contenido));
        try {
            String respuesta = http.post()
                    .uri("/{version}/{phoneNumberId}/messages", version, phoneNumberId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(h -> h.setBearerAuth(token))
                    .body(cuerpo)
                    .retrieve()
                    .body(String.class);
            JsonNode raiz = json.readTree(respuesta == null ? "{}" : respuesta);
            JsonNode nodoId = raiz.path("messages").path(0).path("id");
            String idExterno = nodoId.isMissingNode() || nodoId.isNull() ? "" : nodoId.asString();
            if (idExterno.isBlank()) throw new ReglaNegocioException("WHATSAPP_RESPUESTA_INVALIDA",
                    "WhatsApp aceptó la solicitud sin devolver el identificador del mensaje.");
            return new Resultado(idExterno, telefono);
        } catch (RestClientResponseException ex) {
            throw new ReglaNegocioException("WHATSAPP_ENVIO_RECHAZADO",
                    "WhatsApp no ha aceptado el mensaje. Revise el canal y sus credenciales.");
        } catch (ReglaNegocioException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ReglaNegocioException("WHATSAPP_NO_DISPONIBLE",
                    "No se ha podido conectar con WhatsApp en este momento.");
        }
    }

    private Resultado enviarTwilio(Long empresaId, String telefono, String contenido) {
        String accountSid = configuracion.valor(empresaId, ConfiguracionWhatsappEmpresaService.TWILIO_ACCOUNT_SID).trim();
        String apiKeySid = configuracion.valorOpcional(empresaId,
                ConfiguracionWhatsappEmpresaService.TWILIO_API_KEY_SID, "").trim();
        String apiKeySecret = configuracion.valorOpcional(empresaId,
                ConfiguracionWhatsappEmpresaService.TWILIO_API_KEY_SECRET, "").trim();
        String origen = normalizarDireccion(configuracion.valor(empresaId,
                ConfiguracionWhatsappEmpresaService.TWILIO_WHATSAPP_FROM));
        try {
            if (apiKeySid.isBlank() || apiKeySecret.isBlank())
                throw new ReglaNegocioException("WHATSAPP_CONFIGURACION",
                        "Faltan la API Key SID o el secret de Twilio.");
            TwilioRestClient cliente = new TwilioRestClient.Builder(apiKeySid, apiKeySecret)
                    .accountSid(accountSid).build();
            Message mensaje = Message.creator(new PhoneNumber("whatsapp:+" + telefono),
                    new PhoneNumber(origen), contenido).create(cliente);
            if (mensaje.getSid() == null || mensaje.getSid().isBlank())
                throw new ReglaNegocioException("WHATSAPP_RESPUESTA_INVALIDA",
                        "Twilio aceptó la solicitud sin devolver el identificador del mensaje.");
            return new Resultado(mensaje.getSid(), telefono);
        } catch (ReglaNegocioException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ReglaNegocioException("WHATSAPP_ENVIO_RECHAZADO",
                    "Twilio no ha aceptado el mensaje. Revise la cuenta, el Sandbox y el destinatario autorizado.");
        }
    }

    private String soloDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("[^0-9]", "");
    }

    private String normalizarDireccion(String valor) {
        String numero = valor == null ? "" : valor.trim();
        if (numero.toLowerCase().startsWith("whatsapp:")) return numero;
        String digitos = soloDigitos(numero);
        if (digitos.isBlank()) throw new ReglaNegocioException("WHATSAPP_CONFIGURACION",
                "Falta el número de WhatsApp de Twilio.");
        return "whatsapp:+" + digitos;
    }

    public record Resultado(String idExterno, String destinatario) {}
}
