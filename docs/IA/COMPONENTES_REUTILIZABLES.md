# Componentes y servicios reutilizables

## Tabla

- El componente `tabla` permite a cada usuario configurar las columnas visibles y su orden mediante el botón pequeño de configuración.
- La preferencia se guarda en backend por cliente, usuario y clave estable de tabla; se recupera desde cualquier dispositivo.
- Las columnas nuevas que se incorporen posteriormente aparecen visibles al final sin invalidar la configuración anterior.
- El componente permite restaurar el orden y la visibilidad originales.
- La configuración predeterminada oculta `empId` y los campos terminados en `IdHis`; el usuario puede volver a mostrarlos expresamente.
- La cabecera propia alinea el título a la izquierda y las acciones de configuración, importación y exportación a la derecha.
- `permitirImportar` y `permitirExportar` activan estas opciones; los eventos mantienen en la pantalla consumidora la lógica específica de cada operación.

- **Ubicación:** `frontend/src/app/components/tabla/`.
- **Función:** lista genérica con filtros por columna, paginación y selección.
- **Inputs:** `columnas`, `titulosColumnas`, `datos`.
- **Output:** `filaSeleccionada`.
- **Uso:** clientes, usuarios, perfiles, personas, productos, domicilios, ventas y compras.

## TablaEdicion y TablaColumna

- **Ubicación:** `components/tablaEdicion/` y `directives/tablaColumna/`.
- **Función:** tabla con plantillas proyectadas por columna y acción de insertar línea.
- **Inputs:** `titulosColumnas`, `columnas`, `datos`.
- **Output:** `insertarLinea`.
- **Uso:** detalle local de ventas y compras.

## SelectorBusqueda

- **Ubicación:** `components/selectorBusqueda/`.
- **Función:** autocompletado local sobre registros recibidos.
- **Inputs:** `datos`, `campoDescripcion`, `campoId`, `idSeleccionado`.
- **Output:** `valorSeleccionado`.
- **Limitación:** no consulta servidor y requiere tres caracteres antes de sugerir resultados.

## SelectorMapa y MapaRegistros

- **Ubicación:** `components/selectorMapa/`, `components/mapaRegistros/`.
- **Función:** elección y visualización Leaflet de coordenadas, con Proj4 UTM/WGS84.
- **SelectorMapa Inputs/Outputs:** `x`, `y`, `epsg`; `xChange`, `yChange`, `epsgChange`.
- **MapaRegistros Inputs:** función `obtenerRegistros`, ruta y nombres de campos de ID, título, coordenadas y EPSG.
- **Uso:** Domicilios y gestión de domicilios.

## SelectorMalla y MallaRegistros

- **Ubicación:** `components/selectorMalla/`, `components/mallaRegistros/`.
- **Función:** escoger, visualizar, pintar o borrar posiciones en una malla 50×100.
- **Dependencia:** `MallaService` y endpoint `/mallas`.
- **Uso:** Productos y gestión de productos.
- **Limitación:** usa creación directa de elementos DOM y `ViewEncapsulation.None`.

## ArbolRegistros

- **Ubicación:** `components/arbolRegistros/`.
- **Función:** representa jerarquías genéricas mediante nodos normalizados con identificador, padre, etiqueta, detalle, tipo y registro original.
- **Inputs:** `nodos`, `idSeleccionado`, `mensajeVacio` y `expandidoInicial`.
- **Output:** `nodoSeleccionado`.
- **Comportamiento:** selección accesible, expansión y contracción por rama o de todo el árbol, detección defensiva de ciclos y representación diferenciada de ramas y hojas.
- **Uso inicial:** Áreas Organizativas muestra áreas y subáreas; Personal de Área muestra áreas como ramas y asignaciones de personas como hojas.
- La validación y persistencia pertenecen a las pantallas consumidoras y al backend; el componente no contiene reglas específicas del dominio.

## Navegación

- **Sidebar:** menú por módulo recibido mediante Input y navegación con Router.
- **Supbar:** muestra sesión y cierra sesión con `localStorage.clear()`.
- **Sesión:** su método `salir()` elimina todos los datos de `localStorage` y vuelve al acceso.

## PdfService

- **Ubicación:** `services/pdf.service.ts`.
- **Función:** genera PDF horizontal con tabla, logotipos opcionales y contador de páginas.
- **Uso:** páginas CRUD que disponen de `Tabla` mediante `@ViewChild`.
