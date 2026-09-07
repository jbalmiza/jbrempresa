import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {API_URL} from '../config/api-url.config';
export interface TipoArticulo { empId?:number; id:number|null; clase:string; nombre:string; imagen:string|null; usuario?:string; fecha?:string; activo:boolean; }
@Injectable({providedIn:'root'})
export class TipoArticuloService {
 private url=`${API_URL}/tipos-articulo`;
 constructor(private http:HttpClient){}
 listar(c:string){return this.http.get<TipoArticulo[]>(`${this.url}/${c}`)}
 guardar(c:string,t:TipoArticulo){return this.http.post<TipoArticulo>(`${this.url}/${c}`,t)}
 eliminar(c:string,id:number){return this.http.delete<void>(`${this.url}/${c}/${id}`)}
}
