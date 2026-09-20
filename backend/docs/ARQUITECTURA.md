# Arquitectura actual

Revisión: 2026-09-15. Fuentes: [pom.xml](../pom.xml), [controladores](../src/main/java/com/jbrempresa/backend/controller), [servicios](../src/main/java/com/jbrempresa/backend/service), [contexto](../src/main/java/com/jbrempresa/backend/core/context/ContextoOperacion.java).

## Plataforma y capas

Backend Spring Boot 4.0.7/Java 21, MVC, JPA/Hibernate, PostgreSQL; H2 en pruebas. Frontend Angular standalone con servicios HTTP, formularios ngModel y componentes compartidos.

controller adapta HTTP y contexto; service aplica reglas y transacciones; repository consulta persistencia; entity y dto separan almacenamiento y contratos cuando existe DTO. Algunos endpoints todavía reciben/devuelven entidades: no se afirma que toda la API esté desacoplada.

core contiene contexto, configuración efectiva y seguridad transversal; shared contiene utilidades. No existe un CRUD universal ni un motor único de histórico para todos los maestros. La reutilización sigue las directivas de ambos proyectos.

## Autenticación y ámbito

JwtFilter valida el bearer y carga JwtUser mediante el servicio de usuarios. ContextoOperacion determina empresa/usuario. El Administrador puede seleccionar empresa o consultar globalmente en endpoints que lo admiten. El resto permanece en su empresa. Catálogo público resuelve la empresa por token; Proveedores autoriza la suministradora mediante relación vigente.

Estos son accesos distintos: [seguridad y matriz de ámbito](SEGURIDAD.md). No aplicar la regla “toda consulta usa exclusivamente la empresa del JWT” a los casos autorizados de selección global o relación comercial.

## Agregados principales

Empresa posee configuración y datos. Persona identifica terceros de negocio. EmpresaRelacion vincula dos empresas en sentidos PROVEEDOR/CLIENTE. DocumentoVenta y detalle representan PRE/PED/ALB/FAC. Reserva y tareas organizan trabajo; no sustituyen al documento. Mensajería interna y comunicaciones externas tienen modelos diferentes.

El catálogo es común para Clientes y Proveedores. El proveedor recibe un pedido en sus tablas de ventas; no se genera una compra espejo en la compradora. Las imágenes protegidas pasan por HttpClient.

## Persistencia y operación

Muchos maestros versionan movimientos A/M/B, pero no todos. No interpretar Activo como “no dado de baja” universalmente: en entidades históricas también identifica la versión vigente. El comportamiento concreto está en el controlador/servicio.

Los flujos compuestos usan transacciones; no hay bloqueo optimista uniforme. Hibernate update, inicializadores y SQL manual conviven. [Modelo](MODELO_DATOS.md), [API](API.md), [operación](OPERACION.md) y [limitaciones](LIMITACIONES.md) describen sus límites actuales.
