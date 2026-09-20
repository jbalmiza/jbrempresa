package com.jbrempresa.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.entity.Parametro;
import com.jbrempresa.backend.repository.ParametroRepository;

@Service
public class ConfiguracionDocumentosVentaService {
    public static final String MODULO = "VENTAS";
    public static final String MOSTRAR_PRESUPUESTOS = "MOSTRAR_PRESUPUESTOS";
    public static final String MOSTRAR_ALBARANES = "MOSTRAR_ALBARANES";
    public static final String TIPO_FACTURA_AUTOMATICA = "TIPO_FACTURA_AUTOMATICA";
    public static final String REQUERIR_CLAVE_MODIFICACION_CADENA = "REQUERIR_CLAVE_MODIFICACION_CADENA";
    public static final String CLAVE_MODIFICACION_CADENA = "CLAVE_MODIFICACION_CADENA";
    public static final String NORMAL = "NORMAL";
    public static final String SIMPLIFICADA = "SIMPLIFICADA";

    private final ParametroRepository parametros;
    public ConfiguracionDocumentosVentaService(ParametroRepository parametros) { this.parametros = parametros; }

    public Configuracion obtener(Long empresaId) {
        if (empresaId == null) {
            return new Configuracion(
                    algunoActivo(MOSTRAR_PRESUPUESTOS), algunoActivo(MOSTRAR_ALBARANES), NORMAL, false);
        }
        return new Configuracion(booleano(empresaId, MOSTRAR_PRESUPUESTOS),
                booleano(empresaId, MOSTRAR_ALBARANES), tipoFactura(empresaId),
                booleano(empresaId, REQUERIR_CLAVE_MODIFICACION_CADENA));
    }

    public boolean documentoVisible(Long empresaId, String tipo) {
        return switch (tipo) {
            case "PRE" -> obtener(empresaId).mostrarPresupuestos();
            case "ALB" -> obtener(empresaId).mostrarAlbaranes();
            default -> true;
        };
    }

    public String tipoFactura(Long empresaId) {
        return valor(empresaId, TIPO_FACTURA_AUTOMATICA)
                .filter(v -> SIMPLIFICADA.equalsIgnoreCase(v)).map(v -> SIMPLIFICADA).orElse(NORMAL);
    }

    public void validarModificacionCadena(Long empresaId, String clave) {
        if (!booleano(empresaId, REQUERIR_CLAVE_MODIFICACION_CADENA)) return;
        String configurada = valor(empresaId, CLAVE_MODIFICACION_CADENA).orElse("");
        if (configurada.isBlank()) throw new IllegalStateException("Configure la clave de modificación en cadena.");
        if (!configurada.equals(clave == null ? "" : clave))
            throw new IllegalArgumentException("La clave de modificación en cadena no es válida.");
    }

    private boolean booleano(Long empresaId, String codigo) {
        return valor(empresaId, codigo).map(Boolean::parseBoolean).orElse(false);
    }
    private java.util.Optional<String> valor(Long empresaId, String codigo) {
        return parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(empresaId, MODULO, codigo)
                .filter(p -> Boolean.TRUE.equals(p.getParAct())).map(Parametro::getParVal);
    }
    private boolean algunoActivo(String codigo) {
        List<Parametro> valores = parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO, codigo);
        return valores.stream().anyMatch(p -> Boolean.parseBoolean(p.getParVal()));
    }

    public record Configuracion(boolean mostrarPresupuestos, boolean mostrarAlbaranes,
            String tipoFacturaAutomatica, boolean requerirClaveModificacionCadena) {}
}
