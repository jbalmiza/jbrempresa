ALTER TABLE recursos_agendables
    ADD COLUMN IF NOT EXISTS rag_hor_vis TIME NOT NULL DEFAULT '08:00';
