-- Configuración SMTP funcional por cliente. La contraseña se informa posteriormente
-- desde el CRUD de Parámetros y el backend la cifra antes de persistirla.
INSERT INTO parametros
    (cli_id, par_cod, par_des, par_val, par_mod, par_usu_mov, par_fec_mov, par_act)
SELECT cliente.cli_id, dato.codigo, dato.descripcion, dato.valor,
       'ADMINISTRACION', 'migracion', CURRENT_TIMESTAMP, TRUE
FROM clientes cliente
CROSS JOIN (VALUES
    ('SMTP_HABILITADO', 'Habilitar envío de correo SMTP', 'false'),
    ('SMTP_SERVIDOR', 'Servidor SMTP', 'smtp.office365.com'),
    ('SMTP_PUERTO', 'Puerto SMTP', '587'),
    ('SMTP_USUARIO', 'Usuario de la cuenta SMTP', 'jesusbalmiza@hotmail.com'),
    ('SMTP_PASSWORD', 'Contraseña o clave de aplicación SMTP', ''),
    ('SMTP_REMITENTE', 'Dirección remitente SMTP', 'jesusbalmiza@hotmail.com'),
    ('SMTP_AUTENTICACION', 'Utilizar autenticación SMTP', 'true'),
    ('SMTP_STARTTLS', 'Utilizar STARTTLS en SMTP', 'true'),
    ('URL_FRONTEND', 'URL pública de GreenSaaS para enlaces', 'http://localhost:4200')
) AS dato(codigo, descripcion, valor)
WHERE NOT EXISTS (
    SELECT 1 FROM parametros parametro
    WHERE parametro.cli_id = cliente.cli_id
      AND parametro.par_mod = 'ADMINISTRACION'
      AND parametro.par_cod = dato.codigo
);
