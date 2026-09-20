import {CommonModule} from '@angular/common';
import {Component,ElementRef,HostListener,Input,OnDestroy,OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {forkJoin} from 'rxjs';
import {AvisoEntregado} from '../../interfaces/aviso-alerta.interface';
import {AvisoAlertaService} from '../../services/aviso-alerta.service';
import {MENUS_MODULOS} from '../../config/menu-modulos.config';

@Component({selector:'avisosVentana',standalone:true,imports:[CommonModule],templateUrl:'./avisosVentana.html',styleUrl:'./avisosVentana.css'})
export class AvisosVentana implements OnInit,OnDestroy {
    @Input() integrado=false;
    avisos:AvisoEntregado[]=[];
    ocultos=new Set<string>();
    private panel?:HTMLElement;
    private contenido?:HTMLElement;
    private barra?:HTMLElement;
    private observador?:ResizeObserver;
    private margenOriginal='';
    private margenInicial=0;
    constructor(private router:Router,private api:AvisoAlertaService,private elemento:ElementRef<HTMLElement>){}
    ngOnInit(){const ruta=this.router.url.split('?')[0].split('#')[0];
        if(!MENUS_MODULOS[ruta.split('/')[1]])return;
        const rutas=ruta==='/empleados/agenda'?['/empleados',ruta]:[ruta];
        forkJoin(rutas.map(destino=>this.api.ventana(destino))).subscribe({next:resultados=>{
            this.avisos=resultados.flat();if(this.avisos.length&&!this.integrado)requestAnimationFrame(()=>this.ubicar());
        },error:()=>this.avisos=[]});
    }
    ngOnDestroy(){this.observador?.disconnect();if(this.contenido)this.contenido.style.marginTop=this.margenOriginal;}
    @HostListener('window:resize') alRedimensionar(){this.actualizarPosicion();}
    clave(a:AvisoEntregado){return `${a.empId}/${a.avisoId}/${a.historicoId}`;}
    get hayVisibles(){return this.avisos.some(a=>!this.ocultos.has(this.clave(a)));}
    ocultar(a:AvisoEntregado){this.ocultos.add(this.clave(a));requestAnimationFrame(()=>this.actualizarPosicion());}
    private ubicar(){
        this.barra=this.elemento.nativeElement.closest<HTMLElement>('supbar')||undefined;
        this.contenido=this.barra?.nextElementSibling?.querySelector<HTMLElement>('main.pagina,main.content')||
            this.elemento.nativeElement.ownerDocument.querySelector<HTMLElement>('main.pagina,main.content')||undefined;
        this.panel=this.elemento.nativeElement.querySelector<HTMLElement>('.avisos-ventana')||undefined;
        if(!this.barra||!this.contenido||!this.panel)return;
        this.margenOriginal=this.contenido.style.marginTop;
        this.margenInicial=parseFloat(this.margenOriginal)||0;
        this.observador=new ResizeObserver(()=>this.actualizarPosicion());
        this.observador.observe(this.panel);
        this.observador.observe(this.contenido);
        this.actualizarPosicion();
    }
    private actualizarPosicion(){
        if(!this.barra||!this.contenido||!this.panel)return;
        const barra=this.barra.getBoundingClientRect();
        const contenido=this.contenido.getBoundingClientRect();
        const estilos=getComputedStyle(this.contenido);
        const izquierda=contenido.left-barra.left+(parseFloat(estilos.paddingLeft)||0);
        const derecha=barra.right-contenido.right+(parseFloat(estilos.paddingRight)||0);
        this.panel.style.left=`${Math.max(0,izquierda)}px`;
        this.panel.style.right=`${Math.max(0,derecha)}px`;
        const visible=this.avisos.some(a=>!this.ocultos.has(this.clave(a)));
        const titulo=this.contenido.firstElementChild?.getBoundingClientRect();
        if(!visible||!titulo){this.contenido.style.marginTop=this.margenOriginal;return;}
        const margenActual=parseFloat(getComputedStyle(this.contenido).marginTop)||0;
        const tituloSinAviso=titulo.top-(margenActual-this.margenInicial);
        const tituloDeseado=this.panel.getBoundingClientRect().bottom+16;
        this.contenido.style.marginTop=`${Math.max(this.margenInicial,this.margenInicial+tituloDeseado-tituloSinAviso)}px`;
    }
}
