package com.jbrempresa.backend.core.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.core.security.CifradoDatosSensibles;
import com.jbrempresa.backend.entity.Parametro;
import com.jbrempresa.backend.repository.ParametroRepository;

@Service
public class ConfiguracionRedsysBizumService {
    public static final String MODULO = "VENTAS";
    public static final String ACTIVO = "PAGO_BIZUM_ACTIVO";
    public static final String PASARELA = "PAGO_BIZUM_PASARELA";
    public static final String ENTORNO = "PAGO_BIZUM_ENTORNO";
    public static final String COMERCIO = "PAGO_BIZUM_COMERCIO";
    public static final String TERMINAL = "PAGO_BIZUM_TERMINAL";
    public static final String CLAVE = "PAGO_BIZUM_CLAVE";
    public static final String BANCO = "PAGO_BIZUM_BANCO";
    public static final String OBLIGATORIO = "PAGO_BIZUM_OBLIGATORIO";
    public static final String PREFIJO_CIFRADO = "ENC:";
    public static final String VALOR_OCULTO = "********";

    private final ParametroRepository parametros;
    private final CifradoDatosSensibles cifrado;

    public ConfiguracionRedsysBizumService(ParametroRepository parametros, CifradoDatosSensibles cifrado) {
        this.parametros = parametros;
        this.cifrado = cifrado;
    }

    public boolean esSensible(String modulo, String codigo) {
        return modulo != null && codigo != null && MODULO.equalsIgnoreCase(modulo.trim())
                && CLAVE.equalsIgnoreCase(codigo.trim());
    }

    public String cifrar(String valor) { return PREFIJO_CIFRADO + cifrado.cifrar(valor); }
    public String ocultar(String valor) { return valor == null || valor.isBlank() ? "" : VALOR_OCULTO; }

    public String valorOpcional(Long empresaId, String codigo, String defecto) {
        return parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(empresaId, MODULO, codigo)
                .filter(p -> Boolean.TRUE.equals(p.getParAct())).map(p -> p.getParVal())
                .map(valor -> descifrarSiProcede(valor)).orElse(defecto);
    }

    public EstadoConfiguracion estado(Long empresaId) {
        boolean activo = Boolean.parseBoolean(valorOpcional(empresaId, ACTIVO, "false"));
        List<String> faltantes = new ArrayList<>();
        exigir(empresaId, PASARELA, faltantes);
        exigir(empresaId, ENTORNO, faltantes);
        exigir(empresaId, COMERCIO, faltantes);
        exigir(empresaId, TERMINAL, faltantes);
        exigir(empresaId, CLAVE, faltantes);
        String pasarela = valorOpcional(empresaId, PASARELA, "REDSYS").toUpperCase();
        String entorno = valorOpcional(empresaId, ENTORNO, "PRUEBAS").toUpperCase();
        boolean preparada = activo && "REDSYS".equals(pasarela) && faltantes.isEmpty();
        return new EstadoConfiguracion(activo, preparada, pasarela, entorno,
                Boolean.parseBoolean(valorOpcional(empresaId, OBLIGATORIO, "false")), List.copyOf(faltantes));
    }

    private void exigir(Long empresaId, String codigo, List<String> faltantes) {
        if (valorOpcional(empresaId, codigo, "").isBlank()) faltantes.add(codigo);
    }

    private String descifrarSiProcede(String valor) {
        if (valor == null) return "";
        return valor.startsWith(PREFIJO_CIFRADO)
                ? cifrado.descifrar(valor.substring(PREFIJO_CIFRADO.length())) : valor;
    }

    public record EstadoConfiguracion(boolean activo, boolean preparada, String pasarela,
            String entorno, boolean obligatorio, List<String> parametrosFaltantes) {}
}
