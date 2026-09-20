-- Imágenes principales diferenciadas para las empresas de demostración y alta
-- de Proveedor Hostelero Central con su catálogo inicial.

BEGIN;

INSERT INTO empresas(emp_id,emp_nom,emp_ima,emp_act,emp_tip_mov,emp_cau_mov,emp_usu_mov,emp_fec_mov)
VALUES(4,'Proveedor Hostelero Central','empresa-4.png','true','A','Alta de empresa proveedora hostelera','SISTEMA',CURRENT_TIMESTAMP)
ON CONFLICT(emp_id) DO UPDATE SET
  emp_nom=EXCLUDED.emp_nom,
  emp_ima=EXCLUDED.emp_ima,
  emp_act='true',
  emp_usu_mov='SISTEMA',
  emp_fec_mov=CURRENT_TIMESTAMP;

UPDATE empresas SET emp_ima='empresa-1.png',emp_usu_mov='SISTEMA',emp_fec_mov=CURRENT_TIMESTAMP WHERE emp_id=1;
UPDATE empresas SET emp_ima='empresa-2.png',emp_usu_mov='SISTEMA',emp_fec_mov=CURRENT_TIMESTAMP WHERE emp_id=2;
UPDATE empresas SET emp_ima='empresa-3.png',emp_usu_mov='SISTEMA',emp_fec_mov=CURRENT_TIMESTAMP WHERE emp_id=3;

-- La empresa nueva recibe el catálogo completo de módulos para que el
-- administrador global pueda configurar posteriormente disponibilidad y orden.
INSERT INTO empresas_modulos(emp_id,mod_id,emm_dis,emm_pos)
SELECT 4,mod_id,TRUE,mod_pos FROM modulos_aplicacion
ON CONFLICT(emp_id,mod_id) DO NOTHING;

-- Rutas comunes de imágenes y documentos de empresa.
INSERT INTO parametros(emp_id,par_cod,par_des,par_val,par_mod,par_usu_mov,par_fec_mov,par_act)
SELECT e.emp_id,'RUTA_IMAGENES','Ruta general de imágenes de empresa',
       'C:\Workspace\proyectos\jbrempresa\data\imagenes','ADMINISTRACION','SISTEMA',CURRENT_TIMESTAMP,TRUE
FROM empresas e WHERE e.emp_id BETWEEN 1 AND 4
ON CONFLICT(emp_id,par_mod,par_cod) DO UPDATE SET par_val=EXCLUDED.par_val,par_act=TRUE,par_fec_mov=CURRENT_TIMESTAMP;

INSERT INTO parametros(emp_id,par_cod,par_des,par_val,par_mod,par_usu_mov,par_fec_mov,par_act)
SELECT e.emp_id,'RUTA_DOCUMENTOS_EMPRESAS','Ruta de documentos de empresas',
       'C:\Workspace\proyectos\jbrempresa\data\adjuntos\empresa-'||e.emp_id||'\Empresas',
       'EMPRESAS','SISTEMA',CURRENT_TIMESTAMP,TRUE
FROM empresas e WHERE e.emp_id BETWEEN 1 AND 4
ON CONFLICT(emp_id,par_mod,par_cod) DO UPDATE SET par_val=EXCLUDED.par_val,par_act=TRUE,par_fec_mov=CURRENT_TIMESTAMP;

INSERT INTO parametros(emp_id,par_cod,par_des,par_val,par_mod,par_usu_mov,par_fec_mov,par_act) VALUES
(4,'RUTA_IMAGENES','Ruta de imágenes de productos','C:\Workspace\proyectos\jbrempresa\data\imagenes','PRODUCTOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'RUTA_IMAGENES','Ruta de imágenes de servicios','C:\Workspace\proyectos\jbrempresa\data\imagenes','SERVICIOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'RUTA_DOCUMENTOS_PRODUCTOS','Ruta de documentos de productos','C:\Workspace\proyectos\jbrempresa\data\adjuntos\empresa-4\Productos','PRODUCTOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'RUTA_DOCUMENTOS_SERVICIOS','Ruta de documentos de servicios','C:\Workspace\proyectos\jbrempresa\data\adjuntos\empresa-4\Servicios','SERVICIOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'CATALOGO_ALIAS','Enlace corto del catálogo','proveedor-hostelero-central','PRODUCTOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'CATALOGO_PUBLICADO','Catálogo publicado','true','PRODUCTOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'CATALOGO_DOMICILIO','Permitir pedidos a domicilio','true','PRODUCTOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'IMAGEN_CATALOGO_ORIGEN','Origen de imagen del catálogo: TIPO o REGISTRO','REGISTRO','PRODUCTOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'IMAGEN_CATALOGO_ORIGEN','Origen de imagen del catálogo: TIPO o REGISTRO','REGISTRO','SERVICIOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'CATALOGO_TOKEN_GENERAL','Token público del catálogo','9e65816bdaf64e12a1c444dcc7a23d6b','PRODUCTOS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'MOSTRAR_PRESUPUESTOS','Mostrar Registro y Gestión de Presupuestos','false','VENTAS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'MOSTRAR_ALBARANES','Mostrar Registro y Gestión de Albaranes','false','VENTAS','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(4,'TIPO_FACTURA_AUTOMATICA','Tipo de factura generada automáticamente desde pedidos','NORMAL','VENTAS','SISTEMA',CURRENT_TIMESTAMP,TRUE)
ON CONFLICT(emp_id,par_mod,par_cod) DO UPDATE SET par_val=EXCLUDED.par_val,par_act=TRUE,par_fec_mov=CURRENT_TIMESTAMP;

-- Cada imagen se registra como adjunto principal de la propia empresa.
UPDATE adjuntos SET adj_nom='Imagen principal · Pizzeria La Esquina',adj_nom_arc='empresa-1.png',
  adj_rut_rel='1/empresa-1-principal.png',adj_mime='image/png',adj_tam=1965625,
  adj_usu_mov='SISTEMA',adj_fec_mov=CURRENT_TIMESTAMP,adj_act=TRUE,adj_pri=TRUE
WHERE emp_id=1 AND adj_mod='EMPRESAS' AND adj_tip_reg='EMPRESA' AND adj_reg_id=1;
UPDATE adjuntos SET adj_nom='Imagen principal · Taller Bosco de coches',adj_nom_arc='empresa-2.png',
  adj_rut_rel='2/empresa-2-principal.png',adj_mime='image/png',adj_tam=1432334,
  adj_usu_mov='SISTEMA',adj_fec_mov=CURRENT_TIMESTAMP,adj_act=TRUE,adj_pri=TRUE
WHERE emp_id=2 AND adj_mod='EMPRESAS' AND adj_tip_reg='EMPRESA' AND adj_reg_id=2;
UPDATE adjuntos SET adj_nom='Imagen principal · Restaurante Cándida',adj_nom_arc='empresa-3.png',
  adj_rut_rel='3/empresa-3-principal.png',adj_mime='image/png',adj_tam=1900457,
  adj_usu_mov='SISTEMA',adj_fec_mov=CURRENT_TIMESTAMP,adj_act=TRUE,adj_pri=TRUE
WHERE emp_id=3 AND adj_mod='EMPRESAS' AND adj_tip_reg='EMPRESA' AND adj_reg_id=3;
INSERT INTO adjuntos(emp_id,adj_mod,adj_tip_reg,adj_reg_id,adj_nom,adj_tip,adj_nom_arc,adj_rut_rel,adj_mime,adj_tam,adj_usu_mov,adj_fec_mov,adj_act,adj_pri)
SELECT 4,'EMPRESAS','EMPRESA',4,'Imagen principal · Proveedor Hostelero Central','ORIGINAL',
       'empresa-4.png','4/empresa-4-principal.png','image/png',1606941,'SISTEMA',CURRENT_TIMESTAMP,TRUE,TRUE
WHERE NOT EXISTS(SELECT 1 FROM adjuntos WHERE emp_id=4 AND adj_mod='EMPRESAS' AND adj_tip_reg='EMPRESA' AND adj_reg_id=4);

INSERT INTO tipos_articulo(emp_id,tia_cla,tia_nom,tia_ima,tia_act,tia_usu_mov,tia_fec_mov) VALUES
(4,'PRODUCTO','INGREDIENTES','producto-predeterminado.png',TRUE,'SISTEMA',CURRENT_TIMESTAMP),
(4,'PRODUCTO','BEBIDAS','producto-predeterminado.png',TRUE,'SISTEMA',CURRENT_TIMESTAMP),
(4,'PRODUCTO','CONSUMIBLES','producto-predeterminado.png',TRUE,'SISTEMA',CURRENT_TIMESTAMP),
(4,'PRODUCTO','LIMPIEZA','producto-predeterminado.png',TRUE,'SISTEMA',CURRENT_TIMESTAMP),
(4,'SERVICIO','LOGÍSTICA','servicio-predeterminado.png',TRUE,'SISTEMA',CURRENT_TIMESTAMP),
(4,'SERVICIO','MANTENIMIENTO','servicio-predeterminado.png',TRUE,'SISTEMA',CURRENT_TIMESTAMP)
ON CONFLICT(emp_id,tia_cla,tia_nom) DO NOTHING;

INSERT INTO productos(pro_id,pro_id_his,emp_id,pro_tip_mov,pro_cau_mov,pro_tip_pro,pro_nom,pro_des,pro_mar,pro_pro,pro_cat,pro_sub_cat,pro_uni_med,pro_con_sto,pro_sto_act,pro_sto_min,pro_pre_com,pro_pre_ven,pro_pre_des,pro_pre_iva,pro_pre_fin,pro_obs,pro_vis_cat,pro_ima,pro_usu_mov,pro_fec_mov,pro_act) VALUES
(111,1,4,'A','Alta de catálogo inicial','INGREDIENTES','Harina de fuerza 25 kg','Harina profesional para pizzas, panes y masas','Molino Central','Fabricante nacional','Ingredientes','Harinas','saco',TRUE,40,8,17.00,23.50,0,10,25.85,'Suministro para pizzería y restaurante',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(112,1,4,'A','Alta de catálogo inicial','INGREDIENTES','Mozzarella rallada 5 kg','Mozzarella de uso profesional para pizzas y gratinados','Lácteos Central','Fabricante nacional','Ingredientes','Lácteos','caja',TRUE,30,6,24.00,32.00,0,10,35.20,'Producto refrigerado',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(113,1,4,'A','Alta de catálogo inicial','INGREDIENTES','Tomate triturado 10 kg','Tomate natural triturado para cocina profesional','Huerta Central','Productor nacional','Ingredientes','Conservas','lata',TRUE,36,8,11.00,16.00,0,10,17.60,'Formato hostelero',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(114,1,4,'A','Alta de catálogo inicial','INGREDIENTES','Aceite de oliva virgen extra 5 l','Aceite de oliva para cocina y aliños','Oliva Central','Almazara nacional','Ingredientes','Aceites','garrafa',TRUE,24,5,26.00,34.00,0,10,37.40,'Formato profesional',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(115,1,4,'A','Alta de catálogo inicial','INGREDIENTES','Jamón serrano loncheado 2 kg','Jamón serrano loncheado para bocadillos y entrantes','Sierra Central','Productor nacional','Ingredientes','Charcutería','envase',TRUE,20,4,29.00,38.00,0,10,41.80,'Producto refrigerado',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(116,1,4,'A','Alta de catálogo inicial','INGREDIENTES','Carne de vacuno para hamburguesas 5 kg','Carne de vacuno preparada para hamburguesas','Cárnicas Central','Productor nacional','Ingredientes','Carnes','caja',TRUE,25,5,31.00,42.00,0,10,46.20,'Producto refrigerado',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(117,1,4,'A','Alta de catálogo inicial','BEBIDAS','Agua mineral 24 x 50 cl','Caja de agua mineral para servicio de sala y reparto','Fuente Central','Embotellador nacional','Bebidas','Aguas','caja',TRUE,50,10,7.00,10.00,0,21,12.10,'Caja de 24 botellas',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(118,1,4,'A','Alta de catálogo inicial','BEBIDAS','Refrescos surtidos 24 x 33 cl','Surtido de refrescos en lata para hostelería','Bebidas Central','Distribuidor nacional','Bebidas','Refrescos','caja',TRUE,45,10,14.00,20.00,0,21,24.20,'Caja surtida de 24 latas',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(119,1,4,'A','Alta de catálogo inicial','CONSUMIBLES','Envases para reparto 100 unidades','Envases reciclables para comida preparada y reparto','Eco Central','Fabricante nacional','Consumibles','Envases','caja',TRUE,35,8,18.00,25.00,0,21,30.25,'Aptos para alimentos',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(120,1,4,'A','Alta de catálogo inicial','LIMPIEZA','Desengrasante profesional 5 l','Desengrasante concentrado para cocinas profesionales','Higiene Central','Fabricante nacional','Limpieza','Cocina','garrafa',TRUE,28,6,12.00,18.00,0,21,21.78,'Uso profesional',TRUE,'producto-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE)
ON CONFLICT(pro_id,pro_id_his) DO NOTHING;

INSERT INTO servicios(ser_id,ser_id_his,emp_id,ser_tip_mov,ser_cau_mov,ser_tip_ser,ser_nom,ser_des,ser_cat,ser_sub_cat,ser_dur_min,ser_pre_ven,ser_pre_des,ser_pre_iva,ser_pre_fin,ser_obs,ser_vis_cat,ser_ima,ser_usu_mov,ser_fec_mov,ser_act) VALUES
(27,1,4,'A','Alta de catálogo inicial','LOGÍSTICA','Entrega refrigerada programada','Entrega de ingredientes y bebidas en vehículo refrigerado dentro de la franja acordada.','Logística','Distribución',60,25.00,0,21,30.25,'Servicio para pizzerías y restaurantes',TRUE,'servicio-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE),
(28,1,4,'A','Alta de catálogo inicial','MANTENIMIENTO','Mantenimiento de equipamiento hostelero','Revisión preventiva de hornos, cámaras, lavavajillas y maquinaria de cocina.','Asistencia técnica','Mantenimiento preventivo',120,90.00,0,21,108.90,'Servicio técnico profesional',TRUE,'servicio-predeterminado.png','SISTEMA',CURRENT_TIMESTAMP,TRUE)
ON CONFLICT(ser_id,ser_id_his) DO NOTHING;

SELECT setval(pg_get_serial_sequence('empresas','emp_id'),GREATEST((SELECT MAX(emp_id) FROM empresas),4),TRUE);
SELECT setval(pg_get_serial_sequence('adjuntos','adj_id'),GREATEST((SELECT MAX(adj_id) FROM adjuntos),1),TRUE);

COMMIT;
