// Importa el decorador Injectable de Angular.
//
// Injectable permite que este servicio pueda
// ser utilizado e inyectado en otros componentes.
import { Injectable } from '@angular/core';

// Importa HttpClient.
//
// HttpClient permite realizar peticiones HTTP
// al backend Spring Boot.
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Venta } from '../models/venta.interface';

// @Injectable define esta clase como un servicio Angular.
//
// providedIn: 'root' crea una única instancia global
// disponible en toda la aplicación.
@Injectable({
  providedIn: 'root'
})

// Define la clase PersonaService.
//
// Este servicio gestionará las peticiones HTTP
// relacionadas con personas.
export class VentaService {

  // URL base del controlador REST de personas.
  //
  // Angular enviará peticiones a:
  // http://localhost:8080/personas
  private apiUrl = 'http://localhost:8080/ventas';

  // Constructor del servicio.
  //
  // Angular inyecta automáticamente HttpClient.
  constructor(private http: HttpClient) {}

  // Método guardar.
  //
  // Envía una persona al backend mediante HTTP POST.
  guardar(venta: any) {

    // this.http.post(...)
    //
    // Realiza una petición POST al backend.
    //
    // this.apiUrl → URL destino
    // persona → datos enviados
    return this.http.post(this.apiUrl, venta);

  }
  
  // Método obtenerVentas.
  //
  // Obtiene las ventas del cliente que ha iniciado sesión.
  obtenerVentas() {

      // Obtiene el cliente guardado en el login
      const cliente = localStorage.getItem('usuarioCliente');

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
	actualizar(venta: Venta): Observable<Venta> {

		return this.http.put<Venta>( `${this.apiUrl}/${venta.venId}`, venta );

	}
	
}


