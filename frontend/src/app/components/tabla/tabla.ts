import {avisarAplicacion,confirmarAplicacion} from '../../core/interaccion/dialogos.service';
import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges
} from '@angular/core';
import { HostListener } from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms';
import { ConfiguracionTablaService } from '../../services/configuracion-tabla.service';
import { BlobUrlUtil } from '../../shared/utils/blob-url.util';

interface ColumnaConfig { campo:string;visible:boolean; }

@Component({
  selector: 'tabla',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tabla.html',
  styleUrls: ['./tabla.css']
})
export class Tabla implements OnChanges {
  private readonly blobs=new BlobUrlUtil();

  // Columnas dinámicas
  @Input() columnas: string[] = [];
  @Input() claveConfiguracion = '';
  @Input() titulo = '';
  @Input() permitirImportar = false;
  @Input() permitirExportar = false;
  /** Muestra también movimientos B. Se usa en las vistas de histórico. */
  @Input() incluirBajas = false;
  @Output() importarSolicitado = new EventEmitter<void>();
  @Output() exportarSolicitado = new EventEmitter<void>();
  
  // Relación campo → título mostrado en la cabecera de la tabla
  @Input() titulosColumnas: { [key: string]: string } = {};

  // Activa la paginación y el filtrado gestionados por el servidor.
  @Input() consultaRemota = false;

  // Estado y datos de paginación recibidos del servidor.
  @Input() cargando = false;
  @Input() totalRegistrosRemotos = 0;
  @Input() paginaRemota = 1;

  // Solicita una consulta remota al componente que conoce el recurso.
  @Output() consultaSolicitada = new EventEmitter<{
    pagina: number;
    tamanio: number;
  }>();

  // Datos privados
  private _datos: any[] = [];

  // Datos dinámicos
  @Input()
  set datos(value: any[]) {

	this._datos = value || [];

    if (this.consultaRemota) {
      // El servidor ya devuelve únicamente la página solicitada.
      this.datosFiltrados = [...this._datos];
      this.datosPagina = [...this._datos];
      this.calcularPaginas();
      return;
    }

    if (this.columnasOriginales.length) this.filtrar();
    else {
      this.datosFiltrados = [...this._datos];
      this.paginaActual = 1;
      this.calcularPaginas();
      this.actualizarPagina();
    }

  }

  get datos(): any[] {

    return this._datos;

  }

  // Envía la fila seleccionada al componente padre
  @Output() filaSeleccionada = new EventEmitter<any>();

  // Guarda la fila seleccionada
  filaActiva: any = null;

  // Filtros por columna
  filtros: any = {};

  // Datos filtrados
  datosFiltrados: any[] = [];

  // Datos de la página actual
  datosPagina: any[] = [];

  // Paginación
  paginaActual = 1;

  registrosPorPagina = 10;

  totalPaginas = 1;

  columnasOriginales: string[] = [];
  columnasVisibles: string[] = [];
  configuracionAplicada: ColumnaConfig[] = [];
  configuracionEdicion: ColumnaConfig[] = [];
  configurando = false;
  guardandoConfiguracion = false;
  private claveCargada = '';
  anchosColumnas: Record<string, number> = {};
  private redimensionando: { columna:string; inicioX:number; anchoInicial:number } | null = null;

  constructor(private configuracionService:ConfiguracionTablaService) {}

  // Selecciona una fila
  seleccionarFila(fila: any) {

    this.filaActiva = fila;

    this.filaSeleccionada.emit(fila);

  }

  // Filtra los registros
  filtrar() {

    // En modo remoto, el filtro se aplica al confirmar con Intro.
    if (this.consultaRemota) {
      return;
    }

    const columnaMovimiento = this.columnaTipoMovimiento();
    const buscandoBajas = columnaMovimiento
      ? String(this.filtros[columnaMovimiento] ?? '').trim().toUpperCase() === 'B'
      : false;

    this.datosFiltrados = this.datos.filter(fila => {

      if (!this.incluirBajas && columnaMovimiento && !buscandoBajas && String(fila[columnaMovimiento] ?? '').toUpperCase() === 'B') {
        return false;
      }

      return this.columnasVisibles.every(columna => {

        const filtro = this.filtros[columna];

        if (!filtro) {

          return true;

        }

        const valor = fila[columna];

        return String(valor ?? '')
          .toLowerCase()
          .includes(
            String(filtro).toLowerCase()
          );

      });

    });

    this.paginaActual = 1;

    this.calcularPaginas();

    this.actualizarPagina();

  }

  // Calcula el total de páginas
  calcularPaginas() {

    if (this.consultaRemota) {
      this.totalPaginas = Math.max(
        1,
        Math.ceil(this.totalRegistrosRemotos / this.registrosPorPagina)
      );

      return;
    }

    this.totalPaginas = Math.max(
      1,
      Math.ceil(
        this.datosFiltrados.length /
        this.registrosPorPagina
      )
    );

  }

  // Actualiza los registros mostrados
  actualizarPagina() {

    const inicio =
      (this.paginaActual - 1) *
      this.registrosPorPagina;

    const fin =
      inicio +
      Number(this.registrosPorPagina);

    this.datosPagina =
      this.datosFiltrados.slice(
        inicio,
        fin
      );

  }

  // Primera página
  primeraPagina() {

    if (this.consultaRemota) {
      this.solicitarConsulta(1);
      return;
    }

    this.paginaActual = 1;

    this.actualizarPagina();

  }

  // Página anterior
  paginaAnterior() {

    if (this.consultaRemota) {
      if (this.paginaRemota > 1) {
        this.solicitarConsulta(this.paginaRemota - 1);
      }
      return;
    }

    if (this.paginaActual > 1) {

      this.paginaActual--;

      this.actualizarPagina();

    }

  }

  // Página siguiente
  paginaSiguiente() {

    if (this.consultaRemota) {
      if (this.paginaRemota < this.totalPaginas) {
        this.solicitarConsulta(this.paginaRemota + 1);
      }
      return;
    }

    if (this.paginaActual < this.totalPaginas) {

      this.paginaActual++;

      this.actualizarPagina();

    }

  }

  // Última página
  ultimaPagina() {

    if (this.consultaRemota) {
      this.solicitarConsulta(this.totalPaginas);
      return;
    }

    this.paginaActual = this.totalPaginas;

    this.actualizarPagina();

  }

  // Cambio de registros por página
  cambiarRegistrosPorPagina() {

    this.registrosPorPagina =
      Number(this.registrosPorPagina);

    if (this.consultaRemota) {
      this.solicitarConsulta(1);
      return;
    }

    this.paginaActual = 1;

    this.calcularPaginas();

    this.actualizarPagina();

  }

  ngOnChanges(changes: SimpleChanges) {

    if (changes['columnas'] || changes['claveConfiguracion']) {
      this.prepararColumnas();
    }

    if (this.consultaRemota &&
        (changes['totalRegistrosRemotos'] || changes['paginaRemota'])) {

      this.calcularPaginas();
      this.actualizarPagina();

    }

  }

  abrirConfiguracion() {
    this.configuracionEdicion = (this.configuracionAplicada.length
      ? this.configuracionAplicada
      : this.configuracionPredeterminada())
      .map(x=>({...x}));
    this.configurando = true;
  }

  cerrarConfiguracion() { this.configurando = false; }

  moverColumna(indice:number,direccion:number) {
    const destino=indice+direccion;
    if(destino<0||destino>=this.configuracionEdicion.length)return;
    [this.configuracionEdicion[indice],this.configuracionEdicion[destino]]=[this.configuracionEdicion[destino],this.configuracionEdicion[indice]];
  }

  restaurarConfiguracion() {
    this.configuracionEdicion=this.configuracionPredeterminada();
  }

  guardarConfiguracion() {
    if(!this.configuracionEdicion.some(x=>x.visible)){avisarAplicacion('Debe mantenerse visible al menos una columna.');return;}
    const clave=this.obtenerClave();this.guardandoConfiguracion=true;
    this.configuracionService.guardar({cotCla:clave,cotCon:JSON.stringify(this.configuracionEdicion)}).subscribe({
      next:()=>{this.aplicarConfiguracion(this.configuracionEdicion);this.guardandoConfiguracion=false;this.configurando=false;},
      error:e=>{console.error(e);this.guardandoConfiguracion=false;avisarAplicacion('No se pudo guardar la configuración de la tabla.');}
    });

    if (this.filaActiva && !this.datosFiltrados.includes(this.filaActiva)) {
      this.filaActiva = null;
      this.filaSeleccionada.emit(null);
    }
  }

  tituloColumna(campo:string){return this.titulosColumnas[campo]||campo;}

  tieneTipoMovimiento(){return this.columnaTipoMovimiento() !== null;}

  iniciarRedimension(evento:MouseEvent,columna:string,celda:HTMLElement){
    evento.preventDefault();
    evento.stopPropagation();
    this.redimensionando={columna,inicioX:evento.clientX,anchoInicial:celda.getBoundingClientRect().width};
  }

  @HostListener('document:mousemove',['$event'])
  redimensionar(evento:MouseEvent){
    if(!this.redimensionando)return;
    this.anchosColumnas[this.redimensionando.columna]=Math.max(80,this.redimensionando.anchoInicial+evento.clientX-this.redimensionando.inicioX);
  }

  @HostListener('document:mouseup')
  terminarRedimension(){this.redimensionando=null;}

  exportarTabla(){
    if(this.exportarSolicitado.observed){this.exportarSolicitado.emit();return;}
    const columnas=this.columnasVisibles;
    const escapar=(valor:any)=>`"${String(valor??'').replace(/"/g,'""')}"`;
    const lineas=[columnas.map(c=>escapar(this.tituloColumna(c))).join(';'),...this.datosFiltrados.map(f=>columnas.map(c=>escapar(f[c])).join(';'))];
    const blob=new Blob(['\uFEFF'+lineas.join('\r\n')],{type:'text/csv;charset=utf-8'});
    this.blobs.descargar(blob,`${(this.titulo||'tabla').toLowerCase().replace(/[^a-z0-9]+/gi,'-')}.csv`);
  }

  private prepararColumnas() {
    this.columnasOriginales=[...new Set(this.columnas||[])];
    if(!this.columnasVisibles.length)this.aplicarPredeterminada();
    const clave=this.obtenerClave();
    if(!clave||clave===this.claveCargada)return;
    this.claveCargada=clave;
    this.configuracionService.obtener(clave).subscribe({next:v=>{
      if(!v?.cotCon){this.aplicarPredeterminada();return;}
      try{this.aplicarConfiguracion(JSON.parse(v.cotCon));}catch{this.aplicarPredeterminada();}
    },error:()=>this.aplicarPredeterminada()});
  }

  private aplicarConfiguracion(configuracion:ColumnaConfig[]) {
    const validas=(Array.isArray(configuracion)?configuracion:[]).filter(x=>x&&this.columnasOriginales.includes(x.campo));
    const conocidas=new Set(validas.map(x=>x.campo));
    this.columnasOriginales.filter(x=>!conocidas.has(x)).forEach(campo=>validas.push({campo,visible:this.visiblePorDefecto(campo)}));
    this.configuracionAplicada=validas.map(x=>({...x}));
    this.columnasVisibles=validas.filter(x=>x.visible).map(x=>x.campo);
    Object.keys(this.filtros).filter(x=>!this.columnasVisibles.includes(x)).forEach(x=>delete this.filtros[x]);
    this.filtrar();
  }

  private obtenerClave(){const base=this.claveConfiguracion?.trim()||`${window.location.pathname}|${this.columnasOriginales.join(',')}`;return base.substring(0,500);}
  private visiblePorDefecto(campo:string){const nombre=campo.toLowerCase();return nombre!=='cliid'&&!nombre.endsWith('idhis');}
  private configuracionPredeterminada(){return this.columnasOriginales.map(campo=>({campo,visible:this.visiblePorDefecto(campo)}));}
  private aplicarPredeterminada(){this.aplicarConfiguracion(this.configuracionPredeterminada());}
  private columnaTipoMovimiento(){return this.columnasOriginales.find(c=>c.toLowerCase().endsWith('tipmov'))||null;}

  // Envía los filtros actuales para consultar una página en el servidor.
  solicitarConsulta(pagina = 1) {

    this.consultaSolicitada.emit({
      pagina,
      tamanio: Number(this.registrosPorPagina)
    });

  }

}
