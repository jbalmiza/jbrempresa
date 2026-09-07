# Seguridad y autenticación

## Login

[CONFIRMADO] `POST /usuarios/login` recibe un objeto Usuario con `usuUsu` y `usuCon`. El controlador busca por nombre de usuario y verifica siempre la contraseña con BCrypt; no existe compatibilidad con contraseñas en texto plano. El controlador construye `LoginResponse` con token, datos de usuario, cliente y perfil.

[CONFIRMADO] El campo `usuCon` es de solo escritura en JSON: puede recibirse en login y formularios, pero no se envía en respuestas de usuarios.

[CONFIRMADO] En altas, modificaciones y login, el perfil del usuario se busca mediante `empId` y `perId`; no se admite asociar ni devolver perfiles de otro cliente.

[CONFIRMADO] `usuUsu` es único globalmente. Se valida al crear y modificar usuarios, y la columna JPA declara una restricción única para evitar ambigüedad al resolver el subject del JWT.

[REVISAR] Antes de aplicar la restricción en una base de datos existente, comprobar si hay nombres de usuario duplicados y corregirlos; de otro modo la actualización del esquema puede fallar.

[CONFIRMADO] Los módulos activos de Domicilios, Personas, Perfiles, Productos, Ventas, Usuarios y Mallas asignan el campo `*_UsuMov` a partir de `JwtUser.getUsername()` en backend. El valor enviado por Angular no se conserva.

## JWT

[CONFIRMADO] `JwtService` genera tokens firmados con HMAC. El subject es el nombre de usuario e incluye los claims `usuarioId`, `empresaId` y `perfilId`. El vencimiento configurado es de 30 minutos desde su creación.

[CONFIRMADO] La sesión se renueva de forma deslizante. Cuando una petición autenticada usa un token al que le quedan cinco minutos o menos, `JwtFilter` devuelve un token nuevo en la cabecera `X-Refresh-Token`; el interceptor Angular lo guarda en `localStorage`. Si no hay actividad autenticada durante 30 minutos, no se renueva y el token expira.

[CONFIRMADO] La clave de firma se obtiene de la variable de entorno obligatoria `JWT_SECRET`; no se incluye una clave por defecto en el código. Debe tener al menos 32 bytes UTF-8.

### Configuración local de JWT_SECRET

En PowerShell, antes de arrancar el backend:

```powershell
$env:JWT_SECRET = 'una-clave-privada-aleatoria-de-al-menos-32-caracteres'
```

La variable debe configurarse también en el entorno donde se despliegue la aplicación. No debe añadirse su valor a `application.properties`, archivos versionados ni documentación pública.

### Cifrado de datos bancarios

[CONFIRMADO] Los IBAN de domiciliaciones se cifran mediante AES-256-GCM antes de persistirse y la API solo devuelve una versión enmascarada. La clave procede de `BANK_DATA_ENCRYPTION_KEY`, debe tener al menos 32 caracteres y no se guarda en base de datos ni en archivos versionados.

En PowerShell, antes de arrancar el backend: `$env:BANK_DATA_ENCRYPTION_KEY = 'una-clave-privada-distinta-de-al-menos-32-caracteres'`.

La pérdida o modificación de esta clave impediría descifrar los IBAN existentes; debe custodiarse y respaldarse como secreto de infraestructura.

## Filtro y principal autenticado

[CONFIRMADO] `JwtFilter` se ejecuta una vez por petición, lee `Authorization`, extrae el subject y llama a `CustomUserDetailsService`. Este carga `Usuario` y construye `JwtUser`, que contiene IDs de usuario, cliente y perfil. El filtro inserta esa identidad en `SecurityContextHolder`.

## Autorización y CORS

[CONFIRMADO] `SecurityConfig` desactiva CSRF, configura sesiones stateless, permite públicamente `/usuarios/login` y exige autenticación en el resto de rutas. No se han encontrado autoridades por rol: `JwtUser.getAuthorities()` devuelve una colección vacía.

[CONFIRMADO] CORS permite un único origen configurable mediante `CORS_ALLOWED_ORIGIN` (por defecto, `http://localhost:4200` en desarrollo), credenciales, GET/POST/PUT/PATCH/DELETE/OPTIONS y todas las cabeceras. Expone `X-Refresh-Token` para que Angular pueda leer la renovación.

Para ejecutar el backend con otro origen permitido, en PowerShell: `$env:CORS_ALLOWED_ORIGIN = 'https://aplicacion.ejemplo.com'`. El frontend concentra su URL de API en `frontend/src/app/config/api-url.config.ts`; ambos valores deben corresponder al entorno desplegado.

## Frontend

[CONFIRMADO] Login guarda `token`, IDs y nombres de sesión en `localStorage`. `jwtInterceptor` añade el token a cualquier solicitud. Ante una respuesta 401 borra la sesión completa, muestra el mensaje de sesión caducada y redirige a `/accesoLogin`.

[CONFIRMADO] `authGuard` protege las rutas Angular que requieren sesión. Si no existe `token` en `localStorage`, Angular redirige a `/accesoLogin` antes de crear la página. El backend mantiene la validación definitiva del token.

## Puntos a revisar

- [CONFIRMADO] La recuperación de contraseña solicita usuario y correo y responde siempre de forma neutra. Genera un token aleatorio de un solo uso, persiste únicamente su hash SHA-256, caduca a los 30 minutos y revoca los tokens anteriores y el utilizado. La contraseña nueva se cifra con BCrypt y exige al menos 8 caracteres, mayúscula, minúscula y número.
- [CONFIRMADO] Cada cliente configura SMTP mediante parámetros del módulo `ADMINISTRACION`: `SMTP_HABILITADO`, `SMTP_SERVIDOR`, `SMTP_PUERTO`, `SMTP_USUARIO`, `SMTP_PASSWORD`, `SMTP_REMITENTE`, `SMTP_AUTENTICACION`, `SMTP_STARTTLS` y `URL_FRONTEND`. `SMTP_PASSWORD` se cifra en el Core y las consultas solo muestran una máscara. La clave maestra `CORE_DATA_ENCRYPTION_KEY` permanece como secreto de infraestructura. `PASSWORD_RESET_EXPOSE_TOKEN` permanece desactivado salvo pruebas locales controladas.
- [PENDIENTE] Incorporar limitación y bloqueo progresivo de intentos de acceso, registro de intentos y avisos de seguridad.

## Problemas pausados

- [PAUSADO] Autorización por perfil. La aplicación conserva el perfil del usuario, pero no traduce sus permisos a authorities de Spring Security. Su diseño e implementación quedan aplazados por decisión del propietario.

## Trazabilidad

- `security/SecurityConfig.java`, `JwtFilter.java`, `JwtService.java`, `JwtUser.java`
- `core/security/RecuperacionContrasenaService.java`, `controller/RecuperacionContrasenaController.java`
- `controller/UsuarioController.java`, `repository/UsuarioRepository.java`
- `frontend/src/app/interceptors/jwt.interceptor.ts`
