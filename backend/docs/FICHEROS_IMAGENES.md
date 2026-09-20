# Ficheros, adjuntos e imágenes

Revisión documental: 2026-09-15. Describe el árbol de trabajo actual.

## Criterio y almacenamiento

Se aplica el [catálogo de directivas](DIRECTIVAS.md) y [AGENTS.md](../../AGENTS.md): antes de crear o sustituir imágenes se pregunta el enfoque visual si el usuario no lo ha indicado. Los archivos de un registro se gestionan mediante Adjuntos y su imagen principal; no se añaden cargadores particulares.

La raíz común de imágenes de la empresa se configura mediante RUTA_IMAGENES en ADMINISTRACION, PRODUCTOS y SERVICIOS. El resolver admite valores distintos por módulo; configurar la raíz común mantiene la organización acordada. ImagenService construye la ruta física como raíz/empresa-{id}/tipo/archivo; no añade otro segmento de módulo. Los clientes acceden por API, no mediante rutas locales arbitrarias.

## Adjuntos y principal

AdjuntoController lista, carga, sirve, marca principal y elimina archivos de registros. El [inventario REST](API_INVENTARIO.md) contiene rutas y parámetros exactos. La configuración multipart permite 10 MB por archivo y 11 MB por petición. ImagenService limita imágenes a 5 MB y acepta MIME JPEG, PNG y WebP. Esta comprobación usa el tipo declarado; no equivale a una inspección completa del contenido.

Marcar una imagen principal sustituye la principal anterior y sincroniza la referencia de la entidad compatible. Empresa, Productos y Servicios reutilizan esa imagen en sus vistas y catálogos. Los módulos de aplicación usan su adjunto principal y un recurso incluido si falta.

Los parámetros documentales siguen el tipo de registro; consultar el [manual](../../docs/MANUAL_PARAMETROS_CLIENTE.md). Las imágenes predeterminadas se encuentran en src/main/resources/default-images.

## Acceso y conservación

El backend resuelve empresa, registro y archivo y normaliza nombres. El catálogo público solo sirve sus imágenes publicadas; el catálogo de proveedores usa rutas protegidas y valida también la relación. Deben copiarse conjuntamente base de datos, archivos y claves de cifrado en las copias de seguridad. Ver [operación](OPERACION.md) y [seguridad](SEGURIDAD.md).
