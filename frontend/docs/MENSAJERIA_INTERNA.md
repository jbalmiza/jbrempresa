# Mensajería interna

## Componente común

`chatInterno` es la única implementación visual de mensajería interna. Se reutiliza en:

- el botón `Mensajes` de la barra superior, en modo `CHAT`;
- `Administración > Registro > Mensajes`, en modo `REGISTRO`;
- `Administración > Gestión > Gestión de Mensajes`, en modo `GESTION`.

En el modo `CHAT`, la bandeja también presenta avisos y alertas con ubicación `MENSAJES`. Proceden del maestro de Avisos y Alertas, se marcan como leídos individualmente y se suman al contador de la cabecera. No forman una conversación ni ofrecen Responder. La clasificación Aviso/Alerta de una conversación existente conserva su comportamiento propio.

La bandeja muestra conversaciones, contador de mensajes no leídos y clasificación visual. Abrir una conversación la marca como leída y, en los contextos administrativos que la ofrecen, `No leído` permite devolverla a ese estado. El contenido se refresca periódicamente sin recargar la ventana.

Cada mensaje visible, incluido el resumen del último mensaje en la bandeja, identifica siempre Fecha, Hora, Emisor y Destinatario o Destinatarios.

La clasificación se establece exclusivamente al enviar el mensaje o la respuesta y no se ofrecen acciones posteriores para cambiarla. Se representa en el borde completo del cajón, sin cambiar el color del texto ni añadir fondos de estado: neutro para Normal, amarillo para Aviso y rojo para Alerta. El hilo y la respuesta usan una composición compacta.

Al abrir una conversación, el componente desplaza el hilo automáticamente hasta el último mensaje y su área de respuesta.

La acción `Dar de baja` afecta al hilo completo y lo retira de la bandeja de todos sus participantes. Antes de cerrarlo se incorpora el mensaje de auditoría `Conversación dada de baja por X en fecha y hora`. El Administrador consulta las bajas en Administración: las reactiva desde Gestión y las elimina definitivamente o purga hasta una fecha desde Registro.

Al crear una conversación, el Asunto es obligatorio y el campo Mensaje es opcional. Las respuestas, al no disponer de asunto propio, mantienen obligatorio su contenido.

Una conversación formada solo por su Asunto sigue siendo válida y puede contar como pendiente. Una conversación dada de baja queda excluida de la bandeja y del contador de no leídos para todos sus participantes.

## Acciones por contexto

- `CHAT`: crear conversaciones y responder.
- `REGISTRO` (solo Administrador): crear, consultar, modificar mensajes propios y eliminar definitivamente conversaciones dadas de baja, individualmente o hasta una fecha.
- `GESTION` (solo Administrador): consultar, responder, dar de baja conversaciones y reactivarlas. Nunca elimina definitivamente.
- Todos los modos permiten clasificar un mensaje como `Normal`, `Aviso` o `Alerta`.

Normal utiliza presentación neutra, Aviso utiliza amarillo y Alerta utiliza rojo.

## Destinatarios

El destinatario se elige en un único selector:

- Administrador: Todos, Todos los jefes, Todos los empleados y cada usuario individual de la empresa seleccionada.
- Jefe: Administrador, Todos los empleados y cada empleado individual de su empresa.
- Empleado: su usuario Jefe.

Las opciones colectivas se resuelven en el frontend a identificadores concretos antes del envío. Para el Administrador, la empresa elegida en la cabecera delimita los jefes y empleados disponibles.

El frontend nunca calcula permisos de negocio a partir del contenido visible: consume exclusivamente la relación de destinatarios autorizados que entrega la API.

En la composición del mensaje, `Asunto` aparece inmediatamente encima de `Mensaje`; el orden es Destinatario, Tipo, Asunto y Mensaje.

## Contrato API

El servicio `MensajeriaInternaService` consume `/mensajeria-interna`: destinatarios, conversaciones, mensajes, lectura/no lectura, contador, creación, respuesta, modificación y eliminación. La clasificación forma parte del alta o respuesta; no existe una operación posterior para reclasificar.

## Restricción administrativa (2026-09-15)

Aplicación de [FE-DIR-047](DIRECTIVAS.md): las rutas son `/administracion/mensajes` y `/administracion/gestionMensajes`, protegidas por perfil y ocultas a otros usuarios. Se retiran las entradas de Comunicaciones. El modo CHAT no muestra Bajas, Dar de baja, Reactivar, Eliminar, purga por fecha, Modificar ni No leído. Conserva lectura del hilo, Nuevo mensaje y Responder. Los métodos administrativos también comprueban el modo antes de llamar a la API.
