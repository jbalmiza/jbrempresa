-- El tipo C.I.F. se guarda en per_tip_doc y su valor en per_doc.
UPDATE personas
SET per_tip_doc = 'C.I.F.',
    per_doc = COALESCE(NULLIF(per_doc, ''), per_cif)
WHERE per_tip_per = 'JURIDICA';

ALTER TABLE personas DROP COLUMN IF EXISTS per_cif;
