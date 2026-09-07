BEGIN;

ALTER TABLE venta_detalle ADD COLUMN IF NOT EXISTS ven_id_his BIGINT;
ALTER TABLE venta_detalle ADD COLUMN IF NOT EXISTS ven_det_usu_mov VARCHAR(50);
ALTER TABLE venta_detalle ADD COLUMN IF NOT EXISTS ven_det_fec_mov TIMESTAMP;
ALTER TABLE venta_detalle ADD COLUMN IF NOT EXISTS ven_det_act BOOLEAN;

UPDATE venta_detalle SET ven_id_his = 1 WHERE ven_id_his IS NULL;
UPDATE venta_detalle SET ven_det_act = TRUE WHERE ven_det_act IS NULL;
ALTER TABLE venta_detalle ALTER COLUMN ven_id_his SET NOT NULL;

CREATE INDEX IF NOT EXISTS ix_venta_detalle_version
    ON venta_detalle (ven_id, ven_id_his);

COMMIT;
