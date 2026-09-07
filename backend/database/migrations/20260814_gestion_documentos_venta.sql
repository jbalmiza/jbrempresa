CREATE TABLE IF NOT EXISTS documentos_venta_movimientos (
    dvm_id BIGSERIAL PRIMARY KEY,
    cli_id BIGINT NOT NULL,
    dov_id BIGINT NOT NULL,
    dov_tip VARCHAR(3) NOT NULL,
    dov_num VARCHAR(30) NOT NULL,
    per_id BIGINT NOT NULL,
    dov_fec DATE NOT NULL,
    dov_est VARCHAR(20) NOT NULL,
    dov_imp_tot NUMERIC(14,2),
    dvm_tip_mov VARCHAR(20) NOT NULL,
    dvm_cau_mov VARCHAR(500),
    dvm_usu_mov VARCHAR(100) NOT NULL,
    dvm_fec_mov TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_dvm_cliente_documento
    ON documentos_venta_movimientos (cli_id, dov_id, dvm_fec_mov DESC);

INSERT INTO documentos_venta_movimientos
    (cli_id,dov_id,dov_tip,dov_num,per_id,dov_fec,dov_est,dov_imp_tot,
     dvm_tip_mov,dvm_cau_mov,dvm_usu_mov,dvm_fec_mov)
SELECT d.cli_id,d.dov_id,d.dov_tip,d.dov_num,d.per_id,d.dov_fec,d.dov_est,d.dov_imp_tot,
       'ALTA','Movimiento inicial',d.dov_usu_mov,d.dov_fec_mov
FROM documentos_venta d
WHERE NOT EXISTS (SELECT 1 FROM documentos_venta_movimientos m
                  WHERE m.cli_id=d.cli_id AND m.dov_id=d.dov_id);
