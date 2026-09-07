// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Producto.
import { Producto } from '../interfaces/producto.interface';

import { API_URL } from '../config/api-url.config';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de productos.
export class ProductoService {

  // URL del controlador.
  private apiUrl = `${API_URL}/productos`;

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda un producto.
  guardar(producto: Producto): Observable<Producto> {

    // Envía la petición.
    return this.http.post<Producto>(
      this.apiUrl,
      producto
    );

  }

  // Obtiene los productos.
  obtenerProductos() {

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

  // Elimina un producto.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza un producto.
  actualizar(producto: Producto): Observable<Producto> {

    // Envía la petición.
    return this.http.put<Producto>(
      `${this.apiUrl}/${producto.proId}`,
      producto
    );

  }

  baja(id: number): Observable<Producto> {
    return this.http.post<Producto>(`${this.apiUrl}/${id}/baja`, {});
  }

  obtenerHistorico(id: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrl}/${id}/historico`);
  }

  deshacer(id: number): Observable<Producto> {
    return this.http.post<Producto>(`${this.apiUrl}/${id}/deshacer`, {});
  }

  subirImagen(id: number, archivo: File): Observable<Producto> {
    const datos = new FormData(); datos.append('archivo', archivo);
    return this.http.post<Producto>(`${this.apiUrl}/${id}/imagen`, datos);
  }

  obtenerImagen(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/imagen`, { responseType: 'blob' });
  }

}

