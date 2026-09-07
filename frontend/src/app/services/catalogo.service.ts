import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../config/api-url.config';

export interface CatalogoProducto { tipo:'PRODUCTO'|'SERVICIO'; id:number; nombre:string; descripcion:string; categoria:string; subcategoria:string; precio:number; agotado:boolean; imagenUrl:string; }
export interface CatalogoPublico { empresa:string; empresaImagenUrl:string; domicilioPermitido:boolean; modalidad:'EN_POSICION'|'DOMICILIO'; ubicacion:string|null; productos:CatalogoProducto[]; }
export interface CatalogoConfiguracion { publicado:boolean; permitirDomicilio:boolean; tokenGeneral:string; empresaConImagen:boolean; }
export interface CatalogoPosicion { capId:number|null; capFil:number; capCol:number; capUbi:string; capToken:string; capAct:boolean; }

@Injectable({providedIn:'root'})
export class CatalogoService {
  readonly apiUrl = `${API_URL}/catalogo`;
  constructor(private http:HttpClient){}
  publico(token:string):Observable<CatalogoPublico>{return this.http.get<CatalogoPublico>(`${this.apiUrl}/publico/${token}`);}
  pedir(token:string,pedido:any):Observable<any>{return this.http.post(`${this.apiUrl}/publico/${token}/pedidos`,pedido);}
  configuracion():Observable<CatalogoConfiguracion>{return this.http.get<CatalogoConfiguracion>(`${this.apiUrl}/gestion/configuracion`);}
  configurar(c:CatalogoConfiguracion):Observable<CatalogoConfiguracion>{return this.http.put<CatalogoConfiguracion>(`${this.apiUrl}/gestion/configuracion`,c);}
  posiciones():Observable<CatalogoPosicion[]>{return this.http.get<CatalogoPosicion[]>(`${this.apiUrl}/gestion/posiciones`);}
  guardarPosicion(p:CatalogoPosicion):Observable<CatalogoPosicion>{return this.http.post<CatalogoPosicion>(`${this.apiUrl}/gestion/posiciones`,p);}
  regenerar(id:number):Observable<CatalogoPosicion>{return this.http.post<CatalogoPosicion>(`${this.apiUrl}/gestion/posiciones/${id}/regenerar`,{});}
  imagen(url:string):string{return `${API_URL}${url}`;}
  qrUrl(p:CatalogoPosicion):string{return `${this.apiUrl}/gestion/posiciones/${p.capId}/qr?baseUrl=${encodeURIComponent(location.origin)}`;}
  qr(p:CatalogoPosicion):Observable<Blob>{return this.http.get(this.qrUrl(p),{responseType:'blob'});}
}
