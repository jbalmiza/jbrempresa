# Convenciones Angular observadas

## Estructura

[CONFIRMADO] La aplicación usa componentes standalone. Cada componente se organiza normalmente como `nombre.ts`, `nombre.html` y `nombre.css`, bajo `pages/` o `components/`. Los contratos se encuentran en `interfaces/` y los servicios en `services/`.

## Nombres

[CONFIRMADO] Las clases usan PascalCase (`ProductoService`, `SelectorMapa`); ficheros y carpetas usan nombres camelCase o con sufijos (`producto.service.ts`, `producto.interface.ts`). Los selectores son camelCase, por ejemplo `selectorMapa` y `tablaEdicion`.

## Formularios y estado

[CONFIRMADO] Los formularios son template-driven: importan `FormsModule` y emplean `[(ngModel)]`. Las páginas CRUD conservan estado local como `vistaActiva`, `modoFormulario`, `datos`, la selección y el objeto editado. Las validaciones son métodos locales que muestran `alert`.

## HTTP

[CONFIRMADO] Cada recurso tiene un servicio inyectable global que utiliza `HttpClient`. La URL base se centraliza en `config/api-url.config.ts` mediante `API_URL` (por defecto, `http://localhost:8080` para desarrollo); los servicios forman sus rutas a partir de ella. Los métodos devuelven `Observable` y las páginas se suscriben directamente.

[CONFIRMADO] La pantalla de Usuarios usa una consulta remota paginada: no carga registros al abrirse; `Consultar` solicita una página de hasta 100 registros. Los filtros de la primera fila se envían al servidor al pulsar Intro y el paginador solicita únicamente la página necesaria.

## JWT y sesión

[CONFIRMADO] `app.config.ts` registra `provideHttpClient(withInterceptors([jwtInterceptor]))`. El interceptor lee `token` de `localStorage` y agrega la cabecera Bearer. Login guarda token, IDs y nombres en `localStorage`.

[CONFIRMADO] Login, `Sidebar` y `AccesoModulos` usan las claves `usuario` y `perfil` de `localStorage` para los datos visibles de sesión.

[CONFIRMADO] Los cierres de sesión de `Sidebar`, `AccesoModulos` y `Supbar` eliminan la sesión completa con `localStorage.clear()` antes de navegar al acceso.

## Navegación

[CONFIRMADO] `app.routes.ts` importa componentes de forma directa. Las rutas de acceso son públicas y las rutas de módulos/páginas usan `authGuard`, que comprueba la existencia de `token` antes de permitir la navegación. `Router` se inyecta en páginas y navegación reutilizable.

## Tablas, mapas y mallas

[CONFIRMADO] `Tabla` es el componente de listado estándar. Los selectores, mapas y mallas se configuran mediante Inputs/Outputs y se reutilizan en páginas especializadas. `PdfService` exporta listados filtrados.

## Comentarios y formato

[CONFIRMADO] Hay comentarios pedagógicos y abundantes, con mezclas de tabulaciones/espacios y estilos de importación. Mantener el estilo local del archivo modificado.

## Trazabilidad

- `app.config.ts`, `app.routes.ts`, `interceptors/jwt.interceptor.ts`
- `components/tabla/tabla.ts`, `components/selectorBusqueda/selectorBusqueda.ts`
- `pages/mProductos/productos/productos.ts`, `pages/mVentas/documentosVenta/documentosVenta.ts`
