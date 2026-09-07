BEGIN;
CREATE TABLE IF NOT EXISTS cajas (
 caj_id BIGSERIAL PRIMARY KEY, emp_id BIGINT NOT NULL, caj_nom VARCHAR(100) NOT NULL,
 caj_des VARCHAR(500), caj_act BOOLEAN NOT NULL DEFAULT TRUE, caj_usu_mov VARCHAR(100) NOT NULL,
 caj_fec_mov TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT uk_caja_nombre UNIQUE(emp_id,caj_nom)
);
CREATE TABLE IF NOT EXISTS caja_sesiones (
 cas_id BIGSERIAL PRIMARY KEY, emp_id BIGINT NOT NULL, caj_id BIGINT NOT NULL,
 cas_est VARCHAR(10) NOT NULL, cas_fec_ape TIMESTAMP NOT NULL, cas_imp_ini NUMERIC(14,2) NOT NULL,
 cas_fec_cie TIMESTAMP, cas_imp_esp NUMERIC(14,2), cas_imp_con NUMERIC(14,2), cas_imp_dif NUMERIC(14,2),
 cas_obs VARCHAR(500), cas_usu_mov VARCHAR(100) NOT NULL, cas_fec_mov TIMESTAMP NOT NULL,
 CONSTRAINT fk_caja_sesion_caja FOREIGN KEY(caj_id) REFERENCES cajas(caj_id),
 CONSTRAINT ck_caja_sesion_estado CHECK(cas_est IN ('ABIERTA','CERRADA'))
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_caja_sesion_abierta ON caja_sesiones(emp_id,caj_id) WHERE cas_est='ABIERTA';
CREATE TABLE IF NOT EXISTS caja_movimientos (
 cam_id BIGSERIAL PRIMARY KEY, emp_id BIGINT NOT NULL, cas_id BIGINT NOT NULL,
 cam_tip VARCHAR(20) NOT NULL, cam_med VARCHAR(20) NOT NULL, cam_imp NUMERIC(14,2) NOT NULL,
 cam_con VARCHAR(200) NOT NULL, per_id BIGINT, dov_id BIGINT, cam_obs VARCHAR(500),
 cam_act BOOLEAN NOT NULL DEFAULT TRUE, cam_usu_mov VARCHAR(100) NOT NULL, cam_fec_mov TIMESTAMP NOT NULL,
 CONSTRAINT fk_caja_movimiento_sesion FOREIGN KEY(cas_id) REFERENCES caja_sesiones(cas_id),
 CONSTRAINT ck_caja_movimiento_tipo CHECK(cam_tip IN ('ENTRADA','SALIDA','COBRO','DEVOLUCION')),
 CONSTRAINT ck_caja_movimiento_medio CHECK(cam_med IN ('EFECTIVO','TARJETA','TRANSFERENCIA','OTRO')),
 CONSTRAINT ck_caja_movimiento_importe CHECK(cam_imp>0)
);
CREATE INDEX IF NOT EXISTS idx_caja_movimientos_sesion ON caja_movimientos(emp_id,cas_id,cam_fec_mov);
COMMIT;
