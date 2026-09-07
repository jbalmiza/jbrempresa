# Convenciones Java y Spring observadas

## Estructura y nombres

[CONFIRMADO] Los paquetes son `com.jbrempresa.backend.<capa>`. Las clases son PascalCase; métodos y atributos camelCase. Las entidades usan prefijos del dominio: `pro*`, `ven*`, `com*`, `per*`, `dom*`, `mal*`, `usu*`, `cli*`.

## Entidades

[CONFIRMADO] Se emplean `@Entity`, `@Table(name = "...")`, `@Id` y `@Column(name = "...")`. La mayoría de ID usan `@GeneratedValue(strategy = GenerationType.IDENTITY)`. Las fechas `LocalDateTime` se serializan con `@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")`.

[CONFIRMADO] `Malla` también usa `@GeneratedValue(strategy = GenerationType.IDENTITY)`. Los valores de `siguiente-id` son solo orientativos para formularios y no deben usarse para persistir.

## Repositorios

[CONFIRMADO] Extienden `JpaRepository<Entidad, Long>`, declaran consultas derivadas como `findByEmpIdAndProId` y consultas JPQL para `obtenerSiguienteId()` con `COALESCE(MAX(...),0) + 1`. Se usa `Optional` para búsquedas que pueden no existir y `orElseThrow` en operaciones de actualización/borrado.

## Controladores y CRUD

[CONFIRMADO] Los controladores usan `@RestController`, `@RequestMapping`, verbos `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`. La mayoría inyecta dependencias con `@Autowired` de campo.

Patrón usual: convertir ID `0` a `null` en alta; fijar fecha de servidor; en actualización comprobar existencia, fijar ID y guardar; borrar tras localizar el registro.

## Cliente, usuario y seguridad

[CONFIRMADO] Los controladores aislados repiten un método privado `obtenerEmpresa()` que recupera el principal desde `SecurityContextHolder`, lo convierte a `JwtUser` y devuelve `getEmpresaId()`.

[CONFIRMADO] Las contraseñas de Usuario se codifican con BCrypt antes de persistirse y se validan con `PasswordEncoder.matches`. `usuCon` usa serialización JSON de solo escritura.

[CONFIRMADO] Las relaciones multiempresa se validan con métodos de repositorio que incluyen `empId`; por ejemplo, un Usuario solo puede usar un Perfil del mismo cliente.

[CONFIRMADO] Persona permite `domId` vacío, pero si se indica valida que el Domicilio pertenezca al cliente. Venta exige vendedor y comprador del mismo cliente.

[CONFIRMADO] Los identificadores de autenticación deben validarse antes de persistir. Usuario usa `existsByUsuUsu` y `existsByUsuUsuAndUsuIdNot` para garantizar la unicidad de `usuUsu`.

[CONFIRMADO] Los controladores activos fijan los campos de auditoría `*_UsuMov` desde el usuario autenticado y las fechas `*_FecMov` desde el servidor. No confiar en esos valores del cuerpo HTTP.

## Servicios

[CONFIRMADO] No hay una capa Service universal. Los controladores suelen hablar con repositorios directamente. `ProductoService` y `MallaService` contienen la lógica necesaria para sincronizar productos y mallas.

## Comentarios y formato

[CONFIRMADO] El código contiene comentarios frecuentes explicando imports, anotaciones, atributos, métodos y bloques de control. La sangría y espaciado varían entre archivos; no se detectó una herramienta de formato configurada.

## Errores y validación

[CONFIRMADO] Los controladores y servicios lanzan excepciones con mensajes en español. `GlobalExceptionHandler` los convierte en respuestas JSON `ApiError`: recursos no encontrados en 404, conflictos de posiciones ocupadas en 409 y validaciones en 400. Los errores no controlados responden 500 sin exponer detalles internos.

[CONFIRMADO] Usuarios, Personas y Productos validan en backend sus campos obligatorios antes de persistir. Ventas valida vendedor y comprador antes de persistir.

[REVISAR] La clasificación actual se basa en los mensajes de las excepciones existentes. Si se crean nuevos errores de dominio, usar mensajes claros o introducir excepciones tipadas de forma autorizada.

## Trazabilidad

- `entity/Producto.java`, `entity/Malla.java`, `entity/Empresa.java`
- `repository/ProductoRepository.java`, `repository/MallaRepository.java`
- `controller/ProductoController.java`, `controller/PersonaController.java`
- `service/ProductoService.java`, `service/MallaService.java`
