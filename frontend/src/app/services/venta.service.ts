// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Venta.
import { Venta } from '../interfaces/venta.interface';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de ventas.
export class VentaService {

  // URL del controlador.
  private apiUrl = 'http://localhost:8080/ventas';

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda una venta.
  guardar(venta: Venta) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      venta
    );

  }

  // Obtiene las ventas.
  obtenerVentas() {

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

  // Elimina una venta.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza una venta.
  actualizar(venta: Venta): Observable<Venta> {

    // Envía la petición.
    return this.http.put<Venta>(
      `${this.apiUrl}/${venta.venId}`,
      venta
    );

  }

}