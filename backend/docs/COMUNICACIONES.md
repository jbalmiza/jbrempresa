# Comunicaciones externas

Revisión: 2026-09-15.

Comunicacion es la cabecera funcional llamada Conversación; Mensaje conserva los elementos del hilo. Un mensaje puede entrar sin conversación y permanecer en Bandeja hasta su clasificación. La identidad externa pertenece a ContactoCanal; PersonaContactoCanal vincula esa identidad con una o varias Personas.

La identificación es supervisada: una coincidencia orienta la selección, no confirma por sí sola la Persona. Una relación de contacto revocada deja de orientar futuras identificaciones y conserva la evidencia histórica. Si existen varias Personas, el usuario selecciona la que interviene. Las conversaciones permiten responsable Usuario y Área.

La Bandeja permite identificar, clasificar y vincular a una conversación. Los mensajes tienen Adjuntos. Meta y Twilio disponen de recepción y envío de texto; SIMULADO permite recorrer el proceso sin servicios externos. Las propuestas de respuesta no equivalen a un agente de IA integrado.

Los webhooks validan su firma e identificador externo para evitar entradas duplicadas. La existencia del adaptador no certifica la entrega con una cuenta real. Consultar [integraciones](INTEGRACIONES.md), [API](API.md) e [inventario de endpoints](API_INVENTARIO.md).

Este dominio se distingue de [mensajería interna](MENSAJERIA_INTERNA.md), que utiliza conversaciones y participantes propios para Jefes, Empleados y Administrador. También se distingue del maestro [Avisos y Alertas](AVISOS_ALERTAS.md) publicado en el catálogo.
