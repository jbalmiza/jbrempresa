import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

export interface CampoIdentificacion {
  etiqueta: string;
  valor: string | number | null | undefined;
}

@Component({
  selector: 'datosIdentificacion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './datosIdentificacion.html',
  styleUrl: '../../styles/estiloGeneral.css',
})
export class DatosIdentificacion {
  @Input() campos: CampoIdentificacion[] = [];
  get camposPresentados(): CampoIdentificacion[] {
    const normalizados = this.campos.map(campo => campo.etiqueta.trim().toLowerCase() === 'empresa' ? {...campo, etiqueta:'Id Empresa'} : campo);
    if (normalizados.some(campo => campo.etiqueta.trim().toLowerCase() === 'id empresa')) return normalizados;
    return [{etiqueta:'Id Empresa', valor:Number(localStorage.getItem('empresaId')) || null}, ...normalizados];
  }
}
