ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_cau_mov VARCHAR(500);
ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_tip_per VARCHAR(10) NOT NULL DEFAULT 'FISICA';
ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_cif VARCHAR(30);
ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_raz_soc_cor VARCHAR(100);
ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_raz_soc_lar VARCHAR(250);

ALTER TABLE domicilios ADD COLUMN IF NOT EXISTS dom_cau_mov VARCHAR(500);
ALTER TABLE productos ADD COLUMN IF NOT EXISTS pro_cau_mov VARCHAR(500);
ALTER TABLE servicios ADD COLUMN IF NOT EXISTS ser_cau_mov VARCHAR(500);
ALTER TABLE ventas ADD COLUMN IF NOT EXISTS ven_cau_mov VARCHAR(500);

UPDATE personas SET per_tip_per = 'FISICA' WHERE per_tip_per IS NULL OR TRIM(per_tip_per) = '';
UPDATE personas SET per_cau_mov = CASE per_tip_mov WHEN 'A' THEN 'Alta del registro' WHEN 'M' THEN 'Modificación del registro' WHEN 'B' THEN 'Baja del registro' END WHERE per_cau_mov IS NULL;
UPDATE domicilios SET dom_cau_mov = CASE dom_tip_mov WHEN 'A' THEN 'Alta del registro' WHEN 'M' THEN 'Modificación del registro' WHEN 'B' THEN 'Baja del registro' END WHERE dom_cau_mov IS NULL;
UPDATE productos SET pro_cau_mov = CASE pro_tip_mov WHEN 'A' THEN 'Alta del registro' WHEN 'M' THEN 'Modificación del registro' WHEN 'B' THEN 'Baja del registro' END WHERE pro_cau_mov IS NULL;
UPDATE servicios SET ser_cau_mov = CASE ser_tip_mov WHEN 'A' THEN 'Alta del registro' WHEN 'M' THEN 'Modificación del registro' WHEN 'B' THEN 'Baja del registro' END WHERE ser_cau_mov IS NULL;
UPDATE ventas SET ven_cau_mov = CASE ven_tip_mov WHEN 'A' THEN 'Alta del registro' WHEN 'M' THEN 'Modificación del registro' WHEN 'B' THEN 'Baja del registro' END WHERE ven_cau_mov IS NULL;
