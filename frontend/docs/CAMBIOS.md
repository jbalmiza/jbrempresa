# Cambios del frontend

## 2026-09-20 - Eliminación de Venta Táctil

- Tras comprobar consumidores y dependencias, eliminadas las rutas de Empleados y Ventas, sus accesos de menú, la pantalla Venta Táctil y los componentes exclusivos de teclado, tarjeta y contenedor táctiles.
- Pedidos por catálogo, Gestión de ventas, agenda, facturación y avisos no dependen de esta pantalla y permanecen operativos. El filtro de destinos de avisos deja de contemplar una ruta inexistente.
- Verificación: búsqueda completa de referencias activas, compilación Angular y revisión de reutilización, rutas, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Reorganización del menú de Empleados

- El módulo Empleados queda dividido en `Registro`, con Pedidos, Avisos y Alertas y Táctil como última opción, y `Gestión`, con Gestión de agenda y Gestión de ventas.
- Pedidos reutiliza el catálogo de clientes existente; Gestión de ventas reutiliza la consulta de ventas del empleado. Gestión de agenda continúa siendo la entrada predeterminada del empleado.
- Verificación: compilación Angular, comprobación de rutas y selección predeterminada, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Separación automática de unidades personalizadas

- Al personalizar una línea con cantidad superior a uno, la cesta separa automáticamente una unidad y aplica los cambios solo a ella. Las unidades restantes conservan la composición anterior.
- Los controles de cantidad de la cesta actúan sobre su línea concreta y las nuevas unidades añadidas desde el catálogo se agrupan con la línea estándar sin personalización.
- Verificación: compilación Angular, revisión del flujo de tres unidades con una personalizada, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Resumen de componentes en observaciones

- Al aplicar una personalización, las observaciones de la línea muestran brevemente los componentes quitados y añadidos por nombre. Las observaciones escritas por el cliente se conservan y el resumen se sustituye al volver a personalizar.
- Verificación: recompilación Angular activa y revisión de facilidad de uso, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Selector de componentes compacto

- La personalización presenta cada componente en una sola fila con selección, nombre, estado incluido y precio adicional visible incluso cuando es cero. Los alérgenos se retiran de este selector y continúan disponibles en la consulta específica del producto.
- Verificación: recompilación Angular activa y revisión de adaptación móvil, facilidad de uso, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Personalización de componentes en el pedido del catálogo

- Cada producto del pedido incorpora el botón `Personalizar`. La ventana permite buscar, añadir o quitar cualquier componente activo de la empresa, identifica los incluidos, muestra suplementos y alérgenos, permite restablecer la composición y resume los cambios en la línea.
- El total visible incluye los suplementos de los componentes añadidos; quitar componentes no reduce el precio. La selección se aplica a todas las unidades de la línea.
- Verificación: compilación Angular, adaptación de la ventana al ancho móvil y revisión de reutilización, facilidad de uso, aislamiento empresarial, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Cabecera compacta del catálogo

- Reducidos la altura, el relleno y la imagen de la cabecera del catálogo en escritorio. La vista móvil real y simulada conserva sus dimensiones anteriores para mantener legibilidad y superficie táctil.
- Verificación: recompilación Angular activa y revisión de adaptación móvil, coherencia visual, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Táctil como última opción de Ventas en Empleados

- Reordenado el submenú Ventas del módulo Empleados: Catálogo de clientes, Registro de ventas y Táctil.
- Verificación: recompilación Angular activa y revisión de Directiva 1, coherencia de navegación y revisión obligatoria.

## 2026-09-20 - Consulta de alérgenos en el catálogo

- Los productos con alérgenos heredados de sus componentes muestran un botón compacto `Alérgenos` en su tarjeta y un detalle consultable. Los productos sin alérgenos y los servicios no muestran el control.
- Verificación: compilación Angular y revisión de las directivas de reutilización, adaptación móvil, funcionalidad general y revisión obligatoria.

## 2026-09-19 - Recálculo inmediato de Total Compra al cambiar IVA

- Alcance: modificar IVA Compra recalcula inmediatamente Total Compra en Registro y Gestión de Productos.
- Verificación: revisión del enlace del evento y cálculo 10 con descuento 10 % e IVA 20 % = 10,80.

## 2026-09-19 - IVA incluido en Total Compra

- Alcance: Total Compra aplica el descuento y suma el IVA de compra; el beneficio continúa calculándose sobre precios sin IVA.
- Verificación: prueba dirigida con precio 10, descuento 0 e IVA 21, cuyo total es 12,10.

## 2026-09-19 - Aplicación directa del beneficio en Productos

- Alcance: el IVA de compra no interviene en Total Compra ni en el cálculo del beneficio. Con beneficio 20 %, el botón de compra convierte 10 en venta 12 y el botón de venta convierte 10 en compra 8. Los botones permanecen disponibles con el valor inicial 20 mientras se materializa el parámetro de empresa.
- Verificación: prueba dirigida de ambos botones y compilación Angular.

## 2026-09-19 - Precios sin IVA y beneficio del 20 % en Productos

- Alcance: Precio Compra y Precio Venta se introducen sin IVA. Ambos descuentos usan el mismo ancho. Los botones aplican o invierten el porcentaje de beneficio de la empresa; con compra 10, descuentos 0 y beneficio 20, la venta resultante es 12. El IVA se aplica únicamente al calcular cada total.
- Verificación: compilación Angular, prueba dirigida 10 → 12 y cálculo inverso.

## 2026-09-18 - Cálculo de beneficio mediante botones en Productos

- Alcance: Datos Importes Compra muestra Precio, Descuento, IVA y Total Compra; Datos Importes Venta muestra Precio, Descuento, IVA y Total Venta. Los totales se actualizan dentro de cada apartado. El cálculo del margen entre apartados deja de ejecutarse al editar campos: «Aplicar beneficio a venta» calcula desde la compra y «Aplicar beneficio a compra» calcula desde la venta. Los precios pueden fijarse independientemente sin pulsar los botones. El coste calculado desde la venta se identifica como estimado.
- Verificación: compilación Angular, cálculos directos e inversos con IVA y descuentos distintos, comprobación documental.

## 2026-09-18 - Posición y ancho uniformes de notas en formularios

- Alcance: las notas de Control de stock pasan antes de los campos en Registro y Gestión de Productos y ocupan el ancho del apartado. Se coloca también antes de los campos la información de Publicación del catálogo y Datos Cliente de facturas normales. FE-DIR-051 fija posición, clases, icono, ancho y separación para notas presentes y futuras.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Ancho y separación de notas informativas en Productos

- Alcance: las notas de importes y margen ocupan el ancho del apartado y dejan espacio antes de los campos. FE-DIR-051 incorpora el criterio de ancho y separación.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Estilo común de la información en formularios

- Alcance: las explicaciones de importes y margen en Registro de Productos utilizan el aviso informativo común. FE-DIR-051 establece este estilo para todos los formularios.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Importes de compra y venta de productos con margen objetivo

- Alcance: Registro de Productos separa importes de compra y venta, acepta precios con IVA incluido, muestra IVA de compra y venta y explica que el margen bruto objetivo se calcula sobre la venta neta tras descuento. El parámetro `PRODUCTOS / MARGEN_BRUTO_OBJETIVO` se configura por empresa; con el control activo, cambiar compra calcula venta y cambiar venta estima compra. Al desactivarlo ambos precios son independientes. El origen estimado del coste se indica en el formulario. La base de venta se conserva sin IVA para los flujos existentes y las tablas presentan también su importe con IVA.
- Verificación: compilación Angular y cálculos directos e inversos con IVA y descuento.

## 2026-09-18 - Directiva de funcionalidad general para todas las empresas

- Alcance: se registra FE-DIR-050 para exigir que las nuevas funciones estén disponibles para todas las empresas, con uso opcional según cada una.
- Verificación: comprobación documental.

## 2026-09-18 - Modificación y eliminación de ventas propias

- Alcance: el empleado puede modificar y eliminar la cadena de un pedido suyo desde Registro de ventas. Se mantienen la confirmación de eliminación, la propagación a factura y la clave de modificación de cadena cuando esté configurada. No se habilita Insertar ni Eliminar documento aislado.
- Verificación: compilación Angular, pruebas de autorización backend y comprobación documental.

## 2026-09-18 - Registro de ventas propias en Empleados

- Alcance: Ventas incorpora Registro de ventas junto a Táctil en Empleados. La pantalla reutiliza el registro de pedidos en modo de consulta: muestra solo los pedidos creados por el empleado, permite Ver y Exportar y vuelve a Mi agenda; no muestra acciones de escritura ni Importar.
- Verificación: compilación Angular, pruebas de filtrado y permisos en backend y comprobación documental.

## 2026-09-18 - Cierre de sesión al caducar el token

- Alcance: la aplicación comprueba la caducidad del JWT con la pantalla abierta y al volver a ella; al vencer, limpia la sesión y lleva al acceso. La renovación sigue asociada a interacción real, no a consultas automáticas.
- Verificación: prueba dirigida de caducidad del token, compilación Angular y comprobación documental.

## 2026-09-18 - Venta táctil en Empleados

- Alcance: Empleados incorpora Ventas como segunda opción del menú, con acceso a la misma pantalla Táctil de Ventas. Al cerrar, el empleado vuelve a Mi agenda. La pantalla táctil no se ofrece como destino de avisos de Ventana porque no muestra esos avisos.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Proveedores fuera de los destinos de Ventana

- Alcance: el selector de ventanas deja de ofrecer las rutas del módulo Proveedores; permanecen sus avisos en Mensajes y en el catálogo de proveedor.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Ventanas de Clientes y Proveedores

- Alcance: el selector de ventanas incorpora las rutas reales de Clientes, Proveedores y todas las rutas del menú de Empleados. El catálogo de sesión de Clientes muestra avisos de Ventana y Mi agenda conserva también los dirigidos a la entrada de Empleados.
- Verificación: compilación Angular, prueba de entrega en Mi agenda y comprobación documental.

## 2026-09-18 - Ventana de Avisos del módulo Empleados

- Alcance: el Administrador puede dirigir un aviso de Ventana a `/empleados/avisosAlertas`; la ruta de Comunicaciones sigue siendo una ventana distinta. Los avisos existentes deben cambiar de ventana si se desean mostrar allí.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Avisos para todas las empresas

- Alcance: el Administrador puede elegir «Todas las empresas» al enviar un aviso o alerta de Ventana o Mensajes a Empresa; la tabla muestra ese destino con su nombre.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Entrada del empleado en Mi agenda

- Alcance: el acceso del perfil Empleado a `/empleados` abre `/empleados/agenda`; los avisos destinados a la ventana inicial de Empleados se muestran también en esa vista.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Fecha final predeterminada de avisos y alertas

- Alcance: al insertar, la fecha final propuesta pasa de mañana a siete días después, a las 23:59:59; la fecha inicial sigue siendo hoy a las 00:00:00.
- Verificación: compilación Angular y comprobación documental.

## 2026-09-18 - Vista e intervalo independientes en agenda

- Alcance: Vista permite 1 día o 1 semana; Intervalo permite 5, 10, 15 y 30 minutos o 1 hora. Cambiar uno no modifica el otro.
- Verificación: compilación Angular y prueba dirigida de la agenda.

## 2026-09-18 - Intervalos de agenda de minutos a semana

- Alcance: el selector individual muestra 5, 10, 15 y 30 minutos, 1 hora, 1 día y 1 semana; las opciones de día y semana cambian la vista sin alterar el intervalo de minutos guardado.
- Verificación: compilación Angular y prueba dirigida del selector.

## 2026-09-18 - Selector combinado de agenda

- Alcance: un único selector reúne Día y Semana con cada intervalo visual y la vista Mes; el intervalo se conserva al pasar por Mes.
- Verificación: compilación Angular y prueba dirigida de selección de vista e intervalo.

## 2026-09-18 - Vista e intervalo configurables de agenda

- Alcance: Día/Semana/Mes se elige en un selector; la configuración individual y global guarda el intervalo visual por agenda de empleado y lo restaura al abrirla.
- Verificación: compilación Angular y pruebas dirigidas de agenda.

Historial de cambios: las entradas conservan el estado de su fecha; la especificación vigente está en los documentos temáticos del [índice](README.md).

## 2026-09-18 - Selector de intervalos de agenda

- Alcance: el intervalo visual de las agendas individual y múltiple añade 10 minutos; los botones de intervalo se sustituyen por un selector común con 5, 10, 15, 30 y 60 minutos.
- Verificación: compilación Angular y prueba de tramo visual de 10 minutos.

## 2026-09-18 - Menús visibles en Empleados y Proveedores

- Alcance: todos los grupos de esos dos módulos permanecen desplegados; el resto conserva su comportamiento de acordeón.
- Verificación: compilación Angular de componentes y plantillas.

## 2026-09-18 - Navegación de módulos en móvil y tableta

- Alcance: el panel abre módulos en la misma pestaña en pantallas de hasta 1100 px o con puntero táctil. El botón Módulos se oculta al empleado dentro del módulo; para los demás perfiles, la vuelta desde vista compacta permanece en la misma pestaña.
- Verificación: compilación Angular de componentes y plantillas.

## 2026-09-17 - Empresa visible en cabecera móvil

- Alcance: la cabecera móvil mantiene imagen y empresa en la primera fila y sitúa las acciones en la segunda, evitando que el nombre quede detrás de los botones.
- Verificación: reglas de distribución y documentación revisadas.

## 2026-09-17 - Selección en Gestión de Avisos y Alertas

- Alcance: se estabilizan las filas de presentación y la selección se vincula al aviso original mediante empresa e identificador.
- Verificación: compilación Angular de plantillas.

## 2026-09-17 - Filas del formulario de avisos

- Alcance: Tipo, Ubicación y Ventana forman la primera fila; Emisor, Destinatario y Empresa destinataria la segunda; Título y Mensaje la tercera.
- Verificación: estructura y orden de controles comprobados.

## 2026-09-17 - Orden de campos de avisos

- Alcance: Ubicación se sitúa inmediatamente después de Tipo en el formulario.
- Verificación: orden de controles y documentación comprobados.

## 2026-09-17 - Precisión de la directiva de trabajo n.º 1

- Alcance: los cambios acotados deben resolverse con la solución y verificación proporcionales, evitando trabajo y explicaciones sin valor; los ajustes visuales requieren comprobación en pantalla cuando sea posible.
- Verificación: catálogo de directivas y registro documental revisados.

## 2026-09-17 - Título en avisos de ventana

- Alcance: se vuelve a mostrar el título en negrita antes del mensaje, manteniendo la presentación compacta.
- Verificación: plantilla y documentación revisadas.

## 2026-09-17 - Avisos de ventana compactos

- Alcance: los avisos de ventana se sitúan encima del título y a la derecha del menú, ocupando el ancho del contenido principal. Se corrigió el panel que aparecía en el borde izquierdo: CSS aporta alineación y separación iniciales y el componente las ajusta al tamaño real. Solo el contenido reserva altura; el menú no se desplaza. Se muestra el mensaje y el control para ocultarlo, en una fila cuando hay espacio suficiente.
- Ajuste de espaciado: 12 px bajo la cabecera y 16 px sobre el título, calculados con la altura visible del aviso.
- Verificación: compilación del frontend y comprobación documental.

## 2026-09-17 - Disposición y periodo inicial de avisos

- Alcance: Empresa destinataria se sitúa junto a Destinatario y Ubicación comienza la fila siguiente. Los nuevos avisos proponen el día actual completo y el siguiente como periodo de publicación, con segundos conservados al modificar.
- Corrección visual y funcional: Empresa destinataria y Ventana usan clases de campo con estilo común; Usuario y Fecha Movimiento se rellenan al insertar igual que en el Registro de Usuarios.
- Verificación: compilación TypeScript, orden y estilo de campos, y comprobación documental correctos.

## 2026-09-17 - Enrutamiento de avisos y alertas

- Alcance: Registro muestra Emisor, Destinatario, Ubicación, Empresa destinataria y Ventana; la tabla y el histórico declaran los nuevos campos. El Jefe de una proveedora puede actuar como Proveedor sin crear un perfil nuevo. Empleados dispone de acceso al Registro de sus avisos. El selector de ventanas incluye el Registro de Compras en su ruta real `/compras/compras`.
- Entrega: catálogo de clientes y catálogo de proveedor muestran sus propios avisos; las ventanas autorizadas muestran avisos contextuales y la bandeja Mensajes muestra avisos informativos sin respuesta, con lectura individual y contador.
- Verificación: compilación Angular, 9 pruebas dirigidas de mensajería y catálogo y comprobador documental correctos; `/empleados/avisosAlertas` responde HTTP 200 en el servidor de desarrollo. Se mantienen los avisos de presupuesto de paquete y CSS de la compilación.

## 2026-09-17 - Mi catálogo en el menú de Proveedores

- Alcance: Proveedores muestra al entrar el menú del módulo, como Empleados; el grupo Catálogo incorpora la opción Mi catálogo, que abre la pantalla existente en `/proveedores/catalogo`.
- Decisión funcional: la pantalla conserva la selección de proveedor y el flujo de pedido compartido; solo cambia su acceso y el título visible.
- Verificación: compilación Angular y comprobador documental.

## 2026-09-17 - Directiva de trabajo n.º 1

- Alcance: incorporada la directiva permanente FE-DIR-049, identificada como directiva de trabajo n.º 1 y aplicable a todos los desarrollos. Exige autonomía, análisis y cambios completos, corrección de causas raíz y verificaciones proporcionales al riesgo; evita únicamente trabajo redundante sin reducir la calidad.
- Decisión funcional confirmada: si este modo de trabajo provoca un problema, se comunica al usuario para revisarlo. El cierre de cada tarea incluye un resumen breve de cambios, comprobaciones y pendientes relevantes.
- Verificación: revisión de la secuencia de directivas y del formato documental; no se modifica código funcional.

## 2026-09-15 · Consolidación integral de documentación

- Alcance: revisión de toda la documentación frontend/backend y general; contratos, configuración, operación, seguridad, modelo, módulos, uso, integraciones y pruebas.
- Consolidación de referencias IA, preservando visión y decisiones temáticas sin catálogos permanentes paralelos. Actualizados README y enlaces.
- Inventarios generados desde fuentes: 266 endpoints, 54 entidades y rutas Angular. Nuevo verificador `docs/verificar-documentacion.mjs`, ejecutable desde la raíz.
- Verificaciones: comprobador documental correcto para 62 Markdown, índices, enlaces locales explícitos, secuencias de 48 directivas FE y 40 BE e inventarios. Revisión de formato con `git diff --check`. No se repiten tests de negocio por este cambio documental.
- La auditoría se cierra documentalmente y se rectifica la existencia del inicializador de relaciones. Compras, histórico/movimiento de relaciones y demás diferencias siguen identificadas en LIMITACIONES; no se modifica código funcional ni datos por esta consolidación.

## 2026-09-15 - Auditoría de documentación

- Informe de diagnóstico de documentación frontend, backend y general, con 13 hallazgos y prioridades. No se modifica código ni datos funcionales.
- Verificación: inventario inicial de 48 Markdown; enlaces locales explícitos; 48 directivas FE y 40 BE sin saltos ni duplicados; contraste dirigido con rutas, controladores, servicios, plantillas, entidades y configuración. No se vuelve a ejecutar la suite funcional completa.
- Los hallazgos permanecen pendientes: el informe no equivale a corregir los documentos señalados.

## 2026-09-15 - Catálogos de proveedores relacionados

- Proveedores reutiliza el catálogo de Clientes y su flujo de pedidos, con selección de proveedor y autorización por relación activa en consulta, imágenes y envío. Sin relaciones no hay acceso desde el módulo. Directivas FE-DIR-048 y BE-DIR-040.
- Verificación: compilación Angular correcta; 9 pruebas Maven y 3 pruebas Vitest correctas. Backend reiniciado. API: empresas 1 y 3 acceden al proveedor 4 con 12 artículos e imágenes HTTP 200; empresa 2 sin proveedores recibe 403 en catálogo, imágenes y pedido. Acceso anónimo denegado (403). Frontend /proveedores responde 200. No se generan pedidos de prueba reales.

## 2026-09-15 - Eliminar relaciones de empresa

- Eliminación definitiva de la pareja desde Registro, con confirmación, validación de empresa y transacción. Nuevo endpoint DELETE /empresas-relaciones/{id}. Gestión conserva exclusivamente Baja y Reactivar.
- Verificación: compilación Angular correcta y 3 pruebas Maven correctas (eliminación de pareja, restricción por empresa y pareja incoherente). Backend reiniciado en 8080; DELETE sobre id inexistente responde 400. Las cuatro relaciones originales permanecen registradas. Frontend disponible con HTTP 200.

## 2026-09-15 - Relaciones del proveedor hostelero

- El 2026-09-15 se registraron mediante la API dos relaciones activas: Pizzeria La Esquina (empresa 1) y Restaurante Cándida (empresa 3) tienen como PROVEEDOR a Proveedor Hostelero Central (empresa 4). En la empresa 4 se crearon automáticamente las relaciones CLIENTE correspondientes. Parejas de identificadores: 1 ↔ 2 y 3 ↔ 4. Fecha de inicio: 2026-09-15; sin fecha fin. Verificación: consulta posterior de las tres empresas confirma los cuatro registros activos y sus enlaces inversos.

## 2026-09-15 - Tabla común de Relaciones de Empresa

- Registro y Gestión sustituyen la tabla HTML particular por el componente común de Productos, con filtros, paginación y configuración de todos los campos. Se reutiliza la barra de acciones y se respeta la consulta inicial vacía.
- Verificación: compilación Angular de desarrollo correcta, recompilación del servidor local correcta y revisión de los 13 campos del contrato y de la selección del registro original.

## 2026-09-15 - Mensajería administrativa y cabecera

- Arranque local: el frontend estaba detenido; se inicia Angular en el puerto 4200. Verificada la pantalla `/accesoLogin` con HTTP 200; backend disponible en 8080 (HTTP 403 sin autenticación).

- Mensajes se traslada de Comunicaciones a Administración, con rutas y menús exclusivos del Administrador. La cabecera solo ofrece Nuevo mensaje y Responder, además de consulta y navegación. Registro elimina bajas individualmente o hasta una fecha; Gestión da de baja y reactiva. Directiva FE-DIR-047.
- Verificación: Compilación Angular de desarrollo correcta. Vitest: 5 pruebas de permisos y separación de contextos correctas. El primer intento falló al iniciar el proceso de pruebas; el segundo terminó correctamente.

## 2026-09-14 - Acciones de tareas en Gestión de Agendas

- Los pedidos ocupados dejan de utilizar rojo de error y pasan a amarillo ámbar de aviso, también en la leyenda; la franja aplica `box-sizing` y anchura máxima para permanecer completamente dentro de su celda.
- Alineadas las acciones de cada tarea como un grupo compacto a la derecha: pago y cambio de estado quedan contiguos, con anchura y separación uniformes; en móvil se distribuyen debajo de la información.
- Eliminado el desplazamiento vertical interno de la agenda común tanto en modo individual como múltiple; la agenda crece en altura y el modo múltiple conserva únicamente la barra horizontal cuando sea necesaria.
- Ajustado el tratamiento independiente de los ejes: el eje vertical usa `clip` para impedir que el navegador transforme `visible` en `auto` al habilitar el desplazamiento horizontal.
- Reforzada la compatibilidad entre navegadores eliminando cualquier límite de altura heredado y fijando altura automática con el desbordamiento vertical oculto; las filas determinan siempre la altura total de la agenda.
- Reiniciado y verificado el servidor de desarrollo: el proceso anterior no había incorporado la nueva hoja `agendaMultiple.css`; la respuesta activa de `http://localhost:4200/main.js` contiene ya las reglas sin desplazamiento vertical.
- Corregido el modo múltiple para que la primera franja dependa exclusivamente de la hora inicial de visualización configurada (`ragHorVis`) y no retroceda hasta el inicio del horario laboral; si las columnas tienen valores distintos, se utiliza el más temprano para conservar su alineación.
- Eliminado el redondeo de `ragHorVis` al intervalo: una hora inicial `19:15` comienza exactamente a las `19:15`, también cuando el intervalo visual es de 30 o 60 minutos.
- Gestión de Agendas deja de renderizar una agenda propia y consume el componente común `AgendaRegistros`, igual que `Mi agenda`; el componente admite una o varias columnas de recursos sin cambiar el bloque de tareas ni sus acciones.
- En modo múltiple, `Tareas` permanece siempre visible debajo de la malla, muestra el empleado responsable y ofrece las mismas transiciones de estado y pago que la agenda individual.
- La malla limita su altura y dispone de desplazamiento propio para mantener visible el panel inferior de información y acciones; al seleccionar un tramo, el enfoque se mueve de forma inmediata al panel dentro del contenedor real.

- Al seleccionar un tramo ocupado, el detalle muestra todas las tareas de las reservas incluidas.
- Cada tarea ofrece las acciones que corresponden a su estado: `Iniciar`/`Recoger`, `Finalizar`/`Entregar` y `Marcar pagado` cuando pertenece a un pedido.
- Tras una acción se actualiza la malla completa para reflejar de inmediato los nuevos estados.
- Las franjas rojas son ahora botones reales: al pulsarlas se abre y desplaza a la vista el detalle interactivo.
- Corregida la repetición visual de una misma reserva en varios intervalos: el estilo del botón anulaba el atributo `hidden`; ahora solo se crea la franja correspondiente al inicio real.
- El detalle y sus acciones se presentan debajo de la agenda, igual que en la vista individual; al pulsar una franja, el componente desplaza y enfoca automáticamente ese detalle.
- Una tarea `En curso` o `Finalizada` puede volver a `Pendiente` con confirmación, y un pedido pagado puede desmarcarse también con confirmación.
- Verificación: compilación Angular de producción correcta.

## 2026-09-13 - Contexto global de empleado para el Administrador

- La barra superior muestra al Administrador un selector con las agendas de empleados de la empresa elegida.
- La selección se conserva como contexto global reutilizable por Agenda y por futuros procesos de otras ventanas.
- `Mi agenda` consume ese contexto sin incorporar un selector particular en la ventana.
- Cambiar de empleado reutiliza el componente común de Agenda y recarga sus horarios, reservas y tareas sin recargar la aplicación.
- Si está seleccionada `Todas las empresas`, se solicita elegir primero una empresa; nunca se conserva un empleado de otro contexto empresarial.
- El acceso concedido a una agenda incluye sus acciones: Administrador, Jefe o empleado titular pueden avanzar tareas y marcar el pago cuando tienen acceso a esa agenda.
- En la vista personal tampoco se presentan las acciones genéricas de edición o reprogramación de reservas.
- Jefes y empleados mantienen el acceso directo a su propia agenda sin selector.

## 2026-09-13 - Integridad de campos en tablas

- Se establece como directiva que toda tabla declara todos los campos funcionales de su registro, aunque algunos permanezcan ocultos inicialmente.
- El componente común `tabla` incorpora `columnasOcultasPorDefecto`; estos campos siguen disponibles en la configuración de columnas.
- La tabla de Empresas incorpora nombre comercial, razón social, NIF/CIF, actividad, teléfono, correo, web, domicilio fiscal, imagen y todos los datos de movimiento.
- Imagen, tipo de movimiento y causa de movimiento quedan ocultos por defecto, pero pueden habilitarse desde la configuración.
- El domicilio fiscal se presenta mediante la dirección completa y no mediante `dom_id`.
- Verificación: compilación Angular de producción correcta; permanecen los avisos de presupuesto y dependencias CommonJS ya existentes.

## 2026-09-11 - Ampliación del Registro de Empresas

- El formulario mantiene `Datos Identificación` y añade los apartados `Datos Empresa`, `Datos Contacto` y `Datos Domicilio`.
- Datos Empresa contiene nombre comercial, razón social, NIF/CIF y actividad; Datos Contacto contiene teléfono, correo electrónico y página web.
- El domicilio fiscal se selecciona mediante el componente común `selectorBusqueda` y solo presenta domicilios pertenecientes a la empresa del registro.
- Los campos nuevos están disponibles en alta, consulta y modificación del Registro; Gestión conserva exclusivamente sus acciones operativas.
- Verificación: compilación Angular de producción correcta; permanecen los avisos de presupuesto y dependencias CommonJS ya existentes.

## 2026-09-13 - Título del centro de módulos

- El encabezado del centro de acceso se simplifica a `Módulos de Gestión`.
- Verificación: compilación Angular de producción correcta.

## 2026-09-11 - Recarga de imagen al cambiar la empresa global

- La imagen principal de la cabecera se resuelve con el mismo contexto `X-Empresa-Seleccionada` que utiliza el resto de consultas del administrador.
- Al cambiar de empresa y recargar el contexto, la cabecera muestra el adjunto principal de la nueva empresa; con `Todas las empresas` no se presenta una imagen concreta.

## 2026-09-11 - Imágenes principales propias para las empresas

- Pizzeria La Esquina, Taller Bosco de coches, Restaurante Cándida y Proveedor Hostelero Central disponen de ilustraciones 3D raster diferenciadas, con transparencia real y sin texto incrustado.
- Las imágenes se consumen mediante el mecanismo común de adjuntos principales de Empresa y la ruta general configurada, sin incorporar recursos gráficos en componentes funcionales.
- Enfoque de generación: ilustración 3D profesional, composición cuadrada compacta, iluminación de estudio y elementos representativos de cada actividad; generación realizada con la herramienta integrada de imágenes.

## 2026-09-11 - Modificación y eliminación exclusivas de Registro

- Se establece como directiva funcional permanente que ninguna pantalla de Gestión muestra las acciones `Modificar`, `Eliminar` ni eliminaciones en cascada.
- Se retiran estas acciones de Gestión de Personas, Productos, Servicios, Recursos, Domicilios, Avisos/Alertas y documentos de Ventas.
- En Ventas, `Eliminar cadena` se traslada a Registro y queda disponible junto a la eliminación individual; Gestión conserva Ver, bajas, reactivaciones, históricos, adjuntos y acciones operativas.
- Si una petición futura contradice esta separación, debe recordarse la directiva antes de realizar cambios.
- Verificación: compilación Angular de producción correcta; permanecen los avisos de presupuesto y dependencias CommonJS ya existentes.

## 2026-09-11 - Módulos por ámbito y estilos de Comunicaciones

- La ventana de acceso sustituye `Productos contratados` por `Módulos de Empresa Gestión`.
- Clientes, Empleados y Proveedores se presentan en un segundo apartado independiente denominado `Módulos de Personas`, conservando dentro de cada grupo el orden configurado por empresa.
- La imagen predeterminada de Proveedores se sustituye por una ilustración 3D raster de la misma familia visual, protagonizada por una persona y acompañada de referencias discretas al suministro y la logística. El PNG utiliza transparencia alfa real.
- Bandeja, Contactos de canal y Avisos/Alertas normalizan sus botones con las clases comunes de consulta, vista, alta, guardado, modificación, eliminación, cancelación, reactivación y vuelta.
- En la consulta de Avisos/Alertas se corrige la clase inexistente `accion-alta` por la acción común `accion-reactivar`; en modo de consulta, el cierre del formulario pasa a mostrarse como `Volver`.
- Verificación: transparencia alfa comprobada en el PNG y compilación Angular de producción correcta; permanecen los avisos de presupuesto y dependencias CommonJS ya existentes.

## 2026-09-11 - Confirmación para modificar pedidos en cadena

- La modificación encadenada de Pedidos se inicia desde Registro; ninguna pantalla de Gestión presenta la acción `Modificar`.
- Antes de abrir un pedido para modificarlo, se informa de que la factura y, cuando está habilitado, el albarán se modificarán conjuntamente.
- Si el usuario responde `No`, se muestra el aviso de que el pedido no puede modificarse sin sus documentos asociados y la operación queda cancelada.
- Cuando la empresa exige clave, esta se solicita mediante el sistema común de diálogos y se envía exclusivamente al guardar la modificación.
- El sistema común de diálogos incorpora una solicitud reutilizable de texto o contraseña, evitando controles particulares y avisos nativos.
- Verificación: compilación Angular de producción correcta; permanecen los avisos de presupuesto y dependencias CommonJS ya existentes.

## 2026-09-10 - Cabecera común en Gestión de Agendas

- Gestión de Agendas utiliza la clase común de título y alinea sus acciones principales a la derecha en la misma fila.
- Eliminado el estilo particular del título para mantener el mismo comportamiento visual y adaptable que el resto de ventanas.

## 2026-09-10 - Anchura uniforme de tarjetas del catálogo

- La cuadrícula común del catálogo conserva las columnas vacías cuando una categoría contiene pocos registros.
- Una tarjeta única de producto o servicio mantiene la misma anchura que las tarjetas de una categoría con varios elementos, evitando que se extienda por toda la fila.
- El comportamiento se aplica por igual a productos y servicios y conserva la presentación de una sola columna en móvil.

## 2026-09-10 - Tarjetas de módulos más compactas

- Reducidos la altura, el espaciado y el tamaño de imagen de las tarjetas del panel de módulos.
- Eliminado el recuadro gris de los iconos para aprovechar directamente la transparencia de los nuevos PNG.
- Se conserva una sombra ligera aplicada únicamente a la ilustración para separarla del fondo blanco sin añadir otro panel visual.
- Verificación: compilación Angular de producción correcta.

## 2026-09-10 - Renovación visual de imágenes de módulos

- Sustituidas las trece imágenes predeterminadas de los módulos por una familia gráfica raster coherente, con acabado tridimensional suave y una paleta común azul marino, azul, dorado y verde.
- Los recursos transparentes se han normalizado a PNG de 512 × 512 píxeles para conservar detalle y nitidez en pantallas de distinta densidad.
- Las tarjetas amplían y realzan la imagen mediante un contenedor neutro, sombra ligera y una animación discreta, manteniendo el comportamiento adaptable en móvil.
- La imagen principal configurada mediante `Adjuntos` continúa teniendo prioridad sobre la imagen predeterminada del módulo.
- Verificación: inspección visual de los recursos finales y compilación Angular de producción.

## 2026-09-10 - Selector global de empresa

- La cabecera del Administrador sustituye el texto `Todas las empresas` por un selector con el conjunto global y cada empresa activa.
- La selección se conserva en el navegador, mantiene la ruta abierta y recarga la pantalla con el nuevo contexto.
- El interceptor HTTP incorpora automáticamente `X-Empresa-Seleccionada` en todas las peticiones autenticadas del Administrador, evitando implementaciones particulares por pantalla.
- La imagen de cabecera también se obtiene de la empresa seleccionada; el resto de perfiles conserva la cabecera anterior sin selector.

## 2026-09-10 - Empresa visible para el Administrador global

- El componente común de tabla mantiene visible la columna Empresa en cualquier módulo cuando la sesión pertenece al Administrador.
- El filtro ordinario de esa columna permite limitar localmente la consulta global a una empresa sin crear variantes de tabla por pantalla.
- Jefe, Empleado y Cliente conservan la visibilidad y el ámbito configurados para su empresa.

## 2026-09-10 - Fase 5: acciones de reparto

- La agenda común identifica las tareas con habilidad `REPARTO` sin crear una pantalla especial para repartidores.
- Sus acciones se presentan como `Recoger` y `Entregar`, conservando internamente la secuencia común `PENDIENTE → EN_CURSO → FINALIZADO`.
- Las tareas de elaboración mantienen las acciones `Iniciar` y `Finalizar`.
- Cada tarea de pedido no cobrado permite `Marcar pagado`; después se muestra la marca `Pagado` sin alterar el estado de elaboración o reparto.
- Verificación: compilación Angular de producción correcta; permanecen únicamente las advertencias previas de presupuesto y dependencias CommonJS.

## 2026-09-10 - Fase 4: tareas automáticas de pedidos

- Al confirmar un pedido del catálogo, sus líneas quedan disponibles automáticamente en la agenda del empleado asignado por el backend.
- No se añade una agenda alternativa: las tareas se muestran y gestionan mediante el componente común `AgendaRegistros` ya utilizado por Empleado y Jefe.
- La cantidad completa de cada línea permanece agrupada en una única tarea.
- Verificación del contrato: pruebas completas del backend correctas y servicio actualizado en ejecución.

## 2026-09-10 - Módulo inicial de Proveedores

- Incorporada la tarjeta y la ruta autenticada del módulo `Proveedores`.
- El módulo parte vacío y reutiliza la pantalla común de módulos sin opciones funcionales.
- Añadido un icono vectorial coherente con el estilo de los módulos actuales.
- Verificación: compilación Angular de producción correcta; permanecen las advertencias previas de presupuesto y dependencias CommonJS.

## 2026-09-10 - Navegación diaria de Mi agenda

- `Mi agenda` abre en la fecha actual y muestra siempre el panel del día, aunque no existan tareas.
- Incorporados día anterior, Hoy, selector de fecha y día siguiente para consultar tareas pasadas o futuras.
- Las tareas se ordenan por hora prevista y mantienen visibles el tipo y los estados Pendiente, En curso y Finalizado.
- La ausencia de tareas se representa como una agenda diaria vacía, no como ausencia de agenda.
- Verificación: compilación de producción Angular correcta.
- Refactorización: `Mi agenda` ya no implementa una agenda propia; compone directamente el componente común `AgendaRegistros`.
- El componente común incorpora las tareas y sus cambios de estado, además de reservas, disponibilidad, navegación Día/Semana/Mes y configuración cuando el consumidor la permite.
- La portada del módulo Empleados no carga la agenda; esta se consulta únicamente al pulsar la opción `Mi agenda` del menú.
- Eliminado el botón redundante `Consultar` de Mi agenda, ya que la entrada a la opción realiza la carga automáticamente.
- El botón `Calendario` se oculta cuando es la única vista disponible y solo aparece si permite regresar desde Reserva o Configuración.

## 2026-09-10 - Mi agenda para el perfil Jefe

- El Jefe utiliza la misma pantalla personal de agenda que el Empleado.
- Su agenda puede contener tareas generales aunque no tenga habilidades de elaboración o reparto.
- El mantenimiento del recurso y del horario continúa en Gestión de Recursos, evitando duplicar pantallas.

## 2026-09-10 - Cabecera compacta de usuario y mensajes

- Eliminadas del panel de módulos las tarjetas independientes de Bandeja, Revisión y número de módulos disponibles.
- La barra superior incorpora un único botón `Mensajes` con contador y un desplegable compacto que reúne mensajes de Bandeja y conversaciones pendientes de Revisión.
- Los textos permanentes de Usuario y Perfil se sustituyen por un botón `Usuario` cuyo desplegable es exclusivamente informativo.
- La empresa y la fecha continúan visibles; los botones `Módulos` y `Salir` conservan su ubicación y comportamiento.
- La solución pertenece al componente común de cabecera y se aplica a Empleado, Jefe y Administrador en todas sus ventanas.
- Verificación: compilación de producción Angular correcta.
- Ajuste posterior: el grupo derecho queda ordenado como `Módulos`, `Mensajes`, `Usuario` y `Salir`, situando Usuario entre Mensajes y Salir.
- Los botones Usuario y Mensajes adoptan un diseño compacto con icono, fondo suave y estado desplegado visible.
- Ajuste de color: Módulos usa morado suave, Usuario azul suave, Salir gris y Mensajes cambia de verde a rojo suave según existan pendientes.
- El contador de Mensajes sustituye al icono y aparece a la izquierda. El botón de usuario presenta el identificador de acceso de la tabla Usuarios; el desplegable muestra Nombre y Perfil.
- Módulos incorpora un icono a la izquierda, Usuario un icono a la izquierda y Salir un icono a la derecha.
- En anchos reducidos desaparecen los textos de estos tres botones y permanecen los iconos con etiquetas accesibles.
- Ajustados los iconos: Usuario utiliza una silueta de persona y Salir reutiliza la forma de flecha de Volver invertida hacia la derecha, situada antes del texto.
- Eliminados los fondos blancos de los iconos y del contador; la silueta de Usuario y la flecha de Salir se compensan ópticamente para mostrar un tamaño aparente uniforme.
- El contador de Mensajes se presenta como una insignia circular roja o verde con el número centrado en blanco.
- Eliminada la flecha del botón de usuario; toda su superficie abre y cierra directamente el desplegable informativo.

## 2026-09-10 - Corrección visual de Mi agenda

- La agenda del empleado carga la estructura común de módulo para situar el menú lateral y el contenido en la misma fila.
- Normalizados tipografía, anchura del contenido y altura del panel vacío para mantener el estilo del resto de ventanas.
- Verificación: compilación de producción Angular correcta.

## 2026-09-10 - Separación de recursos en Gestión

- Gestión de Recursos utiliza una única tabla y un botón de vista morado para alternar entre empleados y maquinaria.
- Las dos vistas conservan configuraciones de columnas y filtros independientes aunque nunca aparecen simultáneamente.
- Registro de Recursos mantiene una única tabla conjunta para el CRUD del maestro.
- Verificación: compilación de producción Angular correcta.

### Decisión funcional

- Nunca se muestran dos tablas simultáneamente dentro de una misma ventana; cuando existan varios conjuntos tabulares, se alternarán mediante un control de vista.

## 2026-09-10 - Horario semanal en una sola columna

- El componente común de configuración de agendas muestra los siete días uno debajo de otro.
- Cada día conserva sus franjas horarias alineadas horizontalmente y el comportamiento adaptable en pantallas pequeñas.
- Verificación: compilación de producción Angular correcta.

## 2026-09-10 - Fase 3: agenda personal del empleado

- El módulo Empleados abre directamente la pantalla `Mi agenda` cuando el trabajador entra con sus credenciales.
- La pantalla muestra únicamente las tareas devueltas para el usuario autenticado, distinguiendo `Producto` o `Servicio` junto a su estado.
- Cada tarea permite avanzar de `Pendiente` a `En curso` y posteriormente a `Finalizado`; una tarea finalizada queda solo informativa.
- La vista es adaptable a móvil y utiliza el sistema común de diálogos para confirmaciones de resultado y errores.
- Verificación: compilación de producción Angular correcta.

## 2026-09-10 - Fase 1 del modelo de tareas de pedido

- Ampliado el contrato de tareas de Agenda con línea, tipo Producto/Servicio, habilidad, cantidad, duraciones y tiempos previstos y reales.
- Definidos los estados de tarea `PENDIENTE`, `EN_CURSO` y `FINALIZADO` como valores independientes del tipo.
- Esta fase prepara el contrato; no cambia todavía la pantalla ni el acceso del empleado.
- Verificación: compilación Angular de producción correcta.

## 2026-09-08 - Acción Ver en Registro de Usuarios

- Añadida la acción `Ver`, visible únicamente tras seleccionar un usuario y situada antes de `Modificar`.
- El formulario de consulta reutiliza el modo común de solo lectura y no muestra acciones de guardado.
- `Modificar` y `Eliminar` también permanecen ocultas mientras no exista una selección.

## 2026-09-08 - Limpieza de Gestión de Recursos

- Eliminado el bloque informativo sin funcionalidad situado bajo la tabla con el texto `Seleccione un recurso para administrarlo`.

## 2026-09-08 - Homogeneización de Gestión de Catálogo

- Sustituidos los paneles y controles particulares por contenedores, grupos, campos y barras comunes.
- Corregido el gran espacio vertical alineando al inicio las filas de la cabecera común.
- Añadidas acciones semánticas con iconos para Configuración, Códigos QR y Vista previa.

## 2026-09-08 - Cabeceras de ventana en una sola fila

- El título y la barra principal de acciones se alinean en la misma fila mediante el estilo común de `.pagina`.
- El contenido posterior conserva el ancho completo y los botones pueden distribuirse dentro del espacio restante.
- En pantallas estrechas la cabecera vuelve a apilarse para conservar legibilidad y áreas táctiles adecuadas.
- En escritorio, la barra principal queda alineada al extremo derecho de la cabecera.

## 2026-09-08 - Consulta coherente de bajas

- Corregida Gestión de Avisos y Alertas para ocultar las bajas en la consulta ordinaria, igual que el resto de maestros versionados.
- Las bajas se recuperan explícitamente filtrando `Tipo Movimiento` por `B`; `incluirBajas` queda reservado para vistas de histórico.
- Revisadas todas las apariciones de `incluirBajas`: no existen otras tablas principales que fuercen su visualización.

## 2026-09-08 - Cierre de avisos y ayuda de stock

- Cada aviso o alerta del catálogo incorpora una X visible que lo oculta durante la visita actual.
- Productos muestra una nota contextual cuando `Control Stock` está marcado, indicando la obligación de informar existencias y el comportamiento al llegar a cero.
- La nota utiliza el estilo común destacado `nota-informativa`, con fondo azul suave, borde lateral, icono y contraste accesible para reutilizarlo en otros formularios.
- La ayuda de stock permanece visible tanto si el control está marcado como si no, usa una variante compacta y los checkbox comunes se ajustan a 20 px para integrarse con el resto del formulario.

## 2026-09-08 - Edición fiable del stock actual

- Los formularios de Registro y Gestión normalizan explícitamente el stock como entero no negativo antes de enviarlo.
- Se evita que un campo numérico vacío o inválido genere una confirmación engañosa.

## 2026-09-08 - Registro y Gestión de Avisos/Alertas

- Renombrado `Operación` de Comunicaciones como `Registro`.
- Añadidas pantallas de Registro y Gestión con sus acciones e histórico correspondientes.
- Añadidos avisos amarillos y alertas rojas suaves antes del menú del catálogo.
- Verificado mediante compilación de producción de Angular.

## 2026-09-08 - Imagen de Tipos gestionada solo mediante Adjuntos

- Retirada la columna técnica `Imagen genérica` de las tablas de Tipos de Producto y Tipos de Servicio.
- La imagen continúa administrándose exclusivamente mediante `Adjuntos`; la marcada como principal es la consumida por el catálogo.

## 2026-09-08 - Distintivos comerciales del catálogo

- Añadidos `Novedad`, `Mejor precio` y `Outlet` a Datos Adicionales de Productos y Servicios.
- Los indicadores se conservan también al operar desde Gestión de Productos y Gestión de Servicios.
- El catálogo los muestra como etiquetas diferenciadas sobre la imagen, junto al identificador y al posible estado `Agotado`.
- Verificado con `npm.cmd run build`.

## 2026-09-08 - Consulta progresiva desde Mapa y Malla

- En Gestión de Personas, Productos, Domicilios y Pedidos, la primera acción `Consultar` desde Mapa/Malla abre la Tabla vacía y la segunda carga todos los registros.
- El componente compartido de Tabla solicita la carga al escribir un filtro cuando todavía no existen datos; al recibirlos conserva el filtro y presenta solo las coincidencias.
- Las tablas con consulta remota actualizan la consulta tras una breve espera de escritura.
- Las tablas vacías de los CRUD se han conectado al mismo mecanismo reutilizable.
- Verificado con `npm.cmd run build`.

## 2026-09-08 - Carga inicial de Registro y Gestión

- Aplicada la regla de vista inicial: Malla tiene prioridad sobre Mapa y ambas cargan automáticamente; una Tabla sin esas vistas abre vacía hasta pulsar `Consultar`.
- Eliminada la consulta automática inicial en Tipos de Producto/Servicio, Servicios, Cajas, Recursos, catálogos territoriales y registros complementarios de Personas.
- Registro y Gestión de documentos de venta con tabla también abren sin datos; Gestión de Pedidos conserva la Malla predeterminada con carga automática.
- Verificado con `npm.cmd run build`.

## 2026-09-08 - Baja de Servicios condicionada a selección

- Corregida la barra de Gestión de Servicios para que `Baja` solo aparezca en la tabla cuando existe un servicio seleccionado y no está dado de baja.
- Revisadas las condiciones opcionales equivalentes del resto de barras; no se encontraron otras acciones operativas que aparecieran sin selección.
- Verificado con `npm.cmd run build`.

## 2026-09-08 - Visibilidad coherente de la acción Ver

- Revisadas todas las acciones `Ver` de los CRUD del frontend.
- Corregido Tipos de Producto y Tipos de Servicio para mostrar `Ver` únicamente cuando existe una fila seleccionada.
- Gestión de Productos y Gestión de Domicilios limitan además la acción a la vista de tabla, evitando conservarla visualmente al abrir el formulario, los adjuntos, la malla o el mapa.
- El resto de registros ya condicionaba correctamente la acción a una selección en su vista correspondiente.
- Verificado con `npm.cmd run build`.

## 2026-09-08 - Acción Ver en Registro de Productos

- Incorporada la acción `Ver` a la barra del Registro de Productos, disponible al seleccionar una fila.
- El formulario se abre en modo de consulta, impide modificar sus campos y oculta las acciones de guardado y actualización.
- La salida del modo de consulta se identifica como `Volver a consulta`.
- Verificado con `npm.cmd run build`.

## 2026-09-08 - Columnas técnicas ocultas por defecto

- `Id Empresa`, `Tipo Movimiento` y `Causa Movimiento` quedan ocultas inicialmente en todas las tablas mediante una regla única del componente compartido.
- Se conserva su presencia en `Configurar tabla`, donde cada usuario puede mostrarlas y guardar su decisión.
- `Id Histórico` continúa oculto por defecto según el comportamiento existente.
- Las preferencias anteriormente guardadas reciben el nuevo valor inicial mediante una migración única, sin impedir cambios posteriores del usuario.
- Verificado con `npm.cmd run build`.

## 2026-09-08 - Selección de imágenes del catálogo

- El parámetro `IMAGEN_CATALOGO_ORIGEN` muestra un selector con `Imagen principal del tipo` y `Adjunto principal del registro`.
- Productos y Servicios mantienen parámetros independientes, permitiendo combinar criterios distintos en el mismo catálogo.
- Verificado con `npm.cmd run build`.

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
## 2026-09-08 - Reordenación del menú de Administración

- El grupo `Utilidades` se presenta antes de `Parametrización` en el menú lateral de Administración.
- Se elimina de `Utilidades` el acceso duplicado a `Parámetros`; el acceso se mantiene exclusivamente dentro de `Parametrización`.
- La definición de perfiles y permisos por operación queda pendiente de confirmación funcional y no se modifica en este cambio.
## 2026-09-09 - Módulos iniciales de Empleados y Clientes

- Se incorporan las tarjetas de acceso y las rutas autenticadas de los módulos `Empleados` y `Clientes`.
- Ambos módulos parten vacíos: muestran únicamente su título y un menú lateral sin opciones, sin incorporar todavía CRUD, datos ni permisos específicos.
- La pantalla inicial vacía se implementa como componente reutilizable y se configura mediante los datos de cada ruta.
- Se añaden iconos vectoriales coherentes con el estilo visual de los módulos existentes.
- Decisión funcional: cualquier función futura se analizará y validará antes de incorporarla a estos módulos.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`; permanecen únicamente los avisos previos de presupuesto y dependencias CommonJS.
## 2026-09-09 - Maestro y Gestión de módulos

- Añadido `Módulos` en Registro con CRUD, vista de consulta y gestión de imagen mediante Adjuntos.
- Añadida `Gestión de Módulos`, limitada a disponibilidad y orden para la empresa activa.
- El panel pasa a consumir obligatoriamente la disponibilidad y el orden persistidos para la empresa.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`; permanecen únicamente advertencias previas de presupuesto y CommonJS.
## 2026-09-09 - Orden de módulos obligatorio por empresa

- Eliminados el arrastre de módulos y la acción `Restablecer orden` del panel principal.
- El panel aplica siempre el orden establecido en Gestión de Módulos para la empresa activa y el usuario no puede alterarlo.
- Eliminado el servicio de persistencia del orden individual.
## 2026-09-09 - Registro y Gestión en el menú de Administración

- Añadido el grupo `Registro` inmediatamente debajo de `Seguridad`.
- `Módulos`, `Áreas Organizativas` y `Personal de Área` se agrupan dentro de Registro.
- `Gestión de Módulos` permanece como operación independiente dentro de Gestión.
- Eliminado el grupo `Estructura Organizativa` y retirado el acceso duplicado a Módulos desde Parametrización.
## 2026-09-09 - Acciones de confirmación de Gestión de Módulos

- `Guardar` se traslada desde la cabecera al pie del formulario de disponibilidad y orden.
- Añadida la acción inferior `Cancelar`, que vuelve al módulo de Administración sin guardar los cambios.
- La cabecera conserva únicamente las acciones de consulta y navegación.
## 2026-09-09 - Persistencia verificable del orden de módulos

- Gestión envía posiciones consecutivas según el orden visible y, después de guardar, sustituye la lista por la configuración devuelta por el backend.
- Los errores de persistencia muestran el detalle recibido mediante el sistema común de diálogos.
## 2026-09-09 - Menú y Gestión de Usuarios

- Eliminado el grupo Seguridad; Usuarios, Perfiles, Empresas y Auditoría pasan al grupo Registro.
- Añadida Gestión de Usuarios con Ver, Baja, Reactivar e Histórico según la selección y el estado.
- Baja y Reactivación solicitan causa en un formulario y sitúan Confirmar y Cancelar al pie.
- El formulario de Gestión muestra Tipo y Causa dentro de Datos Movimiento; Registro conserva únicamente Usuario, Fecha y Activo.
- Verificación: compilación Angular de producción correcta mediante `npm.cmd run build`.
## 2026-09-09 - Limpieza de opciones de Administración

- Eliminada la opción sin acceso funcional `Auditoría` del grupo Registro.
- Eliminada la opción sin acceso funcional `Selectores` del grupo Parametrización.
## 2026-09-09 - Homogeneización del formulario y navegación de retorno

- El formulario de Módulos muestra en `Id Empresa` el identificador numérico de la empresa activa, eliminando el texto incorrecto `Configuración general`.
- Los campos Descripción y Posición de Módulos adoptan los anchos y estilos comunes de formulario.
- Todas las acciones que regresan desde una consulta, histórico o adjuntos se muestran con el texto uniforme `Volver`; `Cancelar` se conserva en los formularios de edición y alta.
- Verificación: compilación Angular de producción y comprobación de diferencias sin errores.
## 2026-09-09 - Acceso a módulos por perfil

- Administrador y Jefe pueden entrar en todos los módulos disponibles para su empresa.
- Cliente solo visualiza y puede navegar al módulo Clientes; Empleado solo al módulo Empleados.
- La protección se aplica tanto al panel como a la navegación directa mediante URL.
- Las pantallas vacías de Clientes y Empleados se mantienen como punto de partida para su definición progresiva.
## 2026-09-09 - Empresa visible en Administración

- Todas las tablas con datos por empresa del módulo Administración fuerzan visible la columna `Empresa`.
- La regla prevalece sobre configuraciones antiguas que hubieran ocultado `Id Empresa`.
- Se conserva el comportamiento común: una tabla vacía permite informar el filtro antes de Consultar y mantiene ese filtro al recibir los resultados.
- Verificación: compilación Angular de producción correcta.
## 2026-09-09 - Representación del ámbito Administrador

- La cabecera del Administrador muestra `Todas las empresas` en lugar de la empresa técnica asociada internamente.
- En las columnas Empresa se conservan los identificadores numéricos y se representa con `T` únicamente el ámbito global del Administrador.
- La exportación de la tabla utiliza la misma representación visible.
## 2026-09-09 - Apertura vacía de tablas remotas

- Corregido el componente común de tabla para que la inicialización de columnas no lance automáticamente una consulta remota.
- Registro y Gestión de Usuarios abren con la tabla vacía hasta pulsar `Consultar` o informar un filtro.
- Si se elimina un filtro que ya había provocado una consulta, la tabla vuelve a solicitar el conjunto sin ese filtro.
## 2026-09-09 - Configuración empresarial de módulos

- Gestión de Módulos incorpora un selector de Empresa para que el Administrador consulte y configure cada panel por separado.
- Cambiar la empresa vacía los resultados; es necesario pulsar `Consultar`, respetando la regla general de carga de vistas sin mapa ni malla.
- La opción Gestión de Módulos se oculta para Jefe, Cliente y Empleado, y la ruta directa redirige fuera de la pantalla.
- Guardar persiste tanto los módulos habilitados como el orden mostrado para la empresa seleccionada.

## 2026-09-09 - Maestros exclusivos del Administrador

- Registro y Gestión de Módulos, Perfiles y Empresas se ocultan a Jefe, Cliente y Empleado.
- La navegación directa a esas rutas también queda bloqueada para perfiles distintos de Administrador.
- El selector de Perfil del formulario de Usuarios muestra una sola vez los perfiles pertenecientes a la empresa del usuario editado.
- El formulario usa la consulta auxiliar `GET /perfiles/selector` para usuarios no administradores.
- Verificación: compilación Angular de producción correcta; se mantienen únicamente los avisos de presupuesto y dependencias CommonJS ya existentes.

## 2026-09-09 - Acciones contextuales en Registro de Perfiles

- Ver, Modificar y Eliminar permanecen ocultas hasta seleccionar un perfil en la tabla.
- Incorporada la acción Ver en modo de solo lectura y un retorno uniforme mediante `Volver`.
- Una consulta nueva limpia la selección anterior para evitar acciones sobre un registro que ya no esté visible.

## 2026-09-09 - Registro y Gestión de Empresas

- Registro de Empresas incorpora Consultar, Ver, Insertar, Modificar, Eliminar y Adjuntos con visibilidad contextual.
- Creada Gestión de Empresas, exclusiva del Administrador, con Ver, Baja, Reactivar, Histórico y Adjuntos.
- Las consultas ordinarias excluyen empresas de baja; solo se incluyen cuando se informa el filtro Activo.
- La directiva común de barras sitúa siempre Mapa, Malla o Árbol a la izquierda y Volver en el extremo derecho.
- Los botones Árbol usan la paleta y el icono comunes de acciones visuales.
- Mapa, Malla y Árbol comparten una misma paleta morada suave para identificarlos como vistas visuales.

## 2026-09-09 - Enlace corto de pedidos por empresa

- Cada empresa dispone de un alias único y legible para compartir su catálogo mediante `/catalogo/{alias}`.
- El alias se genera inicialmente a partir del nombre de empresa y puede editarse en Gestión de Catálogo.
- Los enlaces generales muestran la dirección corta; los tokens existentes y los QR de posiciones continúan siendo compatibles.
- Verificación: compilación Angular de producción correcta.
- Verificación: compilación Angular de producción correcta.
## 2026-09-10 - Catálogo en el módulo Clientes

- El módulo Clientes muestra el mismo catálogo común de productos y servicios que se ofrece mediante el enlace público.
- Al acceder con una sesión de Cliente, el catálogo obtiene automáticamente el alias configurado para su empresa; no se mantiene una segunda vista ni una segunda fuente de datos.
- La presentación móvil se activa automáticamente en pantallas estrechas y continúa siendo compatible con el parámetro `vista=movil`.
- Verificación: compilación Angular de producción correcta; permanecen las advertencias previas de presupuesto y dependencias CommonJS.
# 2026-09-11 — Documentos de venta y completitud de Personas

- El menú de Ventas oculta Registro y Gestión de presupuestos y albaranes según los parámetros de la empresa activa.
- Registro y Gestión de facturas utilizan una única pantalla para facturas normales y simplificadas, muestran el tipo y permiten convertir una simplificada en normal.
- Los datos fiscales se muestran para la factura normal y se informa de que deben completarse exclusivamente desde Personas antes de emitirla.
- Personas muestra en tabla y formulario si sus datos están completos o incompletos.
- El catálogo admite correo electrónico y lo remite junto al teléfono para identificar correctamente al cliente.
- Verificación: `npm.cmd run build` correcto; permanecen únicamente los avisos previos de tamaño y dependencias CommonJS.
- Ajuste posterior: el aviso extenso de completitud se sustituye por un campo deshabilitado `Estado de los datos`, actualizado en tiempo real, y el teléfono se presenta y valida como obligatorio.
- Para personas jurídicas, la razón social corta pasa a ser obligatoria y la razón social larga queda opcional.
- Gestión de Pedidos oculta la acción `Convertir a albarán` cuando `MOSTRAR_ALBARANES` está desactivado para la empresa activa; el backend mantiene la misma restricción ante accesos directos.
- La barra común sitúa siempre `Histórico` inmediatamente antes de `Volver`, y mantiene `Volver` como última acción a la derecha en todas las ventanas.
- `Adjuntos` se sitúa siempre a la izquierda de `Histórico`, dejando como cierre común `Adjuntos · Histórico · Volver`.
- Presupuestos, pedidos, albaranes y facturas sustituyen su bloque particular `Datos Cliente` por el componente común `datosPersonaRelacion`, idéntico al utilizado en Recursos para empleados. Se eliminan los campos de teléfono y correo duplicados.
- Las pantallas de Gestión de presupuestos, pedidos, albaranes y facturas incorporan la acción `Modificar`; permanece oculta sin selección y en documentos ya convertidos.
# 2026-09-13 - Catálogo central de directivas

- Se crea `DIRECTIVAS.md` como catálogo único, numerado y estable de las directivas funcionales y visuales del frontend.
- `README.md` deja de duplicar reglas concretas y pasa a enlazar el catálogo; `CAMBIOS.md` conserva únicamente el histórico.
- Se recopilan las decisiones transversales confirmadas sobre Registro/Gestión, tablas, barras, componentes, personas, adjuntos, perfiles, módulos, agendas, catálogo y ventas.

## 2026-09-13 - Coherencia del precio público

- Se fija en el contrato del catálogo que el precio final mostrado es el importe unitario cobrado al cliente.
- La confirmación del pedido coincide exactamente con precio visible por cantidad; el backend deriva base e IVA sin introducir diferencias de céntimos.
- Verificación real con diez pedidos de la empresa 1 y cantidades simples y múltiples.
# 2026-09-14 — Chat y mensajería interna

- Se incorpora el componente reutilizable `chatInterno` en la barra superior y en Registro/Gestión de Comunicaciones.
- Se añaden conversación, respuesta, lectura/no lectura, contador de pendientes, modificación, eliminación y clasificación Normal/Aviso/Alerta.
- Se sustituye la selección por casillas por un selector único con destinatarios colectivos e individuales según el perfil y la empresa activa.
- Se ordena el formulario como Destinatario, Tipo, Asunto y Mensaje para mantener Asunto inmediatamente encima del contenido.
- El selector recibe todos los empleados activos de las cuatro empresas al garantizarse una cuenta vinculada para cada uno en backend.
- Cada mensaje de la bandeja y del hilo muestra Fecha, Hora, Emisor y Destinatarios.
- Se compactan los cajones y el área de respuesta; Normal usa contorno neutro, Aviso amarillo y Alerta rojo, manteniendo el texto y el fondo sin color de clasificación.
- Se eliminan los controles de reclasificación de los mensajes ya enviados; el tipo solo se establece al enviar cada mensaje o respuesta.
- Al abrir una conversación, el hilo se posiciona automáticamente al final para mostrar el mensaje más reciente.
- Se incorpora la baja lógica global de conversaciones y la administración de bajas con consulta, reactivación, eliminación definitiva individual y purga hasta una fecha.
- El alta de conversaciones permite dejar vacío el campo Mensaje; únicamente exige destinatario y Asunto.
- Se corrige el aviso superior para excluir completamente las conversaciones dadas de baja, aunque sus participantes conserven la relación histórica.
- Administración incorpora Registro y Gestión de Relaciones de Empresa; Productos sustituye el proveedor obligatorio de texto libre por un selector opcional de proveedores relacionados.
- Se añade refresco periódico sin recarga completa de la página.
- Verificación: compilación de producción de Angular.
# 2026-09-19 - Cálculo de beneficio basado solo en el precio

- Los botones de beneficio de Productos toman exclusivamente el precio sin IVA de compra o de venta como origen.
- Los descuentos, IVA y totales quedan fuera del cálculo del precio de destino y conservan su función para calcular cada total.
- Verificado el cálculo con un beneficio del 20 %: compra 10 genera venta 12 y venta 12 genera compra 9,60, aunque existan otros porcentajes en el formulario.
# 2026-09-19 - Disponibilidad de productos y componentes

- Añadido `Datos Disponibilidad` al Producto con selección independiente de los siete días de la semana.
- Incorporados Registro y Gestión de Componentes al módulo Productos, con tipo, identificación, importes, los 14 alérgenos oficiales de la UE y datos de movimiento. Los tipos disponibles son Ingrediente, Materia prima, Pieza, Material, Envase y Accesorio.
- Añadida FE-DIR-052 para normalizar el nombre de los apartados nuevos de formularios.
# 2026-09-19 - Componentes de ejemplo para la pizzería

- El Registro de Componentes dispone de 20 ejemplos de hostelería para Pizzeria La Esquina, clasificados por tipo y con sus datos económicos y alérgenos.
# 2026-09-20 - Consulta bajo demanda en Componentes

- Registro y Gestión de Componentes se abren sin ejecutar búsquedas; los datos se cargan únicamente al pulsar `Consultar`.
# 2026-09-20 - Igualdad entre Registro y Gestión de Productos

- Gestión de Productos adopta los apartados `Datos Importes Compra`, `Datos Importes Venta` y `Datos Disponibilidad` del formulario de Registro, manteniendo sus campos informativos en modo lectura.
- Añadidas FE-DIR-053 para exigir formularios equivalentes entre Registro y Gestión y FE-DIR-054 para revisar las directivas aplicables en cada desarrollo.
- Revisión de directivas: cumple separación Registro/Gestión, Datos Movimiento, nombres de apartados, notas informativas, integridad de columnas y reutilización de la utilidad común de precios.
# 2026-09-20 - Componentes de productos

- Añadido `Datos Componentes` a Productos mediante la tabla de edición común usada en ventas, con inserción de líneas, selector de componente, cantidad y eliminación.
- Gestión presenta el mismo apartado y contenido en modo informativo.
- Revisión de directivas: cumple igualdad Registro/Gestión, nombres de apartados, reutilización, aislamiento de componentes por empresa y revisión obligatoria de directivas.
# 2026-09-20 - Avisos compactos en el catálogo

- Reducidos el espaciado, la altura y el botón de cierre de los avisos del catálogo; en escritorio título, emisor y mensaje aprovechan una sola fila y en móvil conservan la disposición vertical.
- Revisión de directivas: cumple Directiva 1, adaptación móvil, estilo común de avisos y revisión obligatoria de directivas.
# 2026-09-20 - Catálogo de clientes en Empleados

- Añadida `Catálogo de clientes` al menú Ventas del módulo Empleados reutilizando la misma ventana del módulo Clientes.
- El botón `Cerrar` vuelve al módulo Empleados cuando el catálogo se abre desde este menú.
- Revisión de directivas: cumple reutilización obligatoria, igualdad funcional de la ventana, navegación móvil en la misma pestaña, Directiva 1 y revisión obligatoria de directivas.
# 2026-09-20 - Avisos del catálogo sin emisor

- Retirado el emisor de los avisos y alertas visibles en el catálogo; la fila compacta muestra únicamente título, mensaje y cierre.
- Revisión de directivas: cumple Directiva 1, adaptación móvil, coherencia visual y revisión obligatoria.
