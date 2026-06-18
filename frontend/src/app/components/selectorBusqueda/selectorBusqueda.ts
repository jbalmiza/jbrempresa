import { Component, Input } from '@angular/core';
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
export class SelectorBusqueda {

  @Input()
  datos: any[] = [];

  // Texto introducido en el buscador
  textoBusqueda = '';
  
  // Guarda el domicilio seleccionado por el usuario
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
      d.domDir
        .toLowerCase()
        .includes(this.textoBusqueda.toLowerCase())
    );

  }

  // Este método se ejecuta al pulsar una opción de la lista
  seleccionar(dato: any) {

    this.seleccionado = dato;

    this.textoBusqueda = dato.domDir;

    // Envía el domId al componente padre
    this.valorSeleccionado.emit(dato.domId);

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

}