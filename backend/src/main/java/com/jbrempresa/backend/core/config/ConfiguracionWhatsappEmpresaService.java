package com.jbrempresa.backend.core.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.core.security.CifradoDatosSensibles;
import com.jbrempresa.backend.entity.Parametro;
import com.jbrempresa.backend.repository.ParametroRepository;

@Service
public class ConfiguracionWhatsappEmpresaService {
    public static final String MODULO = "COMUNICACIONES";
    public static final String PHONE_NUMBER_ID = "WHATSAPP_PHONE_NUMBER_ID";
    public static final String BUSINESS_ACCOUNT_ID = "WHATSAPP_BUSINESS_ACCOUNT_ID";
    public static final String ACCESS_TOKEN = "WHATSAPP_ACCESS_TOKEN";
    public static final String APP_SECRET = "WHATSAPP_APP_SECRET";
    public static final String VERIFY_TOKEN = "WHATSAPP_WEBHOOK_VERIFY_TOKEN";
    public static final String GRAPH_API_VERSION = "WHATSAPP_GRAPH_API_VERSION";
    public static final String PROVEEDOR = "WHATSAPP_PROVEEDOR";
    public static final String TWILIO_ACCOUNT_SID = "TWILIO_ACCOUNT_SID";
    public static final String TWILIO_AUTH_TOKEN = "TWILIO_AUTH_TOKEN";
    public static final String TWILIO_API_KEY_SID = "TWILIO_API_KEY_SID";
    public static final String TWILIO_API_KEY_SECRET = "TWILIO_API_KEY_SECRET";
    public static final String TWILIO_WHATSAPP_FROM = "TWILIO_WHATSAPP_FROM";
    public static final String TWILIO_WEBHOOK_URL = "TWILIO_WEBHOOK_URL";
    public static final String PREFIJO_CIFRADO = "ENC:";
    public static final String VALOR_OCULTO = "********";

    private final ParametroRepository parametros;
    private final CifradoDatosSensibles cifrado;

    public ConfiguracionWhatsappEmpresaService(ParametroRepository parametros, CifradoDatosSensibles cifrado) {
        this.parametros = parametros;
        this.cifrado = cifrado;
    }

    public boolean esSensible(String modulo, String codigo) {
        if (modulo == null || codigo == null || !MODULO.equalsIgnoreCase(modulo.trim())) return false;
        String normalizado = codigo.trim().toUpperCase();
        return ACCESS_TOKEN.equals(normalizado) || APP_SECRET.equals(normalizado) || VERIFY_TOKEN.equals(normalizado)
                || TWILIO_AUTH_TOKEN.equals(normalizado) || TWILIO_API_KEY_SECRET.equals(normalizado);
    }

    public String cifrar(String valor) { return PREFIJO_CIFRADO + cifrado.cifrar(valor); }
    public String ocultar(String valor) { return valor == null || valor.isBlank() ? "" : VALOR_OCULTO; }

    public String valor(Long empresaId, String codigo) {
        Parametro parametro = parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(empresaId, MODULO, codigo)
                .filter(p -> Boolean.TRUE.equals(p.getParAct()))
                .orElseThrow(() -> new IllegalStateException("Falta el parámetro " + codigo + " para el cliente " + empresaId + "."));
        return descifrarSiProcede(parametro.getParVal());
    }

    public String valorOpcional(Long empresaId, String codigo, String defecto) {
        return parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(empresaId, MODULO, codigo)
                .filter(p -> Boolean.TRUE.equals(p.getParAct()))
                .map(p -> p.getParVal()).map(valor -> descifrarSiProcede(valor)).orElse(defecto);
    }

    public String proveedor(Long empresaId) {
        String proveedor = valorOpcional(empresaId, PROVEEDOR, "TWILIO").trim().toUpperCase();
        if (!List.of("TWILIO", "META").contains(proveedor))
            throw new IllegalStateException("El proveedor de WhatsApp debe ser TWILIO o META.");
        return proveedor;
    }

    public Long clientePorTwilioFrom(String numero) {
        String buscado = normalizarWhatsapp(numero);
        return parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO, TWILIO_WHATSAPP_FROM).stream()
                .filter(p -> p != null)
                .filter(p -> normalizarWhatsapp(p.getParVal()).equals(buscado))
                .map(p -> p.getEmpId()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Número de Twilio no configurado."));
    }

    public Long clientePorPhoneNumberId(String phoneNumberId) {
        return parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO, PHONE_NUMBER_ID).stream()
                .filter(p -> p != null)
                .filter(p -> igualdadSegura(p.getParVal(), phoneNumberId))
                .map(p -> p.getEmpId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Número receptor de WhatsApp no configurado."));
    }

    public Long clientePorVerifyToken(String token) {
        List<Parametro> candidatos = parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO, VERIFY_TOKEN);
        return candidatos.stream()
                .filter(p -> p != null)
                .filter(p -> igualdadSegura(descifrarSiProcede(p.getParVal()), token))
                .map(p -> p.getEmpId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Token de verificación de WhatsApp no válido."));
    }

    private String descifrarSiProcede(String valor) {
        if (valor == null) return "";
        return valor.startsWith(PREFIJO_CIFRADO)
                ? cifrado.descifrar(valor.substring(PREFIJO_CIFRADO.length()))
                : valor;
    }

    private boolean igualdadSegura(String izquierda, String derecha) {
        if (izquierda == null || derecha == null) return false;
        return MessageDigest.isEqual(izquierda.getBytes(StandardCharsets.UTF_8), derecha.getBytes(StandardCharsets.UTF_8));
    }

    private String normalizarWhatsapp(String valor) {
        return valor == null ? "" : valor.toLowerCase().replace("whatsapp:", "").replaceAll("[^0-9+]", "");
    }
}
