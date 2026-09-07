# Decisión arquitectónica: Organización Interna y núcleo común

Fecha de aprobación: 5 de agosto de 2026.

## Decisiones aprobadas

- Las áreas organizativas forman una jerarquía mediante un área superior opcional.
- Una Persona puede pertenecer a varias áreas, pero solo una asignación activa puede ser principal.
- Una asignación puede identificar al responsable de un área y registrar cargo y periodo.
- Personal de Área se relaciona con Persona, nunca directamente con Usuario.
- Toda asignación exige una Persona activa del mismo cliente.
- Todo Usuario exige una Persona activa del mismo cliente mediante `usuPerId`.
- `Usuario.perId` continúa representando el Perfil; no se reutiliza para Persona.
- Una Persona solo puede tener un Usuario dentro del mismo cliente.
- Los usuarios históricos sin Persona se regularizan creando Personas provisionales identificadas con documento interno.

## Núcleo común inicial

Solo se incorporan piezas con consumidores reales:

- Backend: `ContextoOperacion` centraliza cliente, usuario y fecha de la operación autenticada.
- Backend: `RecuperacionContrasenaService` centraliza el flujo público y seguro de recuperación de credenciales.
- Backend: `ConfiguracionSmtpEmpresaService` resuelve y valida el transporte SMTP de cada cliente; la contraseña se protege mediante `CifradoDatosSensibles`.
- Frontend: `ContextoSesionService` centraliza los datos de sesión almacenados en el navegador.
- `FechasUtil` se traslada a `shared/utils`, ya que es una utilidad de presentación y no una responsabilidad del núcleo.

No se aprueba todavía un CRUD universal, una entidad JPA base ni un motor genérico de históricos.

## Norma de evolución del núcleo

- El propietario no tendrá que indicar expresamente qué debe formar parte del `core`.
- En cada cambio se evaluará de oficio si aparecen responsabilidades transversales o duplicaciones entre módulos.
- Las capacidades con consumidores reales en varios módulos se incorporarán al núcleo común o a la capa compartida que corresponda.
- La lógica específica de negocio seguirá perteneciendo a su módulo; el `core` no se utilizará como almacén de código sin ubicación clara.
- Las extracciones que cambien contratos, persistencia, seguridad o límites funcionales se someterán primero a decisión arquitectónica. Las reorganizaciones internas compatibles podrán aplicarse directamente y quedarán documentadas.
