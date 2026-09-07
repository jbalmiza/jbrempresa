BEGIN;

CREATE TABLE IF NOT EXISTS areas_organizativas (
    are_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    are_cod VARCHAR(30) NOT NULL,
    are_nom VARCHAR(100) NOT NULL,
    are_des VARCHAR(500),
    are_id_pad BIGINT,
    are_usu_mov VARCHAR(50) NOT NULL,
    are_fec_mov TIMESTAMP NOT NULL,
    are_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_area_pad FOREIGN KEY (are_id_pad) REFERENCES areas_organizativas(are_id),
    CONSTRAINT ux_area_codigo_cliente UNIQUE (cli_id, are_cod)
);

CREATE TABLE IF NOT EXISTS personal_area (
    pea_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    are_id BIGINT NOT NULL,
    per_id BIGINT NOT NULL,
    pea_car VARCHAR(100),
    pea_res BOOLEAN NOT NULL DEFAULT FALSE,
    pea_pri BOOLEAN NOT NULL DEFAULT FALSE,
    pea_fec_des DATE,
    pea_fec_has DATE,
    pea_usu_mov VARCHAR(50) NOT NULL,
    pea_fec_mov TIMESTAMP NOT NULL,
    pea_act BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_personal_area FOREIGN KEY (are_id) REFERENCES areas_organizativas(are_id),
    CONSTRAINT ck_personal_fechas CHECK (pea_fec_has IS NULL OR pea_fec_des IS NULL OR pea_fec_has >= pea_fec_des)
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_personal_area_activo
    ON personal_area (cli_id, are_id, per_id) WHERE pea_act = TRUE;
CREATE UNIQUE INDEX IF NOT EXISTS ux_personal_area_principal
    ON personal_area (cli_id, per_id) WHERE pea_act = TRUE AND pea_pri = TRUE;

ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS usu_per_id BIGINT;

-- Los usuarios del cliente 1 ya siguen la correspondencia Usuario-Persona existente.
UPDATE usuarios u SET usu_per_id = u.usu_id
WHERE u.usu_per_id IS NULL
  AND EXISTS (SELECT 1 FROM personas p WHERE p.cli_id=u.cli_id AND p.per_id=u.usu_id AND p.per_act=TRUE);

-- Regularización aprobada de usuarios que no disponían de Persona.
WITH pendientes AS (
    SELECT u.*, (SELECT COALESCE(MAX(per_id),0) FROM personas)
           + ROW_NUMBER() OVER (ORDER BY u.cli_id,u.usu_id) AS nuevo_per_id
    FROM usuarios u
    WHERE u.usu_per_id IS NULL
), insertadas AS (
    INSERT INTO personas
      (per_id,per_id_his,per_tip_mov,cli_id,per_tip_doc,per_doc,per_nom,per_ape1,
       per_nom_com,per_ema,per_usu_mov,per_fec_mov,per_act,per_hus)
    SELECT nuevo_per_id,1,'A',cli_id,'INT','USUARIO-'||usu_id,usu_nom,'Sin especificar',
           usu_nom,usu_ema,usu_usu,CURRENT_TIMESTAMP,TRUE,25830
    FROM pendientes
    RETURNING per_id,cli_id,per_doc
)
UPDATE usuarios u
SET usu_per_id=i.per_id
FROM insertadas i
WHERE u.cli_id=i.cli_id AND i.per_doc='USUARIO-'||u.usu_id;

ALTER TABLE usuarios ALTER COLUMN usu_per_id SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS ux_usuario_persona_cliente ON usuarios (cli_id, usu_per_id);

COMMIT;
