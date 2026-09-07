# Catálogo, QR y pedidos públicos

## Significado de la malla

La malla es un mapa configurable por empresa, compuesto por filas, columnas y celdas coloreadas. Su estructura no está vinculada a mesas ni a un sector concreto: representa el espacio físico que el usuario decida, como un local, almacén, taller o zona de atención.

Las celdas manuales conservan el dibujo del mapa y las celdas vinculadas relacionan una posición con un producto o pedido. En los datos de prueba el negro se utiliza como camino, sin convertir ese significado visual en una regla fija del backend.

## Modelo

### Producto

- `pro_vis_cat`: indica si aparece en el catálogo.
- `pro_ima`: nombre relativo de la imagen principal.
- Un producto visible debe tener una imagen.

### Servicio

- `ser_vis_cat`: indica si aparece en el catálogo.
- `ser_ima`: nombre relativo de la imagen principal.
- Productos y Servicios se publican por defecto y reciben una imagen predeterminada sustituible.

### Empresa

- `emp_ima`: nombre relativo de la imagen principal utilizada en la cabecera del catálogo.

### Pedido

- `dov_ubi`: texto descriptivo de ubicación.
- `dov_ori`: `INTERNO` o `CATALOGO`.
- `dov_mod`: `EN_POSICION` o `DOMICILIO`.
- `dov_dir_env`: dirección de envío propia del pedido, sin relación con el maestro de Domicilios.
- `dov_fil_mal` y `dov_col_mal`: posición de la malla cuando corresponde.
- `dvd_obs`: observaciones de una línea, máximo 250 caracteres.
- `dov_obs`: observaciones generales, máximo 500 caracteres.

Los campos de ubicación, origen y modalidad se propagan al convertir pedido, albarán y factura. El histórico conserva la información de ubicación y modalidad.

### Posiciones públicas

La tabla `catalogo_posiciones` mantiene una identidad permanente por empresa, fila y columna. Cada posición tiene:

- ubicación textual;
- fila y columna;
- token público aleatorio y único;
- estado activo y auditoría.

La identidad del QR no se almacena en la ocupación temporal de `mallas`. Pagar un pedido libera la ocupación, pero conserva el QR físico.

## Parámetros por empresa

Las rutas no forman parte del contrato de configuración del Catálogo. Se consultan y modifican desde Parámetros del módulo propietario.

Módulo `PRODUCTOS`:

- `RUTA_IMAGENES`;
- `CATALOGO_PUBLICADO`;
- `CATALOGO_DOMICILIO`;
- `CATALOGO_TOKEN_GENERAL`.

Módulo `ADMINISTRACION`:

- `RUTA_IMAGENES`.

Módulo `SERVICIOS`:

- `RUTA_IMAGENES`.

Estructura física:

```text
[ruta productos]/empresa-{empId}/productos/
[ruta servicios]/empresa-{empId}/servicios/
[ruta empresas]/empresa-{empId}/empresas/
```

La base de datos conserva solo el nombre relativo del archivo. El backend normaliza las rutas para impedir salir del directorio configurado.

## Imágenes

- Formatos: JPEG, PNG y WebP.
- Máximo: 5 MB.
- Una imagen principal por registro.
- Los adjuntos incorporan `adj_pri`; solo uno puede ser principal para cada registro.
- Marcar un adjunto como principal copia su contenido al directorio de imágenes del módulo y actualiza el registro.
- El adjunto principal no puede eliminarse hasta seleccionar otro.
- Las imágenes públicas se sirven mediante endpoints del catálogo; no se expone la ruta física.

## API autenticada

- `GET /catalogo/gestion/configuracion`
- `PUT /catalogo/gestion/configuracion`
- `GET /catalogo/gestion/pago`
- `GET /catalogo/gestion/posiciones`
- `POST /catalogo/gestion/posiciones`
- `POST /catalogo/gestion/posiciones/{id}/regenerar`
- `GET /catalogo/gestion/posiciones/{id}/qr`
- `POST /productos/{id}/imagen`
- `GET /productos/{id}/imagen`
- `POST /servicios/{id}/imagen`
- `GET /servicios/{id}/imagen`
- `POST /empresas/{id}/imagen`
- `GET /empresas/{id}/imagen`
- `PUT /adjuntos/{id}/principal?codigoRuta=...`

## API pública

- `GET /catalogo/publico/{token}`
- `POST /catalogo/publico/{token}/pedidos`
- `GET /catalogo/publico/{token}/productos/{id}/imagen`
- `GET /catalogo/publico/{token}/servicios/{id}/imagen`
- `GET /catalogo/publico/{token}/empresa/imagen`

Solo estas rutas públicas están exentas de JWT. El token resuelve internamente empresa y posición; el navegador no decide `empId`, fila ni columna.

## Creación del pedido

1. Se valida que el catálogo esté publicado.
2. Se resuelve empresa y modalidad desde el token.
3. Se reutiliza una Persona con coincidencia exacta de teléfono o se crea una nueva con nombre y teléfono.
4. El pedido se crea como `PED`, origen `CATALOGO` y estado `EMITIDO`.
5. Los precios, descuentos, impuestos y disponibilidad se obtienen nuevamente del servidor. Cada línea identifica `PRODUCTO` o `SERVICIO` y genera una referencia `pro_id` o `ser_id`.
6. Las observaciones se guardan en pedido y líneas.
7. Si existe control de stock, se descuentan unidades.
8. Para pedidos en posición se registra la ocupación en la malla.

Al dar de baja o eliminar un pedido de catálogo se devuelve el stock. Al reactivarlo se comprueba y vuelve a descontar. La posición permanece ocupada hasta `PAGADO`.

## Seguridad

- El catálogo no acepta precios enviados por el navegador.
- Los recursos se filtran por la empresa resuelta desde el token.
- Los nombres de archivo se normalizan.
- Los QR pueden regenerarse para invalidar enlaces compartidos.
