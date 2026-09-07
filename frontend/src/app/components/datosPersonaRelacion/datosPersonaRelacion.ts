import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Persona } from '../../interfaces/persona.interface';
import { Domicilio } from '../../interfaces/domicilio.interface';
import { DomicilioService } from '../../services/domicilio.service';
import { SelectorBusqueda } from '../selectorBusqueda/selectorBusqueda';

@Component({
  selector: 'datosPersonaRelacion',
  standalone: true,
  imports: [CommonModule, SelectorBusqueda],
  templateUrl: './datosPersonaRelacion.html',
  styleUrl: './datosPersonaRelacion.css',
})
export class DatosPersonaRelacion implements OnInit {
  @Input() personas: Persona[] = [];
  @Input() personaId: number | null = null;
  @Input() etiqueta = 'Persona *';
  @Output() personaIdChange = new EventEmitter<number>();
  domicilios: Domicilio[] = [];

  constructor(private domicilioService: DomicilioService) {}

  ngOnInit(): void {
    this.domicilioService.obtenerDomicilios().subscribe({
      next: (datos) => this.domicilios = datos.filter((d) => d.domTipMov !== 'B'),
      error: (error) => console.error('No se pudieron cargar los domicilios de Persona.', error),
    });
  }

  seleccionar(id: number): void { this.personaIdChange.emit(id); }
  get persona(): Persona | null { return this.personas.find((p) => Number(p.perId) === Number(this.personaId)) || null; }
  get direccion(): string { return this.domicilios.find((d) => Number(d.domId) === Number(this.persona?.domId))?.domDir || ''; }
}
