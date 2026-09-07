import { Injectable } from '@angular/core';
import { HttpClient,HttpParams } from '@angular/common/http';
import { API_URL } from '../config/api-url.config';

export interface ConfiguracionTablaUsuario { cotCla:string;cotCon:string; }

@Injectable({providedIn:'root'})
export class ConfiguracionTablaService {
  private url=`${API_URL}/configuraciones-tabla`;
  constructor(private http:HttpClient){}
  obtener(clave:string){return this.http.get<ConfiguracionTablaUsuario|null>(this.url,{params:new HttpParams().set('clave',clave)});}
  guardar(valor:ConfiguracionTablaUsuario){return this.http.put<ConfiguracionTablaUsuario>(this.url,valor);}
}
