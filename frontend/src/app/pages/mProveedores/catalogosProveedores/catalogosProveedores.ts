import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Supbar } from '../../../components/supbar/supbar';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { CatalogoPublico } from '../../catalogoPublico/catalogoPublico';
import { CatalogoService } from '../../../services/catalogo.service';
import { DialogosService } from '../../../core/interaccion/dialogos.service';

@Component({selector:'catalogos-proveedores',standalone:true,
  imports:[CommonModule,FormsModule,Supbar,Sidebar,CatalogoPublico],
  templateUrl:'./catalogosProveedores.html',styleUrls:['../../../styles/estiloGeneral.css']})
export class CatalogosProveedores implements OnInit {
  proveedores:{id:number;nombre:string}[]=[];
  proveedorId=0;
  constructor(private api:CatalogoService,private dialogos:DialogosService,private router:Router){}
  ngOnInit(){this.api.proveedores().subscribe({next:r=>{
    this.proveedores=r;
    if(r.length===1)this.proveedorId=r[0].id;
    if(!r.length)this.dialogos.avisar('La empresa no tiene relaciones activas con proveedores.');
  },error:e=>this.dialogos.error(e)});}
  volver(){this.router.navigate(['/accesoModulos']);}
}
