-- Aplica una sola vez el nuevo valor predeterminado a las preferencias ya
-- guardadas. Después de esta migración cada usuario puede volver a mostrar
-- las columnas desde el configurador y su decisión se conserva normalmente.
UPDATE configuraciones_tabla configuracion
SET cot_con = (
        SELECT jsonb_agg(
            CASE
                WHEN lower(regexp_replace(columna.valor ->> 'campo', '[^a-zA-Z0-9]', '', 'g')) IN ('empid', 'cliid')
                  OR lower(regexp_replace(columna.valor ->> 'campo', '[^a-zA-Z0-9]', '', 'g')) LIKE '%tipmov'
                  OR lower(regexp_replace(columna.valor ->> 'campo', '[^a-zA-Z0-9]', '', 'g')) LIKE '%caumov'
                THEN jsonb_set(columna.valor, '{visible}', 'false'::jsonb, TRUE)
                ELSE columna.valor
            END
            ORDER BY columna.orden
        )::text
        FROM jsonb_array_elements(configuracion.cot_con::jsonb)
             WITH ORDINALITY AS columna(valor, orden)
    ),
    cot_usu_mov = 'MIGRACION',
    cot_fec_mov = CURRENT_TIMESTAMP
WHERE configuracion.cot_act = TRUE
  AND jsonb_typeof(configuracion.cot_con::jsonb) = 'array'
  AND EXISTS (
      SELECT 1
      FROM jsonb_array_elements(configuracion.cot_con::jsonb) AS existente(valor)
      WHERE lower(regexp_replace(existente.valor ->> 'campo', '[^a-zA-Z0-9]', '', 'g')) IN ('empid', 'cliid')
         OR lower(regexp_replace(existente.valor ->> 'campo', '[^a-zA-Z0-9]', '', 'g')) LIKE '%tipmov'
         OR lower(regexp_replace(existente.valor ->> 'campo', '[^a-zA-Z0-9]', '', 'g')) LIKE '%caumov'
  );
