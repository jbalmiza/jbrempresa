# Arquitectura del backend

## Proposito

Backend modular y multiempresa. Una empresa es la organizacion propietaria de los datos; no es un cliente de ventas ni un establecimiento. Actualmente cada empresa es unica y no existe una entidad de establecimientos.

## Plataforma

- Java 21, Spring Boot 4.0.7 y Spring Web MVC.
- Spring Security, JWT y Bean Validation.
- Spring Data JPA e Hibernate; PostgreSQL en ejecucion y H2 en pruebas.
- ZXing para QR, Java Mail y SDK de Twilio para integraciones.

## Paquetes

- `controller`: contratos HTTP y contexto autenticado.
- `service`: reglas de agenda, catalogo, mallas, productos, servicios y ventas.
- `service.pagos`: abstraccion de pasarela y preparacion Redsys/Bizum.
- `repository`: consultas JPA acotadas por empresa.
- `entity` y `dto`: persistencia y contratos de entrada/salida.
- `security`: JWT; `core.context`: empresa y usuario de la peticion.
- `core.config`: configuracion efectiva de SMTP, WhatsApp y pagos.
- `core.security`: cifrado, proteccion de entradas y recuperacion de contrasena.
- `core.comunicaciones`: recepcion, clasificacion y envio.
- `exception`: conversion central de errores HTTP.

## Flujo autenticado

1. `JwtFilter` lee el bearer token.
2. `JwtService` valida firma y caducidad y crea `JwtUser`.
3. Spring Security rechaza rutas protegidas sin autenticacion.
4. El controlador obtiene empresa, usuario y fecha del contexto.
5. Servicios y repositorios operan solo sobre esa empresa.
6. La entidad registra auditoria y activo cuando corresponde.

## Multiempresa

`emp_id` es el limite de seguridad y negocio. Los identificadores funcionales pueden repetirse entre empresas. Toda consulta y cambio debe combinar empresa e identificador. La empresa procede del JWT, no de un valor libre del cliente. El catalogo publico la deduce de un token QR opaco.

## Ciclo de los registros

- Muchos maestros usan la clave logica `emp_id + id funcional`.
- `*_act` representa baja logica cuando existe.
- `*_usu_mov`, `*_fec_mov`, `*_tip_mov` y `*_cau_mov` auditan cambios.
- Los CRUD principales ofrecen baja, historico y deshacer.
- Catalogos auxiliares pueden borrarse fisicamente si no tienen dependencias.

## Malla

La malla es un mapa creado por el usuario, no un almacen ni local fijo. Puede representar cualquier espacio. Una celda combina entidad, fila, columna, color, ubicacion textual y referencia. El negro se usa como camino.

Productos y pedidos pueden ocupar posiciones. La posicion del pedido se conserva al generar albaran y factura. Actualmente se permiten varios pedidos en una misma posicion; no se bloquea de forma unica hasta el pago.

## Transacciones y concurrencia

Los flujos compuestos usan `@Transactional`. La numeracion documental se centraliza en `NumeradorDocumentoVentaService`. Restricciones unicas protegen tokens y posiciones publicas. No hay una estrategia general de bloqueo optimista mediante campo de version.
