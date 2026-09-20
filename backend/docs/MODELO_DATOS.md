# Modelo de datos

## Precios de productos y margen objetivo

`productos.pro_pre_com` contiene el precio de compra sin IVA; `pro_des_com` es el descuento de compra, `pro_iva_com` su tipo de IVA y `pro_tot_com` es el total de compra tras aplicar descuento y sumar IVA. `pro_pre_ven` es el precio de venta sin IVA; `pro_pre_des` es el descuento de venta, `pro_pre_iva` es el IVA de venta y `pro_pre_fin` es el total de venta con IVA tras descuento.

Todas las empresas disponen del parámetro `PRODUCTOS / PORCENTAJE_BENEFICIO`, inicialmente al 20 %. «Aplicar beneficio a venta» suma el porcentaje exclusivamente al precio de compra sin IVA: compra 10 produce venta 12. «Aplicar beneficio a compra» resta el porcentaje exclusivamente al precio de venta sin IVA: venta 10 produce compra 8. Descuentos, IVA y totales no intervienen en estos cálculos.

Revisión documental: 2026-09-15. Describe el árbol de trabajo actual.

## Distintivos comerciales de catálogo

Productos incorpora `pro_nov`, `pro_mej_pre` y `pro_out`; Servicios incorpora `ser_nov`, `ser_mej_pre` y `ser_out`. Son indicadores booleanos con valor predeterminado `false` que se conservan en cada versión histórica del registro.

## Reglas generales

- `emp_id` identifica la empresa propietaria y forma parte de la clave logica de casi todos los datos.
- Los sufijos `id`, `act`, `usu_mov`, `fec_mov`, `tip_mov` y `cau_mov` expresan identificador, activo y auditoria.
- `Persona`, `Producto`, `Servicio` y `Domicilio` usan clases de id compuesto donde corresponde.
- JPA crea o adapta tablas mediante `ddl-auto=update`; existen scripts SQL en `database/migrations`, pero no un ejecutor de migraciones versionadas integrado.

## Administracion

| Entidad/tabla | Funcion y relaciones |
|---|---|
| `Empresa` / `empresas` | Organizacion propietaria; nombre, datos fiscales, contacto e imagen. |
| `Usuario` / `usuarios` | Credenciales, perfil, empresa, estado y auditoria. |
| `Perfil` / `perfiles` | Perfil funcional del usuario dentro de empresa. |
| `Parametro` / `parametros` | Valor por empresa, modulo y codigo; combinacion unica. |
| `AreaOrganizativa` / `areas_organizativas` | Unidad organizativa. |
| `PersonalArea` / `personal_area` | Asignacion de persona/usuario a un area. |
| `ConfiguracionTabla` / `configuraciones_tabla` | Preferencia única por empresa, usuario y tabla. La visibilidad inicial oculta Id Empresa, Tipo Movimiento, Causa Movimiento e Id Histórico; el usuario puede mostrarlos posteriormente. |
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
## Convención de nombres de tipos

Los nombres de Tipos de Producto y Tipos de Servicio se registran y presentan en plural. Cuando el tipo se utiliza como código textual en Productos, Servicios o capacidades de Recursos, debe actualizarse conjuntamente en todas esas relaciones.
## Facturas y Personas

- `personas.per_dat_com`: estado calculado de completitud. En persona física exige tipo de persona, tipo y número de documento, nombre, primer apellido, domicilio y teléfono; en jurídica exige tipo de persona, tipo y número de documento, razón social corta, domicilio y teléfono. No impide que catálogo u otros procesos creen una persona con información mínima.
- `documentos_venta.dov_tip_fac`: `NORMAL` o `SIMPLIFICADA` cuando `dov_tip = 'FAC'`.
- `documentos_venta_movimientos.dov_tip_fac`: conserva el subtipo de la factura en cada movimiento.
- La coincidencia de personas externas exige que todos los contactos proporcionados coincidan. Un teléfono o correo diferente representa otra persona cliente y no se fusiona.
- Personas es el único módulo autorizado para completar o ampliar la ficha; otros procesos solo pueden darla de alta con los datos disponibles.

## Administración ampliada y caja

`EmpresaRelacion` representa los dos sentidos proveedor/cliente; conserva movimiento pero no versiones históricas. `ModuloAplicacion` y `EmpresaModulo` configuran el panel y su disponibilidad por empresa. Las conversaciones, mensajes y participantes internos son entidades distintas de Comunicaciones externas. Caja contiene cajas, sesiones y movimientos.

El [inventario generado](MODELO_INVENTARIO.md) enumera todas las entidades y campos Java actuales. No sustituye las restricciones de los servicios ni certifica que la base local haya aplicado cada SQL. Ver [limitaciones](LIMITACIONES.md), [relaciones](RELACIONES_EMPRESA.md) y [mensajería](MENSAJERIA_INTERNA.md).
# Disponibilidad de productos y componentes

`productos` declara `pro_dis_lun` a `pro_dis_dom`; los siete valores están activos por defecto. Las variantes comerciales de tamaño o precio se registran como productos distintos.

`componentes` es un maestro histórico por empresa identificado por `(emp_id, cmp_id, cmp_id_his)`. Registra tipo, nombre, descripción, precio adicional sin IVA, IVA y los 14 alérgenos de declaración obligatoria en la Unión Europea. Los tipos generales son Ingrediente, Materia prima, Pieza, Material, Envase y Accesorio. Registro contiene el CRUD del maestro y Gestión sus movimientos y baja lógica.

`productos_componentes` registra la composición de cada versión histórica de producto mediante empresa, producto, histórico, componente y cantidad. Un componente solo puede aparecer una vez en la misma versión, debe pertenecer a la empresa del producto y su cantidad debe ser mayor que cero. Al modificar el producto se conserva la composición de cada versión para que Gestión muestre el histórico correcto.
