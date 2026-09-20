# Inventario de endpoints REST

Revisión: 2026-09-15. Inventario generado desde las fuentes; no acredita pruebas funcionales.

Generar: `node docs/verificar-documentacion.mjs --generar`. Validar sin escribir: `node docs/verificar-documentacion.mjs`.

Ver [contrato y permisos](API.md). Cada controlador enlazado define validación, parámetros y DTO. La tabla inventaría métodos declarados, no permisos inferidos por su nombre.

## AdjuntoController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/AdjuntoController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/adjuntos` | `List<Adjunto>` | `consultar( @RequestParam("modulo") String modulo, @RequestParam("tipoRegistro") String tipoRegistro, @RequestParam("registroId") Long registroId)` |
| POST | `/adjuntos` | `Adjunto` | `guardar( @RequestParam("modulo") String modulo, @RequestParam("tipoRegistro") String tipoRegistro, @RequestParam("registroId") Long registroId, @RequestParam("codigoRuta") String codigoRuta, @RequestParam("nombre") String nombre, @RequestParam("tipo") String tipo, @RequestPart("archivo") MultipartFile archivo)` |
| PUT | `/adjuntos/{id}/principal` | `Adjunto` | `marcarPrincipal( @PathVariable("id") Long id, @RequestParam("codigoRuta") String codigoRuta)` |
| GET | `/adjuntos/{id}/contenido` | `ResponseEntity<Resource>` | `obtenerContenido( @PathVariable("id") Long id, @RequestParam("codigoRuta") String codigoRuta, @RequestParam(name = "descargar", defaultValue = "false") boolean descargar)` |
| DELETE | `/adjuntos/{id}` | `void` | `eliminar( @PathVariable("id") Long id, @RequestParam("codigoRuta") String codigoRuta)` |

## AgendaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/AgendaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/agenda/recursos` | `List<RecursoSalida>` | `recursos()` |
| POST | `/agenda/recursos` | `RecursoSalida` | `guardarRecurso(@Valid @RequestBody RecursoEntrada entrada)` |
| GET | `/agenda/recursos/{id}/horarios` | `List<HorarioSalida>` | `horarios(@PathVariable Long id)` |
| PUT | `/agenda/recursos/{id}/horarios` | `List<HorarioSalida>` | `horarios(@PathVariable Long id,@Valid @RequestBody List<HorarioEntrada> entrada)` |
| GET | `/agenda/recursos/{id}/excepciones` | `List<ExcepcionSalida>` | `excepciones(@PathVariable Long id,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate desde,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate hasta)` |
| POST | `/agenda/recursos/{id}/excepciones` | `ExcepcionSalida` | `excepcion(@PathVariable Long id,@Valid @RequestBody ExcepcionEntrada entrada)` |
| DELETE | `/agenda/excepciones/{id}` | `void` | `eliminarExcepcion(@PathVariable Long id)` |
| GET | `/agenda/reservas` | `List<ReservaSalida>` | `reservas(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)LocalDateTime desde,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)LocalDateTime hasta)` |
| POST | `/agenda/reservas` | `ReservaSalida` | `reservar(@Valid @RequestBody ReservaEntrada entrada)` |
| POST | `/agenda/reservas/{id}/reprogramar` | `ReservaSalida` | `reprogramar(@PathVariable Long id,@Valid @RequestBody ReprogramacionEntrada entrada)` |
| PUT | `/agenda/reservas/{id}/estado` | `ReservaSalida` | `estado(@PathVariable Long id,@Valid @RequestBody EstadoEntrada entrada)` |
| GET | `/agenda/reservas/{id}/reprogramaciones` | `List<ReprogramacionSalida>` | `historial(@PathVariable Long id)` |

## AgendaEmpleadoController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/AgendaEmpleadoController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/empleados/mi-agenda` | `AgendaEmpleadoSalida` | `consultar(@RequestParam(required = false) Long recursoAgendaId)` |
| PUT | `/empleados/mi-agenda/tareas/{id}/estado` | `TareaEmpleadoSalida` | `estado(@PathVariable Long id, @Valid @RequestBody EstadoTareaEntrada entrada)` |
| PUT | `/empleados/mi-agenda/tareas/{id}/pagado` | `TareaEmpleadoSalida` | `pagado(@PathVariable Long id, @Valid @RequestBody PagoTareaEntrada entrada)` |

## AreaOrganizativaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/AreaOrganizativaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/areas-organizativas` | `List<AreaSalida>` | `consultar()` |
| POST | `/areas-organizativas` | `AreaSalida` | `guardar(@Valid @RequestBody AreaEntrada datos)` |
| PUT | `/areas-organizativas/{id}` | `AreaSalida` | `actualizar(@PathVariable Long id,@Valid @RequestBody AreaEntrada datos)` |
| DELETE | `/areas-organizativas/{id}` | `void` | `eliminar(@PathVariable Long id)` |

## AvisoAlertaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/AvisoAlertaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/avisos-alertas` | `List<AvisoAlerta>` | `consultar()` |
| GET | `/avisos-alertas/siguiente-id` | `Long` | `siguienteId()` |
| POST | `/avisos-alertas` | `AvisoAlerta` | `crear(@RequestBody AvisoAlerta a)` |
| PUT | `/avisos-alertas/{id}` | `AvisoAlerta` | `actualizar(@PathVariable Long id,@RequestBody AvisoAlerta a)` |
| DELETE | `/avisos-alertas/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| POST | `/avisos-alertas/{id}/baja` | `AvisoAlerta` | `baja(@PathVariable Long id)` |
| POST | `/avisos-alertas/{id}/reactivar` | `AvisoAlerta` | `reactivar(@PathVariable Long id)` |
| GET | `/avisos-alertas/{id}/historico` | `List<AvisoAlerta>` | `historico(@PathVariable Long id)` |
| GET | `/avisos-alertas/bandeja` | `List<AvisoAlertaDistribucionService.Entrega>` | `bandeja()` |
| POST | `/avisos-alertas/bandeja/{empresa}/{id}/lectura` | `void` | `leer(@PathVariable Long empresa,@PathVariable Long id)` |
| GET | `/avisos-alertas/ventana` | `List<AvisoAlertaDistribucionService.Entrega>` | `ventana(@RequestParam String ruta)` |

## CajaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/CajaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/caja/cajas` | `List<Caja>` | `cajas()` |
| POST | `/caja/cajas` | `Caja` | `crearCaja(@RequestBody Caja c)` |
| PUT | `/caja/cajas/{id}` | `Caja` | `editarCaja(@PathVariable Long id,@RequestBody Caja c)` |
| DELETE | `/caja/cajas/{id}` | `void` | `eliminarCaja(@PathVariable Long id)` |
| PATCH | `/caja/cajas/{id}/estado` | `Caja` | `estadoCaja(@PathVariable Long id,@RequestBody Estado e)` |
| GET | `/caja/sesiones` | `List<CajaSesion>` | `sesiones()` |
| POST | `/caja/cajas/{id}/abrir` | `CajaSesion` | `abrir(@PathVariable Long id,@RequestBody Apertura a)` |
| POST | `/caja/sesiones/{id}/cerrar` | `CajaSesion` | `cerrar(@PathVariable Long id,@RequestBody Cierre c)` |
| GET | `/caja/sesiones/{id}/movimientos` | `List<CajaMovimiento>` | `movimientos(@PathVariable Long id)` |
| POST | `/caja/sesiones/{id}/movimientos` | `CajaMovimiento` | `movimiento(@PathVariable Long id,@RequestBody CajaMovimiento m)` |
| DELETE | `/caja/movimientos/{id}` | `void` | `anular(@PathVariable Long id)` |

## CatalogoController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/CatalogoController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/catalogo/gestion/configuracion` | `CatalogoDtos.Configuracion` | `configuracion()` |
| PUT | `/catalogo/gestion/configuracion` | `CatalogoDtos.Configuracion` | `configurar(@Valid @RequestBody CatalogoDtos.ConfiguracionEntrada e)` |
| GET | `/catalogo/gestion/pago` | `ConfiguracionRedsysBizumService.EstadoConfiguracion` | `configuracionPago()` |
| GET | `/catalogo/gestion/posiciones` | `List<CatalogoPosicion>` | `posiciones()` |
| POST | `/catalogo/gestion/posiciones` | `CatalogoPosicion` | `posicion(@RequestBody CatalogoPosicion p)` |
| POST | `/catalogo/gestion/posiciones/{id}/regenerar` | `CatalogoPosicion` | `regenerar(@PathVariable Long id)` |
| GET | `/catalogo/gestion/posiciones/{id}/qr` | `byte[]` | `qr(@PathVariable Long id,@RequestParam String baseUrl)` |
| GET | `/catalogo/proveedores` | `List<com.jbrempresa.backend.dto.administracion.EmpresaRelacionDtos.Opcion>` | `proveedores()` |
| GET | `/catalogo/proveedores/{id}` | `CatalogoDtos.CatalogoPublico` | `proveedor(@PathVariable Long id)` |
| POST | `/catalogo/proveedores/{id}/pedidos` | `CatalogoDtos.PedidoConfirmacion` | `pedirProveedor(@PathVariable Long id,@Valid @RequestBody CatalogoDtos.PedidoEntrada e)` |
| GET | `/catalogo/proveedores/{proveedor}/productos/{id}/imagen` | `ResponseEntity<Resource>` | `imagenProductoProveedor(@PathVariable Long proveedor,@PathVariable Long id)` |
| GET | `/catalogo/proveedores/{proveedor}/servicios/{id}/imagen` | `ResponseEntity<Resource>` | `imagenServicioProveedor(@PathVariable Long proveedor,@PathVariable Long id)` |
| GET | `/catalogo/proveedores/{proveedor}/empresa/imagen` | `ResponseEntity<Resource>` | `imagenEmpresaProveedor(@PathVariable Long proveedor)` |
| GET | `/catalogo/publico/{token}` | `CatalogoDtos.CatalogoPublico` | `publico(@PathVariable String token)` |
| POST | `/catalogo/publico/{token}/pedidos` | `CatalogoDtos.PedidoConfirmacion` | `pedir(@PathVariable String token,@Valid @RequestBody CatalogoDtos.PedidoEntrada e)` |
| GET | `/catalogo/publico/{token}/productos/{id}/imagen` | `ResponseEntity<Resource>` | `imagenProducto(@PathVariable String token,@PathVariable Long id)` |
| GET | `/catalogo/publico/{token}/servicios/{id}/imagen` | `ResponseEntity<Resource>` | `imagenServicio(@PathVariable String token,@PathVariable Long id)` |
| GET | `/catalogo/publico/{token}/empresa/imagen` | `ResponseEntity<Resource>` | `imagenEmpresa(@PathVariable String token)` |

## CatalogoTerritorialController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/CatalogoTerritorialController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/territorio/paises` | `List<Pais>` | `paises()` |
| POST | `/territorio/paises` | `Pais` | `crearPais(@RequestBody Pais v)` |
| PUT | `/territorio/paises/{id}` | `Pais` | `editarPais(@PathVariable Long id,@RequestBody Pais v)` |
| DELETE | `/territorio/paises/{id}` | `void` | `borrarPais(@PathVariable Long id)` |
| GET | `/territorio/provincias` | `List<Provincia>` | `provincias()` |
| POST | `/territorio/provincias` | `Provincia` | `crearProvincia(@RequestBody Provincia v)` |
| PUT | `/territorio/provincias/{id}` | `Provincia` | `editarProvincia(@PathVariable Long id,@RequestBody Provincia v)` |
| DELETE | `/territorio/provincias/{id}` | `void` | `borrarProvincia(@PathVariable Long id)` |
| GET | `/territorio/municipios` | `List<Municipio>` | `municipios()` |
| POST | `/territorio/municipios` | `Municipio` | `crearMunicipio(@RequestBody Municipio v)` |
| PUT | `/territorio/municipios/{id}` | `Municipio` | `editarMunicipio(@PathVariable Long id,@RequestBody Municipio v)` |
| DELETE | `/territorio/municipios/{id}` | `void` | `borrarMunicipio(@PathVariable Long id)` |
| GET | `/territorio/codigos-postales` | `List<CodigoPostal>` | `codigos()` |
| POST | `/territorio/codigos-postales` | `CodigoPostal` | `crearCodigo(@RequestBody CodigoPostal v)` |
| PUT | `/territorio/codigos-postales/{id}` | `CodigoPostal` | `editarCodigo(@PathVariable Long id,@RequestBody CodigoPostal v)` |
| DELETE | `/territorio/codigos-postales/{id}` | `void` | `borrarCodigo(@PathVariable Long id)` |
| GET | `/territorio/vias` | `List<Via>` | `vias()` |
| POST | `/territorio/vias` | `Via` | `crearVia(@RequestBody Via v)` |
| PUT | `/territorio/vias/{id}` | `Via` | `editarVia(@PathVariable Long id,@RequestBody Via v)` |
| DELETE | `/territorio/vias/{id}` | `void` | `borrarVia(@PathVariable Long id)` |

## ComponenteController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/ComponenteController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/componentes` | `List<Componente>` | `listar()` |
| GET | `/componentes/{id}/historico` | `List<Componente>` | `historico(@PathVariable Long id)` |
| POST | `/componentes` | `Componente` | `guardar(@RequestBody Componente i)` |
| PUT | `/componentes/{id}` | `Componente` | `actualizar(@PathVariable Long id,@RequestBody Componente i)` |
| DELETE | `/componentes/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| POST | `/componentes/{id}/baja` | `Componente` | `baja(@PathVariable Long id)` |
| POST | `/componentes/{id}/deshacer` | `Componente` | `deshacer(@PathVariable Long id)` |

## CompraController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/CompraController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| POST | `/compras` | `CompraDtos.Salida` | `guardar(@Valid @RequestBody CompraDtos.Entrada entrada)` |
| PUT | `/compras/{id}` | `CompraDtos.Salida` | `actualizar(@PathVariable Long id,@Valid @RequestBody CompraDtos.Entrada entrada)` |
| GET | `/compras` | `List<CompraDtos.Salida>` | `consultar()` |
| GET | `/compras/siguiente-id` | `Long` | `siguienteId()` |
| DELETE | `/compras/{id}` | `void` | `eliminar(@PathVariable Long id)` |

## ComunicacionController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/ComunicacionController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/comunicaciones` | `List<ConversacionSalida>` | `consultar()` |
| GET | `/comunicaciones/entrada` | `List<MensajeSalida>` | `entrada()` |
| GET | `/comunicaciones/entrada/{mensajeId}/candidatos` | `List<Persona>` | `candidatos(@PathVariable Long mensajeId)` |
| PATCH | `/comunicaciones/entrada/{mensajeId}/persona/{personaId}` | `MensajeSalida` | `confirmarPersona(@PathVariable Long mensajeId,@PathVariable Long personaId)` |
| POST | `/comunicaciones/entrada` | `MensajeSalida` | `recibir(@Valid @RequestBody MensajeEntrada datos)` |
| PATCH | `/comunicaciones/entrada/{mensajeId}/clasificacion` | `MensajeSalida` | `confirmarClasificacion(@PathVariable Long mensajeId,@Valid @RequestBody ClasificacionEntrada datos)` |
| POST | `/comunicaciones/entrada/{mensajeId}/nueva-conversacion` | `ConversacionSalida` | `nuevaConversacion(@PathVariable Long mensajeId,@Valid @RequestBody ConversacionEntrada datos)` |
| POST | `/comunicaciones/entrada/{mensajeId}/vincular/{comunicacionId}` | `Mensaje` | `vincularExistente(@PathVariable Long mensajeId,@PathVariable Long comunicacionId)` |
| GET | `/comunicaciones/{id}` | `ConversacionSalida` | `obtener(@PathVariable Long id)` |
| GET | `/comunicaciones/persona/{personaId}` | `List<ConversacionSalida>` | `conversacionesPersona(@PathVariable Long personaId)` |
| POST | `/comunicaciones` | `ConversacionSalida` | `crear(@Valid @RequestBody ConversacionEntrada datos)` |
| PUT | `/comunicaciones/{id}` | `ConversacionSalida` | `actualizar(@PathVariable Long id,@Valid @RequestBody ConversacionEntrada datos)` |
| PATCH | `/comunicaciones/{id}/estado/{estado}` | `ConversacionSalida` | `estado(@PathVariable Long id,@PathVariable String estado)` |
| PATCH | `/comunicaciones/{id}/asignacion` | `ConversacionSalida` | `asignar(@PathVariable Long id,@RequestParam(required=false) Long usuId,@RequestParam(required=false) Long areId)` |
| GET | `/comunicaciones/{id}/mensajes` | `List<MensajeSalida>` | `mensajes(@PathVariable Long id)` |
| POST | `/comunicaciones/{id}/mensajes` | `MensajeSalida` | `mensaje(@PathVariable Long id,@Valid @RequestBody RespuestaEntrada datos)` |
| GET | `/comunicaciones/contactos` | `List<ContactoSalida>` | `contactos()` |
| GET | `/comunicaciones/contactos/sin-identificar` | `List<ContactoSalida>` | `contactosSinIdentificar()` |
| GET | `/comunicaciones/contactos/{contactoId}/personas` | `List<PersonaContactoCanal>` | `personasContacto(@PathVariable Long contactoId)` |
| POST | `/comunicaciones/contactos/{contactoId}/persona/{personaId}` | `PersonaContactoCanal` | `identificarContacto(@PathVariable Long contactoId,@PathVariable Long personaId)` |
| DELETE | `/comunicaciones/contactos/{contactoId}/persona/{personaId}` | `void` | `revocarContacto(@PathVariable Long contactoId,@PathVariable Long personaId)` |
| POST | `/comunicaciones/contactos` | `ContactoSalida` | `contacto(@Valid @RequestBody ContactoEntrada datos)` |

## ConfiguracionTablaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/ConfiguracionTablaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/configuraciones-tabla` | `ResponseEntity<ConfiguracionTabla>` | `obtener(@RequestParam String clave)` |
| PUT | `/configuraciones-tabla` | `ConfiguracionTabla` | `guardar(@RequestBody ConfiguracionTabla entrada)` |

## DocumentoVentaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/DocumentoVentaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/documentos-venta/configuracion` | `ConfiguracionDocumentosVentaService.Configuracion` | `configuracion()` |
| GET | `/documentos-venta/{tipo}` | `List<DocumentoVentaDtos.Salida>` | `consultar(@PathVariable String tipo, @RequestParam(defaultValue = "false") boolean incluirBajas)` |
| POST | `/documentos-venta/{tipo}` | `DocumentoVentaDtos.Salida` | `guardar(@PathVariable String tipo, @Valid @RequestBody DocumentoVentaDtos.Entrada entrada)` |
| PUT | `/documentos-venta/{tipo}/{id}` | `DocumentoVentaDtos.Salida` | `actualizar(@PathVariable String tipo, @PathVariable Long id, @RequestParam(defaultValue = "false") boolean propagarCadena, @RequestParam(required = false) String clave, @Valid @RequestBody DocumentoVentaDtos.Entrada entrada)` |
| POST | `/documentos-venta/{tipo}/{id}/baja` | `DocumentoVentaDtos.Salida` | `baja(@PathVariable String tipo, @PathVariable Long id)` |
| POST | `/documentos-venta/{tipo}/{id}/reactivar` | `DocumentoVentaDtos.Salida` | `reactivar(@PathVariable String tipo, @PathVariable Long id)` |
| GET | `/documentos-venta/{tipo}/{id}/historico` | `List<DocumentoVentaMovimiento>` | `historico(@PathVariable String tipo, @PathVariable Long id)` |
| DELETE | `/documentos-venta/{tipo}/{id}` | `void` | `eliminarActual(@PathVariable String tipo, @PathVariable Long id)` |
| DELETE | `/documentos-venta/{tipo}/{id}/completo` | `void` | `eliminarCompleto(@PathVariable String tipo, @PathVariable Long id)` |
| POST | `/documentos-venta/{tipo}/{id}/convertir` | `DocumentoVentaDtos.Salida` | `convertir(@PathVariable String tipo, @PathVariable Long id)` |

## DomicilioController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/DomicilioController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| POST | `/domicilio` | `Domicilio` | `guardar(@RequestBody Domicilio domicilio)` |
| PUT | `/domicilio/{id}` | `Domicilio` | `actualizar(@PathVariable Long id, @RequestBody Domicilio domicilio)` |
| GET | `/domicilio` | `List<Domicilio>` | `obtenerDomicilios()` |
| GET | `/domicilio/siguiente-id` | `Long` | `obtenerSiguienteId()` |
| DELETE | `/domicilio/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| POST | `/domicilio/{id}/baja` | `Domicilio` | `baja(@PathVariable Long id)` |
| GET | `/domicilio/{id}/historico` | `List<Domicilio>` | `historico(@PathVariable Long id)` |
| POST | `/domicilio/{id}/deshacer` | `Domicilio` | `deshacer(@PathVariable Long id)` |

## EmpresaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/EmpresaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/empresas` | `List<Empresa>` | `obtenerEmpresas(@RequestParam(defaultValue = "false") boolean incluirBajas)` |
| POST | `/empresas` | `Empresa` | `guardar(@RequestBody Empresa entrada)` |
| GET | `/empresas/siguiente-id` | `Long` | `siguienteId()` |
| DELETE | `/empresas/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| PUT | `/empresas/{id}/baja` | `Empresa` | `baja(@PathVariable Long id, @RequestBody CausaEmpresa datos)` |
| PUT | `/empresas/{id}/reactivacion` | `Empresa` | `reactivar(@PathVariable Long id, @RequestBody CausaEmpresa datos)` |
| GET | `/empresas/{id}/historico` | `List<EmpresaMovimiento>` | `historico(@PathVariable Long id)` |
| PUT | `/empresas/{id}` | `Empresa` | `actualizar(@PathVariable Long id, @RequestBody Empresa entrada)` |
| POST | `/empresas/{id}/imagen` | `Empresa` | `subirImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo)` |
| GET | `/empresas/{id}/imagen` | `ResponseEntity<Resource>` | `obtenerImagen(@PathVariable Long id)` |

## EmpresaRelacionController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/EmpresaRelacionController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/empresas-relaciones` | `List<Salida>` | `listar()` |
| GET | `/empresas-relaciones/{id}/historico` | `List<com.jbrempresa.backend.entity.EmpresaRelacionMovimiento>` | `historico(@PathVariable Long id)` |
| GET | `/empresas-relaciones/empresas` | `List<Opcion>` | `empresas()` |
| GET | `/empresas-relaciones/proveedores` | `List<Opcion>` | `proveedores()` |
| POST | `/empresas-relaciones` | `Salida` | `crear(@Valid@RequestBody Entrada in)` |
| PUT | `/empresas-relaciones/{id}` | `Salida` | `actualizar(@PathVariable Long id,@Valid@RequestBody Entrada in)` |
| DELETE | `/empresas-relaciones/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| PATCH | `/empresas-relaciones/{id}/baja` | `void` | `baja(@PathVariable Long id,@Valid @RequestBody MovimientoEntrada in)` |
| PATCH | `/empresas-relaciones/{id}/reactivacion` | `void` | `reactivar(@PathVariable Long id,@Valid @RequestBody MovimientoEntrada in)` |

## HelloController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/HelloController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/api/hello` | `String` | `hello()` |

## MallaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/MallaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| POST | `/mallas` | `Malla` | `guardar( @RequestBody Malla malla)` |
| PUT | `/mallas/{id}` | `Malla` | `actualizar( @PathVariable Long id, @RequestBody Malla malla)` |
| GET | `/mallas` | `List<Malla>` | `obtenerMallas()` |
| GET | `/mallas/siguiente-id` | `Long` | `obtenerSiguienteId()` |
| POST | `/mallas/pintar` | `Malla` | `pintar( @RequestBody Malla malla)` |
| DELETE | `/mallas/posicion/{entidad}/{fila}/{columna}` | `void` | `borrarPosicion( @PathVariable String entidad, @PathVariable Integer fila, @PathVariable Integer columna)` |
| DELETE | `/mallas/{id}` | `void` | `eliminar( @PathVariable Long id)` |

## MensajeriaInternaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/MensajeriaInternaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/mensajeria-interna/destinatarios` | `List<Destinatario>` | `destinatarios()` |
| GET | `/mensajeria-interna/conversaciones` | `List<Conversacion>` | `conversaciones()` |
| GET | `/mensajeria-interna/conversaciones/bajas` | `List<Conversacion>` | `bajas()` |
| GET | `/mensajeria-interna/conversaciones/{id}/mensajes` | `List<Mensaje>` | `mensajes(@PathVariable Long id,@RequestParam(defaultValue="true")boolean marcarLeido)` |
| PATCH | `/mensajeria-interna/conversaciones/{id}/lectura` | `Conversacion` | `lectura(@PathVariable Long id,@RequestBody Map<String,Boolean> in)` |
| GET | `/mensajeria-interna/no-leidos` | `Map<String,Long>` | `noLeidos()` |
| POST | `/mensajeria-interna/conversaciones` | `Conversacion` | `crear(@Valid@RequestBody NuevaConversacion in)` |
| POST | `/mensajeria-interna/conversaciones/{id}/mensajes` | `Mensaje` | `responder(@PathVariable Long id,@Valid@RequestBody NuevoMensaje in)` |
| PUT | `/mensajeria-interna/mensajes/{id}` | `Mensaje` | `editar(@PathVariable Long id,@Valid@RequestBody EditarMensaje in)` |
| PATCH | `/mensajeria-interna/conversaciones/{id}/baja` | `void` | `baja(@PathVariable Long id)` |
| PATCH | `/mensajeria-interna/conversaciones/{id}/reactivacion` | `Conversacion` | `reactivar(@PathVariable Long id)` |
| DELETE | `/mensajeria-interna/conversaciones/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| DELETE | `/mensajeria-interna/conversaciones` | `Map<String,Integer>` | `eliminarHasta(@RequestParam java.time.LocalDate hasta)` |

## ModuloAplicacionController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/ModuloAplicacionController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/modulos-aplicacion` | `List<ModuloAplicacion>` | `listar()` |
| POST | `/modulos-aplicacion` | `ModuloAplicacion` | `guardar(@RequestBody ModuloEntrada in)` |
| PUT | `/modulos-aplicacion/{id}` | `ModuloAplicacion` | `actualizar(@PathVariable Long id,@RequestBody ModuloEntrada in)` |
| DELETE | `/modulos-aplicacion/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| GET | `/modulos-aplicacion/gestion` | `List<ModuloPanel>` | `gestion(@RequestParam Long empresaId)` |
| PUT | `/modulos-aplicacion/gestion` | `List<ModuloPanel>` | `gestionar(@RequestParam Long empresaId,@RequestBody List<Configuracion> lista)` |
| GET | `/modulos-aplicacion/panel` | `List<ModuloPanel>` | `panel()` |

## ParametroController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/ParametroController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/parametros` | `List<Parametro>` | `consultar( @RequestParam(name = "modulo", required = false) String modulo)` |
| POST | `/parametros` | `Parametro` | `guardar(@RequestBody Parametro parametro)` |
| PUT | `/parametros/{id}` | `Parametro` | `actualizar(@PathVariable Long id, @RequestBody Parametro parametro)` |
| DELETE | `/parametros/{id}` | `void` | `eliminar(@PathVariable Long id)` |

## PerfilController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/PerfilController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| POST | `/perfiles` | `Perfil` | `guardar( @RequestBody Perfil perfil)` |
| PUT | `/perfiles/{id}` | `Perfil` | `actualizar( @PathVariable Long id, @RequestBody Perfil perfil)` |
| GET | `/perfiles` | `List<Perfil>` | `obtenerPerfiles()` |
| GET | `/perfiles/selector` | `List<Perfil>` | `obtenerPerfilesSelector()` |
| GET | `/perfiles/siguiente-id` | `Long` | `obtenerSiguienteId()` |
| DELETE | `/perfiles/{id}` | `void` | `eliminar( @PathVariable Long id)` |

## PersonaComplementosController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/PersonaComplementosController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/personas-complementos/representantes` | `Representante` | `crear(@RequestBody Representante v)` |
| POST | `/personas-complementos/representantes` | `Representante` | `crear(@RequestBody Representante v)` |
| PUT | `/personas-complementos/representantes/{id}` | `Representante` | `actualizar(@PathVariable Long id,@RequestBody Representante v)` |
| DELETE | `/personas-complementos/representantes/{id}` | `void` | `borrarRepresentante(@PathVariable Long id)` |
| GET | `/personas-complementos/domicilios-notificacion` | `DomicilioNotificacion` | `crear(@RequestBody DomicilioNotificacion v)` |
| POST | `/personas-complementos/domicilios-notificacion` | `DomicilioNotificacion` | `crear(@RequestBody DomicilioNotificacion v)` |
| PUT | `/personas-complementos/domicilios-notificacion/{id}` | `DomicilioNotificacion` | `actualizar(@PathVariable Long id,@RequestBody DomicilioNotificacion v)` |
| DELETE | `/personas-complementos/domicilios-notificacion/{id}` | `void` | `borrarNotificacion(@PathVariable Long id)` |
| GET | `/personas-complementos/domiciliaciones-bancarias` | `DomiciliacionBancaria` | `crear(@RequestBody DomiciliacionBancaria v)` |
| POST | `/personas-complementos/domiciliaciones-bancarias` | `DomiciliacionBancaria` | `crear(@RequestBody DomiciliacionBancaria v)` |
| PUT | `/personas-complementos/domiciliaciones-bancarias/{id}` | `DomiciliacionBancaria` | `actualizar(@PathVariable Long id,@RequestBody DomiciliacionBancaria v)` |
| DELETE | `/personas-complementos/domiciliaciones-bancarias/{id}` | `void` | `borrarBanco(@PathVariable Long id)` |

## PersonaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/PersonaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| POST | `/personas` | `Persona` | `guardar( @RequestBody Persona persona)` |
| PUT | `/personas/{id}` | `Persona` | `actualizar( @PathVariable("id") Long id, @RequestBody Persona persona)` |
| GET | `/personas` | `List<Persona>` | `obtenerPersonas()` |
| GET | `/personas/siguiente-id` | `Long` | `obtenerSiguienteId()` |
| DELETE | `/personas/{id}` | `void` | `eliminar( @PathVariable("id") Long id)` |
| GET | `/personas/selector` | `List<Persona>` | `obtenerPersonasSelector()` |
| POST | `/personas/{id}/baja` | `Persona` | `baja(@PathVariable("id") Long id)` |
| GET | `/personas/{id}/historico` | `List<Persona>` | `obtenerHistorico(@PathVariable("id") Long id)` |
| POST | `/personas/{id}/deshacer` | `Persona` | `deshacer(@PathVariable("id") Long id)` |

## PersonalAreaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/PersonalAreaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/personal-area` | `List<PersonalSalida>` | `consultar()` |
| POST | `/personal-area` | `PersonalSalida` | `guardar(@Valid @RequestBody PersonalEntrada datos)` |
| PUT | `/personal-area/{id}` | `PersonalSalida` | `actualizar(@PathVariable Long id,@Valid @RequestBody PersonalEntrada datos)` |
| DELETE | `/personal-area/{id}` | `void` | `eliminar(@PathVariable Long id)` |

## ProductoController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/ProductoController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| POST | `/productos` | `Producto` | `guardar( @RequestBody Producto producto)` |
| PUT | `/productos/{id}` | `Producto` | `actualizar( @PathVariable Long id, @RequestBody Producto producto)` |
| GET | `/productos` | `List<Producto>` | `obtenerProductos()` |
| GET | `/productos/siguiente-id` | `Long` | `obtenerSiguienteId()` |
| DELETE | `/productos/{id}` | `void` | `eliminar( @PathVariable Long id)` |
| POST | `/productos/{id}/baja` | `Producto` | `baja(@PathVariable Long id)` |
| GET | `/productos/{id}/historico` | `List<Producto>` | `historico(@PathVariable Long id)` |
| POST | `/productos/{id}/deshacer` | `Producto` | `deshacer(@PathVariable Long id)` |
| POST | `/productos/{id}/imagen` | `Producto` | `subirImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo)` |
| GET | `/productos/{id}/imagen` | `ResponseEntity<Resource>` | `obtenerImagen(@PathVariable Long id)` |

## PropuestaRespuestaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/PropuestaRespuestaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/comunicaciones/{comunicacionId}/propuestas-respuesta` | `List<PropuestaRespuesta>` | `consultar(@PathVariable Long comunicacionId)` |
| POST | `/comunicaciones/{comunicacionId}/propuestas-respuesta` | `PropuestaRespuesta` | `generar(@PathVariable Long comunicacionId)` |
| POST | `/comunicaciones/{comunicacionId}/propuestas-respuesta/{propuestaId}/aprobar` | `Mensaje` | `aprobar(@PathVariable Long comunicacionId, @PathVariable Long propuestaId, @RequestBody Map<String, String> datos)` |
| POST | `/comunicaciones/{comunicacionId}/propuestas-respuesta/{propuestaId}/rechazar` | `PropuestaRespuesta` | `rechazar(@PathVariable Long comunicacionId, @PathVariable Long propuestaId)` |

## RecuperacionContrasenaController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/RecuperacionContrasenaController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| POST | `/auth/password/solicitar` | `RespuestaRecuperacionContrasena` | `solicitar( @RequestBody SolicitudRecuperacionContrasena solicitud)` |
| POST | `/auth/password/confirmar` | `ResponseEntity<Void>` | `confirmar( @RequestBody ConfirmacionRecuperacionContrasena confirmacion)` |

## RecursoController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/RecursoController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/recursos` | `List<RecursoOperativo>` | `consultar()` |
| POST | `/recursos` | `RecursoOperativo` | `crear(@RequestBody RecursoOperativo r)` |
| PUT | `/recursos/{id}` | `RecursoOperativo` | `actualizar(@PathVariable Long id,@RequestBody RecursoOperativo entrada)` |
| POST | `/recursos/{id}/baja` | `RecursoOperativo` | `baja(@PathVariable Long id)` |
| DELETE | `/recursos/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| GET | `/recursos/{id}/historico` | `List<RecursoOperativo>` | `historico(@PathVariable Long id)` |
| POST | `/recursos/{id}/deshacer` | `RecursoOperativo` | `deshacer(@PathVariable Long id)` |
| PATCH | `/recursos/{id}/operativo` | `RecursoOperativo` | `operativo(@PathVariable Long id,@RequestBody Estado e)` |
| GET | `/recursos/{id}/capacidades` | `List<RecursoCapacidad>` | `capacidades(@PathVariable Long id)` |
| PUT | `/recursos/{id}/capacidades` | `List<RecursoCapacidad>` | `capacidades(@PathVariable Long id,@RequestBody List<CapacidadEntrada> lista)` |
| GET | `/recursos/tipos-capacidad` | `List<CapacidadEntrada>` | `tipos()` |

## ServicioController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/ServicioController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/servicios` | `List<Servicio>` | `consultar()` |
| GET | `/servicios/siguiente-id` | `Long` | `siguienteId()` |
| POST | `/servicios` | `Servicio` | `guardar(@RequestBody Servicio s)` |
| PUT | `/servicios/{id}` | `Servicio` | `actualizar(@PathVariable Long id,@RequestBody Servicio s)` |
| DELETE | `/servicios/{id}` | `void` | `eliminar(@PathVariable Long id)` |
| POST | `/servicios/{id}/baja` | `Servicio` | `baja(@PathVariable Long id)` |
| GET | `/servicios/{id}/historico` | `List<Servicio>` | `historico(@PathVariable Long id)` |
| POST | `/servicios/{id}/deshacer` | `Servicio` | `deshacer(@PathVariable Long id)` |
| POST | `/servicios/{id}/imagen` | `Servicio` | `subirImagen(@PathVariable Long id,@RequestParam("archivo") MultipartFile archivo)` |
| GET | `/servicios/{id}/imagen` | `ResponseEntity<Resource>` | `obtenerImagen(@PathVariable Long id)` |

## TipoArticuloController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/TipoArticuloController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/tipos-articulo/{clase}` | `List<TipoArticulo>` | `listar(@PathVariable String clase)` |
| POST | `/tipos-articulo/{clase}` | `TipoArticulo` | `guardar(@PathVariable String clase,@RequestBody TipoArticulo in)` |
| DELETE | `/tipos-articulo/{clase}/{id}` | `void` | `eliminar(@PathVariable String clase,@PathVariable Long id)` |
| POST | `/tipos-articulo/{clase}/{id}/imagen` | `TipoArticulo` | `imagen(@PathVariable String clase,@PathVariable Long id,@RequestParam MultipartFile archivo)` |
| GET | `/tipos-articulo/{clase}/{id}/imagen` | `ResponseEntity<Resource>` | `verImagen(@PathVariable String clase,@PathVariable Long id)` |

## TwilioWhatsappWebhookController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/TwilioWhatsappWebhookController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| POST | `/webhooks/twilio/whatsapp` | `ResponseEntity<String>` | `recibir( @RequestHeader(name = "X-Twilio-Signature", required = false) String firma, @RequestParam Map<String,String> parametros)` |

## UsuarioController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/UsuarioController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/usuarios/actividad` | `void` | `actividad()` |
| POST | `/usuarios` | `UsuarioSalida` | `guardar( @Valid @RequestBody UsuarioEntrada datos)` |
| PUT | `/usuarios/{id}` | `UsuarioSalida` | `actualizar( @PathVariable Long id, @Valid @RequestBody UsuarioEntrada datos)` |
| GET | `/usuarios` | `List<UsuarioSalida>` | `obtenerUsuarios()` |
| GET | `/usuarios/consulta` | `Page<UsuarioSalida>` | `consultarUsuarios( @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "50") int tamanio, @RequestParam(required = false) String empId, @RequestParam(required = false) String usuId, @RequestParam(required = false) String usuUsu, @RequestParam(required = false) String perId, @RequestParam(required = false) String usuNom, @RequestParam(required = false) String usuEma, @RequestParam(required = false) String usuUsuMov, @RequestParam(required = false) String usuFecMov, @RequestParam(required = false) String usuAct)` |
| PUT | `/usuarios/{id}/baja` | `UsuarioSalida` | `baja(@PathVariable Long id, @RequestBody CausaUsuario datos)` |
| PUT | `/usuarios/{id}/reactivacion` | `UsuarioSalida` | `reactivar(@PathVariable Long id, @RequestBody CausaUsuario datos)` |
| GET | `/usuarios/{id}/historico` | `List<UsuarioMovimiento>` | `historico(@PathVariable Long id)` |
| GET | `/usuarios/siguiente-id` | `Long` | `obtenerSiguienteId()` |
| POST | `/usuarios/login` | `LoginResponse` | `login( @Valid @RequestBody LoginRequest usuario)` |
| DELETE | `/usuarios/{id}` | `void` | `eliminar( @PathVariable Long id)` |

## WhatsappWebhookController

[Fuente](../src/main/java/com/jbrempresa/backend/controller/WhatsappWebhookController.java).

| Método | Ruta | Salida Java | Operación y parámetros Java |
|---|---|---|---|
| GET | `/webhooks/meta/whatsapp` | `ResponseEntity<String>` | `verificar( @RequestParam(name = "hub.mode", required = false) String modo, @RequestParam(name = "hub.verify_token", required = false) String token, @RequestParam(name = "hub.challenge", required = false) String reto)` |
| POST | `/webhooks/meta/whatsapp` | `ResponseEntity<Void>` | `recibir(@RequestHeader(name = "X-Hub-Signature-256", required = false) String firma, @RequestBody String cuerpo)` |
