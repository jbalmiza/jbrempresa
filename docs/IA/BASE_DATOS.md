# Base de datos deducible desde el código

## Tecnología y gestión

[CONFIRMADO] La aplicación configura PostgreSQL mediante JDBC y Spring JPA/Hibernate. `spring.jpa.hibernate.ddl-auto=update` está definido en `application.properties`.

[CONFIRMADO] La conexión se configura mediante las variables de entorno obligatorias `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`; las credenciales no se incluyen en el repositorio.

### Configuración local de PostgreSQL

En PowerShell, antes de arrancar el backend:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/saasdb'
$env:DB_USERNAME = 'postgres'
$env:DB_PASSWORD = 'tu-contraseña-local'
```

Estas variables deben configurarse también en el entorno de despliegue y no deben incluirse sus valores reales en archivos versionados.

## Tablas conocidas

| Entidad | Tabla | ID |
|---|---|---|
| Cliente | `clientes` | `emp_id` |
| Usuario | `usuarios` | `usu_id` |
| Perfil | `perfiles` | `per_id` |
| Persona | `personas` | `per_id` |
| Domicilio | `domicilios` | `dom_id` |
| Producto | `productos` | `pro_id` |
| Venta | `ventas` | `ven_id` |
| DocumentoVentaDetalle | `documentos_venta_detalle` | `dvd_id` |
| NumeradorDocumentoVenta | `documentos_venta_numeradores` | `emp_id`, `dov_tip` |
| RecursoAgendable | `recursos_agendables` | `rag_id` |
| HorarioRecurso | `horarios_recurso` | `hor_id` |
| ExcepcionRecurso | `excepciones_recurso` | `exr_id` |
| Reserva | `reservas` | `res_id` |
| ReservaRecurso | `reserva_recursos` | `rer_id` |
| TareaReserva | `tareas_reserva` | `tar_id` |
| ReprogramacionReserva | `reprogramaciones_reserva` | `rpr_id` |
| Compra | `compras` | `com_id` |
| Malla | `mallas` | `mal_id` |

## Convenciones observables

[CONFIRMADO] Las tablas son nombres plurales en snake_case. Las columnas conservan un prefijo funcional y usan snake_case: `pro_pre_ven`, `ven_fec_mov`, `dom_co_x`. La mayoría de entidades de negocio incluye `emp_id`, un campo de activo (`*_act`), usuario de modificación (`*_usu_mov`) y fecha de modificación (`*_fec_mov`).

[DEDUCIDO] `emp_id` representa la separación lógica por empresa/cliente, dado que se propaga por entidades, JWT, repositorios y controladores.

[CONFIRMADO] Los registros de `clientes` se aprovisionan fuera de la API HTTP. Cada usuario consulta únicamente el cliente asociado a su `emp_id` autenticado.

[CONFIRMADO] La columna `usuarios.usu_usu` es única globalmente, ya que se utiliza como subject del JWT para recuperar al usuario autenticado.

[CONFIRMADO] Los identificadores definitivos se asignan por PostgreSQL mediante `GenerationType.IDENTITY`, incluida la tabla `mallas`. Los cálculos de siguiente ID se conservan solo como ayuda visual.

## Relaciones observables por IDs

- Usuario contiene `emp_id` y `per_id`.
- Perfil contiene `emp_id`.
- Persona contiene `emp_id` y `dom_id`.
- Producto contiene `emp_id` y coordenadas de malla.
- Venta contiene `emp_id`, `per_id_ven` y `per_id_com`.
- Compra contiene `emp_id`, `per_id_com`, `per_id_ven` y `pro_id`.
- DocumentoVentaDetalle contiene `dov_id` y admite líneas de producto (`pro_id`) o servicio (`ser_id`).
- Malla contiene `emp_id` y referencia polimórfica `mal_ref_id` junto con `mal_ent`.

[CONFIRMADO] Las relaciones activas Persona-Domicilio y Venta-Persona se comprueban en backend con el mismo `emp_id` antes de persistirse.

[PENDIENTE DE DEFINIR] El código no muestra restricciones físicas, índices, claves foráneas ni scripts de migración; no deben inventarse.

## Trazabilidad

- `backend/src/main/resources/application.properties`
- `backend/src/main/java/com/jbrempresa/backend/entity/`

## Organización interna (actualización 2026-08-05)

| Entidad | Tabla | ID |
|---|---|---|
| ÁreaOrganizativa | `areas_organizativas` | `are_id` |
| PersonalArea | `personal_area` | `pea_id` |

- `areas_organizativas.are_id_pad` permite una jerarquía opcional dentro del mismo cliente.
- `personal_area` relaciona Persona y Área, y registra cargo, responsable, pertenencia principal, vigencia y movimiento.
- `usuarios.usu_per_id` identifica obligatoriamente a la Persona; `usuarios.per_id` continúa identificando el Perfil.
- Existe unicidad por cliente para código de área, asignación activa Persona-Área, área principal activa y relación Usuario-Persona.
- La migración `20260805_organizacion_interna_core.sql` crea la estructura, enlaza usuarios existentes y crea Personas provisionales cuando faltaba la Persona requerida.

## Catálogos territoriales (actualización 2026-08-06)

- Jerarquía normalizada y separada por cliente: `paises → provincias → municipios → codigos_postales → vias`.
- Todas las tablas incorporan cliente, usuario de movimiento, fecha de movimiento y activo.
- Los códigos son únicos dentro de cada cliente y nivel territorial.
- Las eliminaciones se bloquean cuando existen registros dependientes.
- `domicilios.dom_via_id` relaciona el domicilio con la vía. Los campos textuales de dirección se conservan como fotografía legible de cada versión histórica.
- La migración aplicada es `20260806_catalogos_territoriales.sql`.

## Comunicaciones omnicanal (actualización 2026-08-06)

- `comunicaciones`: cabecera, tipo, origen, canal, estado, asunto, Persona y asignación.
- `mensajes`: detalle secuencial de una comunicación, con dirección, autor, contenido y estado.
- Un mensaje recibido puede tener `com_id` y `men_sec` vacíos mientras permanezca pendiente en Bandeja. Al vincularse recibe conversación y secuencia.
- El mensaje conserva asunto, identificación comunicada/detectada, Persona, confianza y fechas de recepción y procesamiento.
- `contactos_canal`: identidad externa por canal. No almacena un indicador redundante de verificación.
- `personas_contactos_canal`: relación multivaluada y confirmada entre Personas e identidades; sustituye la antigua relación directa.
- Una identidad se considera identificada cuando posee al menos una relación activa en `personas_contactos_canal`.
- `mensajes.coc_id` conserva la identidad concreta desde la que se recibió el mensaje.
- La migración aplicada es `20260806_comunicaciones_base.sql`.
- La limpieza de compatibilidades se aplica mediante `20260806_limpieza_compatibilidades.sql`.

## Preferencias de tablas por usuario (actualización 2026-08-07)

- `configuraciones_tabla` guarda el orden y la visibilidad de columnas por cliente, usuario y clave de tabla.
- Incluye usuario de movimiento, fecha de movimiento y activo conforme a la norma transversal.
- La restricción única `(emp_id, usu_id, cot_cla)` evita configuraciones duplicadas.
- La migración aplicada es `20260807_configuraciones_tabla.sql`.

## Datos complementarios de Personas (actualización 2026-08-07)

- `representantes` relaciona una Persona representada con otra Persona representante, tipo, vigencia, principal y observaciones.
- `domicilios_notificacion` relaciona una Persona con un Domicilio existente, tipo, vigencia, principal y observaciones.
- `domiciliaciones_bancarias` registra operaciones de `CARGO` o `ABONO`, titular, IBAN cifrado, BIC, mandato, principal y estado.
- Las eliminaciones son bajas lógicas y las tres tablas incorporan cliente, usuario/fecha de movimiento y activo.
- Solo existe un representante y domicilio de notificación principal por Persona. En domiciliaciones puede existir uno principal por Persona y tipo de operación.
- La migración aplicada es `20260807_personas_complementos.sql`.
