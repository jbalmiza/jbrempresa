# Operacion y mantenimiento

## Requisitos

- JDK 21.
- Maven Wrapper incluido (`mvnw.cmd` en Windows).
- PostgreSQL accesible.
- Variables obligatorias de base de datos y cifrado.
- Frontend separado para la interfaz; el backend escucha normalmente en el puerto 8080.

## Arranque en PowerShell

```powershell
$env:DB_URL='jdbc:postgresql://localhost:5432/jbrempresa'
$env:DB_USERNAME='<usuario>'
$env:DB_PASSWORD='<contrasena>'
$env:CORE_DATA_ENCRYPTION_KEY='<clave-segura>'
cd backend
.\mvnw.cmd spring-boot:run
```

La configuracion concreta del entorno se detalla en [CONFIGURACION.md](CONFIGURACION.md). No guardar secretos reales en el repositorio.

## Compilacion y pruebas

```powershell
cd backend
.\mvnw.cmd test
.\mvnw.cmd package
```

La prueba actual solo comprueba que el contexto Spring carga con H2. Un resultado correcto no demuestra todos los CRUD, aislamiento multiempresa, catalogo, stock, webhooks o ventas. Cada cambio de riesgo debe añadir pruebas unitarias o de integracion del comportamiento modificado.

## Esquema de base de datos

`spring.jpa.hibernate.ddl-auto=update` modifica el esquema al arrancar. Es comodo durante el desarrollo con datos de prueba, pero no ofrece version, rollback ni revision del SQL. Antes de trabajar con datos reales debe adoptarse Flyway o Liquibase y cambiar produccion a `validate`.

Como los datos actuales son de prueba, una incompatibilidad puede resolverse corrigiendo o eliminando esos datos, pero la operacion debe quedar documentada y no convertirse en practica de produccion.

## Diagnostico

- Fallo de conexion: revisar URL, puerto, base, usuario, contrasena y servicio PostgreSQL.
- Fallo al iniciar por cifrado: definir `CORE_DATA_ENCRYPTION_KEY` o la compatibilidad `BANK_DATA_ENCRYPTION_KEY`.
- `401`: comprobar bearer JWT y caducidad.
- `403`/CORS: revisar `CORS_ALLOWED_ORIGIN` y origen real del frontend.
- Imagen ausente: revisar parametro de ruta del modulo, adjunto principal y permisos del directorio.
- Catalogo vacio: comprobar publicacion, token/posicion activos, registros activos y visibles.
- Webhook no recibido: la URL debe ser publica, HTTPS y estar configurada en el proveedor.

## Observabilidad y copias

Hibernate muestra SQL porque `spring.jpa.show-sql=true`; no es recomendable en produccion por volumen y posible exposicion. No hay Spring Boot Actuator configurado. Produccion necesitara logs estructurados, correlacion, metricas, alertas y endpoint de salud.

Las copias deben incluir PostgreSQL y directorios de adjuntos. Restaurar solo la base dejaria rutas sin fichero; restaurar solo ficheros dejaria adjuntos huerfanos.

## Lista antes de produccion

- Migraciones versionadas y copia/restauracion ensayada.
- Secretos en gestor seguro y rotacion definida.
- HTTPS, CORS restringido y rate limiting.
- Pruebas de autorizacion y aislamiento entre empresas.
- Pruebas de stock, numeracion, cadena documental e idempotencia.
- Monitorizacion, auditoria y politica de retencion.
- Integraciones externas probadas en sandbox.
