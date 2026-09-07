import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { ExcepcionAgenda, HorarioAgenda, RecursoAgenda, ReservaAgenda } from '../../../interfaces/agenda.interface';
import { AgendaService } from '../../../services/agenda.service';
import { RecursoService } from '../../../services/recurso.service';
import { ConfiguracionAgenda } from '../../../components/configuracionAgenda/configuracionAgenda';
import { avisarAplicacion } from '../../../core/interaccion/dialogos.service';

interface AgendaEmpleado {
  recurso: RecursoAgenda;
  horarios: HorarioAgenda[];
  excepciones: ExcepcionAgenda[];
  configurada: boolean;
}

interface TramoAgenda {
  minutos: number;
  etiqueta: string;
}

interface SeleccionAgenda {
  empleado: AgendaEmpleado;
  inicio: number;
  fin: number;
  estado: 'DISPONIBLE' | 'OCUPADO' | 'PREPARACION' | 'LIMPIEZA' | 'NO_DISPONIBLE';
  reservas: ReservaAgenda[];
  detalle: string;
}

@Component({
  selector: 'gestionAgendas',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar, ConfiguracionAgenda],
  templateUrl: './gestionAgendas.html',
  styleUrls: ['../../../styles/estiloGeneral.css', './gestionAgendas.css', './gestionAgendasConfig.css']
})
export class GestionAgendas implements OnInit {
  fecha = this.fechaLocal(new Date());
  intervalo: 5 | 15 | 30 | 60 = 30;
  buscar = '';
  cargando = false;
  mensaje = '';
  agendas: AgendaEmpleado[] = [];
  recursosConfiguracion: RecursoAgenda[] = [];
  reservas: ReservaAgenda[] = [];
  paginaEmpleados = 0;
  readonly empleadosPorPagina = 6;
  seleccion: SeleccionAgenda | null = null;
  panel: 'calendario' | 'configuracion' = 'calendario';

  constructor(private agendaService: AgendaService, private recursoService: RecursoService) {}

  ngOnInit(): void {
    this.cargar();
  }

  get agendasFiltradas(): AgendaEmpleado[] {
    const texto = this.buscar.trim().toLocaleLowerCase('es-ES');
    return texto
      ? this.agendas.filter(a => a.recurso.ragNom.toLocaleLowerCase('es-ES').includes(texto))
      : this.agendas;
  }

  get agendasVisibles(): AgendaEmpleado[] {
    const inicio = this.paginaEmpleados * this.empleadosPorPagina;
    return this.agendasFiltradas.slice(inicio, inicio + this.empleadosPorPagina);
  }

  get totalPaginas(): number {
    return Math.max(1, Math.ceil(this.agendasFiltradas.length / this.empleadosPorPagina));
  }

  get tramos(): TramoAgenda[] {
    const horarios = this.agendasVisibles.flatMap(a => a.horarios.filter(h => h.horDia === this.diaSemana));
    const reservas = this.reservas.filter(r => this.agendasVisibles.some(a => a.recurso.ragId === r.ragId));
    const horasVisuales = this.agendasVisibles.map(a => this.minutos(a.recurso.ragHorVis || '08:00'));
    const inicioVisual = horasVisuales.length ? Math.min(...horasVisuales) : 480;
    const finalesHorario = this.agendasVisibles.flatMap(agenda => agenda.horarios
      .filter(h => h.horDia === this.diaSemana)
      .map(h => this.minutos(h.horFin)));
    const inicios = [inicioVisual, ...horarios.map(h => this.minutos(h.horIni))];
    const finales = [Math.min(1440, inicioVisual + 240), ...finalesHorario, ...reservas.map(r => this.minutos(r.resFin.slice(11, 16)))];
    const inicio = Math.max(0, Math.floor(Math.min(...inicios) / this.intervalo) * this.intervalo);
    const fin = Math.min(1440, Math.ceil(Math.max(...finales) / this.intervalo) * this.intervalo);
    return Array.from({ length: Math.ceil((fin - inicio) / this.intervalo) }, (_, indice) => {
      const minutos = inicio + indice * this.intervalo;
      return { minutos, etiqueta: this.hora(minutos) };
    });
  }

  get diaSemana(): number {
    const dia = this.fechaComoDate().getDay();
    return dia === 0 ? 7 : dia;
  }

  get fechaLarga(): string {
    const texto = new Intl.DateTimeFormat('es-ES', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' }).format(this.fechaComoDate());
    return texto.charAt(0).toUpperCase() + texto.slice(1);
  }

  cargar(): void {
    this.cargando = true;
    this.mensaje = '';
    this.seleccion = null;
    const desde = `${this.fecha}T00:00:00`;
    const fechaHasta = this.fechaComoDate();
    fechaHasta.setDate(fechaHasta.getDate() + 1);
    const hasta = `${this.fechaLocal(fechaHasta)}T00:00:00`;

    forkJoin({
      recursosAgenda: this.agendaService.recursos(),
      empleadosRegistrados: this.recursoService.consultar(),
      reservas: this.agendaService.reservas(desde, hasta)
    }).subscribe({
      next: ({ recursosAgenda, empleadosRegistrados, reservas }) => {
        const agendasPorEmpleado = new Map(recursosAgenda.filter(r => r.ragTip === 'EMPLEADO').map(r => [r.ragRefId, r]));
        const empleados = empleadosRegistrados
          .filter(e => e.reoTip === 'EMPLEADO' && e.reoAct && e.reoTipMov !== 'B' && e.reoId)
          .map(e => agendasPorEmpleado.get(e.reoId!) ?? ({
            ragId: -e.reoId!, ragTip: 'EMPLEADO', ragRefId: e.reoId!, ragNom: e.reoNom,
            ragCap: 1, ragMarPre: 0, ragMarPos: 0, ragHorVis: '08:00'
          } as RecursoAgenda));
        this.reservas = reservas;
        if (!empleados.length) {
          this.agendas = [];
          this.recursosConfiguracion = [];
          this.cargando = false;
          return;
        }
        forkJoin(empleados.map(recurso => recurso.ragId > 0 ? forkJoin({
          horarios: this.agendaService.horarios(recurso.ragId).pipe(catchError(() => of([] as HorarioAgenda[]))),
          excepciones: this.agendaService.excepciones(recurso.ragId, this.fecha, this.fecha).pipe(catchError(() => of([] as ExcepcionAgenda[]))),
          configurada: of(true)
        }) : of({ horarios: [] as HorarioAgenda[], excepciones: [] as ExcepcionAgenda[], configurada: false }))).subscribe({
          next: configuraciones => {
            this.agendas = empleados.map((recurso, indice) => ({ recurso, ...configuraciones[indice] }))
              .sort((a, b) => a.recurso.ragNom.localeCompare(b.recurso.ragNom, 'es'));
            this.recursosConfiguracion = this.agendas.map(agenda => agenda.recurso);
            this.ajustarPagina();
            this.cargando = false;
          },
          error: error => this.mostrarError(error)
        });
      },
      error: error => this.mostrarError(error)
    });
  }

  cambiarFecha(dias: number): void {
    const fecha = this.fechaComoDate();
    fecha.setDate(fecha.getDate() + dias);
    this.fecha = this.fechaLocal(fecha);
    this.cargar();
  }

  hoy(): void {
    this.fecha = this.fechaLocal(new Date());
    this.cargar();
  }

  cambiarIntervalo(intervalo: 5 | 15 | 30 | 60): void {
    this.intervalo = intervalo;
    this.seleccion = null;
  }

  abrirConfiguracion(): void {
    this.seleccion = null;
    this.panel = 'configuracion';
  }

  configuracionGuardada(): void {
    this.panel = 'calendario';
    this.cargar();
    avisarAplicacion('Configuración global de agendas guardada correctamente.');
    window.scrollTo({ top: 0, behavior: 'auto' });
    requestAnimationFrame(() => document.querySelector('.titulo-pagina')?.scrollIntoView({ block: 'start' }));
  }

  filtrar(): void {
    this.paginaEmpleados = 0;
    this.seleccion = null;
  }

  moverEmpleados(direccion: -1 | 1): void {
    this.paginaEmpleados = Math.min(this.totalPaginas - 1, Math.max(0, this.paginaEmpleados + direccion));
    this.seleccion = null;
  }

  reservasTramo(agenda: AgendaEmpleado, inicio: number): ReservaAgenda[] {
    const fin = inicio + this.intervalo;
    return this.reservas.filter(r => r.ragId === agenda.recurso.ragId
      && r.resEst !== 'CANCELADA' && r.resEst !== 'AUSENCIA'
      && this.minutos(r.resIni.slice(11, 16)) < fin && this.minutos(r.resFin.slice(11, 16)) > inicio);
  }

  mostrarReserva(r: ReservaAgenda, inicio: number): boolean {
    const minutos = this.minutos(r.resIni.slice(11, 16));
    return minutos >= inicio && minutos < inicio + this.intervalo;
  }

  estado(agenda: AgendaEmpleado, inicio: number): SeleccionAgenda['estado'] {
    if (this.reservasTramo(agenda, inicio).length) return 'OCUPADO';
    if (this.esPreparacion(agenda, inicio)) return 'PREPARACION';
    if (this.esLimpieza(agenda, inicio)) return 'LIMPIEZA';
    return this.disponible(agenda, inicio) ? 'DISPONIBLE' : 'NO_DISPONIBLE';
  }

  seleccionar(empleado: AgendaEmpleado, inicio: number): void {
    const fin = inicio + this.intervalo;
    const reservas = this.reservasTramo(empleado, inicio);
    const estado = this.estado(empleado, inicio);
    const detalle = estado === 'DISPONIBLE' ? 'Tramo disponible.'
      : estado === 'OCUPADO' ? (reservas.length === 1 ? 'Tramo ocupado por una reserva.' : `Tramo ocupado por ${reservas.length} reservas.`)
      : estado === 'PREPARACION' ? `Preparación (${empleado.recurso.ragMarPre} min).`
      : estado === 'LIMPIEZA' ? `Limpieza (${empleado.recurso.ragMarPos} min).`
      : this.motivoNoDisponible(empleado, inicio, fin);
    this.seleccion = { empleado, inicio, fin, estado, reservas, detalle };
  }

  seleccionada(empleado: AgendaEmpleado, inicio: number): boolean {
    return this.seleccion?.empleado.recurso.ragId === empleado.recurso.ragId && this.seleccion.inicio === inicio;
  }

  etiquetaEstado(estado: SeleccionAgenda['estado']): string {
    return { DISPONIBLE: 'Disponible', OCUPADO: 'Ocupado', PREPARACION: 'Preparación', LIMPIEZA: 'Limpieza', NO_DISPONIBLE: 'No disponible' }[estado];
  }

  horaReserva(reserva: ReservaAgenda): string {
    return `${reserva.resIni.slice(11, 16)}–${reserva.resFin.slice(11, 16)}`;
  }

  private disponible(agenda: AgendaEmpleado, inicio: number): boolean {
    const fin = inicio + this.intervalo;
    const excepciones = agenda.excepciones.filter(e => e.exrFec === this.fecha);
    if (excepciones.some(e => !e.exrDis && this.solapa(e, inicio, fin))) return false;
    if (excepciones.some(e => e.exrDis && this.cubre(e, inicio, fin))) return true;
    return agenda.horarios.filter(h => h.horDia === this.diaSemana)
      .some(h => inicio >= this.minutos(h.horIni) && fin <= this.minutos(h.horFin));
  }

  private esPreparacion(agenda: AgendaEmpleado, inicio: number): boolean {
    if (!agenda.recurso.ragMarPre) return false;
    const horarios = agenda.horarios.filter(h => h.horDia === this.diaSemana);
    if (!horarios.length) return false;
    const apertura = Math.min(...horarios.map(h => this.minutos(h.horIni)));
    return inicio < apertura + agenda.recurso.ragMarPre && inicio + this.intervalo > apertura;
  }

  private esLimpieza(agenda: AgendaEmpleado, inicio: number): boolean {
    if (!agenda.recurso.ragMarPos) return false;
    const horarios = agenda.horarios.filter(h => h.horDia === this.diaSemana);
    if (!horarios.length) return false;
    const cierre = Math.max(...horarios.map(h => this.minutos(h.horFin)));
    return inicio < cierre && inicio + this.intervalo > cierre - agenda.recurso.ragMarPos;
  }

  private motivoNoDisponible(agenda: AgendaEmpleado, inicio: number, fin: number): string {
    const excepcion = agenda.excepciones.find(e => !e.exrDis && this.solapa(e, inicio, fin));
    return excepcion?.exrMot ? `No disponible: ${excepcion.exrMot}` : 'Fuera del horario configurado.';
  }

  private solapa(excepcion: ExcepcionAgenda, inicio: number, fin: number): boolean {
    return excepcion.exrIni === null || this.minutos(excepcion.exrIni) < fin && this.minutos(excepcion.exrFin!) > inicio;
  }

  private cubre(excepcion: ExcepcionAgenda, inicio: number, fin: number): boolean {
    return excepcion.exrIni === null || inicio >= this.minutos(excepcion.exrIni) && fin <= this.minutos(excepcion.exrFin!);
  }

  private ajustarPagina(): void {
    this.paginaEmpleados = Math.min(this.paginaEmpleados, this.totalPaginas - 1);
  }

  private mostrarError(error: any): void {
    this.cargando = false;
    this.mensaje = error?.error?.mensaje || error?.error?.message || error?.error?.detail || 'No se pudo cargar la gestión de agendas.';
  }

  private fechaComoDate(): Date {
    const [anio, mes, dia] = this.fecha.split('-').map(Number);
    return new Date(anio, mes - 1, dia);
  }

  private fechaLocal(fecha: Date): string {
    return `${fecha.getFullYear()}-${String(fecha.getMonth() + 1).padStart(2, '0')}-${String(fecha.getDate()).padStart(2, '0')}`;
  }

  private minutos(hora: string): number {
    const [horas, minutos] = hora.slice(0, 5).split(':').map(Number);
    return horas * 60 + minutos;
  }

  hora(minutos: number): string {
    return `${String(Math.floor(minutos / 60)).padStart(2, '0')}:${String(minutos % 60).padStart(2, '0')}`;
  }
}
