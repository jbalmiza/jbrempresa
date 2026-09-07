import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SelectorBusqueda } from '../../../components/selectorBusqueda/selectorBusqueda';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { ContactoCanal } from '../../../interfaces/comunicacion.interface';
import { Persona } from '../../../interfaces/persona.interface';
import { ComunicacionService } from '../../../services/comunicacion.service';
import { PersonaService } from '../../../services/persona.service';

@Component({selector:'contactos-sin-identificar',standalone:true,imports:[CommonModule,FormsModule,Sidebar,Supbar,Tabla,SelectorBusqueda,BarraAcciones],templateUrl:'./contactosSinIdentificar.html',styleUrl:'./contactosSinIdentificar.css'})
export class ContactosSinIdentificar implements OnInit {
  datos:ContactoCanal[]=[]; personas:Persona[]=[]; seleccionado:ContactoCanal|null=null;
  personaId=0; mostrarTodos=false; relaciones:any[]=[]; relacionSeleccionada:any=null;
  columnas=['cocId','cocCan','cocIde','cocFecMov'];
  columnasRelaciones=['perNomCom','perDoc','pccTip','pccFecMov'];
  titulosColumnas:any={cocId:'Id',cocCan:'Canal',cocIde:'Identidad',cocFecMov:'Último movimiento'};
  titulosRelaciones:any={perNomCom:'Persona',perDoc:'Documento',pccTip:'Tipo',pccFecMov:'Confirmada'};

  constructor(private service:ComunicacionService,private personaService:PersonaService,private router:Router){}
  ngOnInit(){this.personaService.obtenerPersonasSelector().subscribe({next:v=>this.personas=v,error:e=>this.error(e)});this.consultar();}
  consultar(){this.seleccionado=null;this.personaId=0;this.relaciones=[];this.relacionSeleccionada=null;const p=this.mostrarTodos?this.service.contactos():this.service.contactosSinIdentificar();p.subscribe({next:v=>this.datos=v,error:e=>this.error(e)});}
  cambiarConsulta(todos:boolean){this.mostrarTodos=todos;this.consultar();}
  seleccionar(v:ContactoCanal){this.seleccionado=v;this.personaId=0;this.relacionSeleccionada=null;this.cargarRelaciones();}
  cargarRelaciones(){if(!this.seleccionado)return;this.service.relacionesContacto(this.seleccionado.cocId).subscribe({next:v=>this.relaciones=v.map(r=>{const p=this.personas.find(x=>x.perId===r.perId);return {...r,perNomCom:p?.perNomCom||'',perDoc:p?.perDoc||''};}),error:e=>this.error(e)});}
  identificar(){if(!this.seleccionado||!this.personaId)return;this.service.identificarContacto(this.seleccionado.cocId,this.personaId).subscribe({next:()=>this.mostrarTodos?this.cargarRelaciones():this.consultar(),error:e=>this.error(e)});}
  async revocar(){if(!this.seleccionado||!this.relacionSeleccionada)return;if(!await confirmarAplicacion('La identidad dejará de sugerir esta Persona en mensajes futuros. Los mensajes históricos no cambiarán.',true))return;this.service.revocarContacto(this.seleccionado.cocId,this.relacionSeleccionada.perId).subscribe({next:()=>this.consultar(),error:e=>this.error(e)});}
  volver(){this.router.navigate(['/comunicaciones']);}
  private error(e:any){console.error(e);avisarAplicacion(e?.error?.message||'No se pudo completar la operación.');}
}
