import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'datosMovimiento',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './datosMovimiento.html',
  styleUrl: '../../styles/estiloGeneral.css'
})
export class DatosMovimiento implements OnChanges {
  @Input() tipo = '';
  @Input() causa = '';
  @Input() usuario = '';
  @Input() fecha: string | Date | null | undefined = '';
  @Input() activo: boolean | null | undefined = true;
  @Input() mostrarTipo = true;
  @Input() mostrarCausa = true;
  @Input() mostrarUsuario = true;
  @Input() mostrarFecha = true;
  @Input() mostrarActivo = true;
  @Input() causaEditable = false;
  @Input() previsualizarMovimiento = false;
  @Output() causaChange = new EventEmitter<string>();
  @Output() activoChange = new EventEmitter<boolean>();

  private usuarioPrevisualizado = '';
  private fechaPrevisualizada = new Date();

  ngOnChanges(): void {
    if (this.previsualizarMovimiento) {
      this.usuarioPrevisualizado = localStorage.getItem('usuario') || '';
      this.fechaPrevisualizada = new Date();
    }
  }

  get usuarioMostrado(): string {
    return this.previsualizarMovimiento ? this.usuarioPrevisualizado : this.usuario;
  }

  get fechaMostrada(): string {
    const valor = this.previsualizarMovimiento ? this.fechaPrevisualizada : this.fecha;
    if (!valor) return '';
    const fecha = valor instanceof Date ? valor : new Date(valor);
    return Number.isNaN(fecha.getTime()) ? String(valor) : new Intl.DateTimeFormat('es-ES', {
      day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit'
    }).format(fecha);
  }
}
