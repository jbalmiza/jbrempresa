# Catálogo y pedidos públicos

## Concepto de malla

La malla es un mapa editable que crea cada empresa para representar la distribución física que necesita. No presupone un tipo de negocio ni elementos fijos: puede dibujar un local, un almacén, un taller, puestos de atención, caminos, estanterías o cualquier otra organización mediante posiciones y colores.

Los registros funcionales, como productos o pedidos, pueden ocupar posiciones del mapa. Los colores sin registro asociado forman parte del dibujo definido por el usuario; en las distribuciones de prueba el negro representa caminos.

Productos y Servicios comparten la presentación móvil. Cada elemento indica su tipo, muestra una imagen y puede añadirse al mismo pedido.

La imagen principal no se edita en el formulario del registro. Se incorpora en Adjuntos y se marca allí como principal entre los archivos de imagen disponibles.

`Visible en catálogo` se encuentra en Datos Adicionales y está activado por defecto en las nuevas altas de ambos módulos.

## Acceso

- Ruta pública: `/catalogo/:token`.
- No requiere inicio de sesión.
- El host del backend se deduce del host desde el que se abre Angular y utiliza el puerto `8080`. Esto permite abrir un QR desde un dispositivo móvil conectado a la misma red.
- Ruta de gestión autenticada: `/productos/catalogo`.
- Menú: **Productos > Utilidades > Catálogo**.

## Presentación móvil

El catálogo está diseñado primero para teléfono móvil. Muestra:

- imagen y nombre de la empresa;
- ubicación cuando el acceso procede del QR de una posición;
- tipos de producto y tipos de servicio en navegación horizontal;
- tipos de servicio diferenciados mediante un color verde azulado propio;
- subcategoría, imagen, nombre, descripción y precio final de cada producto;
- estado `Agotado` para productos con control de stock y sin unidades;
- selector de cantidad y cesta fija inferior.

La cabecera incluye la acción `Cerrar`. Cuando el catálogo se ha abierto en una pestaña desde la aplicación, devuelve el foco a la pestaña de origen y cierra la del catálogo, siguiendo el comportamiento de la acción `Módulos`.

Los registros se agrupan por tipo de producto o tipo de servicio y después muestran su subcategoría cuando existe.

## Cesta y confirmación

El usuario puede preparar la cesta sin identificarse. Al confirmar se solicitan únicamente:

- nombre obligatorio;
- teléfono obligatorio;
- dirección de envío en un único campo de texto, solo para pedidos a domicilio;
- observaciones opcionales del pedido, máximo 500 caracteres;
- observaciones opcionales por producto, máximo 250 caracteres.

Tras enviar se muestra número de pedido, total y confirmación. El botón queda bloqueado durante el envío para evitar duplicados accidentales.

## Modalidades

- `EN_POSICION`: el enlace procede del QR único de una posición de la malla.
- `DOMICILIO`: el enlace general permite un pedido con dirección de envío cuando la empresa lo ha habilitado.

## Gestión interna

La pantalla de Catálogo contiene:

- **Configuración**: publicación y pedidos a domicilio. Las rutas de imágenes se administran en Parámetros de cada módulo.
- **Códigos QR**: alta de ubicación, fila y columna; apertura del enlace; descarga y regeneración del QR.

En el registro de productos, la ubicación textual, la fila y la columna seleccionadas en la malla se cargan al modificar el producto y se conservan al actualizarlo.

La opción `Visible en catálogo` se encuentra en Datos Adicionales, junto a observaciones y duración, porque controla la disponibilidad comercial complementaria del producto.
- **Vista previa**: enlace general del catálogo para pedidos a domicilio.

Cada posición tiene un token público aleatorio. Regenerarlo invalida el enlace anterior sin depender de que la posición esté ocupada por un pedido.

## Imágenes

- Un producto tiene una imagen principal y el indicador `Visible en catálogo`.
- Un producto solo puede publicarse si tiene imagen.
- La empresa debe tener imagen para publicar el catálogo.
- Formatos admitidos: JPEG, PNG y WebP.
- Tamaño máximo: 5 MB.
- Solo un adjunto de imagen puede estar marcado como principal por registro.
- La imagen principal se gestiona desde Adjuntos en Productos, Servicios y Empresas.
- La malla de productos muestra una ampliación de la imagen principal y el nombre al pasar el ratón por una posición ocupada.

## Contratos TypeScript

- `Producto`: `proVisCat`, `proIma`.
- `Empresa`: `empIma`.
- `DocumentoVenta`: `dovUbi`, `dovOri`, `dovMod`, `dovDirEnv`.
- `DocumentoVentaDetalle`: `dvdObs`.
- Servicio: `src/app/services/catalogo.service.ts`.

## Verificación

```powershell
cd frontend
npm.cmd run build
npm.cmd start -- --host 0.0.0.0
```

URL local: `http://localhost:4200`.
