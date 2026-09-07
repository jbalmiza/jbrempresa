BEGIN;

ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_cox DOUBLE PRECISION;
ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_coy DOUBLE PRECISION;
ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_hus BIGINT;

UPDATE personas p
SET per_cox = d.dom_cox,
    per_coy = d.dom_coy,
    per_hus = d.dom_hus
FROM domicilios d
WHERE p.cli_id = d.cli_id
  AND p.dom_id = d.dom_id
  AND d.dom_act = TRUE
  AND p.per_cox IS NULL
  AND p.per_coy IS NULL;

COMMIT;
