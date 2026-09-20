# Seguridad y autorización

Revisión: 2026-09-15. Fuentes: [SecurityConfig](../src/main/java/com/jbrempresa/backend/security/SecurityConfig.java), [JwtFilter](../src/main/java/com/jbrempresa/backend/security/JwtFilter.java), [ContextoOperacion](../src/main/java/com/jbrempresa/backend/core/context/ContextoOperacion.java), [AccesoPerfilService](../src/main/java/com/jbrempresa/backend/security/AccesoPerfilService.java).

## JWT y perfiles

Login usa BCrypt. JWT dura 30 minutos y se renueva mediante X-Refresh-Token exclusivamente al recibir `GET /usuarios/actividad` enviado tras interacción del usuario. Las consultas automáticas no renuevan la sesión. Angular guarda el token en localStorage y lo envía en Authorization. JwtFilter vuelve a cargar el usuario para formar el principal. La guarda Angular ayuda a navegar, pero no reemplaza validación backend.

El panel filtra los módulos por disponibilidad empresarial y perfil. Administrador/Jefe acceden a los módulos reconocidos; Cliente al catálogo y Empleado a su agenda. Existen restricciones adicionales, por ejemplo administración de empresas, módulos y mensajes.

El Empleado registra pedidos mediante el catálogo y puede consultar, modificar o eliminar sus propios pedidos en Gestión de ventas. El filtro se aplica en la API por empresa y autor del movimiento de alta; la modificación y eliminación verifican la titularidad antes de alterar el pedido o su cadena. El controlador rechaza con 403 los tipos ajenos, pedidos de otros usuarios y operaciones no autorizadas.

## Matriz de empresa efectiva

| Contexto | Origen del ámbito | Condición |
|---|---|---|
| Usuario no administrador | Empresa de JwtUser | La cabecera de selección no cambia su empresa. |
| Administrador, empresa concreta | X-Empresa-Seleccionada positiva | Los endpoints usan el contexto seleccionado según su contrato. |
| Administrador, global | Sin selección o valor 0 | empresaConsulta puede ser null para consultas globales; empresaId usa la empresa del usuario como fallback. No todas las operaciones admiten “Todas”. |
| Catálogo público | Token general, alias o posición | Publicación y reglas del catálogo. No identifica personalmente al visitante. |
| Catálogo de proveedor | Empresa compradora autenticada y proveedor de la URL | Permiso PROVEEDORES y relación PROVEEDOR activa/vigente; Administrador debe elegir una empresa. |

No confiar en empId del cuerpo ni en ocultar acciones. Las escrituras deben comprobar referencias en su contexto. Los accesos cruzados de proveedor son una excepción autorizada, no una consulta global.

## Rutas públicas y secretos

Son públicos /usuarios/login, /auth/password/**, /catalogo/publico/** y webhooks Meta/Twilio. Las demás rutas exigen JWT. El catálogo privado de Proveedores no devuelve tokens públicos, aunque el catálogo publicado conserva su URL pública para clientes.

JWT_SECRET y claves de cifrado llegan por entorno. CifradoDatosSensibles usa AES-GCM para datos sensibles; parámetros protegidos se devuelven enmascarados. PASSWORD_RESET_EXPOSE_TOKEN debe ser false fuera de pruebas. No versionar secretos ni registrar JWT o contenido sensible.

## CORS, webhooks y errores

CORS permite orígenes de desarrollo configurables, métodos REST y X-Refresh-Token. CSRF está desactivado al usar bearer, no sesión por cookie. Meta y Twilio validan firmas en sus adaptadores; la verificación inicial del webhook no sustituye la firma de eventos. Los errores se centralizan, aunque denegaciones previas al controlador pueden carecer del cuerpo JSON habitual.

## Límites reales

No existe una autorización declarativa uniforme por operación para toda la API. Las verificaciones están repartidas entre controladores/servicios: deben revisarse por recurso. No se acredita ausencia global de vulnerabilidades. Tampoco hay rate limiting general ni monitorización Actuator. Véanse [limitaciones](LIMITACIONES.md), [pruebas](PRUEBAS.md) e [integraciones](INTEGRACIONES.md).
