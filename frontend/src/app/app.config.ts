import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import {
  provideHttpClient,
  withInterceptors
} from '@angular/common/http';

import { routes } from './app.routes';

// Importa el interceptor JWT
import { jwtInterceptor } from './interceptors/jwt.interceptor';

export const appConfig: ApplicationConfig = {

  providers: [

    // Actualiza la interfaz cuando finalizan peticiones HTTP y otras tareas
    // asíncronas. zone.js ya se carga desde main.ts.
    provideZoneChangeDetection(),

    provideRouter(routes),

    provideHttpClient(
      withInterceptors([
        jwtInterceptor
      ])
    )

  ]

};
