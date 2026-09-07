# Contrato de seguridad de la API

Estas reglas son obligatorias para todos los módulos actuales y futuros de GreenSaaS.

## Aislamiento por cliente

- El cliente de una operación autenticada se obtiene exclusivamente de `ContextoOperacion.empresaId()`, cuyo origen es el JWT validado.
- Un `empId` recibido en una URL o cuerpo nunca autoriza el acceso. Si una ruta antigua aún lo incluye, debe coincidir con el JWT o se rechaza.
- Toda lectura, modificación y borrado de negocio debe consultar por la clave del registro **y** por `empId`.
- Los consecutivos se calculan por cliente. No se permiten `MAX(id)` globales para entidades multiempresa.
- Los campos de cliente, auditoría, activo, usuario y fecha de movimiento son propiedad del backend.
- Los webhooks sin JWT son la única excepción: resuelven el cliente mediante el identificador de canal configurado y validan la firma del proveedor.

## DTO y validación

- Los endpoints nuevos usan DTO de entrada y salida; una entidad JPA no es un contrato público.
- Los DTO de entrada llevan Bean Validation (`@NotBlank`, `@NotNull`, tamaños, formato y rangos) y el controlador usa `@Valid`.
- No se admiten en DTO de entrada `empId`, contraseñas cifradas ni campos de auditoría.
- Los contratos heredados están protegidos de forma central: se descartan esos campos, se ejecuta Bean Validation y la respuesta se desacopla de la instancia gestionada. Al modificar un CRUD heredado, debe sustituirse por DTO tipado.
- Las respuestas de usuario nunca incluyen el hash de contraseña.

## Errores

Toda respuesta de error usa `ApiError`: `fecha`, `estado`, `codigo`, `mensaje`, `ruta`, `referencia` y `campos`.

- Las validaciones devuelven HTTP 400 y el detalle por campo.
- Las reglas de negocio devuelven un código estable y un mensaje comprensible.
- Los conflictos de integridad devuelven HTTP 409.
- Los accesos prohibidos devuelven HTTP 403.
- Los errores internos devuelven HTTP 500 con una referencia; nunca se exponen SQL, Hibernate, trazas o secretos.
- Angular normaliza este contrato en el interceptor HTTP para que las pantallas antiguas y nuevas muestren el mismo mensaje.
