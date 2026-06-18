// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Supbar } from '../../components/supbar/supbar';

// Se define la configuración del componente Angular
@Component({
  selector: 'AccesoModulos', // Nombre HTML del componente
  standalone: true, // El componente funciona de forma independiente, sin módulos (NgModule).
  imports: [Supbar], // Otros componentes o módulos usados por este componente.
  templateUrl: './accesoModulos.html', // Archivo HTML que usa este componente.
  styleUrl: './accesoModulos.css' // Archivo CSS que usa este componente.
})

// Definición de la lógica del componente 
export class AccesoModulos {
	
	// Nombre del usuario conectado.
	usuarioNombre: string = '';

	// Perfil del usuario conectado.
	usuarioPerfil: string = '';

	// Angular inyecta el router.
  	constructor(private router: Router) {
	
		// Obtiene el nombre del usuario almacenado.
		this.usuarioNombre = localStorage.getItem('usuarioNombre') || '';

		// Obtiene el perfil almacenado.
		this.usuarioPerfil = localStorage.getItem('usuarioPerfil') || '';
	
  }

// Se define el método abrirModulo
  abrirModulo(modulo: string) {

//Cambia la página del módulo
	this.router.navigate(['/' + modulo]);

  }
  
// Se define el método salir
  salir() {

	// Elimina el nombre del usuario.
	localStorage.removeItem('usuarioNombre');

	// Elimina el perfil del usuario.
	localStorage.removeItem('usuarioPerfil');

	// Vuelve a la pantalla de login.
	this.router.navigate(['/accesoLogin']);

  }

}