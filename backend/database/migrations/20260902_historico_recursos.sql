-- Versionado A/M/B para Recursos. Los datos existentes son de prueba y se
-- convierten en la primera versión activa de cada recurso.

BEGIN;

ALTER TABLE recursos_operativos DROP CONSTRAINT IF EXISTS uk_recurso_codigo;
ALTER TABLE recursos_operativos DROP CONSTRAINT IF EXISTS recursos_operativos_pkey;

ALTER TABLE recursos_operativos ADD COLUMN IF NOT EXISTS reo_id_his BIGINT;
ALTER TABLE recursos_operativos ADD COLUMN IF NOT EXISTS reo_tip_mov VARCHAR(1);
ALTER TABLE recursos_operativos ADD COLUMN IF NOT EXISTS reo_cau_mov VARCHAR(500);

UPDATE recursos_operativos SET reo_id_his = 1 WHERE reo_id_his IS NULL;
UPDATE recursos_operativos SET reo_tip_mov = 'A' WHERE reo_tip_mov IS NULL;
UPDATE recursos_operativos SET reo_cau_mov = 'Alta del registro' WHERE reo_cau_mov IS NULL;

ALTER TABLE recursos_operativos ALTER COLUMN reo_id DROP IDENTITY IF EXISTS;
ALTER TABLE recursos_operativos ALTER COLUMN reo_id_his SET NOT NULL;
ALTER TABLE recursos_operativos ALTER COLUMN reo_tip_mov SET NOT NULL;
ALTER TABLE recursos_operativos ADD CONSTRAINT recursos_operativos_pkey PRIMARY KEY (reo_id, reo_id_his);
ALTER TABLE recursos_operativos ADD CONSTRAINT ck_recurso_movimiento CHECK (reo_tip_mov IN ('A','M','B'));

CREATE UNIQUE INDEX IF NOT EXISTS uk_recurso_codigo_activo
ON recursos_operativos (emp_id, upper(reo_cod))
WHERE reo_act = TRUE;

COMMIT;
