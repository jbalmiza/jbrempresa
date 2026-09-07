ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS usu_tel VARCHAR(30);

UPDATE usuarios usuario
SET usu_nom = persona.per_nom_com,
    usu_tel = COALESCE(usuario.usu_tel, persona.per_tel),
    usu_ema = COALESCE(usuario.usu_ema, persona.per_ema)
FROM personas persona
WHERE persona.cli_id = usuario.cli_id
  AND persona.per_id = usuario.usu_per_id
  AND persona.per_act = TRUE;
