ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_int VARCHAR(30);
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_con_int DOUBLE PRECISION;
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_ori_cla VARCHAR(20);
ALTER TABLE mensajes ADD COLUMN IF NOT EXISTS men_est_cla VARCHAR(20);
ALTER TABLE comunicaciones ADD COLUMN IF NOT EXISTS com_int VARCHAR(30);

UPDATE mensajes
SET men_int = COALESCE(men_int, 'OTRO'),
    men_con_int = COALESCE(men_con_int, 0),
    men_ori_cla = COALESCE(men_ori_cla, 'MIGRACION'),
    men_est_cla = COALESCE(men_est_cla, 'PENDIENTE')
WHERE men_int IS NULL OR men_est_cla IS NULL;
