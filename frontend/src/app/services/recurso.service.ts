import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '../config/api-url.config';
import { Recurso, RecursoCapacidad } from '../interfaces/recurso.interface';
@Injectable({providedIn:'root'}) export class RecursoService{
 private api=`${API_URL}/recursos`; constructor(private http:HttpClient){}
 consultar(){return this.http.get<Recurso[]>(this.api);} crear(r:Recurso){return this.http.post<Recurso>(this.api,r);}
 actualizar(r:Recurso){return this.http.put<Recurso>(`${this.api}/${r.reoId}`,r);}
 eliminar(id:number){return this.http.delete<void>(`${this.api}/${id}`);}
 baja(id:number){return this.http.post<Recurso>(`${this.api}/${id}/baja`,{});}
 historico(id:number){return this.http.get<Recurso[]>(`${this.api}/${id}/historico`);}
 deshacer(id:number){return this.http.post<Recurso>(`${this.api}/${id}/deshacer`,{});}
 operativo(id:number,activo:boolean){return this.http.patch<Recurso>(`${this.api}/${id}/operativo`,{activo});}
 capacidades(id:number){return this.http.get<RecursoCapacidad[]>(`${this.api}/${id}/capacidades`);}
 tiposCapacidad(){return this.http.get<{origen:'PRODUCTO'|'SERVICIO';tipo:string;activa:boolean}[]>(`${this.api}/tipos-capacidad`);}
 guardarCapacidades(id:number,c:any[]){return this.http.put<RecursoCapacidad[]>(`${this.api}/${id}/capacidades`,c);}
}
