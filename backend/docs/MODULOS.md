# Módulos y estado funcional

Revisión: 2026-09-15. Fuentes: [inventario REST](API_INVENTARIO.md), [rutas Angular](../../frontend/docs/RUTAS.md), [menús](../../frontend/src/app/config/menu-modulos.config.ts).

| Módulo | Implementación actual |
|---|---|
| Administración | Empresas, usuarios, perfiles, parámetros, áreas, personal de área, módulos, relaciones de empresa y mensajería interna administrativa. |
| Clientes | Catálogo de la empresa de sesión; cesta y pedido mediante el catálogo común. También hay acceso público por token/alias. |
| Proveedores | Catálogos de empresas con relación PROVEEDOR vigente; pedidos en la suministradora. Sin compra espejo. |
| Empleados | Pedidos mediante catálogo, avisos, gestión de agenda y gestión de ventas propias; el empleado consulta, modifica o elimina pedidos de su empresa creados por él. Administrador/Jefe tienen ámbito ampliado validado. |
| Personas | Personas físicas/jurídicas, domicilio, contactos, representantes y domiciliaciones. La agenda pertenece al recurso Empleado, no a Persona. |
| Territorio | Países, provincias, municipios, códigos postales, vías y domicilios; dependencias y selección territorial. |
| Productos | CRUD/versiones, stock, tipos, precios, visibilidad, origen de imagen, malla y gestión de catálogo. |
| Servicios | CRUD/versiones, tipos, duración, precios, visibilidad y catálogo común. |
| Ventas | PRE/PED/ALB/FAC, detalles mixtos, movimientos, numeración, cadena y factura automática según parámetros. |
| Compras | Entidad y controlador básicos. Contrato Angular desalineado; no se declara un ciclo completo operativo. |
| Recursos | Empleado/Maquinaria, capacidades y operatividad. Agenda de empleados, reservas y tareas; maquinaria sin agenda equivalente completa. El intervalo visual se guarda en el recurso agendable de cada empleado y la configuración global puede aplicarlo a todos. |
| Caja | Maestro de cajas, sesiones de apertura/cierre, importes y movimientos. Registro interno no equivale a cargo bancario. |
| Comunicaciones | Bandeja externa, identidades, conversaciones, clasificación supervisada, propuestas, envío configurado y avisos/alertas. |

## Flujos transversales

El pedido de catálogo recalcula importes y disponibilidad, descuenta stock donde corresponda y llama a asignación y facturación. No todas las líneas requieren trabajo: depende de duración/capacidades y configuración.

La malla es un dibujo definido por cada empresa, no un tipo fijo de local. Adjuntos y configuración de tablas se reutilizan; no todos los maestros ofrecen idéntico histórico.

Las acciones permanentes de Registro/Gestión están en DIRECTIVAS. Disponibilidad del módulo en el panel, permisos de perfil y acceso a datos son comprobaciones distintas.

## Documentos relacionados

[Relaciones](RELACIONES_EMPRESA.md), [mensajes](MENSAJERIA_INTERNA.md), [avisos](AVISOS_ALERTAS.md), [catálogo](CATALOGO.md), [modelo](MODELO_DATOS.md), [límites abiertos](LIMITACIONES.md) y [evolución](PLANIFICACION_FUTURA.md).
