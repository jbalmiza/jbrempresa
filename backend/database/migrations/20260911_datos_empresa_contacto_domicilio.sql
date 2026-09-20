-- Ampliación del maestro Empresa con datos mercantiles, contacto y domicilio fiscal.

ALTER TABLE empresas ADD COLUMN IF NOT EXISTS emp_raz_soc VARCHAR(200);
ALTER TABLE empresas ADD COLUMN IF NOT EXISTS emp_nif VARCHAR(30);
ALTER TABLE empresas ADD COLUMN IF NOT EXISTS emp_act_eco VARCHAR(200);
ALTER TABLE empresas ADD COLUMN IF NOT EXISTS emp_tel VARCHAR(30);
ALTER TABLE empresas ADD COLUMN IF NOT EXISTS emp_ema VARCHAR(150);
ALTER TABLE empresas ADD COLUMN IF NOT EXISTS emp_web VARCHAR(250);
ALTER TABLE empresas ADD COLUMN IF NOT EXISTS dom_id BIGINT;

CREATE INDEX IF NOT EXISTS idx_empresas_domicilio_fiscal ON empresas(emp_id,dom_id);
