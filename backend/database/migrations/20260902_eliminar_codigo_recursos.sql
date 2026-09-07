BEGIN;

DROP INDEX IF EXISTS uk_recurso_codigo_activo;
ALTER TABLE recursos_operativos DROP COLUMN IF EXISTS reo_cod;

COMMIT;
