import {Injectable} from '@angular/core'; import {HttpClient} from '@angular/common/http'; import {API_URL} from '../config/api-url.config'; import {ModuloAplicacion} from '../interfaces/modulo-aplicacion.interface';
@Injectable({providedIn:'root'}) export class ModuloAplicacionService{
 private url=`${API_URL}/modulos-aplicacion`; constructor(private http:HttpClient){}
 listar(){return this.http.get<ModuloAplicacion[]>(this.url)} guardar(m:ModuloAplicacion){return this.http.post<ModuloAplicacion>(this.url,m)} actualizar(m:ModuloAplicacion){return this.http.put<ModuloAplicacion>(`${this.url}/${m.id}`,m)} eliminar(id:number){return this.http.delete<void>(`${this.url}/${id}`)}
 gestion(empresaId:number){return this.http.get<ModuloAplicacion[]>(`${this.url}/gestion`,{params:{empresaId}})} guardarGestion(empresaId:number,m:ModuloAplicacion[]){return this.http.put<ModuloAplicacion[]>(`${this.url}/gestion`,m.map((x,i)=>({moduloId:x.id,disponible:x.disponible,posicion:i+1})),{params:{empresaId}})}
 panel(){return this.http.get<ModuloAplicacion[]>(`${this.url}/panel`)}
}
