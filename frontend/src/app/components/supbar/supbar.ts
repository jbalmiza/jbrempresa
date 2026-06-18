// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa Component para crear componentes Angular.
import { Component } from '@angular/core';

// Importa CommonModule.
import { CommonModule } from '@angular/common';

// Importa Router para navegación.
import { Router } from '@angular/router';

// Configuración del componente.
@Component({
  selector: 'supbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './supbar.html',
  styleUrl: './supbar.css'
})

// Definición de la lógica del componente.
export class Supbar {

	// Cliente del usuario conectado.
	usuarioCliente: string = '';	
	
  	// Nombre del usuario conectado.
  	usuarioUsuario: string = '';

	// Perfil del usuario conectado.
  	usuarioPerfil: string = '';

  	// Constructor.
  	constructor(private router: Router) {

		// Obtiene el nombre del usuario.
		this.usuarioCliente = localStorage.getItem('cliente') || '',
		
		// Obtiene el nombre del usuario.
    	this.usuarioUsuario = localStorage.getItem('usuario') || '';

    	// Obtiene el perfil.
    	this.usuarioPerfil = localStorage.getItem('perfil') || '';

  }

  // Vuelve a módulos.
  modulos() {

    this.router.navigate(['/accesoModulos']);

  }

  // Cierra la sesión.
  salir() {

    localStorage.clear();

    this.router.navigate(['/accesoLogin']);

  }

}