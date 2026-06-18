export interface Persona {

	idCliente: number | null;	
  	idPersona: number | null;
  

  	tipoDocumento: string;
  	documento: string;

  	nombre: string;
  	apellido1: string;
  	apellido2: string;
	fechaNacimiento: string;

  	telefono: string;
  	email: string;

  	idDomicilio: number;

  	activo: boolean;
  	usuarioMovimiento: string;
  	fechaMovimiento: string;

}