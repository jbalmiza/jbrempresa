# Configuración de Twilio WhatsApp en GreenSaaS

Revisión del código y configuración local: 2026-09-15. La interfaz, condiciones y disponibilidad de la cuenta externa no se han verificado en esta revisión.

## Preparación

Para probar el proceso interno se puede usar SIMULADO sin conectar un teléfono. Para integrar Twilio hace falta un remitente y credenciales habilitados por ese proveedor, además de una URL HTTPS que llegue al backend. La página de Facebook no es un requisito del código local. Esta guía no requiere migrar el teléfono particular.

Los parámetros de COMUNICACIONES son WHATSAPP_PROVEEDOR=TWILIO, TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN, TWILIO_API_KEY_SID, TWILIO_API_KEY_SECRET, TWILIO_WHATSAPP_FROM y TWILIO_WEBHOOK_URL. El remitente es el número del proveedor en formato whatsapp:+NUMERO_INTERNACIONAL; no el móvil destinatario de la prueba.

El Auth Token valida firmas entrantes y la API Key autoriza los envíos. Introducir secretos únicamente en el mecanismo de configuración protegido. Consulte el [manual de parámetros](MANUAL_PARAMETROS_CLIENTE.md).

## Conexión y prueba

1. Arrancar backend y frontend conforme a [Operación](../backend/docs/OPERACION.md).
2. Disponer de una URL HTTPS accesible que reenvíe al backend. Un túnel de desarrollo debe permanecer activo durante la prueba.
3. Configurar POST /webhooks/twilio/whatsapp en el proveedor y guardar la misma URL completa en TWILIO_WEBHOOK_URL. Si cambia el dominio del túnel, actualizar ambos.
4. Usar un destinatario habilitado en el entorno del proveedor y enviar un mensaje al remitente configurado.
5. Consultar Comunicaciones, identificar la Persona y clasificar el mensaje en una conversación.
6. Registrar una respuesta de salida en esa conversación y comprobar el identificador externo y resultado. ENVIADO no acredita por sí solo lectura o entrega final.

## Diagnóstico

| Resultado | Comprobar |
|---|---|
| No llega al backend | URL HTTPS, túnel y puerto; configuración de recepción del proveedor. |
| Firma rechazada | Auth Token y coincidencia exacta de URL, protocolo, ruta y barra final. |
| Empresa no encontrada | TWILIO_WHATSAPP_FROM y destinatario To del evento. |
| Salida rechazada | Proveedor activo, cuenta, API Key, remitente y habilitación del destinatario. |
| Funciona SIMULADO pero no WhatsApp | Conectividad y configuración externa; son circuitos diferentes. |

Los requisitos de alta, aprobación, límites de prueba y paso a producción deben comprobarse en la cuenta y documentación vigente del proveedor. Esta actualización no realizó envíos externos ni cambió credenciales.
