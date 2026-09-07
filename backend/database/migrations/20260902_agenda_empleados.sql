-- La agenda deja de pertenecer a Persona y pasa a pertenecer al recurso Empleado.
-- Las reservas, horarios y excepciones conservan el mismo rag_id.

BEGIN;

ALTER TABLE recursos_agendables DROP CONSTRAINT IF EXISTS ck_recurso_tipo;

UPDATE recursos_agendables agenda
SET rag_tip = 'EMPLEADO',
    rag_ref_id = (
        SELECT recurso.reo_id
        FROM recursos_operativos recurso
        WHERE recurso.emp_id = agenda.emp_id
          AND recurso.reo_tip = 'EMPLEADO'
          AND recurso.per_id = agenda.rag_ref_id
          AND recurso.reo_act = TRUE
        ORDER BY recurso.reo_id
        LIMIT 1
    ),
    rag_nom = (
        SELECT recurso.reo_nom
        FROM recursos_operativos recurso
        WHERE recurso.emp_id = agenda.emp_id
          AND recurso.reo_tip = 'EMPLEADO'
          AND recurso.per_id = agenda.rag_ref_id
          AND recurso.reo_act = TRUE
        ORDER BY recurso.reo_id
        LIMIT 1
    ),
    rag_cap = 1
WHERE agenda.rag_tip = 'PERSONA'
  AND EXISTS (
    SELECT 1
    FROM recursos_operativos recurso
    WHERE recurso.emp_id = agenda.emp_id
      AND recurso.reo_tip = 'EMPLEADO'
      AND recurso.per_id = agenda.rag_ref_id
      AND recurso.reo_act = TRUE
  );

INSERT INTO recursos_agendables (
    emp_id, rag_tip, rag_ref_id, rag_nom, rag_cap,
    rag_mar_pre, rag_mar_pos, rag_hor_vis,
    rag_usu_mov, rag_fec_mov, rag_act
)
SELECT recurso.emp_id, 'EMPLEADO', recurso.reo_id, recurso.reo_nom, 1,
       0, 0, '08:00', 'MIGRACION', CURRENT_TIMESTAMP, TRUE
FROM recursos_operativos recurso
WHERE recurso.reo_tip = 'EMPLEADO'
  AND recurso.reo_act = TRUE
  AND NOT EXISTS (
      SELECT 1
      FROM recursos_agendables agenda
      WHERE agenda.emp_id = recurso.emp_id
        AND agenda.rag_tip = 'EMPLEADO'
        AND agenda.rag_ref_id = recurso.reo_id
  );

CREATE TEMP TABLE agendas_persona_eliminadas ON COMMIT DROP AS
SELECT rag_id
FROM recursos_agendables
WHERE rag_tip = 'PERSONA';

CREATE TEMP TABLE reservas_persona_eliminadas ON COMMIT DROP AS
SELECT DISTINCT res_id
FROM reserva_recursos
WHERE rag_id IN (SELECT rag_id FROM agendas_persona_eliminadas);

DELETE FROM tareas_reserva
WHERE rag_id IN (SELECT rag_id FROM agendas_persona_eliminadas)
   OR res_id IN (SELECT res_id FROM reservas_persona_eliminadas);

DELETE FROM reprogramaciones_reserva
WHERE res_id IN (SELECT res_id FROM reservas_persona_eliminadas);

DELETE FROM reserva_recursos
WHERE rag_id IN (SELECT rag_id FROM agendas_persona_eliminadas);

DELETE FROM reservas
WHERE res_id IN (SELECT res_id FROM reservas_persona_eliminadas);

DELETE FROM horarios_recurso
WHERE rag_id IN (SELECT rag_id FROM agendas_persona_eliminadas);

DELETE FROM excepciones_recurso
WHERE rag_id IN (SELECT rag_id FROM agendas_persona_eliminadas);

DELETE FROM recursos_agendables
WHERE rag_id IN (SELECT rag_id FROM agendas_persona_eliminadas);

ALTER TABLE recursos_agendables
    ADD CONSTRAINT ck_recurso_tipo
    CHECK (rag_tip IN ('EMPLEADO', 'DOMICILIO'));

COMMIT;
