# Evolución pendiente

Revisión: 2026-09-15. Fuente: código actual, directivas y [visión](../../docs/VISION_PRODUCTO.md).

## Ya implementado

Recursos Empleado/Maquinaria, capacidades/operatividad, agenda de empleado, horarios/excepciones/reservas, asignación de tareas por pedido, estados operativos, factura automática y catálogos de proveedores relacionados. Su ubicación real es Recursos/Empleados/Ventas, no el antiguo Registro de empleados dentro de Ventas.

## Pendiente funcional

- Resolver Compras y Datos Movimiento/histórico de Relaciones.
- Confirmación pública con comprobante, repetir pedido y tiempo estimado completo.
- Agenda/capacidad combinada de maquinaria y empleado; mantenimiento y ocupación material.
- Replanificación por ausencia/avería, prioridades y comparación de tiempos.
- Pagos externos y conciliación; una marca Pagado no acredita un cargo.
- Automatización IA por empresa/proceso/acción con niveles PROPONER, CONFIRMAR, AUTOMATICO y BLOQUEAR.
- Nuevos canales y asistencia sobre datos reales.

La división de líneas no forma parte de la implementación autorizada actual: la directiva conserva una tarea por línea agrupada. Cualquier propuesta de lotes/división debe respetar o revisar explícitamente esa decisión.

## Antes de producción

Migraciones repetibles, restauración ensayada, controles por operación, rate limiting, observabilidad y pruebas integrales. No se fija un plazo ni se declara ninguna fase terminada por haberla documentado.

[Limitaciones verificadas](LIMITACIONES.md) separa defectos conocidos de objetivos; [pruebas](PRUEBAS.md) informa la cobertura actual.
