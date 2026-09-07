# Módulo de Recursos

Recursos dispone de Registro, Gestión y Parámetros.

Registro admite Empleado y Maquinaria. Un empleado obliga a seleccionar una Persona activa; maquinaria usa nombre y descripción propios. Activo pertenece al registro y Operativo se administra desde Gestión.

Gestión cambia operatividad y capacidades. Estas se generan desde tipos activos de producto y servicio y muestran su origen. Los empleados activos disponen además de Agenda, con horario semanal, excepciones, reservas, reprogramaciones y control de solapamientos. La agenda se identifica por el recurso Empleado y no por la Persona vinculada.

Gestión aplica el ciclo versionado común `A/M/B`: permite Ver, Modificar, Baja, Reactivar, Histórico y Deshacer. Las bajas se ocultan en la consulta general y la tabla muestra el aviso para recuperarlas filtrando `Tipo Movimiento` por `B`. Capacidades y Agenda solo se ofrecen sobre la versión vigente que no esté de baja. Los cambios de operatividad generan también una versión `M`.

Registro se limita al CRUD del maestro: Consultar, Insertar, Ver/Modificar y Eliminar. Baja, Reactivar, Histórico, Capacidades y Agenda pertenecen exclusivamente a Gestión.

El formulario agrupa `Id Empresa`, `Id Recurso` e `Id Histórico` en Datos Identificación. `Código` forma parte de Datos Recurso junto a Tipo, Nombre, Persona y Descripción.

La Persona vinculada a un empleado se elige con el buscador común de Personas. Permite localizar por nombre, documento, apellidos o razón social y sustituye al desplegable HTML.

La Persona aporta la identidad y los datos personales del empleado, pero no tiene agenda propia. Maquinaria todavía no dispone de agenda desde esta pantalla.

No existe compatibilidad visual ni funcional con antiguas agendas de Persona; los datos de prueba incompatibles se eliminan durante la migración.

## Datos de prueba actuales

La empresa 1 dispone de cinco empleados: especialista en Pizza, especialista en Bocadillo, empleado polivalente Pizza/Bocadillo y dos empleados de Taller. La maquinaria incluye un horno de Pizza, una estación de Bocadillo y dos elevadores de Taller. Todos se crearon activos y operativos.
