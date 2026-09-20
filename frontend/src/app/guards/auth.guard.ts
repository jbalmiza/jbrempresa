import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { ContextoSesionService } from '../core/session/contexto-sesion.service';
import { tokenExpirado } from '../core/session/token-expirado.util';
import { avisarAplicacion } from '../core/interaccion/dialogos.service';

// Protege las rutas que requieren una sesion iniciada.
export const authGuard: CanActivateFn = (_route, state) => {

  // Obtiene el router de Angular.
  const router = inject(Router);

  // Comprueba que existe un token de sesion.
  const token = localStorage.getItem('token');
  if (token && tokenExpirado(token)) {
    inject(ContextoSesionService).cerrarSesion();
    avisarAplicacion('La sesión ha caducado. Inicia sesión de nuevo.');
    return router.createUrlTree(['/accesoLogin']);
  }

  // Permite continuar si existe sesion.
  if (token) {
    const perfil = (localStorage.getItem('perfil') || '').trim().toUpperCase();
    const rutaActual = state.url.split('?')[0];
    const rutasExclusivasAdministrador = new Set([
      '/administracion/empresas',
      '/administracion/gestionEmpresas',
      '/administracion/perfiles',
      '/administracion/modulos',
      '/administracion/gestionModulos',
      '/administracion/mensajes',
      '/administracion/gestionMensajes'
    ]);
    if (rutasExclusivasAdministrador.has(rutaActual) && perfil !== 'ADMINISTRADOR') {
      return router.createUrlTree(['/administracion']);
    }
    const modulo = rutaActual.split('/').filter(Boolean)[0]?.toUpperCase();
    const permitido = perfil === 'ADMINISTRADOR' || perfil === 'JEFE'
      || (perfil === 'CLIENTE' && modulo === 'CLIENTES')
      || (perfil === 'EMPLEADO' && modulo === 'EMPLEADOS')
      || modulo === 'ACCESOMODULOS';
    if (!permitido) return router.createUrlTree(['/accesoModulos']);
    if (perfil === 'EMPLEADO' && rutaActual === '/empleados') return router.createUrlTree(['/empleados/agenda']);
    return true;

  }

  // Redirige al acceso si no existe sesion.
  return router.createUrlTree([
    '/accesoLogin'
  ]);

};
