ALTER TABLE recursos_agendables
    ADD COLUMN IF NOT EXISTS rag_int_vis INTEGER NOT NULL DEFAULT 30;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'ck_recurso_intervalo_visual'
    ) THEN
        ALTER TABLE recursos_agendables
            ADD CONSTRAINT ck_recurso_intervalo_visual CHECK (rag_int_vis IN (5, 10, 15, 30, 60));
    END IF;
END $$;
