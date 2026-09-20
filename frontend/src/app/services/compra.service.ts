// Importa Injectable.
import { Injectable } from '@angular/core';

// Importa HttpClient.
import { HttpClient } from '@angular/common/http';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa la interfaz Compra.
import { Compra } from '../interfaces/compra.interface';

import { API_URL } from '../config/api-url.config';

// Define el servicio.
@Injectable({
  providedIn: 'root'
})

// Gestiona las operaciones de compras.
export class CompraService {
  private entrada(compra: Compra) {
    return {...compra,
      comFecPre: compra.comFecPre || null, comFecPed: compra.comFecPed || null,
      comFecAlb: compra.comFecAlb || null, comFecFac: compra.comFecFac || null,
      comFecPag: compra.comFecPag || null,
      detalles: (compra.detalles || []).map(l => ({proId:l.proId, comDetCan:l.comDetCan, comDetPre:l.comDetPre, comDetDes:l.comDetDes, comDetIva:l.comDetIva}))
    };
  }


  // URL del controlador.
  private apiUrl = `${API_URL}/compras`;

  // Constructor.
  constructor(private http: HttpClient) {}

  // Guarda una compra.
  guardar(compra: Compra) {

    // Envía la petición.
    return this.http.post(
      this.apiUrl,
      this.entrada(compra)
    );

  }

  // Obtiene las compras.
  obtenerCompras() {

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

  // Elimina una compra.
  eliminar(id: number) {

    // Envía la petición.
    return this.http.delete(
      `${this.apiUrl}/${id}`
    );

  }

  // Actualiza una compra.
  actualizar(compra: Compra): Observable<Compra> {

    // Envía la petición.
    return this.http.put<Compra>(
      `${this.apiUrl}/${compra.comId}`,
      this.entrada(compra)
    );

  }

}
