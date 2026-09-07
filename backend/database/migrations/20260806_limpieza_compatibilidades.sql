-- La identificación de un contacto se deriva de su relación activa con una Persona.
ALTER TABLE contactos_canal DROP COLUMN IF EXISTS coc_ver;
