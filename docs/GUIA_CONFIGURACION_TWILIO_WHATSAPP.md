# Guía para crear y configurar Twilio WhatsApp

## 1. Objetivo

Esta guía configura una cuenta de prueba de Twilio para recibir mensajes de WhatsApp en la aplicación y responder desde Comunicaciones. La integración de Meta permanece disponible; el proveedor activo se decide por cliente mediante `WHATSAPP_PROVEEDOR`.

## 2. Crear la cuenta

1. Entrar en `https://www.twilio.com/` y pulsar **Start for free**.
2. Registrar el correo, verificarlo y completar la verificación del teléfono solicitada por Twilio.
3. En las preguntas iniciales seleccionar un uso relacionado con atención al cliente o notificaciones y elegir **With code**.
4. Entrar en Twilio Console.

Una cuenta Trial tiene límites: solo destinatarios autorizados, Sandbox de WhatsApp, saldo/mensajes limitados y restricciones de producción.

## 3. Obtener las credenciales de cuenta

En el Dashboard o mediante **API keys and Auth tokens**:

1. Copiar **Account SID** y guardarlo como `TWILIO_ACCOUNT_SID`.
2. Mostrar y copiar **Primary Auth Token** y guardarlo como `TWILIO_AUTH_TOKEN`.

El Auth Token se utiliza para validar que los webhooks proceden realmente de Twilio. No debe publicarse ni enviarse en capturas.

## 4. Crear una API Key para los envíos

1. Abrir **API keys and Auth tokens**.
2. Entrar en la sección de API Keys y pulsar la opción para crear una clave.
3. Elegir una clave estándar para la aplicación.
4. Copiar el **SID** de la clave, normalmente con prefijo `SK`, en `TWILIO_API_KEY_SID`.
5. Copiar el **Client secret** en `TWILIO_API_KEY_SECRET`.
6. Confirmar que el secreto ha sido guardado.

Twilio muestra el Client secret una sola vez. Si se pierde, hay que crear otra API Key; no se puede recuperar.

## 5. Activar el entorno de prueba de WhatsApp

1. Desde el Dashboard buscar **Messaging**.
2. En el bloque de WhatsApp pulsar **Try out WhatsApp**.
3. Elegir el teléfono destinatario.
4. Escanear el código QR con el móvil o enviar el mensaje que indique Twilio, por ejemplo `join twilio-trial`, al número de prueba mostrado.
5. Esperar la confirmación de vinculación.

Twilio puede exigir volver a vincular el dispositivo en cada sesión de prueba. Solo puede haber un destinatario activo a la vez en este modo.

## 6. Configurar el remitente

En **Try out WhatsApp**, copiar el número de prueba que aparece como remitente y guardarlo en el cliente con el prefijo de canal:

```text
TWILIO_WHATSAPP_FROM = whatsapp:+NUMERO_INTERNACIONAL
```

No se debe poner aquí el móvil personal destinatario. Es el número de Twilio al que se envió `join twilio-trial`.

## 7. Publicar el backend local con Cloudflare Tunnel

Twilio no puede llamar a `localhost`. Para pruebas se crea una URL HTTPS temporal.

### Instalación normal en Windows

Abrir PowerShell:

```powershell
winget install --id Cloudflare.cloudflared --exact
```

Cerrar y abrir PowerShell y comprobar:

```powershell
cloudflared --version
```

### Instalación directa si `winget` se bloquea

```powershell
New-Item -ItemType Directory -Force "$env:LOCALAPPDATA\Cloudflare"
```

```powershell
Invoke-WebRequest "https://github.com/cloudflare/cloudflared/releases/latest/download/cloudflared-windows-amd64.exe" -OutFile "$env:LOCALAPPDATA\Cloudflare\cloudflared.exe"
```

```powershell
& "$env:LOCALAPPDATA\Cloudflare\cloudflared.exe" --version
```

### Iniciar el túnel

Con el backend iniciado en el puerto 8080:

```powershell
& "$env:LOCALAPPDATA\Cloudflare\cloudflared.exe" tunnel --url http://localhost:8080
```

Cloudflare mostrará una URL parecida a:

```text
https://palabras-aleatorias.trycloudflare.com
```

Mantener abierta esa consola. El webhook completo será:

```text
https://palabras-aleatorias.trycloudflare.com/webhooks/twilio/whatsapp
```

Los Quick Tunnels son temporales y solo adecuados para desarrollo. La URL cambia al detener y crear el túnel de nuevo.

## 8. Configurar el webhook de entrada en Twilio

1. Abrir nuevamente **Try out WhatsApp**.
2. Vincular el dispositivo si Twilio vuelve a mostrar **Connect to testing environment**.
3. Seleccionar **Inbound**.
4. En **Auto-reply settings**, cambiar **Standard reply** por **Custom webhook**.
5. Pegar la URL pública completa terminada en `/webhooks/twilio/whatsapp`.
6. Seleccionar el método `POST` si se solicita.
7. Guardar con **Save**.

La URL configurada en Twilio y `TWILIO_WEBHOOK_URL` deben ser idénticas. Una diferencia de protocolo, dominio, barra final o ruta provoca el rechazo de la firma.

## 9. Crear los parámetros del cliente

En el módulo `COMUNICACIONES`, configurar:

```text
WHATSAPP_PROVEEDOR      = TWILIO
TWILIO_ACCOUNT_SID      = AC...
TWILIO_AUTH_TOKEN       = secreto
TWILIO_API_KEY_SID      = SK...
TWILIO_API_KEY_SECRET   = secreto mostrado una vez
TWILIO_WHATSAPP_FROM    = whatsapp:+NUMERO_TWILIO
TWILIO_WEBHOOK_URL      = https://DOMINIO/webhooks/twilio/whatsapp
```

No escribir secretos reales en documentación. La pantalla de parámetros los cifra y posteriormente los muestra como `********`.

## 10. Probar WhatsApp → aplicación

1. Mantener iniciados backend y Cloudflare Tunnel.
2. Desde el móvil vinculado enviar un mensaje normal al número de prueba de Twilio.
3. Entrar en **Comunicaciones → Bandeja de entrada**.
4. Pulsar **Consultar** y abrir el mensaje.
5. Seleccionar una Persona del maestro único de Personas y confirmarla.
6. Confirmar la clasificación.
7. Crear una conversación nueva o vincular el mensaje a una existente de esa Persona.

El mensaje debe quedar como canal `WHATSAPP`, dirección `ENTRADA`, con el teléfono remitente y el Message SID de Twilio.

## 11. Probar aplicación → WhatsApp

1. Entrar en **Comunicaciones → Conversaciones**.
2. Consultar y abrir la conversación de WhatsApp.
3. Escribir la respuesta.
4. Seleccionar autor `Usuario` y dirección `Salida`.
5. Pulsar **Registrar mensaje**.

Para una conversación WhatsApp con dirección `SALIDA`, la aplicación registra el mensaje y lo envía mediante Twilio. El resultado debe contener un Message SID y estado inicial `ENVIADO`.

## 12. Problemas frecuentes

| Síntoma | Comprobación |
|---|---|
| Twilio responde automáticamente pero la aplicación no recibe nada | Confirmar que Inbound usa **Custom webhook**, no **Standard reply**. |
| Error de firma | Comparar exactamente la URL de Twilio y `TWILIO_WEBHOOK_URL`; comprobar el Auth Token. |
| Cloudflare muestra error de origen | Confirmar que el backend escucha en `http://localhost:8080`. |
| El mensaje entrante no encuentra cliente | Revisar que `TWILIO_WHATSAPP_FROM` coincide con el campo `To` enviado por Twilio. |
| No permite responder | Confirmar API Key SID, API Key Secret, Account SID, remitente y destinatario vinculado al Sandbox. |
| El teléfono no aparece conectado | Volver a enviar `join twilio-trial` o escanear el QR. |
| Tras reiniciar Cloudflare deja de funcionar | Guardar la nueva URL tanto en Twilio como en `TWILIO_WEBHOOK_URL`. |

## 13. Paso a producción

Antes de producción:

1. Actualizar la cuenta Trial y completar verificaciones de Twilio/Meta requeridas.
2. Registrar y aprobar el remitente real de WhatsApp.
3. Sustituir el Quick Tunnel por un dominio HTTPS estable.
4. Crear credenciales nuevas y revocar todas las publicadas durante pruebas.
5. Guardar secretos únicamente mediante la pantalla segura de parámetros o un gestor de secretos.
6. Probar entrada, salida, errores y trazabilidad con un cliente de ensayo.
7. Mantener `WHATSAPP_PROVEEDOR=TWILIO` o cambiarlo a `META` según el proveedor contratado.

## 14. Seguridad

- No guardar Auth Tokens o API Key Secrets en Git.
- No incluir secretos en capturas o documentación.
- Rotar inmediatamente cualquier secreto expuesto.
- Usar una API Key dedicada a la aplicación y eliminarla al dejar de usarla.
- Usar HTTPS y conservar la validación de firmas del webhook.
