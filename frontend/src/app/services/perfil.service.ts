// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Perfil.
import { Perfil } from '../interfaces/perfil.interface';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de perfiles.
export class PerfilService {

  // URL del controlador.
  private apiUrl = 'http://localhost:8080/perfiles';

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda un perfil.
  guardar(perfil: Perfil) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      perfil
    );

  }

  // Obtiene los perfiles.
  obtenerPerfiles() {

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

  // Elimina un perfil.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza un perfil.
  actualizar(perfil: Perfil): Observable<Perfil> {

    // Envía la petición.
    return this.http.put<Perfil>(
      `${this.apiUrl}/${perfil.perId}`,
      perfil
    );

  }

}
