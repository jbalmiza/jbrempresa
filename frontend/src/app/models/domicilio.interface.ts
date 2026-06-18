export interface Domicilio {

	idCliente: number | null;	
  	idDomicilio: number | null;

  	tipoVia: string;
  	via: string;
  	numero: string;
  
  	km: string;
  	edificio: string;
  	bloque: string;
  	portal: string;
  	escalera: string;
  	planta: string;
  	puerta: string;

  	observaciones: string;
  	direccion: string;

  	activo: boolean;
  
  	usuarioMovimiento: string;
  	fechaMovimiento: string;

}