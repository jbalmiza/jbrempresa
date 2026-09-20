import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {API_URL} from '../../config/api-url.config';
import {avisarAplicacion} from '../interaccion/dialogos.service';
import {ContextoSesionService} from './contexto-sesion.service';
import {tokenExpirado} from './token-expirado.util';

@Injectable({providedIn:'root'})
export class SesionActividadService {
  private iniciada=false;
  private ultimoAviso=Date.now();
  private readonly intervalo=4*60*1000;
  private readonly eventos=['pointerdown','keydown','touchstart','wheel'] as const;

  constructor(private http:HttpClient,private router:Router,private contexto:ContextoSesionService){}

  iniciar(){
    if(this.iniciada||typeof document==='undefined')return;
    this.iniciada=true;
    this.eventos.forEach(evento=>document.addEventListener(evento,this.registrar,{passive:true,capture:true}));
    document.addEventListener('visibilitychange',this.comprobarCaducidad);
    setInterval(this.comprobarCaducidad,30000);
    this.comprobarCaducidad();
  }

  private comprobarCaducidad=()=>{
    const token=localStorage.getItem('token');
    if(!token||!tokenExpirado(token))return;
    this.contexto.cerrarSesion();
    avisarAplicacion('La sesión ha caducado tras 30 minutos sin actividad. Inicia sesión de nuevo.');
    void this.router.navigate(['/accesoLogin']);
  };

  private registrar=()=>{
    this.comprobarCaducidad();
    if(!localStorage.getItem('token'))return;
    const ahora=Date.now();
    if(ahora-this.ultimoAviso<this.intervalo)return;
    this.ultimoAviso=ahora;
    this.http.get<void>(`${API_URL}/usuarios/actividad`).subscribe({error:()=>{}});
  };
}
