BEGIN;

WITH carta(orden,tipo,nombre,descripcion,subcategoria,precio_final,duracion) AS (VALUES
 (1,'VINO','Rioja crianza','Botella de vino tinto Rioja crianza.','Tintos',18.00,5),
 (2,'VINO','Ribera del Duero roble','Botella de vino tinto Ribera del Duero roble.','Tintos',20.00,5),
 (3,'VINO','Tempranillo de la casa','Botella de tempranillo joven de la casa.','Tintos',14.00,5),
 (4,'VINO','Albariño','Botella de vino blanco Albariño.','Blancos',19.00,5),
 (5,'VINO','Verdejo','Botella de vino blanco Verdejo.','Blancos',16.00,5),
 (6,'VINO','Chardonnay','Botella de vino blanco Chardonnay.','Blancos',17.00,5),
 (7,'VINO','Rosado de Navarra','Botella de vino rosado de Navarra.','Rosados',15.00,5),
 (8,'VINO','Cava brut nature','Botella de cava brut nature.','Espumosos',22.00,5),
 (9,'VINO','Prosecco','Botella de vino espumoso Prosecco.','Espumosos',18.00,5),
 (10,'VINO','Pedro Ximénez','Copa de vino dulce Pedro Ximénez.','Dulces',5.00,5),
 (11,'PASTA','Espaguetis carbonara','Espaguetis con salsa carbonara cremosa.','Espaguetis',13.00,20),
 (12,'PASTA','Espaguetis boloñesa','Espaguetis con ragú de ternera y tomate.','Espaguetis',13.50,20),
 (13,'PASTA','Penne arrabbiata','Penne con tomate, ajo y guindilla.','Pasta corta',12.00,15),
 (14,'PASTA','Macarrones gratinados','Macarrones con tomate, carne y queso gratinado.','Pasta corta',13.00,20),
 (15,'PASTA','Tagliatelle al pesto','Tagliatelle con pesto de albahaca y parmesano.','Pasta larga',14.00,20),
 (16,'PASTA','Fettuccine Alfredo','Fettuccine con salsa de mantequilla y parmesano.','Pasta larga',14.00,20),
 (17,'PASTA','Lasaña de carne','Lasaña casera de carne y bechamel.','Horno',15.00,25),
 (18,'PASTA','Lasaña vegetal','Lasaña de verduras y queso.','Horno',14.00,25),
 (19,'PASTA','Ravioli de ricotta','Ravioli de ricotta y espinacas con salsa de tomate.','Rellena',15.00,20),
 (20,'PASTA','Tortellini de carne','Tortellini rellenos de carne con salsa suave.','Rellena',15.00,20),
 (21,'HAMBURGUESA','Hamburguesa clásica','Carne de vaca, lechuga, tomate y cebolla.','Clásicas',12.00,20),
 (22,'HAMBURGUESA','Hamburguesa con queso','Carne de vaca con queso cheddar.','Clásicas',13.00,20),
 (23,'HAMBURGUESA','Hamburguesa completa','Carne, queso, bacon, huevo, lechuga y tomate.','Especiales',16.00,25),
 (24,'HAMBURGUESA','Hamburguesa BBQ','Carne, bacon, cebolla crujiente y salsa barbacoa.','Especiales',15.00,20),
 (25,'HAMBURGUESA','Hamburguesa ibérica','Carne de cerdo ibérico, queso manchego y pimientos.','Especiales',16.00,25),
 (26,'HAMBURGUESA','Hamburguesa de pollo','Pechuga de pollo, lechuga, tomate y mayonesa.','Pollo',13.00,20),
 (27,'HAMBURGUESA','Hamburguesa crispy chicken','Pollo crujiente, queso y salsa especial.','Pollo',14.00,20),
 (28,'HAMBURGUESA','Hamburguesa vegetal','Hamburguesa vegetal, lechuga, tomate y aguacate.','Vegetales',14.00,20),
 (29,'HAMBURGUESA','Hamburguesa doble','Doble carne de vaca y doble queso cheddar.','Especiales',18.00,25),
 (30,'HAMBURGUESA','Mini hamburguesas','Trío de mini hamburguesas variadas.','Degustación',15.00,20)
), nuevos AS (
 SELECT c.*,ROW_NUMBER() OVER(ORDER BY c.orden) rn FROM carta c
 WHERE NOT EXISTS(SELECT 1 FROM productos p WHERE p.emp_id=1 AND p.pro_act=TRUE AND UPPER(p.pro_nom)=UPPER(c.nombre))
), base AS (SELECT COALESCE(MAX(pro_id),0) max_id FROM productos WHERE emp_id=1)
INSERT INTO productos(pro_id,pro_id_his,emp_id,pro_tip_mov,pro_cau_mov,pro_tip_pro,pro_nom,pro_des,pro_cat,pro_sub_cat,pro_mar,pro_mod,pro_pro,pro_pre_com,pro_pre_ven,pro_pre_iva,pro_pre_des,pro_pre_fin,pro_sto_act,pro_sto_min,pro_uni_med,pro_con_sto,pro_obs,pro_dur_min,pro_ubi,pro_fil_mal,pro_col_mal,pro_act,pro_usu_mov,pro_fec_mov,pro_vis_cat,pro_ima)
SELECT b.max_id+n.rn,1,1,'A','Carga de carta de prueba',n.tipo,n.nombre,n.descripcion,'Carta',n.subcategoria,'Selección de la casa','Unidad','Nacional',ROUND((n.precio_final/1.10*0.55)::numeric,2),ROUND((n.precio_final/1.10)::numeric,2),10.00,0.00,n.precio_final,50,10,'unidad',TRUE,'Producto de prueba',n.duracion,NULL,NULL,NULL,TRUE,'carga_prueba',CURRENT_TIMESTAMP,TRUE,'producto-predeterminado.png'
FROM nuevos n CROSS JOIN base b;

COMMIT;
