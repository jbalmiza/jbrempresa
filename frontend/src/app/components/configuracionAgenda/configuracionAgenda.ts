import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { avisarAplicacion, confirmarAplicacion } from '../../core/interaccion/dialogos.service';
import { HorarioAgenda, RecursoAgenda, TipoRecursoAgenda } from '../../interfaces/agenda.interface';
import { AgendaService } from '../../services/agenda.service';

interface Franja { inicio: string; fin: string; }
interface DiaConfig { dia: number; nombre: string; activo: boolean; franjas: Franja[]; }

@Component({
  selector: 'configuracionAgenda', standalone: true, imports: [CommonModule, FormsModule],
  templateUrl: './configuracionAgenda.html', styleUrl: './configuracionAgenda.css'
})
export class ConfiguracionAgenda implements OnChanges {
  @Input() recursos: RecursoAgenda[] = [];
  @Input() tipoRecurso: TipoRecursoAgenda = 'EMPLEADO';
  @Output() guardado = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();

  horaVisual = '08:00'; preparacion = 0; limpieza = 0; capacidad = 1;
  cargando = false; mensaje = '';
  dias: DiaConfig[] = this.diasVacios();

  constructor(private service: AgendaService) {}

  ngOnChanges(): void {
    const primero = this.recursos[0];
    if (!primero) return;
    this.horaVisual = (primero.ragHorVis || '08:00').slice(0, 5);
    this.preparacion = primero.ragMarPre || 0;
    this.limpieza = primero.ragMarPos || 0;
    this.capacidad = primero.ragCap || 1;
    if (primero.ragId > 0) this.cargarHorario(primero.ragId); else this.dias = this.diasVacios();
  }

  agregarFranja(dia: DiaConfig): void { dia.franjas.push({ inicio: '16:00', fin: '19:00' }); }
  eliminarFranja(dia: DiaConfig, indice: number): void { if (dia.franjas.length > 1) dia.franjas.splice(indice, 1); }

  async guardar(): Promise<void> {
    if (!this.recursos.length || !this.validar()) return;
    if (this.recursos.length > 1 && !await confirmarAplicacion(`Esta configuración sustituirá el horario de ${this.recursos.length} empleados. ¿Desea continuar?`, true)) return;
    this.cargando = true; this.mensaje = '';
    const horarios = this.horariosEntrada();
    forkJoin(this.recursos.map(recurso => this.service.guardarRecurso({
      ragTip: this.tipoRecurso, ragRefId: recurso.ragRefId, ragNom: recurso.ragNom,
      ragCap: this.tipoRecurso === 'EMPLEADO' ? 1 : this.capacidad,
      ragMarPre: this.preparacion, ragMarPos: this.limpieza, ragHorVis: this.horaVisual
    }))).subscribe({
      next: guardados => forkJoin(guardados.map(recurso => this.service.guardarHorarios(recurso.ragId, horarios))).subscribe({
        next: () => { this.cargando = false; this.guardado.emit(); },
        error: error => this.error(error)
      }),
      error: error => this.error(error)
    });
  }

  private cargarHorario(id: number): void {
    this.service.horarios(id).subscribe({
      next: horarios => {
        this.dias = this.diasVacios();
        this.dias.forEach(dia => {
          const franjas = horarios.filter(h => h.horDia === dia.dia);
          dia.activo = !!franjas.length;
          if (franjas.length) dia.franjas = franjas.map(h => ({ inicio: h.horIni.slice(0, 5), fin: h.horFin.slice(0, 5) }));
        });
      }, error: error => this.error(error)
    });
  }

  private horariosEntrada(): HorarioAgenda[] {
    return this.dias.filter(d => d.activo).flatMap(d => d.franjas.map(f => ({ horDia: d.dia, horIni: f.inicio, horFin: f.fin })));
  }

  private validar(): boolean {
    if (!this.horaVisual) { this.mensaje = 'Informe la hora inicial de visualización.'; avisarAplicacion(this.mensaje); return false; }
    if (this.preparacion < 0 || this.limpieza < 0 || this.capacidad < 1) { this.mensaje = 'Los valores de configuración no son válidos.'; avisarAplicacion(this.mensaje); return false; }
    const incorrecta = this.dias.some(d => d.activo && d.franjas.some(f => !f.inicio || !f.fin || f.inicio >= f.fin));
    if (incorrecta) { this.mensaje = 'Revise las franjas: la hora de inicio debe ser anterior a la de fin.'; avisarAplicacion(this.mensaje); return false; }
    return true;
  }

  private diasVacios(): DiaConfig[] {
    return ['Lunes','Martes','Miércoles','Jueves','Viernes','Sábado','Domingo'].map((nombre, indice) => ({
      dia: indice + 1, nombre, activo: indice < 5, franjas: [{ inicio: '09:00', fin: '14:00' }]
    }));
  }

  private error(error: any): void {
    this.cargando = false;
    this.mensaje = error?.error?.mensaje || error?.error?.message || error?.error?.detail || 'No se pudo guardar la configuración.';
    avisarAplicacion(this.mensaje);
  }
}
