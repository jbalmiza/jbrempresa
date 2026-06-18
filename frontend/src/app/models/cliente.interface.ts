export interface Cliente {

	//Datos Identificación
	idCliente: number | null;

	//Datos Cliente
  	nombre: string;

	//Datos Movimiento
  	usuarioMovimiento: string;
  	fechaMovimiento: string;
	activo: boolean;

}