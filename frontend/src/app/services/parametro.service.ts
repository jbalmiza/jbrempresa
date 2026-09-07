import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api-url.config';
import { Parametro } from '../interfaces/parametro.interface';

@Injectable({ providedIn: 'root' })
export class ParametroService {
  private readonly apiUrl = `${API_URL}/parametros`;
  constructor(private readonly http: HttpClient) {}
  consultar(modulo?: string): Observable<Parametro[]> {
    const options = modulo ? { params: { modulo } } : {};
    return this.http.get<Parametro[]>(this.apiUrl, options);
  }
  guardar(parametro: Parametro): Observable<Parametro> { return this.http.post<Parametro>(this.apiUrl, parametro); }
  actualizar(parametro: Parametro): Observable<Parametro> {
    return this.http.put<Parametro>(`${this.apiUrl}/${parametro.parId}`, parametro);
  }
  eliminar(id: number): Observable<void> { return this.http.delete<void>(`${this.apiUrl}/${id}`); }
}
