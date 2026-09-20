import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DialogosService } from '../../core/interaccion/dialogos.service';
import { ClasificacionMensaje, ConversacionInterna, DestinatarioMensaje, MensajeInterno } from '../../interfaces/mensajeria-interna.interface';
import { MensajeriaInternaService } from '../../services/mensajeria-interna.service';
import { AvisoAlertaService } from '../../services/aviso-alerta.service';
import { AvisoEntregado } from '../../interfaces/aviso-alerta.interface';

interface OpcionDestinatario { valor: string; etiqueta: string; }

@Component({ selector: 'chatInterno', standalone: true, imports: [CommonModule, FormsModule], templateUrl: './chatInterno.html', styleUrl: './chatInterno.css' })
export class ChatInterno implements OnInit, OnDestroy {
  @ViewChild('hilo') private hilo?: ElementRef<HTMLElement>;
  @Input() compacto = false;
  @Input() modo: 'CHAT' | 'REGISTRO' | 'GESTION' = 'CHAT';
  @Output() contadorCambio = new EventEmitter<number>();
  conversaciones: ConversacionInterna[] = [];
  avisos: AvisoEntregado[] = [];
  mensajes: MensajeInterno[] = [];
  destinatarios: DestinatarioMensaje[] = [];
  seleccion: ConversacionInterna | null = null;
  vista: 'LISTA' | 'CONVERSACION' | 'NUEVO' | 'BAJAS' = 'LISTA';
  asunto = '';
  contenido = '';
  clasificacion: ClasificacionMensaje = 'NORMAL';
  destinoSeleccionado = '';
  fechaPurgado = '';
  cargando = false;
  private refresco?: ReturnType<typeof setInterval>;

  constructor(private api: MensajeriaInternaService, private dialogos: DialogosService,private avisosApi:AvisoAlertaService) {}

  ngOnInit() {
    this.cargar();
    this.api.destinatarios().subscribe({ next: r => this.destinatarios = r, error: e => this.dialogos.error(e) });
    this.refresco = setInterval(() => this.refrescar(), 10000);
  }
  ngOnDestroy() { if (this.refresco) clearInterval(this.refresco); }

  cargar() {
    if(this.modo==='CHAT')this.cargarAvisos();
    this.cargando = true;
    this.api.conversaciones().subscribe({
      next: r => { this.conversaciones = r; this.cargando = false; this.emitirContador(); },
      error: e => { this.cargando = false; this.dialogos.error(e); }
    });
  }
  private cargarAvisos(){this.avisosApi.bandeja().subscribe({next:r=>{this.avisos=r;this.emitirContador()},error:e=>this.dialogos.error(e)});}
  leerAviso(a:AvisoEntregado){if(a.leido)return;this.avisosApi.marcarLeido(a).subscribe({next:()=>{a.leido=true;this.emitirContador()},error:e=>this.dialogos.error(e)});}
  get esAdministrador() { return (localStorage.getItem('perfil') || '').trim().toUpperCase() === 'ADMINISTRADOR'; }
  get puedeConsultarBajas() { return this.esAdministrador && this.modo !== 'CHAT'; }
  get puedeEliminar() { return this.esAdministrador && this.modo === 'REGISTRO'; }
  get puedeGestionar() { return this.esAdministrador && this.modo === 'GESTION'; }
  verBajas() { if (!this.puedeConsultarBajas) return; this.vista = 'BAJAS'; this.cargando = true; this.api.bajas().subscribe({ next: r => { this.conversaciones = r; this.cargando = false; }, error: e => { this.cargando = false; this.dialogos.error(e); } }); }
  abrir(c: ConversacionInterna) {
    this.seleccion = c; this.vista = 'CONVERSACION';
    this.api.mensajes(c.id, true).subscribe({ next: r => { this.mensajes = r; c.noLeidos = 0; this.emitirContador(); this.desplazarAlFinal(); }, error: e => this.dialogos.error(e) });
  }
  marcarNoLeida(c: ConversacionInterna, evento: Event) {
    evento.stopPropagation();
    this.api.lectura(c.id, false).subscribe({ next: r => { Object.assign(c, r); this.emitirContador(); }, error: e => this.dialogos.error(e) });
  }
  nueva() { this.vista = 'NUEVO'; this.seleccion = null; this.asunto = ''; this.contenido = ''; this.clasificacion = 'NORMAL'; this.destinoSeleccionado = ''; }
  get opcionesDestinatario(): OpcionDestinatario[] {
    const rol = (localStorage.getItem('perfil') || '').trim().toUpperCase();
    const empleados = this.destinatarios.filter(d => d.perfil === 'EMPLEADO');
    const jefes = this.destinatarios.filter(d => d.perfil === 'JEFE');
    const administradores = this.destinatarios.filter(d => d.perfil === 'ADMINISTRADOR');
    if (rol === 'ADMINISTRADOR') return [
      ...(this.destinatarios.length ? [{ valor: 'TODOS', etiqueta: 'Todos' }] : []),
      ...(jefes.length ? [{ valor: 'JEFES', etiqueta: 'Todos los jefes' }] : []),
      ...(empleados.length ? [{ valor: 'EMPLEADOS', etiqueta: 'Todos los empleados' }] : []),
      ...this.destinatarios.map(d => ({ valor: `USUARIO:${d.usuarioId}`, etiqueta: `${d.nombre} · ${this.etiquetaPerfil(d.perfil)}` }))
    ];
    if (rol === 'JEFE') return [
      ...(administradores.length ? [{ valor: 'ADMINISTRADORES', etiqueta: 'Administrador' }] : []),
      ...(empleados.length ? [{ valor: 'EMPLEADOS', etiqueta: 'Todos los empleados' }] : []),
      ...empleados.map(d => ({ valor: `USUARIO:${d.usuarioId}`, etiqueta: d.nombre }))
    ];
    return jefes.map(d => ({ valor: `USUARIO:${d.usuarioId}`, etiqueta: d.nombre }));
  }
  enviar() {
    const destinos = this.resolverDestinos();
    if (!this.asunto.trim() || !destinos.length) { this.dialogos.avisar('Informe destinatario y asunto.'); return; }
    this.api.crear(this.asunto, this.contenido, destinos, this.clasificacion).subscribe({
      next: c => { this.cargar(); this.abrir(c); this.dialogos.exito('Mensaje enviado.'); }, error: e => this.dialogos.error(e)
    });
  }
  responder() {
    if (!this.seleccion || !this.contenido.trim()) return;
    this.api.responder(this.seleccion.id, this.contenido, this.clasificacion).subscribe({
      next: () => { this.contenido = ''; this.clasificacion = 'NORMAL'; this.abrir(this.seleccion!); this.cargar(); }, error: e => this.dialogos.error(e)
    });
  }
  async editar(m: MensajeInterno) {
    if (!this.puedeEliminar || !m.propio || !this.seleccion?.activa) return;
    const texto = await this.dialogos.solicitarTexto('Modifique el contenido del mensaje.', 'Modificar mensaje');
    if (texto === null || !texto.trim()) return;
    this.api.editar(m.id, texto).subscribe({ next: r => Object.assign(m, r), error: e => this.dialogos.error(e) });
  }
  async darBaja() { if (!this.puedeGestionar || !this.seleccion || !await this.dialogos.confirmar('La conversación dejará de estar visible para todos sus participantes. ¿Continuar?', true)) return; this.api.darBaja(this.seleccion.id).subscribe({ next: () => { this.volver(); this.dialogos.exito('Conversación dada de baja.'); }, error: e => this.dialogos.error(e) }); }
  async reactivar(c: ConversacionInterna, evento: Event) { evento.stopPropagation(); if (!this.puedeGestionar || !await this.dialogos.confirmar('¿Reactivar esta conversación?')) return; this.api.reactivar(c.id).subscribe({ next: () => { this.verBajas(); this.dialogos.exito('Conversación reactivada.'); }, error: e => this.dialogos.error(e) }); }
  async eliminarDefinitivamente(c: ConversacionInterna, evento: Event) { evento.stopPropagation(); if (!this.puedeEliminar || !await this.dialogos.confirmar('¿Eliminar definitivamente esta conversación y todos sus mensajes?', true)) return; this.api.eliminarConversacion(c.id).subscribe({ next: () => { this.verBajas(); this.dialogos.exito('Conversación eliminada definitivamente.'); }, error: e => this.dialogos.error(e) }); }
  async purgarHasta() { if (!this.puedeEliminar || !this.fechaPurgado || !await this.dialogos.confirmar(`¿Eliminar definitivamente las conversaciones dadas de baja hasta el ${this.fechaPurgado}?`, true)) return; this.api.eliminarHasta(this.fechaPurgado).subscribe({ next: r => { this.verBajas(); this.dialogos.exito(`${r.eliminadas} conversaciones eliminadas definitivamente.`); }, error: e => this.dialogos.error(e) }); }
  nombres(destinatarios: DestinatarioMensaje[]) { return destinatarios.map(d => d.nombre).join(', '); }
  volver() { this.vista = 'LISTA'; this.seleccion = null; this.contenido = ''; this.cargar(); }

  private refrescar() {
    if (this.vista === 'CONVERSACION' && this.seleccion) {
      this.api.mensajes(this.seleccion.id, true).subscribe({ next: r => { this.mensajes = r; this.cargarSilenciosamente(); }, error: () => undefined });
    } else if (this.vista === 'LISTA'){this.cargarSilenciosamente();if(this.modo==='CHAT')this.cargarAvisos();}
  }
  private cargarSilenciosamente() { this.api.conversaciones().subscribe({ next: r => { this.conversaciones = r; this.emitirContador(); }, error: () => undefined }); }
  private resolverDestinos(): number[] {
    if (this.destinoSeleccionado.startsWith('USUARIO:')) return [Number(this.destinoSeleccionado.substring(8))].filter(Boolean);
    const perfil = this.destinoSeleccionado === 'JEFES' ? 'JEFE'
      : this.destinoSeleccionado === 'EMPLEADOS' ? 'EMPLEADO'
      : this.destinoSeleccionado === 'ADMINISTRADORES' ? 'ADMINISTRADOR' : '';
    return (perfil ? this.destinatarios.filter(d => d.perfil === perfil) : this.destinoSeleccionado === 'TODOS' ? this.destinatarios : []).map(d => d.usuarioId);
  }
  private etiquetaPerfil(perfil: string) { return perfil === 'JEFE' ? 'Jefe' : perfil === 'EMPLEADO' ? 'Empleado' : 'Administrador'; }
  private emitirContador() { this.contadorCambio.emit(this.conversaciones.reduce((s, c) => s + c.noLeidos, 0)+this.avisos.filter(a=>!a.leido).length); }
  private desplazarAlFinal() { setTimeout(() => { const elemento = this.hilo?.nativeElement; if (elemento) elemento.scrollTop = elemento.scrollHeight; }); }
}
