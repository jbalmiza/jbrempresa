# Relaciones entre empresas

Revisión: 2026-09-17. Fuentes: [EmpresaRelacionService](../src/main/java/com/jbrempresa/backend/service/EmpresaRelacionService.java), [controlador](../src/main/java/com/jbrempresa/backend/controller/EmpresaRelacionController.java), [CatalogoProveedorService](../src/main/java/com/jbrempresa/backend/service/CatalogoProveedorService.java).

## Modelo y operaciones

empresas_relaciones contiene id, empId, empresaRelacionadaId, tipo, parejaId, fechaInicio, fechaFin, observaciones, activa y último usuario/fecha de movimiento. Las respuestas añaden nombres de ambas empresas.

Si A registra a B como PROVEEDOR, B recibe a A como CLIENTE. Alta, modificación, baja y reactivación actúan sobre ambos sentidos. Eliminar borra la pareja en una transacción, validando pertenencia y coherencia inversa. Antes de borrar se liberan sus enlaces. No se eliminan empresas ni productos.

Registro contiene Consultar, Insertar, Ver/Modificar y Eliminar; Gestión Baja y Reactivar. El historial y Datos Movimiento de este maestro tienen una divergencia conocida: [LIM-02](LIMITACIONES.md).

## Contrato

| Método y ruta | Entrada | Resultado |
|---|---|---|
| GET /empresas-relaciones | Empresa del contexto | Lista de Salida. |
| GET /empresas-relaciones/empresas | Contexto | Empresas distintas de la actual, para seleccionar relación. |
| GET /empresas-relaciones/proveedores | Contexto | Proveedores activos para Producto; no aplica fechas como el catálogo. |
| POST /empresas-relaciones | Entrada | Pareja creada y Salida del sentido solicitado. |
| PUT /empresas-relaciones/{id} | Entrada | Actualiza ambos sentidos. |
| PATCH /empresas-relaciones/{id}/baja | Sin cuerpo funcional | Inactiva la pareja. |
| PATCH /empresas-relaciones/{id}/reactivacion | Sin cuerpo funcional | Activa la pareja. |
| DELETE /empresas-relaciones/{id} | Identificador propio | Elimina ambos sentidos, sin respuesta de negocio. |

Entrada: empresaRelacionadaId obligatorio; tipo PROVEEDOR o CLIENTE; fechas opcionales ordenadas; observaciones hasta 500 caracteres. Las validaciones de servicio rechazan autorrelaciones, relación inexistente/incoherente y duplicados; la base impone unicidad por empresa, relacionada y tipo. Véanse [DTO](../src/main/java/com/jbrempresa/backend/dto/administracion/EmpresaRelacionDtos.java) y [errores API](API.md).

## Catálogo de proveedores

Consulta, imágenes y pedido requieren sesión, permiso del módulo y relación activa dentro del periodo. Se comprueba nuevamente al enviar. El pedido pertenece a la proveedora. Contrato completo y diferencia con el catálogo público en [CATALOGO.md](CATALOGO.md); uso de pantallas en [documentación frontend](../../frontend/docs/RELACIONES_EMPRESA.md).

## Datos iniciales

El alta manual del 15 de septiembre registró pizzería y restaurante como clientes del Proveedor Hostelero Central; los identificadores de aquella operación son evidencia histórica en CAMBIOS, no constantes del modelo.

El código actual no incluye un inicializador de relaciones de empresa. Las relaciones de ejemplo creadas manualmente no se reconstruyen automáticamente en una base nueva ni al reiniciar; véase [LIM-03](LIMITACIONES.md). La auditoría del 15 de septiembre afirmó que existía esa clase, pero esa afirmación no describe el árbol de trabajo actual.
