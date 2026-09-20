# Directivas funcionales, de datos y seguridad del backend

Revisión documental: 2026-09-15. Describe el árbol de trabajo actual.

Este documento es el catálogo maestro de directivas permanentes del backend. Los documentos temáticos explican su implementación y `CAMBIOS.md` conserva únicamente el histórico de altas y modificaciones.

## Documentación y datos de desarrollo

1. **BE-DIR-001 — Documentación simultánea.** Todo cambio de Java, API, entidades, seguridad, configuración, base de datos, infraestructura o pruebas se documenta en `backend/docs/` y `CAMBIOS.md` dentro del mismo cambio.
2. **BE-DIR-002 — Contratos compartidos.** Los cambios de contrato entre backend y frontend se documentan en ambos proyectos.
3. **BE-DIR-003 — Secretos.** No se versionan contraseñas ni claves reales. Se documentan las variables, requisitos y procedimientos para suministrarlas desde el entorno.
4. **BE-DIR-004 — Datos de desarrollo.** Los datos existentes son de prueba mientras no se declare lo contrario. Pueden actualizarse o eliminarse y no justifican conservar modelos, ramas, tablas o tipos obsoletos.

## Empresa, acceso y perfiles

5. **BE-DIR-005 — Aislamiento empresarial.** El Jefe solo puede consultar y operar datos de su empresa, con independencia de los filtros recibidos.
6. **BE-DIR-006 — Administrador global.** El Administrador puede consultar y operar todas las empresas y usar un contexto de empresa seleccionado. El valor visual `T` representa su alcance global sin alterar los identificadores persistidos.
7. **BE-DIR-007 — Datos reservados.** Solo el Administrador puede consultar el usuario Administrador global y acceder al Registro y Gestión de Empresas, Perfiles y Módulos.
8. **BE-DIR-008 — Configuración de módulos.** Solo el Administrador decide, por empresa, los módulos habilitados y su orden. El orden es obligatorio para todos los usuarios de esa empresa y no se personaliza por usuario.
9. **BE-DIR-009 — Perfiles.** Administrador tiene alcance global; Jefe accede a todos los módulos habilitados y datos de su empresa; Cliente accede al catálogo; Empleado accede a su agenda mediante credenciales.

## Personas

10. **BE-DIR-010 — Relación canónica.** Toda relación con Persona se persiste mediante `perId`; las API proporcionan `perNomCom` y los datos descriptivos necesarios para su presentación.
11. **BE-DIR-011 — Creación desde procesos.** Otros módulos pueden crear una Persona con nombre y teléfono o correo. Un dato de contacto distinto se trata como otra persona cliente cuando no exista una coincidencia inequívoca.
12. **BE-DIR-012 — Ampliación centralizada.** Solo Personas registra o amplía información personal. Pedidos, ventas, facturas y otros módulos seleccionan y reutilizan esa información.
13. **BE-DIR-013 — Integridad de Persona física.** Una persona física está completa con tipo de persona, tipo y número de documento, nombre, primer apellido, domicilio y teléfono.
14. **BE-DIR-014 — Integridad de Persona jurídica.** Una persona jurídica está completa con tipo de persona, tipo y número de documento, razón social corta, domicilio y teléfono.

## Registro, Gestión, tablas y adjuntos

15. **BE-DIR-015 — Registro frente a Gestión.** Registro ofrece el CRUD del maestro. Gestión ofrece bajas, reactivaciones, histórico y operaciones; no expone Modificar, Eliminar ni Eliminar cadena.
16. **BE-DIR-016 — Movimientos.** Registro informa Usuario, Fecha y Activo. Gestión añade Tipo y Causa y, cuando hay versionado, Id Histórico.
17. **BE-DIR-017 — Integridad de consultas.** Las respuestas de consulta deben permitir que las tablas declaren todos los campos funcionales del registro, incluidos los que el frontend oculte inicialmente.
18. **BE-DIR-018 — Adjuntos compartidos.** Registro y Gestión de una misma fila acceden a los mismos adjuntos. La imagen principal es la marcada como tal en el componente común y pertenece al contexto empresarial del registro.

## Ventas y facturación

19. **BE-DIR-019 — Parámetros documentales.** Cada empresa configura independientemente si habilita Presupuestos y Albaranes; ambos permanecen desactivados por defecto en las empresas iniciales.
20. **BE-DIR-020 — Factura automática.** Cada pedido registrado crea automáticamente su factura.
21. **BE-DIR-021 — Tipo de factura.** Cada empresa configura factura normal o simplificada. Pizzería y restaurante usan simplificada por defecto y taller usa normal.
22. **BE-DIR-022 — Modelo único de factura.** Un mismo registro gestiona facturas normales y simplificadas mediante un campo Tipo. Una simplificada puede convertirse en normal completando previamente la Persona vinculada.
23. **BE-DIR-023 — Modificación encadenada.** Una modificación de Pedido debe propagarse a los documentos asociados. Si el parámetro exige clave, se valida el texto configurado sin almacenar una contraseña reversible; rechazar la propagación cancela la modificación completa.

## Pedidos, tareas y agenda

24. **BE-DIR-024 — Unidad de asignación.** Las líneas iguales de un pedido forman una única tarea y no se reparten entre empleados. Se asignan a un recurso con la habilidad requerida y agenda disponible.
25. **BE-DIR-025 — Estados de trabajo.** Productos y servicios avanzan de Pendiente a En curso y Finalizado, conservando la distinción Tipo + Estado.
26. **BE-DIR-026 — Reparto como servicio.** Reparto es una habilidad y un servicio planificable. Recibe pedidos finalizados, puede recoger uno o varios y completa la entrega mediante su tarea.
27. **BE-DIR-027 — Pago independiente.** Pagado es una marca, no un estado del flujo, y puede registrarse antes de elaborar, tras finalizar o después de entregar.
28. **BE-DIR-028 — Agenda universal.** Todo recurso empleado, incluido el Jefe, tiene agenda aunque no reciba pedidos; puede contener otras tareas y consultar fechas pasadas o futuras.
29. **BE-DIR-029 — Acceso integral a agendas.** El acceso autorizado a una agenda comprende su consulta y todas sus acciones. Administrador y Jefe pueden operar sobre agendas de empleados de la empresa activa; el Empleado solo puede consultar y operar sobre su propia agenda. El backend valida siempre empresa, tipo y vigencia del recurso.
30. **BE-DIR-030 — Operación desde Gestión de Agendas.** Toda tarea perteneciente a una agenda mostrada en Gestión de Agendas puede operarse por Administrador o Jefe dentro de la empresa activa. Los estados persistidos son `PENDIENTE`, `EN_CURSO`, `FINALIZADO` y, cuando proceda, `CANCELADA`. `EN_CURSO` y `FINALIZADO` pueden revertirse a `PENDIENTE`; la marca de pago puede activarse o desactivarse.
31. **BE-DIR-031 — Mensajería interna.** La mensajería conserva conversaciones, lectura individual y clasificación Normal, Aviso o Alerta. Empleado escribe al Jefe; Jefe escribe a empleados de su empresa y al Administrador; Administrador escribe a jefes y empleados respetando la empresa seleccionada. Los permisos se validan en backend.
32. **BE-DIR-032 — Correspondencia Usuario–Empleado.** Salvo el Administrador global, no existe ningún Usuario operativo que no sea Jefe o Empleado, ni ningún recurso Empleado activo sin Usuario. Hasta decidir el enlace definitivo, la correspondencia se mantiene mediante la Persona: `usuarios.usu_per_id` coincide con `recursos_operativos.per_id`. Todo Jefe dispone además de recurso Empleado.
33. **BE-DIR-033 — Identificación de mensajes.** El contrato de mensajería proporciona siempre Fecha, Hora, Emisor y lista de Destinatarios para cada mensaje, incluido el último mensaje presentado en la bandeja.
34. **BE-DIR-034 — Clasificación inmutable de mensajes.** La clasificación Normal, Aviso o Alerta se establece al crear cada mensaje o respuesta y no puede modificarse posteriormente.
35. **BE-DIR-035 — Ciclo de baja de conversaciones.** La baja lógica afecta a la conversación completa y a todos sus participantes, registra usuario, fecha y hora y añade un mensaje final de auditoría. Solo el Administrador consulta y reactiva bajas o las elimina físicamente, individualmente o hasta una fecha.
36. **BE-DIR-036 — Contenido inicial opcional.** Al crear una conversación, el Asunto es obligatorio y el contenido inicial es opcional; el backend persiste como cadena vacía la ausencia de contenido. Las respuestas mantienen contenido obligatorio.
37. **BE-DIR-037 — Exclusión de bajas en avisos.** Las conversaciones inactivas no se devuelven en la bandeja ordinaria ni intervienen en el cálculo de no leídos, con independencia del estado histórico de sus participantes.
38. **BE-DIR-038 — Relaciones inversas de empresa.** Las relaciones de empresa admiten Proveedor y Cliente. Cada alta, modificación, baja o reactivación mantiene automáticamente la relación inversa vinculada y no permite autorrelaciones ni duplicados.

39. **BE-DIR-039 — Administración de mensajes.** Las operaciones administrativas de mensajes son exclusivas del Administrador y se presentan en Registro y Gestión del módulo Administración. El servidor exige este perfil para consultar bajas, modificar, dar de baja, reactivar y eliminar individualmente o hasta una fecha. La cabecera permite crear y responder; la eliminación física pertenece a Registro y la baja y reactivación a Gestión.

40. **BE-DIR-040 — Acceso al catálogo de proveedores.** El catálogo, sus imágenes y los pedidos desde Proveedores exigen autenticación, permiso del módulo y relación PROVEEDOR activa y vigente de la empresa compradora con la suministradora. La comprobación se repite al enviar el pedido. El pedido se registra en la empresa proveedora reutilizando el proceso de catálogo de Clientes.

## Directivas de trabajo

41. **BE-DIR-041 — Directiva de trabajo n.º 1: eficiencia con calidad y autonomía.** En cada desarrollo se trabaja con plena autonomía y la profundidad necesaria para entregar un resultado correcto, completo y de calidad. La optimización afecta al proceso, nunca reduce el análisis, la implementación, las comprobaciones ni el resultado final.

   - Antes de modificar código, se comprende suficientemente la tarea y se investigan las partes necesarias del proyecto, sus dependencias y posibles efectos secundarios. Se aprovechan los patrones y soluciones existentes, se mantiene la coherencia arquitectónica y funcional, se realizan todos los cambios necesarios en las capas afectadas y se corrige la causa raíz de los problemas.
   - Se reutiliza el conocimiento adquirido y se evita únicamente el trabajo sin valor: búsquedas o análisis ya resueltos sin cambios relevantes, comprobaciones equivalentes repetidas, exploración claramente ajena a la tarea, razonamientos duplicados y mejoras no solicitadas que no aporten al resultado. Nunca se limita una investigación necesaria para ahorrar recursos.
   - Las decisiones técnicas deducibles del proyecto se toman autónomamente, incluidos los cambios adicionales necesarios para completar la petición o evitar una regresión directamente relacionada. Solo se pregunta por una decisión funcional importante que no pueda deducirse de la petición ni del proyecto.
   - La profundidad de las verificaciones se adapta al alcance y riesgo. Se comprueban el resultado y sus efectos relacionados, y se amplían las pruebas cuando sean necesarias para garantizar la calidad. Se evitan únicamente verificaciones repetidas que no aporten información nueva.
   - En cambios acotados se aplica la solución más simple que resuelve la petición y una verificación proporcional. No se añaden refactorizaciones, exploraciones, pruebas amplias ni explicaciones repetidas sin una necesidad concreta. En ajustes visuales se comprueba el resultado en pantalla cuando hay un medio disponible; si no lo hay, se indica brevemente ese límite sin darlo por verificado.
   - Se continúa hasta terminar y verificar razonablemente la tarea; después se detiene el trabajo adicional que no aporte valor. Al finalizar se resumen brevemente lo realizado, los elementos principales modificados, las comprobaciones y cualquier cuestión relevante pendiente.
   - Si se detecta que este modo de trabajo ha provocado un problema, se informa al usuario para revisar la directiva.

42. **BE-DIR-042 — Funcionalidad general para todas las empresas.** Todo desarrollo funcional debe diseñarse como una capacidad general de la aplicación, disponible para todas las empresas aunque cada una decida utilizarla o no. No se desarrollan soluciones exclusivas para una empresa o cliente. Por ejemplo, si se incorporan combos de productos con descuento, cualquier empresa podrá utilizarlos sin que sea obligatorio hacerlo.

43. **BE-DIR-043 — Revisión obligatoria de directivas.** Cada desarrollo incluye antes de su finalización una revisión expresa de cumplimiento de todas las directivas aplicables. La revisión se adapta al alcance del cambio y se registra junto con sus verificaciones, sin repetir comprobaciones que no aporten información nueva.
