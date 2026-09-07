ALTER TABLE venta_detalle
    ADD COLUMN IF NOT EXISTS cli_id BIGINT;

UPDATE venta_detalle detalle
SET cli_id = venta.cli_id
FROM ventas venta
WHERE detalle.cli_id IS NULL
  AND venta.ven_id = detalle.ven_id
  AND venta.ven_id_his = detalle.ven_id_his;

-- Los datos actuales son de prueba: una línea sin venta asociada no puede
-- conservarse porque no es posible asignarla de forma segura a un cliente.
DELETE FROM venta_detalle WHERE cli_id IS NULL;

ALTER TABLE venta_detalle
    ALTER COLUMN cli_id SET NOT NULL;

CREATE INDEX IF NOT EXISTS ix_venta_detalle_cliente_version
    ON venta_detalle (cli_id, ven_id, ven_id_his);
