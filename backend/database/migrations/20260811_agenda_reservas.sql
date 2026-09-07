-- Modelo genérico de agenda, disponibilidad, reservas y trabajo operativo.
CREATE TABLE IF NOT EXISTS recursos_agendables (
    rag_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    rag_tip VARCHAR(20) NOT NULL,
    rag_ref_id BIGINT NOT NULL,
    rag_nom VARCHAR(200) NOT NULL,
    rag_cap INTEGER NOT NULL DEFAULT 1,
    rag_mar_pre INTEGER NOT NULL DEFAULT 0,
    rag_mar_pos INTEGER NOT NULL DEFAULT 0,
    rag_hor_vis TIME NOT NULL DEFAULT '08:00',
    rag_usu_mov VARCHAR(100) NOT NULL,
    rag_fec_mov TIMESTAMP NOT NULL,
    rag_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_recurso_agendable UNIQUE (cli_id, rag_tip, rag_ref_id),
    CONSTRAINT ck_recurso_tipo CHECK (rag_tip IN ('PERSONA','DOMICILIO')),
    CONSTRAINT ck_recurso_capacidad CHECK (rag_cap > 0),
    CONSTRAINT ck_recurso_margenes CHECK (rag_mar_pre >= 0 AND rag_mar_pos >= 0)
);

CREATE TABLE IF NOT EXISTS horarios_recurso (
    hor_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    rag_id BIGINT NOT NULL,
    hor_dia SMALLINT NOT NULL,
    hor_ini TIME NOT NULL,
    hor_fin TIME NOT NULL,
    hor_usu_mov VARCHAR(100) NOT NULL,
    hor_fec_mov TIMESTAMP NOT NULL,
    hor_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_horario_recurso FOREIGN KEY (rag_id) REFERENCES recursos_agendables(rag_id),
    CONSTRAINT ck_horario_dia CHECK (hor_dia BETWEEN 1 AND 7),
    CONSTRAINT ck_horario_franja CHECK (hor_ini < hor_fin),
    CONSTRAINT uk_horario_recurso UNIQUE (cli_id, rag_id, hor_dia, hor_ini, hor_fin)
);

CREATE TABLE IF NOT EXISTS excepciones_recurso (
    exr_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    rag_id BIGINT NOT NULL,
    exr_fec DATE NOT NULL,
    exr_ini TIME,
    exr_fin TIME,
    exr_dis BOOLEAN NOT NULL DEFAULT FALSE,
    exr_cap INTEGER,
    exr_mot VARCHAR(300),
    exr_usu_mov VARCHAR(100) NOT NULL,
    exr_fec_mov TIMESTAMP NOT NULL,
    exr_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_excepcion_recurso FOREIGN KEY (rag_id) REFERENCES recursos_agendables(rag_id),
    CONSTRAINT ck_excepcion_franja CHECK ((exr_ini IS NULL AND exr_fin IS NULL) OR (exr_ini IS NOT NULL AND exr_fin IS NOT NULL AND exr_ini < exr_fin)),
    CONSTRAINT ck_excepcion_capacidad CHECK (exr_cap IS NULL OR exr_cap > 0)
);

CREATE TABLE IF NOT EXISTS reservas (
    res_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    clc_id BIGINT,
    dov_id BIGINT,
    com_id BIGINT,
    res_ini TIMESTAMP NOT NULL,
    res_fin TIMESTAMP NOT NULL,
    res_est VARCHAR(20) NOT NULL,
    res_dur_min INTEGER NOT NULL,
    res_tit VARCHAR(200) NOT NULL,
    res_obs VARCHAR(500),
    res_usu_mov VARCHAR(100) NOT NULL,
    res_fec_mov TIMESTAMP NOT NULL,
    res_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (clc_id) REFERENCES clientes_comerciales(clc_id),
    CONSTRAINT fk_reserva_documento FOREIGN KEY (dov_id) REFERENCES documentos_venta(dov_id),
    CONSTRAINT fk_reserva_comunicacion FOREIGN KEY (com_id) REFERENCES comunicaciones(com_id),
    CONSTRAINT ck_reserva_fechas CHECK (res_ini < res_fin),
    CONSTRAINT ck_reserva_duracion CHECK (res_dur_min > 0),
    CONSTRAINT ck_reserva_estado CHECK (res_est IN ('PENDIENTE','CONFIRMADA','EN_CURSO','TERMINADA','CANCELADA','AUSENCIA'))
);

CREATE TABLE IF NOT EXISTS reserva_recursos (
    rer_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    res_id BIGINT NOT NULL,
    rag_id BIGINT NOT NULL,
    rer_ini_ocu TIMESTAMP NOT NULL,
    rer_fin_ocu TIMESTAMP NOT NULL,
    rer_usu_mov VARCHAR(100) NOT NULL,
    rer_fec_mov TIMESTAMP NOT NULL,
    rer_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_reserva_recurso_reserva FOREIGN KEY (res_id) REFERENCES reservas(res_id),
    CONSTRAINT fk_reserva_recurso_recurso FOREIGN KEY (rag_id) REFERENCES recursos_agendables(rag_id),
    CONSTRAINT ck_reserva_recurso_fechas CHECK (rer_ini_ocu < rer_fin_ocu),
    CONSTRAINT uk_reserva_recurso UNIQUE (cli_id, res_id)
);

CREATE TABLE IF NOT EXISTS tareas_reserva (
    tar_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    res_id BIGINT NOT NULL,
    rag_id BIGINT,
    tar_ord INTEGER NOT NULL,
    tar_tit VARCHAR(200) NOT NULL,
    tar_dur_min INTEGER NOT NULL,
    tar_est VARCHAR(20) NOT NULL,
    tar_usu_mov VARCHAR(100) NOT NULL,
    tar_fec_mov TIMESTAMP NOT NULL,
    tar_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_tarea_reserva FOREIGN KEY (res_id) REFERENCES reservas(res_id),
    CONSTRAINT fk_tarea_recurso FOREIGN KEY (rag_id) REFERENCES recursos_agendables(rag_id),
    CONSTRAINT ck_tarea_orden CHECK (tar_ord > 0),
    CONSTRAINT ck_tarea_duracion CHECK (tar_dur_min > 0),
    CONSTRAINT ck_tarea_estado CHECK (tar_est IN ('PENDIENTE','EN_CURSO','TERMINADA','CANCELADA')),
    CONSTRAINT uk_tarea_reserva_orden UNIQUE (cli_id, res_id, tar_ord)
);

CREATE TABLE IF NOT EXISTS reprogramaciones_reserva (
    rpr_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    res_id BIGINT NOT NULL,
    rpr_ini_ant TIMESTAMP NOT NULL,
    rpr_fin_ant TIMESTAMP NOT NULL,
    rpr_ini_nue TIMESTAMP NOT NULL,
    rpr_fin_nue TIMESTAMP NOT NULL,
    rpr_mot VARCHAR(300),
    rpr_usu_mov VARCHAR(100) NOT NULL,
    rpr_fec_mov TIMESTAMP NOT NULL,
    rpr_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_reprogramacion_reserva FOREIGN KEY (res_id) REFERENCES reservas(res_id)
);

CREATE INDEX IF NOT EXISTS idx_horarios_recurso ON horarios_recurso(cli_id, rag_id, hor_dia);
CREATE INDEX IF NOT EXISTS idx_excepciones_recurso ON excepciones_recurso(cli_id, rag_id, exr_fec);
CREATE INDEX IF NOT EXISTS idx_reservas_periodo ON reservas(cli_id, res_ini, res_fin);
CREATE INDEX IF NOT EXISTS idx_reserva_recursos_ocupacion ON reserva_recursos(cli_id, rag_id, rer_ini_ocu, rer_fin_ocu);
CREATE INDEX IF NOT EXISTS idx_tareas_reserva ON tareas_reserva(cli_id, res_id, tar_ord);
