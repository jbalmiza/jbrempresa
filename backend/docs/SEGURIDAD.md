# Seguridad

## Autenticacion JWT

El login valida credenciales con Spring Security y BCrypt. El token firmado identifica usuario, empresa y perfil. La API es stateless: no crea sesion HTTP. `JwtFilter` procesa el header `Authorization`, valida el token y crea el principal usado por el contexto de empresa.

Las rutas publicas son exclusivamente login, recuperacion de contrasena, catalogo publico y webhooks de WhatsApp. El resto exige autenticacion.

## Aislamiento de empresa

La defensa principal es incluir `emp_id` en todas las consultas y validar que referencias y adjuntos pertenecen a la empresa autenticada. Nunca debe aceptarse como autoridad un `emp_id` del cuerpo. Los tokens de catalogo resuelven internamente una sola empresa y posicion.

## Contrasenas y recuperacion

- Las contrasenas se guardan con BCrypt, nunca reversibles.
- La recuperacion genera un token temporal persistido con caducidad y estado de uso.
- `PASSWORD_RESET_EXPOSE_TOKEN` debe ser `false` fuera de desarrollo; si es `true`, la respuesta puede mostrar el token para pruebas.
- La entrega real depende de la configuracion SMTP efectiva de la empresa.

## Datos sensibles

`CifradoDatosSensibles` usa AES-GCM con una clave externa para IBAN y secretos de configuracion. `CORE_DATA_ENCRYPTION_KEY` es la clave preferida y puede heredar `BANK_DATA_ENCRYPTION_KEY`. No deben registrarse claves, IBAN completos, JWT, tokens de proveedor ni la clave secreta Redsys.

Las API de configuracion devuelven secretos enmascarados. Un valor enmascarado recibido en una actualizacion no debe sobrescribir el secreto ya cifrado.

## CORS y CSRF

CORS toma patrones separados por coma de `CORS_ALLOWED_ORIGIN`; por defecto permite Angular local y rangos privados de desarrollo. Admite credenciales, `Authorization`, metodos REST y expone `X-Refresh-Token`. CSRF esta desactivado porque la autenticacion normal usa bearer token y no cookie de sesion.

## Webhooks y catalogo publico

Los webhooks y el catalogo no tienen JWT y requieren protecciones propias. Meta verifica el endpoint mediante su token configurado; los eventos deben deduplicarse por id externo. El token QR es opaco y regenerable, pero no equivale a autenticar una persona. Los datos de pedido se validan y se limitan a lo minimo necesario.

## Riesgos pendientes

- La cobertura automatizada de autorizacion y aislamiento multiempresa es limitada.
- No hay rate limiting documentado para login, recuperacion o pedidos publicos.
- No hay Actuator ni monitorizacion de intentos de acceso.
- Antes de produccion debe sustituirse `ddl-auto=update` por migraciones revisables.
- La futura notificacion Redsys debe validar firma, importe, moneda, pedido e idempotencia antes de cambiar estados.
