WITH empresa AS (
    SELECT emp_id FROM empresas WHERE LOWER(emp_nom) = LOWER('Pizzeria La Esquina') LIMIT 1
), datos(cmp_tip, cmp_nom, cmp_des, cmp_pre_adi, cmp_iva, gluten, crustaceos, huevos, pescado, cacahuetes, soja, leche, frutos_cascara, apio, mostaza, sesamo, sulfitos, altramuces, moluscos) AS (
    VALUES
    ('MATERIA_PRIMA','Harina de trigo','Harina para masa de pizza',0.00,10.00,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Mozzarella','Queso mozzarella para pizza',1.50,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Tomate triturado','Base de tomate para pizza',0.50,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Jamón cocido','Jamón cocido loncheado',1.20,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE),
    ('INGREDIENTE','Pepperoni','Pepperoni en rodajas',1.50,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE),
    ('INGREDIENTE','Bacon','Bacon troceado',1.50,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE),
    ('INGREDIENTE','Atún','Atún en conserva',1.50,10.00,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Anchoas','Anchoas en conserva',1.50,10.00,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Gambas','Gambas peladas',2.00,10.00,FALSE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE),
    ('INGREDIENTE','Champiñón','Champiñón laminado',1.00,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Cebolla','Cebolla fresca',0.75,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Aceitunas','Aceitunas sin hueso',0.75,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Huevo','Huevo',1.00,10.00,FALSE,FALSE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Piña','Piña troceada',1.00,10.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('INGREDIENTE','Salsa barbacoa','Salsa barbacoa',0.50,10.00,TRUE,FALSE,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE,FALSE,TRUE,FALSE,TRUE,FALSE,FALSE),
    ('ENVASE','Caja de pizza 33 cm','Caja de cartón para pizza mediana',0.35,21.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('ENVASE','Caja de pizza XL','Caja de cartón para pizza XL',0.55,21.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('MATERIAL','Papel antigrasa','Papel alimentario para pedidos',0.05,21.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('ACCESORIO','Servilleta','Servilleta individual',0.03,21.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE),
    ('ACCESORIO','Cubiertos desechables','Juego de cubiertos para pedido',0.15,21.00,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE)
), numerados AS (
    SELECT e.emp_id, d.*, ROW_NUMBER() OVER (ORDER BY d.cmp_tip, d.cmp_nom) AS rn
    FROM empresa e CROSS JOIN datos d
), base AS (
    SELECT COALESCE(MAX(cmp_id),0) AS ultimo FROM componentes
)
INSERT INTO componentes(emp_id,cmp_id,cmp_id_his,cmp_tip_mov,cmp_cau_mov,cmp_tip,cmp_nom,cmp_des,cmp_pre_adi,cmp_iva,cmp_gluten,cmp_crustaceos,cmp_huevos,cmp_pescado,cmp_cacahuetes,cmp_soja,cmp_leche,cmp_frutos_cascara,cmp_apio,cmp_mostaza,cmp_sesamo,cmp_sulfitos,cmp_altramuces,cmp_moluscos,cmp_usu_mov,cmp_fec_mov,cmp_act)
SELECT n.emp_id,b.ultimo+n.rn,1,'A','Carga inicial de componentes de hostelería',n.cmp_tip,n.cmp_nom,n.cmp_des,n.cmp_pre_adi,n.cmp_iva,n.gluten,n.crustaceos,n.huevos,n.pescado,n.cacahuetes,n.soja,n.leche,n.frutos_cascara,n.apio,n.mostaza,n.sesamo,n.sulfitos,n.altramuces,n.moluscos,'SISTEMA',CURRENT_TIMESTAMP,TRUE
FROM numerados n CROSS JOIN base b
WHERE NOT EXISTS (SELECT 1 FROM componentes c WHERE c.emp_id=n.emp_id AND c.cmp_act=TRUE AND UPPER(c.cmp_tip)=UPPER(n.cmp_tip) AND UPPER(c.cmp_nom)=UPPER(n.cmp_nom));
