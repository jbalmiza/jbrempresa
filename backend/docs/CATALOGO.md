# Catálogo, imágenes y pedidos

Revisión: 2026-09-15. Fuentes: [CatalogoService](../src/main/java/com/jbrempresa/backend/service/CatalogoService.java), [CatalogoController](../src/main/java/com/jbrempresa/backend/controller/CatalogoController.java), [CatalogoDtos](../src/main/java/com/jbrempresa/backend/dto/ventas/CatalogoDtos.java).

## Accesos

| Acceso | Empresa y autorización |
|---|---|
| /catalogo/publico/{token} | Token general, alias o posición resuelven empresa; catálogo publicado. |
| /catalogo/proveedores/{id} | JWT, permiso PROVEEDORES y relación activa/vigente de compradora a proveedora. No devuelve token público. |
| /catalogo/gestion/* | Configuración en la empresa efectiva del contexto autenticado. |

El módulo Clientes obtiene la configuración de su empresa y usa el catálogo público. Proveedores mantiene consulta, pedido e imágenes en endpoints autenticados. Las rutas públicas existentes permanecen públicas.

## Configuración e imágenes

CATALOGO_PUBLICADO, CATALOGO_DOMICILIO, CATALOGO_TOKEN_GENERAL y CATALOGO_ALIAS se guardan en PRODUCTOS. Publicar requiere imagen de empresa. Las posiciones tienen token regenerable y ubicación/fila/columna.

RUTA_IMAGENES se configura por módulo apuntando al directorio común empresarial; ImagenService añade empresa-{id}/tipo. IMAGEN_CATALOGO_ORIGEN selecciona TIPO o REGISTRO para Productos y Servicios. [Adjuntos e imágenes](FICHEROS_IMAGENES.md).

El catálogo muestra registros activos, visibles y no de baja, con avisos vigentes, precios, distintivos y agotado cuando controla stock. No expone rutas físicas. Las URLs de imagen se construyen para el contexto público o protegido correspondiente.

## Pedido común

1. Verificar publicación y modalidad: EN_POSICION requiere posición; DOMICILIO exige habilitación y dirección.
2. Validar nombre/teléfono, contenido de líneas y cantidades; correo opcional.
3. Reutilizar Persona si todos los contactos aportados coinciden; de otro modo crear una ficha mínima incompleta.
4. Crear PED EMITIDO, origen CATALOGO, no pagado, en la empresa que vende.
5. Resolver artículos e importes en servidor. Precio final por cantidad determina el total; base e IVA se derivan.
6. Descontar stock controlado, guardar observaciones y detalles, llamar a asignación y factura automática; sincronizar malla cuando hay posición.
7. Devolver numero, total, modalidad y ubicacion.

El mismo proceso se usa al pedir a un proveedor. No se persiste una compra espejo ni se identifica automáticamente la empresa compradora dentro del documento: los datos del formulario crean/reutilizan una Persona en la proveedora. Véase LIM-05.

## API

El [inventario](API_INVENTARIO.md) recoge configuración, posiciones/QR, imágenes y pedidos. /catalogo/gestion/pago solo devuelve estado de Redsys/Bizum, no cobra. /catalogo/proveedores lista proveedores accesibles. Relación, estado y fechas se vuelven a validar al pedir.

Baja/eliminación de pedido y su cadena siguen el servicio documental, incluidas compensaciones de stock y retirada de tareas/reservas. No son operaciones del catálogo público.

## Límites

La confirmación pública no ofrece todavía todas las acciones previstas de comprobante/nuevo pedido/estimación. La integración de pagos es preparatoria. Vigencia por fechas se aplica al catálogo de Proveedores, mientras el selector de proveedor de Producto usa activa. [Limitaciones](LIMITACIONES.md) y [experiencia frontend](../../frontend/docs/CATALOGO.md).
