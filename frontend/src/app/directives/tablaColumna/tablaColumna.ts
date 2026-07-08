// Importa librerías para crear directivas Angular.
import { Directive } from '@angular/core';

// Permite recibir valores desde el componente padre.
import { Input } from '@angular/core';

// Representa el contenido de una plantilla Angular (<ng-template>).
import { TemplateRef } from '@angular/core';

// Se define la configuración de la directiva Angular.
@Directive({

  // La directiva se utilizará como un atributo HTML.
  selector: '[tablaColumna]',

  standalone: true

})

// Definición de la lógica de la directiva.
export class TablaColumna {

  // Nombre de la columna a la que pertenece la plantilla.
  @Input()
  tablaColumna = '';

  // Guarda la plantilla asociada a la columna.
  constructor(

    public template: TemplateRef<unknown>

  ) { }

}