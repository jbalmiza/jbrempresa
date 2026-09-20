package com.jbrempresa.backend.core.config;

import java.time.LocalDateTime;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import com.jbrempresa.backend.entity.Parametro;
import com.jbrempresa.backend.repository.EmpresaRepository;
import com.jbrempresa.backend.repository.ParametroRepository;
import com.jbrempresa.backend.service.ConfiguracionDocumentosVentaService;

@Component
public class ParametrosVentasIniciales implements ApplicationRunner {
    private final EmpresaRepository empresas;
    private final ParametroRepository parametros;
    private final JdbcTemplate jdbc;
    public ParametrosVentasIniciales(EmpresaRepository empresas, ParametroRepository parametros, JdbcTemplate jdbc) {
        this.empresas = empresas; this.parametros = parametros; this.jdbc = jdbc;
    }
    @Override public void run(ApplicationArguments args) {
        empresas.findAll().stream().filter(e -> Boolean.parseBoolean(e.getEmpAct())).forEach(e -> {
            crear(e.getEmpId(), ConfiguracionDocumentosVentaService.MOSTRAR_PRESUPUESTOS,
                    "Mostrar Registro y Gestión de Presupuestos", "false");
            crear(e.getEmpId(), ConfiguracionDocumentosVentaService.MOSTRAR_ALBARANES,
                    "Mostrar Registro y Gestión de Albaranes", "false");
            crear(e.getEmpId(), ConfiguracionDocumentosVentaService.TIPO_FACTURA_AUTOMATICA,
                    "Tipo de factura generada automáticamente desde pedidos",
                    Long.valueOf(2L).equals(e.getEmpId()) ? ConfiguracionDocumentosVentaService.NORMAL
                            : ConfiguracionDocumentosVentaService.SIMPLIFICADA);
            crear(e.getEmpId(), ConfiguracionDocumentosVentaService.REQUERIR_CLAVE_MODIFICACION_CADENA,
                    "Requerir clave para modificar pedido y documentos asociados", "false");
            crear(e.getEmpId(), ConfiguracionDocumentosVentaService.CLAVE_MODIFICACION_CADENA,
                    "Clave para modificar pedido y documentos asociados", "");
        });
        jdbc.update("""
                UPDATE personas SET per_dat_com = (
                  NULLIF(TRIM(COALESCE(per_tel, '')), '') IS NOT NULL
                  AND NULLIF(TRIM(COALESCE(per_tip_per, '')), '') IS NOT NULL
                  AND NULLIF(TRIM(COALESCE(per_tip_doc, '')), '') IS NOT NULL
                  AND NULLIF(TRIM(COALESCE(per_doc, '')), '') IS NOT NULL
                  AND COALESCE(dom_id, 0) > 0
                  AND ((per_tip_per = 'JURIDICA' AND NULLIF(TRIM(COALESCE(per_raz_soc_cor, '')), '') IS NOT NULL)
                    OR (per_tip_per <> 'JURIDICA' AND NULLIF(TRIM(COALESCE(per_nom, '')), '') IS NOT NULL
                        AND NULLIF(TRIM(COALESCE(per_ape1, '')), '') IS NOT NULL))
                )
                """);
    }
    private void crear(Long empresaId, String codigo, String descripcion, String valor) {
        if (parametros.existsByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(
                empresaId, ConfiguracionDocumentosVentaService.MODULO, codigo)) return;
        Parametro p = new Parametro(); p.setEmpId(empresaId);
        p.setParMod(ConfiguracionDocumentosVentaService.MODULO); p.setParCod(codigo);
        p.setParDes(descripcion); p.setParVal(valor); p.setParAct(true);
        p.setParUsuMov("SISTEMA"); p.setParFecMov(LocalDateTime.now()); parametros.save(p);
    }
}
