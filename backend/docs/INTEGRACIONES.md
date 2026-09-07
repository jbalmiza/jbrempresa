# Integraciones

## Correo SMTP

La recuperacion de contrasena usa `ConfiguracionSmtpEmpresaService` y Java Mail. La configuracion efectiva se obtiene de parametros de la empresa. Deben definirse host, puerto, usuario, secreto, remitente y seguridad segun el proveedor. Los secretos se almacenan cifrados y se presentan enmascarados.

## WhatsApp

Existen dos entradas publicas:

- Meta: `/webhooks/meta/whatsapp`, con verificacion `GET` y eventos `POST`.
- Twilio: `/webhooks/twilio/whatsapp`, formulario `POST`.

La recepcion normaliza el mensaje, identifica empresa/canal, evita duplicados por id externo y lo coloca en bandeja o conversacion. `ConfiguracionWhatsappEmpresaService` resuelve proveedor y credenciales. El envio de texto usa el proveedor configurado y registra destinatario, id externo y estado.

No deben registrarse tokens de Meta, credenciales Twilio ni contenido sensible innecesario.

## Codigos QR

ZXing genera PNG para una posicion publica. El QR codifica la URL con token regenerable, no los datos internos del pedido. La imagen descargada incorpora como texto ubicacion, fila y columna. Regenerar invalida el acceso mediante el token anterior.

## Redsys/Bizum: estado actual

La arquitectura esta preparada, pero no existe un pago real. `PasarelaPago` define el contrato, `RedsysBizumGateway` es el adaptador previsto y `ConfiguracionRedsysBizumService` valida/muestra el estado. `GET /catalogo/gestion/pago` solo informa si la configuracion esta completa y activa; no crea una operacion bancaria.

Se guardan ocho parametros por empresa en el modulo `VENTAS`:

| Codigo | Uso |
|---|---|
| `PAGO_BIZUM_ACTIVO` | Interruptor funcional, por defecto desactivado. |
| `PAGO_BIZUM_PASARELA` | Pasarela prevista: `REDSYS`. |
| `PAGO_BIZUM_ENTORNO` | `PRUEBAS` o `PRODUCCION`. |
| `PAGO_BIZUM_COMERCIO` | Codigo de comercio/FUC. |
| `PAGO_BIZUM_TERMINAL` | Terminal habilitado para Bizum. |
| `PAGO_BIZUM_CLAVE` | Clave de firma cifrada y enmascarada. |
| `PAGO_BIZUM_BANCO` | Banco contratante, solo informativo. |
| `PAGO_BIZUM_OBLIGATORIO` | Politica futura de obligatoriedad. |

Si hay tres empresas aparecen 24 filas porque son ocho valores independientes por empresa; no son 24 conceptos distintos.

## Trabajo pendiente para activar pagos

1. Confirmar contrato y credenciales Redsys con Bizum habilitado.
2. Implementar creacion de operacion, firma y formulario/redireccion.
3. Persistir intento, identificador, importe, moneda y estado de pago.
4. Implementar callback publico con validacion criptografica e idempotencia.
5. Verificar que importe, moneda y pedido coinciden antes de marcar pagado.
6. Resolver expiracion, rechazo, reintento, devolucion y conciliacion.
7. Probar en entorno Redsys de pruebas y realizar revision de seguridad.

## Decisiones futuras no cerradas

- En que momento del pedido se ofrece Bizum y si es opcional u obligatorio.
- Politica de cancelacion y devoluciones.
- Si se simplifica la presentacion administrativa de los ocho parametros sin alterar los datos requeridos por Redsys.
