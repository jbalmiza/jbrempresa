CREATE TABLE IF NOT EXISTS configuraciones_tabla (
    cot_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    usu_id BIGINT NOT NULL,
    cot_cla VARCHAR(500) NOT NULL,
    cot_con TEXT NOT NULL,
    cot_usu_mov VARCHAR(50) NOT NULL,
    cot_fec_mov TIMESTAMP NOT NULL,
    cot_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_configuraciones_tabla_usuario_clave UNIQUE(cli_id,usu_id,cot_cla)
);
CREATE INDEX IF NOT EXISTS idx_configuraciones_tabla_usuario ON configuraciones_tabla(cli_id,usu_id,cot_act);
