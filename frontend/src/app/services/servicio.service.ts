import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '../config/api-url.config';
import { Servicio } from '../interfaces/servicio.interface';
@Injectable({providedIn:'root'})
export class ServicioService {
 private url=`${API_URL}/servicios`;
 constructor(private http:HttpClient){}
 consultar(){return this.http.get<Servicio[]>(this.url);}
 siguienteId(){return this.http.get<number>(`${this.url}/siguiente-id`);}
 guardar(s:Servicio){return this.http.post<Servicio>(this.url,s);}
 actualizar(s:Servicio){return this.http.put<Servicio>(`${this.url}/${s.serId}`,s);}
 eliminar(id:number){return this.http.delete<void>(`${this.url}/${id}`);}
 baja(id:number){return this.http.post<Servicio>(`${this.url}/${id}/baja`,{});}
 historico(id:number){return this.http.get<Servicio[]>(`${this.url}/${id}/historico`);}
 deshacer(id:number){return this.http.post<Servicio>(`${this.url}/${id}/deshacer`,{});}
 subirImagen(id:number,archivo:File){const datos=new FormData();datos.append('archivo',archivo);return this.http.post<Servicio>(`${this.url}/${id}/imagen`,datos);}
 obtenerImagen(id:number){return this.http.get(`${this.url}/${id}/imagen`,{responseType:'blob'});}
}
