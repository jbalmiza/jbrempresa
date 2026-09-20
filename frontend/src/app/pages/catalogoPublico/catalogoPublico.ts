import { ImagenHttp } from '../../directives/imagenHttp/imagenHttp';
import { DialogosService } from '../../core/interaccion/dialogos.service';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import { Component, OnInit, OnChanges, OnDestroy, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogoComponente, CatalogoProducto, CatalogoPublico as CatalogoDatos, CatalogoService } from '../../services/catalogo.service';
import { AvisosVentana } from '../../components/avisosVentana/avisosVentana';

interface LineaCesta { producto:CatalogoProducto; cantidad:number; observaciones:string; componentes:number[]; resumenComponentes:string; }
@Component({selector:'catalogo-publico',standalone:true,imports:[CommonModule,FormsModule,ImagenHttp,AvisosVentana],templateUrl:'./catalogoPublico.html',styleUrl:'./catalogoPublico.css'})
export class CatalogoPublico implements OnInit, OnChanges, OnDestroy {
  @Input() proveedorId=0;
  @Output() cerrado=new EventEmitter<void>();
  private carga?:Subscription;
  private pedido?:Subscription;
  ngOnChanges(){if(this.proveedorId)this.cargarProveedor();}
  ngOnDestroy(){this.carga?.unsubscribe();this.pedido?.unsubscribe();}
  private cargarProveedor(){
    this.carga?.unsubscribe();this.pedido?.unsubscribe();
    this.catalogo=null;this.cesta=[];this.cestaAbierta=false;this.confirmacion=null;this.confirmando=false;this.error='';this.cargando=true;this.avisosOcultos.clear();
    this.carga=this.service.proveedor(this.proveedorId).subscribe({next:c=>{this.catalogo=c;this.categoria=this.categorias[0]||'';this.cargando=false;},error:e=>this.mostrarError(e)});
  }
  token=''; modoMovil=false; catalogoSesion=false; catalogo:CatalogoDatos|null=null; cargando=true; error=''; categoria=''; cesta:LineaCesta[]=[]; cestaAbierta=false; confirmando=false;
  avisosOcultos=new Set<object>();
  productoAlergenos:CatalogoProducto|null=null;
  lineaPersonalizada:LineaCesta|null=null;componentesTemporales=new Set<number>();buscarComponente='';
  nombre='';telefono='';correo='';direccion='';observaciones='';confirmacion:any=null;
  constructor(private route:ActivatedRoute,public service:CatalogoService,private dialogos:DialogosService,private router:Router){}
  ngOnInit(){
    this.catalogoSesion=!!this.route.snapshot.data['catalogoSesion'];
    this.token=this.route.snapshot.paramMap.get('token')||'';
    this.modoMovil=this.route.snapshot.queryParamMap.get('vista')==='movil'||window.matchMedia('(max-width: 430px)').matches;
    if(this.proveedorId)return;
    if(this.token){this.cargarCatalogo();return;}
    if(this.route.snapshot.data['catalogoSesion']){
      this.service.configuracion().subscribe({
        next:c=>{this.token=c.alias||c.tokenGeneral;this.cargarCatalogo();},
        error:e=>this.mostrarError(e)
      });
      return;
    }
    this.error='El catálogo no está disponible.';this.cargando=false;
  }
  private cargarCatalogo(){this.service.publico(this.token).subscribe({next:c=>{this.catalogo=c;this.categoria=this.categorias[0]||'';this.cargando=false;},error:e=>this.mostrarError(e)});}
  private mostrarError(e:any){this.error=e.error?.detail||e.error?.message||'El catálogo no está disponible.';this.cargando=false;this.dialogos.error(e);}
  get categorias(){
    const conceptos=this.catalogo?.productos||[];
    const productos=conceptos.filter(p=>p.tipo==='PRODUCTO').map(p=>p.categoria);
    const servicios=conceptos.filter(p=>p.tipo==='SERVICIO').map(p=>p.categoria);
    return [...new Set([...productos,...servicios])];
  }
  get productos(){return (this.catalogo?.productos||[]).filter(p=>p.categoria===this.categoria).sort((a,b)=>a.id-b.id);}
  get avisosVisibles(){return (this.catalogo?.avisos||[]).filter(a=>!this.avisosOcultos.has(a));}
  cerrarAviso(aviso:object){this.avisosOcultos.add(aviso);}
  verAlergenos(producto:CatalogoProducto,event:Event){event.stopPropagation();this.productoAlergenos=producto;}
  esCategoriaServicio(categoria:string){return (this.catalogo?.productos||[]).some(p=>p.categoria===categoria&&p.tipo==='SERVICIO');}
  cerrar(){if(this.proveedorId){this.cerrado.emit();return;}const volverA=this.route.snapshot.data['volverA'];if(volverA){this.router.navigate([volverA]);return;}if(window.opener&&!window.opener.closed)window.opener.focus();window.close();}
  cantidad(id:number,tipo?:string){return this.cesta.filter(x=>x.producto.id===id&&(!tipo||x.producto.tipo===tipo)).reduce((n,x)=>n+x.cantidad,0);}
  agregar(p:CatalogoProducto){if(p.agotado)return;const x=this.cesta.find(l=>l.producto.id===p.id&&l.producto.tipo===p.tipo&&!l.resumenComponentes&&this.mismosComponentes(l.componentes,p.componentes||[]));if(x)x.cantidad++;else this.cesta.push({producto:p,cantidad:1,observaciones:'',componentes:[...(p.componentes||[])],resumenComponentes:''});}
  quitar(p:CatalogoProducto){const lineas=this.cesta.filter(l=>l.producto.id===p.id&&l.producto.tipo===p.tipo),x=lineas.find(l=>!l.resumenComponentes&&this.mismosComponentes(l.componentes,p.componentes||[]))||lineas.at(-1);if(x)this.quitarLinea(x);}
  agregarLinea(linea:LineaCesta){linea.cantidad++;}
  quitarLinea(linea:LineaCesta){if(--linea.cantidad<=0)this.cesta=this.cesta.filter(l=>l!==linea);}
  get unidades(){return this.cesta.reduce((n,l)=>n+l.cantidad,0);}
  get total(){return this.cesta.reduce((n,l)=>n+this.precioLinea(l),0);}
  precioLinea(l:LineaCesta){return l.cantidad*(l.producto.precio+this.componentesAnadidos(l).reduce((n,c)=>n+c.precioAdicional,0));}
  componentesAnadidos(l:LineaCesta):CatalogoComponente[]{const base=new Set(l.producto.componentes||[]);return this.componentes.filter(c=>l.componentes.includes(c.id)&&!base.has(c.id));}
  componentesEliminados(l:LineaCesta):CatalogoComponente[]{const elegidos=new Set(l.componentes);return this.componentes.filter(c=>(l.producto.componentes||[]).includes(c.id)&&!elegidos.has(c.id));}
  get componentes(){return this.catalogo?.componentes||[];}
  get componentesFiltrados(){const texto=this.buscarComponente.trim().toLocaleLowerCase();return texto?this.componentes.filter(c=>c.nombre.toLocaleLowerCase().includes(texto)):this.componentes;}
  abrirPersonalizacion(l:LineaCesta){this.lineaPersonalizada=l;this.componentesTemporales=new Set(l.componentes);this.buscarComponente='';}
  alternarComponente(id:number){this.componentesTemporales.has(id)?this.componentesTemporales.delete(id):this.componentesTemporales.add(id);}
  restablecerComponentes(){if(this.lineaPersonalizada)this.componentesTemporales=new Set(this.lineaPersonalizada.producto.componentes||[]);}
  aplicarComponentes(){if(!this.lineaPersonalizada)return;let linea=this.lineaPersonalizada;const elegidos=[...this.componentesTemporales];if(linea.cantidad>1&&!this.mismosComponentes(linea.componentes,elegidos)){linea.cantidad--;linea={...linea,cantidad:1,componentes:[...linea.componentes]};this.cesta.splice(this.cesta.indexOf(this.lineaPersonalizada)+1,0,linea);}if(linea.resumenComponentes)linea.observaciones=linea.observaciones.replace(linea.resumenComponentes,'').replace(/^\s*[|·]\s*|\s*[|·]\s*$/g,'').trim();linea.componentes=elegidos;const sin=this.componentesEliminados(linea).map(c=>c.nombre),con=this.componentesAnadidos(linea).map(c=>c.nombre);linea.resumenComponentes=[sin.length?'Sin: '+sin.join(', '):'',con.length?'Añadir: '+con.join(', '):''].filter(Boolean).join(' · ');linea.observaciones=[linea.observaciones,linea.resumenComponentes].filter(Boolean).join(' | ');this.lineaPersonalizada=null;}
  private mismosComponentes(a:number[],b:number[]){return a.length===b.length&&a.every(id=>b.includes(id));}
  enviar(){if(!this.nombre.trim()||!this.telefono.trim()){this.dialogos.avisar('Indica tu nombre y teléfono.');return;}if(this.catalogo?.modalidad==='DOMICILIO'&&!this.direccion.trim()){this.dialogos.avisar('Indica la dirección de envío.');return;}this.error='';this.confirmando=true;const entrada={nombre:this.nombre,telefono:this.telefono,correo:this.correo,modalidad:this.catalogo?.modalidad,direccionEnvio:this.direccion,observaciones:this.observaciones,lineas:this.cesta.map(l=>({tipo:l.producto.tipo,productoId:l.producto.id,cantidad:l.cantidad,observaciones:l.observaciones,componentes:l.producto.tipo==='PRODUCTO'?l.componentes:[]}))};this.pedido=(this.proveedorId?this.service.pedirProveedor(this.proveedorId,entrada):this.service.pedir(this.token,entrada)).subscribe({next:r=>{this.confirmacion=r;this.cesta=[];this.confirmando=false;scrollTo({top:0,behavior:'smooth'});},error:e=>{this.dialogos.error(e,'No se pudo enviar el pedido.');this.confirmando=false;}});}
}
