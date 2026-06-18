export interface Venta {

	idCliente: number | null;	
  	idVenta: number | null;

  	idPersona: number;
  	idProducto: number;
 
  	activo: boolean;
  	usuarioMovimiento: string;
  	fechaMovimiento: string;

}