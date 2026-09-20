import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '../config/api-url.config';
import { DocumentoVenta, DocumentoVentaMovimiento } from '../interfaces/documento-venta.interface';

export interface ConfiguracionDocumentosVenta {
  mostrarPresupuestos: boolean;
  mostrarAlbaranes: boolean;
  tipoFacturaAutomatica: 'NORMAL' | 'SIMPLIFICADA';
  requerirClaveModificacionCadena: boolean;
}

@Injectable({ providedIn: 'root' })
export class DocumentoVentaService {
  private url = `${API_URL}/documentos-venta`;
  constructor(private http: HttpClient) {}
  configuracion() { return this.http.get<ConfiguracionDocumentosVenta>(`${this.url}/configuracion`); }
  consultar(t: string, incluirBajas = false) { return this.http.get<DocumentoVenta[]>(`${this.url}/${t}`, { params: { incluirBajas } }); }
  guardar(t: string, d: DocumentoVenta) { return this.http.post<DocumentoVenta>(`${this.url}/${t}`, d); }
  actualizar(t: string, d: DocumentoVenta, propagarCadena = false, clave = '') { return this.http.put<DocumentoVenta>(`${this.url}/${t}/${d.dovId}`, d, {params: {propagarCadena, ...(clave ? {clave} : {})}}); }
  eliminar(t: string, id: number) { return this.http.delete<void>(`${this.url}/${t}/${id}`); }
  eliminarCompleto(t: string, id: number) { return this.http.delete<void>(`${this.url}/${t}/${id}/completo`); }
  convertir(t: string, id: number) { return this.http.post<DocumentoVenta>(`${this.url}/${t}/${id}/convertir`, {}); }
  baja(t: string, id: number) { return this.http.post<DocumentoVenta>(`${this.url}/${t}/${id}/baja`, {}); }
  reactivar(t: string, id: number) { return this.http.post<DocumentoVenta>(`${this.url}/${t}/${id}/reactivar`, {}); }
  historico(t: string, id: number) { return this.http.get<DocumentoVentaMovimiento[]>(`${this.url}/${t}/${id}/historico`); }
}
