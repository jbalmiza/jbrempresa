CREATE TABLE IF NOT EXISTS avisos_alertas (
 avi_id BIGINT NOT NULL, avi_id_his BIGINT NOT NULL, emp_id BIGINT NOT NULL,
 avi_tip_mov VARCHAR(1) NOT NULL,
 avi_cau_mov VARCHAR(500), avi_tipo VARCHAR(10) NOT NULL,
 avi_titulo VARCHAR(150) NOT NULL, avi_mensaje VARCHAR(1000) NOT NULL,
 avi_fec_ini TIMESTAMP, avi_fec_fin TIMESTAMP,
 avi_usu_mov VARCHAR(100) NOT NULL, avi_fec_mov TIMESTAMP NOT NULL, avi_act BOOLEAN NOT NULL,
 PRIMARY KEY (emp_id,avi_id,avi_id_his),
 CONSTRAINT ck_avisos_alertas_tipo CHECK (avi_tipo IN ('AVISO','ALERTA')),
 CONSTRAINT ck_avisos_alertas_periodo CHECK (avi_fec_fin IS NULL OR avi_fec_ini IS NULL OR avi_fec_fin >= avi_fec_ini)
);

INSERT INTO avisos_alertas(avi_id,avi_id_his,emp_id,avi_tip_mov,avi_cau_mov,avi_tipo,avi_titulo,avi_mensaje,avi_fec_ini,avi_fec_fin,avi_usu_mov,avi_fec_mov,avi_act)
SELECT v.avi_id,1,e.emp_id,'A','Alta de ejemplos iniciales',v.tipo,v.titulo,v.mensaje,CURRENT_TIMESTAMP,NULL,'MIGRACION',CURRENT_TIMESTAMP,TRUE
FROM empresas e CROSS JOIN (VALUES
 (1::BIGINT,'AVISO','Horario especial','Consulta nuestro horario especial antes de realizar tu pedido.'),
 (2::BIGINT,'ALERTA','Alta demanda','En momentos de alta demanda el tiempo de preparación puede aumentar.'),
 (3::BIGINT,'AVISO','Pedidos personalizados','Indica cualquier observación especial al añadir el producto o servicio.')
) AS v(avi_id,tipo,titulo,mensaje)
WHERE NOT EXISTS(SELECT 1 FROM avisos_alertas a WHERE a.emp_id=e.emp_id AND a.avi_id=v.avi_id);
