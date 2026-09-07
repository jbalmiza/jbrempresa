# Experiencia futura de pedidos y planificacion

Describe pantallas futuras; no implica que esten implementadas. El modelo completo esta en `backend/docs/PLANIFICACION_FUTURA.md`.

## Confirmacion publica

La pantalla movil mostrara referencia, fecha, resumen, importe, estado y tiempo estimado. Nunca dejara al usuario sin accion. Permitira descargar comprobante, realizar otro pedido, finalizar y acceder a Bizum; mientras siga inactivo, Bizum mostrara su indisponibilidad. El comprobante se identificara como pedido sin valor fiscal.

## Ventas

El menu incorporara:

- Registro de empleados: CRUD que selecciona una Persona.
- Gestion de empleados: operatividad y capacidades.
- Agenda local: pedidos, carga y finalizacion estimada.
- Agenda de empleado: horarios, excepciones, reservas, tareas, estado y reasignación.

Las capacidades se presentaran inicialmente mediante tipos de producto y servicio. Activo y Operativo seran controles distintos.

## Recursos materiales

En una fase posterior se incorporara Maquinaria/Recursos para visualizar capacidad e indisponibilidad de hornos, elevadores u otros recursos configurados por la empresa, sin convertir los ejemplos en campos fijos.

## Criterio visual

La planificación permitirá comparar empleados, tareas y horas con una escala temporal común, estados, filtros y reasignación directa. La Persona vinculada aporta identidad, pero la agenda pertenece exclusivamente al empleado.
