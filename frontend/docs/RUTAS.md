# Inventario de rutas Angular

Revisión: 2026-09-15. Inventario generado desde las fuentes; no acredita pruebas funcionales.

Fuente: [app.routes.ts](../src/app/app.routes.ts). Las rutas con `authGuard` requieren sesión y la guarda aplica el perfil; no sustituyen permisos backend. Ver [guía de uso](GUIA_USO.md) y [seguridad](../../backend/docs/SEGURIDAD.md). El menú efectivo se configura además por empresa.

| Ruta | Destino declarado | Guarda declarada |
|---|---|---|
| `/catalogo/:token` | CatalogoPublico | Sin authGuard declarado |
| `/` | Redirección: accesoLogin | Sin authGuard declarado |
| `/accesoLogin` | AccesoLogin | Sin authGuard declarado |
| `/accesoModulos` | AccesoModulos | authGuard |
| `/administracion` | PrincipalAdministracion | authGuard |
| `/territorio` | PrincipalTerritorio | authGuard |
| `/personas` | PrincipalPersonas | authGuard |
| `/productos` | PrincipalProductos | authGuard |
| `/ventas` | PrincipalVentas | authGuard |
| `/compras` | PrincipalCompras | authGuard |
| `/administracion/empresas` | Empresas | authGuard |
| `/administracion/usuarios` | Usuarios | authGuard |
| `/administracion/perfiles` | Perfiles | authGuard |
| `/administracion/gestionEmpresas` | Empresas | authGuard |
| `/administracion/gestionUsuarios` | Usuarios | authGuard |
| `/administracion/relacionesEmpresa` | RelacionesEmpresa | authGuard |
| `/administracion/gestionRelacionesEmpresa` | RelacionesEmpresa | authGuard |
| `/empleados` | ModuloVacio | authGuard |
| `/empleados/agenda` | AgendaEmpleadoPage | authGuard |
| `/empleados/catalogo` | CatalogoPublico | authGuard |
| `/empleados/pedidos` | DocumentosVenta | authGuard |
| `/empleados/avisosAlertas` | AvisosAlertas | authGuard |
| `/clientes` | CatalogoPublico | authGuard |
| `/proveedores` | ModuloVacio | authGuard |
| `/proveedores/catalogo` | CatalogosProveedores | authGuard |
| `/administracion/modulos` | ModulosAplicacion | authGuard |
| `/administracion/gestionModulos` | ModulosAplicacion | authGuard |
| `/servicios` | PrincipalServicios | authGuard |
| `/comunicaciones` | PrincipalComunicaciones | authGuard |
| `/comunicaciones/bandeja` | BandejaComunicaciones | authGuard |
| `/comunicaciones/conversaciones` | BandejaComunicaciones | authGuard |
| `/comunicaciones/pendientes` | BandejaComunicaciones | authGuard |
| `/comunicaciones/contactosSinIdentificar` | ContactosSinIdentificar | authGuard |
| `/comunicaciones/avisosAlertas` | AvisosAlertas | authGuard |
| `/comunicaciones/gestionAvisosAlertas` | AvisosAlertas | authGuard |
| `/administracion/mensajes` | MensajesInternosPage | authGuard |
| `/administracion/gestionMensajes` | MensajesInternosPage | authGuard |
| `/administracion/parametros` | Parametros | authGuard |
| `/administracion/areasOrganizativas` | AreasOrganizativas | authGuard |
| `/administracion/personalArea` | PersonalAreaPage | authGuard |
| `/territorio/vias` | CatalogoTerritorialPage | authGuard |
| `/territorio/codigosPostales` | CatalogoTerritorialPage | authGuard |
| `/territorio/municipios` | CatalogoTerritorialPage | authGuard |
| `/territorio/provincias` | CatalogoTerritorialPage | authGuard |
| `/territorio/paises` | CatalogoTerritorialPage | authGuard |
| `/comunicaciones/parametros` | Parametros | authGuard |
| `/territorio/parametros` | Parametros | authGuard |
| `/personas/parametros` | Parametros | authGuard |
| `/productos/parametros` | Parametros | authGuard |
| `/servicios/parametros` | Parametros | authGuard |
| `/ventas/parametros` | Parametros | authGuard |
| `/compras/parametros` | Parametros | authGuard |
| `/territorio/domicilios` | Domicilios | authGuard |
| `/territorio/gestionDomicilios` | GestionDomicilios | authGuard |
| `/personas/personas` | Personas | authGuard |
| `/personas/gestionPersonas` | Personas | authGuard |
| `/personas/representantes` | ComplementosPersona | authGuard |
| `/personas/domiciliosNotificacion` | ComplementosPersona | authGuard |
| `/personas/domiciliacionesBancarias` | ComplementosPersona | authGuard |
| `/productos/productos` | Productos | authGuard |
| `/productos/componentes` | Componentes | authGuard |
| `/productos/gestionComponentes` | Componentes | authGuard |
| `/servicios/servicios` | Servicios | authGuard |
| `/servicios/tiposServicio` | TiposArticulo | authGuard |
| `/servicios/gestionServicios` | Servicios | authGuard |
| `/servicios/catalogo` | CatalogoGestion | authGuard |
| `/productos/gestionProductos` | GestionProductos | authGuard |
| `/ventas/ventas` | DocumentosVenta | authGuard |
| `/productos/catalogo` | CatalogoGestion | authGuard |
| `/productos/tiposProducto` | TiposArticulo | authGuard |
| `/ventas/presupuestos` | DocumentosVenta | authGuard |
| `/ventas/pedidos` | DocumentosVenta | authGuard |
| `/ventas/albaranes` | DocumentosVenta | authGuard |
| `/ventas/gestionPresupuestos` | DocumentosVenta | authGuard |
| `/ventas/gestionPedidos` | DocumentosVenta | authGuard |
| `/ventas/gestionAlbaranes` | DocumentosVenta | authGuard |
| `/ventas/gestionFacturas` | DocumentosVenta | authGuard |
| `/compras/compras` | Compras | authGuard |
| `/recursos` | PrincipalRecursos | authGuard |
| `/recursos/recursos` | Recursos | authGuard |
| `/recursos/gestionRecursos` | Recursos | authGuard |
| `/recursos/gestionAgendas` | GestionAgendas | authGuard |
| `/recursos/parametros` | Parametros | authGuard |
| `/caja` | PrincipalCaja | authGuard |
| `/caja/cajas` | Cajas | authGuard |
| `/caja/gestionCaja` | Cajas | authGuard |
| `/caja/parametros` | Parametros | authGuard |
