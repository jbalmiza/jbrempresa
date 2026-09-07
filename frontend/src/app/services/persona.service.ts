// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Persona.
import { Persona } from '../interfaces/persona.interface';

import { API_URL } from '../config/api-url.config';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de personas.
export class PersonaService {

  // URL del controlador.
  private apiUrl = `${API_URL}/personas`;

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

  // Maestro único de Personas utilizado por los selectores de todos los módulos.
  obtenerPersonasSelector() {
    return this.http.get<Persona[]>(`${this.apiUrl}/selector`);
  }

  baja(id: number): Observable<Persona> {
    return this.http.post<Persona>(`${this.apiUrl}/${id}/baja`, {});
  }

  obtenerHistorico(id: number): Observable<Persona[]> {
    return this.http.get<Persona[]>(`${this.apiUrl}/${id}/historico`);
  }

  deshacer(id: number): Observable<Persona> {
    return this.http.post<Persona>(`${this.apiUrl}/${id}/deshacer`, {});
  }

}
