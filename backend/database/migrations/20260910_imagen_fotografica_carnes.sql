UPDATE tipos_articulo
SET tia_ima = 'carne.png',
    tia_usu_mov = 'SISTEMA',
    tia_fec_mov = CURRENT_TIMESTAMP
WHERE emp_id = 3
  AND tia_cla = 'PRODUCTO'
  AND UPPER(tia_nom) IN ('CARNE', 'CARNES')
  AND tia_act = TRUE
  AND COALESCE(tia_ima, '') <> 'carne.png';
