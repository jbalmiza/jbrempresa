export interface Usuario {

	idCliente: number | null;
  	idUsuario: number | null;

  	usuario: string;
  	contrasena: string;

  	idPerfil: number;

  	nombre: string;
  	email: string;

  	activo: boolean;

  	usuarioMovimiento: string;
  	fechaMovimiento: string;

}