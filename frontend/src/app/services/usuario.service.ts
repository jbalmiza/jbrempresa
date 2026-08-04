// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Usuario.
import { Usuario } from '../interfaces/usuario.interface';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de usuarios.
export class UsuarioService {

  // URL del controlador.
  private apiUrl = 'http://localhost:8080/usuarios';

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda un usuario.
  guardar(usuario: Usuario) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      usuario
    );

  }

  // Obtiene los usuarios.
  obtenerUsuarios() {

    // Envía la petición.
    return this.http.get<Usuario[]>(
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

  // Elimina un usuario.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza un usuario.
  actualizar(usuario: Usuario): Observable<Usuario> {

    // Envía la petición.
    return this.http.put<Usuario>(
      `${this.apiUrl}/${usuario.usuId}`,
      usuario
    );

  }

  // Comprueba el acceso.
  login(usuario: string, password: string) {

    // Crea los datos del acceso.
    const datosLogin = {

      usuUsu: usuario,
      usuCon: password

    };

    // Envía la petición.
    return this.http.post(
      `${this.apiUrl}/login`,
      datosLogin
    );

  }

}