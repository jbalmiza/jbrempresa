// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa Routes para definir las rutas de navegación Angular
import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

// Importa la página LoginPage
import { AccesoLogin } from './pages/accesoLogin/accesoLogin';
import { AccesoModulos } from './pages/accesoModulos/accesoModulos';

import { PrincipalAdministracion } from './pages/mAdministracion/principalAdministracion/principalAdministracion';
import { PrincipalTerritorio } from './pages/mTerritorio/principalTerritorio/principalTerritorio';
import { PrincipalPersonas } from './pages/mPersonas/principalPersonas/principalPersonas';
import { PrincipalProductos } from './pages/mProductos/principalProductos/principalProductos';
import { PrincipalServicios } from './pages/mServicios/principalServicios/principalServicios';
import { PrincipalVentas } from './pages/mVentas/principalVentas/principalVentas';
import { PrincipalCompras } from './pages/mCompras/principalCompras/principalCompras';
import { PrincipalComunicaciones } from './pages/mComunicaciones/principalComunicaciones/principalComunicaciones';
import { PrincipalRecursos } from './pages/mRecursos/principalRecursos/principalRecursos';
import { Recursos } from './pages/mRecursos/recursos/recursos';
import { GestionAgendas } from './pages/mRecursos/gestionAgendas/gestionAgendas';
import { PrincipalCaja } from './pages/mCaja/principalCaja/principalCaja';
import { Cajas } from './pages/mCaja/cajas/cajas';
import { BandejaComunicaciones } from './pages/mComunicaciones/bandejaComunicaciones/bandejaComunicaciones';
import { ContactosSinIdentificar } from './pages/mComunicaciones/contactosSinIdentificar/contactosSinIdentificar';

import { Empresas } from './pages/mAdministracion/empresas/empresas';
import { Usuarios } from './pages/mAdministracion/usuarios/usuarios';
import { Perfiles } from './pages/mAdministracion/perfiles/perfiles';
import { Parametros } from './pages/mAdministracion/parametros/parametros';
import { AreasOrganizativas } from './pages/mAdministracion/areasOrganizativas/areasOrganizativas';
import { PersonalAreaPage } from './pages/mAdministracion/personalArea/personalArea';
import { Domicilios } from './pages/mTerritorio/domicilios/domicilios';
import { Personas } from './pages/mPersonas/personas/personas';
import { ComplementosPersona } from './pages/mPersonas/complementosPersona/complementosPersona';
import { Productos } from './pages/mProductos/productos/productos';
import { Servicios } from './pages/mServicios/servicios/servicios';
import { Compras } from './pages/mCompras/compras/compras';
import { DocumentosVenta } from './pages/mVentas/documentosVenta/documentosVenta';
import { VentaTactil } from './pages/mVentas/ventaTactil/ventaTactil';

import { GestionDomicilios } from './pages/mTerritorio/gestionDomicilios/gestionDomicilios';
import { GestionProductos } from './pages/mProductos/gestionProductos/gestionProductos';
import { CatalogoTerritorialPage } from './pages/mTerritorio/catalogoTerritorial/catalogoTerritorial';
import { CatalogoGestion } from './pages/mProductos/catalogoGestion/catalogoGestion';
import { TiposArticulo } from './pages/tiposArticulo/tiposArticulo';
import { CatalogoPublico } from './pages/catalogoPublico/catalogoPublico';

// Definición de la lógica del componente / Define las rutas de la aplicación
export const routes: Routes = [

  { path: 'catalogo/:token', component: CatalogoPublico, title: 'Catálogo' },

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
	canActivate: [authGuard],
	title: 'GreenSaas'
  },
  
  {
	// Significa: localhost:4200/dashboard
    path: 'administracion',
	
	// Significa: muestra DashboardPage
    component: PrincipalAdministracion,
	canActivate: [authGuard],
	title: 'Administración'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'territorio',

  // Significa: muestra DashboardPage
    component: PrincipalTerritorio,
	canActivate: [authGuard],
	title: 'Territorio'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'personas',

  // Significa: muestra DashboardPage
    component: PrincipalPersonas,
	canActivate: [authGuard],
	title: 'Personas'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'productos',

  // Significa: muestra DashboardPage
    component: PrincipalProductos,
	canActivate: [authGuard],
	title: 'Productos'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'ventas',

  // Significa: muestra DashboardPage
    component: PrincipalVentas,
	canActivate: [authGuard],
	title: 'Ventas'
  },
  
  {
  // Significa: localhost:4200/dashboard
    path: 'compras',
  
  // Significa: muestra DashboardPage
    component: PrincipalCompras,
	canActivate: [authGuard],
  title: 'Compras'
  },
  
  {
    path: 'administracion/empresas',
    component: Empresas,
	canActivate: [authGuard],
	title: 'Empresas'
  },
  
  {
    path: 'administracion/usuarios',
    component: Usuarios,
	canActivate: [authGuard],
	title: 'Usuarios'
  },
  
  {
    path: 'administracion/perfiles',
    component: Perfiles,
	canActivate: [authGuard],
	title: 'Perfiles'
  },

  { path: 'servicios', component: PrincipalServicios, canActivate: [authGuard], title: 'Servicios' },

  {
    path: 'comunicaciones',
    component: PrincipalComunicaciones,
    canActivate: [authGuard],
    title: 'Comunicaciones'
  },

  { path: 'comunicaciones/bandeja', component: BandejaComunicaciones, canActivate: [authGuard], data: { filtro: 'bandeja' }, title: 'Bandeja de Entrada' },
  { path: 'comunicaciones/conversaciones', component: BandejaComunicaciones, canActivate: [authGuard], data: { filtro: 'conversaciones' }, title: 'Conversaciones' },
  { path: 'comunicaciones/pendientes', component: BandejaComunicaciones, canActivate: [authGuard], data: { filtro: 'pendientes' }, title: 'Pendientes de Revisión' },
  { path: 'comunicaciones/contactosSinIdentificar', component: ContactosSinIdentificar, canActivate: [authGuard], title: 'Contactos de Canal' },

  {
    path: 'administracion/parametros',
    component: Parametros,
	canActivate: [authGuard],
	title: 'Parámetros'
  },

  {
    path: 'administracion/areasOrganizativas',
    component: AreasOrganizativas,
	canActivate: [authGuard],
	title: 'Áreas Organizativas'
  },

  {
    path: 'administracion/personalArea',
    component: PersonalAreaPage,
	canActivate: [authGuard],
	title: 'Personal de Área'
  },
  
  { path: 'territorio/vias', component: CatalogoTerritorialPage, canActivate: [authGuard], data: { catalogo: 'vias' }, title: 'Vías' },
  { path: 'territorio/codigosPostales', component: CatalogoTerritorialPage, canActivate: [authGuard], data: { catalogo: 'codigos-postales' }, title: 'Códigos Postales' },
  { path: 'territorio/municipios', component: CatalogoTerritorialPage, canActivate: [authGuard], data: { catalogo: 'municipios' }, title: 'Municipios' },
  { path: 'territorio/provincias', component: CatalogoTerritorialPage, canActivate: [authGuard], data: { catalogo: 'provincias' }, title: 'Provincias' },
  { path: 'territorio/paises', component: CatalogoTerritorialPage, canActivate: [authGuard], data: { catalogo: 'paises' }, title: 'Países' },

  { path: 'comunicaciones/parametros', component: Parametros, canActivate: [authGuard], data: { moduloParametros: 'COMUNICACIONES' }, title: 'Parámetros de Comunicaciones' },

  {
    path: 'territorio/parametros',
    component: Parametros,
	canActivate: [authGuard],
	data: { moduloParametros: 'TERRITORIO' },
	title: 'Parámetros de Territorio'
  },

  {
    path: 'personas/parametros',
    component: Parametros,
	canActivate: [authGuard],
	data: { moduloParametros: 'PERSONAS' },
	title: 'Parámetros de Personas'
  },

  {
    path: 'productos/parametros',
    component: Parametros,
	canActivate: [authGuard],
	data: { moduloParametros: 'PRODUCTOS' },
	title: 'Parámetros de Productos'
  },

  { path: 'servicios/parametros', component: Parametros, canActivate: [authGuard], data: { moduloParametros: 'SERVICIOS' }, title: 'Parámetros de Servicios' },

  {
    path: 'ventas/parametros',
    component: Parametros,
	canActivate: [authGuard],
	data: { moduloParametros: 'VENTAS' },
	title: 'Parámetros de Ventas'
  },

  {
    path: 'compras/parametros',
    component: Parametros,
	canActivate: [authGuard],
	data: { moduloParametros: 'COMPRAS' },
	title: 'Parámetros de Compras'
  },

  {
    path: 'territorio/domicilios',
    component: Domicilios,
	canActivate: [authGuard],
	title: 'Domicilios'
  },
  
  {
    path: 'territorio/gestionDomicilios',
    component: GestionDomicilios,
	canActivate: [authGuard],
  	title: 'Gestion de Domicilios'
  },
  
  {
    path: 'personas/personas',
    component: Personas,
	canActivate: [authGuard],
	title: 'Personas'
  },

  {
    path: 'personas/gestionPersonas',
    component: Personas,
	canActivate: [authGuard],
	data: { modoGestion: true },
	title: 'Gestión de Personas'
  },

  { path: 'personas/representantes', component: ComplementosPersona, canActivate: [authGuard], data: { tipo: 'representantes' }, title: 'Representantes' },
  { path: 'personas/domiciliosNotificacion', component: ComplementosPersona, canActivate: [authGuard], data: { tipo: 'domicilios-notificacion' }, title: 'Domicilios de Notificación' },
  { path: 'personas/domiciliacionesBancarias', component: ComplementosPersona, canActivate: [authGuard], data: { tipo: 'domiciliaciones-bancarias' }, title: 'Domiciliaciones Bancarias' },
  
  {
    path: 'productos/productos',
    component: Productos,
	canActivate: [authGuard],
	title: 'Productos'
  },

  { path: 'servicios/servicios', component: Servicios, canActivate: [authGuard], title: 'Servicios' },
  { path: 'servicios/tiposServicio', component: TiposArticulo, canActivate: [authGuard], data: { clase: 'SERVICIO' }, title: 'Tipos de Servicio' },
  { path: 'servicios/gestionServicios', component: Servicios, canActivate: [authGuard], data: { modoGestion: true }, title: 'Gestión de Servicios' },
  { path: 'servicios/catalogo', component: CatalogoGestion, canActivate: [authGuard], data: { moduloOrigen: 'servicios' }, title: 'Gestión de Catálogo' },
  
  {
    path: 'productos/gestionProductos',
    component: GestionProductos,
	canActivate: [authGuard],
  	title: 'Gestion de Productos'
  },
  
  {
	path: 'ventas/ventas',
	component: DocumentosVenta,
	canActivate: [authGuard],
	data: { tipo: 'FAC' },
	title: 'Facturas'
  },
  { path: 'ventas/tactil', component: VentaTactil, canActivate: [authGuard], title: 'Venta Táctil' },
  { path: 'productos/catalogo', component: CatalogoGestion, canActivate: [authGuard], data: { moduloOrigen: 'productos' }, title: 'Gestión de Catálogo' },
  { path: 'productos/tiposProducto', component: TiposArticulo, canActivate: [authGuard], data: { clase: 'PRODUCTO' }, title: 'Tipos de Producto' },

  { path: 'ventas/presupuestos', component: DocumentosVenta, canActivate: [authGuard], data: { tipo: 'PRE' }, title: 'Presupuestos' },
  { path: 'ventas/pedidos', component: DocumentosVenta, canActivate: [authGuard], data: { tipo: 'PED' }, title: 'Pedidos' },
  { path: 'ventas/albaranes', component: DocumentosVenta, canActivate: [authGuard], data: { tipo: 'ALB' }, title: 'Albaranes' },
  { path: 'ventas/gestionPresupuestos', component: DocumentosVenta, canActivate: [authGuard], data: { tipo: 'PRE', modoGestion: true }, title: 'Gestión de Presupuestos' },
  { path: 'ventas/gestionPedidos', component: DocumentosVenta, canActivate: [authGuard], data: { tipo: 'PED', modoGestion: true }, title: 'Gestión de Pedidos' },
  { path: 'ventas/gestionAlbaranes', component: DocumentosVenta, canActivate: [authGuard], data: { tipo: 'ALB', modoGestion: true }, title: 'Gestión de Albaranes' },
  { path: 'ventas/gestionFacturas', component: DocumentosVenta, canActivate: [authGuard], data: { tipo: 'FAC', modoGestion: true }, title: 'Gestión de Facturas' },
  
  {
    path: 'compras/compras',
    component: Compras,
	canActivate: [authGuard],
  	title: 'Compras'
  },
  { path: 'recursos', component: PrincipalRecursos, canActivate: [authGuard], title: 'Recursos' },
  { path: 'recursos/recursos', component: Recursos, canActivate: [authGuard], title: 'Registro de Recursos' },
  { path: 'recursos/gestionRecursos', component: Recursos, canActivate: [authGuard], data: { gestion: true }, title: 'Gestión de Recursos' },
  { path: 'recursos/gestionAgendas', component: GestionAgendas, canActivate: [authGuard], title: 'Gestión de Agendas' },
  { path: 'recursos/parametros', component: Parametros, canActivate: [authGuard], data: { moduloParametros: 'RECURSOS', rutaVolver: '/recursos' }, title: 'Parámetros de Recursos' }
  ,{ path: 'caja', component: PrincipalCaja, canActivate: [authGuard], title: 'Caja' }
  ,{ path: 'caja/cajas', component: Cajas, canActivate: [authGuard], title: 'Registro de Cajas' }
  ,{ path: 'caja/gestionCaja', component: Cajas, canActivate: [authGuard], data: { gestion: true }, title: 'Gestión de Caja' }
  ,{ path: 'caja/parametros', component: Parametros, canActivate: [authGuard], data: { moduloParametros: 'CAJA', rutaVolver: '/caja' }, title: 'Parámetros de Caja' }
  
  
  
  
];
