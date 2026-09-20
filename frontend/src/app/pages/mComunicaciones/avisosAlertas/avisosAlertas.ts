import {CommonModule} from '@angular/common'; import {Component,OnInit} from '@angular/core'; import {FormsModule} from '@angular/forms'; import {ActivatedRoute,Router} from '@angular/router';
import {Sidebar} from '../../../components/sidebar/sidebar'; import {Supbar} from '../../../components/supbar/supbar'; import {Tabla} from '../../../components/tabla/tabla'; import {DatosIdentificacion} from '../../../components/datosIdentificacion/datosIdentificacion'; import {DatosMovimiento} from '../../../components/datosMovimiento/datosMovimiento'; import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service'; import {AvisoAlerta,EmisorAviso,DestinatarioAviso,UbicacionAviso} from '../../../interfaces/aviso-alerta.interface'; import {AvisoAlertaService} from '../../../services/aviso-alerta.service';
import {EmpresaService} from '../../../services/empresa.service'; import {EmpresaRelacionService} from '../../../services/empresa-relacion.service'; import {MENUS_MODULOS} from '../../../config/menu-modulos.config';
import {FechasUtil} from '../../../shared/utils/fechas.util';
@Component({selector:'avisos-alertas',standalone:true,imports:[CommonModule,FormsModule,Sidebar,Supbar,Tabla,DatosIdentificacion,DatosMovimiento,BarraAcciones],templateUrl:'./avisosAlertas.html',styleUrl:'../../../styles/estiloGeneral.css'})
export class AvisosAlertas implements OnInit {
 gestion=false; vista:'tabla'|'registro'|'historico'='tabla'; modo:'insertar'|'ver'|'modificar'='insertar'; datos:AvisoAlerta[]=[]; datosPresentacion:AvisoAlerta[]=[]; historicoDatos:AvisoAlerta[]=[]; seleccionado:AvisoAlerta|null=null; aviso=this.vacio(); mostrarObligatorios=false;
 empresasDestino:{id:number;nombre:string}[]=[];
 readonly perfil=(localStorage.getItem('perfil')||'').trim().toUpperCase();
 readonly ventanas=Object.entries(MENUS_MODULOS).flatMap(([modulo,menu])=>menu.grupos
   .filter(g=>['menu1','menu2','menu5'].includes(g.id)||modulo==='empleados')
   .flatMap(g=>g.opciones.filter(o=>o.ruta&&modulo!=='proveedores'&&
     !(modulo==='compras'&&o.ruta==='personas'))
     .map(o=>({ruta:`/${modulo}/${o.ruta}`,nombre:`${menu.titulo} · ${o.texto}`}))))
   .concat([{ruta:'/compras/compras',nombre:'Compras · Registro de Compras'},
     {ruta:'/empleados',nombre:'Empleados · Módulo'},
     {ruta:'/clientes',nombre:'Clientes · Catálogo'}]);
 columnas=['empId','aviId','aviIdHis','aviTipMov','aviCauMov','aviTipo','aviTitulo','aviMensaje','aviEmisor','aviEmisorEmpresa','aviEmisorEmpId','aviEmisorUsuId','aviDestinatario','aviDestEmpresa','aviDestEmpId','aviUbicacion','aviVentana','aviFecIni','aviFecFin','aviUsuMov','aviFecMov','aviAct']; titulos={empId:'Id Empresa',aviId:'Id Aviso',aviIdHis:'Id Histórico',aviTipMov:'Tipo Movimiento',aviCauMov:'Causa Movimiento',aviTipo:'Tipo',aviTitulo:'Título',aviMensaje:'Mensaje',aviEmisor:'Emisor',aviEmisorEmpresa:'Empresa Emisora',aviEmisorEmpId:'Id Empresa Emisora',aviEmisorUsuId:'Id Usuario Emisor',aviDestinatario:'Destinatario',aviDestEmpresa:'Empresa Destinataria',aviDestEmpId:'Id Empresa Destinataria',aviUbicacion:'Ubicación',aviVentana:'Ventana',aviFecIni:'Inicio',aviFecFin:'Fin',aviUsuMov:'Usuario Mod.',aviFecMov:'Fecha Mod.',aviAct:'Activo'};
 constructor(private api:AvisoAlertaService,private route:ActivatedRoute,private router:Router,private empresasApi:EmpresaService,private relaciones:EmpresaRelacionService){} ngOnInit(){this.gestion=!!this.route.snapshot.data['gestion'];this.cargarDestinos()}
 get columnasVista(){return this.gestion?this.columnas:this.columnas.filter(c=>c!=='aviIdHis')}
 private actualizarPresentacion(){this.datosPresentacion=this.datos.map(a=>({...a,aviEmisorEmpresa:this.nombreEmpresa(a.aviEmisorEmpId),aviDestEmpresa:a.aviDestinatario==='EMPRESA'&&a.aviDestEmpId==null?'Todas las empresas':this.nombreEmpresa(a.aviDestEmpId)}))}
 seleccionar(fila:AvisoAlerta|null){this.seleccionado=fila?this.datos.find(a=>a.empId===fila.empId&&a.aviId===fila.aviId)||null:null}
 get historicoPresentacion(){return this.historicoDatos.map(a=>({...a,aviEmisorEmpresa:this.nombreEmpresa(a.aviEmisorEmpId),aviDestEmpresa:a.aviDestinatario==='EMPRESA'&&a.aviDestEmpId==null?'Todas las empresas':this.nombreEmpresa(a.aviDestEmpId)}))}
 private nombreEmpresa(id:number|null){return id==null?'':this.empresasDestino.find(e=>e.id===id)?.nombre||`Empresa ${id}`}
 vacio():AvisoAlerta{const empId=Number(localStorage.getItem('contexto.empresaId'))||Number(localStorage.getItem('empresaId'))||0;const hoy=new Date();const finSemana=new Date(hoy.getFullYear(),hoy.getMonth(),hoy.getDate()+7);const fecha=(dia:Date)=>`${dia.getFullYear()}-${String(dia.getMonth()+1).padStart(2,'0')}-${String(dia.getDate()).padStart(2,'0')}`;return{empId,aviId:null,aviIdHis:1,aviTipMov:'A',aviCauMov:'Alta del registro',aviTipo:'AVISO',aviTitulo:'',aviMensaje:'',aviEmisor:this.emisorInicial(),aviEmisorEmpId:null,aviEmisorUsuId:null,aviDestinatario:'CLIENTE',aviDestEmpId:empId,aviUbicacion:'CATALOGO_CLIENTE',aviVentana:null,aviFecIni:`${fecha(hoy)}T00:00:00`,aviFecFin:`${fecha(finSemana)}T23:59:59`,aviUsuMov:localStorage.getItem('usuario')||'',aviFecMov:FechasUtil.formatearFechaHora(hoy),aviAct:true}}
 consultar(){this.vista='tabla';this.seleccionado=null;this.api.consultar().subscribe({next:r=>{this.datos=r;this.actualizarPresentacion()},error:e=>this.error(e,'No se pudieron consultar los avisos y alertas.')})}
 insertar(){this.aviso=this.vacio();this.configurarDestino();this.modo='insertar';this.mostrarObligatorios=false;this.vista='registro';this.api.siguienteId().subscribe(id=>this.aviso.aviId=id)}
 abrir(modo:'ver'|'modificar'){if(!this.seleccionado)return;this.aviso={...this.seleccionado,aviFecIni:this.fechaFormulario(this.seleccionado.aviFecIni),aviFecFin:this.fechaFormulario(this.seleccionado.aviFecFin)};this.modo=modo;this.vista='registro'}
 guardar(){this.mostrarObligatorios=true;if(!this.aviso.aviTitulo.trim()||!this.aviso.aviMensaje.trim()||!this.aviso.aviDestinatario||!this.aviso.aviUbicacion||(this.aviso.aviUbicacion==='VENTANA'&&!this.aviso.aviVentana)||(['VENTANA','MENSAJES'].includes(this.aviso.aviUbicacion)&&this.aviso.aviDestinatario!=='ADMINISTRADOR'&&!this.aviso.aviDestEmpId&&!(this.aviso.aviEmisor==='ADMINISTRADOR'&&this.aviso.aviDestinatario==='EMPRESA'))){avisarAplicacion('Complete emisor, destinatario y ubicación del aviso.');return;}const peticion=this.modo==='insertar'?this.api.crear(this.aviso):this.api.actualizar(this.aviso);peticion.subscribe({next:()=>{avisarAplicacion(this.modo==='insertar'?'Aviso o alerta insertado correctamente.':'Aviso o alerta actualizado correctamente.');this.consultar()},error:e=>this.error(e,'No se pudo guardar el aviso o alerta.')})}
 async eliminar(){if(!this.seleccionado?.aviId)return;if(!await confirmarAplicacion('¿Eliminar definitivamente el aviso o alerta y todo su histórico?',true))return;this.api.eliminar(this.seleccionado.aviId).subscribe({next:()=>this.consultar(),error:e=>this.error(e,'No se pudo eliminar.')})}
 baja(){if(!this.seleccionado?.aviId)return;this.api.baja(this.seleccionado.aviId).subscribe({next:()=>this.consultar(),error:e=>this.error(e,'No se pudo establecer la baja.')})}
 reactivar(){if(!this.seleccionado?.aviId)return;this.api.reactivar(this.seleccionado.aviId).subscribe({next:()=>this.consultar(),error:e=>this.error(e,'No se pudo reactivar.')})}
 verHistorico(){if(!this.seleccionado?.aviId)return;this.api.historico(this.seleccionado.aviId).subscribe({next:r=>{this.historicoDatos=r;this.vista='historico'},error:e=>this.error(e,'No se pudo consultar el histórico.')})}
 volver(){if(this.vista!=='tabla'){this.vista='tabla';return}this.router.navigate([this.perfil==='EMPLEADO'?'/empleados':'/comunicaciones'])} fechaFormulario(v:string|null){return v?v.substring(0,19):null} private error(e:any,m:string){avisarAplicacion(e.error?.detail||e.error?.mensaje||e.error?.message||m)}
 private emisorInicial():EmisorAviso{const p=(localStorage.getItem('perfil')||'').trim().toUpperCase();return p==='ADMINISTRADOR'?'ADMINISTRADOR':p==='EMPLEADO'?'EMPLEADO':'JEFE'}
 get emisores():EmisorAviso[]{return this.perfil==='ADMINISTRADOR'?['ADMINISTRADOR']:this.perfil==='EMPLEADO'?['EMPLEADO']:['JEFE','PROVEEDOR']}
 get ubicaciones():{valor:UbicacionAviso;nombre:string}[]{const e=this.aviso.aviEmisor;
   if(e==='EMPLEADO')return[{valor:'MENSAJES',nombre:'Mensajes'}];
   if(e==='PROVEEDOR')return[{valor:'MENSAJES',nombre:'Mensajes'},{valor:'CATALOGO_PROVEEDOR',nombre:'Mi catálogo de proveedor'}];
   if(e==='JEFE')return[{valor:'CATALOGO_CLIENTE',nombre:'Catálogo de clientes'},{valor:'VENTANA',nombre:'Ventana de Empleados'},{valor:'MENSAJES',nombre:'Mensajes'}];
   return[{valor:'CATALOGO_CLIENTE',nombre:'Catálogo de clientes'},{valor:'VENTANA',nombre:'Ventana de Registro o Gestión'},{valor:'MENSAJES',nombre:'Mensajes'}]}
 get destinatarios():{valor:DestinatarioAviso;nombre:string}[]{const a=this.aviso;
   if(a.aviUbicacion==='CATALOGO_CLIENTE')return[{valor:'CLIENTE',nombre:'Clientes'}];
   if(a.aviUbicacion==='CATALOGO_PROVEEDOR'||a.aviUbicacion==='VENTANA')return[{valor:'EMPRESA',nombre:'Empresa'}];
   if(a.aviEmisor==='EMPLEADO')return[{valor:'JEFE',nombre:'Jefe de mi empresa'}];
   if(a.aviEmisor==='JEFE')return[{valor:'ADMINISTRADOR',nombre:'Administrador'}];
   if(a.aviEmisor==='PROVEEDOR')return[{valor:'ADMINISTRADOR',nombre:'Administrador'},{valor:'EMPRESA',nombre:'Jefe de empresa cliente'}];
   return[{valor:'EMPRESA',nombre:'Jefe de la empresa'}]}
 get ventanasDisponibles(){return this.aviso.aviEmisor==='JEFE'?this.ventanas.filter(v=>v.ruta==='/empleados'):this.ventanas}
 get empresasSeleccionables(){return this.aviso.aviEmisor==='PROVEEDOR'&&this.aviso.aviUbicacion==='MENSAJES'&&this.aviso.aviDestinatario==='EMPRESA'
   ?this.empresasDestino.filter(e=>e.id!==this.aviso.empId):this.empresasDestino}
 configurarDestino(){const a=this.aviso;const lugares=this.ubicaciones;if(!lugares.some(x=>x.valor===a.aviUbicacion))a.aviUbicacion=lugares[0].valor;
   const destinos=this.destinatarios;if(!destinos.some(x=>x.valor===a.aviDestinatario))a.aviDestinatario=destinos[0].valor;
   a.aviDestEmpId=a.aviDestinatario==='ADMINISTRADOR'?null:a.aviEmisor==='PROVEEDOR'&&a.aviUbicacion==='MENSAJES'&&a.aviDestinatario==='EMPRESA'
     ?this.empresasSeleccionables[0]?.id||null:a.empId;
   a.aviVentana=a.aviUbicacion==='VENTANA'?this.ventanasDisponibles[0]?.ruta||null:null;
 }
 private cargarDestinos(){if(this.perfil==='ADMINISTRADOR')this.empresasApi.obtenerEmpresas().subscribe({next:r=>{this.empresasDestino=r.map(e=>({id:Number(e.empId),nombre:e.empNom}));this.actualizarPresentacion()},error:e=>this.error(e,'No se pudieron cargar las empresas.')});
   else this.relaciones.listar().subscribe({next:r=>{this.empresasDestino=[{id:Number(localStorage.getItem('empresaId')),nombre:localStorage.getItem('empresa')||'Mi empresa'},...r.filter(x=>x.tipo==='CLIENTE'&&x.activa).map(x=>({id:x.empresaRelacionadaId,nombre:x.empresaRelacionada}))];this.actualizarPresentacion()},error:e=>this.error(e,'No se pudieron cargar las empresas relacionadas.')});}
}
