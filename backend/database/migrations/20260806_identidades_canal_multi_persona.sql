BEGIN;
CREATE TABLE IF NOT EXISTS personas_contactos_canal(
 pcc_id BIGSERIAL PRIMARY KEY,cli_id BIGINT NOT NULL,coc_id BIGINT NOT NULL REFERENCES contactos_canal(coc_id),per_id BIGINT NOT NULL,pcc_tip VARCHAR(20) NOT NULL,pcc_usu_mov VARCHAR(50) NOT NULL,pcc_fec_mov TIMESTAMP NOT NULL,pcc_act BOOLEAN NOT NULL DEFAULT TRUE,UNIQUE(cli_id,coc_id,per_id)
);
INSERT INTO personas_contactos_canal(cli_id,coc_id,per_id,pcc_tip,pcc_usu_mov,pcc_fec_mov,pcc_act)
SELECT cli_id,coc_id,per_id,'CONFIRMADA',coc_usu_mov,coc_fec_mov,TRUE FROM contactos_canal WHERE per_id IS NOT NULL
ON CONFLICT(cli_id,coc_id,per_id) DO NOTHING;
ALTER TABLE contactos_canal DROP COLUMN IF EXISTS per_id;
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS coc_id BIGINT;
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_can VARCHAR(20);
UPDATE mensajes SET men_can=CASE WHEN men_dir='INTERNA' THEN 'INTERNO' ELSE 'SIMULADO' END WHERE men_can IS NULL;
CREATE INDEX IF NOT EXISTS idx_personas_contacto_canal ON personas_contactos_canal(cli_id,coc_id,pcc_act);
CREATE INDEX IF NOT EXISTS idx_mensajes_contacto_canal ON mensajes(cli_id,coc_id);
COMMIT;
