# Organización y núcleo común

Revisión: 2026-09-15.

Las áreas organizativas tienen un área superior opcional. Personal de Área vincula Personas con áreas, admite varias asignaciones y una principal activa, responsable, cargo y periodo. La identidad de negocio pertenece a Persona; no se sustituye por Usuario.

Usuario utiliza usuPerId para Persona; perId identifica el Perfil en esa entidad. Las reglas de pertenencia y unicidad se consultan en los servicios: el Administrador global y la selección de empresa requieren distinguir identidad del usuario y contexto de operación. No se presupone que toda consulta esté limitada a la empresa del JWT.

ContextoOperacion centraliza usuario, empresa efectiva y fecha. La recuperación de contraseña, SMTP y cifrado tienen servicios comunes. Las pantallas reutilizan componentes compartidos; la lógica de dominio permanece en su servicio. No existe un motor universal de CRUD o históricos que permita asumir que todos los maestros tienen las mismas operaciones.

Referencias: [arquitectura](ARQUITECTURA.md), [seguridad](SEGURIDAD.md), [usuarios](GESTION_USUARIOS.md), [inventario de entidades](MODELO_INVENTARIO.md) y [directivas](DIRECTIVAS.md).
