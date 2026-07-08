// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

// Importa SidebarComponent para ...
import { Sidebar } from '../../../components/sidebar/sidebar';

import { Supbar } from '../../../components/supbar/supbar';

// Se define la configuración del componente Angular
@Component({
  selector: 'PrincipalCompras', // Nombre HTML del componente
  standalone: true, // El componente funciona de forma independiente, sin módulos (NgModule).
  imports: [Sidebar, Supbar], // Otros componentes o módulos usados por este componente.
  templateUrl: './principalCompras.html', // Archivo HTML que usa este componente.
  styleUrl: '../../../styles/estiloPrincipal.css'
})

// Definición de la lógica del componente 
export class PrincipalCompras {

	// Angular inyecta el router.
	constructor(private router: Router) {}

	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/accesoModulos']);
	}
}










