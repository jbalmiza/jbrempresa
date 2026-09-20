# Mensajería interna

## Modelo

- `conversaciones_internas`: asunto y fechas de la conversación.
- `participantes_conversacion_interna`: participantes, vigencia y última lectura individual.
- `mensajes_internos`: remitente, contenido, clasificación, fechas y estado activo.

La lectura se conserva por participante. La baja lógica afecta a toda la conversación; la eliminación física elimina también sus mensajes y participantes.

Los avisos y alertas dirigidos a `MENSAJES` se entregan desde `AvisoAlertaDistribucionService` y se leen por usuario y versión en `avisos_alertas_lecturas`. Se presentan en la misma bandeja y contador de la cabecera, sin incorporarse a `conversaciones_internas` y sin permitir respuestas. Véase [Avisos y alertas](AVISOS_ALERTAS.md).

Las respuestas de mensaje incluyen la lista de destinatarios calculada a partir de los participantes, excluyendo al emisor. El resumen de conversación incluye los mismos datos para su último mensaje.

## Permisos de destinatarios

- Empleado: puede dirigirse al Jefe de su empresa.
- Jefe: puede dirigirse a uno, varios o todos los Empleados de su empresa y al Administrador.
- Administrador: puede dirigirse a uno, varios o todos los Jefes y Empleados; cuando existe empresa activa en el contexto, los destinatarios quedan limitados a ella.

Solo el Administrador, si es el remitente, modifica el contenido de un mensaje desde Registro de Administración. No existe eliminación individual de mensajes: solo el Administrador participante puede dar de baja la conversación completa desde Gestión de Administración. Cualquier participante autorizado puede leer y responder mientras esté activa. La clasificación queda fijada en el envío y no puede modificarse posteriormente.

La acción de baja en Gestión es una baja lógica de la conversación completa para todos sus participantes. Registra `fechaBaja` y el usuario responsable y añade al hilo el mensaje final `Conversación dada de baja por X en fecha y hora`. Solo el Administrador consulta las bajas, las reactiva y realiza eliminación física individual o masiva hasta una fecha. La eliminación física se rechaza sobre conversaciones activas.

En el alta de conversación, `asunto` es obligatorio y `contenido` es opcional. Si no se proporciona contenido se conserva una cadena vacía. En las respuestas, `contenido` sigue siendo obligatorio.

La bandeja ordinaria y el contador de no leídos filtran por `ConversacionInterna.activa`. Una baja no puede producir avisos para ninguno de sus participantes. El mensaje inicial vacío continúa siendo un registro válido y no se excluye por su contenido.

## Clasificación

Los valores válidos son `NORMAL`, `AVISO` y `ALERTA`. La validación se aplica en DTO y base de datos.

## API

- `GET /mensajeria-interna/destinatarios`
- `GET /mensajeria-interna/conversaciones`
- `GET /mensajeria-interna/conversaciones/bajas`
- `GET /mensajeria-interna/conversaciones/{id}/mensajes`
- `PATCH /mensajeria-interna/conversaciones/{id}/lectura`
- `GET /mensajeria-interna/no-leidos`
- `POST /mensajeria-interna/conversaciones`
- `POST /mensajeria-interna/conversaciones/{id}/mensajes`
- `PATCH /mensajeria-interna/conversaciones/{id}/baja`
- `PATCH /mensajeria-interna/conversaciones/{id}/reactivacion`
- `DELETE /mensajeria-interna/conversaciones/{id}`
- `DELETE /mensajeria-interna/conversaciones?hasta=AAAA-MM-DD`
- `PUT /mensajeria-interna/mensajes/{id}`

La migración de referencia es `database/migrations/20260914_mensajeria_interna.sql`.

## Restricción administrativa (2026-09-15)

Aplicación de [BE-DIR-039](DIRECTIVAS.md). Las URL y DTO existentes se mantienen. Se refuerzan `PUT /mensajes/{id}` y `PATCH /conversaciones/{id}/baja` con la comprobación de Administrador antes de acceder a datos. Consulta de bajas, reactivación y eliminación ya exigían este perfil. La API valida la identidad autenticada, no el modo visual del cliente. La separación Registro/Gestión se aplica en el componente común y en las rutas de Administración.
