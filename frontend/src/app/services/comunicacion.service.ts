import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '../config/api-url.config';
import { Comunicacion, Mensaje, ContactoCanal, PersonaContactoCanal, PropuestaRespuesta } from '../interfaces/comunicacion.interface';

@Injectable({providedIn:'root'})
export class ComunicacionService {
  private url=`${API_URL}/comunicaciones`;
  constructor(private http:HttpClient){}
  consultar(){return this.http.get<Comunicacion[]>(this.url)}
  crear(v:Partial<Comunicacion>){return this.http.post<Comunicacion>(this.url,v)}
  entrada(){return this.http.get<Mensaje[]>(`${this.url}/entrada`)}
  recibir(v:Partial<Mensaje>){return this.http.post<Mensaje>(`${this.url}/entrada`,v)}
  confirmarClasificacion(id:number,menInt:string){return this.http.patch<Mensaje>(`${this.url}/entrada/${id}/clasificacion`,{menInt})}
  candidatos(id:number){return this.http.get<any[]>(`${this.url}/entrada/${id}/candidatos`)}
  confirmarPersona(idMensaje:number,idPersona:number){return this.http.patch<Mensaje>(`${this.url}/entrada/${idMensaje}/persona/${idPersona}`,{})}
  conversacionesPersona(idPersona:number){return this.http.get<Comunicacion[]>(`${this.url}/persona/${idPersona}`)}
  crearDesdeMensaje(id:number,v:Partial<Comunicacion>){return this.http.post<Comunicacion>(`${this.url}/entrada/${id}/nueva-conversacion`,v)}
  vincular(idMensaje:number,idComunicacion:number){return this.http.post<Mensaje>(`${this.url}/entrada/${idMensaje}/vincular/${idComunicacion}`,{})}
  mensajes(id:number){return this.http.get<Mensaje[]>(`${this.url}/${id}/mensajes`)}
  agregarMensaje(id:number,v:Partial<Mensaje>){return this.http.post<Mensaje>(`${this.url}/${id}/mensajes`,v)}
  cambiarEstado(id:number,estado:string){return this.http.patch<Comunicacion>(`${this.url}/${id}/estado/${estado}`,{})}
  asignar(id:number,usuId:number|null,areId:number|null){const params:any={};if(usuId)params.usuId=usuId;if(areId)params.areId=areId;return this.http.patch<Comunicacion>(`${this.url}/${id}/asignacion`,{},{params})}
  contactos(){return this.http.get<ContactoCanal[]>(`${this.url}/contactos`)}
  contactosSinIdentificar(){return this.http.get<ContactoCanal[]>(`${this.url}/contactos/sin-identificar`)}
  relacionesContacto(idContacto:number){return this.http.get<PersonaContactoCanal[]>(`${this.url}/contactos/${idContacto}/personas`)}
  identificarContacto(idContacto:number,idPersona:number){return this.http.post<PersonaContactoCanal>(`${this.url}/contactos/${idContacto}/persona/${idPersona}`,{})}
  revocarContacto(idContacto:number,idPersona:number){return this.http.delete<void>(`${this.url}/contactos/${idContacto}/persona/${idPersona}`)}
  propuestas(id:number){return this.http.get<PropuestaRespuesta[]>(`${this.url}/${id}/propuestas-respuesta`)}
  generarPropuesta(id:number){return this.http.post<PropuestaRespuesta>(`${this.url}/${id}/propuestas-respuesta`,{})}
  aprobarPropuesta(id:number,propuestaId:number,contenido:string){return this.http.post<Mensaje>(`${this.url}/${id}/propuestas-respuesta/${propuestaId}/aprobar`,{contenido})}
  rechazarPropuesta(id:number,propuestaId:number){return this.http.post<PropuestaRespuesta>(`${this.url}/${id}/propuestas-respuesta/${propuestaId}/rechazar`,{})}
}
