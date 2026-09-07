// URL base del backend para el entorno de desarrollo.
// Al desplegar en otro entorno, se modifica este único valor o se sustituye por su configuración de compilación.
const ubicacion = globalThis.location;
export const API_URL = `${ubicacion?.protocol || 'http:'}//${ubicacion?.hostname || 'localhost'}:8080`;
