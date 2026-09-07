import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Supbar } from '../../components/supbar/supbar';

@Component({selector:'AccesoModulos',standalone:true,imports:[CommonModule,Supbar],templateUrl:'./accesoModulos.html',styleUrl:'./accesoModulos.css'})
export class AccesoModulos {
  modulos=[
    {nombre:'Administración',ruta:'administracion',icono:'assets/modulos/mAdministracion.svg',descripcion:'Empresas, usuarios, perfiles y configuración.'},
    {nombre:'Comunicaciones',ruta:'comunicaciones',icono:'assets/modulos/mComunicaciones.svg',descripcion:'Bandeja, conversaciones y atención omnicanal.'},
    {nombre:'Territorio',ruta:'territorio',icono:'assets/modulos/mTerritorio.svg',descripcion:'Domicilios y estructura territorial.'},
    {nombre:'Personas',ruta:'personas',icono:'assets/modulos/mPersonas.svg',descripcion:'Personas, representantes y datos relacionados.'},
    {nombre:'Productos',ruta:'productos',icono:'assets/modulos/mProductos.svg',descripcion:'Catálogo y gestión de productos.'},
    {nombre:'Servicios',ruta:'servicios',icono:'assets/modulos/mServicios.svg',descripcion:'Catálogo, duración y gestión de servicios.'},
    {nombre:'Compras',ruta:'compras',icono:'assets/modulos/mCompras.svg',descripcion:'Compras y relación con proveedores.'},
    {nombre:'Ventas',ruta:'ventas',icono:'assets/modulos/mVentas.svg',descripcion:'Operaciones comerciales y detalle de ventas.'},
    {nombre:'Recursos',ruta:'recursos',icono:'assets/modulos/mRecursos.svg',descripcion:'Empleados, maquinaria y capacidades operativas.'},
    {nombre:'Caja',ruta:'caja',icono:'assets/modulos/mCaja.svg',descripcion:'Aperturas, cierres y movimientos de caja.'}
  ];
  constructor(private router:Router){}
  abrirModulo(modulo:string){const url=this.router.serializeUrl(this.router.createUrlTree(['/'+modulo]));window.open(url,'_blank');}
}
