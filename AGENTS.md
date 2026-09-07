# Normas de documentación

La documentación forma parte obligatoria de cada cambio y tiene prioridad máxima.

- Todo cambio de frontend debe documentarse dentro de `frontend/docs/` en el mismo cambio.
- Todo cambio de backend, base de datos, API o infraestructura debe documentarse dentro de `backend/docs/` en el mismo cambio.
- Actualizar el registro `CAMBIOS.md` correspondiente con fecha, alcance y verificaciones realizadas.
- Cuando un cambio afecte al contrato entre frontend y backend, documentarlo en ambos proyectos.
- Registrar las decisiones funcionales confirmadas, no solo los detalles de implementación.
- No guardar contraseñas, claves JWT, claves de cifrado ni otros secretos reales en archivos versionados.
- Documentar nombres de variables, requisitos y procedimientos para proporcionar secretos desde el entorno.

# Directiva sobre datos de desarrollo

- Todos los datos existentes en la base de datos se consideran datos de prueba hasta que se declare expresamente lo contrario.
- Cuando datos de prueba entren en conflicto con un desarrollo nuevo, deben actualizarse o eliminarse según necesite el modelo vigente.
- No conservar compatibilidad, ramas de código, tipos, tablas ni registros obsoletos únicamente para preservar datos de prueba preexistentes.

# Separación entre Registro y Gestión

- Las pantallas de Registro contienen exclusivamente el CRUD del maestro: Consultar, Insertar, Ver/Modificar y Eliminar.
- Las bajas lógicas, reactivaciones, históricos y acciones operativas pertenecen a las pantallas de Gestión, nunca a Registro.
- Mantener esta separación en todos los módulos existentes y futuros.

# Presentación de relaciones con Persona

- Toda relación con Persona se persiste mediante `perId`.
- En tablas y formularios relacionados no se muestra el identificador: se presenta `perNomCom`.
- El formulario muestra además domicilio completo, teléfono y correo dentro de un apartado `Datos Persona`.

# Reutilización obligatoria

- Toda funcionalidad, control visual o lógica susceptible de reutilizarse debe implementarse como componente, servicio, directiva o utilidad genérica, según corresponda.
- Las pantallas consumidoras deben configurar y componer esas piezas comunes, evitando implementaciones duplicadas o acopladas innecesariamente a un único módulo.
- Antes de crear una pieza nueva, revisar los componentes y servicios compartidos existentes y ampliar uno de ellos cuando resulte coherente.

# Datos Movimiento en Registro y Gestión

- Registro no tiene histórico y su bloque `Datos Movimiento` contiene solamente Usuario, Fecha y Activo.
- Gestión tiene histórico y su bloque `Datos Movimiento` contiene Tipo, Causa, Usuario, Fecha y Activo.
- Todos los campos de `Datos Movimiento` son informativos y permanecen deshabilitados.

# Avisos al usuario

- Los avisos, validaciones y errores dirigidos al usuario deben mostrarse mediante las ventanas emergentes del sistema común de diálogos.
- No implementar nuevos avisos mediante `alert`, mensajes aislados dentro del formulario ni mecanismos particulares de una pantalla.

# Referencia para nuevos CRUD e imágenes

- El CRUD de Productos es el referente funcional y visual para cualquier CRUD nuevo: estructura Registro/tabla/formulario, barra y orden de acciones, componentes `Datos Identificación` y `Datos Movimiento`, tamaños de campo y estilos comunes.
- Antes de implementar un CRUD nuevo, preguntar qué campos debe contener y confirmar cualquier diferencia respecto al CRUD de Productos.
- Antes de crear o sustituir imágenes, preguntar qué enfoque visual desea el usuario (vectorial, fotográfico u otro), salvo que ya lo haya indicado expresamente en la petición actual.
- Las imágenes deben almacenarse siempre bajo el directorio común de imágenes configurado para la empresa; no se guardan recursos gráficos dispersos en directorios funcionales.
- Toda imagen o documento asociado a un registro se incorpora y gestiona exclusivamente mediante el botón y componente genérico `Adjuntos`; no se crean campos de carga de archivos particulares dentro de formularios.
- Cuando una imagen de Adjuntos se marca como principal, esa selección es la utilizada por las vistas y catálogos que representen el registro.

# Datos Identificación y títulos de ventana

- Todo formulario que incluya `Datos Identificación` debe mostrar siempre `Id Empresa` y el identificador propio del registro.
- En pantallas de Registro no se muestra `Id Histórico`; en pantallas de Gestión se añade obligatoriamente `Id Histórico` cuando el maestro dispone de versionado histórico.
- El componente genérico `datosIdentificacion` garantiza automáticamente la presencia y la etiqueta `Id Empresa`; las pantallas deben configurar el identificador del registro y, cuando corresponda, el histórico.
- Las ventanas muestran únicamente su título y la barra de acciones: no se añaden textos descriptivos, subtítulos ni introducciones debajo del título.

# Referencia para nuevos CRUD

- El CRUD de Productos es el referente funcional y visual para cualquier CRUD nuevo: estructura Registro/tabla/formulario, barra y orden de acciones, componentes `Datos Identificación` y `Datos Movimiento`, tamaños de campo y estilos comunes.
- Antes de implementar un CRUD nuevo, preguntar qué campos debe contener y confirmar cualquier diferencia respecto al CRUD de Productos.
- Antes de crear o sustituir imágenes, preguntar qué enfoque visual desea el usuario (vectorial, fotográfico u otro), salvo que ya lo haya indicado expresamente en la petición actual.
- Las imágenes deben almacenarse siempre bajo el directorio común de imágenes configurado para la empresa; no se guardan recursos gráficos dispersos en directorios funcionales.
