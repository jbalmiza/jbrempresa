import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'selectorBusqueda',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './selectorBusqueda.html',
  styleUrl: './selectorBusqueda.css'
})

export class SelectorBusqueda implements OnChanges {

  // Lista de registros recibidos desde el componente padre
  private _datos: any[] = [];

  // Nombre del campo que se mostrará al usuario
  @Input()
  campoDescripcion: string = '';

  // Nombre del campo cuyo valor se enviará al componente padre
  @Input()
  campoId: string = '';

  @Input()
  set datos(value: any[]) {

    this._datos = value;

    // Actualiza la selección cuando cambia la lista
    this.actualizarSeleccion();

  }

  get datos(): any[] {

    return this._datos;

  }

  // Id seleccionado recibido desde el componente padre
  @Input()
  idSeleccionado: number = 0;

  // Texto introducido en el buscador
  textoBusqueda = '';

  // Guarda el registro seleccionado por el usuario
  seleccionado: any = null;

  // Envía el valor seleccionado al componente padre
  @Output()
  valorSeleccionado = new EventEmitter<number>();

  // Devuelve únicamente los registros que coinciden con la búsqueda
  get datosFiltrados(): any[] {

    // Si ya hay un registro seleccionado, no muestra sugerencias
    if (this.seleccionado) {

      return [];

    }

    // Si se han escrito menos de 3 caracteres, no muestra sugerencias
    if (this.textoBusqueda.trim().length < 3) {

      return [];

    }

    return this.datos.filter(d =>
      String(d[this.campoDescripcion] || '')
        .toLowerCase()
        .includes(this.textoBusqueda.toLowerCase())
    );

  }

  // Este método se ejecuta al pulsar una opción de la lista
  seleccionar(dato: any) {

    this.seleccionado = dato;

    this.textoBusqueda = dato[this.campoDescripcion];

    // Envía el id correspondiente al componente padre
    this.valorSeleccionado.emit(
      dato[this.campoId]
    );

  }

  // Se ejecuta cuando el usuario modifica el texto
  cambiarTexto() {

    // Elimina la selección anterior
    this.seleccionado = null;

    // Si el cuadro queda vacío, informa al componente padre
    if (this.textoBusqueda.trim() === '') {

      this.valorSeleccionado.emit(0);

    }

  }

  // Se ejecuta cuando cambia algún valor recibido desde el componente padre
  ngOnChanges(changes: SimpleChanges): void {

    this.actualizarSeleccion();

  }

  // Actualiza el registro seleccionado en el componente
  private actualizarSeleccion() {

    // Comprueba que existe un id seleccionado y que la lista ya está cargada
    if (this.idSeleccionado && this.datos.length > 0) {

      // Busca el registro correspondiente al id recibido
      const registro = this.datos.find(
        d => Number(d[this.campoId]) === Number(this.idSeleccionado)
      );

      // Si encuentra el registro
      if (registro) {

        // Guarda el registro seleccionado
        this.seleccionado = registro;

        // Muestra la descripción en el cuadro de búsqueda
        this.textoBusqueda = registro[this.campoDescripcion];

      }

    }

  }

}