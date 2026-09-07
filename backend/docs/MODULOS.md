# Modulos funcionales

## Administracion

Empresas, usuarios, perfiles, areas, asignacion de personal, parametros y preferencias de tablas. Los parametros generales pertenecen a Administracion y los especificos conservan su modulo. La imagen de empresa es un adjunto principal.

## Territorio

Paises, provincias, municipios, codigos postales, vias y domicilios. Las dependencias impiden borrar niveles utilizados. Domicilios dispone de alta, modificacion, baja, historico y deshacer.

## Personas

Personas son clientes de ventas, proveedores u otros terceros, no empresas. Incluye selector, baja, historico y deshacer. Complementos: representantes, domicilios de notificacion y domiciliaciones bancarias. El IBAN se cifra y se devuelve enmascarado.

## Productos

Tipo, nombre, descripcion, clasificacion, stock, catalogo, malla y auditoria. La imagen principal se elige en Adjuntos. La malla muestra nombre, imagen, stock, fila y columna. Los tipos agrupan el catalogo.

## Servicios

CRUD, historico, baja, imagen principal, catalogo y agrupacion por tipo. Incorpora duracion y puede actuar como recurso de agenda.

## Ventas

Documentos `PRE`, `PED`, `ALB` y `FAC`, con lineas y movimientos. La cadena genera referencias posteriores. La posicion nace en pedido y pasa a albaran y factura. La direccion de envio es texto del pedido, sin crear domicilio maestro. Hay observaciones de linea y cabecera. El catalogo crea pedidos y descuenta stock. El cobro real no esta activo.

## Compras

Compras asociadas a proveedor/persona, fechas, estado, importes, auditoria y activo. Incluye alta, consulta, actualizacion, baja, historico y deshacer.

## Comunicaciones

Identidades de canal, bandeja, personas vinculadas, conversaciones, mensajes, intenciones, asignacion y propuestas. Recibe webhooks, evita duplicados externos y envia por el proveedor configurado. Una entrada requiere confirmar persona y clasificacion antes de vincularse.

## Agenda

Recursos, horarios, excepciones, reservas, recursos asignados, tareas y reprogramaciones. Un recurso agendable puede referenciar un empleado o un domicilio. La disponibilidad combina horarios, excepciones y reservas.

## Recursos

Módulo independiente con Registro, Gestión y Parámetros. Registra `EMPLEADO`, vinculado a una Persona activa, y `MAQUINARIA`. Sus registros siguen el histórico `A/M/B`, con baja, reactivación y deshacer. Distingue Activo de Operativo; cambiar la operatividad genera una versión `M`. Gestión asigna capacidades procedentes de tipos de producto y servicio y administra la agenda propia de cada empleado activo. La Persona vinculada no tiene agenda. Todavía no calcula tiempos ni genera tareas automáticamente.

## Catalogo publico

Productos y servicios visibles agrupados por tipo, movil y sin login. Cada posicion tiene token QR regenerable. El visitante selecciona articulos y aporta datos minimos para posicion o domicilio. Consulte [CATALOGO.md](CATALOGO.md).

## Capacidades transversales

Adjuntos lista, carga, descarga, elimina y marca imagen principal. Configuracion de tablas guarda preferencias por empresa, usuario y tabla; no sustituye Parametros.
