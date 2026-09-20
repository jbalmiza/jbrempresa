-- Recogida deja de ser un servicio disponible en cualquier empresa.
DELETE FROM recursos_capacidades
WHERE rec_ori = 'SERVICIO' AND UPPER(TRIM(rec_tip)) = 'RECOGIDA';

DELETE FROM servicios
WHERE UPPER(TRIM(ser_tip_ser)) = 'RECOGIDA';

DELETE FROM tipos_articulo
WHERE tia_cla = 'SERVICIO' AND UPPER(TRIM(tia_nom)) = 'RECOGIDA';
