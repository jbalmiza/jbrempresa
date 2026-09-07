import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import {CommonModule} from '@angular/common';
import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute,Router} from '@angular/router';
import {Sidebar} from '../../../components/sidebar/sidebar';
import {Supbar} from '../../../components/supbar/supbar';
import {Tabla} from '../../../components/tabla/tabla';
import {AgendaRegistros} from '../../../components/agendaRegistros/agendaRegistros';
import {DatosPersonaRelacion} from '../../../components/datosPersonaRelacion/datosPersonaRelacion';
import {DatosIdentificacion} from '../../../components/datosIdentificacion/datosIdentificacion';
import {DatosMovimiento} from '../../../components/datosMovimiento/datosMovimiento';
import {Recurso,RecursoCapacidad} from '../../../interfaces/recurso.interface';
import {Persona} from '../../../interfaces/persona.interface';
import {RecursoService} from '../../../services/recurso.service';
import {PersonaService} from '../../../services/persona.service';
import {DialogosService} from '../../../core/interaccion/dialogos.service';

@Component({selector:'Recursos',standalone:true,imports:[CommonModule,FormsModule,Sidebar,Supbar,Tabla,AgendaRegistros,DatosPersonaRelacion,DatosIdentificacion,DatosMovimiento,BarraAcciones],templateUrl:'./recursos.html',styleUrl:'../../../styles/estiloGeneral.css'})
export class Recursos{
 gestion=false;vista:'tabla'|'registro'|'capacidades'|'agenda'|'historico'='tabla';modo:'insertar'|'modificar'|'ver'='insertar';
 datos:Recurso[]=[];datosHistorico:Recurso[]=[];personas:Persona[]=[];seleccion:Recurso|null=null;recurso=this.vacio();tipos:any[]=[];capacidades=new Set<string>();
 columnas=['empId','reoId','reoIdHis','reoTipMov','reoCauMov','reoNom','reoTip','personaNomCom','reoOpe','reoUsuMov','reoFecMov','reoAct'];
 titulos:any={empId:'Empresa',reoId:'Recurso',reoIdHis:'Id Histórico',reoTipMov:'Tipo Movimiento',reoCauMov:'Causa Movimiento',reoNom:'Nombre',reoTip:'Tipo',personaNomCom:'Persona',reoOpe:'Operativo',reoUsuMov:'Usuario Mod.',reoFecMov:'Fecha Mod.',reoAct:'Activo'};
 constructor(private api:RecursoService,private personaApi:PersonaService,private route:ActivatedRoute,private router:Router,private dialogos:DialogosService){this.gestion=!!route.snapshot.data['gestion'];personaApi.obtenerPersonasSelector().subscribe({next:x=>{this.personas=x;this.cargar();},error:e=>this.error(e)});}
 cargar(){this.vista='tabla';this.seleccion=null;this.api.consultar().subscribe({next:x=>this.datos=this.enriquecer(x),error:e=>this.error(e)});}
 insertar(){this.modo='insertar';this.recurso=this.vacio();this.vista='registro';}
 ver(){if(!this.seleccion)return;this.modo='ver';this.recurso={...this.seleccion};this.vista='registro';}
 modificar(){if(!this.seleccion||this.seleccion.reoTipMov==='B')return;this.modo='modificar';this.recurso={...this.seleccion,reoTipMov:'M',reoCauMov:'Modificación del registro'};this.vista='registro';}
 guardar(){if(!this.recurso.reoNom.trim()||(this.recurso.reoTip==='EMPLEADO'&&!this.recurso.perId)){this.dialogos.avisar('Nombre y Persona para empleados son obligatorios.');return;}const op=this.modo==='insertar'?this.api.crear(this.recurso):this.api.actualizar(this.recurso);op.subscribe({next:()=>this.cargar(),error:e=>this.error(e)});}
 async baja(){if(!this.seleccion||this.seleccion.reoTipMov==='B'||!await confirmarAplicacion('¿Desea dar de baja el recurso seleccionado? El movimiento quedará en el histórico.',true))return;this.api.baja(this.seleccion.reoId!).subscribe({next:()=>this.cargar(),error:e=>this.error(e)});}
 async eliminar(){if(!this.seleccion||!await confirmarAplicacion('¿Desea eliminar definitivamente el recurso?',true))return;this.api.eliminar(this.seleccion.reoId!).subscribe({next:()=>this.cargar(),error:e=>this.error(e)});}
 async reactivar(){if(!this.seleccion||this.seleccion.reoTipMov!=='B'||!await confirmarAplicacion('¿Desea reactivar el recurso seleccionado?'))return;this.api.deshacer(this.seleccion.reoId!).subscribe({next:()=>this.cargar(),error:e=>this.error(e)});}
 historico(){if(!this.seleccion)return;this.api.historico(this.seleccion.reoId!).subscribe({next:x=>{this.datosHistorico=this.enriquecer(x);this.vista='historico';},error:e=>this.error(e)});}
 async deshacer(){if(!this.seleccion||!await confirmarAplicacion('¿Desea deshacer el último movimiento del recurso?'))return;this.api.deshacer(this.seleccion.reoId!).subscribe({next:()=>this.cargar(),error:e=>this.error(e)});}
 cambiarOperativo(r:Recurso){this.api.operativo(r.reoId!,r.reoOpe).subscribe({next:x=>this.recurso=x,error:e=>{r.reoOpe=!r.reoOpe;this.error(e);}});}
 gestionarCapacidades(){if(!this.seleccion||this.seleccion.reoTipMov==='B')return;this.recurso={...this.seleccion};Promise.all([this.api.tiposCapacidad().toPromise(),this.api.capacidades(this.recurso.reoId!).toPromise()]).then(([t,c])=>{this.tipos=t||[];this.capacidades=new Set((c||[]).filter(x=>x.recAct).map(x=>this.clave(x.recOri,x.recTip)));this.vista='capacidades';}).catch(e=>this.error(e));}
 agenda(){if(!this.seleccion?.reoId||this.seleccion.reoTip!=='EMPLEADO'||this.seleccion.reoTipMov==='B')return;this.recurso={...this.seleccion};this.vista='agenda';}
 alternar(c:any){const k=this.clave(c.origen,c.tipo);this.capacidades.has(k)?this.capacidades.delete(k):this.capacidades.add(k);}
 guardarGestion(){const lista=this.tipos.map(c=>({origen:c.origen,tipo:c.tipo,activa:this.capacidades.has(this.clave(c.origen,c.tipo))}));this.api.guardarCapacidades(this.recurso.reoId!,lista).subscribe({next:()=>this.cargar(),error:e=>this.error(e)});}
 nombrePersona(id:number|null){return this.personas.find(x=>Number(x.perId)===Number(id))?.perNomCom?.trim()||'';}
 enriquecer(registros:Recurso[]){return registros.map(r=>({...r,personaNomCom:this.nombrePersona(r.perId)}));}
 volver(){this.router.navigate(['/recursos']);}clave(o:string,t:string){return `${o}|${t}`;}
 vacio():Recurso{return{empId:Number(localStorage.getItem('empresaId'))||0,reoId:null,reoIdHis:1,reoNom:'',reoTip:'EMPLEADO',perId:null,reoDes:'',reoTipMov:'A',reoCauMov:'Alta del registro',reoOpe:true,reoAct:true};}
 error(e:any){this.dialogos.error(e);}
}
