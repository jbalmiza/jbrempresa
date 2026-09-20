# Visión de GreenSaaS

Revisión: 2026-09-15. Visión funcional y evolución; no es un inventario de funciones terminadas.

GreenSaaS aspira a asistir el trabajo administrativo y operativo de empresas de distintos sectores: recibir una necesidad, identificar a la Persona, consultar catálogo y recursos, ejecutar el proceso autorizado y mantener la comunicación hasta resolverlo.

## Flujo y responsabilidades

Canal → mensaje → conversación → Persona → documento comercial → reserva y recursos → tareas → respuesta y seguimiento.

La conversación conserva lo comunicado; el documento comercial, lo solicitado, vendido o facturado; la reserva, el tiempo y recursos comprometidos; la tarea, el trabajo a ejecutar. Una reserva cancelada no cancela automáticamente el documento comercial. La duración se conserva al reservar.

El modelo comercial vigente es DocumentoVenta con líneas de productos y servicios y tipos Presupuesto, Pedido, Albarán y Factura. Las agendas actuales pertenecen a empleados y domicilios: Persona aporta identidad y contacto, pero no es titular de agenda. Las tareas y las reservas mantienen sus propios estados; no deben confundirse TERMINADA de Reserva con FINALIZADO de tarea.

## Objetivo de automatización confirmado, pendiente de implementación

Los niveles previstos por empresa, proceso y acción son PROPONER, CONFIRMAR, AUTOMATICO y BLOQUEAR. La IA deberá consultar datos reales, respetar permisos y límites y registrar propuesta, aprobación, ejecución y resultado. La supervisión prevista exige confirmación para operaciones comerciales y reservas; las excepciones sensibles deben escalarse. Estos niveles expresan la decisión funcional de evolución, no un motor disponible.

WhatsApp dispone de adaptadores de entrada y salida. La IA con herramientas y autonomía sigue pendiente. Empleados, capacidades, agendas y asignación de tareas ya tienen implementación y no deben presentarse como módulos por empezar.

## Fuente de verdad y siguiente trabajo

Las directivas permanentes se mantienen exclusivamente en [frontend](../frontend/docs/DIRECTIVAS.md) y [backend](../backend/docs/DIRECTIVAS.md), conforme a [AGENTS.md](../AGENTS.md). Este documento explica el propósito y no mantiene un catálogo alternativo.

Para lo disponible: [módulos](../backend/docs/MODULOS.md) y [guía de uso](../frontend/docs/GUIA_USO.md). Para diferencias y evolución: [limitaciones](../backend/docs/LIMITACIONES.md) y [planificación](../backend/docs/PLANIFICACION_FUTURA.md). La autonomía, pagos, autorización uniforme, migraciones reproducibles y pruebas completas no se consideran terminados.
