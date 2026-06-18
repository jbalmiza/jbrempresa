// 3º El intermediario entre pantalla y backend (Framework Angular / Lenguaje TypeScript)

// Importa libreria que crea el servicio y lo comparte entre componentes.
import { Injectable } from '@angular/core';

// Importa libreria (mensajero) para hacer peticiones http al backend (get/postput/delete).
import { HttpClient } from '@angular/common/http';

// Importa libreria para esperar la respuesta tras una petición al backend.
import { Observable } from 'rxjs';

// Angular establece que este servicio existe en toda la aplicación.
@Injectable({
	
// Angular establece que root crea el servicio y lo comparte entre componentes.
  providedIn: 'root'
})

// Angular permite usar la clase desde otros archivos
export class ApiService {

// Angular guarda la URL base del backend, así evitas repetir 'http://localhost:8080/api'
  private apiUrl = 'http://localhost:8080/api';

// Angular crea e inyecta automáticamente HttpClient
  constructor(private http: HttpClient) {}

// Angular define el método getHello, y observa y espera un string.
  getHello(): Observable<string> {

// Angular Se retorna al navegador lo que devuelve una petición http get que obtiene datos.
    return this.http.get(
		
// Angular contruye la url completa para la llamada al método hello.
      `${this.apiUrl}/hello`,
	  
// Angular interpreta la respuesta esperada como texto y no como JSON por defecto.
      { responseType: 'text' }
    );

  }
}