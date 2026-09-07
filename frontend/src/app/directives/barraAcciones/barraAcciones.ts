import { AfterViewInit, Directive, ElementRef, OnDestroy } from '@angular/core';

/** Normaliza cualquier barra de acciones sin apropiarse de la lógica de negocio. */
@Directive({ selector: '[barraAcciones]', standalone: true, host: {'role':'toolbar','aria-label':'Acciones disponibles'} })
export class BarraAcciones implements AfterViewInit,OnDestroy {
  private observador?:MutationObserver;
  private readonly orden=['consultar','ver','insertar','modificar','eliminar','baja','reactivar','capacidades','agenda','historico','deshacer','guardar','actualizar','cancelar','volver'];
  constructor(private elemento:ElementRef<HTMLElement>){}
  ngAfterViewInit(){this.ordenar();this.observador=new MutationObserver(()=>this.ordenar());this.observador.observe(this.elemento.nativeElement,{childList:true,subtree:true});}
  ngOnDestroy(){this.observador?.disconnect();}
  private ordenar(){for(const boton of Array.from(this.elemento.nativeElement.querySelectorAll<HTMLElement>('button'))){const indice=this.orden.findIndex(a=>boton.classList.contains(`accion-${a}`));boton.style.order=String(indice<0?50:indice);}}
}
