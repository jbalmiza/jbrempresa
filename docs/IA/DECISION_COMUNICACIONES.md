# Decisión arquitectónica: Comunicaciones omnicanal

Fecha: 6 de agosto de 2026.

## Agregado principal

- Una `Comunicacion` es la cabecera y unidad de trabajo, equivalente conceptualmente a la cabecera de una Venta.
- Un `Mensaje` es un elemento secuencial del hilo, equivalente conceptualmente al detalle de una Venta.
- El nombre funcional de la cabecera es `Conversación`; no se utilizará el término expediente en la interfaz.
- Un mensaje de entrada existe inicialmente sin conversación y permanece en Bandeja hasta ser clasificado.
- La clasificación vincula el mensaje a una conversación existente o crea una conversación nueva.
- Una comunicación puede ser inicialmente `CONVERSACION`, `AVISO` o `ALERTA`.
- Todos los tipos pueden contener mensajes de entrada, salida o internos.

## Identidad y canales

- `ContactoCanal` representa una identidad única dentro de un canal y cliente.
- `PersonaContactoCanal` establece la relación multivaluada: una identidad puede corresponder a varias Personas y una Persona puede utilizar varias identidades.
- La Persona puede quedar sin identificar al recibir el primer mensaje.
- El mensaje conserva nombre, DNI, teléfono y correo comunicados o detectados, además de la Persona finalmente identificada y su confianza.
- Canales previstos: simulado, correo, WhatsApp e interno. Solo el simulado se usa en la primera fase.
- Una identidad sin relaciones confirmadas aparece en Contactos sin Identificar.
- El estado identificado no se duplica en `contactos_canal`: se deduce por la existencia de una relación activa en `personas_contactos_canal`.
- Revocar una relación Persona–Identidad realiza una baja lógica: no modifica los mensajes históricos y solo evita sugerencias futuras. Si se confirma de nuevo, se reactiva la misma relación.
- Si existen varias Personas relacionadas, el usuario debe seleccionar cuál interviene en el mensaje concreto.

## Reglas

- Aislamiento obligatorio por cliente.
- Mensajes numerados dentro de su comunicación.
- Identificador externo único por cliente para impedir duplicados de webhooks.
- Estados controlados y asignación opcional a Usuario o Área.
- La IA será un autor posible, pero no se integra hasta definir supervisión, herramientas y auditoría.
- La identificación inicial utiliza coincidencias exactas por DNI, teléfono o correo.
- Una coincidencia nunca crea ni confirma automáticamente una Persona: un usuario debe escogerla o crearla.
- Solo se puede vincular un mensaje a una conversación de la misma Persona confirmada.
- Una conversación puede asignarse simultáneamente a un Usuario responsable y a un Área responsable.

## Primera fase implementada

- Modelo, repositorios, API y migración.
- Bandeja con creación simulada, hilo, respuesta y cambio de estado.
- Vistas filtradas de Conversaciones y Pendientes de Revisión.
- Bandeja real de mensajes sin conversación, clasificación manual y vinculación a conversación nueva o existente.
- Fechas diferenciadas de recepción, procesamiento y movimiento.
- Documentos e imágenes asociados a cada mensaje mediante Adjuntos.
- Identificación supervisada, alta de Persona desde el mensaje, conversaciones sugeridas por Persona y asignación a Usuario/Área.
- Identidades reutilizables, relaciones con varias Personas y pantalla Contactos sin Identificar.
- Selección de Persona mediante el buscador genérico compartido con Ventas.
- Confirmación visible de la Persona y búsqueda de conversaciones existentes mediante el mismo selector genérico.
- Gestión de Contactos de Canal con consulta de relaciones, alta supervisada y revocación lógica.
