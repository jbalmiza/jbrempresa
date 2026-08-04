// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Producto.
import { Producto } from '../interfaces/producto.interface';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de productos.
export class ProductoService {

  // URL del controlador.
  private apiUrl = 'http://localhost:8080/productos';

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda un producto.
  guardar(producto: Producto) {

    // Envía la petición.
    return this.http.post(
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

}

