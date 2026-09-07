# API REST

## Convenciones

- URL local: `http://localhost:8080`.
- JSON salvo cargas multipart, webhooks form-urlencoded e imagenes binarias.
- Rutas protegidas: `Authorization: Bearer <jwt>`.
- Identificadores y resultados quedan limitados a la empresa autenticada.
- `POST` crea, `PUT` reemplaza datos editables, `PATCH` ejecuta cambios parciales y `DELETE` elimina o da de baja segun el recurso.
- Los CRUD con auditoria suelen exponer `siguiente-id`, `baja`, `historico` y `deshacer`.

## Acceso publico

| Base | Operaciones | Finalidad |
|---|---|---|
| `/usuarios/login` | `POST` | Autenticar y emitir JWT. |
| `/auth/password` | `POST /solicitar`, `POST /confirmar` | Recuperar contrasena mediante token temporal. |
| `/catalogo/publico/{token}` | `GET` | Obtener empresa, posicion, grupos y articulos visibles. |
| `/catalogo/publico/{token}/pedidos` | `POST` | Validar cesta y crear pedido. |
| `/catalogo/publico/{token}/{productos|servicios}/{id}/imagen` | `GET` | Servir imagen principal del articulo. |
| `/catalogo/publico/{token}/empresa/imagen` | `GET` | Servir imagen principal de empresa. |
| `/webhooks/meta/whatsapp` | `GET`, `POST` | Verificacion y eventos de Meta WhatsApp. |
| `/webhooks/twilio/whatsapp` | `POST` | Mensajes entrantes de Twilio. |

Todas las demas rutas requieren JWT.

## Administracion

| Base | Operaciones principales |
|---|---|
| `/empresas` | consulta, actualizacion e imagen de empresa. |
| `/usuarios` | alta, actualizacion, consulta paginada, siguiente id, baja y login. |
| `/perfiles` | alta, actualizacion, consulta, siguiente id y eliminacion. |
| `/parametros` | consulta, alta, actualizacion y eliminacion por modulo/empresa. |
| `/areas-organizativas` | CRUD de areas con validacion de dependencias. |
| `/personal-area` | CRUD de asignaciones de personal. |
| `/configuraciones-tabla` | lectura y guardado de preferencias de tabla del usuario. |

## Maestros y operaciones

| Base | Operaciones principales |
|---|---|
| `/personas` | CRUD, selector, siguiente id, baja, historico y deshacer. |
| `/personas-complementos` | representantes, domicilios de notificacion y domiciliaciones bancarias. |
| `/territorio` | CRUD de paises, provincias, municipios, codigos postales y vias. |
| `/domicilio` | CRUD, siguiente id, baja, historico y deshacer. |
| `/productos` | CRUD, siguiente id, baja, historico, deshacer y contenido de imagen. |
| `/servicios` | CRUD, siguiente id, baja, historico, deshacer y contenido de imagen. |
| `/compras` | CRUD documental, baja, historico y deshacer. |

## Ventas

`/documentos-venta` concentra presupuestos, pedidos, albaranes y facturas. Permite consultar por tipo, crear/actualizar, gestionar detalle, registrar movimientos, dar de baja, consultar historico, deshacer y generar el siguiente documento de la cadena. Los DTO de `dto.ventas` son el contrato y evitan exponer directamente toda la entidad.

La posicion de malla solo se edita en pedido, pero queda copiada en albaran y factura. El backend no trata la posicion como una mesa. Los importes del documento se calculan a partir de sus lineas segun el flujo que lo crea.

## Malla y catalogo gestionado

| Ruta | Operaciones |
|---|---|
| `/mallas` | alta, actualizacion, consulta, siguiente id, pintar celda, borrar posicion y eliminar celda. |
| `/catalogo/gestion/configuracion` | `GET`, `PUT` de publicacion y domicilio. |
| `/catalogo/gestion/pago` | `GET` del estado enmascarado de Redsys/Bizum. No inicia pagos. |
| `/catalogo/gestion/posiciones` | listar y crear posicion publica. |
| `/catalogo/gestion/posiciones/{id}/regenerar` | renovar el token publico. |
| `/catalogo/gestion/posiciones/{id}/qr` | descargar PNG QR con ubicacion, fila y columna. |

## Comunicaciones

`/comunicaciones` ofrece bandeja de entrada, candidatos de persona, confirmacion de persona y clasificacion, creacion/vinculacion de conversaciones, consulta por persona, cambio de estado/asignacion, mensajes y contactos de canal. Los identificadores externos impiden procesar dos veces el mismo mensaje.

`/comunicaciones/{comunicacionId}/propuestas-respuesta` lista, crea, aprueba y rechaza propuestas. La aprobacion registra la decision; el envio efectivo sigue el flujo del canal.

## Agenda

`/agenda` agrupa recursos agendables, horarios semanales, excepciones, consulta de disponibilidad, reservas, recursos de reserva, tareas y reprogramaciones. `RecursoEntrada.ragTip` admite `EMPLEADO` y `DOMICILIO`; para `EMPLEADO`, `ragRefId` corresponde a `recursos_operativos.reo_id`, el nombre procede del recurso y la capacidad es uno. Las rutas concretas se definen en `AgendaController`; toda referencia se valida contra la empresa activa.

## Recursos operativos

`/recursos` ofrece consulta, alta, modificación versionada y baja lógica. `PATCH /{id}/operativo` cambia disponibilidad; `GET` y `PUT /{id}/capacidades` gestionan capacidades; `GET /tipos-capacidad` obtiene tipos activos de Productos y Servicios.

Los recursos utilizan versiones `A/M/B`. `PUT /{id}` crea una versión `M`, `POST /{id}/baja` crea la versión `B`, `GET /{id}/historico` devuelve todas las versiones y `POST /{id}/deshacer` elimina la última y reactiva la anterior. `PATCH /{id}/operativo` también crea una versión `M` para conservar trazabilidad.

`DELETE /recursos/{id}` pertenece al CRUD de Registro y solo elimina recursos sin histórico ni agenda. Los recursos ya operativos se retiran mediante Baja desde Gestión para conservar sus relaciones.

## Adjuntos

`/adjuntos` usa los parametros de consulta `modulo`, `entidad` y `registroId`. Permite listar, subir multipart, marcar `/{id}/principal`, descargar `/{id}/contenido` y eliminar. Marcar principal sincroniza el campo de imagen de la entidad compatible.

## Salud tecnica

`GET /api/hello` es una ruta autenticada de comprobacion basica. No sustituye un endpoint Actuator de salud; Actuator no esta instalado.
