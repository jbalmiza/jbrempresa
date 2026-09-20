# Avisos y alertas

Revisión documental: 2026-09-17. Describe el árbol de trabajo actual.

Comunicaciones denomina `Registro` a su primer bloque e incorpora `Avisos y Alertas`. Un único componente configurable atiende Registro y Gestión.

- Registro: Consultar, Insertar, Ver, Modificar y Eliminar.
- Gestión: Consultar, Ver, Baja, Reactivar e Histórico.
- Las tablas nacen vacías y solo cargan datos al pulsar `Consultar` o al introducir un filtro.
- Registro no presenta Id Histórico ni los campos de movimiento propios de Gestión en el formulario.
- Las bajas permanecen ocultas en la consulta ordinaria de Gestión. Para seleccionarlas y reactivarlas, se muestra `Tipo Movimiento` desde la configuración de columnas y se filtra por `B`; el histórico sí presenta todos los movimientos.

En el catálogo, los mensajes se muestran antes del menú: avisos en amarillo suave y alertas en rojo suave.

El formulario de Registro añade Emisor, Destinatario y Ubicación. El emisor corresponde al usuario autenticado: Administrador, Jefe o Empleado; el Jefe de una empresa suministradora puede elegir Proveedor cuando existe una relación de proveedor activa. La empresa destinataria se elige para envíos a otra empresa; cuando el Administrador dirige un aviso a Empresa en Ventana o Mensajes puede elegir «Todas las empresas». La ventana se elige de un listado de rutas de Registro y Gestión, incluida la ruta real `/compras/compras` del Registro de Compras. La entrada del módulo Empleados es `/empleados` y su Registro de Avisos y Alertas es `/empleados/avisosAlertas`: son destinos distintos. El Jefe puede publicar en la entrada del módulo; el Empleado envía siempre a la bandeja de Mensajes de su Jefe. El Proveedor puede enviar al Administrador o al Jefe de una compradora relacionada, o publicar en su catálogo de proveedor.

El selector de Ventana incluye las rutas reales de Empleados (`/empleados/agenda` y `/empleados/avisosAlertas`) y el catálogo de sesión de Clientes (`/clientes`). Las rutas de Proveedores se excluyen porque este módulo no crea avisos ni alertas de ubicación Ventana. El empleado redirigido a Mi agenda ve allí tanto los avisos de `/empleados` como los de `/empleados/agenda`. El catálogo público sin sesión conserva únicamente sus avisos de ubicación Catálogo de clientes; los de Ventana `/clientes` se muestran solo en el catálogo de sesión.

En el formulario, la primera fila agrupa Tipo, Ubicación y Ventana cuando corresponde; la segunda agrupa Emisor, Destinatario y Empresa destinataria cuando corresponde; la tercera agrupa Título y Mensaje. Los selectores de Empresa destinataria y Ventana utilizan los estilos comunes de campo. Al insertar, el periodo propuesto abarca desde las 00:00:00 del día actual hasta las 23:59:59 de siete días después, según la fecha local del usuario. Usuario y Fecha Movimiento se muestran inicialmente con el usuario de sesión y la fecha y hora actuales, siguiendo el Registro de Usuarios. Las fechas existentes se conservan al abrir un registro para modificarlo; el servidor establece los datos definitivos de movimiento al guardar.

La tabla de Gestión conserva las filas de presentación entre comprobaciones de Angular. Al seleccionar una fila se recupera el aviso original por empresa e identificador, de modo que la selección y sus acciones usan los datos persistidos sin los campos calculados para la tabla.

Los avisos y alertas de ubicación Ventana se muestran encima del título de la ventana, desde el borde derecho del menú hasta el borde derecho del contenido. El panel deja 12 px respecto a la cabecera y ajusta el contenido para mantener 16 px entre el aviso y el título. La reserva se aplica solo al contenido principal: el menú izquierdo conserva su posición. Cada aviso presenta su título en negrita, seguido del mensaje y el botón para ocultarlo en esa vista; ocupa una fila cuando el texto cabe y se adapta al ancho disponible.

Los avisos con ubicación Mensajes aparecen en el botón y en la bandeja común, con contador y lectura individual, pero sin Responder. Son distintos de las conversaciones clasificadas como Aviso o Alerta. El catálogo de clientes y el catálogo de proveedor consultan ubicaciones distintas del mismo maestro; los registros anteriores permanecen en el catálogo de clientes con emisor Jefe.

Cada mensaje dispone de una X visible. Cerrarlo solo lo oculta en la visita actual del catálogo; no modifica el registro ni su estado en Gestión.
