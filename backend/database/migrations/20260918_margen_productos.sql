ALTER TABLE productos ADD COLUMN IF NOT EXISTS pro_iva_com NUMERIC(10,2);
ALTER TABLE productos ADD COLUMN IF NOT EXISTS pro_des_com NUMERIC(10,2);
ALTER TABLE productos ADD COLUMN IF NOT EXISTS pro_tot_com NUMERIC(14,2);
ALTER TABLE productos ADD COLUMN IF NOT EXISTS pro_pre_com_est BOOLEAN;
ALTER TABLE productos DROP COLUMN IF EXISTS pro_con_margen;
ALTER TABLE productos ALTER COLUMN pro_pre_ven TYPE NUMERIC(14,4);

INSERT INTO parametros(emp_id, par_mod, par_cod, par_des, par_val, par_usu_mov, par_fec_mov, par_act)
SELECT e.emp_id, 'PRODUCTOS', 'PORCENTAJE_BENEFICIO',
       'Porcentaje de beneficio aplicado entre precio de compra y venta sin IVA',
       '20', 'SISTEMA', CURRENT_TIMESTAMP, TRUE
FROM empresas e
WHERE NOT EXISTS (
    SELECT 1 FROM parametros p
    WHERE p.emp_id = e.emp_id
      AND UPPER(p.par_mod) = 'PRODUCTOS'
      AND UPPER(p.par_cod) = 'PORCENTAJE_BENEFICIO'
);
