BEGIN;

UPDATE personas
SET per_tip_doc = 'C.I.F.',
    per_doc = CASE per_id
        WHEN 19 THEN 'B93625147'
        WHEN 20 THEN 'B29741863'
        ELSE per_doc
    END
WHERE per_tip_per = 'JURIDICA';

UPDATE personas
SET per_nom_com = TRIM(per_doc) || ' - ' || TRIM(per_raz_soc_lar)
WHERE per_tip_per = 'JURIDICA'
  AND per_doc IS NOT NULL
  AND TRIM(per_doc) <> '';

COMMIT;
