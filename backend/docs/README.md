# Documentacion del backend

Esta carpeta describe el backend tal como esta implementado. El codigo es la referencia final; estos documentos explican su arquitectura, contratos, datos, operacion y limites.

## Indice

- [ARQUITECTURA.md](ARQUITECTURA.md): capas, contexto de empresa y convenciones.
- [MODULOS.md](MODULOS.md): alcance funcional de todos los modulos.
- [API.md](API.md): rutas REST y finalidad de cada controlador.
- [MODELO_DATOS.md](MODELO_DATOS.md): entidades, relaciones, claves y auditoria.
- [SEGURIDAD.md](SEGURIDAD.md): JWT, contrasenas, aislamiento, cifrado y CORS.
- [FICHEROS_IMAGENES.md](FICHEROS_IMAGENES.md): adjuntos, imagen principal y almacenamiento.
- [INTEGRACIONES.md](INTEGRACIONES.md): correo, WhatsApp, QR y Redsys/Bizum.
- [OPERACION.md](OPERACION.md): compilacion, pruebas, arranque y esquema.
- [CONFIGURACION.md](CONFIGURACION.md): variables y parametros funcionales.
- [CATALOGO.md](CATALOGO.md): catalogo movil, QR, pedidos y stock.
- [PLANIFICACION_FUTURA.md](PLANIFICACION_FUTURA.md): empleados, capacidades, tareas, agendas, maquinaria y tiempos.
- [CAMBIOS.md](CAMBIOS.md): registro cronologico.

## Estado actual

- Spring Boot 4.0.7 y Java 21.
- Spring Data JPA sobre PostgreSQL; H2 en pruebas.
- JWT sin sesion de servidor.
- Datos funcionales separados por `emp_id`.
- Hibernate actualiza el esquema con `ddl-auto=update`; no hay Flyway ni Liquibase.
- Solo existe una prueba automatizada de carga de contexto; la cobertura funcional es limitada.

## Norma obligatoria

Toda modificacion de Java, API, entidades, seguridad, configuracion, base de datos o pruebas debe actualizar el documento tematico y `CAMBIOS.md`. Una funcionalidad futura debe figurar como pendiente, nunca como disponible.
