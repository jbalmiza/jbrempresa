# Arquitectura actual

## Visión general

[CONFIRMADO] El repositorio contiene un backend Spring Boot y un frontend Angular. Ambos se ejecutan como proyectos separados y se comunican por HTTP.

```text
[ Angular standalone ]
   pages -> services -> HttpClient + JWT
                 |
                 v
[ Spring Boot ]
   controllers -> repositories -> JPA/Hibernate
        |              ^
        v              |
    security/JWT    PostgreSQL
```

## Árbol simplificado

```text
backend/
  src/main/java/com/jbrempresa/backend/
    controller/ entity/ repository/ service/ security/ dto/
  src/main/resources/application.properties
frontend/
  src/app/
    pages/ components/ services/ interfaces/ directives/
    app.config.ts app.routes.ts interceptors/
```

## Backend

[CONFIRMADO] `BackendApplication` arranca Spring Boot. La configuración declara PostgreSQL, JPA/Hibernate y `ddl-auto=update`. Los controladores REST reciben los cuerpos JSON y persisten entidades mediante repositorios JPA. Solo Productos y Mallas tienen una capa de servicio con lógica de integración entre ambas entidades.

[CONFIRMADO] La conexión PostgreSQL se suministra externamente mediante `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`.

[CONFIRMADO] Las excepciones de controladores REST se convierten en respuestas JSON homogéneas mediante `GlobalExceptionHandler`.

## Frontend

[CONFIRMADO] `main.ts` llama a `bootstrapApplication(App, appConfig)`. La aplicación no usa `NgModule`: las páginas y componentes son standalone. `app.routes.ts` registra rutas planas para acceso, módulos y áreas funcionales.

## Flujo de autenticación

```text
AccesoLogin -> UsuarioService.login -> POST /usuarios/login
  -> UsuarioRepository.findByUsuUsu + PasswordEncoder.matches
  -> JwtService.generarToken
  -> LoginResponse
  -> localStorage
  -> jwtInterceptor añade Bearer token
  -> JwtFilter -> JwtUser en SecurityContext
```

[CONFIRMADO] El filtro recarga el usuario desde base de datos por nombre de usuario antes de construir el `JwtUser` autenticado. Los tokens vencen a los 30 minutos desde su creación. Cuando quedan cinco minutos o menos, el filtro entrega un nuevo token mediante `X-Refresh-Token` y el interceptor Angular lo almacena; así la sesión se prolonga mientras haya actividad autenticada. Ante un 401, el interceptor limpia la sesión, avisa al usuario y redirige al acceso.

[CONFIRMADO] La clave de firma JWT no pertenece al repositorio: `JwtService` la recibe mediante la variable de entorno `JWT_SECRET` al arrancar el backend.

## Multiempresa

[CONFIRMADO] Las entidades de negocio incluyen `empId`. Domicilios, perfiles, personas, productos, usuarios, ventas y mallas obtienen el cliente desde el principal `JwtUser` y usan repositorios con `empId` para la mayoría de operaciones.

[CONFIRMADO] Clientes se consulta exclusivamente desde el `empId` del `JwtUser`; el endpoint mantiene una lista de un solo elemento para conservar el contrato actual de Angular. Las altas, modificaciones y bajas de clientes no se exponen por HTTP.

[REVISAR] Compras usa `empId` en la URL; ver `PATRONES.md` y `SEGURIDAD.md`.

## Navegación y presentación

[CONFIRMADO] Las rutas conducen a pantallas de módulos y páginas de registro. Las páginas principales incorporan `Sidebar` y `Supbar`. Las páginas de datos suelen alternar una tabla y un formulario; Productos añade vistas de malla y Territorio añade mapas.

[CONFIRMADO] Angular aplica `authGuard` a las rutas que requieren sesión; la ausencia de token redirige al acceso antes de construir la página.

## Trazabilidad

- `backend/src/main/java/com/jbrempresa/backend/BackendApplication.java`
- `backend/src/main/resources/application.properties`
- `frontend/src/main.ts`
- `frontend/src/app/app.config.ts`
- `frontend/src/app/app.routes.ts`

## GreenSaaS Core (actualización 2026-08-05)

[CONFIRMADO] Se ha iniciado el núcleo transversal con una implementación pequeña y utilizada realmente:

- Backend: `core/context/ContextoOperacion` centraliza usuario autenticado, cliente, nombre de usuario y fecha del servidor.
- Frontend: `core/session/ContextoSesionService` centraliza la lectura y limpieza del contexto de sesión.
- Las utilidades sin responsabilidad de núcleo, como `FechasUtil`, residen en `shared/utils`.

[DECISIÓN] El núcleo crecerá solo ante responsabilidades transversales comprobadas. Auditoría, históricos, permisos, parámetros y adjuntos deberán extraerse mediante contratos comunes antes de continuar duplicando lógica por módulo.
