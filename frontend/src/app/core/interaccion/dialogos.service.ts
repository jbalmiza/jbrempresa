import { Injectable, signal } from '@angular/core';
import { mensajeErrorApi } from '../errores/api-error';
let instanciaDialogos:DialogosService|null=null;
export function avisarAplicacion(mensaje:any):void{instanciaDialogos?.avisar(String(mensaje??''),/error|no se pudo|incumple|inválid|restricción/i.test(String(mensaje))?'error':'info');}
export function confirmarAplicacion(mensaje:string,destructiva=false):Promise<boolean>{return instanciaDialogos?.confirmar(mensaje,destructiva)??Promise.resolve(false);}

/** Punto único de interacción para avisos y confirmaciones de la aplicación. */
@Injectable({ providedIn: 'root' })
export class DialogosService {
  constructor(){instanciaDialogos=this;}
  readonly avisos=signal<{id:number;mensaje:string;tipo:'info'|'exito'|'error';persistente:boolean}[]>([]);
  readonly confirmacion=signal<{mensaje:string;destructiva:boolean;resolver:(valor:boolean)=>void}|null>(null);
  readonly solicitudTexto=signal<{titulo:string;mensaje:string;tipo:'text'|'password';resolver:(valor:string|null)=>void}|null>(null);
  private secuencia=0;
  avisar(mensaje:string,tipo:'info'|'exito'|'error'='info',persistente=tipo==='error'):void{const aviso={id:++this.secuencia,mensaje,tipo,persistente};this.avisos.update(a=>[...a,aviso]);if(!persistente)setTimeout(()=>this.cerrarAviso(aviso.id),4000);}
  exito(mensaje:string):void{this.avisar(mensaje,'exito');}
  confirmar(mensaje:string,destructiva=false):Promise<boolean>{return new Promise(resolver=>this.confirmacion.set({mensaje,destructiva,resolver}));}
  responder(valor:boolean):void{const c=this.confirmacion();if(!c)return;this.confirmacion.set(null);c.resolver(valor);}
  solicitarTexto(mensaje:string,titulo='Introduzca el valor',tipo:'text'|'password'='text'):Promise<string|null>{return new Promise(resolver=>this.solicitudTexto.set({titulo,mensaje,tipo,resolver}));}
  responderTexto(valor:string|null):void{const s=this.solicitudTexto();if(!s)return;this.solicitudTexto.set(null);s.resolver(valor);}
  cerrarAviso(id:number):void{this.avisos.update(a=>a.filter(x=>x.id!==id));}
  error(error: unknown, mensajePredeterminado = 'No se pudo completar la operación.'): void {
    console.error(error);
    this.avisar(mensajeErrorApi(error, mensajePredeterminado));
  }
}
