import {CommonModule} from '@angular/common';
import {Component,OnDestroy,OnInit} from '@angular/core';
import {AgendaEmpleado,TareaAgendaEmpleado} from '../../../interfaces/agenda.interface';
import {AgendaService} from '../../../services/agenda.service';
import {DialogosService} from '../../../core/interaccion/dialogos.service';
import {Sidebar} from '../../../components/sidebar/sidebar';
import {Supbar} from '../../../components/supbar/supbar';
import {AgendaRegistros} from '../../../components/agendaRegistros/agendaRegistros';
import {ContextoSesionService} from '../../../core/session/contexto-sesion.service';
import {Subscription} from 'rxjs';

@Component({selector:'agenda-empleado',standalone:true,imports:[CommonModule,Sidebar,Supbar,AgendaRegistros],templateUrl:'./agendaEmpleado.html',styleUrls:['../../../styles/estiloPrincipal.css','./agendaEmpleado.css']})
export class AgendaEmpleadoPage implements OnInit,OnDestroy{
  agenda:AgendaEmpleado|null=null;cargando=false;actualizando:number|null=null;recursoAgendaId=0;private seleccion?:Subscription;
  readonly esAdministrador=(localStorage.getItem('perfil')||'').trim().toUpperCase()==='ADMINISTRADOR';
  constructor(private api:AgendaService,private dialogos:DialogosService,private contexto:ContextoSesionService){}
  ngOnInit(){if(this.esAdministrador)this.seleccion=this.contexto.empleadoAgendaId$.subscribe(id=>{this.recursoAgendaId=id;this.agenda=null;if(id)this.consultar()});else this.consultar()}
  ngOnDestroy(){this.seleccion?.unsubscribe()}
  consultar(){this.cargando=true;this.api.miAgenda(this.esAdministrador?this.recursoAgendaId||undefined:undefined).subscribe({next:r=>{this.agenda=r;this.cargando=false},error:e=>{this.cargando=false;this.dialogos.error(e,'No se pudo consultar la agenda.')}})}
  avanzar(tarea:TareaAgendaEmpleado){const estado=tarea.estado==='PENDIENTE'?'EN_CURSO':'FINALIZADO';this.actualizando=tarea.tarId;this.api.estadoTareaPropia(tarea.tarId,estado).subscribe({next:r=>{if(this.agenda)this.agenda={...this.agenda,tareas:this.agenda.tareas.map(t=>t.tarId===r.tarId?r:t)};this.actualizando=null;this.dialogos.exito(estado==='EN_CURSO'?'Tarea iniciada.':'Tarea finalizada.')},error:e=>{this.actualizando=null;this.dialogos.error(e)}})}
  async cambiarEstado(evento:{tarea:TareaAgendaEmpleado;estado:'PENDIENTE'|'EN_CURSO'|'FINALIZADO'}){if(evento.estado==='PENDIENTE'&&!await this.dialogos.confirmar('¿Desea devolver esta tarea al estado Pendiente?'))return;if(evento.estado==='PENDIENTE'){this.actualizando=evento.tarea.tarId;this.api.estadoTareaPropia(evento.tarea.tarId,'PENDIENTE').subscribe({next:()=>{this.actualizando=null;this.consultar();this.dialogos.exito('La tarea vuelve a estar pendiente.')},error:e=>{this.actualizando=null;this.dialogos.error(e)}});return;}this.avanzar(evento.tarea)}
  async marcarPagado(evento:{tarea:TareaAgendaEmpleado;pagado:boolean}){if(!evento.pagado&&!await this.dialogos.confirmar('¿Desea desmarcar el pedido como pagado?'))return;this.actualizando=evento.tarea.tarId;this.api.marcarPedidoPagado(evento.tarea.tarId,evento.pagado).subscribe({next:()=>{this.actualizando=null;this.consultar();this.dialogos.exito(evento.pagado?'Pedido marcado como pagado.':'Se ha desmarcado el pago del pedido.')},error:e=>{this.actualizando=null;this.dialogos.error(e)}})}
  textoEstado(estado:string){return estado==='EN_CURSO'?'En curso':estado.charAt(0)+estado.slice(1).toLowerCase()}
}
