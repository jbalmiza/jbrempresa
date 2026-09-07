// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Empresa.
import { Empresa } from '../interfaces/empresa.interface';

import { API_URL } from '../config/api-url.config';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de empresas.
export class EmpresaService {

  // URL del controlador.
  private apiUrl = `${API_URL}/empresas`;

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda un empresa.
  guardar(empresa: Empresa) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      empresa
    );

  }

  // Obtiene los empresas.
  obtenerEmpresas() {

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

  // Elimina un empresa.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza un empresa.
  actualizar(empresa: Empresa): Observable<Empresa> {

    // Envía la petición.
    return this.http.put<Empresa>(
      `${this.apiUrl}/${empresa.empId}`,
      empresa
    );

  }

  subirImagen(id: number, archivo: File): Observable<Empresa> {
    const datos = new FormData(); datos.append('archivo', archivo);
    return this.http.post<Empresa>(`${this.apiUrl}/${id}/imagen`, datos);
  }

  obtenerImagen(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/imagen`, { responseType: 'blob' });
  }

}
