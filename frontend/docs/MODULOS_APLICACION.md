# Panel y mantenimiento de módulos

Revisión documental: 2026-09-18. Describe el árbol de trabajo actual.

Administración incorpora `Módulos` dentro de Registro para mantener título, descripción, código, ruta, posición inicial e imagen mediante Adjuntos. `Gestión de Módulos` se limita a ordenar los módulos y decidir cuáles están disponibles en la empresa activa.

El panel presenta únicamente los módulos disponibles para la empresa y respeta obligatoriamente el orden establecido desde Gestión de Módulos.

En pantallas de hasta 1100 px o dispositivos de puntero táctil, el acceso a un módulo se realiza en la misma pestaña. En escritorio con puntero preciso se abre otra pestaña. El botón «Módulos» no aparece dentro de un módulo cuando el usuario tiene perfil Empleado; para los demás perfiles, en navegación compacta vuelve al panel en la misma pestaña.

No existe orden personal por usuario. El backend filtra el panel por perfil y la guarda de rutas comprueba el acceso. Esto no equivale a autorización uniforme de cada operación de negocio; véase [seguridad](../../backend/docs/SEGURIDAD.md).
