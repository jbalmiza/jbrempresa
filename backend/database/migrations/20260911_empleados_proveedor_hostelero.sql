-- Tres empleados operativos con usuario propio para Proveedor Hostelero Central.

BEGIN;

DROP INDEX IF EXISTS ux_usuario_persona_empresa;
DROP INDEX IF EXISTS ux_usuario_persona_cliente;

INSERT INTO personas(per_id,per_id_his,emp_id,per_tip_mov,per_cau_mov,per_tip_per,per_tip_doc,per_doc,per_nom,per_ape1,per_nom_com,per_tel,per_ema,per_dat_com,per_usu_mov,per_fec_mov,per_act) VALUES
(37,1,4,'A','Alta de plantilla inicial','FISICA','INT','PROVEEDOR-ALMACEN-4','Laura','Martín','Laura Martín · Almacén','600000041','almacen4@pruebas.local',FALSE,'SISTEMA',CURRENT_TIMESTAMP,TRUE),
(38,1,4,'A','Alta de plantilla inicial','FISICA','INT','PROVEEDOR-REPARTO-4','Miguel','Santos','Miguel Santos · Reparto','600000042','reparto4@pruebas.local',FALSE,'SISTEMA',CURRENT_TIMESTAMP,TRUE),
(39,1,4,'A','Alta de plantilla inicial','FISICA','INT','PROVEEDOR-MANT-4','Raúl','Ortega','Raúl Ortega · Mantenimiento','600000043','mantenimiento4@pruebas.local',FALSE,'SISTEMA',CURRENT_TIMESTAMP,TRUE)
ON CONFLICT(per_id,per_id_his) DO NOTHING;

INSERT INTO usuarios(usu_id,emp_id,per_id,usu_per_id,usu_usu,usu_con,usu_nom,usu_ema,usu_tel,usu_tip_mov,usu_cau_mov,usu_usu_mov,usu_fec_mov,usu_act)
SELECT datos.usu_id,4,17,datos.per_id,datos.usuario,base.usu_con,datos.nombre,datos.correo,datos.telefono,
       'A','Alta de usuario empleado de empresa proveedora','SISTEMA',CURRENT_TIMESTAMP,'true'
FROM (VALUES
  (17::bigint,37::bigint,'almacen4','Laura Martín · Almacén','almacen4@pruebas.local','600000041'),
  (18::bigint,38::bigint,'reparto4','Miguel Santos · Reparto','reparto4@pruebas.local','600000042'),
  (19::bigint,39::bigint,'mantenimiento4','Raúl Ortega · Mantenimiento','mantenimiento4@pruebas.local','600000043')
) AS datos(usu_id,per_id,usuario,nombre,correo,telefono)
CROSS JOIN (SELECT usu_con FROM usuarios WHERE usu_usu='empleado4') base
ON CONFLICT(usu_id) DO UPDATE SET
  emp_id=EXCLUDED.emp_id,per_id=EXCLUDED.per_id,usu_per_id=EXCLUDED.usu_per_id,
  usu_usu=EXCLUDED.usu_usu,usu_con=EXCLUDED.usu_con,usu_nom=EXCLUDED.usu_nom,
  usu_ema=EXCLUDED.usu_ema,usu_tel=EXCLUDED.usu_tel,usu_act='true',usu_fec_mov=CURRENT_TIMESTAMP;

CREATE UNIQUE INDEX ux_usuario_persona_cliente ON usuarios(emp_id,usu_per_id);

INSERT INTO recursos_operativos(reo_id,reo_id_his,emp_id,reo_nom,reo_tip,per_id,reo_des,reo_tip_mov,reo_cau_mov,reo_ope,reo_act,reo_usu_mov,reo_fec_mov) VALUES
(22,1,4,'Laura Martín · Almacén','EMPLEADO',37,'Preparación y expedición de suministros hosteleros.','A','Alta de plantilla inicial',TRUE,TRUE,'SISTEMA',CURRENT_TIMESTAMP),
(23,1,4,'Miguel Santos · Reparto','EMPLEADO',38,'Distribución refrigerada y entrega a clientes.','A','Alta de plantilla inicial',TRUE,TRUE,'SISTEMA',CURRENT_TIMESTAMP),
(24,1,4,'Raúl Ortega · Mantenimiento','EMPLEADO',39,'Mantenimiento preventivo de equipamiento hostelero.','A','Alta de plantilla inicial',TRUE,TRUE,'SISTEMA',CURRENT_TIMESTAMP)
ON CONFLICT(reo_id,reo_id_his) DO NOTHING;

INSERT INTO recursos_agendables(rag_id,emp_id,rag_tip,rag_ref_id,rag_nom,rag_cap,rag_mar_pre,rag_mar_pos,rag_hor_vis,rag_usu_mov,rag_fec_mov,rag_act) VALUES
(26,4,'EMPLEADO',22,'Laura Martín · Almacén',1,15,15,'07:00','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(27,4,'EMPLEADO',23,'Miguel Santos · Reparto',1,15,15,'07:00','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(28,4,'EMPLEADO',24,'Raúl Ortega · Mantenimiento',1,15,15,'08:00','SISTEMA',CURRENT_TIMESTAMP,TRUE)
ON CONFLICT(emp_id,rag_tip,rag_ref_id) DO NOTHING;

INSERT INTO horarios_recurso(emp_id,rag_id,hor_dia,hor_ini,hor_fin,hor_usu_mov,hor_fec_mov,hor_act)
SELECT 4,agenda.rag_id,dia.dia,agenda.inicio,agenda.fin,'SISTEMA',CURRENT_TIMESTAMP,TRUE
FROM (VALUES
  (26::bigint,'07:00'::time,'15:00'::time),
  (27::bigint,'07:00'::time,'15:00'::time),
  (28::bigint,'08:00'::time,'16:00'::time)
) AS agenda(rag_id,inicio,fin)
CROSS JOIN (SELECT generate_series(1,5)::smallint AS dia) dia
WHERE NOT EXISTS(SELECT 1 FROM horarios_recurso h WHERE h.emp_id=4 AND h.rag_id=agenda.rag_id AND h.hor_dia=dia.dia);

INSERT INTO recursos_capacidades(emp_id,reo_id,rec_ori,rec_tip,rec_act) VALUES
(4,22,'PRODUCTO','INGREDIENTES',TRUE),(4,22,'PRODUCTO','BEBIDAS',TRUE),
(4,22,'PRODUCTO','CONSUMIBLES',TRUE),(4,22,'PRODUCTO','LIMPIEZA',TRUE),
(4,23,'SERVICIO','LOGÍSTICA',TRUE),(4,24,'SERVICIO','MANTENIMIENTO',TRUE)
ON CONFLICT(emp_id,reo_id,rec_ori,rec_tip) DO NOTHING;

SELECT setval(pg_get_serial_sequence('usuarios','usu_id'),GREATEST((SELECT MAX(usu_id) FROM usuarios),1),TRUE);
SELECT setval(pg_get_serial_sequence('recursos_agendables','rag_id'),GREATEST((SELECT MAX(rag_id) FROM recursos_agendables),1),TRUE);

COMMIT;
