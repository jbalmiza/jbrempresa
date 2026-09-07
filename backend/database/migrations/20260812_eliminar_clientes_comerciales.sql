-- Persona pasa a ser el cliente directo de ventas y agenda.
ALTER TABLE documentos_venta ADD COLUMN IF NOT EXISTS per_id BIGINT;
UPDATE documentos_venta d SET per_id = c.per_id FROM clientes_comerciales c
WHERE d.clc_id = c.clc_id AND d.per_id IS NULL;
ALTER TABLE documentos_venta ALTER COLUMN per_id SET NOT NULL;

ALTER TABLE reservas ADD COLUMN IF NOT EXISTS per_id BIGINT;
UPDATE reservas r SET per_id = c.per_id FROM clientes_comerciales c
WHERE r.clc_id = c.clc_id AND r.per_id IS NULL;

ALTER TABLE reservas DROP CONSTRAINT IF EXISTS fk_reserva_cliente;
ALTER TABLE documentos_venta DROP COLUMN IF EXISTS clc_id;
ALTER TABLE reservas DROP COLUMN IF EXISTS clc_id;
DROP TABLE IF EXISTS clientes_comerciales;
