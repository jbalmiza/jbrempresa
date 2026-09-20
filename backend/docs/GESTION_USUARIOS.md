# Gestión de usuarios

Revisión documental: 2026-09-15. Describe el árbol de trabajo actual.

Registro de Usuarios conserva exclusivamente el CRUD del maestro. Gestión de Usuarios incorpora las operaciones de baja lógica, reactivación e histórico.

Las bajas y reactivaciones requieren una causa, actualizan `usu_act`, `usu_tip_mov`, `usu_cau_mov`, usuario y fecha, y generan una fila inmutable en `usuarios_movimientos`. Un usuario no puede darse de baja a sí mismo y un usuario inactivo no puede iniciar sesión.

La consulta ordinaria muestra usuarios activos. Para localizar una baja debe informarse un filtro en la columna Activo. La migración `20260909_gestion_usuarios.sql` incorpora los campos de movimiento y la tabla histórica.
