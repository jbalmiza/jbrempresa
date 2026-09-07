import { Injectable } from '@angular/core';import { HttpClient,HttpParams } from '@angular/common/http';import { API_URL } from '../config/api-url.config';import { ExcepcionAgenda,HorarioAgenda,RecursoAgenda,RecursoAgendaEntrada,ReprogramacionAgenda,ReservaAgenda,ReservaAgendaEntrada,EstadoReserva } from '../interfaces/agenda.interface';
@Injectable({providedIn:'root'})export class AgendaService{private url=`${API_URL}/agenda`;constructor(private http:HttpClient){}
 recursos(){return this.http.get<RecursoAgenda[]>(`${this.url}/recursos`)}
 guardarRecurso(v:RecursoAgendaEntrada){return this.http.post<RecursoAgenda>(`${this.url}/recursos`,v)}
 horarios(id:number){return this.http.get<HorarioAgenda[]>(`${this.url}/recursos/${id}/horarios`)}
 guardarHorarios(id:number,v:HorarioAgenda[]){return this.http.put<HorarioAgenda[]>(`${this.url}/recursos/${id}/horarios`,v)}
 excepciones(id:number,desde:string,hasta:string){return this.http.get<ExcepcionAgenda[]>(`${this.url}/recursos/${id}/excepciones`,{params:new HttpParams().set('desde',desde).set('hasta',hasta)})}
 guardarExcepcion(id:number,v:ExcepcionAgenda){return this.http.post<ExcepcionAgenda>(`${this.url}/recursos/${id}/excepciones`,v)}
 eliminarExcepcion(id:number){return this.http.delete<void>(`${this.url}/excepciones/${id}`)}
 reservas(desde:string,hasta:string){return this.http.get<ReservaAgenda[]>(`${this.url}/reservas`,{params:new HttpParams().set('desde',desde).set('hasta',hasta)})}
 reservar(v:ReservaAgendaEntrada){return this.http.post<ReservaAgenda>(`${this.url}/reservas`,v)}
 reprogramar(id:number,resIni:string,resFin:string,motivo:string){return this.http.post<ReservaAgenda>(`${this.url}/reservas/${id}/reprogramar`,{resIni,resFin,motivo})}
 estado(id:number,estado:EstadoReserva){return this.http.put<ReservaAgenda>(`${this.url}/reservas/${id}/estado`,{estado})}
 historial(id:number){return this.http.get<ReprogramacionAgenda[]>(`${this.url}/reservas/${id}/reprogramaciones`)}
}
