ALTER TABLE avisos_alertas ADD COLUMN IF NOT EXISTS avi_emisor VARCHAR(20);
ALTER TABLE avisos_alertas ADD COLUMN IF NOT EXISTS avi_emisor_emp_id BIGINT;
ALTER TABLE avisos_alertas ADD COLUMN IF NOT EXISTS avi_emisor_usu_id BIGINT;
ALTER TABLE avisos_alertas ADD COLUMN IF NOT EXISTS avi_destinatario VARCHAR(20);
ALTER TABLE avisos_alertas ADD COLUMN IF NOT EXISTS avi_dest_emp_id BIGINT;
ALTER TABLE avisos_alertas ADD COLUMN IF NOT EXISTS avi_ubicacion VARCHAR(30);
ALTER TABLE avisos_alertas ADD COLUMN IF NOT EXISTS avi_ventana VARCHAR(160);

UPDATE avisos_alertas SET avi_emisor='JEFE', avi_emisor_emp_id=emp_id,
    avi_destinatario='CLIENTE', avi_dest_emp_id=emp_id, avi_ubicacion='CATALOGO_CLIENTE'
WHERE avi_ubicacion IS NULL;

UPDATE avisos_alertas a SET avi_emisor_usu_id=(
    SELECT u.usu_id FROM usuarios u JOIN perfiles p ON p.emp_id=u.emp_id AND p.per_id=u.per_id
    WHERE u.emp_id=a.emp_id AND UPPER(p.per_nom)='JEFE' ORDER BY u.usu_id LIMIT 1)
WHERE a.avi_emisor_usu_id IS NULL AND a.avi_emisor='JEFE';

CREATE TABLE IF NOT EXISTS avisos_alertas_lecturas (
    id BIGSERIAL PRIMARY KEY, emp_id BIGINT NOT NULL, avi_id BIGINT NOT NULL,
    avi_id_his BIGINT NOT NULL, usu_id BIGINT NOT NULL, fecha_lectura TIMESTAMP NOT NULL,
    CONSTRAINT uq_aviso_lectura UNIQUE(emp_id,avi_id,avi_id_his,usu_id)
);
