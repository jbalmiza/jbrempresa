BEGIN;

ALTER TABLE IF EXISTS clientes RENAME TO empresas;

DO $$
DECLARE
    tabla RECORD;
BEGIN
    FOR tabla IN
        SELECT table_schema, table_name
        FROM information_schema.columns
        WHERE column_name = 'cli_id'
          AND table_schema = 'public'
    LOOP
        EXECUTE format(
            'ALTER TABLE %I.%I RENAME COLUMN cli_id TO emp_id',
            tabla.table_schema,
            tabla.table_name
        );
    END LOOP;
END $$;

ALTER TABLE empresas RENAME COLUMN cli_nom TO emp_nom;
ALTER TABLE empresas RENAME COLUMN cli_act TO emp_act;
ALTER TABLE empresas RENAME COLUMN cli_usu_mov TO emp_usu_mov;
ALTER TABLE empresas RENAME COLUMN cli_fec_mov TO emp_fec_mov;

ALTER TABLE documentos_venta
    ADD COLUMN IF NOT EXISTS dov_fil_mal INTEGER,
    ADD COLUMN IF NOT EXISTS dov_col_mal INTEGER;

ALTER TABLE documentos_venta_movimientos
    ADD COLUMN IF NOT EXISTS dov_fil_mal INTEGER,
    ADD COLUMN IF NOT EXISTS dov_col_mal INTEGER;

ALTER TABLE documentos_venta
    ADD CONSTRAINT ck_documentos_venta_posicion_completa
    CHECK ((dov_fil_mal IS NULL AND dov_col_mal IS NULL)
        OR (dov_fil_mal > 0 AND dov_col_mal > 0));

CREATE INDEX IF NOT EXISTS idx_documentos_venta_posicion
    ON documentos_venta (emp_id, dov_fil_mal, dov_col_mal)
    WHERE dov_fil_mal IS NOT NULL AND dov_col_mal IS NOT NULL;

COMMIT;
