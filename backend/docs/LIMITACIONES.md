# Limitaciones conocidas y validación pendiente

Revisión: 2026-09-17. Fuente: contraste con código y auditoría documental. Estos puntos permanecen abiertos; la actualización documental no los corrige.

| Identificador | Estado actual | Trabajo pendiente |
|---|---|---|
| LIM-01 · Compras | CompraService usa alta/consulta en /compras; CompraController espera /compras/{empId}. No expone baja, histórico ni deshacer. | Alinear contrato, interfaz y validación del recorrido. No presentar el CRUD completo como operativo. |
| LIM-02 · Movimiento de Relaciones | Gestión muestra Usuario, Fecha y Activo; oculta Tipo/Causa y el maestro no conserva histórico versionado. | Cumplir las directivas de Datos Movimiento y resolver el histórico de Gestión. La tabla común sí declara los 13 campos existentes. |
| LIM-03 · Datos iniciales de relaciones | No existe `RelacionesEmpresaIniciales.java` ni otro inicializador de relaciones en el código actual. Las relaciones de ejemplo se crearon manualmente y no se reconstruyen automáticamente en una base nueva. | Si se necesitan relaciones de ejemplo reproducibles, definir y documentar su carga explícita. No atribuir al arranque la recreación de relaciones eliminadas. |
| LIM-04 · Autorización transversal | Hay guarda de rutas, permisos de módulo y comprobaciones por controlador/servicio; no existe un motor único de permisos por operación aplicado a todos los endpoints. | Revisar sistemáticamente operaciones y aislamiento. Ocultar un botón no constituye seguridad backend. |
| LIM-05 · Pedido a proveedor | Se crea un documento de venta en la empresa suministradora, utilizando los datos de contacto del formulario. | No existe aquí compra espejo ni enlace explícito del documento con la empresa compradora. No confundir el catálogo con un ciclo completo de aprovisionamiento. |
| LIM-06 · Pagos | Caja y marca Pagado registran operaciones internas. Redsys/Bizum no ejecuta un cobro real. | Implementar y validar pago externo, firma, callbacks, conciliación y devoluciones. |
| LIM-07 · Automatización IA | Comunicaciones y propuestas existen; los niveles de autonomía forman parte del objetivo. | Motor de interpretación y ejecución supervisada/autónoma, trazabilidad y límites operativos. |
| LIM-08 · Operación | Hibernate update e inicializadores conviven con scripts SQL. No hay gestor general de migraciones ni Actuator. | Migración reproducible, restauración ensayada y observabilidad antes de datos reales. |
| LIM-09 · Validación | Existen pruebas unitarias/integración parciales y comprobaciones locales registradas. | Recorrido E2E completo, concurrencia de stock/numeración y autorización de todos los módulos. |
| LIM-10 · Confirmación de catálogo | Muestra número e importe; Cerrar funciona según el contexto de apertura. | Comprobante, nuevo pedido y estimación pública completa permanecen pendientes. |
| LIM-11 · Parámetros y agenda | El selector de proveedor de Producto filtra activa; el catálogo de Proveedores comprueba además las fechas. Reserva usa TERMINADA y tarea usa FINALIZADO. | Mantener documentadas las diferencias; una futura unificación exige cambio funcional explícito. |

## Evidencia

- [CompraController](../src/main/java/com/jbrempresa/backend/controller/CompraController.java) y [CompraService](../../frontend/src/app/services/compra.service.ts).
- [Formulario Relaciones](../../frontend/src/app/pages/mAdministracion/relacionesEmpresa/relacionesEmpresa.html), [entidad](../src/main/java/com/jbrempresa/backend/entity/EmpresaRelacion.java) y [servicio de relaciones](../src/main/java/com/jbrempresa/backend/service/EmpresaRelacionService.java).
- [Catálogo de proveedores](../src/main/java/com/jbrempresa/backend/service/CatalogoProveedorService.java), [catálogo/pedidos](../src/main/java/com/jbrempresa/backend/service/CatalogoService.java).
- [Seguridad](SEGURIDAD.md), [pruebas](PRUEBAS.md), [evolución](PLANIFICACION_FUTURA.md).

La restricción de relaciones se aplica al módulo autenticado Proveedores. Un catálogo publicado sigue teniendo su ruta pública para Clientes; no constituye un catálogo privado exclusivamente B2B.
