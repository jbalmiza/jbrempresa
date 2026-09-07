BEGIN;

INSERT INTO tipos_articulo
    (emp_id, tia_cla, tia_nom, tia_ima, tia_usu_mov, tia_fec_mov, tia_act)
VALUES (2, 'PRODUCTO', 'REPUESTO', 'taller.svg', 'Sistema', CURRENT_TIMESTAMP, TRUE)
ON CONFLICT (emp_id, tia_cla, tia_nom) DO UPDATE SET
    tia_ima = EXCLUDED.tia_ima, tia_usu_mov = EXCLUDED.tia_usu_mov,
    tia_fec_mov = CURRENT_TIMESTAMP, tia_act = TRUE;

WITH catalogo(orden, nombre, descripcion, subcategoria, marca, modelo, precio_compra, precio_venta, iva, precio_final, stock, stock_minimo) AS (VALUES
    (1, 'Aceite motor 5W-30 5 L', 'Aceite sintético para mantenimiento periódico del motor.', 'Lubricantes', 'Bosco Motor', '5W-30 5L', 27.27, 41.32, 21.00, 49.99, 20, 4),
    (2, 'Filtro de aceite universal', 'Filtro de aceite compatible con una amplia selección de turismos.', 'Filtros', 'Bosco Parts', 'FO-100', 6.60, 11.56, 21.00, 13.99, 30, 6),
    (3, 'Filtro de aire de motor', 'Filtro de aire para proteger el motor frente a polvo e impurezas.', 'Filtros', 'Bosco Parts', 'FA-200', 9.90, 16.52, 21.00, 19.99, 25, 5),
    (4, 'Líquido refrigerante 5 L', 'Refrigerante orgánico listo para usar y protección anticongelante.', 'Fluidos', 'Bosco Motor', 'G12 5L', 11.55, 18.17, 21.00, 21.99, 20, 4),
    (5, 'Líquido de frenos DOT 4', 'Líquido de frenos DOT 4 para sistemas hidráulicos de turismos.', 'Fluidos', 'Bosco Motor', 'DOT4 1L', 6.20, 10.74, 21.00, 12.99, 20, 4),
    (6, 'Escobillas limpiaparabrisas', 'Juego de dos escobillas universales para parabrisas delantero.', 'Accesorios', 'Bosco Parts', 'ECO-600', 12.40, 20.65, 21.00, 24.99, 18, 4),
    (7, 'Batería 12 V 70 Ah', 'Batería de arranque para turismo con capacidad de 70 Ah.', 'Electricidad', 'Bosco Energy', '12V-70AH', 74.38, 107.43, 21.00, 129.99, 8, 2),
    (8, 'Bombilla halógena H7', 'Bombilla H7 de 12 V y 55 W para iluminación delantera.', 'Electricidad', 'Bosco Light', 'H7-55W', 4.95, 8.26, 21.00, 9.99, 40, 8),
    (9, 'Kit de emergencia para coche', 'Kit con chaleco reflectante, triángulos y linterna de emergencia.', 'Seguridad', 'Bosco Safety', 'KIT-01', 16.50, 28.92, 21.00, 34.99, 15, 3),
    (10, 'Ambientador para automóvil', 'Ambientador de larga duración para el interior del vehículo.', 'Accesorios', 'Bosco Care', 'FRESH-01', 2.10, 4.12, 21.00, 4.99, 50, 10)
), nuevos AS (
    SELECT c.*, ROW_NUMBER() OVER (ORDER BY c.orden) AS rn FROM catalogo c
    WHERE NOT EXISTS (SELECT 1 FROM productos p WHERE p.emp_id = 2 AND p.pro_act = TRUE AND UPPER(p.pro_nom) = UPPER(c.nombre))
), base AS (SELECT COALESCE(MAX(pro_id), 0) AS max_id FROM productos)
INSERT INTO productos
    (pro_id, pro_id_his, emp_id, pro_tip_mov, pro_cau_mov, pro_tip_pro, pro_nom, pro_des,
     pro_cat, pro_sub_cat, pro_mar, pro_mod, pro_pro, pro_pre_com, pro_pre_ven, pro_pre_iva,
     pro_pre_des, pro_pre_fin, pro_sto_act, pro_sto_min, pro_uni_med, pro_con_sto, pro_obs,
     pro_dur_min, pro_ubi, pro_fil_mal, pro_col_mal, pro_act, pro_usu_mov, pro_fec_mov,
     pro_vis_cat, pro_ima)
SELECT b.max_id + n.rn, 1, 2, 'A', 'Alta de catálogo de Taller Bosco', 'REPUESTO', n.nombre,
    n.descripcion, 'Tienda taller', n.subcategoria, n.marca, n.modelo, 'Distribución nacional',
    n.precio_compra, n.precio_venta, n.iva, 0.00, n.precio_final, n.stock, n.stock_minimo,
    'unidad', TRUE, 'Producto de demostración de Taller Bosco', 0, NULL, NULL, NULL, TRUE,
    'Sistema', CURRENT_TIMESTAMP, TRUE, NULL
FROM nuevos n CROSS JOIN base b;

INSERT INTO parametros
    (emp_id, par_mod, par_cod, par_des, par_val, par_usu_mov, par_fec_mov, par_act)
VALUES
    (2, 'PRODUCTOS', 'CATALOGO_PUBLICADO', 'Catálogo publicado', 'true', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (2, 'PRODUCTOS', 'CATALOGO_DOMICILIO', 'Permitir pedidos a domicilio', 'false', 'Sistema', CURRENT_TIMESTAMP, TRUE)
ON CONFLICT (emp_id, par_mod, par_cod) DO UPDATE SET
    par_des = EXCLUDED.par_des, par_val = EXCLUDED.par_val, par_usu_mov = EXCLUDED.par_usu_mov,
    par_fec_mov = CURRENT_TIMESTAMP, par_act = TRUE;

COMMIT;
