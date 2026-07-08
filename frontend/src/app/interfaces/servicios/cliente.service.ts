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

import { Cliente } from '../models/cliente.interface';

// @Injectable indica que esta clase es un servicio Angular.
//
// providedIn: 'root' hace que Angular cree
// una única instancia global del servicio.
@Injectable({
  providedIn: 'root'
})

// Define la clase ClienteService.
//
// Este servicio se encargará de comunicarse
// con el backend Spring Boot.
export class ClienteService {

  // URL base del controlador REST de usuarios.
  //
  // Angular enviará peticiones a:
  // http://localhost:8080/clientes
  private apiUrl = 'http://localhost:8080/clientes';

  // Constructor del servicio.
  //
  // Angular inyecta automáticamente HttpClient.
  constructor(private http: HttpClient) {}

  // Método guardar.
  //
  // Envía un usuario al backend mediante HTTP POST.
  guardar(cliente: any) {

    // this.http.post(...)
    //
    // Realiza una petición POST al backend.
    //
    // this.apiUrl → URL destino
    // usuario → datos enviados
    return this.http.post(this.apiUrl, cliente);

  }
  
  // Método obtenerUsuarios.
  //
  // Obtiene todos los usuarios desde Spring Boot.
  obtenerClientes() {

  	// this.http.get(...)
  	//
  	// Realiza una petición HTTP GET.
  	//
  	// <any[]> indica que devuelve un array de objetos.
  	return this.http.get<any[]>(this.apiUrl);

  }
  
  // Obtiene el siguiente ID disponible desde Spring Boot.
  obtenerSiguienteId() {

    return this.http.get<number>( this.apiUrl + '/siguiente-id' );

  }
  
  // Elimina un perfil por su ID.
  eliminar(id: number) {

    return this.http.delete( `${this.apiUrl}/${id}` );

  }
  
  // actualiza un perfil por su ID.
  actualizar(cliente: Cliente): Observable<Cliente> {

      return this.http.put<Cliente>( `${this.apiUrl}/${cliente.cliId}`, cliente );

  }

}