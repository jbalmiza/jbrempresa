# Roadmap documental

La dirección funcional y la hoja de ruta recomendada vigentes se encuentran en `VISION_PRODUCTO.md`. Este archivo conserva el inventario técnico y no debe contradecir esa visión.

## 1. Funcionalidades existentes

- Aplicación Angular standalone con rutas para acceso, selección de módulos, administración, territorio, personas, productos, ventas y compras.
- Backend REST Spring Boot con PostgreSQL/JPA.
- Login con JWT e interceptor Angular.
- CRUD observable para clientes, usuarios, perfiles, domicilios, personas, productos, ventas, compras y mallas.
- Componentes reutilizables de tabla, selectores, mapas, mallas, navegación y exportación PDF.

## 2. Funcionalidades parcialmente implementadas

- [REVISAR] Detalle de ventas y compras: se editan estructuras de detalle en frontend; no se ha podido confirmar un flujo backend completo de persistencia.
- [REVISAR] Compras: existen página, servicio, entidad, repositorio y controlador, pero sus rutas HTTP no coinciden.
- [REVISAR] Gestión de permisos por perfil: el modelo contiene campos de permisos, pero no se observa autorización efectiva mediante authorities.
- [IMPLEMENTADO] Recuperación de contraseña por correo mediante enlace temporal y de un solo uso, con configuración SMTP independiente y cifrada por cliente en Parámetros de Administración.

## Problemas pausados

- [PAUSADO] Autorización por perfil. Los perfiles y sus campos de permisos existen, pero `JwtUser` no aporta authorities y la seguridad actual solo exige autenticación. No modificar hasta recibir una decisión específica sobre el modelo de permisos.

## 3. Pendientes de definir

_Espacio reservado para decisiones del propietario._

## 4. Decisiones futuras

_Espacio reservado para decisiones del propietario._

## Nota

Este documento no propone funcionalidades nuevas. Solo refleja evidencia presente en el código a la fecha de esta documentación.
