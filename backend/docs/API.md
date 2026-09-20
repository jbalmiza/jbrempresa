# Contrato de la API

Revisión: 2026-09-15. Fuente: controladores y DTO actuales.

## Referencia completa

El [inventario REST generado](API_INVENTARIO.md) incluye cada método declarado, ruta, tipo de salida y firma de entrada, con enlace a su controlador. Regenerar cuando cambien las fuentes. El [modelo](MODELO_DATOS.md) explica la persistencia; las anotaciones de cada DTO indican validación.

Base local: http://localhost:8080. JSON por defecto; multipart para adjuntos, binario para imágenes/QR y formulario para Twilio. Fechas de petición: ISO según DTO y anotaciones. No inferir el método HTTP de una acción de pantalla: baja puede usar POST, PUT o PATCH según recurso.

## Autenticación, contexto y errores

Enviar Authorization: Bearer <jwt> en rutas protegidas. El Administrador puede añadir X-Empresa-Seleccionada; los demás permanecen en la empresa de su identidad. Los endpoints globales y catálogo de proveedores tienen excepciones explícitas: [matriz](SEGURIDAD.md).

Login, recuperación de contraseña, catálogo público y webhooks son públicos. El resto requiere sesión, más las verificaciones específicas de la operación.

| Código | Interpretación habitual |
|---|---|
| 400 | Entrada o regla funcional inválida; también IllegalArgumentException de varios servicios. |
| 401 | JWT inválido o caducado. |
| 403 | Falta de acceso, perfil o relación; también puede ser rechazo sin sesión. |
| 404 | Recurso/enlace no encontrado en el ámbito. |
| 409 | Conflicto de estado o disponibilidad donde se usa ResponseStatusException. |

No todos los rechazos tienen el mismo código ni cuerpo. Spring Security puede responder antes del manejador; los consumidores deben tolerar cuerpo vacío y usar el servicio común de errores. Las respuestas concretas se definen en controlador/servicio y GlobalExceptionHandler.

## Familias funcionales

| Familia | Bases | Referencia funcional |
|---|---|---|
| Acceso | /usuarios, /auth/password | Gestión de usuarios, seguridad. |
| Administración | /empresas, /perfiles, /parametros, /areas-organizativas, /personal-area, /configuraciones-tabla | Modelo y configuración. |
| Módulos | /modulos-aplicacion | [Módulos](MODULOS_APLICACION.md). |
| Relaciones | /empresas-relaciones | [Contrato de relaciones](RELACIONES_EMPRESA.md). |
| Personas y territorio | /personas, /personas-complementos, /territorio, /domicilio | Modelo, referencias y versionado. |
| Productos y servicios | /productos, /servicios, /tipos-articulo | Catálogo y tipos. |
| Ventas | /documentos-venta | PRE/PED/ALB/FAC, detalles, movimientos y cadena. |
| Compras | /compras | Contrato con segmento empId; divergencia frontend LIM-01. |
| Recursos y agenda | /recursos, /agenda, /empleados/mi-agenda | Recursos, horarios, reservas y tareas. `GET/POST /agenda/recursos` incluye `ragIntVis` (5, 10, 15, 30 o 60 minutos) por agenda; valor inicial 30. |
| Caja | /caja | Cajas, apertura/cierre, movimientos y anulaciones. |
| Comunicación externa | /comunicaciones, /webhooks/meta/whatsapp, /webhooks/twilio/whatsapp | [Integraciones](INTEGRACIONES.md). |
| Comunicación interna | /mensajeria-interna | [Mensajes](MENSAJERIA_INTERNA.md). |
| Avisos | /avisos-alertas | [Avisos](AVISOS_ALERTAS.md). |
| Catálogo | /catalogo/gestion, /catalogo/publico, /catalogo/proveedores | [Catálogo](CATALOGO.md). |
| Soporte visual | /mallas, /adjuntos | Malla, contenido y adjunto principal. |

Esta tabla agrupa familias; para rutas completas, nombres exactos y todas las operaciones consultar el inventario enlazado.

## Contratos de uso frecuente

### Login

POST /usuarios/login con JSON `{"usuUsu":"<usuario>","usuCon":"<contraseña>"}`. LoginResponse proporciona token, usuario, perfil y empresa cuando hay acceso. El controlador puede devolver cuerpo nulo ante credenciales incorrectas; no interpretar HTTP exitoso sin token como autenticación válida. `GET /usuarios/actividad` requiere JWT y es la única petición que puede devolver `X-Refresh-Token`; las consultas automáticas no prolongan la sesión.

### Relación entre empresas

POST /empresas-relaciones, empresa compradora en contexto:

```json
{"empresaRelacionadaId":4,"tipo":"PROVEEDOR","fechaInicio":"2026-09-15","fechaFin":null,"observaciones":""}
```

El 4 es un ejemplo de datos de desarrollo, no un identificador fijo. [Contrato y salida](RELACIONES_EMPRESA.md).

### Pedido desde Proveedores

GET /catalogo/proveedores lista opciones id/nombre. GET /catalogo/proveedores/{id} devuelve CatalogoPublico. POST /catalogo/proveedores/{id}/pedidos recibe:

```json
{"nombre":"Empresa compradora","telefono":"600000000","correo":"","modalidad":"DOMICILIO","direccionEnvio":"Dirección de entrega","observaciones":"","lineas":[{"tipo":"PRODUCTO","productoId":1,"cantidad":1,"observaciones":""}]}
```

La cantidad debe ser positiva y la lista no vacía. El proveedor, precios, stock y permisos se resuelven en servidor. PedidoConfirmacion devuelve numero, total, modalidad y ubicacion. No enviar esta muestra sin querer crear un pedido: su escritura afecta stock y puede generar factura/tareas.

### Compras: contrato real

POST/GET /compras/{empId}, PUT/DELETE /compras/{empId}/{id} y GET /compras/siguiente-id. El controller actual no ofrece baja/histórico/deshacer. El servicio Angular requiere alineación antes de considerar validado ese recorrido.

### Pedidos del empleado

Para el perfil Empleado, `GET /documentos-venta/PED` devuelve únicamente los pedidos de su empresa cuyo movimiento `ALTA` registró su usuario. La titularidad no cambia si otra persona modifica el pedido. Puede ejecutar `PUT /documentos-venta/PED/{id}`, `DELETE /documentos-venta/PED/{id}` y `DELETE /documentos-venta/PED/{id}/completo` solo sobre pedidos propios. La modificación respeta la propagación y clave configurada; la eliminación completa afecta a la cadena asociada. `GET /documentos-venta/{tipo}` para otros tipos, la creación directa, las bajas, conversiones e históricos documentales devuelven 403.

## Alcance de la documentación

Una firma Java no garantiza compatibilidad de todos los consumidores ni autorización exhaustiva. [Limitaciones](LIMITACIONES.md) registra diferencias conocidas. El inventario se comprueba automáticamente y los flujos se verifican mediante [pruebas](PRUEBAS.md).
