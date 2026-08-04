// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Domicilio.
import { Domicilio } from '../interfaces/domicilio.interface';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de domicilios.
export class DomicilioService {

  // URL del controlador.
  private apiUrl = 'http://localhost:8080/domicilio';

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda un domicilio.
  guardar(domicilio: Domicilio) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      domicilio
    );

  }

  // Obtiene los domicilios.
  obtenerDomicilios() {

    // Envía la petición.
    return this.http.get<any[]>(
      this.apiUrl
    );

  }

  // Obtiene el siguiente ID.
  obtenerSiguienteId() {

    // Envía la petición.
    return this.http.get<number>(
      `${this.apiUrl}/siguiente-id`
    );

  }

  // Elimina un domicilio.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza un domicilio.
  actualizar(domicilio: Domicilio): Observable<Domicilio> {

    // Envía la petición.
    return this.http.put<Domicilio>(
      `${this.apiUrl}/${domicilio.domId}`,
      domicilio
    );

  }

}