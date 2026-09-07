-- Registra los tipos realmente usados por los datos de demostración y asigna
-- sus imágenes genéricas almacenadas en data/imagenes/empresa-{id}/tipos.
INSERT INTO tipos_articulo
    (emp_id, tia_cla, tia_nom, tia_ima, tia_usu_mov, tia_fec_mov, tia_act)
VALUES
    (1, 'PRODUCTO', 'BOCADILLO', 'bocadillo.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (1, 'PRODUCTO', 'HAMBURGUESA', 'hamburguesa.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (1, 'PRODUCTO', 'PIZZA', 'pizza.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (1, 'SERVICIO', 'EVENTOS', 'eventos.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (1, 'SERVICIO', 'RECOGIDA', 'recogida.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (1, 'SERVICIO', 'REPARTO', 'reparto.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (2, 'SERVICIO', 'TALLER', 'taller.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'PRODUCTO', 'BEBIDA', 'bebida.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'PRODUCTO', 'CARNE', 'carne.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'PRODUCTO', 'ENTRANTE', 'entrante.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'PRODUCTO', 'PASTA', 'pasta.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'PRODUCTO', 'PESCADO', 'pescado.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'PRODUCTO', 'POSTRE', 'postre.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'PRODUCTO', 'VINO', 'vino.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'SERVICIO', 'EVENTOS', 'eventos.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'SERVICIO', 'MENU_DEGUSTACION', 'menu-degustacion.png', 'Sistema', CURRENT_TIMESTAMP, TRUE),
    (3, 'SERVICIO', 'RESERVA_MESA', 'reserva-mesa.png', 'Sistema', CURRENT_TIMESTAMP, TRUE)
ON CONFLICT (emp_id, tia_cla, tia_nom) DO UPDATE SET
    tia_ima = EXCLUDED.tia_ima,
    tia_usu_mov = EXCLUDED.tia_usu_mov,
    tia_fec_mov = CURRENT_TIMESTAMP,
    tia_act = TRUE;

-- Las versiones vectoriales son las activas; los PNG se conservan como alternativa futura.
UPDATE tipos_articulo SET tia_ima = regexp_replace(tia_ima, '\.png$', '.svg')
WHERE (emp_id, tia_cla, tia_nom) IN (
 (1,'PRODUCTO','BOCADILLO'),(1,'PRODUCTO','HAMBURGUESA'),(1,'PRODUCTO','PIZZA'),
 (1,'SERVICIO','EVENTOS'),(1,'SERVICIO','RECOGIDA'),(1,'SERVICIO','REPARTO'),(2,'SERVICIO','TALLER'),
 (3,'PRODUCTO','BEBIDA'),(3,'PRODUCTO','CARNE'),(3,'PRODUCTO','ENTRANTE'),(3,'PRODUCTO','PASTA'),
 (3,'PRODUCTO','PESCADO'),(3,'PRODUCTO','POSTRE'),(3,'PRODUCTO','VINO'),(3,'SERVICIO','EVENTOS'),
 (3,'SERVICIO','MENU_DEGUSTACION'),(3,'SERVICIO','RESERVA_MESA'));
