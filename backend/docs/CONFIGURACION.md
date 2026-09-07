# Configuración y arranque

## PostgreSQL local confirmado

- Host: `localhost`.
- Puerto: `5432`.
- Base de datos: `saasdb`.
- Usuario: `postgres`.
- Servicio detectado: PostgreSQL 18.
- La conexión local fue comprobada el 31 de agosto de 2026.

La contraseña no se almacena en el repositorio. Debe proporcionarse mediante `DB_PASSWORD` en el entorno local.

## Variables obligatorias

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/saasdb'
$env:DB_USERNAME = 'postgres'
$env:DB_PASSWORD = '<contraseña-local>'
$env:JWT_SECRET = '<clave-aleatoria-de-al-menos-32-caracteres>'
$env:BANK_DATA_ENCRYPTION_KEY = '<clave-distinta-de-al-menos-32-caracteres>'
```

Opcionalmente puede definirse `CORE_DATA_ENCRYPTION_KEY`. Si no existe, se utiliza `BANK_DATA_ENCRYPTION_KEY`.

No deben incluirse valores reales de contraseñas o claves en archivos versionados, capturas o registros.

El inventario local de credenciales puede mantenerse en `backend/docs/SECRETOS.md`. Este archivo está excluido expresamente mediante `.gitignore`; debe permanecer fuera de Git y no sustituye un gestor de secretos con copia de seguridad.

## Bizum mediante Redsys

La preparación se configura por empresa mediante parámetros del módulo `VENTAS`:

- `PAGO_BIZUM_ACTIVO`: activa el medio de pago; debe permanecer en `false` hasta completar la integración real.
- `PAGO_BIZUM_PASARELA`: `REDSYS`.
- `PAGO_BIZUM_ENTORNO`: `PRUEBAS` o `PRODUCCION`.
- `PAGO_BIZUM_COMERCIO`: código de comercio o FUC entregado por el banco.
- `PAGO_BIZUM_TERMINAL`: terminal Redsys habilitado para Bizum.
- `PAGO_BIZUM_CLAVE`: clave secreta; se almacena cifrada y la API nunca la devuelve en claro.
- `PAGO_BIZUM_BANCO`: banco contratante, con uso informativo.
- `PAGO_BIZUM_OBLIGATORIO`: determina en el futuro si un pedido debe pagarse antes de confirmarse.

La base local contiene estos parámetros desactivados para todas las empresas de prueba. No se deben introducir credenciales de producción hasta implementar y verificar la firma, la notificación asíncrona y las URL HTTPS de retorno de Redsys.

## Arranque

Desde la misma terminal en la que se definieron las variables:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

El backend escucha en `http://localhost:8080`. Hibernate utiliza `ddl-auto=update` y aplica las columnas y tablas nuevas al arrancar.

Productos y Servicios configuran `RUTA_IMAGENES` en sus propios módulos. La imagen de empresa utiliza `RUTA_IMAGENES` de `ADMINISTRACION`. Los adjuntos conservan sus rutas documentales específicas. La imagen principal se elige desde Adjuntos y el backend la copia a la ruta de imágenes correspondiente.

## Red local y CORS

El entorno de desarrollo acepta por defecto Angular en `localhost`, `127.0.0.1` y rangos privados habituales del puerto `4200`. En despliegue debe configurarse `CORS_ALLOWED_ORIGIN` con una lista separada por comas.

## Pruebas

Las pruebas utilizan H2 temporal en modo PostgreSQL mediante `src/test/resources/application.properties`. No acceden ni modifican `saasdb`.

```powershell
.\mvnw.cmd test
```

## Imágenes por módulo

Cada empresa configura `RUTA_IMAGENES` independientemente en `PRODUCTOS`, `SERVICIOS` y `ADMINISTRACION`. Bajo cada raíz se utiliza `empresa-{id}/productos`, `empresa-{id}/servicios` o `empresa-{id}/empresas`.

La base local utiliza `C:\Workspace\proyectos\jbrempresa\data\imagenes` para Productos y Servicios. Las imágenes predeterminadas fuente están en `src/main/resources/default-images`.
