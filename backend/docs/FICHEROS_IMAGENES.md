# Ficheros, adjuntos e imagenes

## Principio de almacenamiento

Cada modulo define mediante Parametros la ruta base de sus imagenes. No hay una unica ruta global impuesta a todos los modulos. Dentro de la ruta se separan los ficheros por empresa, modulo y tipo de entidad, evitando mezclar imagenes de empresas distintas.

Estructura conceptual:

```text
<ruta-del-modulo>/empresa-<emp_id>/<modulo>/<entidad>/<archivo>
```

La ruta exacta la resuelve `ImagenService`; los clientes nunca deben enviar una ruta fisica arbitraria para leer un archivo.

## Adjunto

`AdjuntoController` trabaja con `modulo`, `entidad` y `registroId`:

- `GET /adjuntos`: lista metadatos del registro.
- `POST /adjuntos`: carga multipart; maximo global 10 MB por archivo y 11 MB por peticion.
- `PUT /adjuntos/{id}/principal`: marca una imagen como principal.
- `GET /adjuntos/{id}/contenido`: devuelve el binario con su tipo de contenido.
- `DELETE /adjuntos/{id}`: elimina la asociacion y el fichero gestionado.

El backend valida empresa, entidad y registro antes de servir o cambiar un adjunto.

## Imagen principal

La seleccion se realiza en el apartado Adjuntos, no en los formularios principales. Al marcar una imagen:

1. se verifica que es una imagen y pertenece al registro;
2. se desmarca la principal anterior;
3. se marca el adjunto elegido;
4. se sincroniza la ruta/campo de imagen de empresa, producto, servicio u otra entidad compatible.

La misma imagen se usa en catalogo, cabecera de empresa y tooltip de malla. Productos y servicios sin imagen propia pueden usar los recursos predeterminados incluidos en `src/main/resources/default-images`.

## Reglas de seguridad

- No exponer rutas locales en respuestas publicas.
- Normalizar nombres y evitar secuencias de traversal.
- Comprobar el tipo real/permitido y no confiar solo en la extension.
- Resolver siempre el fichero dentro de la ruta configurada y empresa activa.
- No reutilizar un adjunto de otra empresa o registro.

## Entidades actuales con imagen

- Empresa: identidad visual de la aplicacion y catalogo.
- Producto: ficha, catalogo y malla.
- Servicio: ficha, catalogo y malla cuando corresponda.

Si otro modulo incorpora imagen, debe añadir su propio parametro de ruta y adoptar el mismo flujo de Adjuntos e imagen principal.
