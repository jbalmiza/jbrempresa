import {CommonModule} from '@angular/common';
import {Component,OnInit,ViewChild} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute,Router} from '@angular/router';
import {Sidebar} from '../../components/sidebar/sidebar'; import {Supbar} from '../../components/supbar/supbar'; import {Tabla} from '../../components/tabla/tabla';
import {DatosIdentificacion} from '../../components/datosIdentificacion/datosIdentificacion'; import {DatosMovimiento} from '../../components/datosMovimiento/datosMovimiento'; import {BarraAcciones} from '../../directives/barraAcciones/barraAcciones';
import {DocumentacionAdjunta} from '../../components/documentacionAdjunta/documentacionAdjunta';
import {avisarAplicacion,confirmarAplicacion} from '../../core/interaccion/dialogos.service'; import {TipoArticulo,TipoArticuloService} from '../../services/tipo-articulo.service';
@Component({selector:'tipos-articulo',standalone:true,imports:[CommonModule,FormsModule,Sidebar,Supbar,Tabla,DatosIdentificacion,DatosMovimiento,DocumentacionAdjunta,BarraAcciones],templateUrl:'./tiposArticulo.html',styleUrls:['../../styles/estiloGeneral.css','./tiposArticulo.css']})
export class TiposArticulo implements OnInit {
 @ViewChild(Tabla) tabla!:Tabla; clase='PRODUCTO';modulo='productos';vistaActiva:'tabla'|'registro'|'adjuntos'='tabla';modoFormulario:'insertar'|'ver'|'modificar'='insertar';tipos:TipoArticulo[]=[];seleccionado:TipoArticulo|null=null;tipo:TipoArticulo=this.vacio();mostrarObligatorios=false;
 columnas=['id','nombre','imagen','usuario','fecha','activo'];titulosColumnas={id:'Id Tipo',nombre:'Nombre',imagen:'Imagen genérica',usuario:'Usuario Mod.',fecha:'Fecha Mod.',activo:'Activo'};
 constructor(private api:TipoArticuloService,private route:ActivatedRoute,private router:Router){} ngOnInit(){this.clase=this.route.snapshot.data['clase'];this.modulo=this.clase==='PRODUCTO'?'productos':'servicios';this.consultar()} get titulo(){return`REGISTRO DE TIPOS DE ${this.clase}`}
 vacio():TipoArticulo{return{empId:Number(localStorage.getItem('empresaId'))||0,id:null,clase:this.clase,nombre:'',imagen:null,activo:true}} consultar(){this.vistaActiva='tabla';this.seleccionado=null;this.api.listar(this.clase).subscribe({next:r=>this.tipos=r,error:e=>avisarAplicacion(e.error?.mensaje||'No se pudieron cargar los tipos.')})}
 insertar(){this.tipo=this.vacio();this.mostrarObligatorios=false;this.modoFormulario='insertar';this.vistaActiva='registro'}
 abrir(modo:'ver'|'modificar'){if(!this.seleccionado){avisarAplicacion('Debe seleccionar un registro.');return}this.tipo={...this.seleccionado};this.modoFormulario=modo;this.vistaActiva='registro'}
 adjuntos(){if(!this.seleccionado){avisarAplicacion('Debe seleccionar un registro.');return}this.vistaActiva='adjuntos'}
 guardar(){this.mostrarObligatorios=true;if(!this.tipo.nombre.trim())return;this.api.guardar(this.clase,this.tipo).subscribe({next:()=>this.finalizar(),error:e=>avisarAplicacion(e.error?.mensaje||'No se pudo guardar el tipo.')})}
 private finalizar(){avisarAplicacion(this.modoFormulario==='insertar'?'Tipo insertado correctamente.':'Tipo actualizado correctamente.');this.consultar()}
 async eliminar(){const t=this.seleccionado;if(!t?.id){avisarAplicacion('Debe seleccionar un registro.');return}if(!await confirmarAplicacion(`¿Eliminar el tipo ${t.nombre}?`,true))return;this.api.eliminar(this.clase,t.id).subscribe({next:()=>{avisarAplicacion('Tipo eliminado correctamente.');this.consultar()},error:e=>avisarAplicacion(e.error?.mensaje||'No se pudo eliminar el tipo.')})} volver(){this.router.navigate(['/'+this.modulo])}
}
