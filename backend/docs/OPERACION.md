# Arranque, operación y mantenimiento

Revisión: 2026-09-15. Fuentes: [pom.xml](../pom.xml), [application.properties](../src/main/resources/application.properties), [package.json](../../frontend/package.json).

## Requisitos

JDK 21, Node.js/npm compatibles con el package-lock del frontend, PostgreSQL y permisos sobre los directorios de datos. El wrapper Maven está incluido. El entorno local documentado usa PostgreSQL en localhost:5432 y base saasdb. Otros entornos suministran su propia DB_URL.

## Secretos y configuración

Antes de arrancar, proporcionar desde el entorno/gestor de secretos:
DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET y BANK_DATA_ENCRYPTION_KEY.
CORE_DATA_ENCRYPTION_KEY es opcional y hereda BANK_DATA_ENCRYPTION_KEY. No basta con definir CORE_DATA_ENCRYPTION_KEY: application.properties también requiere BANK_DATA_ENCRYPTION_KEY.

JWT_SECRET requiere al menos 32 bytes UTF-8. Las claves de cifrado deben conservarse para poder leer datos previamente cifrados. Requisitos completos y parámetros en [CONFIGURACION.md](CONFIGURACION.md). No copiar secretos a archivos versionados.

## Backend: primera terminal

Desde la raíz del repositorio, con las variables ya cargadas:

```powershell
$requeridas = 'DB_URL','DB_USERNAME','DB_PASSWORD','JWT_SECRET','BANK_DATA_ENCRYPTION_KEY'
foreach ($nombreVariable in $requeridas) {
  if (![Environment]::GetEnvironmentVariable($nombreVariable)) {
    throw "Falta la variable $nombreVariable"
  }
}
Set-Location backend
.\mvnw.cmd spring-boot:run
```

Esperar “Started BackendApplication” y Tomcat en 8080. Un fallo de esquema, conexión o clave impide dar el arranque por terminado.

## Frontend: segunda terminal

Desde la raíz:

```powershell
Set-Location frontend
npm.cmd ci
npm.cmd start
```

Abrir http://localhost:4200/accesoLogin. Para probar en la red local, arrancar con `npm.cmd start -- --host 0.0.0.0 --port 4200` y usar la dirección del equipo servidor. Los dos procesos deben seguir ejecutándose.

## Verificaciones independientes

- GET http://localhost:4200/accesoLogin debe servir la interfaz (HTTP 200).
- Una petición sin JWT a /mensajeria-interna/no-leidos normalmente devuelve 403: demuestra respuesta del backend, no prueba un flujo autenticado.
- Iniciar sesión y realizar una consulta autorizada verifica autenticación y acceso funcional.
- No sustituir la comprobación del frontend por la del puerto 8080.
- Para reiniciar, detener únicamente el proceso identificado como servidor del proyecto; no cerrar todos los procesos Java o Node del equipo.

## Compilación y pruebas

Backend: ` .\mvnw.cmd test` y ` .\mvnw.cmd package`, desde backend.
Frontend: `npm.cmd run build -- --configuration development` y `npm.cmd test -- --watch=false`, desde frontend.
La compilación predeterminada de producción aplica sus propios límites de tamaño; un build de desarrollo correcto no acredita un build de producción. Detalle en [PRUEBAS.md](PRUEBAS.md).

## Esquema y datos iniciales

Hibernate usa ddl-auto=update. Hay SQL en database/migrations y clases CommandLineRunner en core/config; no existe Flyway/Liquibase que registre automáticamente todos los scripts. No ejecutar indiscriminadamente todo el directorio sobre una base existente. Revisar cada migración, copia previa, dependencias e idempotencia.

Los inicializadores de perfiles, módulos, imágenes, parámetros y estados tienen reglas propias. Véase LIM-03 sobre relaciones y [modelo](MODELO_DATOS.md). Los datos continúan siendo de prueba.

## Diagnóstico y recuperación

| Síntoma | Comprobación |
|---|---|
| 4200 no responde | Proceso npm/Angular, compilación, puerto y terminal. |
| 8080 no responde | Log de arranque, PostgreSQL, variables y puerto. |
| 401 | Token inválido o expirado; volver a iniciar sesión. |
| 403 | Autenticación/autorización y relación empresarial; no atribuirlo automáticamente a CORS. |
| Imagen ausente | Adjuntos, imagen principal, RUTA_IMAGENES, origen TIPO/REGISTRO y permisos. |
| Proveedores vacío | Empresa seleccionada, relación PROVEEDOR activa y dentro de fechas. |
| Catálogo rechazado | Publicación, token si público y permiso/relación si protegido. |
| Fallo webhook | Firma, URL pública, proveedor y parámetros de empresa. |

Las copias incluyen PostgreSQL y directorios de adjuntos/imágenes. Restaurar ambos de forma consistente en un entorno aislado, conservando las claves de cifrado. El repositorio no contiene un procedimiento automatizado de backup/restauración certificado. No hay Actuator configurado; logs y pruebas locales no sustituyen monitorización de producción.
