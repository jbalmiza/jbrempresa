# Auditoría de documentación de la aplicación

Fecha: 2026-09-15. Estado: consolidación documental completada; diferencias de implementación registradas en LIMITACIONES.

## Cierre de la revisión documental

El diagnóstico que sigue conserva la situación anterior a la consolidación y sus cifras de origen. Sus referencias de línea ya no describen las posiciones de los archivos actualizados. La entrada vigente es [documentación](../../docs/README.md).

Se han actualizado arquitectura, operación, configuración, seguridad, API, modelo, módulos, catálogo, relaciones, mensajería, recursos, integraciones, pruebas e índices. Los documentos IA remiten a referencias consolidadas y los objetivos se distinguen de lo implementado. Hay guía de uso e inventarios regenerables de endpoints, entidades y rutas.

AUD-01 a AUD-11 y AUD-13 quedan atendidos documentalmente. AUD-03 conserva el defecto funcional de Compras; AUD-04 no acredita autorización exhaustiva; AUD-11 conserva el problema de inicialización; AUD-12 se documenta, pero su corrección requiere desarrollo. El detalle vigente está en [LIMITACIONES](LIMITACIONES.md). Cerrar la documentación no cierra estos defectos de código.

Verificación de cierre: 62 Markdown, 266 endpoints y 54 entidades; enlaces locales explícitos, índices, secuencias FE/BE e inventarios comprobados mediante `node docs/verificar-documentacion.mjs`. No se han repetido las pruebas funcionales ni validado servicios externos por este cambio.

## Diagnóstico original (antes de consolidar)

## Dictamen

La documentación es una base útil para continuar el desarrollo, pero **no es todavía suficiente como referencia única, coherente y reproducible de la aplicación actual**.

Los últimos documentos de relaciones, mensajería y proveedores explican razonablemente sus cambios y coinciden en buena parte con el código. Sin embargo, añadir apartados al final no ha actualizado siempre los índices, los resúmenes de arquitectura, el inventario de API, el modelo de datos ni las decisiones anteriores. Un lector obtiene respuestas distintas según el archivo que consulte.

Para el propietario sirve como memoria de decisiones. Para incorporar otro desarrollador, mantener la aplicación sin contexto previo o preparar su operación, necesita consolidación. No se necesita multiplicar documentos: se necesita que los existentes distingan claramente norma vigente, implementación actual, historial y trabajo pendiente.

## Alcance y método

- Instantánea del árbol de trabajo actual, sobre HEAD `ea912a4`, con cambios locales. No se atribuye este contenido a una versión publicada o a ese commit sin sus cambios locales.
- Inventario estructural de **48 Markdown y 4.895 líneas** antes de añadir este informe: 11 en `frontend/docs/`, 19 en `backend/docs/` y 18 en `docs/`, incluidos 14 en `docs/IA/`.
- Revisión adicional de `AGENTS.md`, README de ambos proyectos, configuración de ejecución e inventario de pruebas.
- Lectura temática y contraste dirigido con rutas Angular, plantillas, servicios, controladores, entidades y configuración. El inventario es global; la comprobación funcional es por muestreo, no una certificación línea por línea de todos los módulos.
- Comprobación automática de enlaces Markdown locales explícitos, presencia en índices y numeración de directivas. Los caminos mencionados solo entre comillas o en bloques de código no cuentan como enlaces comprobados. No se verificaron destinos web ni todos los anclajes internos.
- No se ejecutaron nuevamente todas las pruebas, no se recorrieron todas las pantallas ni se auditaron servicios externos, datos productivos o la seguridad completa de la aplicación. La auditoría no modifica código ni registros de negocio.

## Aspectos que están bien

1. Existe separación documental entre frontend y backend, con registros de cambios y documentos temáticos.
2. Los catálogos tienen **48 directivas FE y 40 BE**, sin saltos ni identificadores duplicados detectados.
3. No se encontraron destinos inexistentes entre los enlaces Markdown locales explícitos examinados.
4. Mensajería documenta su separación entre cabecera, Registro y Gestión, y las restricciones principales se reconocen en `ChatInterno` y `MensajeriaInternaService`.
5. Relaciones documenta la pareja proveedor/cliente, la eliminación de ambos sentidos y el acceso al catálogo de proveedores. El servicio de proveedores valida autenticación, permiso, empresa, estado y fechas de la relación.
6. La documentación de pagos distingue preparación de Redsys/Bizum y pago real pendiente, en lugar de presentar la integración como terminada.
7. `SECRETOS.md` y `CREDENCIALES_USUARIOS.md` están excluidos por Git en el árbol actual. Esto no constituye una revisión del historial completo de secretos.

## Hallazgos

### AUD-01 · Alta · Varias fuentes compiten por definir las reglas vigentes

**Evidencia:** [AGENTS.md](../../AGENTS.md) ordena centralizar las directivas en los catálogos FE/BE. [docs/IA/INSTRUCCIONES_IA.md](../../docs/IA/INSTRUCCIONES_IA.md), líneas 1 y 55, mantiene instrucciones permanentes y otra regla de actualización en `docs/IA/`. [docs/IA/ROADMAP.md](../../docs/IA/ROADMAP.md), línea 22, presenta la autorización por perfil como pausada. Los catálogos actuales ya contienen permisos por perfil y existen `authGuard`, `AccesoPerfilService` y comprobaciones por servicio.

**Impacto:** una persona o herramienta puede aplicar reglas de otra etapa, ignorar una directiva reciente o pedir autorizaciones que no corresponden al proceso vigente.

**Corrección propuesta:** fijar una jerarquía explícita. Mantener `AGENTS.md` como instrucciones de trabajo y los catálogos como normas funcionales; clasificar cada documento IA como vigente, histórico o sustituido, enlazando su reemplazo. No basta con conservar todos como “actuales”.

### AUD-02 · Alta · El procedimiento de arranque no es autocontenido

**Evidencia:** [OPERACION.md](OPERACION.md), desde la línea 14, utiliza `jdbc:postgresql://localhost:5432/jbrempresa` y define `CORE_DATA_ENCRYPTION_KEY`, pero omite `JWT_SECRET` y `BANK_DATA_ENCRYPTION_KEY`. [CONFIGURACION.md](CONFIGURACION.md) documenta `saasdb` y las variables obligatorias. [application.properties](../src/main/resources/application.properties) requiere `BANK_DATA_ENCRYPTION_KEY`; [JwtService.java](../src/main/java/com/jbrempresa/backend/security/JwtService.java) requiere `JWT_SECRET`.

**Impacto:** copiar el primer procedimiento en un entorno limpio puede fallar, aunque funcione en una terminal que ya hereda secretos. Los dos nombres de base tampoco distinguen claramente ejemplo genérico y entorno local confirmado.

**Corrección propuesta:** un único procedimiento completo, con valores de ejemplo y nombres de variables, enlazado desde los demás. Incluir backend 8080, frontend 4200 y comprobaciones independientes de ambos; una respuesta del backend no prueba que la interfaz esté arrancada.

### AUD-03 · Alta · El inventario REST no refleja todo el contrato actual

**Evidencia:** [API.md](API.md) no menciona las bases de siete controladores presentes: `/empleados/mi-agenda`, `/avisos-alertas`, `/caja`, `/empresas-relaciones`, `/mensajeria-interna`, `/modulos-aplicacion` y `/tipos-articulo`. Tampoco recoge las nuevas rutas `/catalogo/proveedores` dentro de la sección de catálogo.

La sección Compras atribuye baja, histórico y deshacer al recurso. [CompraController.java](../src/main/java/com/jbrempresa/backend/controller/CompraController.java) no expone esas operaciones; usa rutas con `/{empId}`. [compra.service.ts](../../frontend/src/app/services/compra.service.ts) realiza, entre otras, alta y consulta sobre `/compras` sin ese segmento. El aviso antiguo de desalineación en `docs/IA/ROADMAP.md` sigue teniendo fundamento.

**Impacto:** un consumidor no puede implementar el contrato solo con la documentación. En Compras existe además una diferencia de implementación, no únicamente un texto antiguo.

**Corrección propuesta:** actualizar el inventario desde controladores y servicios. Para cada contrato documentar método, ruta, entrada, salida, empresa efectiva, permiso y errores. Resolver Compras en un cambio funcional separado; no ocultar su estado real presentándolo como completo.

### AUD-04 · Alta · El aislamiento por empresa está descrito de forma demasiado absoluta

**Evidencia:** [API.md](API.md), línea 8, y [ARQUITECTURA.md](ARQUITECTURA.md), sección Multiempresa, presentan la empresa autenticada como único ámbito. [ContextoOperacion.java](../src/main/java/com/jbrempresa/backend/core/context/ContextoOperacion.java) admite selección del Administrador y consultas globales. [CatalogoProveedorService.java](../src/main/java/com/jbrempresa/backend/service/CatalogoProveedorService.java) autoriza acceder a una empresa distinta mediante una relación de proveedor.

**Impacto:** resulta difícil distinguir una consulta cruzada autorizada de una filtración, y se pueden escribir pruebas con expectativas erróneas.

**Corrección propuesta:** documentar una matriz de ámbito: usuario ordinario, Administrador con empresa, Administrador con todas las empresas, catálogo público y catálogo de proveedor relacionado. Explicar la cabecera `X-Empresa-Seleccionada`, quién puede usarla y dónde se ignora. Distinguir autenticación, permiso de módulo y autorización sobre registros.

### AUD-05 · Alta · Hay documentos que contradicen Registro/Gestión

**Evidencia:** [frontend/docs/RECURSOS.md](../../frontend/docs/RECURSOS.md), línea 9, incluye Modificar en Gestión, aunque después declara esa acción exclusiva de Registro. [frontend/docs/AVISOS_ALERTAS.md](../../frontend/docs/AVISOS_ALERTAS.md), línea 6, permite modificar el periodo desde Gestión; [AVISOS_ALERTAS.md](AVISOS_ALERTAS.md), línea 5, dice que Gestión nunca modifica el maestro.

Las plantillas actuales [recursos.html](../../frontend/src/app/pages/mRecursos/recursos/recursos.html) y [avisosAlertas.html](../../frontend/src/app/pages/mComunicaciones/avisosAlertas/avisosAlertas.html) restringen Modificar a `!gestion`, de acuerdo con FE-DIR-004 y BE-DIR-015.

**Impacto:** la documentación puede inducir a reintroducir una acción expresamente prohibida.

**Corrección propuesta:** sustituir la descripción antigua en su lugar; no mantenerla y añadir una frase contraria más abajo. En estos ejemplos debe corregirse la documentación para reflejar la regla y el código actuales.

### AUD-06 · Media · El modelo de datos antiguo sigue presentado como actual

**Evidencia:** [docs/IA/BASE_DATOS.md](../../docs/IA/BASE_DATOS.md), desde la línea 25, incluye `Cliente/clientes` y `Venta/ventas`; [docs/IA/MODULOS_ACTUALES.md](../../docs/IA/MODULOS_ACTUALES.md), línea 6, describe aprovisionamiento de clientes fuera de HTTP. El código actual dispone de [EmpresaController.java](../src/main/java/com/jbrempresa/backend/controller/EmpresaController.java), con altas, modificaciones y bajas, y de `DocumentoVenta`.

El inventario de [MODELO_DATOS.md](MODELO_DATOS.md) tampoco incorpora los nuevos maestros de relaciones y módulos ni el modelo de mensajería interna, aunque estén documentados por separado.

**Impacto:** se confunden empresa propietaria, persona cliente y empresa proveedora; se puede diseñar contra tablas o conceptos anteriores.

**Corrección propuesta:** consolidar el modelo vigente y enlazar los temas específicos. Añadir un esquema sencillo Empresa → Relación → Empresa y diferenciarlo de Persona y de los documentos de venta/compra.

### AUD-07 · Media · El futuro y lo implementado no están reconciliados

**Evidencia:** ambos `PLANIFICACION_FUTURA.md` declaran que no describen funciones disponibles e incluyen empleados, capacidades y tareas como futuros, con una ubicación prevista en Ventas. [MODULOS.md](MODULOS.md), línea 41, indica que todavía no se generan tareas automáticamente. [CatalogoService.java](../src/main/java/com/jbrempresa/backend/service/CatalogoService.java), línea 109 en esta instantánea, llama a asignación de tareas y generación de factura. Las pantallas de Recursos y agenda ya existen.

[docs/IA/TAREAS_PENDIENTES.md](../../docs/IA/TAREAS_PENDIENTES.md) mantiene pendientes como redimensionar columnas, mientras [tabla.ts](../../frontend/src/app/components/tabla/tabla.ts) ya implementa redimensionamiento.

**Impacto:** se puede duplicar trabajo ya realizado o interpretar una propuesta antigua como decisión todavía vigente.

**Corrección propuesta:** revisar cada elemento con estados Implementado, Parcial, Pendiente o Sustituido, y evidencia. Conservar realmente pendientes las acciones de confirmación pública, pagos u optimizaciones que no estén implementadas.

### AUD-08 · Media · Las instrucciones sobre imágenes no coinciden

**Evidencia:** [FICHEROS_IMAGENES.md](FICHEROS_IMAGENES.md), línea 5, indica continuar automáticamente el enfoque visual existente salvo petición contraria. FE-DIR-025 y `AGENTS.md` exigen preguntar el enfoque antes de crear o sustituir imágenes cuando no se haya indicado.

También hay descripciones repartidas de ruta por módulo, directorio común de empresa, Adjuntos e imagen por tipo. No conviene presentar esas capas como reglas alternativas.

**Impacto:** se pueden crear imágenes sin la consulta requerida o ubicarlas siguiendo una interpretación distinta del almacenamiento común.

**Corrección propuesta:** enlazar la directiva vigente y explicar en un único esquema el directorio común, la configuración por módulo, las subcarpetas y el origen TIPO/REGISTRO. Distinguir el límite general de adjuntos de 10 MB del límite de imagen de 5 MB: esos dos límites, por sí solos, no son una contradicción.

### AUD-09 · Media · El estado de pruebas documentado es falso por antigüedad

**Evidencia:** [README.md](README.md), línea 28, y [OPERACION.md](OPERACION.md), línea 32, dicen que solo existe una prueba de carga de contexto. El inventario actual tiene **7 archivos Java de pruebas** y **5 archivos `.spec.ts`**. Archivos de pruebas no equivalen a número de casos, cobertura o ejecución correcta.

Los cambios recientes registran pruebas concretas, pero no hay una matriz consolidada que permita saber qué comportamientos críticos están cubiertos.

**Impacto:** se infravalora lo comprobado y se desconoce lo que continúa sin verificar. Compilar, obtener HTTP 200 y pasar una prueba de permisos son evidencias distintas.

**Corrección propuesta:** actualizar el estado general y registrar por funcionalidad el comando, casos verificados, resultado y límites. No presentar pruebas unitarias como validación completa del recorrido de usuario.

### AUD-10 · Media · Los índices no llevan a todos los documentos actuales

**Evidencia:** antes de esta auditoría faltaban cuatro temas en [el índice frontend](../../frontend/docs/README.md): Avisos, Mensajería, Módulos y Relaciones. Faltaban cinco en [el índice backend](README.md): los cuatro anteriores y Gestión de Usuarios.

**Impacto:** la documentación existe, pero un lector que empieza por el README no la encuentra. La ausencia de enlaces rotos no demuestra que el índice sea completo.

**Corrección propuesta:** mantener un índice completo por proyecto y una entrada común que explique cómo se relacionan `docs/`, frontend y backend. La incorporación de este informe no subsana las omisiones anteriores.

### AUD-11 · Media · Los añadidos recientes conservan resúmenes viejos y repiten contenido

**Evidencia:** [frontend/docs/RELACIONES_EMPRESA.md](../../frontend/docs/RELACIONES_EMPRESA.md) omite Eliminar en su resumen inicial y lo incorpora después. El apartado Catálogos en Proveedores está reproducido en los dos documentos CATALOGO y en los dos RELACIONES_EMPRESA. El recorrido y las comprobaciones se mezclan con identificadores concretos de los datos de prueba.

[RELACIONES_EMPRESA.md](RELACIONES_EMPRESA.md), línea 7, afirma que los datos de desarrollo crean las relaciones al existir las empresas. Rectificación de esta auditoría: sí existe `RelacionesEmpresaIniciales.java`. Busca «Proveedor Hostelería Central», mientras los datos recientes usan «Proveedor Hostelero Central»; normaliza acentos, pero no esa diferencia de palabras. Puede crear parejas si encuentra las empresas esperadas. Por ello no se garantiza reproducir las relaciones de prueba en una base nueva ni conservar su eliminación tras un reinicio si se cumplen las condiciones de recreación. Véase LIM-03.

**Impacto:** cada cambio requiere sincronizar varias copias; un apartado posterior puede corregir una afirmación anterior sin eliminar la ambigüedad.

**Corrección propuesta:** reescribir el estado actual de cada tema, dejar el historial en CAMBIOS y separar los datos de ejemplo de las garantías de aprovisionamiento. Frontend explica interacción y backend reglas/contrato; se enlazan, sin copiar ambos textos íntegros.

### AUD-12 · Alta · Hay una divergencia real en Datos Movimiento de Relaciones

**Evidencia:** FE-DIR-005 exige Tipo, Causa, Usuario, Fecha y Activo en Gestión. [relacionesEmpresa.html](../../frontend/src/app/pages/mAdministracion/relacionesEmpresa/relacionesEmpresa.html), línea 12, fuerza `mostrarTipo=false` y `mostrarCausa=false` en ambos modos. [EmpresaRelacion.java](../src/main/java/com/jbrempresa/backend/entity/EmpresaRelacion.java) conserva estado y último movimiento, sin un histórico de versiones como el de otros maestros.

**Impacto:** la nueva tabla sí usa el componente común y declara sus 13 campos, pero eso no garantiza que todo el mantenimiento cumpla las normas transversales. No procede afirmar que Registro/Gestión están completamente homologados.

**Corrección propuesta:** tratarlo como trabajo funcional pendiente y definir cómo cumplir movimiento e histórico según las reglas vigentes. No rebajar silenciosamente la directiva para acomodar el código. La auditoría no implementa ese cambio.

### AUD-13 · Media · Falta una guía integrada para operar la aplicación

**Evidencia:** hay documentos de temas y decisiones, pero no un recorrido único que reúna perfil, selección de empresa, configuración del catálogo, relación proveedor, pedido, resultado y consulta posterior. El README de frontend conserva principalmente instrucciones genéricas de Angular. [frontend/docs/CATALOGO.md](../../frontend/docs/CATALOGO.md), línea 31, sitúa Catálogo en Utilidades, mientras [menu-modulos.config.ts](../../frontend/src/app/config/menu-modulos.config.ts) lo ubica en Gestión para Productos y Servicios.

**Impacto:** el propietario puede seguir necesitando la conversación para ejecutar una operación que la documentación supuestamente describe. Tampoco se distingue suficientemente “hacer un pedido de venta al proveedor” de un proceso completo de Compras: el flujo nuevo reutiliza ventas del proveedor; no documenta una compra reflejada automáticamente en la empresa compradora.

**Corrección propuesta:** una guía breve de procesos con requisitos, pasos, resultado y errores esperables. Para Proveedores explicar qué empresa es compradora, dónde se registra el pedido y qué operaciones posteriores existen realmente. Una matriz de roles debe indicar qué usuarios pueden entrar al módulo; pertenecer a una empresa relacionada no sustituye el permiso de módulo.

## Valoración específica de los últimos cambios

| Tema | Coincidencia principal con código | Limitación documental o pendiente |
|---|---|---|
| Tabla de Relaciones | Componente `tabla`, 13 campos, filtros, configuración, selección del registro original. | No basta para declarar homologado el formulario de Gestión; véase AUD-12. |
| Eliminar relación | Confirmación en Registro y eliminación transaccional de la pareja. | Resumen introductorio sin consolidar; no confundir eliminación con baja. |
| Proveedores | Selector por relaciones vigentes, catálogo común, imágenes autenticadas y comprobación al pedir. | Falta integrar API, índices, modelo y guía de usuario. La restricción está aplicada al módulo protegido; las rutas públicas de catálogo conservan su acceso público. |
| Mensajes | Acciones administrativas separadas de la cabecera y protegidas por perfil. | Inventarios generales sin actualizar; algunos párrafos genéricos requieren matizar el contexto. |

**Respuesta a si una documentación “como esta” es suficiente:** sirve para explicar un cambio concreto, pero no sustituye una documentación consolidada del estado actual. Es importante no confundir haber añadido texto con haber actualizado toda la información que dependía de ese cambio.

## Plan de corrección recomendado

### Prioridad 1 · Evitar instrucciones equivocadas

1. Unificar jerarquía documental y marcar los documentos IA sustituidos.
2. Corregir procedimiento de arranque y variables obligatorias.
3. Consolidar Registro/Gestión y las reglas visuales de imágenes.
4. Actualizar contrato REST y excepciones de ámbito multiempresa.

### Prioridad 2 · Sincronizar el estado del producto

5. Actualizar modelo de datos, módulos, planificación y pruebas.
6. Resolver por separado los hallazgos funcionales de Compras y Datos Movimiento de Relaciones.
7. Consolidar Relaciones, Proveedores y Mensajería; separar ejemplos de datos y garantías de inicialización.
8. Completar índices y recorrido de usuario por procesos.

### Prioridad 3 · Mantenerlo verificable

9. Añadir a cada documento vigente fecha de revisión y fuentes de implementación relevantes.
10. Incorporar verificaciones automáticas de enlaces, documentos fuera del índice, numeración y presencia de bases REST. En `.github/workflows` no se encontró una automatización configurada en esta instantánea.
11. Vincular cada cambio funcional a directivas, documento temático, contrato afectado y verificación real. No crear otra copia de las reglas.

## Criterio mínimo para considerar suficiente la documentación

Un cambio debería dejar verificables estas preguntas:

- ¿Qué comportamiento está disponible y qué sigue pendiente?
- ¿Quién puede hacerlo, sobre qué empresa y bajo qué condiciones?
- ¿En qué pantalla está y qué campos/acciones muestra?
- ¿Qué datos consulta o modifica y qué ocurre con las relaciones inversas?
- ¿Cuál es el contrato y qué errores puede devolver?
- ¿Cómo se configura, arranca y comprueba?
- ¿Qué se ha probado realmente y qué no?
- ¿Qué documento manda y qué instrucciones quedaron sustituidas?

**Conclusión:** ajustar y consolidar la documentación actual antes de considerarla un manual fiable de la aplicación. Este informe registra el diagnóstico; no da por corregidos los hallazgos ni certifica el funcionamiento completo del sistema.
