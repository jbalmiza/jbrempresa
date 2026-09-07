# Modelo de datos

## Reglas generales

- `emp_id` identifica la empresa propietaria y forma parte de la clave logica de casi todos los datos.
- Los sufijos `id`, `act`, `usu_mov`, `fec_mov`, `tip_mov` y `cau_mov` expresan identificador, activo y auditoria.
- `Persona`, `Producto`, `Servicio` y `Domicilio` usan clases de id compuesto donde corresponde.
- JPA crea o adapta tablas mediante `ddl-auto=update`; no existe un historial versionado de DDL.

## Administracion

| Entidad/tabla | Funcion y relaciones |
|---|---|
| `Empresa` / `empresas` | Organizacion propietaria; nombre, datos fiscales, contacto e imagen. |
| `Usuario` / `usuarios` | Credenciales, perfil, empresa, estado y auditoria. |
| `Perfil` / `perfiles` | Perfil funcional del usuario dentro de empresa. |
| `Parametro` / `parametros` | Valor por empresa, modulo y codigo; combinacion unica. |
| `AreaOrganizativa` / `areas_organizativas` | Unidad organizativa. |
| `PersonalArea` / `personal_area` | Asignacion de persona/usuario a un area. |
| `ConfiguracionTabla` / `configuraciones_tabla` | Preferencia unica por empresa, usuario y tabla. |
| `RecuperacionContrasena` | Token temporal, caducidad y consumo de recuperacion. |

## Territorio y personas

`Pais -> Provincia -> Municipio -> CodigoPostal -> Via -> Domicilio` forma el catalogo territorial. Las referencias se validan antes de borrar. `Persona` es el tercero de negocio. `Representante` relaciona una persona con su representante; `DomicilioNotificacion` selecciona domicilios por finalidad; `DomiciliacionBancaria` conserva titular, IBAN cifrado, BIC, mandato, prioridad y estado.

`ContactoCanal` representa una identidad externa como telefono o correo. `PersonaContactoCanal` es la relacion revocable entre identidad y persona.

## Productos, servicios y ficheros

`Producto` contiene tipo, nombre, descripcion, precios/clasificacion, stock, visibilidad, imagen y auditoria. `Servicio` contiene tipo, nombre, descripcion, duracion, precio, visibilidad, imagen y auditoria. Ambos se agrupan en el catalogo por tipo.

`Adjunto` vincula metadatos y ruta fisica con empresa, modulo, entidad y registro. Solo un adjunto compatible debe actuar como imagen principal; al marcarlo se actualiza la referencia de imagen de la entidad.

`Malla` almacena celdas por empresa y entidad: fila, columna, color, ubicacion y referencia de registro. Una posicion negra puede ser camino. La semantica del dibujo la decide el usuario.

## Ventas y compras

`DocumentoVenta` es la cabecera comun de `PRE`, `PED`, `ALB` y `FAC`. `DocumentoVentaDetalle` contiene productos/servicios, cantidad, precio, importe y observaciones. `DocumentoVentaMovimiento` conserva el historico y la referencia entre documentos. La posicion, modalidad, ubicacion, fila, columna, direccion de envio y observaciones viven en el documento cuando aplican.

`Compra` representa el documento de compras con proveedor/persona, fechas, estado, importes, activo y auditoria.

## Catalogo

`CatalogoPosicion` pertenece a empresa y contiene ubicacion, fila, columna, token publico, activo y auditoria. Son unicas tanto `emp_id + fila + columna` como el token. El token se puede regenerar para invalidar el QR anterior.

La configuracion de publicacion y domicilio se almacena como parametros. Los pedidos publicos se persisten en las tablas normales de ventas; no hay una tabla de carrito temporal.

## Comunicaciones

- `Comunicacion`: conversacion con persona, canal, intencion, estado, asignacion y ultima actividad.
- `Mensaje`: entrada/salida/interno, contenido, canal, remitente/destinatario, clasificacion, estado e id externo.
- `PropuestaRespuesta`: texto propuesto, estado de aprobacion/rechazo y auditoria.
- `ContactoCanal` y `PersonaContactoCanal`: identidad externa y vinculacion confirmada.

## Agenda

- `RecursoAgendable`: referencia única por empresa, tipo y registro. El tipo `EMPLEADO` referencia `RecursoOperativo.reoId`; `DOMICILIO` conserva la agenda del lugar. Persona no es un recurso agendable.
- `HorarioRecurso`: disponibilidad semanal.
- `ExcepcionRecurso`: cierre o apertura excepcional por fecha/franja.
- `Reserva`: cabecera, persona, fechas, estado y auditoria.
- `ReservaRecurso`: recursos ocupados por reserva.
- `TareaReserva`: trabajo asociado.
- `ReprogramacionReserva`: trazabilidad de cambios de fecha/hora.

## Recursos operativos

- `RecursoOperativo` / `recursos_operativos`: clave histórica compuesta `reo_id + reo_id_his`, movimiento `A/M/B`, nombre, tipo, Persona, descripción, activo, operativo y auditoría. La relación con Persona se guarda mediante `per_id`.
- `RecursoCapacidad` / `recursos_capacidades`: relación única entre recurso, origen `PRODUCTO`/`SERVICIO` y tipo.

Los empleados de `RecursoOperativo` son los titulares de `RecursoAgendable`; ambos se relacionan mediante `rag_tip = EMPLEADO` y `rag_ref_id = reo_id`. La maquinaria todavía no se crea como recurso agendable.

Los únicos tipos admitidos son `EMPLEADO` y `DOMICILIO`. No se conserva un tipo de agenda de Persona ni una variante de legado: los datos de prueba incompatibles se migran al empleado correspondiente o se eliminan con sus dependencias.

## Integridad y bajas

El modelo combina restricciones declaradas (`uniqueConstraints`), validaciones de servicio y bajas logicas. No todas las relaciones tienen asociaciones JPA navegables: muchas se expresan mediante ids y repositorios. Por ello, cualquier nueva escritura debe validar existencia, empresa y activo explicitamente.
