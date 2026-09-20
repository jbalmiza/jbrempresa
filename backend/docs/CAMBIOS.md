# Cambios del backend

## 2026-09-20 - Retirada del contrato de venta rápida

- Comprobado que `POST /documentos-venta/venta-rapida`, su entrada y la creación de cliente genérico eran consumidos exclusivamente por la pantalla Venta Táctil eliminada. Se retiran sin afectar al alta de pedidos por catálogo, Registro/Gestión documental, facturación ni agenda.
- Verificación: búsqueda completa de consumidores, compilación backend y revisión de contratos, permisos, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Resumen único de personalización

- El registro del pedido reconoce el resumen de componentes recibido en las observaciones y evita duplicarlo al validar y persistir la personalización.
- Verificación: compilación backend y revisión del contrato, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Personalización de componentes en pedidos de catálogo

- El contrato del catálogo entrega los componentes activos de la empresa y la composición de cada producto. Cada línea de pedido acepta la composición final elegida por el cliente.
- El servidor valida que todos los componentes pertenezcan a la empresa, calcula los suplementos con su IVA, mantiene el precio al retirar componentes y registra en las observaciones de preparación los componentes añadidos y eliminados.
- Ampliada a 2.000 caracteres la observación de detalle para conservar personalizaciones extensas. Los clientes anteriores que no envían componentes mantienen la composición original.
- Verificación: compilación backend y revisión del contrato, aislamiento empresarial, cálculo económico, compatibilidad, Directiva 1 y revisión obligatoria.

## 2026-09-20 - Componentes de los productos de la empresa 1

- Analizados los 30 productos activos de la empresa 1 y sus descripciones. Se han conservado los 20 componentes existentes, creado 38 componentes ausentes y registrado 128 relaciones con las versiones vigentes de los productos.
- Se han diferenciado componentes funcionalmente distintos, como tomate fresco y tomate triturado, pan de bocadillo, masa de pizza y pan de hamburguesa. Los alérgenos solo se han informado cuando se deducen del componente; las composiciones indeterminadas quedan sin una atribución inventada.
- Verificación: los 30 productos activos tienen componentes vinculados, todas las relaciones pertenecen a la empresa 1 y no existen relaciones creadas para otras empresas. Revisión de aislamiento empresarial, datos de desarrollo y Directiva 1 completada.

## 2026-09-20 - Alérgenos de productos en el catálogo

- El contrato del catálogo incorpora la lista consolidada y sin duplicados de alérgenos definidos en los componentes de la versión vigente de cada producto.
- La consulta carga componentes y relaciones por empresa para evitar consultas repetidas por cada producto. Los productos sin alérgenos y los servicios devuelven una lista vacía.
- Verificación: compilación backend y revisión de las directivas de aislamiento empresarial, funcionalidad general y revisión obligatoria.

## 2026-09-19 - IVA incluido en Total Compra

- Alcance: el servidor recalcula Total Compra aplicando descuento e IVA de compra.
- Verificación: compilación Maven y comprobación documental.

## 2026-09-19 - Total de compra sin aplicación de IVA

- Alcance: el servidor calcula Total Compra aplicando únicamente el descuento al precio de compra sin IVA; conserva el IVA de compra como dato informativo.
- Verificación: compilación Maven y comprobación documental.

## 2026-09-19 - Porcentaje de beneficio inicial para todas las empresas

- Alcance: todas las empresas existentes reciben `PRODUCTOS / PORCENTAJE_BENEFICIO = 20` al iniciar el backend y las nuevas empresas lo reciben al registrarse. La migración permite aplicar el mismo valor directamente en la base existente. Los totales de compra se calculan desde precio sin IVA, descuento e IVA.
- Verificación: compilación Maven, prueba de fórmula y comprobación documental.

## 2026-09-18 - Descuento y total de compra de productos

- Alcance: Productos incorpora `pro_des_com` y `pro_tot_com`; el servidor calcula el total de compra a partir del precio con IVA y el descuento. Se retira el indicador de control automático del margen, sustituido por dos acciones explícitas en Registro. La migración `20260918_margen_productos.sql` incluye los nuevos campos y elimina el indicador anterior si existe.
- Verificación: compilación Maven y comprobación documental.

## 2026-09-18 - Campos de IVA de compra y control de margen en productos

- Alcance: Productos conserva IVA de compra, control de margen y origen estimado del coste. La base de venta admite cuatro decimales para conservar los precios finales con IVA introducidos por el usuario. El parámetro `PRODUCTOS / MARGEN_BRUTO_OBJETIVO` acepta porcentajes desde 0 y menores de 100, por empresa. Se incorpora la migración `20260918_margen_productos.sql`.
- Verificación: compilación Maven y comprobación documental.

## 2026-09-18 - Directiva de funcionalidad general para todas las empresas

- Alcance: se registra BE-DIR-042 para exigir que las nuevas funciones estén disponibles para todas las empresas, con uso opcional según cada una.
- Verificación: comprobación documental.

## 2026-09-18 - Edición y eliminación de pedidos propios del empleado

- Alcance: el empleado puede modificar y eliminar un pedido creado por él mismo. La API verifica empresa, tipo PED y autor del movimiento de alta antes de tocar el pedido o su cadena. Siguen vedadas las operaciones de Gestión y la creación fuera de Venta Táctil; la modificación conserva la protección de cadena y su clave configurada.
- Verificación: pruebas dirigidas de controlador y repositorio con H2 y comprobación documental.

## 2026-09-18 - Consulta de pedidos propios del empleado

- Alcance: `GET /documentos-venta/PED` limita la respuesta del perfil Empleado a los pedidos de su empresa cuyo movimiento de alta corresponde a su usuario. El empleado no puede consultar otros tipos documentales ni ejecutar operaciones de registro o gestión; Venta Táctil sigue creando sus pedidos.
- Verificación: pruebas dirigidas de controlador y repositorio con H2, compilación Maven y comprobación documental.

## 2026-09-18 - Caducidad de sesión con actividad real

- Alcance: el JWT sigue durando 30 minutos y solo se renueva mediante `GET /usuarios/actividad`, enviado tras interacción real. Las consultas periódicas de mensajes ya no prolongan una sesión inactiva.
- Verificación: prueba dirigida del filtro JWT y compilación Maven.

## 2026-09-18 - Entrega de avisos a todas las empresas

- Alcance: `aviDestEmpId=null` representa todas las empresas para avisos del Administrador dirigidos a Empresa en Ventana o Mensajes. El servidor valida el emisor y distribuye el aviso a cada empresa durante su vigencia.
- Verificación: compilación Maven, prueba dirigida de permisos y comprobación documental.

## 2026-09-18 - Intervalo visual persistente por agenda

- Alcance: `ragIntVis` en recursos agendables, API y migración SQL; acepta 5, 10, 15, 30 o 60 minutos, con 30 para agendas existentes.
- Verificación: compilación Maven y comprobación documental.

Historial de cambios: las entradas conservan el estado de su fecha; la especificación vigente está en los documentos temáticos del [índice](README.md).

## 2026-09-17 - Precisión de la directiva de trabajo n.º 1

- Alcance: los cambios acotados deben resolverse con la solución y verificación proporcionales, evitando trabajo y explicaciones sin valor.
- Verificación: catálogo de directivas y registro documental revisados.

## 2026-09-17 - Destinatarios y ubicaciones de avisos y alertas

- Alcance: ampliados el maestro y la API con emisor, destinatario, empresa y ubicación; consultas separadas para catálogo de clientes, catálogo de proveedor, ventanas y bandeja Mensajes. La lectura por usuario y versión usa `avisos_alertas_lecturas`.
- Seguridad: el servidor comprueba el perfil del usuario emisor, la relación vigente cuando actúa una empresa proveedora, la empresa destinataria y la propiedad del aviso para gestionarlo. Los datos de prueba anteriores se clasifican como avisos del Jefe para el catálogo de clientes mediante inicialización repetible y migración SQL documentada.
- Verificación: compilación Maven y 10 pruebas dirigidas de distribución y catálogo; arranque con PostgreSQL y actualización de los avisos de prueba. API: entregas temporales de Jefe a Administrador, Proveedor al Jefe de la compradora y Jefe a la ventana Empleados, con lectura individual y aislamiento de empresa; los registros temporales se eliminaron. Inventarios y comprobador documental correctos.

## 2026-09-17 - Datos iniciales de relaciones

- Alcance: corregido `RELACIONES_EMPRESA.md` para indicar que el código actual no contiene un inicializador de relaciones y que los ejemplos creados manualmente no se reconstruyen automáticamente. Eliminado el enlace a la clase inexistente.
- Verificación: contraste con las fuentes y ejecución del comprobador documental completo.

## 2026-09-17 - Corrección de LIM-03

- Alcance: `LIMITACIONES.md` refleja que el código actual carece de un inicializador de relaciones de empresa; las relaciones de ejemplo creadas manualmente no se reconstruyen automáticamente en una base nueva. Sustituida la referencia a la clase inexistente por el servicio real.
- Verificación: búsqueda de la clase y de inicializadores de relaciones en las fuentes; comprobación del enlace corregido y del verificador documental. Queda otro enlace a esa clase en `RELACIONES_EMPRESA.md` para su revisión independiente.

## 2026-09-17 - Actualización del inventario de entidades

- Alcance: regenerado `MODELO_INVENTARIO.md` desde las clases `@Entity` actuales, con 56 entidades. No se modifica el modelo ni la base de datos.
- Verificación: el comprobador documental deja de señalar este inventario como desactualizado; permanecen dos enlaces documentales rotos, tratados por separado.

## 2026-09-17 - Actualización del inventario de API

- Alcance: regenerado `API_INVENTARIO.md` desde los controladores actuales, con 267 endpoints. No se modifica el contrato ni el código de la API.
- Verificación: el comprobador documental deja de señalar este inventario como desactualizado; permanecen otros avisos documentales independientes.

## 2026-09-17 - Directiva de trabajo n.º 1

- Alcance: incorporada la directiva permanente BE-DIR-041, identificada como directiva de trabajo n.º 1 y aplicable a todos los desarrollos. Exige autonomía, análisis y cambios completos, corrección de causas raíz y verificaciones proporcionales al riesgo; evita únicamente trabajo redundante sin reducir la calidad.
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

## 2026-09-15 - Mensajería administrativa y cabecera

- El servicio exige Administrador también para dar de baja conversaciones y modificar mensajes. Se mantienen las restricciones administrativas de consulta de bajas, reactivación y eliminación; no cambian URL ni DTO. Directiva BE-DIR-039.
- Verificación: Maven: 3 pruebas de permisos correctas para Jefe, Empleado y Cliente, sin acceso ni mutación de repositorios. Backend compilado y arrancado el 2026-09-15 con los cambios: Tomcat en puerto 8080, estado ACCEPTING_TRAFFIC y respuesta HTTP 403 del endpoint protegido de mensajes sin autenticación.

## 2026-09-14 - Acceso integral a las acciones de agenda

- Administrador y Jefe pueden avanzar estados y marcar pagos desde cualquier agenda de empleado a la que tengan acceso dentro de la empresa activa.
- El Empleado conserva el acceso exclusivamente a su propia agenda.
- La autorización se valida en el backend sobre la empresa y la agenda de la tarea; no existe un modo de agenda limitado a solo lectura.
- Verificación: pruebas Maven y comprobación funcional de API.
- Corregida la restricción `ck_tarea_estado`, que conservaba el valor obsoleto `TERMINADA` y rechazaba el estado funcional `FINALIZADO`.
- Se incorpora una migración repetible al arranque y el script `20260914_estado_finalizado_tareas.sql` para normalizar datos existentes y el contrato de la base de datos.
- La API de tareas admite la transición confirmada a `PENDIENTE` desde `EN_CURSO` o `FINALIZADO`, limpiando las fechas reales y sincronizando reserva y pedido.
- La marca de pago se convierte en una operación booleana para permitir tanto marcar como desmarcar el pedido.
- El contrato de reservas expone `pagado` para que Gestión de Agendas presente la acción correcta.

## 2026-09-13 - Consulta de agendas de empleados por el Administrador

- `GET /empleados/mi-agenda` admite el parámetro opcional `recursoAgendaId` para la consulta delegada del Administrador.
- La agenda solicitada debe ser de tipo Empleado, pertenecer a la empresa seleccionada y corresponder a un recurso activo y operativo.
- Jefes y empleados continúan restringidos a su propia agenda y no pueden consultar la de otro recurso enviando el parámetro.

## 2026-09-13 - Directiva de integridad de columnas

- Toda consulta tabular debe poder proporcionar todos los campos funcionales del registro; la ocultación inicial es una decisión de presentación y no elimina el campo de la tabla configurable.
- Las relaciones deben acompañarse de su descripción funcional cuando esté disponible, evitando limitar la presentación al identificador técnico.

## 2026-09-11 - Datos de empresa, contacto y domicilio fiscal

- El maestro Empresa incorpora razón social, NIF/CIF, actividad, teléfono, correo electrónico, página web y domicilio fiscal.
- El domicilio se persiste mediante `dom_id` y, al modificar, se valida que el domicilio activo pertenezca a la propia empresa.
- El contrato JSON de Empresa expone `empRazSoc`, `empNif`, `empActEco`, `empTel`, `empEma`, `empWeb` y `domId`.
- La migración `20260911_datos_empresa_contacto_domicilio.sql` amplía la tabla `empresas` de forma repetible.
- Verificación: migración aplicada en PostgreSQL y pruebas Maven correctas.

## 2026-09-11 - Plantilla operativa del proveedor hostelero

- Se registran tres personas empleadas en Proveedor Hostelero Central: almacén, reparto y mantenimiento.
- Cada empleado dispone de un usuario propio con perfil `Empleado`, recurso operativo, agenda laboral de lunes a viernes y capacidades acordes a su función.
- Almacén recibe tareas de productos de Ingredientes, Bebidas, Consumibles y Limpieza; reparto y mantenimiento reciben sus respectivos servicios.
- Las credenciales locales de prueba se incorporan a `CREDENCIALES_USUARIOS.md`, archivo no versionado destinado a su custodia.
- Verificación: migración aplicada en PostgreSQL y relaciones Persona–Usuario–Recurso–Agenda comprobadas.

## 2026-09-11 - Adjuntos según la empresa seleccionada por el administrador

- El controlador común de adjuntos deja de tomar directamente la empresa fija incluida en el JWT y utiliza `ContextoOperacion`.
- Para el administrador global, las consultas, contenidos y operaciones de adjuntos respetan `X-Empresa-Seleccionada`; el resto de perfiles continúa restringido a su empresa del token.
- Esto permite que la imagen principal de la cabecera corresponda a la empresa elegida en el selector global.
- Verificación: pruebas Maven del backend correctas.

## 2026-09-11 - Cuarta empresa proveedora y catálogos iniciales

- Se registra la empresa de prueba `Proveedor Hostelero Central` con identificador `4` y acceso inicial a los módulos configurables por el administrador global.
- Se incorporan diez productos de suministro hostelero en las categorías Ingredientes, Bebidas, Consumibles y Limpieza, destinados a las necesidades habituales de la pizzería y el restaurante.
- Se incorporan los servicios `Entrega refrigerada programada` y `Mantenimiento de equipamiento hostelero`.
- Las cuatro empresas disponen de una imagen raster 3D diferenciada, almacenada en la ruta común de imágenes y vinculada también como adjunto principal del registro Empresa.
- Para los adjuntos de Empresa se normaliza `RUTA_DOCUMENTOS_EMPRESAS` bajo `data/adjuntos/empresa-{id}/Empresas`; el resto de rutas documentales existentes no cambia.
- La migración `20260911_proveedor_hostelero_empresa_imagenes_catalogo.sql` es repetible: actualiza empresa, rutas e imágenes y evita duplicar módulos, tipos y artículos.
- Verificación: migración aplicada correctamente en PostgreSQL; existen cuatro empresas, diez productos y dos servicios activos para la empresa `4`, y una única imagen principal por empresa.

## 2026-09-11 - Directiva de acciones de Registro y Gestión

- `Modificar`, `Eliminar` y las eliminaciones en cascada quedan definidas como acciones exclusivas de Registro.
- Las pantallas de Gestión se reservan para consulta, bajas, reactivaciones, históricos y procesos operativos; no modifican ni eliminan el maestro.
- En Ventas, la eliminación completa de la cadena documental se inicia exclusivamente desde Registro.

## 2026-09-11 - Repositorio local estable para Maven Wrapper

- El wrapper deja de depender del valor anómalo de `user.home` que dirigía el repositorio Maven a `C:\.m2\repository`.
- La configuración estándar `.mvn/maven.config` fija la caché de dependencias en `backend/.m2/repository`, una ruta escribible y propia del proyecto.
- `backend/.m2/` queda excluido de Git porque contiene exclusivamente distribuciones y dependencias descargadas.
- El procedimiento ordinario vuelve a ser `./mvnw.cmd test` o `./mvnw.cmd package`, sin variables ni argumentos adicionales.
- Verificación: `./mvnw.cmd -q test` ejecutado directamente, con 8 pruebas correctas y repositorio resuelto en `backend/.m2/repository`.

## 2026-09-11 - Modificación transaccional de pedidos y documentos asociados

- Se incorporan por empresa los parámetros `REQUERIR_CLAVE_MODIFICACION_CADENA` y `CLAVE_MODIFICACION_CADENA`; inicialmente no se exige clave y su valor queda vacío.
- La clave es un control operativo definido por el Jefe y se conserva como texto en el parámetro, sin hash ni tratamiento como credencial de acceso.
- La API de modificación de pedidos exige confirmar la propagación cuando existen documentos asociados y, si está configurado, valida la clave recibida.
- Pedido, factura y, cuando los albaranes están habilitados, albarán se actualizan dentro de una sola transacción. Se sincronizan cliente, ubicación, importes, observaciones y líneas, conservando números, fechas, tipos y estados propios de cada documento.
- La factura y el albarán registran el movimiento `MODIFICACION_CADENA` en su histórico.
- Corregida la detección de empresas activas del inicializador para que cree efectivamente ambos parámetros en las empresas actuales y futuras.
- Verificación: 8 pruebas Maven correctas, parámetros comprobados en las tres empresas y backend reiniciado correctamente en el puerto 8080.

## 2026-09-10 - Fotografías activas en el catálogo

- Los tipos de producto y servicio de demostración que todavía apuntaban a pictogramas SVG pasan a utilizar sus fotografías PNG ya disponibles en el directorio común de imágenes.
- La actualización incluye los tipos de restauración, reparto, eventos y taller de todas las empresas actuales y se ejecuta de forma idempotente al iniciar el backend.
- Se mantienen sin cambios las imágenes principales particulares de productos o servicios cuando el parámetro de catálogo selecciona el origen `REGISTRO`.
- Verificación: 8 pruebas Maven correctas.

## 2026-09-10 - Imagen fotográfica para Carnes

- El tipo de producto `Carnes` de la empresa de restauración deja de utilizar el pictograma vectorial genérico y pasa a mostrar la fotografía rasterizada `carne.png` ya disponible en el directorio común de imágenes.
- La actualización de los datos de demostración es idempotente y contempla tanto la denominación antigua `Carne` como la vigente `Carnes`.
- No se modifican las imágenes principales que cada producto pueda tener administradas mediante Adjuntos.

## 2026-09-10 - Contexto empresarial seleccionable

- El Administrador puede enviar la empresa de trabajo mediante la cabecera común `X-Empresa-Seleccionada`.
- `0` o la ausencia de cabecera representan `Todas las empresas`; un identificador positivo limita las consultas y operaciones al contexto seleccionado.
- El backend solo acepta este cambio de contexto para el perfil Administrador. Para Jefe, Empleado y Cliente ignora la cabecera y conserva obligatoriamente la empresa del token.
- Los controladores que ya admiten consulta global distinguen entre `Todas las empresas` y una empresa concreta mediante `empresaConsulta`.
- Verificación: 8 pruebas Maven correctas.

## 2026-09-10 - Regla común de consulta multempresa

- El ámbito de consulta queda centralizado: Administrador puede consultar todas las empresas y los demás perfiles quedan forzados a la empresa de su sesión.
- Las consultas principales de Productos, Servicios, Tipos de artículo, Recursos, Domicilios, Documentos de venta, Compras, Cajas y Avisos/Alertas devuelven al Administrador los registros de todas las empresas.
- `Mi agenda` deja de excluir expresamente al Administrador y conserva la resolución mediante la Persona y el recurso Empleado vinculados al usuario.
- Las operaciones de escritura continúan exigiendo una empresa concreta para impedir modificaciones ambiguas sobre el conjunto global.
- Verificación: 8 pruebas Maven correctas.

## 2026-09-10 - Fase 5: finalización y reparto

- El estado de cada tarea actualiza también su reserva y el pedido relacionado.
- El pedido pasa a `EN_CURSO` al comenzar la primera elaboración y a `FINALIZADO` cuando terminan todas sus tareas de producto y servicio.
- Los pedidos a domicilio generan entonces una sola tarea `SERVICIO · REPARTO`; los pedidos en posición no generan entrega.
- El reparto se asigna al primer repartidor disponible, dura inicialmente 30 minutos y ocupa su agenda, por lo que los pedidos anteriores determinan el horario de los siguientes.
- En una tarea de reparto, `EN_CURSO` representa pedido recogido y `FINALIZADO` representa pedido entregado; al entregarlo, el pedido pasa a `ENTREGADO`.
- La creación es idempotente y no permite dos repartos activos para el mismo pedido.
- `Pagado` se persiste como una marca booleana independiente del estado y puede establecerse desde cualquier tarea propia del pedido, antes, durante o después de la elaboración y entrega.
- Verificación: 8 pruebas Maven correctas, empaquetado correcto, actualización automática de `dov_pag` en PostgreSQL y backend reiniciado en el puerto 8080.

## 2026-09-10 - Fase 4: asignación automática de pedidos

- La confirmación de un pedido del catálogo crea automáticamente una tarea por cada línea completa de producto o servicio.
- La habilidad requerida se obtiene del Tipo de Producto o Tipo de Servicio y se compara con las capacidades activas de los recursos Empleado.
- La cantidad de una línea nunca se divide: su duración total es la duración unitaria multiplicada por la cantidad y se asigna a una sola agenda.
- Entre los empleados capacitados se elige el que tenga el primer hueco disponible, respetando horarios, márgenes, excepciones, capacidad y reservas existentes.
- La búsqueda se realiza en intervalos de cinco minutos durante los siguientes treinta días. Si no existe recurso o hueco, el pedido se rechaza de forma transaccional con un mensaje explicativo.
- La asignación crea la reserva, la ocupación del recurso y la tarea en estado `PENDIENTE`; la entrega y el reparto permanecen reservados para la fase 5.
- La inicialización de datos omite la sincronización de secuencias exclusiva de PostgreSQL cuando las pruebas usan H2, permitiendo verificar el contexto completo.
- Verificación: 7 pruebas Maven correctas, incluidas cantidad indivisible y rechazo sin empleado capacitado; empaquetado correcto y backend reiniciado en el puerto 8080.

## 2026-09-10 - Agenda personal del Jefe

- El Jefe se considera también trabajador y recibe un recurso operativo de tipo Empleado y una agenda personal en su empresa.
- La agenda inicial se configura de lunes a viernes, de 09:00 a 18:00, y puede modificarse desde Gestión de Recursos.
- No se asignan habilidades productivas o de reparto automáticamente; podrá recibir tareas generales.
- `Mi agenda` admite Empleado y Jefe y resuelve siempre la agenda desde las credenciales autenticadas.
- La inicialización reutiliza recursos y agendas existentes para evitar duplicados.

## 2026-09-10 - Fase 3: acceso del empleado a su agenda

- Incorporada la API personal `GET /empleados/mi-agenda`, que resuelve la Persona, el recurso y la agenda exclusivamente desde las credenciales autenticadas.
- El empleado no puede indicar ni consultar la agenda de otro trabajador; los perfiles diferentes de Empleado reciben una denegación de acceso.
- Incorporado el avance controlado de tareas propias mediante `PUT /empleados/mi-agenda/tareas/{id}/estado`.
- Las transiciones permitidas son `PENDIENTE → EN_CURSO → FINALIZADO`, registrando las horas reales y el usuario del movimiento.
- En esta fase la agenda puede aparecer vacía hasta incorporar la asignación automática de líneas de pedido.
- Verificación: compilación Maven correcta y consulta real con credenciales de Empleado limitada a su propia agenda.

## 2026-09-10 - Fase 1 del modelo de tareas de pedido

- Ampliada la tarea de agenda para relacionarla opcionalmente con una línea de pedido, un producto o un servicio.
- Incorporados tipo de tarea, habilidad, cantidad, duración unitaria, planificación e instantes reales de inicio y finalización.
- La combinación Empresa y Línea de pedido es única, garantizando una sola tarea para la cantidad completa de cada línea.
- El contrato de Agenda devuelve los nuevos datos manteniendo compatibles las reservas manuales existentes.
- Esta fase no genera ni asigna todavía tareas automáticamente.
- Verificación: compilación completa del backend correcta mediante Maven.

## 2026-09-10 - Fase 2: trabajadores de reparto

- El empleado de demostración de cada empresa se registra como recurso operativo vinculado a su Persona, sin crear usuarios duplicados.
- Cada repartidor recibe la capacidad `SERVICIO · REPARTO` y una agenda individual.
- Si no existía una configuración previa, se crea un horario inicial de 09:00 a 23:00 todos los días, modificable posteriormente desde Gestión de Agendas.
- La inicialización es idempotente: reactiva y reutiliza recurso, capacidad y agenda existentes.
- La numeración de recursos operativos se calcula globalmente para respetar su clave primaria compartida entre empresas.
- Verificación: compilación Maven correcta, backend reiniciado y comprobados por API el repartidor, la capacidad `REPARTO`, la agenda y sus siete días de horario en las tres empresas.

## 2026-09-08 - Tipos de producto y servicio en plural

- Normalizados en plural todos los tipos actuales de producto y servicio.
- Actualizados conjuntamente el maestro, todas las versiones de Productos y Servicios y las capacidades de Recursos para conservar la integridad funcional.
- La migración `20260908_tipos_articulo_plural.sql` es repetible y conserva los tipos que ya estaban en plural.

## 2026-09-08 - Validación del stock actual

- Cuando un producto controla stock, las altas y modificaciones rechazan un stock actual vacío o negativo.
- La API deja de confirmar actualizaciones que no contienen un valor de stock persistible.
- La migración `20260908_recuperar_stock_actual.sql` recupera el último stock no nulo del histórico para las versiones vigentes afectadas por el comportamiento anterior.

## 2026-09-08 - CRUD y publicación de Avisos/Alertas

- Creado el maestro histórico multiempresa con periodo opcional, baja y reactivación.
- Ampliado el catálogo público para devolver únicamente mensajes activos y vigentes.
- Registrados tres ejemplos en cada empresa mediante migración idempotente.
- Verificado con 5 pruebas Maven sin fallos y en PostgreSQL.

## 2026-09-08 - Retirada completa del servicio Recogida

- Eliminados de todas las empresas el tipo `RECOGIDA`, los servicios clasificados con ese tipo y las capacidades de recursos asociadas.
- La migración global es repetible y elimina también posibles registros de prueba equivalentes creados en otras empresas.
- Verificada su aplicación sobre la base de datos de desarrollo: se retiraron un servicio y una capacidad restantes.

## 2026-09-08 - Distintivos de Productos y Servicios

- Añadidos los indicadores persistentes `Novedad`, `Mejor precio` y `Outlet` a Productos y Servicios, con valor inicial `false`.
- Ampliado el contrato público del catálogo con `novedad`, `mejorPrecio` y `outlet`.
- La migración `20260908_distintivos_catalogo.sql` conserva los datos existentes y puede aplicarse repetidamente.
- Verificado con las pruebas Maven del backend.

## 2026-09-08 - Tipos genéricos de catálogo

- Registrados en todas las empresas los tipos `Productos` (producto) y `Servicios` (servicio).
- Añadidas imágenes vectoriales coherentes con el estilo actual del catálogo en el directorio común configurado para cada empresa.
- La migración es idempotente: reactiva y actualiza la imagen de los tipos si ya existen, sin duplicarlos.
- Verificado mediante consulta directa de los registros creados y de sus ficheros asociados.

## 2026-09-08 - Preferencias iniciales de columnas técnicas

- Añadida la migración `20260908_columnas_movimiento_ocultas.sql` para ocultar `empId`/`cliId`, `*TipMov` y `*CauMov` en las configuraciones de tabla ya guardadas.
- La actualización es única y conserva la posibilidad de que cada usuario vuelva a mostrar y guardar esas columnas.
- Migración aplicada en PostgreSQL de desarrollo: una configuración actualizada y ninguna columna técnica objetivo permanece visible.

## 2026-09-08 - Origen configurable de imágenes del catálogo

- Añadido `IMAGEN_CATALOGO_ORIGEN` de forma independiente para `PRODUCTOS` y `SERVICIOS`.
- Los valores admitidos son `TIPO` y `REGISTRO`; `TIPO` conserva el comportamiento previo.
- Centralizada la selección en `ImagenCatalogoService` y validado el valor al guardar parámetros.
- Añadida la migración idempotente `20260908_origen_imagen_catalogo.sql` para crear ambos parámetros por empresa.
- Cubierta mediante pruebas unitarias la selección por defecto, la selección por registro y la validación de valores.
- Verificado con `mvn test`: cinco pruebas ejecutadas sin fallos; migración aplicada a las tres empresas de desarrollo.

## 2026-09-07 - Inventario local de secretos

- Añadido `SECRETOS.md` en la raíz del proyecto como inventario exclusivamente local y excluido de Git.
- Registrados los datos de conexión conocidos, las variables obligatorias, las integraciones sensibles y las credenciales aisladas de pruebas.
- Los valores reales no disponibles se marcan pendientes, sin inventarlos ni incorporarlos a archivos versionados.

## 2026-09-07 - Limpieza de diagnósticos Java

- Sustituidos los usos obsoletos de `JsonNode.asText()` por `asString()` en WhatsApp.
- Eliminadas importaciones y sobrecargas internas sin uso.
- Reescritas referencias de método problemáticas como lambdas explícitas para respetar el análisis de nulabilidad de JSpecify/Eclipse.
- Eliminado el posible acceso nulo a `MultipartFile` mediante una salida de error explícita.
- Conservado el comportamiento funcional de catálogo, agenda, documentos de venta, configuración y mensajería.
- Verificado mediante compilación limpia y prueba de contexto: 1 prueba, sin fallos ni errores.

## 2026-09-07 - Verificación del módulo de Caja

- Verificada la creación ORM de `cajas`, `caja_sesiones` y `caja_movimientos` en una base limpia.
- Confirmado que las consultas están aisladas por empresa y que apertura, cierre, registro y anulación validan el estado de la sesión.
- El esquema activo debe actualizarse reiniciando el backend con `spring.jpa.hibernate.ddl-auto=update`; los datos existentes se consideran de prueba.
- Verificado con la prueba de contexto Maven: 1 prueba ejecutada, sin fallos ni errores.
- La configuración de ejecución de VS Code deja de contener credenciales o claves y referencia `JWT_SECRET`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `BANK_DATA_ENCRYPTION_KEY` y `CORE_DATA_ENCRYPTION_KEY` desde el entorno.

## 2026-09-03 - Productos y servicios en Venta táctil

- Se confirma que toda funcionalidad de Ventas admite líneas de Producto (`dvdTipLin=P`, `proId`) y Servicio (`dvdTipLin=S`, `serId`).
- Venta táctil pasa a consumir el contrato existente de venta rápida para ambos tipos de línea; no se requiere modificación del endpoint.
- El backend conserva la validación del identificador según el tipo y normaliza nombre y duración desde la entidad correspondiente.

## 2026-09-03 - Directiva de movimientos de Registro y Gestión

- Registro no mantiene histórico ni incorpora Tipo o Causa de movimiento; conserva Usuario, Fecha y Activo.
- Gestión incorpora histórico y los campos Tipo y Causa, además de Usuario, Fecha y Activo.
- Los datos de movimiento se exponen como información de solo lectura en los formularios.

## 2026-09-03 - Renovación de sesión por actividad

- Añadido `GET /usuarios/actividad` como punto autenticado ligero para la señal de actividad del frontend.
- El punto no modifica datos: permite que `JwtFilter` valide y renueve el JWT dentro de la ventana existente de cinco minutos.
- Se conserva una duración de 30 minutos, aplicada ahora como periodo de inactividad efectiva cuando el cliente utiliza la señal.
- Verificado con `mvn test`: una prueba ejecutada sin fallos ni errores.

## 2026-09-02 - Histórico y baja de Recursos

- Convertidos Recursos a clave histórica `reoId + reoIdHis` y movimientos `A/M/B`.
- Añadidas modificación versionada, baja, consulta de histórico, reactivación y deshacer.
- El cambio de operatividad crea una versión `M`; las capacidades continúan ligadas al identificador estable del recurso.
- Las agendas de empleados dados de baja o no operativos dejan de ofrecerse y rechazan nuevas reservas.
- La migración convierte los recursos de prueba existentes en su versión inicial `A` y aplica unicidad de código únicamente a versiones activas.
- Verificado con `mvn test`, migración en PostgreSQL local, empaquetado y arranque en `localhost:8080`.

## 2026-09-02 - Eliminación desde Registro de Recursos

- Restaurado `DELETE /recursos/{id}` para el CRUD de Registro.
- La eliminación definitiva se limita a recursos sin histórico ni agenda; Baja continúa siendo una acción exclusiva de Gestión.
- Verificado con `mvn test`.

## 2026-09-02 - Agenda de empleados

- Sustituido el recurso agendable `PERSONA` por `EMPLEADO`, referenciado mediante `reoId`.
- La creación y actualización de agenda valida que el recurso sea un empleado activo de la empresa autenticada y fuerza capacidad uno.
- Añadida migración que conserva horarios, excepciones y reservas al trasladar las agendas existentes al empleado vinculado.
- La migración crea una agenda inicial para cada empleado activo que todavía no la tenga.
- Las antiguas agendas de Persona sin empleado asociado y sus datos dependientes de prueba se eliminan; el modelo no conserva el tipo ni compatibilidad de legado.
- Verificado con `mvn test`, empaquetado del JAR, aplicación de la migración en PostgreSQL local y arranque en `localhost:8080`.

## 2026-09-01 - Recursos de prueba

- Se crearon cinco empleados operativos para Pizza, Bocadillo y Taller.
- Se creó un empleado polivalente con capacidades Pizza y Bocadillo.
- Se crearon horno, estación de bocadillos y dos elevadores con sus capacidades.

## 2026-09-01 - Módulo Recursos

- Recursos Empleado y Maquinaria, vinculando empleados a Personas.
- Estados Activo y Operativo y capacidades de producto/servicio.
- API, repositorios y tablas multiempresa.

## 2026-09-01 - Diseno futuro de planificacion

- Se documentaron confirmacion, referencia y comprobante de pedido.
- Se definieron empleados vinculados a Personas, operatividad y capacidades.
- Se definio una planificacion unica con agenda local y por persona.
- Se incorporo la evolucion futura de maquinaria/recursos materiales.
- Se propuso una implantacion por fases para simplificar tiempos.

## 2026-09-01 - Revision integral de documentacion

- Se amplio el indice del backend para cubrir arquitectura, modulos, API, modelo de datos, seguridad, ficheros, integraciones y operacion.
- Se documento el alcance real de los 25 controladores y de las entidades por area funcional.
- Se separo expresamente funcionalidad implementada de preparacion futura, especialmente Redsys/Bizum.
- Se registraron limites operativos actuales: `ddl-auto=update`, ausencia de migraciones versionadas, cobertura de pruebas limitada, sin Actuator ni rate limiting documentado.
- Se consolidaron las reglas multiempresa, malla generica, imagen principal mediante Adjuntos y cadena pedido-albaran-factura.

## 2026-09-01 - Mapa de local de prueba

- Creada una malla de Pedidos de prueba con cuatro superficies que representan mesas y caminos negros entre ellas.
- La distribución es únicamente un ejemplo de datos; la malla continúa siendo genérica para cualquier tipo de empresa o espacio.

## 2026-09-01 - Distribución de productos en el almacén de prueba

- Asignada la ubicación `Almacén principal`, fila y columna a los 20 productos activos de la empresa de prueba.
- Sincronizadas sus posiciones azules en la malla sin ocupar las celdas negras, reservadas como caminos.

## 2026-09-01 - Preparación de Bizum mediante Redsys

- Añadida configuración Redsys independiente por empresa dentro del módulo `VENTAS`.
- La clave secreta `PAGO_BIZUM_CLAVE` se cifra con AES-GCM y se devuelve enmascarada desde la API de parámetros.
- Añadido el contrato `PasarelaPago` para aislar la lógica de pedidos del proveedor y el adaptador inicial `RedsysBizumGateway`.
- La pasarela permanece desactivada y rechaza operaciones hasta disponer de credenciales y completar la firma Redsys real.
- Añadido `GET /catalogo/gestion/pago` para comprobar si la configuración está preparada y conocer los parámetros pendientes sin exponer secretos.

## 2026-09-01 - Agrupación del catálogo por tipo

- El catálogo público agrupa ahora los productos por `pro_tip_pro` y los servicios por `ser_tip_ser`.
- Reclasificados los datos de prueba de pizzas y bocadillos con los tipos `PIZZA` y `BOCADILLO`.

## 2026-09-01 - Ruta general e imagen principal de empresa

- Trasladada `RUTA_IMAGENES` de empresa desde el módulo inexistente `EMPRESAS` a `ADMINISTRACION`.
- El adjunto conserva `EMPRESAS` como clasificación funcional del registro, pero utiliza la configuración general de Administración al marcarse como principal.
- Preparada la migración de las imágenes actuales de empresa a adjuntos principales.

## 2026-09-01 - Lámina descargable del código QR

- La descarga del QR incorpora debajo del código la ubicación, fila y columna de la posición.
- Añadido ajuste automático del tamaño del texto para ubicaciones extensas.

## 2026-09-01 - Separación de configuración técnica del catálogo

- La configuración del Catálogo solo recibe publicación y permiso de pedidos a domicilio.
- Guardar el Catálogo ya no crea ni modifica `RUTA_IMAGENES` de Productos, Servicios o Empresas.
- Las rutas continúan administrándose como parámetros independientes de cada módulo.

## 2026-09-01 - Alineación de la gestión del catálogo

- Sin cambios de API ni modelo; la corrección afecta exclusivamente al layout del frontend.

## 2026-09-01 - Publicación del catálogo de prueba

- Activado `CATALOGO_PUBLICADO` para la empresa de prueba, que no tenía creado el parámetro y por ello devolvía `404`.
- Verificada la respuesta del enlace público con 20 productos y 20 servicios.
- Se mantiene desactivada la modalidad de pedidos a domicilio.

## 2026-09-01 - Vista previa de posiciones de producto

- No se modifica el contrato del backend: la malla ya entrega la posición y Productos proporciona el stock necesario para la vista previa del frontend.

## 2026-08-31 - Actualización de posición en productos

- Corregido el orden de persistencia de movimientos históricos para desactivar y confirmar la versión vigente antes de insertar la nueva.
- Aplicada la misma corrección a modificación, baja y deshacer de Productos y Servicios.
- Se mantiene la restricción de una sola versión activa por empresa y registro.

## 2026-08-31 - Imagen principal desde Adjuntos

- Añadido `adj_pri` para identificar de forma exclusiva el adjunto de imagen principal de un registro.
- Añadido `PUT /adjuntos/{id}/principal`, limitado a imágenes de Productos, Servicios y Empresas de la empresa autenticada.
- Al seleccionar el principal, el archivo se copia a la ruta `RUTA_IMAGENES` del módulo y se actualiza `pro_ima`, `ser_ima` o `emp_ima`.
- Se impide eliminar el adjunto principal hasta seleccionar otra imagen.
- Verificado con `mvn test` sobre H2.

## 2026-08-31 - Consulta de imagen de empresa

- Añadido `GET /empresas/{id}/imagen` para la previsualización autenticada del formulario.
- Aplicada `emp_ima` a la base local; la ruta de sus imágenes queda configurada mediante `RUTA_IMAGENES` de `ADMINISTRACION`.
- Asignada una imagen predeterminada a las tres empresas de prueba y creadas sus carpetas independientes.

## 2026-08-31 - Servicios e imágenes predeterminadas

- Añadidos `ser_vis_cat` y `ser_ima`, con carga y consulta autenticada.
- Incorporados Servicios al catálogo y al pedido como línea `S` con `ser_id`.
- Productos y Servicios se crean visibles y con imagen predeterminada.
- Actualizados 20 productos y 20 servicios de prueba.
- Creados parámetros `RUTA_IMAGENES` para `PRODUCTOS` y `SERVICIOS`.

## 2026-08-31 - Revisión de ubicación de productos

- Verificado que la actualización existente recibe y persiste `proUbi`, `proFilMal` y `proColMal`; la incidencia corregida estaba en la carga del formulario frontend y no requiere cambiar el contrato API.

## 2026-08-31

- Añadidos catálogo público, configuración por empresa y endpoints de gestión.
- Añadida tabla permanente de posiciones públicas y tokens regenerables.
- Añadida generación de QR mediante ZXing.
- Añadido almacenamiento de imágenes por módulo, empresa y tipo.
- Añadidas imágenes principales de Producto y Empresa.
- Añadida validación de imagen para productos visibles y publicación del catálogo.
- Añadidos origen, modalidad, dirección, ubicación y posición a pedidos.
- Añadidas observaciones por línea de venta.
- Añadida creación o reutilización de Persona por teléfono desde el catálogo.
- Añadida creación de pedidos públicos en estado `EMITIDO`.
- Añadido cálculo seguro de precios en servidor.
- Añadido descuento y devolución de stock en pedidos de catálogo.
- Conservada la ocupación de la malla hasta el estado `PAGADO`.
- Añadidas rutas públicas limitadas en Spring Security y CORS configurable para red local.
- Añadida configuración de pruebas con H2 sin acceso a PostgreSQL real.
- Verificado con compilación, empaquetado y `mvn test`.
## 2026-09-02 - Nombre completo canónico de personas

- El backend calcula siempre `perNomCom` al crear o modificar una Persona: `documento - nombre y apellidos` para personas físicas y `documento - razón social larga` para jurídicas.
- Las relaciones externas continúan almacenando `perId`, por lo que muestran el nombre vigente de la Persona sin copiar ni desnormalizar su denominación.
- Añadida la migración `20260902_nombre_completo_personas.sql` para normalizar todos los datos existentes, incluido el histórico.
## 2026-09-02 - Documentación de personas jurídicas de prueba

- Informado `C.I.F.` como tipo de documento y restaurados los CIF de las dos personas jurídicas de prueba.
- Recalculado su nombre completo con el formato `CIF - razón social larga`.
- Corregida la carga de prueba para usar los campos vigentes `per_tip_doc` y `per_doc` en lugar del antiguo `per_cif`.
- Verificado en PostgreSQL que las personas jurídicas y sus nombres completos cumplen el formato esperado.
## 2026-09-02 - Eliminación del código de Recurso

- Eliminado `reo_cod` del modelo, repositorio, validaciones y base de datos porque el Recurso se identifica mediante `reo_id` y el código no participa en ninguna relación funcional.
- Retirado también el índice de unicidad asociado mediante `20260902_eliminar_codigo_recursos.sql`.
- Verificado con pruebas Maven y aplicación de la migración en PostgreSQL.
## 2026-09-02 - Productos de carta de prueba

- Registrados 50 productos visibles en catálogo para la empresa de prueba: 10 carnes, 10 pescados, 10 entrantes, 10 bebidas y 10 postres.
- Informados nombre, descripción, categoría, subcategoría, precios, IVA, stock, duración e imagen predeterminada.
- La migración es repetible y evita duplicar productos existentes por nombre.
- Verificada en PostgreSQL la existencia de 10 altas activas por cada tipo solicitado.

## 2026-09-03 - Base funcional del módulo de Caja

- Añadidas las tablas `cajas`, `caja_sesiones` y `caja_movimientos` mediante `20260903_modulo_caja_base.sql`.
- El maestro de cajas dispone de CRUD; la baja y reactivación se tramitan desde Gestión y no se permite dar de baja una caja con sesión abierta.
- Cada caja admite como máximo una sesión abierta. El cierre calcula el efectivo esperado y la diferencia frente al importe contado.
- Los movimientos soportan `ENTRADA`, `SALIDA`, `COBRO` y `DEVOLUCION`, con medio de pago e importe positivo; pueden relacionarse opcionalmente por `per_id` y `dov_id`.
- Las sesiones cerradas y sus importes constituyen el histórico operativo inicial. Los movimientos solo se pueden registrar o anular mientras la sesión está abierta.
- La vinculación automática entre pagos de documentos de venta y Caja queda expresamente pendiente hasta definir las reglas del negocio.
- Migración aplicada en PostgreSQL local y backend compilado correctamente con Maven.

## 2026-09-03 - Contrato de Venta Táctil

- Añadido `POST /documentos-venta/venta-rapida` para registrar directamente pedidos emitidos desde el TPV táctil.
- El servidor obtiene o crea por empresa la Persona `CLIENTE-GENERICO - Cliente Genérico`; el pedido conserva la relación mediante `per_id`.
- Los importes, nombres, duraciones y totales de las líneas siguen normalizándose en servidor con las mismas reglas que el resto de documentos de venta.
- Las unidades repetidas de un producto se reciben agrupadas en una sola línea con su cantidad y observación común.
- Verificado con `mvn test`: una prueba ejecutada sin fallos ni errores.

## 2026-09-03 - Ampliación de productos de carta de prueba

- Añadida la migración repetible `20260903_productos_vinos_pastas_hamburguesas_prueba.sql`.
- Registrados 10 vinos, 10 platos de pasta y 10 hamburguesas para la empresa de prueba.
- Cada familia utiliza un `pro_tip_pro` propio (`VINO`, `PASTA` y `HAMBURGUESA`) para aparecer como grupo independiente en Venta Táctil.
- Informados precios, IVA, descripción, subcategoría, stock, duración, visibilidad e imagen predeterminada.
- Migración aplicada y verificada en PostgreSQL: 10 altas activas y visibles en cada uno de los tres tipos.
# 2026-09-07 - Separación de datos de demostración por empresa

- Se definen tres empresas de prueba: `Pizzeria La Esquina`, `Taller Bosco de coches` y `Restaurante Cándida`.
- Los usuarios 1, 2 y 3 quedan asignados respectivamente a las empresas 1, 2 y 3 con perfil administrador; los usuarios de prueba restantes se eliminan.
- Cada empresa dispone exactamente de tres empleados relacionados mediante `per_id`, con nombres y funciones coherentes, y una agenda inicial por empleado.
- Pizzeria La Esquina conserva productos de tipo Bocadillo, Pizza y Hamburguesa e incorpora Reparto, Recogida y Preparación para celebraciones.
- Taller Bosco recibe los veinte servicios existentes de taller y conserva dos elevadores como maquinaria.
- Restaurante Cándida recibe Carne, Pescado, Entrante, Bebida, Postre, Vino y Pasta e incorpora Reserva de mesa, Menú degustación y Celebración de eventos.
- Se eliminan documentos de venta, reservas, mallas y relaciones operativas de prueba incompatibles antes de reasignar los maestros, conforme a la directiva de datos de desarrollo.
- La migración transaccional de reorganización queda registrada en `database/migrations/20260907_separacion_empresas_demo.sql` como carga única para el estado de prueba conocido.
- Verificación en PostgreSQL `saasdb`: tres empresas activas, usuarios 1/2/3 asignados respectivamente, tres empleados y tres agendas por empresa, 30 productos en Pizzería, 70 en Restaurante, 3/20/3 servicios por empresa y cero relaciones Empleado–Persona o Agenda–Empleado con empresa incoherente.
- La etiqueta visible de cada usuario contiene únicamente su identificador funcional y el nombre de su Persona asociada (`1 · Nombre Apellidos`); no repite las palabras `Usuario` ni el nombre de la empresa, ya presentes en la cabecera.
## 2026-09-07 - Sustitución segura de horarios de agenda

- El reemplazo del horario semanal ejecuta una eliminación masiva inmediata en base de datos y fuerza su confirmación antes de insertar las nuevas franjas.
- Se evita la colisión con la restricción única cuando una franja nueva coincide con otra existente del mismo empleado.
- Verificación: pruebas automatizadas del backend y empaquetado ejecutados correctamente con Maven 3.9.16.
- Añadida una prueba JPA que registra, elimina y vuelve a registrar el horario de jueves a domingo de 19:30 a 23:30, comprobando cuatro franjas sin colisión de unicidad.
## 2026-09-07 - Maestro común de tipos de artículo

- Se añade el maestro multiempresa `tipos_articulo` para productos y servicios, con alta, modificación, baja e imagen por tipo.
- El catálogo público resuelve la imagen mediante el tipo del producto o servicio y deja de exigir una fotografía individual.
- Se registran todos los tipos actualmente utilizados por las tres empresas y se asigna una imagen genérica a cada uno.
- Los archivos se almacenan bajo `data/imagenes/empresa-{id}/tipos`; el catálogo ya no recurre a la imagen individual del artículo.
- Inventario verificado: 17 asociaciones empresa/clase/tipo activas, todas con archivo de imagen existente.
- La carga queda reproducible mediante `database/migrations/20260907_tipos_articulo_imagenes.sql`.
- Verificación: pruebas Maven (2 pruebas, 0 fallos) y empaquetado ejecutados correctamente; backend reiniciado en `localhost:8080`.
- Se añaden 17 SVG bajo el directorio común `data/imagenes/empresa-{id}/tipos` y se reasignan los 17 tipos activos a estos archivos.
- El catálogo sirve los SVG con el tipo MIME `image/svg+xml`; los PNG generados anteriormente se conservan sin estar asignados.
- Verificación: los 17 SVG son XML válido, las 17 referencias activas de base de datos terminan en `.svg`, Maven supera 2 pruebas sin fallos y el backend queda activo en `localhost:8080`.

## 2026-09-07 - Imagen principal de tipos mediante Adjuntos


- La operación genérica para marcar una imagen principal admite `TIPO_PRODUCTO` y `TIPO_SERVICIO`.
- La imagen seleccionada se copia al directorio común `empresa-{id}/tipos` y actualiza la referencia del maestro `tipos_articulo`.
- El contrato reutiliza los módulos y parámetros documentales existentes de Productos y Servicios; no introduce rutas particulares de carga en el CRUD de tipos.

## 2026-09-07 - Catálogo y productos de Taller Bosco

- Corregida la ausencia del catálogo público de la empresa 2: existían el token, la imagen de empresa y veinte servicios visibles, pero faltaba el parámetro activo `CATALOGO_PUBLICADO`.
- Registrados diez productos vendibles de taller con precios, IVA, stock y visibilidad de catálogo.
- Añadido el tipo de producto `REPUESTO`, reutilizando la ilustración vectorial `taller.svg` ya disponible para la empresa; no se incorpora una imagen nueva sin decisión visual del usuario.
- La carga queda reproducible e idempotente mediante `database/migrations/20260907_catalogo_productos_taller_bosco.sql`.

## 2026-09-07 - Orden estable del catálogo público

- El contrato del catálogo devuelve primero todos los tipos de producto y después todos los tipos de servicio.
- Dentro de cada tipo, los productos o servicios se devuelven por identificador ascendente, garantizando una presentación estable en cualquier cliente.
- El cambio mantiene el mismo DTO y no modifica los campos del contrato HTTP.
- Verificación: compilación correcta y 2 pruebas Maven superadas; comprobación en `localhost` de 10 productos (`101` a `110`) antes de los 20 servicios (`1` a `20`), todos ordenados por identificador dentro de su tipo.
## 2026-09-09 - Configuración de módulos por empresa y usuario

- Incorporado el maestro de módulos de aplicación y su API CRUD.
- Añadida configuración de disponibilidad y orden aislada por empresa.
- El panel utiliza el orden administrativo aislado por empresa.
- Las imágenes de módulo se gestionan exclusivamente mediante Adjuntos y su marca principal.
- Añadida migración idempotente y carga inicial de los módulos conocidos al arrancar.
- Verificación: el frontend consumidor compila correctamente. La ejecución automatizada de Maven queda pendiente porque el wrapper existente no puede iniciarse en el entorno (`Cannot start maven from wrapper`).
## 2026-09-09 - Eliminación del orden personal de módulos

- Retirados la entidad, el repositorio y los endpoints de orden de módulos por usuario.
- La posición efectiva procede exclusivamente de `empresas_modulos` y se aplica a todos los usuarios de la empresa.
- Añadida una migración de limpieza para eliminar la tabla obsoleta `usuarios_modulos_orden` si llegó a crearse.
## 2026-09-09 - Guardado transaccional del orden de módulos

- La disponibilidad y las posiciones de todos los módulos se guardan en una única transacción.
- Se validan identificadores duplicados y posiciones ausentes antes de persistir.
- La API fuerza la escritura y devuelve el orden realmente almacenado para que el frontend lo verifique inmediatamente.
## 2026-09-09 - Gestión de Usuarios

- Añadidas baja lógica, reactivación e histórico de usuarios con causa obligatoria.
- Se impide la baja del usuario conectado y el inicio de sesión de usuarios inactivos.
- La consulta ordinaria excluye bajas; se recuperan mediante filtro explícito de Activo.
- Añadidos Tipo y Causa de movimiento al maestro y una tabla inmutable de movimientos.
- Verificación backend pendiente de Maven por el fallo preexistente de arranque del wrapper.
## 2026-09-09 - Perfiles funcionales y usuarios de demostración

- Definidos los perfiles `Administrador`, `Jefe`, `Cliente` y `Empleado` con alcance funcional explícito.
- El administrador es único; Administrador y Jefe acceden a todos los módulos, Cliente únicamente a Clientes y Empleado únicamente a Empleados.
- El arranque crea de forma idempotente un Jefe, Cliente y Empleado por empresa activa y el administrador global `jackalblue` en la empresa principal.
- Si existía el acceso provisional `jackablue`, se renombra automáticamente a `jackalblue` sin duplicar administradores.
- Las contraseñas se almacenan exclusivamente mediante BCrypt. Las credenciales en claro no se documentan en el repositorio.
- El endpoint del panel filtra los módulos según el perfil autenticado.
- El inicializador sincroniza las secuencias de Perfiles y Usuarios antes de insertar datos, contemplando bases de prueba con identificadores cargados manualmente.
## 2026-09-09 - Reparación del Maven Wrapper en Windows

- Corregido `mvnw.cmd` para manejar correctamente un repositorio local `.m2` que no sea un enlace simbólico.
- Se evita el error `No se puede indizar en una matriz nula` al ejecutar el wrapper desde PowerShell o CMD.
- Verificación completada correctamente mediante `mvnw.cmd -q -DskipTests package`.
## 2026-09-09 - Consulta global de empresas

- El perfil Administrador obtiene todas las empresas en Registro de Empresas.
- El perfil Jefe y los perfiles de empresa conservan el aislamiento y solo pueden obtener su propia empresa.
- El Administrador puede consultar, modificar y gestionar la imagen de cualquier empresa; la misma operación permanece bloqueada entre empresas para el resto de perfiles.
## 2026-09-09 - Alcance multempresa del módulo Administración

- Administrador consulta por defecto todas las empresas en Usuarios, Perfiles, Parámetros, Áreas Organizativas y Personal de Área, además del Registro de Empresas.
- Los filtros enviados antes de consultar, incluido `Empresa`, se aplican sobre el conjunto multempresa.
- Jefe permanece limitado en el backend a la empresa de su sesión, con independencia de los filtros o identificadores recibidos.
- Las operaciones sobre registros existentes conservan la empresa del registro; un Jefe no puede operar sobre registros de otra empresa.
- Verificación: compilación completa del backend correcta.
## 2026-09-09 - Filtro de empresa global

- El filtro `T` de la columna Empresa identifica al usuario Administrador global en la consulta remota de Usuarios.
- Los identificadores numéricos de las empresas permanecen sin cambios en persistencia y contratos.
## 2026-09-09 - Depuración de usuarios de prueba

- Eliminados los accesos antiguos que no figuran en el documento local de credenciales.
- Se conservan exclusivamente `jackalblue` y los usuarios Jefe, Cliente y Empleado generados para cada empresa activa.
- La limpieza elimina previamente recuperaciones de contraseña e históricos asociados a las cuentas descartadas y se repite de forma idempotente al arrancar.
## 2026-09-09 - Gestión de Módulos por empresa

- Gestión de Módulos pasa a ser una operación exclusiva del perfil Administrador y el backend devuelve `403` al resto de perfiles.
- La consulta y el guardado reciben obligatoriamente una empresa válida y trabajan sobre su configuración independiente en `empresas_modulos`.
- La disponibilidad y el orden guardados determinan obligatoriamente el panel de módulos de los usuarios de esa empresa.

## 2026-09-09 - Acceso exclusivo a Módulos, Perfiles y Empresas

- Todos los endpoints de mantenimiento de Módulos, Perfiles y Empresas devuelven `403` a cualquier perfil distinto de Administrador.
- Se mantiene accesible la lectura de la imagen de la propia empresa porque la cabecera de la aplicación la necesita.
- Se incorpora `GET /perfiles/selector`, limitado siempre a la empresa autenticada, para asignar perfiles desde el Registro de Usuarios sin exponer el CRUD de Perfiles.
- Verificación: compilación completa del backend correcta mediante Maven.

## 2026-09-09 - Operaciones completas de Empresas

- La API exclusiva del Administrador incorpora alta, eliminación, baja, reactivación e histórico de Empresas.
- Baja y reactivación exigen causa y registran el movimiento de forma transaccional en `empresas_movimientos`.
- La consulta ordinaria excluye bajas y admite `incluirBajas=true` para búsquedas expresas por estado.
- Se impide eliminar o dar de baja la empresa técnica vinculada al Administrador global.
- Corregida una referencia fuera de alcance en la consulta auxiliar de Usuarios que impedía compilar el backend.
- Verificación: compilación completa del backend correcta mediante Maven.

## 2026-09-09 - Privacidad del Administrador global

- El usuario global `jackalblue` solo se devuelve en consultas realizadas por el perfil Administrador.
- La exclusión se aplica a la consulta paginada, a la consulta auxiliar de usuarios y al acceso indirecto para baja, reactivación e histórico.
- La restricción se ejecuta en el backend y no depende de ocultar filas en el navegador.
- Verificación: compilación completa del backend correcta mediante Maven.

## 2026-09-09 - Enlace corto de pedidos por empresa

- Incorporado el parámetro empresarial `CATALOGO_ALIAS`, generado de forma única a partir del nombre de la empresa.
- Las operaciones públicas aceptan tanto el alias legible como los tokens generales y de posición anteriores.
- La actualización valida el formato en minúsculas con guiones y rechaza alias asignados a otra empresa.
- Verificación: compilación completa del backend correcta mediante Maven.
## 2026-09-10 - Módulo inicial de Proveedores

- Registrado `PROVEEDORES` en el catálogo inicial de módulos para que se incorpore a todas las empresas.
- El Administrador y el Jefe pueden recibir acceso según la disponibilidad configurada para cada empresa; Cliente y Empleado conservan sus módulos exclusivos.
# 2026-09-11 — Configuración documental, facturación automática y personas incompletas

- Se incorporan los parámetros por empresa `MOSTRAR_PRESUPUESTOS`, `MOSTRAR_ALBARANES` y `TIPO_FACTURA_AUTOMATICA`.
- Presupuestos y albaranes quedan ocultos por defecto en las empresas actuales; el acceso directo a sus API también se valida.
- Cada pedido nuevo, incluido el procedente del catálogo o de una conversión, crea automáticamente su factura y copia sus líneas e importes.
- Las empresas 1 y 3 generan facturas simplificadas; la empresa 2 genera facturas normales.
- El mismo maestro de facturas admite los tipos `NORMAL` y `SIMPLIFICADA`, también en su histórico.
- Las facturas normales no pueden emitirse con una persona marcada con datos incompletos.
- Se añade `per_dat_com`. Los procesos externos pueden crear personas incompletas con nombre y contacto; un teléfono o correo diferente impide reutilizar una persona existente.
- Verificación: compilación Maven correcta con Java 25 y Maven 3.9.16.
- Ajuste posterior: el teléfono pasa a ser obligatorio en el CRUD de Personas. La completitud física exige tipo de persona, tipo y número de documento, nombre, primer apellido, domicilio y teléfono; la jurídica exige tipo de persona, tipo y número de documento, razón social corta, domicilio y teléfono. La razón social larga queda opcional.
# 2026-09-13 - Catálogo central de directivas

- Se crea `DIRECTIVAS.md` como catálogo único, numerado y estable de las directivas funcionales, de datos y seguridad del backend.
- `README.md` deja de duplicar reglas concretas y pasa a enlazar el catálogo; `CAMBIOS.md` conserva únicamente el histórico.
- Se recopilan las decisiones transversales confirmadas sobre empresa, perfiles, personas, Registro/Gestión, adjuntos, facturación, pedidos y agendas.
# 2026-09-13 - Domicilios en el contexto de empresa del Administrador

- Las operaciones de alta, modificación, eliminación, baja, histórico y deshacer de domicilios usan ahora el contexto empresarial común.
- El Administrador puede crear y mantener domicilios de la empresa seleccionada sin que queden asociados por error a su empresa de autenticación.
- Este ajuste permite completar correctamente el domicilio fiscal de cualquier empresa desde la sesión global.
# 2026-09-13 - Validación integral de pedidos públicos y agenda

- El pedido público toma como importe contractual el precio final mostrado en el catálogo, evitando diferencias de céntimos al recalcular base e IVA por cantidades.
- La base y la cuota de IVA se derivan del total final mostrado, manteniendo el detalle fiscal y haciendo coincidir cesta, pedido y factura.
- Eliminar un pedido o su cadena elimina también sus tareas, asignaciones y reservas de agenda; ya no quedan ocupaciones huérfanas.
- Se completa la información empresarial y fiscal de las cuatro empresas con datos de prueba coherentes y domicilios normalizados.
- Verificación realizada mediante diez pedidos reales por `/catalogo/publico/pizzeria-la-esquina/pedidos`: 10 pedidos emitidos y no pagados, 10 facturas simplificadas emitidas con el mismo total, 14 reservas y 14 tareas pendientes.
- Las 14 líneas conservan una única tarea por línea, incluidas dos líneas de cuatro unidades; se asignaron 5 tareas de Pizzas al maestro pizzero y 9 de Bocadillos/Hamburguesas al recurso de Cocina, sin solapes considerando los márgenes de agenda.
# 2026-09-14 — Chat y mensajería interna

- Se añade el modelo persistente de conversaciones, participantes, lectura individual y mensajes internos.
- Se publica la API de destinatarios, bandeja, contador, lectura/no lectura, creación, respuesta, modificación, clasificación y eliminación lógica.
- Se aplican permisos por perfil y contexto de empresa en el servicio, independientemente de la interfaz.
- Se corrige la resolución de destinatarios para consultar el perfil mediante `perId`; `usuPerId` permanece reservado para la Persona vinculada al usuario.
- Se reconcilian al arrancar las cuatro empresas: se crea una cuenta de prueba para cada recurso empleado activo que carezca de ella y se eliminan las cuentas Cliente no operativas. El Administrador global es la única excepción a la correspondencia Jefe/Empleado.
- Se amplía el contrato de conversaciones y mensajes con emisor y destinatarios explícitos para garantizar su presentación completa.
- Se retira la operación de reclasificación posterior; Normal, Aviso o Alerta queda fijado al enviar el mensaje o la respuesta.
- Se sustituye la eliminación individual de mensajes por la baja lógica global de conversaciones, con auditoría, consulta y reactivación administrativa y eliminación física individual o masiva hasta fecha.
- El contrato de alta exige Asunto pero admite el contenido inicial del Mensaje vacío o nulo.
- Se filtran las conversaciones dadas de baja tanto en la bandeja activa como en el contador de mensajes pendientes.
- Se incorpora el maestro de relaciones Proveedor/Cliente con pareja inversa automática y los selectores de empresas y proveedores relacionados.
- Verificación: compilación y pruebas Maven.
# 2026-09-19 - Regla del cálculo de beneficio de Productos

- Documentado que los botones calculan el precio de destino exclusivamente desde el precio sin IVA de origen y el porcentaje de beneficio de la empresa.
- Descuentos, IVA y totales no intervienen en esa conversión.
# 2026-09-19 - Disponibilidad de productos y componentes

- Ampliado Producto con disponibilidad por cada día de la semana, activa por defecto.
- Añadido el maestro histórico de Componentes, sus importes, los 14 indicadores de alérgenos, endpoints de Registro y operaciones de Gestión.
- Añadida la migración `20260919_disponibilidad_productos_componentes.sql`.
# 2026-09-19 - Componentes de hostelería de ejemplo

- Registrados 20 componentes para Pizzeria La Esquina: ingredientes, materia prima, envases, material y accesorios.
- Informados precios adicionales, IVA y alérgenos aplicables.
- Añadida la carga idempotente `20260919_componentes_hosteleria_pizzeria.sql` y comprobado que una segunda ejecución no duplica registros.
# 2026-09-20 - Revisión obligatoria de directivas

- Añadida BE-DIR-043 para exigir que cada desarrollo revise y registre el cumplimiento de las directivas aplicables antes de finalizar.
# 2026-09-20 - Componentes de productos

- Añadida la composición de productos mediante líneas de componente y cantidad, separada por empresa y versión histórica del producto.
- El backend valida pertenencia a la empresa, componente activo, cantidad positiva y ausencia de duplicados.
- Añadida la migración `20260920_productos_componentes.sql`.
- Revisión de directivas: cumple aislamiento por empresa, versionado de Gestión, separación Registro/Gestión, integridad del contrato y funcionalidad general.
