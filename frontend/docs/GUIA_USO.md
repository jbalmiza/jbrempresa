# Guía de uso de la aplicación

Revisión: 2026-09-17. Alcance: comportamiento implementado; [limitaciones abiertas](../../backend/docs/LIMITACIONES.md).

## Acceso y empresa

1. Abrir http://localhost:4200/accesoLogin e iniciar sesión con las credenciales proporcionadas fuera de la documentación versionada.
2. Entrar en un módulo del panel. Disponibilidad empresarial y perfil determinan qué tarjetas se muestran.
3. Administrador: elegir la empresa en la cabecera antes de una operación concreta. “Todas” es ámbito global de consulta donde esté admitido; no sirve para abrir proveedores de una compradora indeterminada.
4. Jefe: opera su empresa. Cliente: catálogo. Empleado: agenda. La API aplica comprobaciones adicionales.

Si no arranca, comprobar frontend y backend por separado en [OPERACION.md](../../backend/docs/OPERACION.md).

## Consultar y mantener registros

Registro contiene el CRUD. Seleccionar una fila para ver/modificar/eliminar cuando el módulo lo ofrezca. Gestión contiene bajas, reactivaciones, histórico y operaciones, conforme a DIRECTIVAS. Las limitaciones de un maestro no autorizan excepciones generales.

Las tablas sin mapa/malla inicial abren vacías: pulsar Consultar o escribir un filtro. Configurar columnas permite mostrar campos ocultos y cambiar orden. La preferencia es por usuario/empresa/tabla. [TABLAS.md](TABLAS.md).

Adjuntos permite incorporar documentos/imágenes y elegir imagen principal. Datos Identificación y Movimiento son informativos según contexto.

## Registrar un proveedor y pedirle

1. En la empresa compradora, Administración > Registro > Relaciones de Empresa.
2. Insertar: elegir empresa suministradora, tipo Proveedor y fechas opcionales. Guardar crea la inversa Cliente.
3. Entrar en Proveedores y pulsar Catálogo > Mi catálogo. Una relación vigente abre el catálogo; varias permiten elegir.
4. Añadir artículos, abrir cesta y completar datos de contacto/entrega. Realizar pedido devuelve número y total.
5. El pedido queda en Ventas de la suministradora. No aparece automáticamente como compra en la compradora.
6. Sin relación activa y vigente no se permite consultar ni pedir desde Proveedores. Si la relación se da de baja después de abrir, el envío se rechaza.
7. Baja/Reactivar están en Gestión de Relaciones. Eliminar en Registro borra también la inversa previa confirmación.

Pizzería y restaurante tienen ejemplos registrados con Proveedor Hostelero Central; no son identificadores universales ni garantía de una base recién instalada.

## Clientes

Clientes utiliza el catálogo de su empresa. El QR o enlace público permite el acceso sin sesión. Cesta y pedido son comunes con Proveedores. La diferencia es la autorización/empresa vendedora; un catálogo publicado conserva su acceso público. [CATALOGO.md](CATALOGO.md).

## Mensajes internos

Los avisos y alertas con ubicación Mensajes aparecen como elementos informativos en la bandeja del botón Mensajes. Al abrirlos se marcan como leídos; no admiten respuesta. Los avisos de una ventana aparecen dentro de ella mientras estén vigentes y pueden ocultarse durante la visita actual. Empleados ofrece Avisos y Alertas para remitirlos al Jefe; el Jefe puede situarlos en la ventana inicial de Empleados o en los Mensajes del Administrador. Proveedores se identifica mediante la relación con una empresa y puede dirigirse al Administrador, al Jefe de una compradora relacionada o a su propio catálogo.

La cabecera permite Nuevo mensaje y Responder. Tipo Normal/Aviso/Alerta se fija al enviar. Abrir el hilo marca lectura.

Administrador: Administración > Registro > Mensajes permite modificar mensajes propios y eliminar conversaciones dadas de baja; la purga por fecha también está en Registro. Administración > Gestión > Gestión de Mensajes permite baja y reactivación. Cada baja afecta a todos los participantes. [MENSAJERIA_INTERNA.md](MENSAJERIA_INTERNA.md).

## Trabajo y agenda

Recursos registra Empleado/Maquinaria. Gestión permite operatividad y capacidades; los empleados disponen de agenda. Al entrar al módulo Empleados, el trabajador abre directamente Mi agenda; allí también ve los avisos dirigidos a la ventana inicial de Empleados. Administrador/Jefe pueden operar agendas autorizadas.

En Empleados, Registro ofrece Pedidos mediante el catálogo y Avisos y Alertas. Gestión de ventas permite al empleado consultar, modificar o eliminar los pedidos que creó, también después de que el jefe los modifique. La modificación propaga los cambios a los documentos asociados y solicita la clave de modificación de cadena si está configurada. Eliminar cadena borra también los documentos asociados, previa confirmación. El empleado no puede insertar desde esta gestión ni operar pedidos ajenos.

En la agenda individual, Vista ofrece 1 día y 1 semana; Intervalo ofrece 5, 10, 15 y 30 minutos y 1 hora. Ambos selectores son independientes. La vista múltiple usa un selector de intervalo. La configuración de cada agenda guarda su intervalo visual; al volver a abrirla se aplica el valor elegido. Configuración global aplica el mismo intervalo y horario a todos los empleados incluidos.

Las tareas usan Pendiente, En curso y Finalizado; el pago es independiente. Revertir estado/pago requiere los controles de confirmación de la interfaz. No confundir el estado de tarea con el de reserva. [RECURSOS.md](RECURSOS.md).

## Caja y comunicaciones

Caja administra apertura/cierre y movimientos internos; no cobra por una pasarela bancaria. Comunicaciones gestiona entradas externas, identificación supervisada, conversaciones y respuestas configuradas. Los mensajes internos de cabecera son otro modelo. WhatsApp necesita credenciales y proveedor externo operativos.

## Errores habituales

- Sin proveedor: comprobar tipo, empresa, activa y fechas.
- Catálogo indisponible: comprobar publicación e imagen de empresa, además del acceso.
- Imagen ausente: comprobar Adjuntos y origen TIPO/REGISTRO.
- Sesión caducada: volver a iniciar sesión.
- Compras: existe una desalineación de contrato pendiente; no usarla como evidencia de un circuito de aprovisionamiento completo.
