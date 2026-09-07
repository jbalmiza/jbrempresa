BEGIN;

UPDATE personas
SET per_nom_com = CONCAT(
    TRIM(per_doc),
    ' - ',
    CASE
        WHEN per_tip_per = 'JURIDICA' THEN TRIM(per_raz_soc_lar)
        ELSE TRIM(CONCAT_WS(' ', per_nom, per_ape1, NULLIF(TRIM(per_ape2), '')))
    END
)
WHERE per_doc IS NOT NULL
  AND TRIM(per_doc) <> '';

COMMIT;
