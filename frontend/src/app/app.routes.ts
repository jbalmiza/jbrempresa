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
import { PrincipalCompras } from './pages/mCompras/principalCompras/principalCompras';

import { Clientes } from './pages/mAdministracion/clientes/clientes';
import { Usuarios } from './pages/mAdministracion/usuarios/usuarios';
import { Perfiles } from './pages/mAdministracion/perfiles/perfiles';
import { Domicilios } from './pages/mTerritorio/domicilios/domicilios';
import { Personas } from './pages/mPersonas/personas/personas';
import { Productos } from './pages/mProductos/productos/productos';
import { Ventas } from './pages/mVentas/ventas/ventas';
import { Compras } from './pages/mCompras/compras/compras';

import { GestionProductos } from './pages/mProductos/gestionProductos/gestionProductos';

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
    component: AccesoLogin,
	title: 'Login'
  },
  
  {
	// Significa: localhost:4200/modules
    path: 'accesoModulos',
	
	// Significa: muestra modules
    component: AccesoModulos,
	title: 'GreenSaas'
  },
  
  {
	// Significa: localhost:4200/dashboard
    path: 'administracion',
	
	// Significa: muestra DashboardPage
    component: PrincipalAdministracion,
	title: 'Administración'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'territorio',

  // Significa: muestra DashboardPage
    component: PrincipalTerritorio,
	title: 'Territorio'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'personas',

  // Significa: muestra DashboardPage
    component: PrincipalPersonas,
	title: 'Personas'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'productos',

  // Significa: muestra DashboardPage
    component: PrincipalProductos,
	title: 'Productos'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'ventas',

  // Significa: muestra DashboardPage
    component: PrincipalVentas,
	title: 'Ventas'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'compras',
  
  // Significa: muestra DashboardPage
    component: PrincipalCompras,
  title: 'Compras'
  },
  
  {
    path: 'administracion/clientes',
    component: Clientes,
	title: 'Clientes'
  },
  
  {
    path: 'administracion/usuarios',
    component: Usuarios,
	title: 'Usuarios'
  },
  
  {
    path: 'administracion/perfiles',
    component: Perfiles,
	title: 'Perfiles'
  },
  
  {
    path: 'territorio/domicilios',
    component: Domicilios,
	title: 'Domicilios'
  },
  
  {
    path: 'personas/personas',
    component: Personas,
	title: 'Personas'
  },
  
  {
    path: 'productos/productos',
    component: Productos,
	title: 'Productos'
  },
  
  {
    path: 'productos/gestionProductos',
    component: GestionProductos,
  	title: 'Gestion de Productos'
  },
  
  {
    path: 'ventas/ventas',
    component: Ventas,
	title: 'Ventas'
  },
  
  {
    path: 'compras/compras',
    component: Compras,
  	title: 'Compras'
  }
  
  
  
  
];