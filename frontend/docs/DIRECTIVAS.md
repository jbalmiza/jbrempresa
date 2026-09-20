# Directivas funcionales y visuales del frontend

Revisión documental: 2026-09-15. Describe el árbol de trabajo actual.

Este documento es el catálogo maestro de directivas permanentes del frontend. Las decisiones propias de un módulo se desarrollan en su documento temático, pero toda regla transversal debe figurar aquí con un identificador estable. `CAMBIOS.md` conserva únicamente el histórico de cuándo se incorporó o modificó una directiva.

## Documentación

1. **FE-DIR-001 — Documentación simultánea.** Todo cambio visual, funcional, de rutas, componentes, interfaces o servicios se documenta en `frontend/docs/` y en `CAMBIOS.md` dentro del mismo cambio.
2. **FE-DIR-002 — Decisiones confirmadas.** Se documentan las decisiones funcionales confirmadas, además de los detalles técnicos de implementación.

## Registro y Gestión

3. **FE-DIR-003 — Separación de responsabilidades.** Registro contiene exclusivamente el CRUD del maestro: Consultar, Insertar, Ver/Modificar y Eliminar. Gestión contiene bajas, reactivaciones, históricos y acciones operativas.
4. **FE-DIR-004 — Acciones exclusivas de Registro.** Modificar, Eliminar y Eliminar cadena no aparecen ni se ejecutan en Gestión. En Ventas, Eliminar cadena pertenece al Registro del documento correspondiente.
5. **FE-DIR-005 — Datos Movimiento.** En Registro muestra solamente Usuario, Fecha y Activo. En Gestión muestra Tipo, Causa, Usuario, Fecha y Activo. Todos son informativos y están deshabilitados.
6. **FE-DIR-006 — Datos Identificación.** Todo formulario que use este bloque muestra Id Empresa y el identificador del registro. Id Histórico no aparece en Registro y es obligatorio en Gestión cuando existe versionado.
7. **FE-DIR-007 — Referencia de CRUD.** Productos es la referencia funcional y visual para los CRUD nuevos. Antes de crear uno se confirman sus campos y cualquier diferencia.

## Tablas

8. **FE-DIR-008 — Integridad de columnas.** Toda tabla de consulta declara todos los campos funcionales disponibles del registro representado. Un campo puede estar oculto inicialmente, pero debe permanecer disponible en la configuración común de columnas.
9. **FE-DIR-009 — Relaciones descriptivas.** Las relaciones muestran su descripción funcional y no únicamente el identificador técnico cuando existe dicha descripción.
10. **FE-DIR-010 — Consulta explícita.** Las tablas no realizan la consulta automáticamente al entrar; se cargan al pulsar Consultar o al aplicar el mecanismo de consulta expresamente definido.
11. **FE-DIR-011 — Una tabla por ventana.** Nunca se presentan dos tablas simultáneamente en una ventana. Cuando existan conjuntos alternativos, un botón permite cambiar la tabla visible.

## Barras, botones y ventanas

12. **FE-DIR-012 — Orden de cierre.** Adjuntos se sitúa a la izquierda de Histórico, Histórico inmediatamente a la izquierda de Volver y Volver es siempre la última acción y el botón situado más a la derecha.
13. **FE-DIR-013 — Texto de retorno.** La acción de retorno utiliza siempre el texto `Volver`.
14. **FE-DIR-014 — Vistas especiales.** Mapa, Malla o Árbol es la primera acción a la izquierda y usa el color morado suave común.
15. **FE-DIR-015 — Títulos.** Las ventanas muestran únicamente el título y la barra de acciones, sin subtítulos ni introducciones bajo el título.
16. **FE-DIR-016 — Diálogos comunes.** Los avisos, validaciones y errores se muestran con el sistema común de diálogos; no se usan `alert` ni avisos particulares incrustados en una pantalla.
17. **FE-DIR-017 — Cabecera de sesión.** Módulos usa morado suave, Mensajes verde o rojo suave según haya mensajes, Usuario azul suave y Salir gris. Sus iconos quedan a la izquierda y, al reducir el ancho, permanece el icono y puede ocultarse el texto.

## Componentes y relaciones

18. **FE-DIR-018 — Reutilización obligatoria.** Toda funcionalidad, control o lógica reutilizable se implementa como componente, servicio, directiva o utilidad común. Antes de crear una pieza se revisan y amplían las existentes cuando sea coherente.
19. **FE-DIR-019 — Relaciones con Persona.** Todo formulario que vincule una Persona usa `datosPersonaRelacion`: selecciona por `perNomCom`, persiste `perId` y muestra domicilio completo, teléfono y correo sin edición.
20. **FE-DIR-020 — Alta y ampliación de personas.** Otros procesos pueden crear una persona con nombre y al menos un dato crítico de contacto. La ampliación de sus datos se realiza exclusivamente en Personas, y un contacto distinto identifica a otro cliente cuando no pueda unificarse con certeza.
21. **FE-DIR-021 — Estado de datos de Persona.** El formulario de Personas muestra un campo no editable `Completos` o `Incompletos`, recalculado según los requisitos del tipo físico o jurídico. El teléfono es obligatorio y, para personas jurídicas, la razón social corta es la obligatoria.

## Imágenes y adjuntos

22. **FE-DIR-022 — Gestión mediante Adjuntos.** Toda imagen o documento asociado a un registro se incorpora exclusivamente mediante el componente genérico Adjuntos. No se crean cargas particulares en formularios.
23. **FE-DIR-023 — Imagen principal.** Las vistas y catálogos utilizan la imagen marcada como principal en Adjuntos y la actualizan cuando cambia el contexto de empresa.
24. **FE-DIR-024 — Almacenamiento común.** Las imágenes se guardan bajo el directorio común configurado para la empresa, nunca dispersas en directorios funcionales.
25. **FE-DIR-025 — Enfoque visual.** Antes de crear o sustituir imágenes se confirma el enfoque visual, salvo que la petición actual ya lo establezca.

## Empresas, perfiles y módulos

26. **FE-DIR-026 — Contexto global.** El Administrador puede consultar todas las empresas y seleccionar una en la cabecera; el cambio actualiza consultas e imagen. El Jefe queda limitado siempre a su empresa.
27. **FE-DIR-027 — Administración restringida.** Registro y Gestión de Empresas, Perfiles y Módulos son exclusivos del Administrador. Gestión de Módulos define por empresa cuáles están habilitados y su orden obligatorio.
28. **FE-DIR-028 — Acceso por perfil.** Administrador accede a todos los módulos y empresas; Jefe a los módulos y datos de su empresa; Cliente al catálogo; Empleado a su agenda.
29. **FE-DIR-029 — Orden del centro de módulos.** El centro usa el título `Módulos de Gestión` y separa Clientes, Empleados y Proveedores dentro de `Módulos de Personas`.

## Agenda, catálogo y ventas

30. **FE-DIR-030 — Agenda común.** Jefes y empleados utilizan el mismo componente Agenda, sin modos diferentes. Se abre al pulsar Mi agenda, permite navegar por fechas y muestra el día vacío cuando no hay tareas.
31. **FE-DIR-031 — Catálogo de cliente.** El Cliente accede únicamente al catálogo de su empresa, con presentación normal o móvil según el dispositivo y mediante una URL empresarial con `catalogo`.
32. **FE-DIR-032 — Documentos configurables.** Presupuestos y albaranes solo se muestran cuando sus parámetros de empresa están activos. Si albaranes está desactivado, Pedidos no muestra Convertir a albarán.
33. **FE-DIR-033 — Facturas.** Cada pedido genera automáticamente una factura normal o simplificada según la empresa. El mismo Registro de Facturas gestiona ambos tipos y adapta sus campos; una simplificada puede convertirse en normal tras completar la Persona.
34. **FE-DIR-034 — Contexto global de empleado.** El Administrador selecciona en la barra superior un empleado de la empresa activa. La selección es reutilizable por cualquier proceso y se limpia al cambiar de empresa. El acceso autorizado a una agenda comprende todas sus acciones, sin un modo adicional de solo lectura.
35. **FE-DIR-035 — Operación desde Gestión de Agendas.** Gestión de Agendas permite actuar sobre cualquier tarea de cualquiera de las agendas mostradas, respetando la autorización de acceso a la empresa. Las tareas `En curso` o `Finalizadas` pueden volver a `Pendiente` con confirmación y el pago puede desmarcarse con confirmación.
36. **FE-DIR-036 — Agenda individual y múltiple.** El componente funcional Agenda admite una o varias agendas; cambia el número de columnas, pero conserva navegación, selección, detalle inferior y acciones comunes.
37. **FE-DIR-037 — Sin desplazamiento vertical interno.** La agenda nunca muestra una barra vertical propia: crece para presentar todas sus franjas. Cuando varias columnas no caben, únicamente se permite desplazamiento horizontal.
38. **FE-DIR-038 — Mensajería interna común.** La barra superior y las pantallas de mensajes reutilizan el mismo componente de chat. Su ubicación y permisos administrativos se rigen por FE-DIR-047. Registro crea, consulta, modifica y elimina; Gestión responde y opera el ciclo de baja. Todos los usuarios pueden gestionar lectura y clasificar mensajes como Normal, Aviso o Alerta.
39. **FE-DIR-039 — Usuarios y empleados.** Salvo el Administrador global, todo usuario mostrado por la aplicación corresponde a un Jefe o Empleado, y todo Empleado activo dispone de usuario. La mensajería presenta a los destinatarios mediante el nombre de su recurso empleado.
40. **FE-DIR-040 — Identificación de mensajes.** Toda representación de un mensaje, tanto en la bandeja como en el hilo, muestra siempre Fecha, Hora, Emisor y Destinatario o Destinatarios.
41. **FE-DIR-041 — Clasificación visual de mensajes.** La clasificación Normal, Aviso o Alerta se establece exclusivamente durante el envío de cada mensaje y no puede cambiarse posteriormente. Los mensajes conservan texto normal y expresan esa clasificación mediante el contorno completo del cajón: neutro para Normal, amarillo para Aviso y rojo para Alerta. La presentación debe ser compacta.
42. **FE-DIR-042 — Apertura de conversaciones.** Al abrir una conversación, el hilo se desplaza automáticamente hasta su final para presentar el mensaje más reciente y el área de respuesta.
43. **FE-DIR-043 — Baja de conversaciones.** La baja afecta a la conversación completa y a todos sus participantes. El Administrador consulta las bajas con usuario, fecha y hora, puede reactivarlas y eliminarlas definitivamente de forma individual o masiva hasta una fecha.
44. **FE-DIR-044 — Alta de conversaciones.** En el alta de una conversación, el Asunto es obligatorio y el contenido del campo Mensaje es opcional. El destinatario continúa siendo necesario para efectuar el envío.
45. **FE-DIR-045 — Avisos de conversaciones activas.** Una conversación con solo Asunto sigue siendo un mensaje válido. Las conversaciones dadas de baja no aparecen en la bandeja ordinaria ni generan contador, color de aviso o notificación para ningún participante.
46. **FE-DIR-046 — Proveedor de producto.** El proveedor de un producto es opcional y se selecciona entre `Sin proveedor` y las empresas con relación activa de tipo Proveedor respecto de la empresa actual.

47. **FE-DIR-047 — Administración de mensajes.** Consulta de bajas, eliminación individual o hasta una fecha y reactivación son exclusivas del Administrador en Administración. Registro permite eliminación definitiva; Gestión permite baja y reactivación. La cabecera ofrece únicamente Nuevo mensaje y Responder, además de consulta y navegación del hilo. Actualiza el ámbito de FE-DIR-038 y FE-DIR-043.

48. **FE-DIR-048 — Catálogos de proveedores relacionados.** Proveedores muestra el catálogo de las empresas con relación PROVEEDOR activa y vigente respecto de la empresa del usuario. Reutiliza el catálogo y el flujo de pedidos de Clientes. Sin relación no permite consultar ni pedir. Con varios proveedores se selecciona uno; con uno se abre directamente. El Administrador debe seleccionar la empresa compradora.

## Directivas de trabajo

49. **FE-DIR-049 — Directiva de trabajo n.º 1: eficiencia con calidad y autonomía.** En cada desarrollo se trabaja con plena autonomía y la profundidad necesaria para entregar un resultado correcto, completo y de calidad. La optimización afecta al proceso, nunca reduce el análisis, la implementación, las comprobaciones ni el resultado final.

   - Antes de modificar código, se comprende suficientemente la tarea y se investigan las partes necesarias del proyecto, sus dependencias y posibles efectos secundarios. Se aprovechan los patrones y soluciones existentes, se mantiene la coherencia arquitectónica y funcional, se realizan todos los cambios necesarios en las capas afectadas y se corrige la causa raíz de los problemas.
   - Se reutiliza el conocimiento adquirido y se evita únicamente el trabajo sin valor: búsquedas o análisis ya resueltos sin cambios relevantes, comprobaciones equivalentes repetidas, exploración claramente ajena a la tarea, razonamientos duplicados y mejoras no solicitadas que no aporten al resultado. Nunca se limita una investigación necesaria para ahorrar recursos.
   - Las decisiones técnicas deducibles del proyecto se toman autónomamente, incluidos los cambios adicionales necesarios para completar la petición o evitar una regresión directamente relacionada. Solo se pregunta por una decisión funcional importante que no pueda deducirse de la petición ni del proyecto.
   - La profundidad de las verificaciones se adapta al alcance y riesgo. Se comprueban el resultado y sus efectos relacionados, y se amplían las pruebas cuando sean necesarias para garantizar la calidad. Se evitan únicamente verificaciones repetidas que no aporten información nueva.
   - En cambios acotados se aplica la solución más simple que resuelve la petición y una verificación proporcional. No se añaden refactorizaciones, exploraciones, pruebas amplias ni explicaciones repetidas sin una necesidad concreta. En ajustes visuales se comprueba el resultado en pantalla cuando hay un medio disponible; si no lo hay, se indica brevemente ese límite sin darlo por verificado.
   - Se continúa hasta terminar y verificar razonablemente la tarea; después se detiene el trabajo adicional que no aporte valor. Al finalizar se resumen brevemente lo realizado, los elementos principales modificados, las comprobaciones y cualquier cuestión relevante pendiente.
   - Si se detecta que este modo de trabajo ha provocado un problema, se informa al usuario para revisar la directiva.

50. **FE-DIR-050 — Funcionalidad general para todas las empresas.** Todo desarrollo funcional debe diseñarse como una capacidad general de la aplicación, disponible para todas las empresas aunque cada una decida utilizarla o no. No se desarrollan soluciones exclusivas para una empresa o cliente. Por ejemplo, si se incorporan combos de productos con descuento, cualquier empresa podrá utilizarlos sin que sea obligatorio hacerlo.

51. **FE-DIR-051 — Información uniforme en formularios.** Toda explicación informativa de un apartado se coloca inmediatamente después de su título y antes de cualquier campo. Se usa el patrón común `nota-informativa nota-informativa-compacta`, con `role="note"`, icono `i` y el contenido dentro de su contenedor; el estilo azul, tipografía y espaciado se toman de `estiloGeneral.css`. Como hijo directo de `.form-grupo`, la nota ocupa todo el ancho disponible y deja separación respecto de títulos y campos (margen superior de 4 px e inferior de 16 px); en pantallas estrechas el texto puede ajustarse en varias líneas. No se colocan notas dentro de la fila de campos ni se usan párrafos sueltos o estilos particulares para esta información. Este patrón rige para los formularios actuales y para toda nueva información que se incorpore.

52. **FE-DIR-052 — Nombres de apartados de formulario.** Todo nuevo apartado de un formulario comienza por la palabra `Datos`, seguida de su contenido funcional; por ejemplo, `Datos Disponibilidad` y `Datos Alérgenos`.

53. **FE-DIR-053 — Igualdad entre formularios de Registro y Gestión.** Los formularios de Registro y Gestión de un mismo maestro mantienen los mismos apartados, campos, etiquetas, orden, información y presentación. Solo difieren en los elementos propios de su finalidad: Gestión añade el identificador histórico y los datos históricos de movimiento, y conserva los campos funcionales como informativos y deshabilitados.

54. **FE-DIR-054 — Revisión obligatoria de directivas.** Cada desarrollo incluye antes de su finalización una revisión expresa de cumplimiento de todas las directivas aplicables. La revisión se adapta al alcance del cambio y se registra junto con sus verificaciones, sin repetir comprobaciones que no aporten información nueva.
