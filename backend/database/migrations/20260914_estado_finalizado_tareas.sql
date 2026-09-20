ALTER TABLE tareas_reserva DROP CONSTRAINT IF EXISTS ck_tarea_estado;

UPDATE tareas_reserva SET tar_est = 'FINALIZADO' WHERE tar_est = 'TERMINADA';

ALTER TABLE tareas_reserva ADD CONSTRAINT ck_tarea_estado
    CHECK (tar_est IN ('PENDIENTE', 'EN_CURSO', 'FINALIZADO', 'CANCELADA'));
