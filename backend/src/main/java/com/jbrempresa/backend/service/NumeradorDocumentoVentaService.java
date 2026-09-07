package com.jbrempresa.backend.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class NumeradorDocumentoVentaService {
    private final JdbcTemplate jdbc;

    public NumeradorDocumentoVentaService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public String siguiente(Long empresaId, String tipo) {
        Long numero = jdbc.queryForObject("""
                INSERT INTO documentos_venta_numeradores (emp_id, dov_tip, ndv_ultimo)
                VALUES (?, ?, 1)
                ON CONFLICT (emp_id, dov_tip)
                DO UPDATE SET ndv_ultimo = documentos_venta_numeradores.ndv_ultimo + 1
                RETURNING ndv_ultimo
                """, Long.class, empresaId, tipo);
        return tipo + "-" + String.format("%06d", numero);
    }
}
