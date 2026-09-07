// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria Component para crear componentes Angular
// Importa libreria Input para recibir datos que son las opciones de menú
import { Component, Input, OnChanges } from '@angular/core';

// Importa funcionalidades comunes de Angular.
import { CommonModule } from '@angular/common';

// Importa Router para navegar entre páginas.
import { Router } from '@angular/router';
import { MENUS_MODULOS } from '../../config/menu-modulos.config';

// Se define la configuración del componente Angular
@Component({
  selector: 'sidebar', // Nombre HTML del componente
  standalone: true, // El componente funciona de forma independiente, sin módulos (NgModule).
  imports: [CommonModule], // Otros componentes o módulos usados por este componente.
  templateUrl: './sidebar.html', // Archivo HTML que usa este componente.
  styleUrl: './sidebar.css' // Archivo CSS que usa este componente.
})

// Definición de la lógica del componente
export class Sidebar implements OnChanges {
  readonly menus=MENUS_MODULOS;

  // Nombre del usuario conectado.
  usuarioNombre: string = '';

  // Perfil del usuario conectado.
  usuarioPerfil: string = '';

  // Recibe el módulo actual desde el componente padre.
  @Input() modulo: string = '';
  get menuActual(){return this.menus[this.modulo];}
  ngOnChanges(){
    const grupos = this.menuActual?.grupos ?? [];

    // Al entrar en cualquier módulo se abre su bloque de Gestión (menu2).
    // En una opción concreta se muestra el bloque que contiene la ruta activa.
    this.menuAbierto = grupos.find(grupo => grupo.id === 'menu2')?.id ?? grupos[0]?.id ?? '';

    const ruta = this.router.url.split('?')[0].split('#')[0].split('/').filter(Boolean)[1];
    if (!ruta) return;

    const grupoActivo = grupos.find(grupo => grupo.opciones.some(opcion => opcion.ruta === ruta));
    if (grupoActivo) this.menuAbierto = grupoActivo.id;
  }

  // Menú actualmente abierto.
  // La segunda sección corresponde a la gestión principal en todos los módulos.
  menuAbierto: string = 'menu2';
  
  // Indica si el sidebar está abierto en móvil.
  menuMovilAbierto: boolean = false;

  // Constructor del componente.
  //
  // Angular inyecta automáticamente Router.
  constructor(private router: Router) {

    // Obtiene el nombre del usuario almacenado.
    this.usuarioNombre = localStorage.getItem('usuario') || '';

    // Obtiene el perfil almacenado.
    this.usuarioPerfil = localStorage.getItem('perfil') || '';

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
    const destino = '/' + this.modulo + '/' + submenu;
    const rutaActual = this.router.url.split('?')[0].split('#')[0].replace(/\/$/, '');

    // Al repetir la pantalla actual se reinicia su estado interno.
    if (rutaActual === destino) {
      window.location.reload();
      return;
    }

    this.router.navigate([destino]);

  }

  esActiva(submenu?:string):boolean{return !!submenu&&this.router.url.split('?')[0].split('#')[0].replace(/\/$/,'')===`/${this.modulo}/${submenu}`;}

  // Se define el método volver.
  modulos() {

    // Cambia la página a módulos.
    this.router.navigate(['/accesoModulos']);

  }
  
  // Se define el método volver.
  salir() {

	// Elimina todos los datos de sesiÃ³n.
	const usuarioRecordado = localStorage.getItem('login.usuarioRecordado');
	localStorage.clear();
	if (usuarioRecordado) localStorage.setItem('login.usuarioRecordado', usuarioRecordado);

	// Vuelve a la pantalla de login.
	this.router.navigate(['/accesoLogin']);

  }

}
