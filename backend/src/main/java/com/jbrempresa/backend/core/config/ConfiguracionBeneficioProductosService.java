package com.jbrempresa.backend.core.config;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.entity.Parametro;
import com.jbrempresa.backend.repository.ParametroRepository;

@Service
public class ConfiguracionBeneficioProductosService {
    public static final String MODULO = "PRODUCTOS";
    public static final String PORCENTAJE_BENEFICIO = "PORCENTAJE_BENEFICIO";
    public static final String VALOR_PREDETERMINADO = "20";
    private final ParametroRepository parametros;

    public ConfiguracionBeneficioProductosService(ParametroRepository parametros) { this.parametros = parametros; }

    public void garantizar(Long empresaId) {
        if (empresaId == null || parametros.existsByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(
                empresaId, MODULO, PORCENTAJE_BENEFICIO)) return;
        Parametro p = new Parametro();
        p.setEmpId(empresaId); p.setParMod(MODULO); p.setParCod(PORCENTAJE_BENEFICIO);
        p.setParDes("Porcentaje de beneficio aplicado entre precio de compra y venta sin IVA");
        p.setParVal(VALOR_PREDETERMINADO); p.setParAct(true);
        p.setParUsuMov("SISTEMA"); p.setParFecMov(LocalDateTime.now());
        parametros.save(p);
    }
}
