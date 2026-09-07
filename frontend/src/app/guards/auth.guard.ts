import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

// Protege las rutas que requieren una sesion iniciada.
export const authGuard: CanActivateFn = () => {

  // Obtiene el router de Angular.
  const router = inject(Router);

  // Comprueba que existe un token de sesion.
  const token = localStorage.getItem('token');

  // Permite continuar si existe sesion.
  if (token) {

    return true;

  }

  // Redirige al acceso si no existe sesion.
  return router.createUrlTree([
    '/accesoLogin'
  ]);

};
