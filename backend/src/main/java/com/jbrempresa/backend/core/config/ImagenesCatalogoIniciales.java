package com.jbrempresa.backend.core.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/** Ajustes idempotentes de imágenes pertenecientes a los datos de demostración. */
@Component
public class ImagenesCatalogoIniciales implements ApplicationRunner {
    private final JdbcTemplate jdbc;

    public ImagenesCatalogoIniciales(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        jdbc.update("""
                UPDATE tipos_articulo
                   SET tia_ima = REGEXP_REPLACE(tia_ima, '\\.svg$', '.png', 'i'),
                       tia_usu_mov = 'SISTEMA',
                       tia_fec_mov = CURRENT_TIMESTAMP
                 WHERE tia_act = TRUE
                   AND LOWER(tia_ima) IN (
                       'bocadillo.svg', 'eventos.svg', 'hamburguesa.svg', 'pizza.svg',
                       'reparto.svg', 'taller.svg', 'bebida.svg', 'carne.svg',
                       'entrante.svg', 'menu-degustacion.svg', 'pasta.svg', 'pescado.svg',
                       'postre.svg', 'reserva-mesa.svg', 'vino.svg'
                   )
                """);
    }
}
