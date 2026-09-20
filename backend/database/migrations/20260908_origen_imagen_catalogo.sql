-- Permite elegir independientemente el origen de imagen del catálogo para
-- productos y servicios. TIPO conserva el comportamiento anterior.
INSERT INTO parametros
    (emp_id, par_cod, par_des, par_val, par_mod, par_usu_mov, par_fec_mov, par_act)
SELECT empresa.emp_id, dato.codigo, dato.descripcion, 'TIPO', dato.modulo,
       'MIGRACION', CURRENT_TIMESTAMP, TRUE
FROM empresas empresa
CROSS JOIN (VALUES
    ('PRODUCTOS', 'IMAGEN_CATALOGO_ORIGEN', 'Origen de imagen del catálogo: TIPO o REGISTRO'),
    ('SERVICIOS', 'IMAGEN_CATALOGO_ORIGEN', 'Origen de imagen del catálogo: TIPO o REGISTRO')
) AS dato(modulo, codigo, descripcion)
WHERE NOT EXISTS (
    SELECT 1 FROM parametros parametro
    WHERE parametro.emp_id = empresa.emp_id
      AND parametro.par_mod = dato.modulo
      AND parametro.par_cod = dato.codigo
);
