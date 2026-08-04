export interface Perfil {

	//Datos Identificación
	cliId: number;
  	perId: number;

	//Datos Perfil
  	perNom: string;
	perTipPer: string;
	
	//Permisos en el módulo de Administración
  	perModAdm: boolean;
	
	//Permisos en el módulo de Territorio
	perModTer: boolean;
	
	//Permisos en el módulo de Personas
	perModPer: boolean;
	
	//Permisos en el módulo de Productos
	perModPro: boolean;
	
	//Permisos en el módulo de Ventas
	perModVen: boolean;

	//Datos Movimiento
  	perUsuMov: string;
  	perFecMov: string;
	perAct: boolean;

}