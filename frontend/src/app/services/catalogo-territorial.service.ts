import { Injectable } from '@angular/core'; import { HttpClient } from '@angular/common/http'; import { API_URL } from '../config/api-url.config'; import { CatalogoTerritorial, Pais, Provincia, Municipio, CodigoPostal, Via } from '../interfaces/catalogo-territorial.interface';
@Injectable({providedIn:'root'}) export class CatalogoTerritorialService {
 private base=`${API_URL}/territorio`; constructor(private http:HttpClient){}
 consultar<T extends CatalogoTerritorial>(tipo:string){return this.http.get<T[]>(`${this.base}/${tipo}`)} guardar<T extends CatalogoTerritorial>(tipo:string,v:T){return this.http.post<T>(`${this.base}/${tipo}`,v)} actualizar<T extends CatalogoTerritorial>(tipo:string,id:number,v:T){return this.http.put<T>(`${this.base}/${tipo}/${id}`,v)} eliminar(tipo:string,id:number){return this.http.delete(`${this.base}/${tipo}/${id}`)}
 paises(){return this.consultar<Pais>('paises')} provincias(){return this.consultar<Provincia>('provincias')} municipios(){return this.consultar<Municipio>('municipios')} codigos(){return this.consultar<CodigoPostal>('codigos-postales')} vias(){return this.consultar<Via>('vias')}
}
