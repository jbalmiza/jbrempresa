import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api-url.config';
import { Adjunto, TipoAdjunto } from '../interfaces/adjunto.interface';

@Injectable({ providedIn: 'root' })
export class AdjuntoService {
  private readonly apiUrl = `${API_URL}/adjuntos`;
  constructor(private readonly http: HttpClient) {}

  consultar(modulo: string, tipoRegistro: string, registroId: number): Observable<Adjunto[]> {
    return this.http.get<Adjunto[]>(this.apiUrl, { params: { modulo, tipoRegistro, registroId } });
  }

  guardar(datos: {
    modulo: string;
    tipoRegistro: string;
    registroId: number;
    codigoRuta: string;
    nombre: string;
    tipo: TipoAdjunto;
    archivo: File;
  }): Observable<Adjunto> {
    const formulario = new FormData();
    formulario.append('modulo', datos.modulo);
    formulario.append('tipoRegistro', datos.tipoRegistro);
    formulario.append('registroId', String(datos.registroId));
    formulario.append('codigoRuta', datos.codigoRuta);
    formulario.append('nombre', datos.nombre);
    formulario.append('tipo', datos.tipo);
    formulario.append('archivo', datos.archivo, datos.archivo.name);
    return this.http.post<Adjunto>(this.apiUrl, formulario);
  }

  contenido(id: number, codigoRuta: string, descargar: boolean): Observable<Blob> {
    const params = new HttpParams().set('codigoRuta', codigoRuta).set('descargar', descargar);
    return this.http.get(`${this.apiUrl}/${id}/contenido`, { params, responseType: 'blob' });
  }

  eliminar(id: number, codigoRuta: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { params: { codigoRuta } });
  }

  marcarPrincipal(id: number, codigoRuta: string): Observable<Adjunto> {
    return this.http.put<Adjunto>(`${this.apiUrl}/${id}/principal`, null, { params: { codigoRuta } });
  }
}
