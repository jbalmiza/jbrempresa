CREATE TABLE IF NOT EXISTS servicios (
 ser_id BIGINT NOT NULL, ser_id_his BIGINT NOT NULL, cli_id BIGINT NOT NULL,
 ser_tip_mov VARCHAR(1) NOT NULL, ser_tip_ser VARCHAR(50) NOT NULL,
 ser_nom VARCHAR(100) NOT NULL, ser_des VARCHAR(500), ser_cat VARCHAR(100) NOT NULL,
 ser_sub_cat VARCHAR(100), ser_dur_min INTEGER NOT NULL,
 ser_pre_ven NUMERIC(14,2) NOT NULL, ser_pre_des NUMERIC(14,2) DEFAULT 0,
 ser_pre_iva NUMERIC(7,2) NOT NULL, ser_pre_fin NUMERIC(14,2) NOT NULL,
 ser_obs VARCHAR(500), ser_usu_mov VARCHAR(100) NOT NULL,
 ser_fec_mov TIMESTAMP NOT NULL, ser_act BOOLEAN NOT NULL,
 CONSTRAINT pk_servicios PRIMARY KEY (ser_id, ser_id_his),
 CONSTRAINT ck_servicios_duracion CHECK (ser_dur_min > 0 AND MOD(ser_dur_min,5)=0)
);
CREATE INDEX IF NOT EXISTS ix_servicios_cliente_activo ON servicios(cli_id,ser_act,ser_id);
