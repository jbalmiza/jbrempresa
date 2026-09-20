import {Component,OnDestroy,OnInit} from '@angular/core'; import {CommonModule} from '@angular/common'; import {Router} from '@angular/router'; import {Supbar} from '../../components/supbar/supbar'; import {ModuloAplicacionService} from '../../services/modulo-aplicacion.service'; import {AdjuntoService} from '../../services/adjunto.service'; import {ModuloAplicacion} from '../../interfaces/modulo-aplicacion.interface'; import {BlobUrlUtil} from '../../shared/utils/blob-url.util';
import {navegarModuloEnMismaPestana} from '../../shared/utils/navegacion-modulos.util';
@Component({selector:'AccesoModulos',standalone:true,imports:[CommonModule,Supbar],templateUrl:'./accesoModulos.html',styleUrl:'./accesoModulos.css'})
export class AccesoModulos implements OnInit,OnDestroy{
 private readonly blobs=new BlobUrlUtil(); modulos:ModuloAplicacion[]=[];
 private readonly codigosPersonas=new Set(['CLIENTES','EMPLEADOS','PROVEEDORES']);
 constructor(private router:Router,private api:ModuloAplicacionService,private adjuntos:AdjuntoService){}
 ngOnInit(){this.api.panel().subscribe({next:m=>{this.modulos=m;this.cargarImagenes()}})} ngOnDestroy(){this.blobs.liberarTodas()}
 icono(m:ModuloAplicacion){const f:Record<string,string>={ADMINISTRACION:'mAdministracion.png',COMUNICACIONES:'mComunicaciones.png',TERRITORIO:'mTerritorio.png',PERSONAS:'mPersonas.png',CLIENTES:'mClientes.png',EMPLEADOS:'mEmpleados.png',PROVEEDORES:'mProveedores.png',PRODUCTOS:'mProductos.png',SERVICIOS:'mServicios.png',COMPRAS:'mCompras.png',VENTAS:'mVentas.png',RECURSOS:'mRecursos.png',CAJA:'mCaja.png'};return (m as any).icono||`assets/modulos/${f[m.codigo]||'mAdministracion.png'}`}
 get modulosEmpresa(){return this.modulos.filter(m=>!this.codigosPersonas.has(m.codigo.toUpperCase()))}
 get modulosPersonas(){return this.modulos.filter(m=>this.codigosPersonas.has(m.codigo.toUpperCase()))}
 abrirModulo(modulo:string){if(navegarModuloEnMismaPestana()){this.router.navigate(['/'+modulo]);return}const url=this.router.serializeUrl(this.router.createUrlTree(['/'+modulo]));window.open(url,'_blank')}
 private cargarImagenes(){this.modulos.filter(m=>m.imagenAdjuntoId).forEach(m=>this.adjuntos.contenido(m.imagenAdjuntoId!,'RUTA_DOCUMENTOS_MODULOS',false).subscribe({next:b=>(m as any).icono=this.blobs.crear(b)}))}
}
