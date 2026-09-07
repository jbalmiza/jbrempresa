# Módulos actuales

| Módulo | Frontend | Backend | Estado observable |
|---|---|---|---|
| Acceso | `accesoLogin`, `accesoModulos` | `UsuarioController.login`, seguridad JWT | [CONFIRMADO] Implementado, con puntos de revisión de sesión. |
| Administración: clientes | `clientes` | Cliente, repositorio y controlador | [CONFIRMADO] Consulta del cliente autenticado; provisión de clientes fuera de HTTP. |
| Administración: usuarios | `usuarios` | Usuario, repositorio y controlador | [CONFIRMADO] CRUD y login presentes. |
| Administración: perfiles | `perfiles` | Perfil, repositorio y controlador | [CONFIRMADO] CRUD presente. |
| Territorio: domicilios | `domicilios`, `gestionDomicilios` | Domicilio, repositorio y controlador | [CONFIRMADO] CRUD, selector y mapa presentes. |
| Personas | `personas` | Persona, repositorio y controlador | [CONFIRMADO] CRUD presente. |
| Productos | `productos`, `gestionProductos` | Producto, ProductoService, MallaService | [CONFIRMADO] CRUD y sincronización de malla presentes. |
| Mallas | componentes de selector/visualización | Malla, repositorio, controlador, servicio | [CONFIRMADO] CRUD, pintura y borrado manual presentes. |
| Ventas | `documentos_venta`, `documentos_venta_detalle` | DocumentoVenta, detalle, repositorios y controlador | [CONFIRMADO] Modelo único para Presupuesto, Pedido, Albarán y Factura, con productos y servicios. |
| Compras | `compras` | Compra, repositorio y controlador | [REVISAR] CRUD existe pero el contrato de rutas frontend/backend no coincide. |

## Navegación de áreas

[CONFIRMADO] Existen páginas principales para Administración, Territorio, Personas, Productos, Ventas y Compras, que incorporan navegación reutilizable.

## Detalles de venta y compra

[CONFIRMADO] Las líneas comerciales se persisten en `documentos_venta_detalle` y cada conversión conserva una copia de las líneas del documento de origen.

## Trazabilidad

- `frontend/src/app/pages/`
- `backend/src/main/java/com/jbrempresa/backend/entity/`
- `backend/src/main/java/com/jbrempresa/backend/controller/`
- `backend/src/main/java/com/jbrempresa/backend/repository/`

## Organización interna (actualización 2026-08-05)

- [CONFIRMADO] `Áreas Organizativas`: CRUD jerárquico, código único y aislamiento por cliente.
- [CONFIRMADO] `Personal de Área`: relaciona Personas con Áreas y registra cargo, responsable, pertenencia principal y vigencia.
- [CONFIRMADO] En Administración, `Estructura Organizativa` contiene únicamente `Áreas Organizativas` y `Personal de Área`.
- [DECISIÓN] Toda cuenta de usuario debe estar vinculada obligatoriamente a una Persona activa del mismo cliente.
- [DECISIÓN] Una Persona es requisito previo para crear Personal de Área. Puede pertenecer a varias áreas, pero solo tener una pertenencia principal activa.
- [ACLARACIÓN] `usuarios.per_id` conserva su significado de perfil; la relación obligatoria con Persona utiliza `usuarios.usu_per_id`.

## Estructura territorial (actualización 2026-08-06)

- [CONFIRMADO] El menú contiene Domicilios, Vías, Códigos Postales, Municipios, Provincias y Países.
- [CONFIRMADO] Se retiraron Comunidades, Plantas, Escaleras, Portales, Bloques y Tipos de Vía como mantenimientos independientes.
- [CONFIRMADO] Países, Provincias, Municipios, Códigos Postales y Vías disponen de CRUD y aislamiento por cliente.
- [DECISIÓN] Los tipos de vía forman una lista cerrada: Calle, Avenida, Plaza, Paseo, Camino, Carretera, Travesía, Urbanización, Polígono y Otro.
- [DECISIÓN] Planta, Escalera, Portal y Bloque permiten vacío o los valores fijos `1–5`; no existe una opción «Sin especificar».

## Comunicaciones (actualización 2026-08-06)

- [CONFIRMADO] Comunicaciones existe como módulo principal con tarjeta, página de entrada y parámetros propios.
- [DECISIÓN] Su menú inicial se divide en Operación (Bandeja de Entrada, Conversaciones y Pendientes de Revisión), Gestión (Contactos sin Identificar) y Configuración (Canales y Parámetros).
- [DECISIÓN] Procesadas será un filtro de Bandeja, no una pantalla independiente.
- [PENDIENTE] Las opciones funcionales se activarán cuando exista el modelo omnicanal; no se crean pantallas sin comportamiento real.
# Personas: datos complementarios

- CRUD de Representantes.
- CRUD de Domicilios de Notificación.
- CRUD de Domiciliaciones Bancarias de cargo y abono con IBAN cifrado.
