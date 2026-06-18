// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

// Importa libreria para mostrar páginas según la ruta actual
import { RouterOutlet } from '@angular/router';

// Se define la configuración del componente Angular
@Component({
  selector: 'app-root', // Nombre HTML del componente
  standalone: true, // El componente funciona de forma independiente, sin módulos (NgModule).
  imports: [RouterOutlet], // Otros componentes o módulos usados por este componente.
  templateUrl: './app.html', // Archivo HTML que usa este componente.
  styleUrl: './app.css' // Archivo CSS que usa este componente.
})

// Definición de la lógica del componente
export class App {

}


