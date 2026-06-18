export interface Producto {

	idCliente: number | null;	
  	idProducto: number | null;

  	tipoProducto: string;
  	nombre: string;
 
  	activo: boolean;
  	usuarioMovimiento: string;
  	fechaMovimiento: string;

}