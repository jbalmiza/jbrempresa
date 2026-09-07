BEGIN;

WITH carta(orden,tipo,nombre,descripcion,subcategoria,precio_final,duracion) AS (VALUES
 (1,'CARNE','Entrecot de ternera','Entrecot de ternera a la plancha.','Ternera',22.00,25),
 (2,'CARNE','Solomillo de ternera','Solomillo de ternera con guarnición.','Ternera',25.00,30),
 (3,'CARNE','Chuletón de vaca','Chuletón de vaca madurada a la parrilla.','Vacuno',32.00,35),
 (4,'CARNE','Secreto ibérico','Secreto ibérico a la brasa.','Cerdo ibérico',18.00,25),
 (5,'CARNE','Presa ibérica','Presa ibérica con patatas.','Cerdo ibérico',20.00,25),
 (6,'CARNE','Costillas de cerdo BBQ','Costillas asadas con salsa barbacoa.','Cerdo',17.00,30),
 (7,'CARNE','Pollo asado','Pollo asado con hierbas aromáticas.','Aves',14.00,30),
 (8,'CARNE','Pechuga de pollo a la plancha','Pechuga de pollo con verduras.','Aves',13.00,20),
 (9,'CARNE','Cordero lechal','Cordero lechal asado al horno.','Cordero',24.00,35),
 (10,'CARNE','Hamburguesa de vaca','Hamburguesa de vaca con queso y patatas.','Vacuno',15.00,20),
 (11,'PESCADO','Merluza a la plancha','Lomo de merluza con verduras.','Pescado blanco',17.00,20),
 (12,'PESCADO','Lubina al horno','Lubina al horno con patatas.','Pescado blanco',20.00,30),
 (13,'PESCADO','Dorada a la espalda','Dorada abierta con ajo y perejil.','Pescado blanco',19.00,25),
 (14,'PESCADO','Salmón a la plancha','Salmón con guarnición vegetal.','Pescado azul',18.00,20),
 (15,'PESCADO','Atún rojo marcado','Lomo de atún rojo marcado a la plancha.','Pescado azul',23.00,20),
 (16,'PESCADO','Bacalao confitado','Bacalao confitado con pisto.','Pescado blanco',19.00,25),
 (17,'PESCADO','Rodaballo a la plancha','Rodaballo con aceite de ajo.','Pescado blanco',26.00,25),
 (18,'PESCADO','Calamares a la plancha','Calamares con ajo y perejil.','Cefalópodos',16.00,20),
 (19,'PESCADO','Pulpo a la gallega','Pulpo con patata, pimentón y aceite.','Marisco',21.00,20),
 (20,'PESCADO','Fritura de pescado','Selección de pescado frito.','Fritura',18.00,20),
 (21,'ENTRANTE','Croquetas de jamón','Croquetas cremosas de jamón ibérico.','Croquetas',9.00,15),
 (22,'ENTRANTE','Ensaladilla rusa','Ensaladilla tradicional con ventresca.','Fríos',8.00,10),
 (23,'ENTRANTE','Jamón ibérico','Ración de jamón ibérico.','Ibéricos',18.00,5),
 (24,'ENTRANTE','Queso manchego','Ración de queso manchego curado.','Quesos',12.00,5),
 (25,'ENTRANTE','Patatas bravas','Patatas fritas con salsa brava.','Calientes',7.00,15),
 (26,'ENTRANTE','Calamares fritos','Calamares a la andaluza.','Fritura',12.00,15),
 (27,'ENTRANTE','Gambas al ajillo','Gambas salteadas con ajo y guindilla.','Marisco',15.00,15),
 (28,'ENTRANTE','Ensalada mixta','Lechuga, tomate, cebolla, atún y huevo.','Ensaladas',9.00,10),
 (29,'ENTRANTE','Tortilla española','Porción de tortilla de patata.','Calientes',7.50,10),
 (30,'ENTRANTE','Pan con alioli','Pan tostado acompañado de alioli.','Pan',4.00,5),
 (31,'BEBIDA','Agua mineral','Botella de agua mineral de 50 cl.','Sin alcohol',2.00,5),
 (32,'BEBIDA','Agua con gas','Botella de agua con gas de 50 cl.','Sin alcohol',2.50,5),
 (33,'BEBIDA','Refresco de cola','Refresco de cola de 33 cl.','Refrescos',3.00,5),
 (34,'BEBIDA','Refresco de naranja','Refresco de naranja de 33 cl.','Refrescos',3.00,5),
 (35,'BEBIDA','Refresco de limón','Refresco de limón de 33 cl.','Refrescos',3.00,5),
 (36,'BEBIDA','Cerveza de barril','Caña de cerveza de barril.','Cervezas',3.00,5),
 (37,'BEBIDA','Cerveza sin alcohol','Botella de cerveza sin alcohol.','Cervezas',3.00,5),
 (38,'BEBIDA','Copa de vino tinto','Copa de vino tinto de la casa.','Vinos',3.50,5),
 (39,'BEBIDA','Copa de vino blanco','Copa de vino blanco de la casa.','Vinos',3.50,5),
 (40,'BEBIDA','Café solo','Café espresso.','Cafés',1.80,5),
 (41,'POSTRE','Tarta de queso','Tarta de queso cremosa.','Tartas',6.00,5),
 (42,'POSTRE','Tarta de chocolate','Tarta de chocolate con cacao.','Tartas',6.00,5),
 (43,'POSTRE','Flan casero','Flan de huevo con caramelo.','Caseros',5.00,5),
 (44,'POSTRE','Arroz con leche','Arroz con leche y canela.','Caseros',5.00,5),
 (45,'POSTRE','Crema catalana','Crema catalana caramelizada.','Caseros',5.50,5),
 (46,'POSTRE','Tiramisú','Tiramisú de café y mascarpone.','Internacionales',6.00,5),
 (47,'POSTRE','Helado de vainilla','Copa de helado de vainilla.','Helados',4.50,5),
 (48,'POSTRE','Helado de chocolate','Copa de helado de chocolate.','Helados',4.50,5),
 (49,'POSTRE','Fruta de temporada','Selección de fruta fresca.','Fruta',4.00,5),
 (50,'POSTRE','Brownie con helado','Brownie templado con helado de vainilla.','Calientes',6.50,10)
), nuevos AS (
 SELECT c.*, ROW_NUMBER() OVER (ORDER BY c.orden) AS rn
 FROM carta c
 WHERE NOT EXISTS (SELECT 1 FROM productos p WHERE p.emp_id=1 AND p.pro_act=TRUE AND UPPER(p.pro_nom)=UPPER(c.nombre))
), base AS (SELECT COALESCE(MAX(pro_id),0) max_id FROM productos WHERE emp_id=1)
INSERT INTO productos(pro_id,pro_id_his,emp_id,pro_tip_mov,pro_cau_mov,pro_tip_pro,pro_nom,pro_des,pro_cat,pro_sub_cat,pro_mar,pro_mod,pro_pro,pro_pre_com,pro_pre_ven,pro_pre_iva,pro_pre_des,pro_pre_fin,pro_sto_act,pro_sto_min,pro_uni_med,pro_con_sto,pro_obs,pro_dur_min,pro_ubi,pro_fil_mal,pro_col_mal,pro_act,pro_usu_mov,pro_fec_mov,pro_vis_cat,pro_ima)
SELECT b.max_id+n.rn,1,1,'A','Carga de carta de prueba',n.tipo,n.nombre,n.descripcion,'Carta',n.subcategoria,'Cocina propia','Ración','Nacional',ROUND((n.precio_final/1.10*0.55)::numeric,2),ROUND((n.precio_final/1.10)::numeric,2),10.00,0.00,n.precio_final,50,10,'unidad',TRUE,'Producto de prueba',n.duracion,NULL,NULL,NULL,TRUE,'carga_prueba',CURRENT_TIMESTAMP,TRUE,'producto-predeterminado.png'
FROM nuevos n CROSS JOIN base b;

COMMIT;
