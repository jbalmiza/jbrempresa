import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { DocumentacionAdjunta } from '../../../components/documentacionAdjunta/documentacionAdjunta';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { Servicio } from '../../../interfaces/servicio.interface';
import { ServicioService } from '../../../services/servicio.service';
import { PdfService } from '../../../services/pdf.service';
import { FechasUtil } from '../../../shared/utils/fechas.util';
import { DialogosService } from '../../../core/interaccion/dialogos.service';
import { TipoArticulo, TipoArticuloService } from '../../../services/tipo-articulo.service';
@Component({selector:'Servicios',standalone:true,imports:[CommonModule,FormsModule,Sidebar,Supbar,Tabla,DocumentacionAdjunta,DatosIdentificacion,DatosMovimiento,BarraAcciones],templateUrl:'./servicios.html',styleUrl:'../../../styles/estiloGeneral.css'})
export class Servicios {
 @ViewChild(Tabla) tabla!:Tabla;
 vistaActiva:'tabla'|'registro'|'adjuntos'|'historico'='tabla';
 modoGestion=false;
 modo:'insertar'|'modificar'|'ver'='insertar'; seleccionada:Servicio|null=null; servicio=this.vacio(); datos:Servicio[]=[]; datosHistorico:Servicio[]=[]; tiposServicio:TipoArticulo[]=[]; mostrarObligatorios=false;
 columnas=['empId','serId','serIdHis','serTipMov','serCauMov','serTipSer','serNom','serCat','serSubCat','serDurMin','serPreVen','serPreIva','serPreFin','serUsuMov','serFecMov','serAct'];
 titulosColumnas:any={empId:'Empresa',serId:'Servicio',serIdHis:'Id Histórico',serTipMov:'Tipo Movimiento',serCauMov:'Causa Movimiento',serTipSer:'Tipo Servicio',serNom:'Nombre',serCat:'Categoría',serSubCat:'Subcategoría',serDurMin:'Duración (min)',serPreVen:'Precio Venta',serPreIva:'IVA',serPreFin:'Precio Final',serUsuMov:'Usuario',serFecMov:'Fecha',serAct:'Activo'};
 constructor(private service:ServicioService,private pdf:PdfService,private router:Router,private route:ActivatedRoute,private dialogos:DialogosService,private tiposApi:TipoArticuloService){this.modoGestion=this.route.snapshot.data['modoGestion']===true;this.tiposApi.listar('SERVICIO').subscribe({next:r=>this.tiposServicio=r,error:()=>avisarAplicacion('No se pudieron cargar los tipos de servicio.')});this.consultar();}
 consultar(){this.vistaActiva='tabla';this.seleccionada=null;this.service.consultar().subscribe({next:r=>this.datos=r,error:e=>this.error(e)});}
 insertar(){this.modo='insertar';this.servicio=this.vacio();this.vistaActiva='registro';this.service.siguienteId().subscribe(id=>this.servicio.serId=id);}
 modificar(){if(!this.seleccionada)return;this.modo='modificar';this.servicio={...this.seleccionada,serTipMov:'M',serCauMov:'Modificación del registro'};this.vistaActiva='registro';}
 ver(){if(!this.seleccionada)return;this.modo='ver';this.servicio={...this.seleccionada};this.vistaActiva='registro';}
 guardar(){this.mostrarObligatorios=true;if(!this.valido())return;const op=this.modo==='insertar'?this.service.guardar({...this.servicio,serId:0,serIdHis:1,serTipMov:'A'}):this.service.actualizar(this.servicio);op.subscribe({next:()=>this.consultar(),error:e=>this.error(e)});}
 async baja(){if(!this.seleccionada||this.seleccionada.serTipMov==='B'||!await confirmarAplicacion('¿Desea dar de baja el servicio seleccionado? El movimiento quedará en el histórico.',true))return;this.service.baja(this.seleccionada.serId).subscribe({next:()=>this.consultar(),error:e=>this.error(e)});}
 async eliminar(){if(!this.seleccionada||!await confirmarAplicacion('¿Desea eliminar definitivamente el servicio y todos sus movimientos históricos?',true))return;this.service.eliminar(this.seleccionada.serId).subscribe({next:()=>this.consultar(),error:e=>this.error(e)});}
 async reactivar(){if(!this.seleccionada||this.seleccionada.serTipMov!=='B'||!await confirmarAplicacion('¿Desea reactivar el servicio seleccionado?'))return;this.service.deshacer(this.seleccionada.serId).subscribe({next:()=>this.consultar(),error:e=>this.error(e)});}
 adjuntos(){if(this.seleccionada)this.vistaActiva='adjuntos';}
 historico(){if(!this.seleccionada)return;this.service.historico(this.seleccionada.serId).subscribe({next:r=>{this.datosHistorico=r;this.vistaActiva='historico';},error:e=>this.error(e)});}
 deshacer(){if(!this.seleccionada)return;this.service.deshacer(this.seleccionada.serId).subscribe({next:()=>this.consultar(),error:e=>this.error(e)});}
 volverConsulta(){this.vistaActiva='tabla';}
 volver(){this.router.navigate(['/servicios']);}
 calcular(){const base=(this.servicio.serPreVen||0)-(this.servicio.serPreDes||0);this.servicio.serPreFin=Number((base+base*(this.servicio.serPreIva||0)/100).toFixed(2));}
 exportar(){this.pdf.exportar('servicios.pdf','Listado de servicios',this.columnas,this.tabla.datosFiltrados,'assets/logos/logo-greensaas.png','assets/logos/logo-jbrempresa.png');}
 private valido(){if(!this.servicio.serTipSer||!this.servicio.serNom||!this.servicio.serCat||!this.servicio.serDurMin||this.servicio.serDurMin%5!==0||this.servicio.serPreVen===null||this.servicio.serPreIva===null){this.dialogos.avisar('Informe los obligatorios. La duración debe ser múltiplo de 5 minutos.');return false;}return true;}
 private vacio():Servicio{return {empId:Number(localStorage.getItem('empresaId'))||0,serId:0,serIdHis:1,serTipMov:'A',serCauMov:'Alta del registro',serTipSer:'',serNom:'',serDes:'',serCat:'',serSubCat:'',serDurMin:5,serPreVen:0,serPreDes:0,serPreIva:21,serPreFin:0,serObs:'',serVisCat:true,serIma:'servicio-predeterminado.png',serUsuMov:localStorage.getItem('usuario')||'',serFecMov:FechasUtil.formatearFechaHora(),serAct:true};}
 private error(e:any){this.dialogos.error(e);}
}
