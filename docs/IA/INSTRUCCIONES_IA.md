# Instrucciones permanentes para IA

La visión funcional y el resultado esperado del producto se mantienen en `VISION_PRODUCTO.md`. Antes de proponer o implementar una capacidad relevante, la IA debe contrastarla con ese documento.

## Norma permanente de coherencia del producto

- Extraer de cada conversación las decisiones, objetivos y restricciones que cambien materialmente el producto, y actualizar la documentación afectada dentro del mismo desarrollo.
- No interpretar un comentario exploratorio como decisión confirmada cuando el propietario todavía está valorando alternativas.
- Distinguir entre `[CONFIRMADO]`, `[PROPUESTO]`, `[RECOMENDACIÓN]`, `[PENDIENTE DE DECISIÓN]`, `[PAUSADO]` e `[IMPLEMENTADO]`.
- Si una petición contradice una decisión confirmada, crea dos modelos incompatibles, debilita una garantía transversal o conduce previsiblemente a rehacer trabajo, detener la implementación y avisar antes de modificar.
- La advertencia debe identificar la decisión anterior, la incompatibilidad concreta, las alternativas y la recomendación profesional.
- Si aparece una limitación técnica, legal, económica o de proveedor que impida garantizar el resultado solicitado, declararla con claridad y separar los hechos comprobados de las inferencias.
- La última decisión explícitamente confirmada sustituye a una anterior solo después de explicar su impacto y actualizar la documentación.
- Mantener `VISION_PRODUCTO.md` como referencia principal; los documentos técnicos detallan su implementación y no deben redefinir el objetivo de forma independiente.

## Alcance

Este documento describe cómo trabajar sobre el repositorio tal como está implementado. No sustituye una decisión del propietario ni autoriza rediseños.

## Reglas confirmadas por el código

- Estudiar los archivos directamente relacionados antes de modificar una funcionalidad. Un cambio HTTP implica revisar la página Angular, su servicio, el controlador y, si procede, el servicio/repositorio backend.
- Mantener el monorepo con `backend/` (Spring Boot) y `frontend/` (Angular standalone) como proyectos separados.
- Mantener las convenciones de nombres existentes: clases Java/TypeScript en PascalCase, atributos y métodos en camelCase, y entidades con prefijos funcionales (`proId`, `venFecMov`, `empId`).
- En el backend, las entidades JPA declaran explícitamente tabla y columnas mediante `@Table` y `@Column`; los repositorios extienden `JpaRepository`.
- La mayoría de recursos de negocio se aíslan por cliente: el backend obtiene el cliente desde `JwtUser.getEmpresaId()` y consulta por `empId`. Preservar ese aislamiento cuando se intervenga en recursos que ya lo usan.
- Si un recurso multiempresa recibe el ID de otro recurso relacionado, validar que ambos pertenezcan al mismo `empId` antes de guardar, actualizar o devolver la relación.
- Los campos de auditoría `*_UsuMov` y `*_FecMov` deben asignarse en backend desde `JwtUser` y la hora del servidor; no confiar en los valores enviados por Angular.
- Las excepciones de controladores deben producir respuestas JSON mediante `GlobalExceptionHandler`; no devolver trazas ni detalles internos al frontend.
- La validación de Angular mejora la experiencia, pero no sustituye la validación del backend. Mantener validaciones de servidor antes de persistir los campos obligatorios de los módulos activos.
- Los clientes se aprovisionan fuera de la API HTTP. No reintroducir altas, modificaciones o bajas de clientes sin autorización específica; la consulta debe limitarse al `empId` de `JwtUser`.
- Mantener el JWT: Angular toma `token` de `localStorage`; el interceptor lo envía como `Authorization: Bearer ...`; Spring lo procesa con `JwtFilter`.
- Las rutas Angular que requieren sesión deben usar `authGuard`. La guarda evita navegación sin token, pero no sustituye la validación JWT del backend.
- Mantener la renovación deslizante de sesión: si se modifica JWT, interceptor o CORS, preservar el intercambio de `X-Refresh-Token` y su lectura por Angular.
- Las contraseñas de usuarios deben persistirse con BCrypt y validarse con `PasswordEncoder.matches`; nunca incluir `usuCon` en respuestas, tablas o registros de consola.
- `usuUsu` es único globalmente porque identifica el subject del JWT. Comprobar su unicidad al crear o modificar usuarios y no introducir identificadores ambiguos entre clientes.
- Los secretos no deben incluirse en código, archivos versionados ni documentación pública. La clave de firma JWT se suministra mediante la variable de entorno obligatoria `JWT_SECRET`.
- Las credenciales de PostgreSQL se suministran mediante `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`; no añadir valores reales al repositorio.
- Las páginas Angular son componentes standalone y usan servicios `providedIn: 'root'`, interfaces en `interfaces/` y formularios template-driven con `FormsModule`/`ngModel`.
- Para CRUDs existentes, conservar las rutas, JSON y nombres de campos salvo autorización expresa; los clientes frontend y controladores backend forman un contrato conjunto.
- Mantener los comentarios descriptivos del área modificada. El repositorio usa comentarios frecuentes, incluso antes de bloques simples.
- En altas de recursos existentes es habitual que el frontend envíe ID `0`; el backend lo convierte a `null` antes de persistir para que la base de datos asigne el identificador.
- Los valores de ID y fecha mostrados al insertar son orientativos. Al guardar, el backend y PostgreSQL asignan los valores definitivos; no usar el valor visual para decidir la persistencia.
- El campo de fecha de movimiento se asigna habitualmente en backend con `LocalDateTime.now()`.

## Recomendaciones documentales

- Realizar cambios mínimos, localizados y compatibles con los patrones del módulo afectado.
- No introducir librerías, frameworks, versiones, capas de abstracción o refactorizaciones generales sin autorización explícita.
- No renombrar clases, atributos, tablas, endpoints ni rutas sin revisar todas sus referencias y obtener autorización.
- No asumir que una coincidencia aislada sea una convención. Documentar o preguntar cuando haya patrones contradictorios.
- Antes de modificar recursos multiempresa, comprobar creación, lectura, modificación y eliminación para confirmar que el `empId` no pueda cruzar clientes.
- Antes de eliminar código aparentemente duplicado, buscar sus usos reales, incluidas plantillas HTML, rutas y servicios.
- Al preparar un cambio relevante, indicar los archivos que se prevé modificar y las implicaciones de contrato o seguridad.
- Tras confirmar un cambio, valorar si altera la arquitectura, seguridad, convenciones, patrones, módulos o roadmap documentados; si los altera, actualizar los Markdown pertinentes en `docs/IA/` dentro del mismo cambio.
- No corregir de forma incidental inconsistencias detectadas en esta documentación; tratarlas como cambios independientes y autorizados.

## Norma obligatoria de valoración arquitectónica

- La IA no debe limitarse a aceptar y ejecutar todas las propuestas. Debe valorar activamente si cada cambio es sostenible para GreenSaaS y expresar una recomendación profesional: **aprobar**, **ajustar**, **aplazar** o **rechazar**.
- Las correcciones pequeñas, locales y reversibles pueden implementarse directamente cuando no alteren contratos, datos ni patrones compartidos.
- Antes de implementar un cambio que afecte a base de datos, claves, relaciones, seguridad, aislamiento multiempresa, varios módulos, componentes genéricos o procesos de negocio, realizar primero una revisión arquitectónica.
- La revisión debe indicar como mínimo:
  - problema que se quiere resolver;
  - encaje con la arquitectura actual;
  - alternativas razonables;
  - impacto en frontend, backend, base de datos y datos existentes;
  - riesgos, deuda técnica y dificultad de reversión;
  - recomendación explícita de la IA;
  - decisiones que debe confirmar el propietario.
- No iniciar la implementación mientras falte una decisión capaz de cambiar sustancialmente el modelo o provocar una migración diferente.
- Cuando un patrón vaya a aplicarse a varias tablas o módulos, implementarlo primero en un módulo piloto, probarlo y obtener validación antes de extenderlo.
- No copiar una solución a otros módulos sin revisar relaciones especiales, detalles dependientes, claves foráneas, bajas, históricos y reglas particulares de cada entidad.
- Registrar las decisiones arquitectónicas relevantes en `docs/IA/` dentro del mismo cambio para evitar contradicciones futuras.
- Si una petición duplica datos o lógica, debilita la seguridad o integridad, crea una abstracción prematura o obliga previsiblemente a rehacer varios módulos, advertirlo antes de modificar el código y proponer una alternativa.
- La aprobación final del propietario autoriza la implementación, pero no elimina la obligación de la IA de señalar riesgos y desacuerdos técnicos con claridad.

### Detección proactiva del núcleo común (`core`)

- [CONFIRMADO] El propietario no necesita solicitar ni mencionar expresamente el `core`. En cada desarrollo, la IA debe revisar de oficio si la responsabilidad es transversal, reutilizable o forma parte de la infraestructura común.
- Esta revisión incluye especialmente seguridad, sesión y contexto de operación, aislamiento multiempresa, auditoría, configuración, archivos, comunicaciones compartidas y contratos o componentes utilizados por varios módulos.
- Cuando exista una responsabilidad transversal probada, debe implementarse o trasladarse al `core`, reutilizando antes las piezas comunes existentes y evitando duplicarla dentro de los módulos funcionales.
- El `core` no es un contenedor general: la lógica propia de Personas, Territorio, Productos, Ventas u otro dominio permanece en su módulo. No se crearán abstracciones especulativas con un único consumidor sin una necesidad transversal demostrada.
- Si incorporar una capacidad al `core` altera contratos públicos, seguridad, persistencia o límites entre módulos, la IA debe presentar la decisión arquitectónica antes de implementarla. Las extracciones internas compatibles pueden realizarse dentro del cambio y deben documentarse.
- [CONFIRMADO] La configuración funcional que pueda variar entre clientes debe almacenarse en `parametros`, identificada por cliente, módulo y código; no debe fijarse en código ni en variables globales del servidor. Los secretos de cliente se cifran antes de persistirse y nunca se devuelven en claro. Las claves maestras, credenciales de infraestructura común y opciones estrictamente propias del despliegue permanecen fuera de la base de datos.

## Norma de datos durante el desarrollo

- [CONFIRMADO] Los datos actualmente almacenados son exclusivamente datos de prueba y no condicionan el diseño funcional ni técnico.
- El modelo de datos correcto, normalizado y sostenible tiene prioridad sobre la compatibilidad con registros de prueba existentes.
- Cuando un desarrollo nuevo lo requiera, se permite transformar, regenerar o eliminar datos de prueba mediante migraciones controladas.
- No añadir campos duplicados, compatibilidad heredada ni lógica alternativa únicamente para conservar datos de prueba.
- Las decisiones de compatibilidad y conservación deberán revisarse expresamente antes de utilizar GreenSaaS con datos reales o en producción.

### Secuencia requerida para cambios estructurales

1. Inspeccionar el estado real del código y de la base de datos.
2. Presentar valoración, alternativas y recomendación.
3. Resolver las decisiones funcionales pendientes.
4. Documentar la decisión adoptada.
5. Implementar un alcance piloto cuando proceda.
6. Compilar, migrar y probar proporcionalmente al riesgo.
7. Validar con el propietario.
8. Extender al resto solo después de la validación.

## Puntos que requieren revisión antes de usarlos como patrón

- Compras usa un contrato de rutas distinto en backend que el usado por `CompraService`.

## Elementos pausados

- [PAUSADO] No implementar ni rediseñar autorización por perfil hasta una autorización específica del propietario. Actualmente el perfil se conserva como dato, pero no concede authorities.
- Clientes no se filtra por `empId`; Compras recibe `empId` por URL.

## Trazabilidad

- `backend/src/main/java/com/jbrempresa/backend/security/`
- `frontend/src/app/app.config.ts`
- `frontend/src/app/interceptors/jwt.interceptor.ts`
- `frontend/src/app/pages/`
