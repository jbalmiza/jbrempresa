import {HttpClient} from '@angular/common/http';import {Injectable} from '@angular/core';import {API_URL} from '../../config/api-url.config';
@Injectable({providedIn:'root'}) export class SesionActividadService{
 private iniciada=false;private ultimoAviso=Date.now();private readonly intervalo=4*60*1000;private readonly eventos=['pointerdown','keydown','touchstart','wheel'] as const;
 constructor(private http:HttpClient){}
 iniciar(){if(this.iniciada||typeof document==='undefined')return;this.iniciada=true;this.eventos.forEach(evento=>document.addEventListener(evento,this.registrar,{passive:true,capture:true}))}
 private registrar=()=>{if(!localStorage.getItem('token'))return;const ahora=Date.now();if(ahora-this.ultimoAviso<this.intervalo)return;this.ultimoAviso=ahora;this.http.get<void>(`${API_URL}/usuarios/actividad`).subscribe({error:()=>{}})};
}
