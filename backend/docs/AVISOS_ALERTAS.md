# Avisos y alertas

Revisión documental: 2026-09-17. Describe el árbol de trabajo actual.

`avisos_alertas` es un maestro multiempresa con histórico por empresa, aviso y versión. Admite los tipos `AVISO` y `ALERTA` y un periodo de publicación opcional. Si se informan ambas fechas, la final no puede ser anterior a la inicial.

Registro contiene consulta, alta, visualización/modificación y eliminación del maestro. Gestión permite consultar, establecer la baja, reactivar y consultar el histórico; nunca modifica ni elimina el maestro.

Cada aviso o alerta conserva `aviEmisor`, empresa y usuario emisor, `aviDestinatario`, empresa destinataria, `aviUbicacion` y, cuando procede, `aviVentana`. `PROVEEDOR` representa la relación entre empresas: un Jefe de la suministradora actúa por ella y el servidor comprueba una relación `CLIENTE` inversa activa y vigente antes de enviar a la compradora. No existe perfil de usuario Proveedor. Los avisos anteriores se asignan a emisor `JEFE`, destinatario `CLIENTE` y ubicación `CATALOGO_CLIENTE` mediante el inicializador repetible y el script `20260917_destinos_avisos_alertas.sql`.

Las ubicaciones son `CATALOGO_CLIENTE`, `CATALOGO_PROVEEDOR`, `VENTANA` y `MENSAJES`. Los dos catálogos comparten el componente, pero consultan avisos distintos: el público recibe los de clientes y el de Proveedores los del catálogo de la suministradora. Cada aviso de catálogo entrega tipo, título, mensaje y emisor. Una ventana muestra sus avisos a todos los usuarios autorizados de la empresa destinataria; el Jefe solo puede publicar en `/empleados`, pantalla inicial del módulo Empleados. El Administrador puede elegir una ventana de Registro o Gestión. Si el Administrador selecciona `EMPRESA` y deja `aviDestEmpId` nulo, el aviso se entrega en la ventana elegida de todas las empresas, incluidas las incorporadas después. Se respeta el periodo de publicación y la baja.

Los avisos de `MENSAJES` son elementos informativos independientes de las conversaciones. No admiten respuestas; aparecen en la bandeja de la barra superior y suman al contador de no leídos. `avisos_alertas_lecturas` guarda la lectura por usuario y versión, de modo que una modificación vuelve a presentarse como no leída. Empleado envía al Jefe de su empresa; Jefe al Administrador; Proveedor al Administrador o al Jefe de una compradora relacionada; Administrador al Jefe de una empresa o de todas las empresas cuando `aviDestEmpId` es nulo. Solo el Administrador global puede usar este destino general. El backend valida emisor, relación, empresa y destino, sin confiar en los valores del formulario. Solo el emisor o el Administrador puede gestionar el maestro; el Empleado consulta sus propios avisos.

API del maestro: `GET/POST /avisos-alertas`, `GET /siguiente-id`, `PUT/DELETE /{id}`, `POST /{id}/baja`, `POST /{id}/reactivar` y `GET /{id}/historico`. Entrega: `GET /avisos-alertas/bandeja`, `POST /avisos-alertas/bandeja/{empresa}/{id}/lectura` y `GET /avisos-alertas/ventana?ruta=...`.

La migración `20260908_avisos_alertas.sql` crea el maestro y registra tres ejemplos por empresa.
