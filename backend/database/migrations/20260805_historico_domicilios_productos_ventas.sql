BEGIN;

ALTER TABLE domicilios ADD COLUMN IF NOT EXISTS dom_id_his BIGINT;
ALTER TABLE domicilios ADD COLUMN IF NOT EXISTS dom_tip_mov VARCHAR(1);
UPDATE domicilios SET dom_id_his = 1 WHERE dom_id_his IS NULL;
UPDATE domicilios SET dom_tip_mov = 'A' WHERE dom_tip_mov IS NULL;
UPDATE domicilios SET dom_act = TRUE WHERE dom_act IS NULL;
ALTER TABLE domicilios ALTER COLUMN dom_id_his SET NOT NULL;
ALTER TABLE domicilios ALTER COLUMN dom_tip_mov SET NOT NULL;
ALTER TABLE domicilios ALTER COLUMN dom_id DROP IDENTITY IF EXISTS;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS pro_id_his BIGINT;
ALTER TABLE productos ADD COLUMN IF NOT EXISTS pro_tip_mov VARCHAR(1);
UPDATE productos SET pro_id_his = 1 WHERE pro_id_his IS NULL;
UPDATE productos SET pro_tip_mov = 'A' WHERE pro_tip_mov IS NULL;
UPDATE productos SET pro_act = TRUE WHERE pro_act IS NULL;
ALTER TABLE productos ALTER COLUMN pro_id_his SET NOT NULL;
ALTER TABLE productos ALTER COLUMN pro_tip_mov SET NOT NULL;
ALTER TABLE productos ALTER COLUMN pro_id DROP IDENTITY IF EXISTS;

ALTER TABLE ventas ADD COLUMN IF NOT EXISTS ven_id_his BIGINT;
ALTER TABLE ventas ADD COLUMN IF NOT EXISTS ven_tip_mov VARCHAR(1);
UPDATE ventas SET ven_id_his = 1 WHERE ven_id_his IS NULL;
UPDATE ventas SET ven_tip_mov = 'A' WHERE ven_tip_mov IS NULL;
UPDATE ventas SET ven_act = TRUE WHERE ven_act IS NULL;
ALTER TABLE ventas ALTER COLUMN ven_id_his SET NOT NULL;
ALTER TABLE ventas ALTER COLUMN ven_tip_mov SET NOT NULL;
ALTER TABLE ventas ALTER COLUMN ven_id DROP IDENTITY IF EXISTS;

-- Las referencias funcionales conservan el identificador estable, no una versión histórica.
-- Se retiran las FK simples que impedirían que el mismo identificador tenga varias versiones.
DO $$
DECLARE restriccion RECORD;
BEGIN
  FOR restriccion IN
    SELECT conrelid::regclass AS tabla, conname
    FROM pg_constraint
    WHERE contype = 'f'
      AND confrelid IN ('domicilios'::regclass, 'productos'::regclass, 'ventas'::regclass)
  LOOP
    EXECUTE format('ALTER TABLE %s DROP CONSTRAINT %I', restriccion.tabla, restriccion.conname);
  END LOOP;
END $$;

DO $$
DECLARE restriccion RECORD;
BEGIN
  FOR restriccion IN
    SELECT conrelid::regclass AS tabla, conname
    FROM pg_constraint
    WHERE contype = 'p'
      AND conrelid IN ('domicilios'::regclass, 'productos'::regclass, 'ventas'::regclass)
  LOOP
    EXECUTE format('ALTER TABLE %s DROP CONSTRAINT %I', restriccion.tabla, restriccion.conname);
  END LOOP;
END $$;

ALTER TABLE domicilios ADD PRIMARY KEY (dom_id, dom_id_his);
ALTER TABLE productos ADD PRIMARY KEY (pro_id, pro_id_his);
ALTER TABLE ventas ADD PRIMARY KEY (ven_id, ven_id_his);

ALTER TABLE domicilios DROP CONSTRAINT IF EXISTS ck_domicilios_tip_mov;
ALTER TABLE domicilios ADD CONSTRAINT ck_domicilios_tip_mov CHECK (dom_tip_mov IN ('A', 'M', 'B'));
ALTER TABLE productos DROP CONSTRAINT IF EXISTS ck_productos_tip_mov;
ALTER TABLE productos ADD CONSTRAINT ck_productos_tip_mov CHECK (pro_tip_mov IN ('A', 'M', 'B'));
ALTER TABLE ventas DROP CONSTRAINT IF EXISTS ck_ventas_tip_mov;
ALTER TABLE ventas ADD CONSTRAINT ck_ventas_tip_mov CHECK (ven_tip_mov IN ('A', 'M', 'B'));

CREATE UNIQUE INDEX IF NOT EXISTS ux_domicilios_version_activa ON domicilios (cli_id, dom_id) WHERE dom_act = TRUE;
CREATE UNIQUE INDEX IF NOT EXISTS ux_productos_version_activa ON productos (cli_id, pro_id) WHERE pro_act = TRUE;
CREATE UNIQUE INDEX IF NOT EXISTS ux_ventas_version_activa ON ventas (cli_id, ven_id) WHERE ven_act = TRUE;

COMMIT;
