# Pruebas y evidencia de validación

Revisión: 2026-09-15. Fuentes: [pruebas backend](../src/test/java/com/jbrempresa/backend) y [frontend](../../frontend/src/app).

## Inventario actual

| Archivo backend | Alcance |
|---|---|
| BackendApplicationTests | Carga de contexto Spring. |
| HorarioRecursoRepositoryTest | Persistencia/repositorio de horarios. |
| AsignacionPedidoServiceTest | Asignación de tareas de pedido. |
| ImagenCatalogoServiceTest | Origen de imagen TIPO/REGISTRO. |
| MensajeriaInternaServiceTest | Denegación de administración de mensajes a otros perfiles. |
| EmpresaRelacionServiceTest | Eliminación de pareja, empresa y coherencia. |
| CatalogoProveedorServiceTest | Relación/perfil/vigencia y delegación de pedido al proveedor. |

Frontend tiene app.spec, agendaRegistros.spec, gestionAgendas.spec, chatInterno.spec y catalogoPublico.spec. Los nombres identifican archivos, no número de casos ni cobertura completa.

## Cómo ejecutar

Desde backend: ` .\mvnw.cmd test`.
Selección: ` .\mvnw.cmd '-Dtest=CatalogoProveedorServiceTest,EmpresaRelacionServiceTest' test`.
Desde frontend: `npm.cmd test -- --watch=false`.
Selección: `npm.cmd test -- --watch=false --include=src/app/pages/catalogoPublico/catalogoPublico.spec.ts`.

H2 de pruebas se configura en [application.properties](../src/test/resources/application.properties). No sustituye la validación de PostgreSQL ni de las migraciones reales.

## Evidencia registrada

En los cambios funcionales del 15 de septiembre se registraron 9 casos Maven de relaciones/proveedores y 3 casos Vitest del catálogo correctos, compilación Angular de desarrollo, lectura de catálogo/imágenes por empresas 1/3 y rechazo 403 a empresa 2 sin relación. Otros cambios registran sus propias selecciones.

Son ejecuciones acotadas. La consolidación documental no vuelve a ejecutar todos los tests ni demuestra pedidos reales, concurrencia, navegación completa, despliegue de producción o integración externa.

## Qué comprobar por cambio

Separar compilación, unidad, integración, HTTP y recorrido de usuario. Registrar comando, resultado y límites en CAMBIOS. Para permisos, incluir caso permitido/denegado y ámbito empresarial. Para relaciones, incluir inversa y fallo sin escrituras parciales. No crear un pedido en datos compartidos solo para comprobar una URL.

El control documental se ejecuta desde raíz con `node docs/verificar-documentacion.mjs`; verifica documentación, no negocio. [Limitaciones](LIMITACIONES.md).
