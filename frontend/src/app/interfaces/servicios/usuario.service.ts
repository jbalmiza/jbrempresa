// Importa el decorador Injectable de Angular.
//
// Injectable permite que esta clase pueda ser inyectada
// automáticamente en otros componentes o servicios.
import { Injectable } from '@angular/core';

// Importa HttpClient.
//
// HttpClient permite realizar peticiones HTTP
// al backend Spring Boot.
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Usuario } from '../models/usuario.interface';

// @Injectable indica que esta clase es un servicio Angular.
//
// providedIn: 'root' hace que Angular cree
// una única instancia global del servicio.
@Injectable({
  providedIn: 'root'
})

// Define la clase UsuarioService.
//
// Este servicio se encargará de comunicarse
// con el backend Spring Boot.
export class UsuarioService {

  // URL base del controlador REST de usuarios.
  //
  // Angular enviará peticiones a:
  // http://localhost:8080/usuarios
  private apiUrl = 'http://localhost:8080/usuarios';

  // Constructor del servicio.
  //
  // Angular inyecta automáticamente HttpClient.
  constructor(private http: HttpClient) {}

  // Método guardar.
  //
  // Envía un usuario al backend mediante HTTP POST.
  guardar(usuario: any) {

    // this.http.post(...)
    //
    // Realiza una petición POST al backend.
    //
    // this.apiUrl → URL destino
    // usuario → datos enviados
    return this.http.post(this.apiUrl, usuario);

  }
  
 	// Obtiene los usuarios del cliente que ha iniciado sesión.
  	obtenerUsuarios() {

      	// Obtiene el cliente guardado en el login
      	const cliente = localStorage.getItem('clienteId');

      	// Envía el cliente como parámetro
      	return this.http.get<any[]>( this.apiUrl + '?cliente=' + cliente );

  	}
  
  	// Obtiene el siguiente ID disponible desde Spring Boot.
  	obtenerSiguienteId() {

    	return this.http.get<number>( this.apiUrl + '/siguiente-id' );

  	}
  
	// Elimina el registro por id y cliente
	eliminar(id: number, cliente: number) {

	  return this.http.delete( `${this.apiUrl}/${id}?cliente=${cliente}` );

	}
  
  	// actualiza un perfil por su ID.
  	actualizar(usuario: Usuario): Observable<Usuario> {

		return this.http.put<Usuario>( `${this.apiUrl}/${usuario.usuId}`, usuario );
	  
	}
  
  // Método login.
  //
  // Comprueba usuario y contraseña.
  login(usuario: string, password: string) {

    // Objeto que se enviará al backend.
    const datosLogin = {

      // Usuario introducido
      usuUsu: usuario,

      // Contraseña introducida
      usuCon: password

    };

    // Envía una petición POST al endpoint login
    return this.http.post( this.apiUrl + '/login', datosLogin );

  }

}