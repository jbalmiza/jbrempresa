CREATE TABLE IF NOT EXISTS propuestas_respuesta (
    prr_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    com_id BIGINT NOT NULL,
    prr_con TEXT NOT NULL,
    prr_con_apr TEXT,
    prr_ori VARCHAR(30) NOT NULL,
    prr_est VARCHAR(20) NOT NULL,
    prr_usu_gen VARCHAR(50) NOT NULL,
    prr_fec_gen TIMESTAMP NOT NULL,
    prr_usu_apr VARCHAR(50),
    prr_fec_apr TIMESTAMP,
    CONSTRAINT fk_prr_comunicacion FOREIGN KEY (com_id) REFERENCES comunicaciones(com_id)
);
CREATE INDEX IF NOT EXISTS idx_prr_conversacion ON propuestas_respuesta(cli_id, com_id, prr_fec_gen DESC);
ALTER TABLE mensajes ALTER COLUMN men_est TYPE VARCHAR(30);
