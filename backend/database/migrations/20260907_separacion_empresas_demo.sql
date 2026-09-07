-- Reorganización integral de los datos de demostración en tres empresas coherentes.
-- Los datos existentes son de prueba; se eliminan operaciones incompatibles antes
-- de reasignar maestros para no conservar relaciones cruzadas entre empresas.

BEGIN;

UPDATE empresas SET emp_nom='Pizzeria La Esquina', emp_act='true', emp_usu_mov='MIGRACION', emp_fec_mov=CURRENT_TIMESTAMP WHERE emp_id=1;
UPDATE empresas SET emp_nom='Taller Bosco de coches', emp_act='true', emp_usu_mov='MIGRACION', emp_fec_mov=CURRENT_TIMESTAMP WHERE emp_id=2;
UPDATE empresas SET emp_nom='Restaurante Cándida', emp_act='true', emp_usu_mov='MIGRACION', emp_fec_mov=CURRENT_TIMESTAMP WHERE emp_id=3;

-- Un usuario operativo por empresa. Se conservan sus contraseñas actuales.
DELETE FROM recuperaciones_contrasena WHERE usu_id IN (4,5);
DELETE FROM usuarios WHERE usu_id NOT IN (1,2,3);
UPDATE usuarios SET emp_id=1, usu_per_id=1, usu_nom='1 · Alejandro Moreno Ruiz', usu_usu_mov='MIGRACION', usu_fec_mov=CURRENT_TIMESTAMP WHERE usu_id=1;
UPDATE usuarios SET emp_id=2, usu_per_id=2, usu_nom='2 · Lucía Navarro Santos', usu_usu_mov='MIGRACION', usu_fec_mov=CURRENT_TIMESTAMP WHERE usu_id=2;
UPDATE usuarios SET emp_id=3, usu_per_id=3, usu_nom='3 · Daniel Romero Vega', usu_usu_mov='MIGRACION', usu_fec_mov=CURRENT_TIMESTAMP WHERE usu_id=3;

-- Cada usuario mantiene una Persona perteneciente a su propia empresa.
UPDATE personas SET emp_id=2 WHERE per_id IN (2,8,9);
UPDATE personas SET emp_id=3 WHERE per_id IN (3,10,11);
UPDATE personas SET per_act=FALSE WHERE per_id=1;
UPDATE personas SET per_act=TRUE, per_tip_mov='M', per_cau_mov='Reactivación para plantilla de demostración', per_usu_mov='MIGRACION', per_fec_mov=CURRENT_TIMESTAMP
WHERE per_id=1 AND per_id_his=(SELECT MAX(p2.per_id_his) FROM personas p2 WHERE p2.per_id=1);

-- Los perfiles usados por los tres usuarios quedan como administradores de su empresa.
UPDATE perfiles SET emp_id=1, per_nom='Administrador', per_mod_adm='true', per_mod_per='true', per_mod_pro='true', per_mod_ter='true', per_mod_ven='true', per_act='true', per_usu_mov='MIGRACION', per_fec_mov=CURRENT_TIMESTAMP WHERE per_id=1;
UPDATE perfiles SET emp_id=2, per_nom='Administrador', per_mod_adm='true', per_mod_per='true', per_mod_pro='true', per_mod_ter='true', per_mod_ven='true', per_act='true', per_usu_mov='MIGRACION', per_fec_mov=CURRENT_TIMESTAMP WHERE per_id=2;
UPDATE perfiles SET emp_id=3, per_nom='Administrador', per_mod_adm='true', per_mod_per='true', per_mod_pro='true', per_mod_ter='true', per_mod_ven='true', per_act='true', per_usu_mov='MIGRACION', per_fec_mov=CURRENT_TIMESTAMP WHERE per_id=3;

-- Se descartan operaciones de prueba que referencian productos, servicios o agendas
-- que cambian de empresa.
DELETE FROM tareas_reserva;
DELETE FROM reprogramaciones_reserva;
DELETE FROM reserva_recursos;
DELETE FROM reservas;
DELETE FROM horarios_recurso;
DELETE FROM excepciones_recurso;
DELETE FROM recursos_agendables;
DELETE FROM recursos_capacidades;
DELETE FROM documentos_venta_detalle;
DELETE FROM documentos_venta_movimientos;
DELETE FROM documentos_venta;
DELETE FROM documentos_venta_numeradores;
DELETE FROM catalogo_posiciones;
DELETE FROM mallas WHERE mal_ent IN ('PRODUCTOS','SERVICIOS','RECURSOS');
DELETE FROM recursos_operativos;

-- Tres empleados por empresa y maquinaria coherente con la actividad.
INSERT INTO recursos_operativos(reo_id,reo_id_his,emp_id,reo_nom,reo_tip,per_id,reo_des,reo_tip_mov,reo_cau_mov,reo_ope,reo_act,reo_usu_mov,reo_fec_mov) VALUES
(1,1,1,'Alejandro Moreno · Maestro pizzero','EMPLEADO',1,'Responsable de pizzas y cocina caliente.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(2,1,1,'Elena Vargas · Reparto','EMPLEADO',6,'Responsable de reparto y entrega de pedidos.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(3,1,1,'Pablo Serrano · Cocina','EMPLEADO',7,'Preparación de bocadillos y hamburguesas.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(4,1,2,'Lucía Navarro · Mecánica','EMPLEADO',2,'Mantenimiento general y revisiones.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(5,1,2,'Marta Delgado · Electricidad','EMPLEADO',8,'Diagnosis, electricidad y climatización.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(6,1,2,'Sergio Herrera · Neumáticos','EMPLEADO',9,'Frenos, dirección y neumáticos.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(7,1,3,'Daniel Romero · Jefe de sala','EMPLEADO',3,'Atención en sala y gestión de reservas.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(8,1,3,'Natalia Gil · Cocina','EMPLEADO',10,'Responsable de cocina y menú degustación.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(9,1,3,'Álvaro Méndez · Sala y eventos','EMPLEADO',11,'Servicio de sala y celebraciones.','A','Plantilla inicial de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(10,1,1,'Horno de pizzas 1','MAQUINARIA',NULL,'Horno principal de la pizzería.','A','Equipamiento de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(11,1,1,'Estación de bocadillos 1','MAQUINARIA',NULL,'Puesto de preparación de bocadillos.','A','Equipamiento de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(12,1,2,'Elevador de taller 1','MAQUINARIA',NULL,'Elevador principal del taller.','A','Equipamiento de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP),
(13,1,2,'Elevador de taller 2','MAQUINARIA',NULL,'Elevador auxiliar del taller.','A','Equipamiento de demostración',TRUE,TRUE,'MIGRACION',CURRENT_TIMESTAMP);

-- Catálogo: la pizzería conserva bocadillos, pizzas y hamburguesas; el restaurante
-- recibe el resto de la carta. El taller no comercializa estos productos de hostelería.
UPDATE productos SET emp_id=1, pro_cau_mov='Catálogo asignado a Pizzeria La Esquina', pro_usu_mov='MIGRACION', pro_fec_mov=CURRENT_TIMESTAMP
WHERE pro_tip_pro IN ('BOCADILLO','PIZZA','HAMBURGUESA');
UPDATE productos SET emp_id=3, pro_cau_mov='Catálogo asignado a Restaurante Cándida', pro_usu_mov='MIGRACION', pro_fec_mov=CURRENT_TIMESTAMP
WHERE pro_tip_pro NOT IN ('BOCADILLO','PIZZA','HAMBURGUESA');

-- Los veinte servicios actuales son propios del taller.
UPDATE servicios SET emp_id=2, ser_cau_mov='Catálogo asignado a Taller Bosco de coches', ser_usu_mov='MIGRACION', ser_fec_mov=CURRENT_TIMESTAMP
WHERE ser_tip_ser='TALLER';

-- Servicios propios de la pizzería y del restaurante.
INSERT INTO servicios(ser_id,ser_id_his,emp_id,ser_tip_mov,ser_cau_mov,ser_tip_ser,ser_nom,ser_des,ser_cat,ser_sub_cat,ser_dur_min,ser_pre_ven,ser_pre_des,ser_pre_iva,ser_pre_fin,ser_obs,ser_vis_cat,ser_ima,ser_usu_mov,ser_fec_mov,ser_act) VALUES
(21,1,1,'A','Alta de catálogo inicial','REPARTO','Reparto a domicilio','Entrega del pedido en el domicilio indicado.','Atención al cliente','Entrega',30,2.73,0,10,3.00,'Servicio de demostración de Pizzeria La Esquina',TRUE,'servicio-predeterminado.png','MIGRACION',CURRENT_TIMESTAMP,TRUE),
(22,1,1,'A','Alta de catálogo inicial','RECOGIDA','Pedido preparado para recoger','Preparación prioritaria para recogida en el establecimiento.','Atención al cliente','Recogida',15,0,0,10,0,'Servicio de demostración de Pizzeria La Esquina',TRUE,'servicio-predeterminado.png','MIGRACION',CURRENT_TIMESTAMP,TRUE),
(23,1,1,'A','Alta de catálogo inicial','EVENTOS','Preparación para celebraciones','Preparación de pizzas, bocadillos y hamburguesas para grupos.','Eventos','Celebraciones',90,54.55,0,10,60.00,'Servicio de demostración de Pizzeria La Esquina',TRUE,'servicio-predeterminado.png','MIGRACION',CURRENT_TIMESTAMP,TRUE),
(24,1,3,'A','Alta de catálogo inicial','RESERVA_MESA','Reserva de mesa','Reserva y preparación de mesa en sala.','Sala','Reservas',15,0,0,10,0,'Servicio de demostración de Restaurante Cándida',TRUE,'servicio-predeterminado.png','MIGRACION',CURRENT_TIMESTAMP,TRUE),
(25,1,3,'A','Alta de catálogo inicial','MENU_DEGUSTACION','Menú degustación','Experiencia gastronómica con selección de platos de temporada.','Restauración','Degustación',120,45.45,0,10,50.00,'Servicio de demostración de Restaurante Cándida',TRUE,'servicio-predeterminado.png','MIGRACION',CURRENT_TIMESTAMP,TRUE),
(26,1,3,'A','Alta de catálogo inicial','EVENTOS','Celebración de eventos','Organización de comidas de empresa y celebraciones privadas.','Eventos','Celebraciones',180,90.91,0,10,100.00,'Servicio de demostración de Restaurante Cándida',TRUE,'servicio-predeterminado.png','MIGRACION',CURRENT_TIMESTAMP,TRUE);

-- Capacidades de los empleados según su puesto.
INSERT INTO recursos_capacidades(emp_id,reo_id,rec_ori,rec_tip,rec_act) VALUES
(1,1,'PRODUCTO','PIZZA',TRUE),(1,2,'SERVICIO','REPARTO',TRUE),(1,2,'SERVICIO','RECOGIDA',TRUE),
(1,3,'PRODUCTO','BOCADILLO',TRUE),(1,3,'PRODUCTO','HAMBURGUESA',TRUE),(1,1,'SERVICIO','EVENTOS',TRUE),
(2,4,'SERVICIO','TALLER',TRUE),(2,5,'SERVICIO','TALLER',TRUE),(2,6,'SERVICIO','TALLER',TRUE),
(3,7,'SERVICIO','RESERVA_MESA',TRUE),(3,7,'PRODUCTO','BEBIDA',TRUE),(3,7,'PRODUCTO','VINO',TRUE),
(3,8,'SERVICIO','MENU_DEGUSTACION',TRUE),(3,8,'PRODUCTO','CARNE',TRUE),(3,8,'PRODUCTO','PESCADO',TRUE),
(3,8,'PRODUCTO','PASTA',TRUE),(3,8,'PRODUCTO','ENTRANTE',TRUE),(3,8,'PRODUCTO','POSTRE',TRUE),
(3,9,'SERVICIO','EVENTOS',TRUE),(3,9,'PRODUCTO','BEBIDA',TRUE),(3,9,'PRODUCTO','VINO',TRUE);

-- Una agenda por empleado con horarios iniciales acordes al negocio.
INSERT INTO recursos_agendables(emp_id,rag_tip,rag_ref_id,rag_nom,rag_cap,rag_mar_pre,rag_mar_pos,rag_hor_vis,rag_usu_mov,rag_fec_mov,rag_act)
SELECT emp_id,'EMPLEADO',reo_id,reo_nom,1,0,0,
       CASE WHEN emp_id=2 THEN '08:00'::time ELSE '11:00'::time END,
       'MIGRACION',CURRENT_TIMESTAMP,TRUE
FROM recursos_operativos WHERE reo_tip='EMPLEADO';

INSERT INTO horarios_recurso(emp_id,rag_id,hor_dia,hor_ini,hor_fin,hor_usu_mov,hor_fec_mov,hor_act)
SELECT a.emp_id,a.rag_id,d.dia,
       CASE WHEN a.emp_id=2 THEN '08:00'::time ELSE '12:00'::time END,
       CASE WHEN a.emp_id=2 THEN '17:00'::time ELSE '23:30'::time END,
       'MIGRACION',CURRENT_TIMESTAMP,TRUE
FROM recursos_agendables a
CROSS JOIN LATERAL (
  SELECT generate_series(
    CASE WHEN a.emp_id=3 THEN 2 ELSE 1 END,
    CASE WHEN a.emp_id=2 THEN 5 ELSE 7 END
  )::smallint AS dia
) d
WHERE a.rag_tip='EMPLEADO';

-- Ajuste de secuencias utilizadas por identificadores automáticos.
SELECT setval(pg_get_serial_sequence('recursos_capacidades','rec_id'),COALESCE((SELECT MAX(rec_id) FROM recursos_capacidades),1),TRUE);
SELECT setval(pg_get_serial_sequence('recursos_agendables','rag_id'),COALESCE((SELECT MAX(rag_id) FROM recursos_agendables),1),TRUE);
SELECT setval(pg_get_serial_sequence('horarios_recurso','hor_id'),COALESCE((SELECT MAX(hor_id) FROM horarios_recurso),1),TRUE);

COMMIT;
