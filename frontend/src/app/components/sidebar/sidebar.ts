// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria Component para crear componentes Angular
// Importa libreria Input para recibir datos que son las opciones de menú
import { Component, Input } from '@angular/core';

// Importa funcionalidades comunes de Angular.
import { CommonModule } from '@angular/common';

// Importa Router para navegar entre páginas.
import { Router } from '@angular/router';

// Se define la configuración del componente Angular
@Component({
  selector: 'sidebar', // Nombre HTML del componente
  standalone: true, // El componente funciona de forma independiente, sin módulos (NgModule).
  imports: [CommonModule], // Otros componentes o módulos usados por este componente.
  templateUrl: './sidebar.html', // Archivo HTML que usa este componente.
  styleUrl: './sidebar.css' // Archivo CSS que usa este componente.
})

// Definición de la lógica del componente
export class Sidebar {

  // Nombre del usuario conectado.
  usuarioNombre: string = '';

  // Perfil del usuario conectado.
  usuarioPerfil: string = '';

  // Recibe el módulo actual desde el componente padre.
  @Input() modulo: string = '';

  // Menú actualmente abierto.
  menuAbierto: string = '';
  
  // Indica si el sidebar está abierto en móvil.
  menuMovilAbierto: boolean = false;

  // Constructor del componente.
  //
  // Angular inyecta automáticamente Router.
  constructor(private router: Router) {

    // Obtiene el nombre del usuario almacenado.
    this.usuarioNombre = localStorage.getItem('usuarioNombre') || '';

    // Obtiene el perfil almacenado.
    this.usuarioPerfil = localStorage.getItem('usuarioPerfil') || '';

  }

  // Abre o cierra un menú.
  toggleMenu(menu: string) {

    // Si el menú ya está abierto.
    if (this.menuAbierto === menu) {

      // Lo cierra.
      this.menuAbierto = '';

    } else {

      // Abre el menú seleccionado.
      this.menuAbierto = menu;

    }

  }
  
  // Abre o cierra el sidebar completo, en modo móvil.
  toggleSidebar() {

    this.menuMovilAbierto = !this.menuMovilAbierto;

  }

  // Método para abrir la página de submenu.
  abrirPagina(submenu: string) {

    // Navega a la página seleccionada.
    this.router.navigate(['/' + this.modulo + '/' + submenu]);

  }

  // Se define el método volver.
  modulos() {

    // Cambia la página a módulos.
    this.router.navigate(['/accesoModulos']);

  }
  
  // Se define el método volver.
  salir() {

	// Elimina el nombre del usuario.
	localStorage.removeItem('usuarioNombre');

	// Elimina el perfil del usuario.
	localStorage.removeItem('usuarioPerfil');

	// Vuelve a la pantalla de login.
	this.router.navigate(['/accesoLogin']);

  }

}