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

// @Injectable define esta clase como un servicio Angular.
//
// providedIn: 'root' crea una única instancia global
// disponible en toda la aplicación.
@Injectable({
  providedIn: 'root'
})

// Define la clase DomicilioService.
//
// Este servicio gestionará las peticiones HTTP
// relacionadas con domicilios.
export class DomicilioService {

  // URL base del controlador REST de domicilios.
  //
  // Angular enviará peticiones a:
  // http://localhost:8080/domicilio
  private apiUrl = 'http://localhost:8080/domicilio';

  // Constructor del servicio.
  //
  // Angular inyecta automáticamente HttpClient.
  constructor(private http: HttpClient) {}

  // Método guardar.
  //
  // Envía un domicilio al backend mediante HTTP POST.
  guardar(domicilio: any) {

    // this.http.post(...)
    //
    // Realiza una petición POST al backend.
    //
    // this.apiUrl → URL destino
    // domicilio → datos enviados
    return this.http.post(this.apiUrl, domicilio);

  }
  
  // Método obtenerDomicilios.
  //
  // Obtiene los domicilios del cliente que ha iniciado sesión.
  obtenerDomicilios() {

      // Obtiene el cliente guardado en el login
      const cliente = localStorage.getItem('usuarioCliente');

      // Envía el cliente como parámetro
      return this.http.get<any[]>(

              this.apiUrl + '?cliente=' + cliente

      );

  }
  
  // Método obtenerSiguienteId.
  //
  // Obtiene el siguiente ID disponible desde Spring Boot.
  obtenerSiguienteId() {

    return this.http.get<number>(
      this.apiUrl + '/siguiente-id'
    );

  }
  
  // Elimina un domicilio por su ID.
  eliminar(id: number) {

    return this.http.delete( `${this.apiUrl}/${id}` );

  }

}