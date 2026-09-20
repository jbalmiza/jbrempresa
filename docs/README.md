# Documentación de GreenSaaS / JBREmpresa

Revisión: 2026-09-15. Estado: entrada principal vigente.

## Por dónde empezar

- [Guía de uso](../frontend/docs/GUIA_USO.md): acceso, empresas, maestros, mensajes, proveedores y pedidos.
- [Arranque y operación](../backend/docs/OPERACION.md): instalación, variables, dos servidores, pruebas y diagnóstico.
- [Documentación frontend](../frontend/docs/README.md).
- [Documentación backend](../backend/docs/README.md).
- [Limitaciones conocidas](../backend/docs/LIMITACIONES.md): diferencias de implementación pendientes.
- [Visión y evolución](VISION_PRODUCTO.md): objetivos y alcance pendiente.
- [Documentos IA](IA/README.md): referencias de navegación; no mantienen reglas paralelas.
- [Manual de parámetros](MANUAL_PARAMETROS_CLIENTE.md).
- [Configuración Twilio](GUIA_CONFIGURACION_TWILIO_WHATSAPP.md).
- [Configuración Meta](comunicaciones-whatsapp-meta.md).
- [Contrato de seguridad](arquitectura-api-segura.md).

## Autoridad y mantenimiento

Las instrucciones de trabajo están en [AGENTS.md](../AGENTS.md). Las reglas funcionales permanentes se catalogan exclusivamente en [directivas frontend](../frontend/docs/DIRECTIVAS.md) y [directivas backend](../backend/docs/DIRECTIVAS.md). Los documentos temáticos describen su aplicación. Si el código incumple una directiva, se registra como limitación; no se cambia la norma para encubrirlo.

Los registros CAMBIOS y la auditoría conservan hechos de su fecha; no son instrucciones actuales. La visión distingue objetivos de funcionalidades disponibles. Las decisiones anteriores sustituidas no prevalecen sobre los catálogos vigentes.

## Comprobación documental

Desde la raíz: `node docs/verificar-documentacion.mjs`. Tras cambiar endpoints, entidades o rutas: `node docs/verificar-documentacion.mjs --generar`.

El verificador compara inventarios con fuentes, revisa enlaces locales explícitos, índices y secuencia de directivas. No prueba funcionalidades, enlaces externos, seguridad ni disponibilidad de servicios. Los inventarios son documentación del código, no introspección de la base de datos.
