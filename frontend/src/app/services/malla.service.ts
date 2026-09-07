// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Malla.
import { Malla } from '../interfaces/malla.interface';

import { API_URL } from '../config/api-url.config';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de mallas.
export class MallaService {

  // URL del controlador.
  private apiUrl = `${API_URL}/mallas`;

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda una malla.
  guardar(malla: Malla) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      malla
    );

  }

  // Obtiene las mallas.
  obtenerMallas() {

    // Envía la petición.
    return this.http.get<Malla[]>(
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

  // Elimina una malla.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza una malla.
  actualizar(malla: Malla): Observable<Malla> {

    // Envía la petición.
    return this.http.put<Malla>(
      `${this.apiUrl}/${malla.malId}`,
      malla
    );

  }
  
  // Pinta una posición de la malla.
  pintar(malla: Malla): Observable<Malla> {

    // Envía la posición y el color al backend.
    return this.http.post<Malla>(
      `${this.apiUrl}/pintar`,
      malla
    );

  }
  
  // Borra una posición pintada de la malla.
  borrarPosicion(
    entidad: string,
    fila: number,
    columna: number
  ) {

    // Envía la petición para eliminar la posición.
    return this.http.delete(
      `${this.apiUrl}/posicion/${entidad}/${fila}/${columna}`
    );

  }

}
