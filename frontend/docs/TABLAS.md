# Tablas configurables

Revisión documental: 2026-09-15. Describe el árbol de trabajo actual.

## Vista inicial y carga de datos

- Una pantalla con Malla abre la Malla como vista predeterminada y carga sus datos automáticamente.
- Si no dispone de Malla pero sí de Mapa, abre el Mapa y carga sus datos automáticamente.
- Si no dispone de Malla ni Mapa, abre la Tabla vacía. Los registros solo se solicitan al pulsar `Consultar`.
- Incorporar posteriormente una Malla o un Mapa a un Registro o Gestión convierte esa vista en la predeterminada.
- Desde una Malla o un Mapa, la primera pulsación de `Consultar` cambia a una Tabla vacía; una segunda pulsación carga todos los registros.
- Escribir un filtro en una Tabla vacía solicita los datos y muestra directamente las coincidencias, conservando el filtro introducido. En tablas con consulta remota, la petición se retrasa brevemente para agrupar la escritura.

El componente compartido `tabla` permite que cada usuario elija el orden y la visibilidad de las columnas. La preferencia se guarda por empresa, usuario y clave de tabla mediante `/configuraciones-tabla`.

## Visibilidad predeterminada

Las siguientes columnas técnicas comienzan ocultas en todas las tablas:

- Id Empresa (`empId` y el antiguo `cliId`).
- Tipo Movimiento (campos terminados en `TipMov`).
- Causa Movimiento (campos terminados en `CauMov`).
- Id Histórico (campos terminados en `IdHis`, comportamiento ya existente).

Oculta no significa eliminada: todas permanecen en `Configurar tabla` y el usuario puede activarlas. Al guardar, su elección personal prevalece en las aperturas posteriores.

La migración `20260908_columnas_movimiento_ocultas.sql` aplica una vez el nuevo valor predeterminado a las configuraciones que ya estaban guardadas. No se aplica repetidamente, por lo que no revierte las decisiones posteriores del usuario.

## Campos y tamaño

Cada consumidor declara todos los campos funcionales, aunque algunos comiencen ocultos. Las relaciones muestran descripciones cuando están disponibles. La cabecera permite arrastrar el borde de una columna para ajustar su ancho, con mínimo de 80 píxeles. Los anchos se mantienen en el estado de la instancia del componente; la preferencia persistida descrita arriba corresponde al orden y visibilidad, no garantiza persistencia de anchos.
