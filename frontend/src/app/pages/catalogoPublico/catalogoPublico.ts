import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CatalogoProducto, CatalogoPublico as CatalogoDatos, CatalogoService } from '../../services/catalogo.service';

interface LineaCesta { producto:CatalogoProducto; cantidad:number; observaciones:string; }
@Component({selector:'catalogo-publico',standalone:true,imports:[CommonModule,FormsModule],templateUrl:'./catalogoPublico.html',styleUrl:'./catalogoPublico.css'})
export class CatalogoPublico implements OnInit {
  token=''; modoMovil=false; catalogo:CatalogoDatos|null=null; cargando=true; error=''; categoria=''; cesta:LineaCesta[]=[]; cestaAbierta=false; confirmando=false;
  nombre='';telefono='';direccion='';observaciones='';confirmacion:any=null;
  constructor(private route:ActivatedRoute,public service:CatalogoService){}
  ngOnInit(){this.token=this.route.snapshot.paramMap.get('token')||'';this.modoMovil=this.route.snapshot.queryParamMap.get('vista')==='movil';this.service.publico(this.token).subscribe({next:c=>{this.catalogo=c;this.categoria=this.categorias[0]||'';this.cargando=false;},error:e=>{this.error=e.error?.detail||e.error?.message||'El catálogo no está disponible.';this.cargando=false;}});}
  get categorias(){
    const conceptos=this.catalogo?.productos||[];
    const productos=conceptos.filter(p=>p.tipo==='PRODUCTO').map(p=>p.categoria);
    const servicios=conceptos.filter(p=>p.tipo==='SERVICIO').map(p=>p.categoria);
    return [...new Set([...productos,...servicios])];
  }
  get productos(){return (this.catalogo?.productos||[]).filter(p=>p.categoria===this.categoria).sort((a,b)=>a.id-b.id);}
  esCategoriaServicio(categoria:string){return (this.catalogo?.productos||[]).some(p=>p.categoria===categoria&&p.tipo==='SERVICIO');}
  cerrar(){if(window.opener&&!window.opener.closed)window.opener.focus();window.close();}
  cantidad(id:number,tipo?:string){return this.cesta.find(x=>x.producto.id===id&&(!tipo||x.producto.tipo===tipo))?.cantidad||0;}
  agregar(p:CatalogoProducto){if(p.agotado)return;const x=this.cesta.find(l=>l.producto.id===p.id&&l.producto.tipo===p.tipo);if(x)x.cantidad++;else this.cesta.push({producto:p,cantidad:1,observaciones:''});}
  quitar(p:CatalogoProducto){const x=this.cesta.find(l=>l.producto.id===p.id);if(!x)return;if(--x.cantidad<=0)this.cesta=this.cesta.filter(l=>l!==x);}
  get unidades(){return this.cesta.reduce((n,l)=>n+l.cantidad,0);}
  get total(){return this.cesta.reduce((n,l)=>n+l.cantidad*l.producto.precio,0);}
  enviar(){if(!this.nombre.trim()||!this.telefono.trim()){this.error='Indica tu nombre y teléfono.';return;}if(this.catalogo?.modalidad==='DOMICILIO'&&!this.direccion.trim()){this.error='Indica la dirección de envío.';return;}this.error='';this.confirmando=true;this.service.pedir(this.token,{nombre:this.nombre,telefono:this.telefono,modalidad:this.catalogo?.modalidad,direccionEnvio:this.direccion,observaciones:this.observaciones,lineas:this.cesta.map(l=>({tipo:l.producto.tipo,productoId:l.producto.id,cantidad:l.cantidad,observaciones:l.observaciones}))}).subscribe({next:r=>{this.confirmacion=r;this.cesta=[];this.confirmando=false;scrollTo({top:0,behavior:'smooth'});},error:e=>{this.error=e.error?.detail||e.error?.message||'No se pudo enviar el pedido.';this.confirmando=false;}});}
}
