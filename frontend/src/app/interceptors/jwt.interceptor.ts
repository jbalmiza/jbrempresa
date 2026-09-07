import {avisarAplicacion,confirmarAplicacion} from '../core/interaccion/dialogos.service';
import {
  HttpInterceptorFn,
  HttpErrorResponse,
  HttpResponse
} from '@angular/common/http';

import { catchError } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { normalizarErrorApi } from '../core/errores/api-error';

// Interceptor JWT
export const jwtInterceptor: HttpInterceptorFn = (
  req,
  next
) => {

  // Obtiene el token almacenado
  const token = localStorage.getItem('token');

  // Si existe token añade la cabecera Authorization
  if (token) {

    req = req.clone({

      setHeaders: {

        Authorization: `Bearer ${token}`

      }

    });

  }

  // Continúa la petición
  return next(req).pipe(

    // Guarda el token renovado por el backend.
    tap((respuesta) => {

      // Comprueba que la respuesta contiene cabeceras HTTP.
      if (respuesta instanceof HttpResponse) {

        // Obtiene el token renovado.
        const tokenRenovado =
          respuesta.headers.get('X-Refresh-Token');

        // Sustituye el token actual si se ha renovado.
        if (tokenRenovado) {

          localStorage.setItem(
            'token',
            tokenRenovado
          );

        }

      }

    }),

    catchError((error: HttpErrorResponse) => {

      normalizarErrorApi(error);

      // Si el token ha expirado o no es válido
      if (error.status === 401) {

        // Elimina la sesión almacenada
        const usuarioRecordado = localStorage.getItem('login.usuarioRecordado');
        localStorage.clear();
        if (usuarioRecordado) localStorage.setItem('login.usuarioRecordado', usuarioRecordado);

        // Informa al usuario de que debe iniciar sesión de nuevo
        avisarAplicacion(
          'La sesión ha caducado tras 30 minutos de inactividad, debe iniciar sesión nuevamente.'
        );

        // Redirige al login
        window.location.href = '/accesoLogin';

      }

      return throwError(() => error);

    })

  );

};
