/** Comprueba la fecha de caducidad del JWT sin confiar en ella para autorizar peticiones. */
export function tokenExpirado(token: string): boolean {
  try {
    const cuerpo = token.split('.')[1];
    if (!cuerpo) return true;
    const datos = JSON.parse(atob(cuerpo.replace(/-/g, '+').replace(/_/g, '/')));
    return !Number.isFinite(datos.exp) || datos.exp * 1000 <= Date.now();
  } catch {
    return true;
  }
}
