-- Tipos genéricos para agrupar productos y servicios minoritarios en el catálogo.
INSERT INTO tipos_articulo (emp_id, tia_cla, tia_nom, tia_ima, tia_usu_mov, tia_fec_mov, tia_act)
SELECT emp_id, 'PRODUCTO', 'Productos', 'productos.svg', 'migracion', CURRENT_TIMESTAMP, TRUE FROM empresas
ON CONFLICT (emp_id, tia_cla, tia_nom) DO UPDATE SET tia_ima=EXCLUDED.tia_ima,tia_act=TRUE,tia_usu_mov=EXCLUDED.tia_usu_mov,tia_fec_mov=EXCLUDED.tia_fec_mov;

INSERT INTO tipos_articulo (emp_id, tia_cla, tia_nom, tia_ima, tia_usu_mov, tia_fec_mov, tia_act)
SELECT emp_id, 'SERVICIO', 'Servicios', 'servicios.svg', 'migracion', CURRENT_TIMESTAMP, TRUE FROM empresas
ON CONFLICT (emp_id, tia_cla, tia_nom) DO UPDATE SET tia_ima=EXCLUDED.tia_ima,tia_act=TRUE,tia_usu_mov=EXCLUDED.tia_usu_mov,tia_fec_mov=EXCLUDED.tia_fec_mov;
