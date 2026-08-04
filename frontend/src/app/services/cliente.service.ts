// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Cliente.
import { Cliente } from '../interfaces/cliente.interface';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de clientes.
export class ClienteService {

  // URL del controlador.
  private apiUrl = 'http://localhost:8080/clientes';

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda un cliente.
  guardar(cliente: Cliente) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      cliente
    );

  }

  // Obtiene los clientes.
  obtenerClientes() {

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

  // Elimina un cliente.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza un cliente.
  actualizar(cliente: Cliente): Observable<Cliente> {

    // Envía la petición.
    return this.http.put<Cliente>(
      `${this.apiUrl}/${cliente.cliId}`,
      cliente
    );

  }

}