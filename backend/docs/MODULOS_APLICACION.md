# Configuración de módulos de aplicación

Revisión documental: 2026-09-15. Describe el árbol de trabajo actual.

El maestro `modulos_aplicacion` define el código técnico, título, descripción, ruta y posición inicial de cada módulo. Los módulos conocidos se crean al arrancar el backend si todavía no existen.

`empresas_modulos` conserva por empresa la disponibilidad y el orden obligatorio para todos sus usuarios. No existe una preferencia individual de posición.

La API autenticada `/modulos-aplicacion` ofrece el CRUD del maestro, `/gestion` actualiza disponibilidad y orden de la empresa activa, y `/panel` devuelve el panel efectivo filtrado por perfil mediante AccesoPerfilService. El mantenimiento del maestro y de su configuración exige Administrador.

Las imágenes se incorporan mediante Adjuntos con módulo `ADMINISTRACION`, tipo de registro `MODULO` y parámetro `RUTA_DOCUMENTOS_MODULOS`. La imagen marcada como principal es la presentada en el panel; si no existe, el frontend usa el recurso PNG incluido con la aplicación.

La migración `20260909_modulos_aplicacion.sql` crea las tablas, carga los módulos iniciales, configura todas las empresas existentes y registra la ruta documental. En nuevas empresas, la configuración de cada módulo se crea de forma diferida al consultar su Gestión o panel.
