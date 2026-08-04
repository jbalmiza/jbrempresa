// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Persona.
import { Persona } from '../interfaces/persona.interface';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de personas.
export class PersonaService {

  // URL del controlador.
  private apiUrl = 'http://localhost:8080/personas';

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda una persona.
  guardar(persona: Persona) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      persona
    );

  }

  // Obtiene las personas.
  obtenerPersonas() {

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

  // Elimina una persona.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza una persona.
  actualizar(persona: Persona): Observable<Persona> {

    // Envía la petición.
    return this.http.put<Persona>(
      `${this.apiUrl}/${persona.perId}`,
      persona
    );

  }

}
