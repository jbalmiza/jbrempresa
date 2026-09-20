-- Facturación automática y estado de completitud de Personas.
ALTER TABLE personas ADD COLUMN IF NOT EXISTS per_dat_com boolean NOT NULL DEFAULT false;
ALTER TABLE documentos_venta ADD COLUMN IF NOT EXISTS dov_tip_fac varchar(15);
ALTER TABLE documentos_venta_movimientos ADD COLUMN IF NOT EXISTS dov_tip_fac varchar(15);

UPDATE personas
SET per_dat_com = (
  NULLIF(TRIM(COALESCE(per_tel, '')), '') IS NOT NULL
  AND NULLIF(TRIM(COALESCE(per_tip_per, '')), '') IS NOT NULL
  AND NULLIF(TRIM(COALESCE(per_tip_doc, '')), '') IS NOT NULL
  AND NULLIF(TRIM(COALESCE(per_doc, '')), '') IS NOT NULL
  AND COALESCE(dom_id, 0) > 0
  AND (
    (per_tip_per = 'JURIDICA' AND NULLIF(TRIM(COALESCE(per_raz_soc_cor, '')), '') IS NOT NULL)
    OR
    (per_tip_per <> 'JURIDICA' AND NULLIF(TRIM(COALESCE(per_nom, '')), '') IS NOT NULL AND NULLIF(TRIM(COALESCE(per_ape1, '')), '') IS NOT NULL)
  )
);
