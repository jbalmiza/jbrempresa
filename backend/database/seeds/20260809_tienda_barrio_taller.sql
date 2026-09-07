BEGIN;

DELETE FROM mallas WHERE mal_ent = 'PRODUCTOS' AND mal_tip = 'PRODUCTO';
DELETE FROM productos;
DELETE FROM servicios;

WITH catalogo(id,nombre,descripcion,subcategoria,precio,stock,duracion) AS (VALUES
 (1,'Bocadillo de jamón serrano','Pan crujiente, tomate y jamón serrano','Bocadillos clásicos',4.50,24,5),
 (2,'Bocadillo de tortilla','Tortilla de patatas en pan recién horneado','Bocadillos clásicos',4.00,20,5),
 (3,'Bocadillo de lomo y queso','Lomo a la plancha con queso fundido','Bocadillos calientes',5.50,18,10),
 (4,'Bocadillo de pollo completo','Pollo, lechuga, tomate y mayonesa','Bocadillos calientes',5.75,16,10),
 (5,'Bocadillo vegetal','Lechuga, tomate, atún, huevo y mayonesa','Bocadillos fríos',5.00,14,5),
 (6,'Bocadillo de chorizo','Chorizo caliente en pan crujiente','Bocadillos calientes',4.50,15,10),
 (7,'Bocadillo de calamares','Calamares fritos y limón','Bocadillos especiales',6.50,12,10),
 (8,'Bocadillo de atún y pimientos','Atún, pimientos asados y aceite de oliva','Bocadillos fríos',5.25,16,5),
 (9,'Bocadillo de bacon y queso','Bacon crujiente con queso fundido','Bocadillos calientes',5.50,17,10),
 (10,'Bocadillo de albóndigas','Albóndigas caseras con salsa de tomate','Bocadillos especiales',6.00,10,10),
 (11,'Pizza Margarita','Tomate, mozzarella y orégano','Pizzas clásicas',8.50,20,15),
 (12,'Pizza Cuatro Quesos','Mozzarella, gorgonzola, parmesano y emmental','Pizzas clásicas',10.50,16,15),
 (13,'Pizza Barbacoa','Carne, bacon, mozzarella y salsa barbacoa','Pizzas especiales',11.50,15,15),
 (14,'Pizza Pepperoni','Tomate, mozzarella y pepperoni','Pizzas clásicas',10.00,18,15),
 (15,'Pizza Vegetal','Tomate, mozzarella, pimiento, cebolla y champiñón','Pizzas vegetales',10.00,14,15),
 (16,'Pizza Carbonara','Nata, mozzarella, bacon, cebolla y champiñón','Pizzas especiales',11.00,13,15),
 (17,'Pizza Hawaiana','Tomate, mozzarella, jamón cocido y piña','Pizzas especiales',10.50,12,15),
 (18,'Pizza Prosciutto','Tomate, mozzarella y jamón cocido','Pizzas clásicas',9.50,17,15),
 (19,'Pizza Marinera','Tomate, mozzarella, atún, gambas y mejillones','Pizzas especiales',12.50,10,20),
 (20,'Pizza Picante','Tomate, mozzarella, carne picante y jalapeños','Pizzas especiales',11.50,11,15)
)
INSERT INTO productos(pro_id,pro_id_his,cli_id,pro_tip_mov,pro_cau_mov,pro_tip_pro,pro_nom,pro_des,pro_cat,pro_sub_cat,pro_mar,pro_mod,pro_pro,pro_pre_com,pro_pre_ven,pro_pre_iva,pro_pre_des,pro_pre_fin,pro_sto_act,pro_sto_min,pro_uni_med,pro_con_sto,pro_obs,pro_dur_min,pro_ubi,pro_fil_mal,pro_col_mal,pro_act,pro_usu_mov,pro_fec_mov)
SELECT id,1,1,'A','Carga de catálogo de prueba','ALIMENTACION',nombre,descripcion,'Comida preparada',subcategoria,'La Esquina',NULL,'Proveedor local',ROUND(precio*0.55,2),ROUND(precio/1.10,2),10,0,precio,stock,5,'unidad',TRUE,'Producto de prueba para tienda de barrio',duracion,NULL,NULL,NULL,TRUE,'carga_prueba',CURRENT_TIMESTAMP FROM catalogo;

WITH catalogo(id,nombre,descripcion,categoria,subcategoria,duracion,precio) AS (VALUES
 (1,'Cambio de aceite y filtro','Sustitución de aceite de motor y filtro','Mantenimiento','Aceite y filtros',45,79.00),
 (2,'Revisión pre-ITV','Comprobación general previa a inspección técnica','Inspección','Pre-ITV',60,69.00),
 (3,'Cambio de pastillas de freno delanteras','Sustitución de pastillas del eje delantero','Frenos','Pastillas',90,145.00),
 (4,'Cambio de discos y pastillas delanteros','Sustitución completa de discos y pastillas','Frenos','Discos y pastillas',150,329.00),
 (5,'Alineación de dirección','Alineación de ejes y ajuste de convergencia','Neumáticos','Alineación',45,55.00),
 (6,'Equilibrado de cuatro ruedas','Equilibrado dinámico de las cuatro ruedas','Neumáticos','Equilibrado',45,48.00),
 (7,'Cambio de batería','Diagnóstico y sustitución de batería','Electricidad','Batería',30,139.00),
 (8,'Carga de aire acondicionado','Recarga de gas y comprobación del circuito','Climatización','Aire acondicionado',60,89.00),
 (9,'Diagnosis electrónica','Lectura de centralitas y borrado de errores','Diagnosis','Electrónica',45,49.00),
 (10,'Cambio de correa de distribución','Sustitución del kit de distribución','Motor','Distribución',240,549.00),
 (11,'Cambio de embrague','Sustitución del kit de embrague','Transmisión','Embrague',360,749.00),
 (12,'Sustitución de amortiguadores delanteros','Cambio de amortiguadores del eje delantero','Suspensión','Amortiguadores',180,389.00),
 (13,'Cambio de bujías','Sustitución y comprobación de bujías','Mantenimiento','Encendido',60,95.00),
 (14,'Cambio de filtro de habitáculo','Sustitución del filtro de polen','Mantenimiento','Filtros',30,39.00),
 (15,'Cambio de líquido de frenos','Sustitución y purgado del circuito','Frenos','Líquido de frenos',60,69.00),
 (16,'Reparación de pinchazo','Desmontaje, reparación y equilibrado','Neumáticos','Pinchazos',30,25.00),
 (17,'Cambio de dos neumáticos','Montaje, válvulas y equilibrado de dos neumáticos','Neumáticos','Sustitución',75,219.00),
 (18,'Pulido de faros','Restauración y protección de dos faros','Carrocería','Faros',60,65.00),
 (19,'Revisión de frenos','Inspección del sistema de frenado','Frenos','Diagnóstico',30,35.00),
 (20,'Mantenimiento anual completo','Aceite, filtros y revisión de niveles y seguridad','Mantenimiento','Revisión anual',120,189.00)
)
INSERT INTO servicios(ser_id,ser_id_his,cli_id,ser_tip_mov,ser_cau_mov,ser_tip_ser,ser_nom,ser_des,ser_cat,ser_sub_cat,ser_dur_min,ser_pre_ven,ser_pre_des,ser_pre_iva,ser_pre_fin,ser_obs,ser_usu_mov,ser_fec_mov,ser_act)
SELECT id,1,1,'A','Carga de catálogo de prueba','TALLER',nombre,descripcion,categoria,subcategoria,duracion,ROUND(precio/1.21,2),0,21,precio,'Servicio de prueba para taller mecánico','carga_prueba',CURRENT_TIMESTAMP,TRUE FROM catalogo;

COMMIT;
