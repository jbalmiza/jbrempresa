# Arquitectura frontend y piezas reutilizables

Revisión: 2026-09-15.

Angular usa componentes standalone, páginas en pages, piezas comunes en components, contratos en interfaces y servicios HTTP en services. API_URL centraliza el destino del backend. El interceptor JWT y la guarda de rutas participan en la sesión y el perfil; los permisos definitivos pertenecen al backend.

La sesión usa JWT de 30 minutos. `SesionActividadService` envía señales de actividad tras interacción real y comprueba periódicamente la fecha `exp` para cerrar la interfaz aunque no haya nuevas peticiones. Las consultas automáticas de mensajes no renuevan el token; el backend solo lo renueva en `/usuarios/actividad`.

## Piezas compartidas

| Pieza | Responsabilidad |
|---|---|
| Tabla | Consulta, filtros, selección, paginación, configuración y ancho de columnas. |
| TablaEdicion / TablaColumna | Edición de líneas con plantillas configuradas por el consumidor. |
| DatosIdentificacion | Empresa e identificador del registro; histórico solo donde corresponde. |
| DatosMovimiento | Campos informativos de auditoría según Registro o Gestión. |
| BarraAcciones | Presentación común de acciones configuradas. |
| Adjuntos | Archivos del registro y selección de imagen principal. |
| SelectorBusqueda | Selección de registros con descripción funcional. |
| ArbolRegistros | Jerarquías de áreas y asignaciones. |
| SelectorMapa / MapaRegistros | Ubicación y consulta geográfica. |
| SelectorMalla / MallaRegistros | Posiciones y representación en malla. |
| AgendaRegistros | Horarios, excepciones, reservas y navegación de agenda. |
| ChatInterno | Mensajería en modos CHAT, REGISTRO y GESTION. |
| CatalogoPublico | Vista y carrito reutilizados por catálogo público y de proveedores. |
| ImagenHttp / utilidades Blob | Descarga autenticada y liberación de URLs de imágenes. |
| Servicio de diálogos | Avisos, validaciones y errores en ventanas comunes. |

Las fuentes están en [components](../src/app/components), [services](../src/app/services), [directives](../src/app/directives) y [shared](../src/app/shared). Los inputs y outputs exactos se consultan en cada pieza; la tabla anterior no impone una interfaz universal.

Productos es la referencia para nuevos CRUD. Consultar las [directivas](DIRECTIVAS.md) antes de extender una pantalla; las diferencias de implementación detectadas figuran en [limitaciones](../../backend/docs/LIMITACIONES.md). No se considera conforme todo el código antiguo solo por describir aquí el patrón requerido.

Ver [tablas](TABLAS.md), [rutas](RUTAS.md), [guía de uso](GUIA_USO.md) y [catálogo](CATALOGO.md).
