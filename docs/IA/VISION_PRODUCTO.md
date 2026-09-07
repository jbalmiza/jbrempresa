# Visión y objetivo de GreenSaaS

Última actualización: 11 de agosto de 2026.

Este documento es la referencia funcional principal del producto. Se actualiza cuando una decisión confirmada modifica el objetivo, los procesos o los límites del sistema. Las propuestas se distinguen expresamente de las decisiones aprobadas.

## 1. Objetivo del producto

[CONFIRMADO] GreenSaaS es una plataforma SaaS multiempresa destinada a realizar y asistir el trabajo administrativo y operativo de empresas de distintos sectores mediante procesos configurables, comunicaciones omnicanal e inteligencia artificial integrada.

El centro del producto no es un CRUD aislado ni un chatbot. Es la capacidad de recibir una necesidad expresada por un cliente, comprenderla, relacionarla con los datos y recursos reales de la empresa, ejecutar el proceso autorizado y mantener la comunicación hasta su resolución.

Ejemplos objetivo:

- recibir por WhatsApp una petición de bocadillos o pizzas, registrar el cliente y el pedido, calcular preparación, asignar recursos y confirmar la entrega;
- recibir una solicitud para un taller, identificar vehículo o servicio, consultar mecánicos, instalaciones y horarios, proponer una cita y reservarla;
- atender consultas sobre productos, disponibilidad y precios empleando el catálogo real;
- elaborar presupuestos, convertirlos en pedidos y continuar hacia albaranes y facturas;
- reservar una instalación, pista, sala o domicilio con agenda;
- extender el mismo flujo a correo, llamadas transcritas y futuros canales sin rediseñar el núcleo.

## 2. Propuesta de valor

GreenSaaS debe actuar como un administrativo digital de la empresa, con supervisión y límites configurables. Debe:

1. Centralizar mensajes y conversaciones de diferentes canales.
2. Identificar a la persona con evidencias de canal y datos de negocio.
3. Interpretar intención, productos, servicios, cantidades, fechas y restricciones.
4. Consultar datos reales de catálogo, clientes, ventas, recursos y agendas.
5. Proponer o ejecutar acciones permitidas y dejar trazabilidad.
6. Mantener informado al cliente y al personal responsable.
7. Convertir cada compromiso adquirido con el cliente en trabajo operativo visible.

## 3. Principios funcionales confirmados

- Multiempresa: los datos, configuración, documentos, canales y operaciones se aíslan por cliente desde el JWT en backend.
- Configuración por cliente: una variable funcional que pueda cambiar entre clientes pertenece a Parámetros, no al código.
- Persona como identidad de negocio: usuarios, clientes, personal de área e identidades de canal se relacionan con Personas.
- Conversación como unidad comunicativa: contiene la secuencia completa de mensajes; no se denomina expediente en la interfaz.
- Mensaje como evidencia: conserva canal, dirección, autor, fechas, identidad conocida, contenido y adjuntos.
- Venta como necesidad comercial: puede contener productos, servicios o ambos.
- Duraciones operativas: productos y servicios pueden aportar tiempo de fabricación o ejecución.
- Recursos agendables: Personas y Domicilios disponen de agenda y podrán participar en una misma reserva.
- Histórico y auditoría: las operaciones relevantes conservan usuario, fecha, tipo y causa del movimiento.
- Componentes comunes: tablas, selectores, adjuntos, árboles, mapas, mallas y agendas se reutilizan cuando el comportamiento es transversal.
- Datos de desarrollo: los datos actuales son de prueba y no deben forzar un diseño incorrecto.

## 4. Modelo conceptual objetivo

```text
Canal externo
  -> Mensaje de entrada
  -> Conversación
  -> Persona / identidad de canal
  -> Intención y datos interpretados
  -> Presupuesto / Pedido / Venta / Reserva
  -> Recursos + disponibilidad
  -> Tareas operativas
  -> Respuesta al cliente
  -> Seguimiento y cierre
```

Las responsabilidades deben permanecer separadas:

- **Conversación:** qué se ha comunicado y acordado.
- **Documento comercial:** qué se solicita, vende, entrega o factura.
- **Reserva:** cuándo se realizará y qué recursos quedan ocupados.
- **Tarea operativa:** qué debe hacer una persona o área.
- **Agenda/disponibilidad:** cuándo puede utilizarse cada recurso.

[RECOMENDACIÓN ARQUITECTÓNICA] No vincular directamente mensajes a huecos de calendario ni convertir la agenda en una lista de ventas. Estas capas evitan rehacer el sistema al incorporar talleres, restauración, instalaciones o nuevos canales.

## 5. Comunicaciones e inteligencia artificial

### Alcance confirmado

- WhatsApp es el primer canal real.
- La arquitectura debe admitir después correo, llamadas transcritas y otros canales.
- La Bandeja contiene mensajes entrantes aún pendientes de clasificación.
- Una conversación reúne el hilo de mensajes asociados a un mismo asunto.
- La IA debe consultar datos reales; no debe inventar productos, precios, disponibilidad ni estados.
- Los adjuntos pertenecen al mensaje que los recibió o envió y deben conservarse en la conversación.

### Evolución prevista

1. Recepción y respuesta real por WhatsApp Cloud API.
2. Identificación y clasificación asistidas.
3. Propuestas de respuesta basadas en catálogo y reglas.
4. Creación supervisada de pedidos, ventas y reservas.
5. Automatización configurable de operaciones de bajo riesgo.
6. Gestión autónoma de conversaciones dentro de límites, permisos y escalado humano.

### Control necesario

[CONFIRMADO] La autonomía no es un único interruptor global. Se configura por cliente, proceso y acción mediante cuatro niveles:

- `PROPONER`: preparar la respuesta u operación sin ejecutarla;
- `CONFIRMAR`: exigir aprobación humana antes de ejecutar;
- `AUTOMATICO`: ejecutar dentro de reglas y límites configurados;
- `BLOQUEAR`: detener y escalar a una persona.

La configuración inicial permite automatizar consultas de catálogo, precios, disponibilidad, clasificación y respuestas informativas. Pedidos, presupuestos, reservas, modificaciones y cancelaciones requieren confirmación. Cobros, devoluciones, descuentos excepcionales, datos sensibles, conflictos y coincidencias dudosas se bloquean y escalan.

Toda actuación debe registrar acción, autor IA/usuario, fecha, datos utilizados, resultado, motivo y confirmación humana cuando corresponda. Cambiar el nivel de autonomía será configuración y no requerirá modificar el desarrollo.

## 6. Ventas y documentos comerciales

[CONFIRMADO]

- Una venta admite líneas de producto, servicio o mezcla de ambos.
- Las versiones de una venta conservan sus propias líneas.
- El flujo documental se compone de Presupuesto, Pedido, Albarán y Factura.
- Un presupuesto puede convertirse en pedido.
- Desde pedido se continúa hacia albarán y factura; no se genera directamente una factura saltando el pedido cuando el flujo requiere trazabilidad.
- El concepto de venta directa se representa funcionalmente como factura cuando corresponda.

[CONFIRMADO E IMPLEMENTADO] `documentos_venta` y `documentos_venta_detalle` constituyen el único modelo comercial. El modelo anterior `ventas`/`venta_detalle` fue retirado sin compatibilidad transitoria porque contenía exclusivamente datos de prueba. Facturas utiliza el mismo modelo que Presupuestos, Pedidos y Albaranes.

[CONFIRMADO E IMPLEMENTADO] La numeración automática de documentos es transaccional, independiente por cliente y tipo, y no reutiliza números al eliminar documentos. La API comercial utiliza DTO validados y recalcula importes en backend.

[PENDIENTE] Deben completarse estados, formatos de numeración configurables, conversiones parciales, anulaciones, cobros e impuestos antes de producción.

## 7. Agenda, reservas, recursos y trabajo

[CONFIRMADO]

- Una Persona y un Domicilio pueden tener una agenda.
- Las agendas muestran día, semana y mes.
- Los solapamientos están prohibidos inicialmente.
- Se configuran días laborables, días no habilitados, horario de inicio y fin y varias franjas con interrupciones.
- Deben existir excepciones para fechas completas o tramos concretos.
- Una venta utiliza las duraciones de sus productos y servicios para ocupar tiempo.
- El objetivo futuro es que la IA consulte recursos y disponibilidad, proponga huecos, registre la reserva tras la confirmación del cliente y comunique el resultado.

[CONFIRMADO] La agenda genérica se implementará basada en:

- recurso (`PERSONA`, `DOMICILIO` y futuros tipos);
- horario semanal;
- excepciones de disponibilidad;
- reserva con inicio, fin, estado y duración consolidada;
- asignación a una única agenda;
- referencia al documento comercial y a su versión;
- tareas operativas derivadas para las personas responsables.

La duración se guarda como una fotografía al reservar. Un cambio posterior en el catálogo no debe alterar citas ya comprometidas.

[CONFIRMADO]

- las líneas se ejecutan consecutivamente y sus duraciones se suman;
- una operación puede dividirse en varias reservas, cada una perteneciente a una sola agenda;
- preparación y limpieza son márgenes configurables;
- las Personas tienen capacidad uno;
- los Domicilios tienen capacidad configurable;
- los estados son `PENDIENTE`, `CONFIRMADA`, `EN_CURSO`, `TERMINADA`, `CANCELADA` y `AUSENCIA`;
- se prohíben solapamientos cuando se supera la capacidad del recurso;
- la duración se copia como fotografía al crear la reserva;
- cada reprogramación conserva trazabilidad del horario anterior;
- cancelar una reserva no cancela automáticamente su pedido o factura.

Una venta o documento comercial no se inserta directamente en el calendario. La entidad Reserva actúa como vínculo entre documento, cliente, conversación, una única agenda y tareas operativas. Si una operación debe ocupar varias agendas, se crean reservas independientes relacionadas con el mismo documento.

## 8. Puesto de trabajo operativo

[PROPUESTO] El calendario no es suficiente para quien ejecuta el trabajo. GreenSaaS deberá ofrecer una vista de trabajo con:

- siguiente pedido, cita o tarea;
- hora prevista y prioridad;
- persona cliente y ubicación;
- productos, servicios, cantidades e instrucciones;
- información relevante extraída de la conversación;
- estados pendiente, en curso, terminado, entregado o cancelado;
- avisos de retraso y comunicación asociada.

Esta vista permitirá utilizar el mismo núcleo en una cocina, un taller, un centro deportivo o un servicio profesional.

## 9. Módulos del producto

- **Administración:** usuarios, perfiles, organización, parámetros y configuración transversal.
- **Comunicaciones:** bandeja, conversaciones, revisión, contactos de canal, canales e IA.
- **Territorio:** domicilios, estructura territorial, mapas y agenda de instalaciones.
- **Personas:** personas físicas/jurídicas, contactos, relaciones, ubicación, agenda y personal.
- **Productos:** catálogo, stock, precios, duración y organización visual mediante malla.
- **Servicios:** catálogo, precios, duración y requisitos de ejecución.
- **Compras:** proveedores, compras y abastecimiento.
- **Ventas:** clientes comerciales, presupuestos, pedidos, albaranes, facturas y líneas mixtas.
- **Planificación/operación:** reservas, recursos y tareas; su ubicación definitiva en menús se decidirá al implementar el modelo.

## 10. Requisitos transversales

- Seguridad por operación y no solo por acceso al módulo.
- DTO y validación de entradas; las entidades JPA no constituyen el contrato público.
- Errores homogéneos y comprensibles.
- Idempotencia para webhooks y acciones de IA.
- Auditoría de propuesta, aprobación, ejecución y resultado.
- Zona horaria por cliente y fechas del servidor.
- Configuración y secretos de canal cifrados por cliente.
- Migraciones repetibles antes de producción.
- Observabilidad de mensajes, automatizaciones, costes y errores.
- Límites de consumo, almacenamiento y automatización por cliente.
- Protección de datos, conservación y eliminación conforme al uso real.

## 11. Criterio para aceptar nuevos desarrollos

Toda petición nueva se contrastará con este documento y se clasificará:

- **Compatible:** amplía la visión sin cambiar decisiones previas.
- **Ajuste necesario:** la intención es válida, pero la solución propuesta generaría duplicidad o deuda.
- **Contradicción:** modifica una decisión confirmada o dos comportamientos no pueden coexistir.
- **Inviable:** no puede garantizarse técnica, legal, económica u operativamente con las restricciones conocidas.

Ante ajuste, contradicción o inviabilidad, la IA debe informar antes de implementar, explicar el impacto, proponer alternativas y solicitar la decisión cuando cambie el modelo.

## 12. Tensiones y decisiones abiertas detectadas

No son errores del propietario; son puntos que han evolucionado y deben resolverse conscientemente:

1. **Supervisión frente a autonomía de IA.** Resuelto el 11 de agosto de 2026 mediante los niveles configurables `PROPONER`, `CONFIRMAR`, `AUTOMATICO` y `BLOQUEAR`, definidos por cliente, proceso y acción.
2. **Modelo comercial unificado.** Resuelto el 11 de agosto de 2026: Presupuesto, Pedido, Albarán y Factura son los únicos documentos comerciales y comparten cabecera y líneas genéricas.
3. **Permisos pausados frente a acciones diferenciadas.** La interfaz distingue Ver, Modificar, Baja, Histórico y Adjuntos, pero la autorización efectiva por operación está pausada. Será obligatoria antes de producción o autonomía real.
4. **Agenda simple frente a planificación real.** Resuelto arquitectónicamente e implementado en backend y frontend entre el 11 y 12 de agosto de 2026 mediante reservas de una única agenda, horarios, excepciones, capacidad, tareas, trazabilidad y el componente genérico `agendaRegistros` compartido por Personas y Domicilios.
5. **Datos de prueba frente a producción.** La libertad actual para regenerar datos termina al usar datos reales; antes serán necesarias migraciones automáticas, copias, pruebas y políticas de conservación.

## 13. Hoja de ruta recomendada

1. Estabilizar CRUD, históricos, aislamiento y componentes comunes.
2. Consolidar Ventas y documentos comerciales.
3. Diseñar recursos, disponibilidad, reservas y tareas; validar manualmente Persona + Domicilio.
4. Completar WhatsApp real con recepción, envío, seguridad e idempotencia.
5. Vincular conversación con cliente y documento comercial.
6. Permitir que la IA proponga productos, servicios, pedidos y huecos con aprobación humana.
7. Incorporar el puesto de trabajo operativo y estados de ejecución.
8. Habilitar automatización configurable y progresiva.
9. Añadir correo, llamadas transcritas y nuevos sectores sobre los mismos contratos.
