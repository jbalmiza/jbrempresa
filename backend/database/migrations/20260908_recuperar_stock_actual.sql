-- Recupera en versiones vigentes el último stock histórico conocido cuando una
-- modificación antigua lo dejó a NULL pese a tener activado el control de stock.
UPDATE productos vigente
SET pro_sto_act = (
    SELECT historico.pro_sto_act
    FROM productos historico
    WHERE historico.emp_id = vigente.emp_id
      AND historico.pro_id = vigente.pro_id
      AND historico.pro_sto_act IS NOT NULL
    ORDER BY historico.pro_id_his DESC
    LIMIT 1
)
WHERE vigente.pro_act = TRUE
  AND vigente.pro_con_sto = TRUE
  AND vigente.pro_sto_act IS NULL;
