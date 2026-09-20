# Experiencia de catálogo

Revisión: 2026-09-15. Fuente: [CatalogoPublico](../src/app/pages/catalogoPublico/catalogoPublico.ts) y [rutas](RUTAS.md).

## Dónde se abre

- Clientes: catálogo de la empresa de sesión.
- Proveedores: catálogo de empresas proveedoras relacionadas; con uno se abre directamente y con varios se selecciona. Sin relaciones se muestra un diálogo.
- /catalogo/:token: acceso público por alias/token o QR.
- Productos > Gestión > Gestión de Catálogo y Servicios > Gestión > Gestión de Catálogo: configuración interna.

En red local el backend se resuelve desde el host del navegador y el puerto 8080. Para acceso externo deben publicarse correctamente ambos servicios; localhost del móvil no apunta al ordenador.

## Presentación y cesta

El componente es común, con adaptación móvil. Muestra empresa/imagen, tipos de producto/servicio, imágenes, nombre, descripción, precio final, distintivos Novedad/Mejor precio/Outlet y agotado. Los tipos de servicio tienen diferenciación visual. Avisos y alertas aparecen antes de las categorías y pueden ocultarse durante la visita.

Añadir/quitar opera por tipo e identificador; Producto y Servicio pueden compartir número. La cesta contiene cantidades y observaciones. Se solicitan nombre/teléfono, correo opcional, dirección para domicilio y observaciones de pedido. El envío bloquea su botón hasta responder. Las validaciones y errores utilizan el diálogo común.

En Proveedores, cambiar de empresa suministradora descarta la cesta y cancela suscripciones previas. No garantiza cancelar un pedido ya recibido por el servidor. Las imágenes se descargan con la directiva genérica imagenHttp y las cabeceras comunes.

## Confirmación y modalidades

EN_POSICION procede de un QR de ubicación. DOMICILIO requiere habilitación y dirección. La confirmación actual muestra número e importe. Cerrar desde proveedor vuelve a módulos; en catálogo público intenta cerrar la ventana y recuperar el foco de su apertura. Un navegador puede impedir cerrar una pestaña abierta manualmente.

Comprobante descargable, repetir pedido y estimación completa son pendientes, no acciones actualmente garantizadas.

## Gestión e imágenes

Configuración de publicación/domicilio, alias, posiciones, QR y vista previa están en Gestión de Catálogo. Las imágenes de registros se gestionan exclusivamente con Adjuntos. IMAGEN_CATALOGO_ORIGEN permite TIPO o REGISTRO por módulo; la imagen principal procede de esa elección. Las nuevas altas mantienen Visible en catálogo según sus formularios.

La norma de creación visual está en DIRECTIVAS. Formatos y límites de servidor en [FICHEROS_IMAGENES.md](../../backend/docs/FICHEROS_IMAGENES.md).

## Qué genera el pedido

El catálogo vende en la empresa mostrada. En Proveedores es la suministradora: el sistema no crea aquí una compra espejo en la empresa del usuario. [Contrato backend](../../backend/docs/CATALOGO.md), [relaciones](RELACIONES_EMPRESA.md), [guía de uso](GUIA_USO.md) y [limitaciones](../../backend/docs/LIMITACIONES.md).
