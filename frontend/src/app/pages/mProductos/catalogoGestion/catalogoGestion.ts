import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { CatalogoConfiguracion, CatalogoPosicion, CatalogoService } from '../../../services/catalogo.service';
import { BlobUrlUtil } from '../../../shared/utils/blob-url.util';

@Component({selector:'catalogo-gestion',standalone:true,imports:[CommonModule,FormsModule,Sidebar,Supbar],templateUrl:'./catalogoGestion.html',styleUrls:['../../../styles/estiloGeneral.css','./catalogoGestion.css','./catalogoGestionOrganizacion.css']})
export class CatalogoGestion implements OnInit{
  private readonly blobs = new BlobUrlUtil();
  moduloOrigen:'productos'|'servicios'='productos';
  vista:'configuracion'|'qr'|'preview'='configuracion';config:CatalogoConfiguracion={publicado:false,permitirDomicilio:false,tokenGeneral:'',empresaConImagen:false};posiciones:CatalogoPosicion[]=[];posicion:CatalogoPosicion=this.vacia();mensaje='';
  constructor(private catalogo:CatalogoService,private router:Router,private route:ActivatedRoute){}
  ngOnInit(){this.moduloOrigen=this.route.snapshot.data['moduloOrigen']==='servicios'?'servicios':'productos';this.cargar();}
  cargar(){this.catalogo.configuracion().subscribe({next:c=>this.config=c,error:e=>this.mensaje=this.textoError(e)});this.catalogo.posiciones().subscribe({next:p=>this.posiciones=p});}
  guardarConfig(){this.mensaje='';this.catalogo.configurar(this.config).subscribe({next:c=>{this.config=c;this.mensaje='Configuración guardada.';},error:e=>this.mensaje=this.textoError(e)});}
  guardarPosicion(){this.catalogo.guardarPosicion(this.posicion).subscribe({next:()=>{this.posicion=this.vacia();this.cargar();},error:e=>this.mensaje=this.textoError(e)});}
  editar(p:CatalogoPosicion){this.posicion={...p};}
  async regenerar(p:CatalogoPosicion){if(!await confirmarAplicacion('El QR anterior dejará de funcionar. ¿Desea continuar?',true))return;this.catalogo.regenerar(p.capId!).subscribe(()=>this.cargar());}
  descargar(p:CatalogoPosicion){this.catalogo.qr(p).subscribe(blob=>this.blobs.descargar(blob,`qr-${p.capUbi.replace(/[^a-z0-9]+/gi,'-')}.png`));}
  get enlaceGeneral(){return `${location.origin}/catalogo/${this.config.tokenGeneral}`;}
  abrirGeneral(){window.open(this.enlaceGeneral,'_blank','noopener');}
  abrirGeneralMovil(){window.open(`${this.enlaceGeneral}?vista=movil`,'_blank','noopener');}
  abrirPosicion(p:CatalogoPosicion){window.open(`${location.origin}/catalogo/${p.capToken}`,'_blank','noopener');}
  volver(){this.router.navigate(['/'+this.moduloOrigen]);}
  private vacia():CatalogoPosicion{return{capId:null,capFil:1,capCol:1,capUbi:'',capToken:'',capAct:true};}
  private textoError(e:any){return e.error?.detail||e.error?.message||'No se pudo completar la operación.';}
}
