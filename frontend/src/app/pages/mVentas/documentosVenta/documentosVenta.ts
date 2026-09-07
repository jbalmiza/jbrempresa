import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { TablaEdicion } from '../../../components/tablaEdicion/tablaEdicion';
import { SelectorBusqueda } from '../../../components/selectorBusqueda/selectorBusqueda';
import { TablaColumna } from '../../../directives/tablaColumna/tablaColumna';
import { DocumentacionAdjunta } from '../../../components/documentacionAdjunta/documentacionAdjunta';
import { MallaRegistros } from '../../../components/mallaRegistros/mallaRegistros';
import {
  DocumentoVenta,
  DocumentoVentaDetalle,
  DocumentoVentaMovimiento,
} from '../../../interfaces/documento-venta.interface';
import { Persona } from '../../../interfaces/persona.interface';
import { Domicilio } from '../../../interfaces/domicilio.interface';
import { Producto } from '../../../interfaces/producto.interface';
import { Servicio } from '../../../interfaces/servicio.interface';
import { DocumentoVentaService } from '../../../services/documento-venta.service';
import { PersonaService } from '../../../services/persona.service';
import { DomicilioService } from '../../../services/domicilio.service';
import { ProductoService } from '../../../services/producto.service';
import { ServicioService } from '../../../services/servicio.service';
import { MallaService } from '../../../services/malla.service';
@Component({
  selector: 'DocumentosVenta',
  standalone: true,
  imports:[
    CommonModule,
    FormsModule,
    Sidebar,
    Supbar,
    Tabla,
    TablaEdicion,
    SelectorBusqueda,
    TablaColumna,
    DocumentacionAdjunta,
    MallaRegistros,
    DatosIdentificacion,
  BarraAcciones],
  templateUrl: './documentosVenta.html',
  styleUrl: '../../../styles/estiloGeneral.css',
})
export class DocumentosVenta {
  tipo: 'PRE' | 'PED' | 'ALB' | 'FAC';
  nombre = '';
  modoGestion = false;
  vista: 'tabla' | 'registro' | 'adjuntos' | 'historico' | 'malla' = 'tabla';
  modo: 'insertar' | 'modificar' | 'ver' = 'insertar';
  datos: DocumentoVenta[] = [];
  datosHistorico: DocumentoVentaMovimiento[] = [];
  seleccionado: DocumentoVenta | null = null;
  documento!: DocumentoVenta;
  personas: Persona[] = [];
  domicilios: Domicilio[] = [];
  personaSeleccionada: Persona | null = null;
  productos: Producto[] = [];
  servicios: Servicio[] = [];
  conceptos: any[] = [];
  columnas = [
    'empId',
    'dovId',
    'dovNum',
    'personaNomCom',
    'dovFec',
    'dovEst',
    'dovOri',
    'dovMod',
    'dovUbi',
    'dovIdOri',
    'dovIdRai',
    'dovImpSub',
    'dovImpDes',
    'dovImpIva',
    'dovImpTot',
    'dovUsuMov',
    'dovFecMov',
  ];
  titulos: any = {
    empId: 'Empresa',
    dovId: 'Id',
    dovNum: 'Número',
    personaNomCom: 'Persona',
    dovFec: 'Fecha',
    dovEst: 'Estado',
    dovOri: 'Origen',
    dovMod: 'Modalidad',
    dovUbi: 'Ubicación',
    dovIdOri: 'Documento Origen',
    dovIdRai: 'Cadena',
    dovImpSub: 'Subtotal',
    dovImpDes: 'Descuento',
    dovImpIva: 'IVA',
    dovImpTot: 'Total',
    dovUsuMov: 'Usuario',
    dovFecMov: 'Fecha Movimiento',
  };
  columnasDetalle = [
    'dvdTipLin',
    'referencia',
    'dvdNom',
    'dvdObs',
    'dvdCan',
    'dvdPre',
    'dvdDes',
    'dvdIva',
    'dvdImp',
    'dvdDurTot',
    'accion',
  ];
  titulosDetalle: any = {
    dvdTipLin: 'Tipo',
    referencia: 'Producto / Servicio',
    dvdNom: 'Descripción',
    dvdObs: 'Observaciones',
    dvdCan: 'Cantidad',
    dvdPre: 'Precio',
    dvdDes: '% Descuento',
    dvdIva: '% IVA',
    dvdImp: 'Importe',
    dvdDurTot: 'Duración',
    accion: 'Acción',
  };
  columnasHistorico = ['dvmId','dovNum','dvmTipMov','dvmCauMov','dovEst','dovImpTot','dvmUsuMov','dvmFecMov'];
  titulosHistorico: any = {dvmId:'Movimiento',dovNum:'Número',dvmTipMov:'Tipo movimiento',dvmCauMov:'Causa',dovEst:'Estado',dovImpTot:'Total',dvmUsuMov:'Usuario',dvmFecMov:'Fecha'};
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: DocumentoVentaService,
    private personaService: PersonaService,
    private domicilioService: DomicilioService,
    private productoService: ProductoService,
    private servicioService: ServicioService,
    private mallaService: MallaService,
  ) {
    this.tipo = this.route.snapshot.data['tipo'];
    this.modoGestion = this.route.snapshot.data['modoGestion'] === true;
    this.nombre = { PRE: 'PRESUPUESTOS', PED: 'PEDIDOS', ALB: 'ALBARANES', FAC: 'FACTURAS' }[
      this.tipo
    ];
    this.documento = this.vacio();
    this.cargarAuxiliares();
    this.consultar();
    if (this.modoGestion && this.tipo === 'PED') this.malla();
  }
  consultar() {
    this.vista = 'tabla';
    this.seleccionado = null;
    this.service
      .consultar(this.tipo, this.modoGestion)
      .subscribe({ next: (r) => (this.datos = this.enriquecerPersonas(r)), error: (e) => this.error(e) });
  }
  malla() { this.vista = 'malla'; this.seleccionado = null; }
  obtenerMallas = () => this.mallaService.obtenerMallas();
  obtenerPedidos = () => this.service.consultar('PED', true);
  formatearPedidoMalla = (pedido: DocumentoVenta) => `${pedido.dovNum} · ${pedido.personaNomCom || this.nombrePersona(pedido.perId)} · ${pedido.dovEst}`;
  abrirPedidoDesdeMalla(registro: any) {
    this.seleccionado = this.datos.find(d => Number(d.dovId) === Number(registro.malRefId)) || null;
    if (this.seleccionado) this.ver();
  }
  insertar() {
    this.modo = 'insertar';
    this.documento = this.vacio();
    this.vista = 'registro';
  }
  ver() {
    if (!this.seleccionado) return;
    this.modo = 'ver';
    this.cargarDocumentoSeleccionado();
  }
  modificar() {
    if (!this.seleccionado || this.seleccionado.dovEst === 'CONVERTIDO') return;
    this.modo = 'modificar';
    this.cargarDocumentoSeleccionado();
  }
  private cargarDocumentoSeleccionado() {
    if (!this.seleccionado) return;
    this.documento = {
      ...this.seleccionado,
      detalles: (this.seleccionado.detalles || []).map((l) => ({
        ...l,
        referencia: l.dvdTipLin === 'S' ? -(l.serId || 0) : l.proId || 0,
      })),
    };
    this.seleccionarPersona(this.documento.perId);
    this.vista = 'registro';
  }
  guardar() {
    if (!this.documento.perId || !this.documento.detalles.length) {
      avisarAplicacion('Cliente y al menos una línea son obligatorios.');
      return;
    }
    if (this.documento.detalles.some((linea) => !linea.dvdTipLin || (!linea.proId && !linea.serId))) {
      avisarAplicacion('Seleccione un producto o servicio en todas las líneas.');
      return;
    }
    const op =
      this.modo === 'insertar'
        ? this.service.guardar(this.tipo, this.documento)
        : this.service.actualizar(this.tipo, this.documento);
    op.subscribe({ next: () => this.consultar(), error: (e) => this.error(e) });
  }
  async eliminar() {
    if (!this.seleccionado || !await confirmarAplicacion('¿Eliminar únicamente este documento?',true)) return;
    this.service
      .eliminar(this.tipo, this.seleccionado.dovId)
      .subscribe({ next: () => this.consultar(), error: (e) => this.error(e) });
  }
  async eliminarCompleto() {
    if (!this.seleccionado || !await confirmarAplicacion('¿Eliminar toda la cadena documental relacionada?',true)) return;
    this.service
      .eliminarCompleto(this.tipo, this.seleccionado.dovId)
      .subscribe({ next: () => this.consultar(), error: (e) => this.error(e) });
  }
  async baja() {
    if (!this.seleccionado || !this.seleccionado.dovAct || !await confirmarAplicacion('¿Dar de baja el documento seleccionado?',true)) return;
    this.service.baja(this.tipo, this.seleccionado.dovId).subscribe({next:()=>this.consultar(),error:e=>this.error(e)});
  }
  async reactivar() {
    if (!this.seleccionado || this.seleccionado.dovAct || !await confirmarAplicacion('¿Reactivar el documento seleccionado?')) return;
    this.service.reactivar(this.tipo, this.seleccionado.dovId).subscribe({next:()=>this.consultar(),error:e=>this.error(e)});
  }
  adjuntos() { if (this.seleccionado) this.vista = 'adjuntos'; }
  historico() {
    if (!this.seleccionado) return;
    this.service.historico(this.tipo,this.seleccionado.dovId).subscribe({next:r=>{this.datosHistorico=r;this.vista='historico';},error:e=>this.error(e)});
  }
  async convertir() {
    if (
      !this.seleccionado ||
      !await confirmarAplicacion(`¿Convertir este ${this.nombre.toLowerCase().slice(0, -1)} al siguiente documento?`)
    )
      return;
    this.service.convertir(this.tipo, this.seleccionado.dovId).subscribe({
      next: (d) => {
        avisarAplicacion(`Documento ${d.dovNum} creado correctamente.`);
        this.consultar();
      },
      error: (e) => this.error(e),
    });
  }
  insertarLinea() {
    this.documento.detalles.push({
      dvdId: 0,
      dovId: 0,
      dvdTipLin: '',
      proId: null,
      serId: null,
      dvdNom: '',
      dvdCan: 1,
      dvdPre: 0,
      dvdDes: 0,
      dvdIva: 0,
      dvdImp: 0,
      dvdDurUni: 0,
      dvdDurTot: 0,
      referencia: 0,
    });
  }
  eliminarLinea(i: number) {
    this.documento.detalles.splice(i, 1);
    this.totales();
  }
  seleccionarConcepto(l: DocumentoVentaDetalle, ref: number) {
    const c = this.conceptos.find((x) => x.ref === ref);
    if (!c) return;
    l.referencia = ref;
    l.dvdTipLin = c.tipo;
    l.proId = c.tipo === 'P' ? c.id : null;
    l.serId = c.tipo === 'S' ? c.id : null;
    l.dvdNom = c.nombre;
    l.dvdPre = c.precio;
    l.dvdDes = c.descuento;
    l.dvdIva = c.iva;
    l.dvdDurUni = c.duracion;
    this.linea(l);
  }
  linea(l: DocumentoVentaDetalle) {
    const base = (l.dvdCan || 0) * (l.dvdPre || 0);
    const descuento = (base * (l.dvdDes || 0)) / 100;
    const neto = base - descuento;
    l.dvdImp = this.redondear(neto + (neto * (l.dvdIva || 0)) / 100);
    l.dvdDurTot = (l.dvdCan || 0) * (l.dvdDurUni || 0);
    this.totales();
  }
  volver() {
    this.router.navigate(['/ventas']);
  }
  private totales() {
    let sub = 0,
      des = 0,
      iva = 0,
      total = 0;
    for (const l of this.documento.detalles) {
      const base = l.dvdCan * l.dvdPre;
      const d = (base * l.dvdDes) / 100;
      const n = base - d;
      const i = (n * l.dvdIva) / 100;
      sub += base;
      des += d;
      iva += i;
      total += n + i;
    }
    Object.assign(this.documento, {
      dovImpSub: this.redondear(sub),
      dovImpDes: this.redondear(des),
      dovImpIva: this.redondear(iva),
      dovImpTot: this.redondear(total),
    });
  }
  private cargarAuxiliares() {
    this.personaService.obtenerPersonas().subscribe({next:(r) => {this.personas = r.filter((p: Persona) => p.perTipMov !== 'B');this.datos=this.enriquecerPersonas(this.datos);},error:(e)=>this.error(e)});
    this.domicilioService.obtenerDomicilios().subscribe((r) => (this.domicilios = r.filter((d: Domicilio) => d.domTipMov !== 'B')));
    this.productoService.obtenerProductos().subscribe({next:(r) => {
      this.productos = r;
      this.construirConceptos();
    },error:(e)=>this.error(e)});
    this.servicioService.consultar().subscribe({next:(r) => {
      this.servicios = r;
      this.construirConceptos();
    },error:(e)=>this.error(e)});
  }
  private construirConceptos() {
    this.conceptos = [
      ...this.productos
        .filter((p) => p.proTipMov !== 'B')
        .map((p) => ({
          ref: p.proId,
          tipo: 'P',
          id: p.proId,
          etiqueta: `Producto · ${p.proNom}`,
          nombre: p.proNom,
          precio: p.proPreVen || 0,
          descuento: p.proPreDes || 0,
          iva: p.proPreIva || 0,
          duracion: p.proDurMin || 0,
        })),
      ...this.servicios
        .filter((s) => s.serTipMov !== 'B')
        .map((s) => ({
          ref: -s.serId,
          tipo: 'S',
          id: s.serId,
          etiqueta: `Servicio · ${s.serNom}`,
          nombre: s.serNom,
          precio: s.serPreVen || 0,
          descuento: s.serPreDes || 0,
          iva: s.serPreIva || 0,
          duracion: s.serDurMin,
        })),
    ];
  }
  private vacio(): DocumentoVenta {
    return {
      dovId: 0,
      empId: Number(localStorage.getItem('empresaId')) || 0,
      dovTip: this.tipo,
      dovNum: '',
      perId: 0,
      dovFec: new Date().toISOString().slice(0, 10),
      dovEst: 'BORRADOR',
      dovIdOri: null,
      dovIdRai: null,
      dovUbi: '',
      dovOri: 'INTERNO',
      dovMod: 'EN_POSICION',
      dovDirEnv: '',
      dovFilMal: null,
      dovColMal: null,
      dovImpSub: 0,
      dovImpDes: 0,
      dovImpIva: 0,
      dovImpTot: 0,
      dovObs: '',
      dovUsuMov: localStorage.getItem('usuario') || '',
      dovFecMov: '',
      dovAct: true,
      detalles: [],
    };
  }
  seleccionarPersona(id: number) {
    this.documento.perId = id;
    this.personaSeleccionada = this.personas.find((p) => Number(p.perId) === Number(id)) || null;
  }
  private nombrePersona(id: number): string { return this.personas.find(p => Number(p.perId) === Number(id))?.perNomCom || ''; }
  private enriquecerPersonas(documentos: DocumentoVenta[]): DocumentoVenta[] { return documentos.map(documento => ({...documento, personaNomCom: this.nombrePersona(documento.perId)})); }
  get direccionPostal() {
    return this.domicilios.find((d) => Number(d.domId) === Number(this.personaSeleccionada?.domId))?.domDir || '';
  }
  importe(valor: number): string {
    return this.redondear(valor || 0).toFixed(2);
  }
  private redondear(valor: number): number {
    return Math.round((valor + Number.EPSILON) * 100) / 100;
  }
  private error(e: any) {
    console.error(e);
    avisarAplicacion(e?.error?.detail || e?.error?.message || 'No se pudo completar la operación.');
  }
}
