// Importa librería para crear componentes Angular.
import { Component } from '@angular/core';

// Permite utilizar las directivas básicas de Angular como
// *ngIf, *ngFor y *ngTemplateOutlet.
import { CommonModule } from '@angular/common';

// Permite definir datos de entrada desde el componente padre.
import { Input } from '@angular/core';

// Permite localizar las plantillas definidas por el componente padre.
import { ContentChildren } from '@angular/core';

// Representa una colección de elementos obtenidos mediante ContentChildren.
import { QueryList } from '@angular/core';

// Importa la directiva TablaColumna.
import { TablaColumna } from '../../directives/tablaColumna/tablaColumna';

// Permite lanzar eventos al componente padre.
import { Output, EventEmitter } from '@angular/core';

// Se define la configuración del componente Angular.
@Component({

  selector: 'tablaEdicion',

  standalone: true,

  imports: [ CommonModule ],

  templateUrl: './tablaEdicion.html',

  styleUrl: './tablaEdicion.css'

})

// Definición de la lógica del componente.
export class TablaEdicion {

  @Input()
  titulo = 'Detalle';

  // Títulos mostrados en la cabecera de la tabla.
  @Input()
  titulosColumnas: { [key: string]: string } = {};

  // Campos que forman las columnas de la tabla.
  @Input()
  columnas: string[] = [];

  // Datos que se mostrarán en la tabla.
  @Input()
  datos: unknown[] = [];

  // Plantillas definidas por el componente padre.
  @ContentChildren(TablaColumna)
  plantillas!: QueryList<TablaColumna>;
  
  // Notifica al componente padre que se desea insertar una nueva línea.
  @Output()
  insertarLinea = new EventEmitter<void>();

  // Obtiene la plantilla correspondiente a una columna.
  obtenerPlantilla(columna: string): TablaColumna | undefined {

    return this.plantillas.find(

      plantilla => plantilla.tablaColumna === columna

    );

  }

}
