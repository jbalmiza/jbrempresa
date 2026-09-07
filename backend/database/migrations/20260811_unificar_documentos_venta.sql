-- documentos_venta es el único modelo comercial.
-- Los datos anteriores son de prueba y no se migran.
DROP TABLE IF EXISTS venta_detalle;
DROP TABLE IF EXISTS ventas;

-- El detalle también queda aislado por cliente y auditado.
ALTER TABLE documentos_venta_detalle ADD COLUMN IF NOT EXISTS cli_id BIGINT;
ALTER TABLE documentos_venta_detalle ADD COLUMN IF NOT EXISTS dvd_usu_mov VARCHAR(100);
ALTER TABLE documentos_venta_detalle ADD COLUMN IF NOT EXISTS dvd_fec_mov TIMESTAMP;
ALTER TABLE documentos_venta_detalle ADD COLUMN IF NOT EXISTS dvd_act BOOLEAN;

UPDATE documentos_venta_detalle dvd
SET cli_id = dov.cli_id,
    dvd_usu_mov = COALESCE(dvd.dvd_usu_mov, dov.dov_usu_mov),
    dvd_fec_mov = COALESCE(dvd.dvd_fec_mov, dov.dov_fec_mov),
    dvd_act = COALESCE(dvd.dvd_act, TRUE)
FROM documentos_venta dov
WHERE dvd.dov_id = dov.dov_id
  AND (dvd.cli_id IS NULL OR dvd.dvd_usu_mov IS NULL OR dvd.dvd_fec_mov IS NULL OR dvd.dvd_act IS NULL);

ALTER TABLE documentos_venta_detalle ALTER COLUMN cli_id SET NOT NULL;
ALTER TABLE documentos_venta_detalle ALTER COLUMN dvd_usu_mov SET NOT NULL;
ALTER TABLE documentos_venta_detalle ALTER COLUMN dvd_fec_mov SET NOT NULL;
ALTER TABLE documentos_venta_detalle ALTER COLUMN dvd_act SET NOT NULL;
CREATE INDEX IF NOT EXISTS idx_documentos_venta_detalle_cliente_documento
    ON documentos_venta_detalle (cli_id, dov_id);

CREATE TABLE IF NOT EXISTS documentos_venta_numeradores (
    cli_id BIGINT NOT NULL,
    dov_tip VARCHAR(3) NOT NULL,
    ndv_ultimo BIGINT NOT NULL,
    CONSTRAINT pk_documentos_venta_numeradores PRIMARY KEY (cli_id, dov_tip),
    CONSTRAINT ck_documentos_venta_numeradores_tipo CHECK (dov_tip IN ('PRE', 'PED', 'ALB', 'FAC')),
    CONSTRAINT ck_documentos_venta_numeradores_positivo CHECK (ndv_ultimo >= 0)
);

INSERT INTO documentos_venta_numeradores (cli_id, dov_tip, ndv_ultimo)
SELECT cli_id, dov_tip,
       MAX(CASE WHEN dov_num ~ ('^' || dov_tip || '-[0-9]+$')
                THEN SUBSTRING(dov_num FROM '[0-9]+$')::BIGINT
                ELSE 0 END)
FROM documentos_venta
GROUP BY cli_id, dov_tip
ON CONFLICT (cli_id, dov_tip)
DO UPDATE SET ndv_ultimo = GREATEST(documentos_venta_numeradores.ndv_ultimo, EXCLUDED.ndv_ultimo);

CREATE UNIQUE INDEX IF NOT EXISTS uk_documentos_venta_sucesor
    ON documentos_venta (cli_id, dov_id_ori)
    WHERE dov_id_ori IS NOT NULL;
