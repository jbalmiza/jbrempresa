-- Datos de prueba: distribuye todos los domicilios dentro del área urbana de Málaga.
-- Sistema de referencia utilizado por la aplicación: ETRS89 / UTM huso 30 (EPSG:25830).
WITH domicilios_ordenados AS (
    SELECT
        ctid,
        row_number() OVER (ORDER BY cli_id, dom_id, dom_id_his) - 1 AS posicion
    FROM domicilios
)
UPDATE domicilios AS domicilio
SET
    dom_cox = 371500
        + ((ordenado.posicion % 5) * 1150)
        + ((floor(ordenado.posicion / 5)::integer % 5) * 120),
    dom_coy = 4063600
        + ((floor(ordenado.posicion / 5)::integer % 5) * 900)
        + ((ordenado.posicion % 5) * 95),
    dom_hus = 25830
FROM domicilios_ordenados AS ordenado
WHERE domicilio.ctid = ordenado.ctid;
