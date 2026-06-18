import {
  HttpInterceptorFn
} from '@angular/common/http';

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
  return next(req);

};