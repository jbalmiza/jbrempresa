# Planificacion futura de pedidos y recursos

## Estado

Recoge decisiones para desarrollo futuro. No describe funciones disponibles. Las cuestiones abiertas necesitan decision antes de implementar su fase.

## Objetivo

Calcular en tiempo real la finalizacion estimada de un pedido y organizar su trabajo. Debe servir para restauracion, talleres y otras empresas sin fijar conceptos como mesa, horno o elevador en el nucleo.

## Decisiones acordadas

- La confirmacion publica no puede terminar sin acciones.
- Mostrara referencia publica, resumen, estado y tiempo estimado.
- Permitira descargar comprobante, realizar otro pedido y finalizar.
- Podra ofrecer Bizum; mientras no este activo informara que no esta disponible.
- El comprobante sera ticket justificativo de pedido, no factura fiscal.
- Ventas incorporara Registro de empleados y Gestion de empleados.
- Un empleado reutiliza una Persona existente; no duplica datos personales.
- Se distinguen empleado activo, empleado operativo y capacidades activas.
- Las capacidades iniciales corresponden a tipos de producto y servicio.
- Cada linea genera trabajo planificable con duracion, cantidad, capacidad, estado y asignacion.
- Habrá una planificación única con vistas de agenda local y agenda de empleado.
- La agenda local muestra pedidos completos; la del empleado, tareas asignadas.
- El tiempo del pedido es cuando termina su ultima tarea, no la suma simple.

## Ejemplo acordado

Dos pizzas de 10 minutos generan 20 minutos de carga. Dos bocadillos de 5 minutos generan 10. Con un empleado operativo de pizzas y otro de bocadillos, ambas cargas avanzan en paralelo: la estimacion es 20 minutos, mas el margen que se decida.

El calculo considera la cola existente. Un empleado adicional con ambas capacidades recibe una tarea si adelanta la finalizacion.

## Modelo previsto

### Empleado

- Empresa y referencia a Persona.
- Activo: pertenece al equipo y conserva historico.
- Operativo: disponible actualmente para asignacion.
- Capacidades habilitadas y agenda propia con horarios y excepciones.

### Capacidad

Deriva inicialmente de tipos de producto y servicio, con referencia estable aunque cambie su nombre. Indica aptitud, no disponibilidad.

### Tarea de pedido

- Pedido y linea de origen.
- Capacidad, cantidad y duracion prevista.
- Empleado y, en otra fase, recursos materiales.
- Inicio/fin previstos y reales.
- Estado: pendiente, asignada, en curso, finalizada o cancelada.
- Prioridad e historico de reasignaciones.

### Agenda

Una sola colección de pedidos y tareas ofrece vista local, vista por empleado, reasignación manual y propuesta automática por capacidad, disponibilidad y carga. Las Personas no tienen agenda propia.

## Maquinaria y recursos

La maquinaria puede limitar el tiempo real: horno para cinco pizzas, dos elevadores, cabina, vehiculo o puesto. Se preve un modulo generico de Maquinaria/Recursos materiales con registro, tipo, activo, operativo, capacidades compatibles, capacidad simultanea, horarios, mantenimiento y reservas asociadas a tareas.

El nucleo usara `recurso material`; horno y elevador seran registros de cada empresa.

## Implantacion recomendada

### Fase 1: estimacion sencilla

- Duracion unitaria de productos y duracion existente de servicios.
- Empleados operativos y capacidades.
- Tareas agrupadas por linea.
- Asignacion propuesta al compatible con menor carga.
- Formula lineal `cantidad x duracion unitaria`.
- Paralelismo entre empleados, agendas local/personal y reasignacion manual.

Esta fase ofrece una estimacion util y explicable.

### Fase 2: recursos materiales

- Modulo de recursos, capacidad simultanea y reservas.
- Calculo combinado de disponibilidad de persona y maquina.
- Mantenimiento e indisponibilidades.

### Fase 3: optimizacion

- Lotes, division de lineas y duraciones no lineales.
- Prioridades, margenes y coordinacion.
- Replanificacion por ausencias, averias o retrasos.
- Comparacion de tiempos previstos y reales.

## Simplificacion recomendada

No introducir maquinaria en el primer calculo. La fase 1 indicara que estima solo con personas y duraciones lineales. El modelo de tareas quedara preparado para recursos sin rehacer pedidos ni agendas.

## Decisiones abiertas

- Division de una linea entre varios empleados.
- Trabajo por lotes frente a tiempo estrictamente lineal.
- Asignacion automatica directa o sujeta a aprobacion.
- Margen de entrega y coordinacion.
- Productos inmediatos como bebidas.
- Ausencia de personas con capacidad requerida.
- Nombre definitivo: Maquinaria o Recursos.
- Consulta posterior del estado mediante referencia publica.
