BEGIN;

CREATE TABLE IF NOT EXISTS comunicaciones (
    com_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    com_tip VARCHAR(20) NOT NULL,
    com_ori VARCHAR(10) NOT NULL,
    com_can VARCHAR(20) NOT NULL,
    com_est VARCHAR(30) NOT NULL,
    com_asu VARCHAR(200) NOT NULL,
    per_id BIGINT,
    usu_id BIGINT,
    are_id BIGINT,
    com_fec_ult TIMESTAMP NOT NULL,
    com_usu_mov VARCHAR(50) NOT NULL,
    com_fec_mov TIMESTAMP NOT NULL,
    com_act BOOLEAN NOT NULL DEFAULT TRUE,
    CHECK (com_tip IN ('CONVERSACION','AVISO','ALERTA')),
    CHECK (com_ori IN ('ENTRADA','SALIDA','INTERNA')),
    CHECK (com_can IN ('SIMULADO','EMAIL','WHATSAPP','INTERNO')),
    CHECK (com_est IN ('NUEVA','EN_PROCESO','PENDIENTE_CLIENTE','PENDIENTE_USUARIO','RESUELTA','CERRADA','ERROR'))
);

CREATE TABLE IF NOT EXISTS mensajes (
    men_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    com_id BIGINT NOT NULL REFERENCES comunicaciones(com_id),
    men_sec BIGINT NOT NULL,
    men_dir VARCHAR(10) NOT NULL,
    men_aut VARCHAR(15) NOT NULL,
    men_con TEXT NOT NULL,
    men_est VARCHAR(20) NOT NULL,
    men_id_ext VARCHAR(200),
    men_rem VARCHAR(200),
    men_des VARCHAR(200),
    men_usu_mov VARCHAR(50) NOT NULL,
    men_fec_mov TIMESTAMP NOT NULL,
    men_act BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (cli_id, com_id, men_sec),
    CHECK (men_dir IN ('ENTRADA','SALIDA','INTERNA')),
    CHECK (men_aut IN ('CLIENTE','USUARIO','IA','SISTEMA'))
);

CREATE TABLE IF NOT EXISTS contactos_canal (
    coc_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    per_id BIGINT,
    coc_can VARCHAR(20) NOT NULL,
    coc_ide VARCHAR(200) NOT NULL,
    coc_ver BOOLEAN NOT NULL DEFAULT FALSE,
    coc_usu_mov VARCHAR(50) NOT NULL,
    coc_fec_mov TIMESTAMP NOT NULL,
    coc_act BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (cli_id, coc_can, coc_ide),
    CHECK (coc_can IN ('SIMULADO','EMAIL','WHATSAPP','INTERNO'))
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_mensajes_externo ON mensajes(cli_id, men_id_ext) WHERE men_id_ext IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_comunicaciones_bandeja ON comunicaciones(cli_id, com_act, com_est, com_fec_ult DESC);
CREATE INDEX IF NOT EXISTS idx_comunicaciones_asignacion ON comunicaciones(cli_id, usu_id, are_id);
CREATE INDEX IF NOT EXISTS idx_mensajes_comunicacion ON mensajes(cli_id, com_id, men_sec);
CREATE INDEX IF NOT EXISTS idx_contactos_persona ON contactos_canal(cli_id, per_id);

COMMIT;
