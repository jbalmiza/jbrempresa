-- Twilio queda seleccionado mientras se mantiene disponible la integración directa con Meta.
INSERT INTO parametros(cli_id,par_cod,par_des,par_val,par_mod,par_usu_mov,par_fec_mov,par_act)
SELECT cli_id,'WHATSAPP_PROVEEDOR','Proveedor de WhatsApp: TWILIO o META','TWILIO',
       'COMUNICACIONES','SISTEMA',CURRENT_TIMESTAMP,TRUE
FROM clientes
ON CONFLICT (cli_id,par_mod,par_cod) DO NOTHING;

INSERT INTO parametros(cli_id,par_cod,par_des,par_val,par_mod,par_usu_mov,par_fec_mov,par_act)
SELECT c.cli_id,p.codigo,p.descripcion,'','COMUNICACIONES','SISTEMA',CURRENT_TIMESTAMP,TRUE
FROM clientes c CROSS JOIN (VALUES
 ('TWILIO_ACCOUNT_SID','Account SID de Twilio'),
 ('TWILIO_AUTH_TOKEN','Auth Token de Twilio'),
 ('TWILIO_API_KEY_SID','SID de API Key de Twilio'),
 ('TWILIO_API_KEY_SECRET','Secret de API Key de Twilio'),
 ('TWILIO_WHATSAPP_FROM','Número emisor Twilio, por ejemplo whatsapp:+14155238886'),
 ('TWILIO_WEBHOOK_URL','URL pública exacta del webhook de Twilio')
) AS p(codigo,descripcion)
ON CONFLICT (cli_id,par_mod,par_cod) DO NOTHING;
