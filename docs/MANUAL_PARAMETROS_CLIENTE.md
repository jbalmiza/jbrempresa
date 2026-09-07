# Manual de parámetros del cliente

## 1. Finalidad

Los parámetros permiten adaptar el comportamiento de cada cliente sin modificar el código. Cada registro pertenece a un único cliente (`emp_id`) y queda identificado por la combinación de módulo (`par_mod`) y código (`par_cod`).

Se administran desde **Administración → Parámetros** o desde la opción de parámetros del módulo correspondiente. Los códigos y módulos se guardan en mayúsculas.

## 2. Campos

| Campo | Utilidad |
|---|---|
| Módulo | Agrupa el parámetro y determina qué parte de la aplicación lo utiliza. |
| Código | Identificador técnico exacto. No debe traducirse ni cambiarse. |
| Descripción | Explicación legible de la finalidad del valor. |
| Valor | Configuración efectiva para el cliente. |
| Activo | Solo los parámetros activos se utilizan. |
| Usuario/fecha de movimiento | Auditoría de la última modificación. |

Los valores sensibles se muestran como `********`. Guardar ese texto conserva el secreto existente; escribir uno nuevo lo sustituye y lo almacena cifrado.

## 3. Reglas generales

- Debe existir como máximo un parámetro con el mismo módulo y código para cada cliente.
- No se deben copiar valores secretos entre clientes.
- Las rutas deben pertenecer al cliente y ser accesibles por el proceso del backend.
- Los teléfonos se guardan en formato internacional, por ejemplo `whatsapp:+34600000000`.
- Los valores booleanos se escriben como `true` o `false`.
- Después de modificar credenciales conviene reiniciar el backend antes de probar.
- El maestro de Personas es único: todos los selectores consultan las personas activas del módulo **Personas**. Ningún módulo mantiene un catálogo alternativo.

## 4. Administración y correo SMTP

Módulo: `ADMINISTRACION`.

| Código | Utilidad | Cómo obtener o decidir el valor | Ejemplo |
|---|---|---|---|
| `SMTP_HABILITADO` | Activa el envío real de correos. | Decisión del administrador. Mantener `false` hasta completar y probar el resto de valores. | `true` |
| `SMTP_SERVIDOR` | Servidor de correo saliente. | Documentación del proveedor de correo. | `smtp.office365.com` |
| `SMTP_PUERTO` | Puerto del servidor SMTP. | Documentación del proveedor. Normalmente 587 con STARTTLS. | `587` |
| `SMTP_USUARIO` | Cuenta usada para autenticarse. | Cuenta de correo creada para el cliente. | `avisos@empresa.es` |
| `SMTP_PASSWORD` | Contraseña o clave de aplicación. Es sensible y se cifra. | Panel de seguridad del proveedor. Con MFA suele requerir una clave de aplicación. | No documentar el valor real. |
| `SMTP_REMITENTE` | Dirección que aparecerá como remitente. | Dirección autorizada por el proveedor SMTP. | `avisos@empresa.es` |
| `SMTP_AUTENTICACION` | Indica si SMTP requiere usuario y contraseña. | Documentación del proveedor. | `true` |
| `SMTP_STARTTLS` | Activa el cifrado STARTTLS. | Documentación del proveedor. | `true` |
| `URL_FRONTEND` | Base pública usada para construir enlaces enviados por correo. | URL desde la que los usuarios abren la aplicación. En local es Angular; en producción, el dominio HTTPS. | `http://localhost:4200` |

## 5. Rutas de documentación adjunta

Cada ruta indica el directorio raíz donde el backend guarda los adjuntos del cliente. La aplicación añade internamente el tipo de registro y su identificador.

| Módulo | Código | Utilidad | Cómo establecer el valor |
|---|---|---|---|
| `PERSONAS` | `RUTA_DOCUMENTOS_PERSONAS` | Adjuntos de personas. | Crear una carpeta exclusiva del cliente con permisos de lectura y escritura para el backend. |
| `PRODUCTOS` | `RUTA_DOCUMENTOS_PRODUCTOS` | Adjuntos de productos. | Igual que el anterior, dentro del área de productos. |
| `SERVICIOS` | `RUTA_DOCUMENTOS_SERVICIOS` | Adjuntos de servicios. | Debe crearse para usar la acción Adjuntos de Servicios. Actualmente no está sembrado en todos los clientes. |
| `TERRITORIO` | `RUTA_DOCUMENTOS_DOMICILIOS` | Adjuntos de domicilios. | Carpeta exclusiva del cliente para domicilios. |
| `VENTAS` | `RUTA_DOCUMENTOS_VENTAS` | Adjuntos de pedidos, albaranes, presupuestos y facturas. | Carpeta exclusiva del cliente para ventas. |
| `COMUNICACIONES` | `RUTA_DOCUMENTOS_COMUNICACIONES` | Documentos e imágenes de mensajes y conversaciones. | Carpeta exclusiva del cliente para comunicaciones. |

Ejemplo de estructura local:

```text
C:\Workspace\Documentos\Cliente 1\Personas
C:\Workspace\Documentos\Cliente 1\Ventas
C:\Workspace\Documentos\Cliente 1\Comunicaciones
```

No se debe apuntar a la misma carpeta para clientes diferentes.

## 6. Selección del proveedor de WhatsApp

Módulo: `COMUNICACIONES`.

| Código | Utilidad | Valores |
|---|---|---|
| `WHATSAPP_PROVEEDOR` | Decide qué integración procesa los envíos de WhatsApp del cliente. | `TWILIO` o `META`. |

Cambiar este valor no elimina la configuración del otro proveedor. Esto permite mantener Meta configurado y activar Twilio temporalmente.

## 7. Parámetros de Twilio

Módulo: `COMUNICACIONES`.

| Código | Utilidad | Cómo obtener el valor |
|---|---|---|
| `TWILIO_ACCOUNT_SID` | Identifica la cuenta propietaria. Suele comenzar por `AC`. | Twilio Console → Dashboard o **API keys and Auth tokens**. |
| `TWILIO_AUTH_TOKEN` | Valida la firma de los webhooks recibidos. Es sensible y se cifra. | Twilio Console → **API keys and Auth tokens** → Primary Auth Token. |
| `TWILIO_API_KEY_SID` | Usuario técnico usado por el backend para enviar mensajes. Suele comenzar por `SK`. | Twilio Console → **API keys and Auth tokens** → API keys → crear una clave. |
| `TWILIO_API_KEY_SECRET` | Secreto de la API Key. Es sensible, se cifra y Twilio solo lo muestra al crearla. | Copiarlo en el momento de crear la API Key. Si se pierde, crear otra clave. |
| `TWILIO_WHATSAPP_FROM` | Remitente de WhatsApp asignado por Twilio. También permite identificar al cliente receptor del webhook. | En pruebas aparece en **Try out WhatsApp**. En producción se obtiene al registrar el remitente. Guardar con `whatsapp:+` y prefijo internacional. |
| `TWILIO_WEBHOOK_URL` | URL pública exacta usada por Twilio y por la validación criptográfica de su firma. | URL HTTPS pública más `/webhooks/twilio/whatsapp`. Debe coincidir carácter por carácter con la configurada en Twilio. |

Para pruebas locales, `TWILIO_WEBHOOK_URL` puede usar un túnel temporal de Cloudflare. Al reiniciar un Quick Tunnel cambia el dominio y debe actualizarse tanto en parámetros como en Twilio.

## 8. Parámetros de Meta WhatsApp Cloud API

Módulo: `COMUNICACIONES`. Solo se utilizan cuando `WHATSAPP_PROVEEDOR=META`.

| Código | Utilidad | Cómo obtener o crear el valor |
|---|---|---|
| `WHATSAPP_PHONE_NUMBER_ID` | Identificador técnico del número emisor/receptor. | Meta for Developers → aplicación → WhatsApp → API Setup. |
| `WHATSAPP_BUSINESS_ACCOUNT_ID` | Identificador de la cuenta de WhatsApp Business. | Meta Business Manager o WhatsApp → API Setup. Actualmente se conserva para configuración y futuras operaciones administrativas. |
| `WHATSAPP_ACCESS_TOKEN` | Autoriza envíos contra Graph API. Es sensible. | Token temporal en API Setup o token permanente de un usuario del sistema para producción. |
| `WHATSAPP_APP_SECRET` | Valida la firma de los webhooks de Meta. Es sensible. | Meta for Developers → App settings → Basic. |
| `WHATSAPP_WEBHOOK_VERIFY_TOKEN` | Texto secreto elegido por el administrador para verificar inicialmente el webhook. Es sensible. | Generar una cadena aleatoria y configurar exactamente la misma en Meta y en el cliente. |
| `WHATSAPP_GRAPH_API_VERSION` | Versión de Graph API utilizada en los envíos. | Versión vigente admitida por la aplicación de Meta, con formato `vNN.N`. |

Webhook de Meta:

```text
https://DOMINIO-PUBLICO/webhooks/meta/whatsapp
```

## 9. Comprobaciones después de modificar parámetros

1. Confirmar que el módulo y el código son exactos.
2. Confirmar que el parámetro está activo.
3. Reiniciar el backend si se han cambiado credenciales o proveedor.
4. Ejecutar una operación pequeña de prueba.
5. Revisar el mensaje funcional y los registros del backend.
6. Nunca incluir secretos reales en capturas, manuales, incidencias o repositorios.

## 10. Estado de sensibilidad

La aplicación cifra y oculta actualmente:

- `SMTP_PASSWORD`
- `TWILIO_AUTH_TOKEN`
- `TWILIO_API_KEY_SECRET`
- `WHATSAPP_ACCESS_TOKEN`
- `WHATSAPP_APP_SECRET`
- `WHATSAPP_WEBHOOK_VERIFY_TOKEN`

Los SID, números, URLs y rutas no se consideran contraseñas, aunque deben gestionarse con prudencia.
