CREATE TABLE IF NOT EXISTS productos_componentes (
 prc_id BIGSERIAL PRIMARY KEY,
 emp_id BIGINT NOT NULL,
 pro_id BIGINT NOT NULL,
 pro_id_his BIGINT NOT NULL,
 cmp_id BIGINT NOT NULL,
 prc_can NUMERIC(14,4) NOT NULL,
 CONSTRAINT uk_producto_componente UNIQUE(emp_id,pro_id,pro_id_his,cmp_id),
 CONSTRAINT fk_producto_componente_producto FOREIGN KEY(pro_id,pro_id_his) REFERENCES productos(pro_id,pro_id_his) ON DELETE CASCADE
);
