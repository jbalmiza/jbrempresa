# Cambios del frontend

## 2026-09-07 - Identificación de servicios y cierre del catálogo

- Los botones correspondientes a tipos de servicio utilizan un color verde azulado propio para diferenciarlos de los tipos de producto.
- La cabecera del catálogo incorpora el botón `Cerrar`, que recupera el foco de la pestaña de origen cuando está disponible y cierra la pestaña del catálogo.
- Verificado con `npm.cmd run build`.

## 2026-09-07 - Estabilización del módulo de Caja

- Registro de Cajas deja de consultar sesiones: el CRUD del maestro ya no queda bloqueado por errores de la operativa de Gestión.
- Gestión carga las sesiones por separado y muestra mensajes contextualizados para cajas, sesiones, movimientos y personas.
- Se separan las observaciones de apertura y cierre para impedir reutilizaciones accidentales.
- Se validan importes, concepto y cierre antes de invocar la API.
- Los movimientos pueden seleccionarse en la tabla y anularse expresamente mientras la sesión está abierta.
- El formulario de Registro utiliza el componente genérico `Datos Movimiento` con Usuario, Fecha y Activo informativos.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Categorías y pie fijo en Venta táctil

- Los botones conservan únicamente el nombre original del tipo; los servicios se distinguen mediante un color propio.
- El resumen, las observaciones y las acciones permanecen fijos al pie del pedido.
- El panel proyectado ocupa toda la altura lateral, de modo que el espacio libre queda sobre el pie incluso cuando no existen líneas.
- Cuando las líneas exceden el espacio disponible, solo la lista del pedido muestra desplazamiento vertical táctil.
- Verificado mediante compilación de desarrollo y compilación de producción.

## 2026-09-03 - Renovación de sesión por actividad real

- Creado un servicio genérico de actividad de sesión, inicializado una sola vez desde la raíz de la aplicación.
- La interacción mediante puntero, teclado, toque o desplazamiento mantiene la sesión activa con una señal autenticada limitada a una cada cuatro minutos.
- La ausencia de actividad no genera señales, por lo que los 30 minutos pasan a representar inactividad real.
- Verificado con `npm.cmd run build`.

## 2026-09-02 - Búsqueda precisa en selectores

- Corregido el selector común para buscar únicamente en el campo descriptivo y los campos de búsqueda configurados por cada pantalla.
- Eliminada la coincidencia accidental con campos técnicos internos del registro.
- En Recursos, Persona busca por nombre completo, documento, nombre, apellidos y razón social.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Redistribución de cabecera y total del TPV

- Eliminada la cabecera independiente de Venta Táctil para ampliar el espacio vertical del catálogo.
- Integrado `Cerrar` como control independiente y fijo al extremo derecho de la misma fila horizontal de tipos, fuera de su desplazamiento.
- Trasladado el total del pedido al pie del panel, junto a las acciones `Imprimir` y `Registrar pedido`.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Ajustes táctiles de tipos y cantidades

- Retirados los controles de cantidad duplicados del resumen del pedido; las unidades solo se modifican desde la tarjeta del producto.
- Los tipos de producto se distribuyen en filas completas mediante ajuste automático, sin tipos ocultos ni desplazamiento horizontal.
- Aumentado a 28 px el grosor de las barras verticales del catálogo y del pedido, con mayor contraste y tirador táctil.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Cabecera compacta de Venta Táctil

- Movido el buscador de productos a la primera fila de cabecera, alineado con el botón independiente `Cerrar`.
- Establecida una segunda fila de tipos con una paleta azul grisácea diferenciada y coherente con el TPV.
- La cuadrícula calcula una columna por tipo y ajusta ancho, texto y separación para mantener siempre todos los botones en una sola fila, sin desplazamiento.
- Verificado con `npm.cmd run build`.
- Los nombres de tipos se mantienen en una sola fila; si el espacio resulta insuficiente, el texto se recorta sin saltos ni puntos suspensivos.

## 2026-09-03 - Logo y compactación del pedido táctil

- Sustituido el contador de resultados de la cabecera por el logotipo de la empresa a la izquierda del buscador, con el nombre como alternativa si no existe imagen.
- Compactadas al máximo las líneas del pedido: menor separación, tipografía ajustada, cantidad e importe en cabecera y observación de una sola fila.
- Reducido también el espacio vertical del encabezado, observación general y acciones del pedido sin eliminar información funcional.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Orden reciente del pedido táctil

- La lista del pedido se ordena por la selección más reciente, mostrando arriba el último producto incorporado.
- Al incrementar un producto ya presente, su cantidad se actualiza y la línea vuelve a la primera posición.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Búsqueda global de productos en TPV

- La búsqueda de Venta Táctil ignora el tipo seleccionado y consulta todos los productos disponibles.
- Se busca por nombre, descripción, tipo, categoría y subcategoría; al vaciar el texto vuelve a aplicarse el tipo activo.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Teclado reutilizable e imágenes en Venta Táctil

- Creado el componente genérico `teclado-tactil`, reutilizable con campos `input` y `textarea` de cualquier pantalla.
- El teclado aparece al enfocar buscador u observaciones y permite letras, números, `Ñ`, puntuación, mayúsculas, espacio, borrado, salto de línea y cierre.
- La escritura respeta la selección, posición del cursor y longitud máxima del campo enlazado.
- Las tarjetas de producto cargan su imagen autenticada y presentan un indicador visual alternativo si no está disponible.
- Las URLs temporales del logo y de productos se liberan al abandonar la pantalla.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Tarjetas compactas de producto

- Compactadas las tarjetas del catálogo táctil para mostrar más productos simultáneamente.
- Reducidos altura, separación, imagen, tipografía y controles manteniendo un tamaño cómodo para pulsación táctil.
- El nombre admite dos líneas y la descripción se resume en una sola, conservando siempre visibles imagen, precio y cantidad.
- La imagen se utiliza como fondo de toda la zona informativa con una capa clara de contraste, evitando reservarle altura propia y dejando el mismo espacio al texto.
- Verificado con `npm.cmd run build`.
- Reducida la capa superpuesta sobre la fotografía para hacerla claramente visible; el texto pasa a blanco con sombra y el precio incorpora contraste oscuro translúcido.

## 2026-09-03 - Base del módulo de Caja

- Añadido el acceso al módulo Caja, sus rutas, icono y navegación lateral.
- Separado el Registro de Cajas (CRUD del maestro) de la Gestión de Caja (baja/reactivación, apertura, movimientos, cierre e histórico de sesiones).
- La gestión permite movimientos de entrada, salida, cobro y devolución con efectivo, tarjeta, transferencia u otro medio.
- La Persona asociada es opcional, se conserva por `perId` y se presenta mediante el bloque `Datos Persona`; también queda preparada una referencia opcional al documento de venta.
- Verificado con `npm.cmd run build`.

## 2026-09-03 - Icono del módulo de Caja

- Rediseñado el icono para ajustarlo al estilo gráfico del resto de módulos.
- Incorporado un símbolo de euro destacado y legible para identificar visualmente la función de Caja.

## 2026-09-03 - Venta Táctil

- Añadida `Táctil` como primera opción del menú de Ventas.
- Creada una pantalla TPV a pantalla completa con cierre visible, categorías de acceso rápido, buscador y tarjetas grandes de producto.
- Cada toque incorpora una unidad y las repeticiones quedan agrupadas por producto, con observaciones por línea y total en tiempo real.
- Incorporadas las acciones de imprimir el borrador y registrar el pedido emitido a cliente genérico.
- La pantalla está adaptada para interacción táctil y dispone de una presentación específica para impresión.
- Verificado con `npm.cmd run build` y carga de `/ventas/tactil` en localhost.

## 2026-09-03 - Agrupación y navegación de Venta Táctil

- Los productos repetidos se muestran como una única línea del pedido con cantidad acumulada y controles para aumentarla o reducirla.
- La observación se aplica a la línea agrupada y el backend recibe una única línea con la cantidad total.
- Los tipos de producto se presentan en una franja horizontal superior desplazable.
- Ensanchadas las barras de desplazamiento de categorías, catálogo y pedido para facilitar su uso en pantallas táctiles.
- Verificado con `npm.cmd run build`.

## 2026-09-02 - Buscador de Persona en Recursos

- Sustituido el desplegable de Persona por el selector de búsqueda común.
- Permitida la búsqueda por nombre completo, documento, nombre, apellidos y razón social.
- Verificado con `npm.cmd run build`.

## 2026-09-02 - Organización del formulario de Recursos

- Agrupados Id Empresa, Id Recurso e Id Histórico dentro de Datos Identificación.
- Trasladado Código al apartado Datos Recurso.
- El alta muestra desde el inicio la empresa activa de la sesión.
- Verificado con `npm.cmd run build`.

## 2026-09-02 - Gestión versionada de Recursos

- Añadidas las acciones Ver, Modificar, Baja, Reactivar, Histórico y Deshacer a Gestión de Recursos.
- Incorporadas a la tabla las columnas Id Histórico, Tipo Movimiento y Causa Movimiento.
- Las bajas quedan ocultas normalmente y se consultan filtrando `Tipo Movimiento` por `B`, con el aviso común de la tabla.
- Capacidades y Agenda solo están disponibles para recursos vigentes que no estén de baja.
- Verificado con `npm.cmd run build`.

## 2026-09-02 - Separación de Registro y Gestión de Recursos

- Restaurada la acción Eliminar exclusivamente en Registro.
- Confirmado que Baja, Reactivar, Histórico, Capacidades y Agenda solo aparecen en Gestión.
- Verificado con `npm.cmd run build`.

## 2026-09-02 - Reinicio general al repetir una opción del menú

- Al pulsar en el menú lateral la misma pantalla que ya está abierta, se recarga la ruta y se recupera su vista inicial.
- La regla es común a todos los módulos y evita conservar estados secundarios como Agenda, Adjuntos o Histórico.
- La navegación entre opciones diferentes continúa utilizando el enrutador de Angular.
- Verificado con `npm.cmd run build`.

## 2026-09-02 - Primera hora visible en la agenda

- Corregida la etiqueta de la primera franja horaria para que no quede oculta bajo la cabecera fija del calendario.
- Añadido espacio antes de la primera fila para mantener entre `08:00`, `08:30` y las horas posteriores una separación uniforme.
- Todas las etiquetas conservan la misma alineación con las líneas divisorias.
- Verificado con `npm.cmd run build`.

## 2026-09-02 - Agenda de empleados

- Retirada la agenda de Gestión de Personas.
- Incorporada la agenda a Gestión de Recursos para empleados activos.
- La pantalla reutiliza el calendario, los horarios, las excepciones, las reservas y las reprogramaciones existentes, identificando al empleado por `reoId`.
- No se mantiene una vista ni compatibilidad con agendas antiguas de Persona; los datos de prueba incompatibles se eliminan en backend.
- Verificado con `npm.cmd run build` y comprobación HTTP de `http://localhost:4200`.

## 2026-09-01 - Iconos de Servicios y Recursos

- Servicios adopta el fondo azul claro común de los módulos.
- Recursos dispone de un icono propio de persona y engranaje.

## 2026-09-01 - Cabecera del centro de trabajo

- Imagen de empresa trasladada a la barra superior común.
- Día y fecha actuales situados a la derecha.
- Eliminada la banda independiente de saludo.

## 2026-09-01 - Módulos sin paginación

- Se eliminó la paginación y se muestran todos los módulos en filas adicionales.
- Se compactó la franja de Bandeja, Revisión y módulos disponibles.

## 2026-09-01 - Módulo Recursos

- Nuevo módulo con Registro, Gestión y Parámetros.
- Gestión de operatividad y capacidades.

## 2026-09-01 - Diseno futuro de pedidos y agendas

- Se documento la confirmacion con referencia, comprobante y acciones claras.
- Se documentaron empleados, agenda local, agenda personal y recursos.
- No se realizaron cambios funcionales en esta fase.

## 2026-09-01 - Mapa inicial de Gestión de Pedidos

- Gestión de Pedidos abre por defecto la vista Malla, igual que Gestión de Productos.
- Documentado que la malla es un mapa editable y genérico creado por cada empresa según la distribución de su espacio.

## 2026-09-01 - Visualización de consultas remotas

- Corregida la tabla compartida para mostrar directamente la página de registros recibida del servidor cuando utiliza paginación remota.
- El Registro de Usuarios vuelve a mostrar los usuarios existentes; la API ya devolvía los datos correctamente, pero no se copiaban a las filas visibles.

## 2026-09-01 - Parámetros preparados para Bizum

- Los parámetros futuros de Bizum mediante Redsys quedan disponibles dentro de los parámetros del módulo Ventas.
- No se muestra todavía una acción de pago en el catálogo porque la pasarela permanece desactivada hasta completar la integración real.

## 2026-09-01 - Orden del formulario de documentos de venta

- Trasladadas las observaciones de Presupuestos, Pedidos, Albaranes y Facturas desde `Importes` a un apartado propio `Datos Adicionales`.
- Movido `Datos Ubicación` de Pedidos al último apartado del formulario.
- Reducidos los campos de ubicación para mantener Modalidad, Ubicación, Fila y Columna en una sola fila; la dirección de envío se muestra aparte cuando corresponde.

## 2026-09-01 - Colores de Malla y Mapa

- Separados `Malla` y `Mapa` del azul reservado para las acciones de consulta.
- Aplicado violeta a `Mapa` y un morado más intenso a `Malla` en todos los módulos que utilizan estas acciones.

## 2026-09-01 - Saludo de sesión y ajustes del catálogo

- El saludo de la portada se calcula con la hora de inicio de sesión y permanece estable mientras dure esa sesión.
- Mostrada una barra de desplazamiento horizontal en los tipos del catálogo cuando no caben en el ancho disponible.
- Limitada la cesta al mismo ancho máximo de `430 px` utilizado por la ventana del catálogo.

## 2026-09-01 - Formato de teléfono y menú por tipo

- Limitada la presentación completa del catálogo público a `430 px`, manteniendo el ancho disponible en teléfonos más estrechos.
- El menú muestra los tipos de producto y los tipos de servicio proporcionados por el backend.

## 2026-09-01 - Rediseño móvil del catálogo público

- Rediseñado el catálogo como carta digital móvil, con imágenes de producto más visibles y fichas de lectura rápida.
- Sustituido el contador vacío por una acción `Añadir`; el selector de cantidad aparece después de incorporar el producto.
- Mejoradas las categorías táctiles, ocultando la barra de desplazamiento, y añadida una barra inferior de pedido con unidades y total.

## 2026-09-01 - Imagen de empresa en la portada

- Mostrada la imagen principal de la empresa activa a la izquierda del saludo de acceso a módulos.
- La imagen se obtiene desde Adjuntos y se oculta si la empresa no tiene una imagen principal disponible.

## 2026-09-01 - Parámetros generales e imagen de empresa

- Retirado el botón Parámetros del Registro de Empresas.
- Eliminado `EMPRESAS` como módulo de parametrización.
- La ruta general de imágenes de empresa se gestiona en `Administración > Parametrización > Parámetros`.

## 2026-09-01 - Formulario de posiciones QR

- Corregido el ancho de Ubicación, Fila, Columna y Añadir para impedir que los controles se solapen.
- Conservada la disposición en una fila en escritorio y apilada en pantallas estrechas.

## 2026-09-01 - Rutas de imágenes en Parámetros

- Retiradas las rutas técnicas de Productos, Servicios y Empresas de la configuración del Catálogo.
- `RUTA_IMAGENES` se mantiene en `Utilidades > Parámetros` de Productos y Servicios.
- La ruta de imágenes de empresa pertenece a los parámetros generales de Administración.
- Ampliado el selector administrativo con los módulos Empresas, Servicios y Comunicaciones.

## 2026-09-01 - Alineación de la gestión del catálogo

- Corregido el contenedor de la pantalla para situar el Catálogo a la derecha del menú lateral desde la parte superior.
- Añadidos al componente sus estilos de layout y la adaptación para pantallas estrechas.

## 2026-09-01 - Información emergente de la malla

- Eliminado el tooltip nativo anterior en las posiciones ocupadas para evitar dos ventanas simultáneas.
- La vista previa nueva muestra imagen, nombre, stock actual, fila y columna del producto.
- Las posiciones libres mantienen la indicación sencilla de fila y columna.

## 2026-08-31 - Selección de malla al modificar productos

- Verificado el envío de fila y columna seleccionadas desde el formulario de producto.
- La corrección necesaria se aplicó en el orden de persistencia histórica del backend, sin modificar el contrato del selector.

## 2026-08-31 - Imagen principal gestionada desde Adjuntos

- Retirados el selector y la previsualización de imagen de los formularios de Productos, Gestión de productos, Servicios y Empresas.
- Adjuntos muestra miniaturas de los archivos de imagen y permite seleccionar una sola imagen principal por registro.
- Añadido el apartado Adjuntos al registro de Empresas con la misma operativa.
- La malla de productos muestra al pasar el ratón una vista ampliada de la imagen principal y el nombre del producto.
- Verificado con `npm.cmd run build`.

## 2026-08-31 - Selector y previsualización de imágenes

- Movido el selector de imagen a Datos Identificación en Productos, Gestión de productos, Servicios y Empresas.
- La imagen seleccionada se previsualiza inmediatamente desde el dispositivo.
- La carga se ejecuta después de guardar o actualizar, permitiendo seleccionar imagen también durante un alta.
- Añadida validación previa de formato JPEG, PNG o WebP y tamaño máximo de 5 MB.

## 2026-08-31 - Imágenes y servicios en catálogo

- Añadida vista previa de imagen en los formularios de Productos y Servicios.
- Servicios incorpora imagen, `Visible en catálogo` en Datos Adicionales y Causa Movimiento de 200 px.
- Las nuevas altas quedan visibles y reciben imagen predeterminada.
- El catálogo móvil permite seleccionar conjuntamente productos y servicios.
- Añadida la ruta independiente de imágenes de Servicios.

## 2026-08-31 - Edición de ubicación de productos

- Corregida la carga de ubicación, fila y columna al abrir un producto en modo modificación.
- Restaurados los colores e iconos predeterminados de Guardar, Actualizar y Cancelar en Gestión de productos.
- Reubicado `Visible en catálogo` en Datos Adicionales y reducido el ancho de Causa Movimiento para mantener los datos de movimiento en una sola fila de escritorio.

## 2026-08-31

- Añadido catálogo público sin autenticación y diseñado para móvil.
- Añadidas categorías, productos con imagen, cesta, cantidades y productos agotados.
- Añadidas observaciones por línea y por pedido.
- Añadida identificación simplificada mediante nombre y teléfono.
- Añadidas modalidades en posición y envío a domicilio.
- Añadida confirmación final con número e importe del pedido.
- Añadida gestión del catálogo en Productos > Utilidades.
- Añadidas configuración, posiciones, descarga y regeneración de QR y vista previa.
- Añadidos imagen principal e indicador de visibilidad a Productos.
- Añadida imagen principal a Empresa.
- Añadidos origen, modalidad, ubicación y dirección de envío a la visualización interna de pedidos.
- Añadido campo Ubicación junto a Fila y Columna en Datos Ubicación de pedidos.
- Actualizada la URL de API para funcionar desde dispositivos de la red local.
- Verificado con `npm.cmd run build`.
## 2026-09-02 - Identificación visible de personas

- Todos los selectores de Persona muestran y buscan exclusivamente por `perNomCom`, con formato `DNI/CIF - nombre y apellidos` o `DNI/CIF - razón social larga`.
- El valor seleccionado y persistido en Recursos y en el resto de relaciones continúa siendo `perId`; no se duplica el nombre visible en las entidades relacionadas.
- Sustituido el desplegable simple de Persona en Personal de Área por el selector con búsqueda común.
- Verificado con `npm.cmd run build`.
## 2026-09-02 - Apartado común Datos Persona

- Creado un bloque reutilizable para relaciones con Persona que mantiene `perId` como valor relacionado y muestra `perNomCom` al usuario.
- Al seleccionar una Persona se muestran en solo lectura la dirección completa de su domicilio asociado, su teléfono y su correo.
- Aplicado el apartado independiente `Datos Persona` en Recursos, Personal de Área, Compras, Usuarios y complementos de Persona.
- Completado `Datos Cliente` de documentos de venta con teléfono y correo; ya mostraba Persona, documento y domicilio.
- Revisados los flujos de Comunicaciones: conservan sus apartados específicos de identificación porque no mezclan la Persona con los datos del registro.
- Corregidos los selectores de comprador y vendedor para que emitan el campo relacional real `perId`.
- Verificado con `npm.cmd run build`.
## 2026-09-02 - Estilo de Datos Persona

- Corregida la encapsulación visual del bloque reutilizable para que etiquetas y controles mantengan la disposición vertical, anchos, bordes, alturas y colores de solo lectura del resto de formularios.
- La distribución de dirección, teléfono y correo se adapta a pantallas estrechas.
- Verificado con `npm.cmd run build`.
## 2026-09-02 - Recurso sin código y tamaños de Datos Persona

- Eliminado el campo Código del formulario, tabla, validaciones e interfaz de Recursos.
- En el bloque reutilizable `Datos Persona`, la dirección conserva el tamaño largo y teléfono y correo usan el tamaño medio en una misma fila.
- El formato se aplica a Recursos, Personal de Área, Compras, Usuarios y complementos de Persona mediante el componente común.
- Verificado con `npm.cmd run build`.
## 2026-09-02 - Tamaño de Causa Movimiento

- Normalizado `Causa Movimiento` como campo mediano (`form-campo-200`) en todos los apartados `Datos Movimiento` que lo incluyen.
- Corregidos Recursos, Personas y Domicilios; Productos y Servicios ya utilizaban el tamaño mediano.
- Revisados los restantes apartados `Datos Movimiento`, que no contienen campo de causa.
- Verificado con `npm.cmd run build`.
## 2026-09-02 - Persona visible en Recursos

- La tabla y el histórico de Recursos muestran `perNomCom` en la columna Persona en lugar del identificador numérico `perId`.
- La relación y el contrato continúan conservando `perId`; `personaNomCom` es un dato calculado exclusivamente para presentación.
- Revisado el formulario: al consultar o modificar un empleado muestra el nombre completo en el selector y, mediante el bloque `Datos Persona`, domicilio completo, teléfono y correo.
- La carga inicial espera al maestro de Personas antes de presentar Recursos, evitando que la columna aparezca vacía por una respuesta asíncrona tardía.
- Verificado con `npm.cmd run build`.
## 2026-09-02 - Presentación general de relaciones con Persona

- Establecida como regla general la persistencia de `perId` y la presentación de `perNomCom` en tablas y formularios relacionados.
- Corregidas las tablas de Recursos, Compras, Personal de Área, Usuarios y Documentos de Venta para sustituir los identificadores de Persona por sus nombres completos.
- Las tablas de representantes, domicilios de notificación y domiciliaciones ya resolvían el nombre completo; en Contactos de canal se retiró la columna numérica redundante.
- Los formularios correspondientes usan `Datos Persona` con nombre completo, domicilio, teléfono y correo; Documentos de Venta conserva su equivalente funcional `Datos Cliente`.
- La tabla maestra de Personas mantiene su propio `perId` porque en ese caso es el identificador del registro consultado, no una relación externa.
- Verificado con `npm.cmd run build`.
# 2026-09-03 - Componentes genéricos de formularios e interacción

- Las categorías de Venta táctil identifican expresamente el origen mediante `Productos · Tipo` y `Servicios · Tipo`, evitando que un tipo de servicio parezca una categoría de producto.

- Venta táctil carga conjuntamente productos y servicios activos y visibles en catálogo.
- La búsqueda recorre ambos orígenes y las categorías se construyen con el tipo/categoría de cada concepto.
- `tarjetaProductoTactil` se sustituye por el componente genérico `tarjetaConceptoTactil`.
- Las líneas mantienen una clave compuesta por tipo e identificador, evitando colisiones cuando Producto y Servicio comparten número.
- Al registrar se envía `dvdTipLin=P` con `proId` o `dvdTipLin=S` con `serId`; precio, descuento, IVA, cantidad y observaciones se conservan para ambos.
- Verificación: compilación Angular de producción correcta.

- Completada la adopción de `BlobUrlUtil` en Supbar, Documentación adjunta, Mallas y Tabla.
- La carga de logotipos, vistas previas, apertura y descarga de adjuntos, previsualización de mallas y exportación CSV liberan ahora sus URLs temporales mediante una única utilidad.
- Verificación: las llamadas directas a `URL.createObjectURL` y `URL.revokeObjectURL` quedan confinadas a `BlobUrlUtil`; compilación Angular de producción correcta.

- Se crea `pantallaTactil` como contenedor reutilizable de pantalla completa con logo, buscador, cierre, navegación, contenido principal, panel lateral y accesorios.
- Las zonas específicas se aportan mediante proyección de contenido; el contenedor no depende de productos, pedidos ni ventas.
- Venta táctil pasa a consumir el contenedor manteniendo sus categorías, catálogo, pedido, totales y teclado genérico.
- El contenedor incorpora la adaptación estructural para escritorio, móvil e impresión.

- Se extrae `tarjetaProductoTactil` como componente reutilizable, independiente del pedido y de los documentos de venta.
- La tarjeta recibe Producto, precio, cantidad e imagen de fondo y emite las acciones Añadir y Quitar.
- Se conserva el diseño compacto y táctil; el nombre se limita visualmente a una sola fila, sin puntos suspensivos.
- Venta táctil mantiene búsqueda, categorías, cantidades, observaciones, totales y registro del pedido.

- El menú lateral pasa de una plantilla repetida por módulo a la configuración tipada `MENUS_MODULOS`.
- La configuración contiene exclusivamente títulos, grupos, opciones y rutas; no se incorporan permisos porque actualmente la aplicación no dispone de ellos.
- Se conservan todas las opciones existentes, incluidas las opciones todavía inactivas, que ahora se muestran expresamente sin navegación.
- La ruta activa queda resaltada y su grupo se abre automáticamente; repetir la ruta actual conserva el refresco completo existente.

- Se incorpora la directiva genérica `barraAcciones` a las barras existentes de 20 pantallas.
- La barra aporta semántica accesible de toolbar y normaliza el orden Consultar, Insertar, Ver, Modificar, Eliminar, Baja/Reactivar, Histórico, Guardar/Actualizar, Cancelar y Volver.
- Las acciones específicas quedan después de las acciones normalizadas y conservan sus condiciones y manejadores en la pantalla consumidora.
- Decisión: no se fuerza un catálogo CRUD sobre barras internas de agenda, caja, comunicaciones, mapas o históricos; comparten contenedor y orden sin perder su comportamiento propio.
- Verificación: compilación Angular de producción correcta y 20 pantallas consumidoras detectadas.

- Sustituidos los avisos y confirmaciones nativos por el componente global `dialogosAplicacion` y las funciones comunes de interacción.
- Los avisos informativos se cierran automáticamente a los cuatro segundos; los errores permanecen hasta cerrarlos.
- Las confirmaciones esperan la decisión del usuario en un modal; las operaciones destructivas se identifican mediante el botón rojo.
- Escape y pulsación sobre el fondo cancelan la confirmación activa.
- Verificación: no quedan llamadas directas a `alert()` o `confirm()` en el código TypeScript y la compilación de producción finaliza correctamente.

- Directiva funcional: Registro no tiene histórico y muestra en Datos Movimiento solo Usuario, Fecha y Activo; Gestión añade Tipo y Causa. Todos estos campos son de solo lectura.
- Finalizada la migración de todos los bloques manuales `Datos Identificación` y `Datos Movimiento` de los formularios existentes a los componentes genéricos.
- En componentes compartidos por rutas de Registro y Gestión, la configuración cambia con el modo de la ruta; Productos y Domicilios aplican la misma distinción en sus pantallas separadas.

- Se crean los componentes reutilizables `datosIdentificacion` y `datosMovimiento` para mantener una presentación uniforme de los bloques comunes.
- `datosIdentificacion` recibe una lista de etiqueta/valor y evita duplicar campos de identificadores deshabilitados.
- `datosMovimiento` admite tipo, causa, usuario, fecha y activo, permite ocultar campos no aplicables y proyectar campos propios de una entidad.
- Recursos, Servicios, Áreas organizativas, Parámetros y Personal por área pasan a consumir los componentes comunes aplicables; el campo específico `Operativo` de Recursos se conserva como contenido proyectado.
- Se crea `DialogosService` como punto único para avisos, confirmaciones y normalización de errores de API. Recursos y Servicios quedan migrados inicialmente.
- Se crea `BlobUrlUtil` para crear, descargar y liberar de forma segura URLs temporales. Venta táctil y la descarga de QR del catálogo dejan de gestionar estas URLs directamente.
- Decisión funcional: los componentes comunes contienen presentación y comportamiento transversal; las reglas de negocio y acciones propias permanecen en cada pantalla.
- Se aplaza la generalización del motor CRUD, los históricos y el contenedor completo de venta táctil hasta revisar sus diferencias funcionales.
- Verificación: compilación de producción de Angular mediante `npm.cmd run build`.

## 2026-09-07 - Menú inicial y estilos de bloques comunes

- Al entrar en un módulo, el menú lateral abre de forma determinista su segundo grupo, correspondiente a Gestión; al visitar una opción concreta se mantiene abierto el grupo que contiene la ruta activa.
- Revisada la configuración de todos los módulos: Administración, Territorio, Personas, Productos, Servicios, Ventas, Comunicaciones, Recursos, Caja y Compras disponen de un segundo grupo de gestión o estructura principal.
- `datosIdentificacion` y `datosMovimiento` cargan ahora la hoja de estilos compartida de formularios, por lo que sus tarjetas, filas, etiquetas, campos deshabilitados y tamaños coinciden con los demás apartados de cada ventana.
- Decisión funcional: la entrada general al módulo prioriza Gestión, pero una ruta interna prioriza la visibilidad de su opción activa.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`; permanecen únicamente los avisos previos de presupuesto CSS y dependencias CommonJS.

## 2026-09-07 - Posición y color de la acción Ver

- La barra de acciones genérica coloca `Ver` inmediatamente después de `Consultar` y antes de `Insertar` en todas las pantallas consumidoras.
- La acción `Ver` adopta de forma permanente la paleta gris común, conservando su icono de visualización y sus estados interactivos.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`.

## 2026-09-07 - Homogeneización del CRUD de tipos e imágenes vectoriales

- El Registro de Tipos de Producto y de Servicio adopta como referente el CRUD de Productos: tabla común seleccionable, formulario separado, datos de identificación y movimiento, y barra de acciones estándar.
- El botón `Ver` aparece en gris y antes de `Insertar`; se mantienen Consultar, Modificar, Eliminar y Volver con los estilos compartidos.
- Las imágenes activas de todos los tipos se sustituyen por ilustraciones SVG ligeras; los PNG anteriores permanecen conservados en el mismo directorio para un posible uso futuro.
- Se mantiene en las tarjetas el texto previo y el identificador superpuesto sobre la esquina superior izquierda de la imagen.
- Verificación: compilación Angular de producción correcta; solo permanecen los avisos previos de presupuesto y dependencias CommonJS.
- Gestión de Catálogo adopta la organización de Gestión de Productos: título común y una única barra con `Configuración`, `Volver`, `Códigos QR` y `Vista previa`, sin separar Volver en la cabecera.

## 2026-09-07 - Selectores de tipo y adjuntos como vía única

- `Tipo Producto` y `Tipo Servicio` dejan de ser texto libre y pasan a ser selectores alimentados por los maestros activos de la empresa.
- El cambio se aplica tanto a Registro como a Gestión de Productos y Servicios.
- Se elimina del formulario de tipos el campo particular para cargar imágenes.
- El Registro de Tipos incorpora la acción estándar `Adjuntos` y reutiliza `documentacionAdjunta`, permitiendo marcar una imagen como principal.
- Directiva confirmada: cualquier imagen o documento asociado a un registro debe gestionarse exclusivamente mediante el botón genérico `Adjuntos`.
- El botón `Adjuntos` queda disponible también en Registro de Productos y Registro de Servicios, no únicamente en sus pantallas de Gestión.

## 2026-09-07 - Identificación obligatoria y títulos sin descripción

- `datosIdentificacion` garantiza automáticamente `Id Empresa` y normaliza la antigua etiqueta `Empresa`.
- Registro muestra Id Empresa e identificador del maestro; Gestión añade Id Histórico en Personas, Productos, Servicios, Recursos y Domicilios cuando existe versionado.
- Registro de Tipos muestra ahora Id Empresa e Id Tipo.
- Se eliminan todas las descripciones introductorias situadas debajo del título de las ventanas de Servicios, Ventas, Áreas Organizativas, Personal de Área y Comunicaciones.

## 2026-09-07 - Vistas normal y móvil de Gestión de Catálogo

- Los botones de navegación de Gestión de Catálogo adoptan los estilos generales de las demás pantallas, incluyendo estado activo y acción Volver.
- La vista previa incorpora dos acciones independientes y ambas se abren en una pestaña nueva: `Vista normal del catálogo` aprovecha de forma adaptable el ancho y alto disponibles, mientras `Vista móvil` presenta el contenido con un ancho de 430 píxeles y una altura mínima de 850 píxeles.
- El catálogo público deja de estar limitado siempre al formato móvil y distribuye sus tarjetas en columnas según el ancho disponible en la vista normal.
- El encabezado de la pantalla se homogeneiza como `GESTIÓN DE CATÁLOGO`, sin descripción adicional.
- Ambas acciones utilizan el mismo enlace público y no duplican la implementación del catálogo; la diferencia se limita al contexto visual de apertura.
- Verificación: compilación Angular de producción correcta; quedan avisos de presupuesto del paquete/CSS y dependencias CommonJS ya identificadas.
- Configuración, Códigos QR, Vista previa y Volver se organizan en una única barra bajo el título, siguiendo la estructura visual del resto de pantallas de gestión.

## 2026-09-07 - Configuración global reutilizable de agendas

- Se extrae la edición de hora inicial, preparación, limpieza y horario semanal a `configuracionAgenda`, compartido por la agenda individual y Gestión de Agendas.
- Gestión de Agendas incorpora el botón `Configurar`; el guardado aplica la misma configuración y sustituye el horario semanal de todos los empleados de la empresa.
- Tras confirmar y completar el guardado global, la pantalla vuelve automáticamente al calendario de Gestión de Agendas y se posiciona al inicio de la vista actualizada.
- Corregida la referencia de empleados entregada al configurador global: ahora permanece estable y solo se actualiza al recargar las agendas, evitando peticiones repetitivas de horarios y el error genérico de operación.
- Las validaciones y errores del configurador de agendas se muestran también mediante las ventanas emergentes del sistema común de diálogos.
- La configuración semanal se compacta en dos columnas en pantallas amplias, reduce separaciones y conserva una columna adaptable en tamaños menores; los errores dejan de duplicarse dentro del formulario.
- Añadida una prueba de Gestión de Agendas que comprueba la representación del horario de 19:30 a 23:30 con preparación de 19:30 a 20:00 y limpieza de 23:00 a 23:30.
- La rejilla global deja de forzar el rango fijo 08:00–20:00 y utiliza la hora inicial configurada y el final del horario semanal.
- Preparación y limpieza se interpretan dentro del horario registrado, tanto en la agenda individual como en Gestión de Agendas.
- Al guardar la configuración global se muestra confirmación emergente, se activa explícitamente el calendario, se recargan los datos y se reposiciona la página en su encabezado.
- Verificación: compilación Angular correcta y prueba específica de Gestión de Agendas superada. La ejecución completa detecta además una prueba inicial obsoleta en `app.spec.ts`, ajena a esta funcionalidad.
- La operación masiva exige confirmación explícita indicando el número de empleados afectados. La configuración individual continúa afectando únicamente al empleado seleccionado.
- Las excepciones fechadas permanecen en la agenda individual porque representan incidencias particulares y no forman parte del horario común masivo.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`.

## 2026-09-07 - Gestión de Catálogo en el menú de Productos

- La opción `Catálogo` deja de pertenecer a `Utilidades` y se incorpora a `Gestión de Productos` con el nombre `Gestión de Catálogo`.
- Se conserva la ruta `/productos/catalogo` y su funcionalidad actual; al acceder queda desplegado el grupo de Gestión correspondiente.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`.

## 2026-09-07 - Gestión global de agendas de empleados

- Se añade `Gestión de Agendas` dentro del grupo `Gestión de Recursos` y la ruta protegida `/recursos/gestionAgendas`.
- La nueva pantalla ofrece una vista diaria global de consulta con horas en filas y empleados en columnas, reutilizando los recursos, horarios, excepciones y reservas de la agenda existente.
- Se incorporan navegación por día anterior, hoy, día siguiente y selector de fecha; la vista muestra siempre un único día.
- Se permite cambiar el intervalo entre 5, 15, 30 y 60 minutos, buscar empleados y desplazarse entre bloques de seis empleados.
- Las cabeceras de empleados y la columna de horas permanecen visibles durante el desplazamiento; las barras vertical y horizontal tienen mayor grosor para facilitar el uso táctil.
- Se mantienen los estados visuales comunes: disponible, ocupado, preparación o limpieza y no disponible. Al seleccionar un tramo se muestra su detalle y las reservas coincidentes sin permitir modificaciones.
- Decisión funcional: esta primera versión es exclusivamente de consulta y no sustituye la agenda individual usada para configurar horarios o gestionar reservas.
- La cuadrícula incluye todos los empleados vigentes registrados: cuando uno todavía no tiene agenda configurada se muestra expresamente como `Sin configurar` y sus tramos aparecen no disponibles, en lugar de ocultarlo.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`; se conservan únicamente los avisos previos de presupuesto CSS y dependencias CommonJS.

## 2026-09-07 - Previsualización de auditoría en Registro de Cajas

- El componente genérico `datosMovimiento` incorpora una previsualización reutilizable del movimiento que muestra el usuario conectado y la fecha y hora actuales.
- El formulario de Cajas activa esta previsualización al insertar y modificar; en modo Ver continúa mostrando el usuario y la fecha del último movimiento persistido.
- Los campos permanecen deshabilitados y el backend continúa siendo la fuente definitiva de los datos de auditoría al guardar.
- Las fechas de movimiento se presentan con formato local español e incluyen segundos.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`.
## 2026-09-07 - Tipos de producto y servicio con imagen genérica

- Se incorporan CRUD reutilizables para Tipos de Producto y Tipos de Servicio, aislados por empresa.
- Cada tipo permite cargar una imagen genérica reutilizada por todas sus tarjetas del catálogo.
- Se conserva sin cambios el texto anterior de cada tarjeta: subtipo o `Servicio`, nombre, descripción y precio.
- El identificador se incorpora como una insignia discreta en la esquina superior izquierda de la zona de imagen.
- La imagen individual se sustituye por la imagen genérica del tipo de producto o servicio.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`.

## 2026-09-07 - Catálogo compartido entre Productos y Servicios

- Incorporado `Gestión de Catálogo` al grupo `Gestión de Servicios`; accede al mismo componente y a las mismas funciones de configuración, códigos QR y vistas previas usadas desde Productos.
- El componente adapta el menú lateral y el regreso al módulo desde el que se abrió, sin duplicar la gestión del catálogo.
- Las tarjetas de productos y servicios continúan usando una única plantilla visual común.
- Los botones de tipos se ordenan siempre con todos los productos a la izquierda y todos los servicios a continuación.
- Los conceptos visibles dentro de cada tipo se ordenan por identificador ascendente.
- Verificación: compilación Angular de producción correcta; permanecen únicamente los avisos previos de presupuesto CSS y dependencias CommonJS.
