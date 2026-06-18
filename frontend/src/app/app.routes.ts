// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa Routes para definir las rutas de navegación Angular
import { Routes } from '@angular/router';

// Importa la página LoginPage
import { AccesoLogin } from './pages/accesoLogin/accesoLogin';
import { AccesoModulos } from './pages/accesoModulos/accesoModulos';

import { PrincipalAdministracion } from './pages/mAdministracion/principalAdministracion/principalAdministracion';
import { PrincipalTerritorio } from './pages/mTerritorio/principalTerritorio/principalTerritorio';
import { PrincipalPersonas } from './pages/mPersonas/principalPersonas/principalPersonas';
import { PrincipalProductos } from './pages/mProductos/principalProductos/principalProductos';
import { PrincipalVentas } from './pages/mVentas/principalVentas/principalVentas';

import { Clientes } from './pages/mAdministracion/clientes/clientes';
import { Usuarios } from './pages/mAdministracion/usuarios/usuarios';
import { Perfiles } from './pages/mAdministracion/perfiles/perfiles';
import { Domicilios } from './pages/mTerritorio/domicilios/domicilios';
import { Personas } from './pages/mPersonas/personas/personas';
import { Productos } from './pages/mProductos/productos/productos';
import { Ventas } from './pages/mVentas/ventas/ventas';

// Definición de la lógica del componente / Define las rutas de la aplicación
export const routes: Routes = [

  {
    // Significa: localhost:4200
	path: '',
	
	// Significa: redirige automáticamente a /login
    redirectTo: 'accesoLogin',
	
	// Significa: la ruta debe coincidir exactamente
	pathMatch: 'full'
  },

  {
	// Significa: localhost:4200/login
    path: 'accesoLogin',
	
	// Significa: muestra LoginPage
    component: AccesoLogin
  },
  
  {
	// Significa: localhost:4200/modules
    path: 'accesoModulos',
	
	// Significa: muestra modules
    component: AccesoModulos
  },
  
  {
	// Significa: localhost:4200/dashboard
    path: 'administracion',
	
	// Significa: muestra DashboardPage
    component: PrincipalAdministracion
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'territorio',

  // Significa: muestra DashboardPage
    component: PrincipalTerritorio
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'personas',

  // Significa: muestra DashboardPage
    component: PrincipalPersonas
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'productos',

  // Significa: muestra DashboardPage
    component: PrincipalProductos
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'ventas',

  // Significa: muestra DashboardPage
    component: PrincipalVentas
  },
  
  {
    path: 'administracion/clientes',
    component: Clientes
  },
  
  {
    path: 'administracion/usuarios',
    component: Usuarios
  },
  
  {
    path: 'administracion/perfiles',
    component: Perfiles
  },
  
  {
    path: 'territorio/domicilios',
    component: Domicilios
  },
  
  {
    path: 'personas/personas',
    component: Personas
  },
  
  {
    path: 'productos/productos',
    component: Productos
  },
  
  {
    path: 'ventas/ventass',
    component: Ventas
  }
  
  
  
  
];