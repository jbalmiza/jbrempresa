package com.jbrempresa.backend.core.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EstadosAgendaIniciales implements ApplicationRunner {
    private final JdbcTemplate jdbc;

    public EstadosAgendaIniciales(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        jdbc.execute("ALTER TABLE tareas_reserva DROP CONSTRAINT IF EXISTS ck_tarea_estado");
        jdbc.update("UPDATE tareas_reserva SET tar_est='FINALIZADO' WHERE tar_est='TERMINADA'");
        jdbc.execute("ALTER TABLE tareas_reserva ADD CONSTRAINT ck_tarea_estado "
                + "CHECK (tar_est IN ('PENDIENTE','EN_CURSO','FINALIZADO','CANCELADA'))");
    }
}
