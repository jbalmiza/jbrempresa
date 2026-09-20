# Registro, gestión y consulta de proveedores

Revisión: 2026-09-17. Fuente: [RelacionesEmpresa](../src/app/pages/mAdministracion/relacionesEmpresa/relacionesEmpresa.ts). Reglas permanentes: [DIRECTIVAS.md](DIRECTIVAS.md).

## Relaciones de Empresa

Administración > Registro > Relaciones de Empresa permite Consultar, Insertar, Ver, Modificar y Eliminar. Seleccionar primero una fila. Eliminar pide confirmación indicando ambas empresas y elimina también la relación inversa.

Administración > Gestión > Gestión de Relaciones de Empresa permite consultar, ver, dar de baja y reactivar. Las dos direcciones cambian juntas. La baja conserva los registros; Eliminar es definitivo y exclusivo de Registro.

En la empresa compradora, tipo Proveedor significa que la empresa relacionada le suministra. En la suministradora aparece la compradora como Cliente.

## Tabla y formulario

Se usa tabla, compartida con Productos: filtros, paginación, configuración persistente y selección. Se declaran los 13 campos del contrato, incluidos nombres de empresas y movimientos. Los identificadores de empresa relacionada y pareja comienzan ocultos; Id Empresa sigue la regla genérica. Registro y Gestión tienen claves de configuración diferentes.

La vista abre vacía y consulta mediante Consultar o filtro. La selección recupera el objeto original aunque la fila muestre fechas y estado formateados. [Reglas de tablas](TABLAS.md).

El formulario edita relacionada, tipo, fechas y observaciones. Hay una limitación abierta: Gestión aún no presenta Tipo/Causa ni un histórico completo en Datos Movimiento. Véase [LIM-02](../../backend/docs/LIMITACIONES.md); no se considera una excepción autorizada a las directivas.

## Producto y módulo Proveedores

Producto ofrece Sin proveedor y empresas relacionadas activas. El módulo Proveedores abre primero su menú, con el grupo Catálogo y la opción **Mi catálogo**. Esta opción abre `/proveedores/catalogo` y utiliza la relación además de sus fechas para mostrar el catálogo: uno abre directamente, varios muestran selector, ninguno muestra un diálogo sin cargar catálogo.

El pedido se prepara con el mismo componente de Clientes. Cambiar proveedor descarta la cesta. No se crea una compra espejo en la compradora. [Guía de uso](GUIA_USO.md) y [catálogo](CATALOGO.md).

La API y los efectos sobre persistencia se mantienen en [el contrato backend](../../backend/docs/RELACIONES_EMPRESA.md), sin duplicar aquí sus esquemas.
