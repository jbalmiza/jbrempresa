# Tareas pendientes

Última actualización: 5 de agosto de 2026.

Este documento recoge tanto el trabajo solicitado pendiente como las mejoras propuestas por Codex. Las propuestas no se implementan sin aprobación; sirven para responder de forma útil cuando el propietario pregunte «¿qué más?», «¿qué hacemos?» o «¿qué queda pendiente?».

## Prioridad alta: validación funcional

- Probar de extremo a extremo el histórico de Personas, Domicilios, Productos y Ventas:
  - alta `A` con histórico `1`;
  - modificación `M` creando una versión nueva;
  - baja lógica `B`;
  - deshacer hasta recuperar la versión anterior;
  - ocultación correcta de Modificar y Eliminar según selección e histórico.
- Probar una venta con varias líneas:
  - guardar la versión inicial;
  - modificar productos, cantidades y precios;
  - comprobar que cada versión conserva sus propias líneas;
  - comprobar baja y deshacer sin perder líneas.
- Validar en Gestión de Personas los tres modos: mapa, consulta y formulario.
- Comprobar que al seleccionar un domicilio se copian sus coordenadas a la persona y que posteriormente pueden modificarse de forma independiente.
- Probar Cancelar desde insertar y modificar en Personas, Domicilios, Productos y Ventas.

## Funcionalidad pendiente

- Decidir y, si se confirma, añadir una forma de consultar las líneas correspondientes a una versión desde el Histórico de Ventas. Actualmente se conservan en base de datos, pero la tabla histórica no ofrece una vista específica del detalle.
- Revisar y alinear las pantallas independientes `GestionDomicilios` y `GestionProductos` con las reglas nuevas de selección, histórico A/M/B, baja lógica y campos de movimiento.
- Aplicar el componente de documentación adjunta a futuros módulos o registros solo cuando se incorporen al alcance.

## Documentación pendiente

- Actualizar `BASE_DATOS.md` con:
  - parámetros por cliente y módulo;
  - adjuntos;
  - claves históricas compuestas;
  - tipos de movimiento A/M/B;
  - coordenadas propias de Persona;
  - pruebas integrales de la cadena Presupuesto -> Pedido -> Albarán -> Factura y de la conservación de sus líneas.
- Actualizar `COMPONENTES_REUTILIZABLES.md` con Documentación Adjunta y las vistas genéricas de tabla/mapa.
- Actualizar `MODULOS_ACTUALES.md`, `ARQUITECTURA.md` y `ROADMAP.md` con el estado real de las funcionalidades.

## Mantenimiento técnico

- Revisar la estrategia de migraciones: actualmente son scripts SQL versionados y aplicados manualmente; valorar Flyway o Liquibase antes de desplegar en más entornos.
- Añadir pruebas automatizadas específicas para creación de versiones, baja lógica, deshacer y aislamiento por cliente.
- Revisar las restricciones referenciales funcionales que se retiraron al convertir Domicilios, Productos y Ventas en entidades versionadas.

## Recomendaciones de evolución de GreenSaaS

Las siguientes ideas son recomendaciones, no encargos confirmados. Antes de ejecutar cualquiera se debe concretar alcance, prioridad y decisiones funcionales.

### Recomendación inmediata: núcleo común

- Diseñar `GreenSaaS Core` como capa transversal para autenticación, cliente actual, permisos, auditoría, parámetros, adjuntos, notificaciones, numeradores, exportación y bajas lógicas.
- Evitar continuar replicando manualmente histórico, movimiento, selección y adjuntos en cada pantalla.
- Crear contratos o servicios base reutilizables para las operaciones comunes de los registros versionados.
- Incorporar Flyway o Liquibase para que los cambios de base de datos sean repetibles y verificables.
- Establecer pruebas automáticas de aislamiento entre clientes antes de ampliar el número de módulos.

### Experiencia global del usuario

- Añadir un buscador global en la barra superior que localice personas, domicilios, productos, ventas, compras y documentos respetando cliente y permisos.
- Incorporar una bandeja de mensajes interna con recibidos, enviados, leídos, archivados y adjuntos.
- Permitir enviar mensajes a un usuario, varios usuarios, un perfil, un área o un módulo completo.
- Definir áreas o equipos y sus integrantes para distribuir mensajes, avisos y tareas.
- Añadir una campana de notificaciones con contador, prioridad, enlace al registro relacionado y opción de marcar como leída.
- Crear un centro de actividad reciente del usuario y del cliente.
- Añadir tareas y recordatorios vinculados a registros, con responsable, fecha límite, estado y prioridad.
- Incorporar un calendario o agenda para tareas, vencimientos y eventos.
- [IMPLEMENTADO BACKEND] Recursos agendables, horarios semanales, excepciones, reservas de una única agenda, capacidad y tareas.
- [IMPLEMENTADO BACKEND] Vínculo opcional de la reserva con cliente, versión concreta del documento comercial y conversación.
- [IMPLEMENTADO BACKEND] Trazabilidad de reprogramaciones y estados `PENDIENTE`, `CONFIRMADA`, `EN_CURSO`, `TERMINADA`, `CANCELADA` y `AUSENCIA`.
- [IMPLEMENTADO] `agendaRegistros` conectado con `/agenda` en Personas y Domicilios, con calendario real, horarios de franjas ilimitadas, excepciones, capacidad, márgenes y reservas manuales.
- Completar el vínculo de reservas individuales con Pedido antes de conectar Comunicaciones e IA.
- Añadir ayuda contextual y una guía inicial para usuarios nuevos.

### Dashboard e información de negocio

- Sustituir los resúmenes estáticos por un dashboard configurable con ventas, compras, cobros pendientes, stock mínimo, actividad y alertas.
- Permitir paneles distintos según usuario, perfil o área.
- Añadir indicadores con acceso directo a la consulta filtrada que explica cada cifra.
- Incorporar informes guardados y programación de informes periódicos.

### Permisos, seguridad y auditoría

- Evolucionar los permisos de módulo a permisos por operación: consultar, insertar, modificar, baja, deshacer, histórico, adjuntos, importar y exportar.
- Permitir restricciones por área, delegación, tipo de registro o datos sensibles.
- Crear una auditoría transversal que registre operación, usuario, fecha, entidad, clave, valores anteriores y nuevos.
- [IMPLEMENTADO] «¿Ha olvidado la contraseña?» mediante correo con enlace de un solo uso, token temporal, caducidad y revocación después del cambio. Cada cliente completa y habilita su configuración SMTP en Parámetros de Administración.
- Añadir limitación y bloqueo progresivo de intentos de acceso, con registro de los intentos y aviso al usuario o administrador.
- Completar el cambio de contraseña autenticado, la gestión de sesiones activas y el cierre remoto.
- Valorar autenticación de doble factor para perfiles sensibles.
- Definir políticas de contraseña, caducidad de sesión y conservación de auditoría.

### Configuración multiempresa

- Ampliar la configuración por cliente con nombre comercial, logo, colores, datos fiscales, idioma, zona horaria, moneda e impuestos.
- Añadir series y numeradores configurables para presupuestos, pedidos, albaranes y facturas.
- Permitir formatos documentales y plantillas diferentes por cliente.
- Controlar límites de almacenamiento, usuarios y módulos por cliente.
- Preparar planes de suscripción, renovaciones y estados cuando GreenSaaS vaya a comercializarse.

### Tablas, consultas e intercambio de datos

- [IMPLEMENTADO PARCIALMENTE] Permitir mostrar, ocultar y mover columnas en el componente Tabla, guardando la preferencia por usuario. Queda pendiente redimensionar columnas.
- Guardar vistas personales con filtros, orden, columnas y registros por página.
- Añadir filtros avanzados, selección múltiple y acciones masivas.
- Completar exportación consistente a CSV, Excel y PDF.
- Diseñar importaciones con plantilla, prevalidación, vista de errores y ejecución transaccional.
- Añadir papelera o consulta específica de bajas con restauración controlada.

### Procesos de ventas, compras y almacén

- Definir estados y transiciones: borrador, presupuesto, pedido, albarán, factura, cobrado o cancelado.
- Establecer qué operaciones se permiten en cada estado y quién puede realizarlas.
- Incorporar vencimientos, formas de pago, cobros parciales y conciliación.
- Añadir movimientos de stock, reservas, entradas, salidas, inventarios y trazabilidad.
- Definir impuestos, descuentos, redondeos y totales mediante reglas comunes.
- Relacionar documentos y comunicaciones con cada etapa del proceso.

### Automatizaciones e integraciones

- Crear reglas sencillas: stock bajo, factura vencida, tarea pendiente o ausencia de actividad.
- Permitir que una regla genere una notificación, mensaje, tarea o correo.
- Preparar una API pública versionada con permisos y límites de consumo.
- Añadir webhooks para altas, modificaciones, cambios de estado y vencimientos.
- Valorar integraciones con correo, almacenamiento externo, bancos, facturación y administración electrónica.

### Calidad y operación del SaaS

- Añadir registro estructurado de errores, trazabilidad de peticiones y monitorización.
- Preparar copias de seguridad, restauraciones probadas y política de conservación.
- Incorporar métricas de rendimiento, disponibilidad, almacenamiento y uso por cliente.
- Añadir pruebas de integración para los flujos críticos y pruebas de interfaz para operaciones principales.
- Definir entornos separados de desarrollo, pruebas y producción con despliegues repetibles.

## Criterio para proponer el siguiente trabajo

Cuando el propietario pregunte qué hacer, priorizar en este orden:

1. Corregir errores y validar las funcionalidades recién incorporadas.
2. Evitar deuda estructural que obligue a repetir cambios en todos los módulos.
3. Mejorar seguridad, aislamiento por cliente y recuperación de datos.
4. Incorporar funciones transversales de alto uso, como permisos, búsqueda, mensajes y notificaciones.
5. Desarrollar nuevos procesos de negocio.
6. Dejar para etapas posteriores suscripciones, automatizaciones avanzadas e integraciones externas.

## Organización interna: validación pendiente

- Validar Áreas Organizativas: alta raíz, alta hija, modificación, prevención de ciclos y bloqueo de baja con dependencias.
- Validar Personal de Área: asignación múltiple, una sola área principal activa, responsable, vigencia y aislamiento por cliente.
- Revisar y completar los datos de las Personas provisionales creadas para usuarios que carecían de Persona.
- Validar que alta y modificación de usuarios exigen una Persona activa y no permiten vincularla a dos usuarios del mismo cliente.

## Estado de GreenSaaS Core

- [INICIADO] La primera base centraliza el contexto de operación backend y la sesión frontend.
- Extraer progresivamente permisos, auditoría, parámetros, adjuntos, históricos y bajas lógicas cuando se diseñen sus contratos comunes.
- No añadir abstracciones al núcleo sin al menos un consumidor real y una responsabilidad transversal comprobada.

## Catálogos territoriales: validación pendiente

- Introducir datos siguiendo el orden País → Provincia → Municipio → Código postal → Vía para cada cliente.
- Validar altas, modificaciones, bajas bloqueadas por dependencias y aislamiento entre clientes.
- Crear o modificar un domicilio seleccionando una Vía y comprobar el rellenado automático de tipo, código postal, municipio y provincia.
- Revisar los domicilios históricos anteriores: conservarán sus textos y deberán seleccionar una Vía al realizar una nueva modificación.

## Comunicaciones omnicanal

- [IMPLEMENTADO] Modelo mínimo de contactos de canal, comunicaciones y mensajes.
- [IMPLEMENTADO] Bandeja y canal simulado para validar el flujo sin depender de WhatsApp, correo o IA.
- [IMPLEMENTADO] Identificación exacta supervisada, creación de Persona, sugerencia de conversaciones y asignación a Usuario/Área.
- [IMPLEMENTADO] Identidades reutilizables, relación con varias Personas y pantalla Contactos sin Identificar.
- [IMPLEMENTADO] Selectores genéricos para confirmar Personas y buscar conversaciones desde la Bandeja y Contactos sin Identificar.
- [IMPLEMENTADO] Consulta y revocación lógica de relaciones Persona–Identidad; los mensajes históricos conservan su Persona.
- Implementar la política de autonomía por cliente, proceso y acción con niveles `PROPONER`, `CONFIRMAR`, `AUTOMATICO` y `BLOQUEAR`.
- Registrar para cada actuación automática la acción, autor, fecha, datos utilizados, resultado, motivo y confirmación humana cuando corresponda.
- Conectar IA y canales reales únicamente tras validar estados, trazabilidad, idempotencia y la política de autonomía aprobada.
