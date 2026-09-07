BEGIN;
ALTER TABLE mensajes ALTER COLUMN com_id DROP NOT NULL;
ALTER TABLE mensajes ALTER COLUMN men_sec DROP NOT NULL;
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS per_id BIGINT;
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_asu VARCHAR(200);
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_nom_rem VARCHAR(150);
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_dni_rem VARCHAR(30);
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_tel_rem VARCHAR(50);
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_ema_rem VARCHAR(150);
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_fec_rec TIMESTAMP;
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_fec_pro TIMESTAMP;
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_con_per DOUBLE PRECISION;
UPDATE mensajes SET men_fec_rec=men_fec_mov WHERE men_fec_rec IS NULL;
CREATE INDEX IF NOT EXISTS idx_mensajes_bandeja_entrada ON mensajes(cli_id,men_fec_rec DESC) WHERE com_id IS NULL AND men_act=TRUE;

INSERT INTO parametros(cli_id,par_cod,par_des,par_val,par_mod,par_usu_mov,par_fec_mov,par_act)
SELECT c.cli_id,'RUTA_DOCUMENTOS_COMUNICACIONES','Ruta de documentos de mensajes y comunicaciones','C:\Workspace\Documentos\Cliente '||c.cli_id||'\Comunicaciones','COMUNICACIONES','migracion',CURRENT_TIMESTAMP,TRUE
FROM (VALUES (1::BIGINT),(2::BIGINT),(3::BIGINT)) c(cli_id)
WHERE NOT EXISTS(SELECT 1 FROM parametros p WHERE p.cli_id=c.cli_id AND p.par_mod='COMUNICACIONES' AND p.par_cod='RUTA_DOCUMENTOS_COMUNICACIONES');
COMMIT;
