export interface Perfil {

	//Datos Identificación
	idCliente: number | null;
  	idPerfil: number | null;

	//Datos Perfil
  	nombre: string;
	tipoPerfil: string;
	
	//Permisos en el módulo de Administración
  	mAdministracion: boolean;
	
	//Permisos en el módulo de Territorio
	mTerritorio: boolean;
	
	//Permisos en el módulo de Personas
	mPersonas: boolean;
	
	//Permisos en el módulo de Productos
	mProductos: boolean;
	
	//Permisos en el módulo de Ventas
	mVentas: boolean;

	//Datos Movimiento
  	usuarioMovimiento: string;
  	fechaMovimiento: string;
	activo: boolean;

}