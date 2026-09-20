package com.jbrempresa.backend.core.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

/** Actualiza los avisos de prueba anteriores al enrutamiento por ubicación. */
@Component
@Order(100)
public class AvisosAlertasDatosIniciales implements ApplicationRunner {
    private final JdbcTemplate jdbc;
    public AvisosAlertasDatosIniciales(JdbcTemplate jdbc){this.jdbc=jdbc;}
    @Override @Transactional public void run(ApplicationArguments args){
        jdbc.update("""
            UPDATE avisos_alertas SET avi_emisor='JEFE', avi_emisor_emp_id=emp_id,
                avi_destinatario='CLIENTE', avi_dest_emp_id=emp_id, avi_ubicacion='CATALOGO_CLIENTE'
            WHERE avi_ubicacion IS NULL
            """);
        jdbc.update("""
            UPDATE avisos_alertas a SET avi_emisor_usu_id=(
                SELECT u.usu_id FROM usuarios u JOIN perfiles p ON p.emp_id=u.emp_id AND p.per_id=u.per_id
                WHERE u.emp_id=a.emp_id AND UPPER(p.per_nom)='JEFE' ORDER BY u.usu_id LIMIT 1)
            WHERE a.avi_emisor_usu_id IS NULL AND a.avi_emisor='JEFE'
            """);
    }
}
