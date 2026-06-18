import {
  Component,
  Input,
  Output,
  EventEmitter
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms';

@Component({
  selector: 'tabla',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tabla.html',
  styleUrls: ['./tabla.css']
})
export class Tabla {

  // Columnas dinámicas
  @Input() columnas: string[] = [];
  
  // Relación campo → título mostrado en la cabecera de la tabla
  @Input() titulosColumnas: { [key: string]: string } = {};

  // Datos privados
  private _datos: any[] = [];

  // Datos dinámicos
  @Input()
  set datos(value: any[]) {

	this._datos = value || [];

    this.datosFiltrados = [...this._datos];

    this.paginaActual = 1;

    this.calcularPaginas();

    this.actualizarPagina();

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

  // Selecciona una fila
  seleccionarFila(fila: any) {

    this.filaActiva = fila;

    this.filaSeleccionada.emit(fila);

  }

  // Filtra los registros
  filtrar() {

    this.datosFiltrados = this.datos.filter(fila => {

      return this.columnas.every(columna => {

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

    this.paginaActual = 1;

    this.actualizarPagina();

  }

  // Página anterior
  paginaAnterior() {

    if (this.paginaActual > 1) {

      this.paginaActual--;

      this.actualizarPagina();

    }

  }

  // Página siguiente
  paginaSiguiente() {

    if (this.paginaActual < this.totalPaginas) {

      this.paginaActual++;

      this.actualizarPagina();

    }

  }

  // Última página
  ultimaPagina() {

    this.paginaActual = this.totalPaginas;

    this.actualizarPagina();

  }

  // Cambio de registros por página
  cambiarRegistrosPorPagina() {

    this.registrosPorPagina =
      Number(this.registrosPorPagina);

    this.paginaActual = 1;

    this.calcularPaginas();

    this.actualizarPagina();

  }

}