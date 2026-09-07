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

  // Campos adicionales por los que se permite buscar.
  @Input()
  camposBusqueda: string[] = [];

  // Dentro de tablas permite que la lista aumente la fila y no quede recortada por el scroll.
  @Input()
  enFlujo: boolean = false;

  @Input()
  set datos(value: any[]) {

    this._datos = Array.isArray(value) ? value : [];

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

    const termino = this.normalizar(this.textoBusqueda);
    const campos = [...new Set([this.campoDescripcion, ...this.camposBusqueda].filter(Boolean))];
    return this.datos.filter((dato) => {
      const valoresConfigurados = campos.map((campo) => dato?.[campo]);
      return valoresConfigurados.some((valor) =>
        this.normalizar(valor).includes(termino),
      );
    });

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
  cambiarTexto(texto: string) {

    this.textoBusqueda = texto ?? '';

    // Elimina la selección anterior
    this.seleccionado = null;

    // Si el cuadro queda vacío, informa al componente padre
    this.valorSeleccionado.emit(0);

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

  private normalizar(valor: unknown): string {
    return String(valor ?? '').normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase().trim();
  }

}
