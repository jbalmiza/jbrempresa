# Cambios del backend

## 2026-09-07 - Inventario local de secretos

- Añadido `backend/docs/SECRETOS.md` como inventario exclusivamente local y excluido de Git.
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
