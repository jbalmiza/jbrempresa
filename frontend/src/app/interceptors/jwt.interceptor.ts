import {
  HttpInterceptorFn,
  HttpErrorResponse
} from '@angular/common/http';

import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

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

    catchError((error: HttpErrorResponse) => {

      // Si el token ha expirado o no es válido
      if (error.status === 401) {

        // Elimina el token almacenado
        localStorage.removeItem('token');

        // Redirige al login
        window.location.href = '/login';

      }

      return throwError(() => error);

    })

  );

};