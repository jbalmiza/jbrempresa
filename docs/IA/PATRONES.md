# Patrones reutilizables comprobados

## CRUD con aislamiento por cliente

**Objetivo.** Gestionar un recurso del cliente autenticado.

**Flujo.**

```text
Página Angular -> XxxService -> /recurso
  -> XxxController obtiene JwtUser.empresaId
  -> XxxRepository.findByEmpId(...)
```

**Responsabilidades.** La página conserva el estado y valida campos; el servicio Angular realiza HTTP; el controlador sobrescribe/asigna `empId`, fecha e ID; el repositorio filtra y persiste.

**Ejemplos.** `PersonaController`, `PerfilController`, `DomicilioController`, `DocumentoVentaController`, `UsuarioController` y sus respectivos servicios Angular.

**Mantener.** Comprobar pertenencia por `empId` en modificación/borrado y no confiar en el `empId` enviado por navegador.

**Relaciones entre recursos.** Cuando un recurso multiempresa recibe un identificador de otro recurso, validar también su pertenencia al mismo `empId`. Por ejemplo, Usuario valida el Perfil con `findByEmpIdAndPerId`.

**Ejemplos activos.** Persona valida `domId` con `findByEmpIdAndDomId`; Venta valida `perIdVen` y `perIdCom` con `findByEmpIdAndPerId`.

**Auditoría.** Antes de guardar o actualizar, sobrescribir `*_UsuMov` con `JwtUser.getUsername()` y `*_FecMov` con la fecha del servidor. No usar el valor recibido desde Angular.

## Identificadores y fechas orientativos en formularios

**Objetivo.** Mostrar al usuario una previsión útil sin comprometer la integridad de concurrencia.

**Flujo.** Los endpoints `siguiente-id` pueden devolver un ID orientativo para mostrar al insertar. El frontend puede mostrar también una fecha local orientativa. Al guardar, el backend ignora el ID de alta y asigna la fecha de servidor; PostgreSQL genera el identificador definitivo.

**Caso concurrente.** Si otro usuario guarda antes, el registro puede recibir un ID y una hora distintos de los mostrados inicialmente. Es el comportamiento esperado.

**Evitar.** Copiar el patrón de Compras sin revisar su contrato, pues es diferente y actualmente inconsistente.

## Contraseñas de usuarios

**Objetivo.** No persistir ni devolver contraseñas en texto plano.

**Flujo.** El controlador cifra `usuCon` con BCrypt en altas y cambios explícitos de contraseña. Login busca el usuario por nombre y valida con `PasswordEncoder.matches`. Las cuentas heredadas con contraseña plana se cifran en el primer acceso correcto.

**Mantener.** En edición, una contraseña vacía conserva el hash existente; `usuCon` es JSON de solo escritura y no debe incluirse en tablas ni respuestas.

## Respuestas de error REST

**Objetivo.** Devolver errores HTTP coherentes a Angular sin exponer trazas internas.

**Flujo.** `GlobalExceptionHandler` intercepta excepciones de controladores y devuelve `ApiError` con `fecha`, `estado` y `mensaje`.

**Estados actuales.** Recurso no encontrado: 404; posición ocupada: 409; validación: 400; excepción imprevista: 500 con mensaje genérico.

## Validación de campos obligatorios

**Objetivo.** No depender exclusivamente de la validación de formularios Angular.

**Flujo.** Los controladores ejecutan validadores privados antes de guardar o actualizar. Usuario exige usuario, perfil, nombre, correo y contraseña en alta; Persona exige tipo/documento/nombre/primer apellido; Producto exige sus campos comerciales principales; Venta exige vendedor y comprador válidos.

**Respuesta.** Una validación incumplida lanza `IllegalArgumentException`, que se traduce en `400` mediante `GlobalExceptionHandler`.

## Tabla, formulario y PDF

**Objetivo.** Consultar, insertar, modificar, eliminar y exportar listados.

**Archivos.** Página de módulo, `components/tabla/`, `services/<recurso>.service.ts`, `services/pdf.service.ts` e interfaz correspondiente.

**Secuencia.** La página alterna `vistaActiva`; `Tabla` emite la fila seleccionada; la página prepara un objeto vacío o copia la selección; llama al servicio; tras éxito recarga `consultar()`. La exportación entrega las columnas y `tabla.datosFiltrados` a `PdfService`.

**Ejemplos.** `pages/mProductos/productos/`, `pages/mAdministracion/clientes/`, `pages/mVentas/documentosVenta/`.

## Selector de búsqueda

**Objetivo.** Elegir un ID por una descripción.

**API.** Entradas `datos`, `campoDescripcion`, `campoId`, `idSeleccionado`; salida `valorSeleccionado`.

**Comportamiento.** Muestra sugerencias a partir de tres caracteres, mantiene la selección y emite `0` si se vacía el texto.

**Ejemplos.** Ventas, Compras y Personas.

## Mapas UTM

**Objetivo.** Elegir y mostrar domicilios mediante Leaflet y Proj4.

**Archivos.** `SelectorMapa`, `MapaRegistros`, páginas de Domicilios.

**Comportamiento.** Convierte entre EPSG 25829/25830/25831 y WGS84. `SelectorMapa` emite `xChange`, `yChange` y `epsgChange`; `MapaRegistros` recibe una función que devuelve registros observables.

## Mallas

**Objetivo.** Ubicar productos o pintar posiciones manuales en una cuadrícula 50×100.

**Flujo comprobado.** `ProductoService` del backend guarda, actualiza o elimina la malla asociada a un producto. `MallaService` Angular llama a `/mallas`; `MallaRegistros` pinta colores y `SelectorMalla` devuelve fila/columna.

**Ejemplos.** `ProductoService.java`, `MallaService.java`, `productos/` y `gestionProductos/`.

**Evitar.** Eliminar posiciones con `malRefId` de registro: el servicio backend lo prohíbe.
