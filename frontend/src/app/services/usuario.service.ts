// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Usuario.
import { Usuario } from '../interfaces/usuario.interface';

import { API_URL } from '../config/api-url.config';

export interface PaginaUsuarios {
  content: Usuario[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de usuarios.
export class UsuarioService {

  // URL del controlador.
  private apiUrl = `${API_URL}/usuarios`;

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

  // Consulta una página de usuarios con los filtros indicados.
  consultar(filtros: Record<string, string>, pagina: number, tamanio: number) {
    let params = new HttpParams().set('pagina', pagina).set('tamanio', tamanio);

    Object.entries(filtros).forEach(([campo, valor]) => {
      if (valor?.trim()) {
        params = params.set(campo, valor.trim());
      }
    });

    return this.http.get<PaginaUsuarios>(`${this.apiUrl}/consulta`, { params });

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

  solicitarRecuperacion(usuario: string, correo: string) {
    return this.http.post<{ mensaje: string; tokenDesarrollo?: string }>(
      `${API_URL}/auth/password/solicitar`,
      { usuario, correo }
    );
  }

  confirmarRecuperacion(
    token: string,
    contrasena: string,
    confirmacion: string
  ) {
    return this.http.post<void>(
      `${API_URL}/auth/password/confirmar`,
      { token, contrasena, confirmacion }
    );
  }

}
