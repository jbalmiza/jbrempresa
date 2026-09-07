# Recepción de WhatsApp mediante Meta

La integración utiliza un adaptador de Meta separado del núcleo de comunicaciones. Los mensajes recibidos terminan en el mismo flujo interno que los mensajes simulados y que los futuros canales de correo o telefonía.

## Parámetros por cliente

Se registran en el módulo `COMUNICACIONES`:

- `WHATSAPP_PHONE_NUMBER_ID`: identificador del número receptor en Meta.
- `WHATSAPP_BUSINESS_ACCOUNT_ID`: identificador de la cuenta empresarial.
- `WHATSAPP_ACCESS_TOKEN`: token para futuras operaciones de salida; se almacena cifrado.
- `WHATSAPP_APP_SECRET`: secreto empleado para validar la firma de los webhooks; se almacena cifrado.
- `WHATSAPP_WEBHOOK_VERIFY_TOKEN`: token elegido por el cliente para verificar el webhook; se almacena cifrado.
- `WHATSAPP_GRAPH_API_VERSION`: versión habilitada en Meta, por ejemplo `v23.0`.

Los valores sensibles se devuelven ocultos desde el CRUD de parámetros.

## Webhook

- URL: `/webhooks/meta/whatsapp`
- `GET`: verificación inicial solicitada por Meta.
- `POST`: recepción de eventos firmados.

El receptor identifica al cliente mediante `phone_number_id`, valida `X-Hub-Signature-256`, evita duplicados mediante el identificador externo de Meta y registra cada mensaje en la bandeja con canal `WHATSAPP`.

Los eventos de estado se aceptan sin crear mensajes. Los textos y respuestas interactivas se registran directamente. En esta primera fase, los mensajes multimedia dejan constancia de su tipo y pie; la descarga del archivo se incorporará en la siguiente fase.

## Respuestas

Al registrar un mensaje con dirección `SALIDA` dentro de una conversación cuyo canal sea `WHATSAPP`, el adaptador envía el texto al teléfono del último mensaje entrante. El identificador devuelto por Meta queda registrado en `men_id_ext` y el estado pasa a `ENVIADO`.

Las conversaciones `SIMULADO` e `INTERNO` no realizan llamadas a Meta. Esto permite validar el circuito funcional antes de configurar un número real.
