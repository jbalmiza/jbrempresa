// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

// Importa libreria para activar funcionalidades básicas de HTML
import { CommonModule } from '@angular/common';

// Importa FormsModule.
//
// Permite utilizar [(ngModel)] en los controles HTML.
import { FormsModule } from '@angular/forms';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

// Importa el servicio de usuarios.
import { UsuarioService } from '../../services/usuario.service';

// Se define la configuración del componente Angular
@Component({
  selector: 'AccesoLogin', // Nombre HTML del componente
  standalone: true, // El componente funciona de forma independiente, sin módulos (NgModule).

  // Módulos utilizados por el componente.
  imports: [CommonModule, FormsModule],

  templateUrl: './accesoLogin.html', // Archivo HTML que usa este componente.
  styleUrl: './accesoLogin.css' // Archivo CSS que usa este componente.
})

// Definición de la lógica del componente
export class AccesoLogin {

  // Usuario introducido en pantalla.
  usuario: string = '';

  // Contraseña introducida en pantalla.
  password: string = '';

  // Angular inyecta Router y UsuarioService.
  constructor(
    private router: Router,
    private usuarioService: UsuarioService
  ) {}

  // Se define el método entrar.
  entrar() {

    // Llama al backend para validar el usuario.
    this.usuarioService.login(this.usuario, this.password).subscribe({

      // Login correcto.
      next: (respuesta: any) => {

        // Si existe respuesta
        if (respuesta) {

			console.log(respuesta);	
			
          // Guarda el token JWT
          localStorage.setItem( 'token', respuesta.token );
		  
		  // Guarda el ID del cliente
		  localStorage.setItem( 'clienteId', respuesta.clienteId.toString() );

		  // Guarda el ID del usuario
		  localStorage.setItem( 'usuarioId', respuesta.usuarioId.toString() );

		  // Guarda el ID del perfil
		  localStorage.setItem( 'perfilId', respuesta.perfilId.toString() );

          // Guarda el cliente
          localStorage.setItem( 'cliente', respuesta.cliente );

		  // Guarda el usuario	  
		  localStorage.setItem( 'usuario', respuesta.usuario );
		  
          // Guarda el perfil
          localStorage.setItem( 'perfil', respuesta.perfil );

          // Mensaje de bienvenida
          alert('Bienvenido');

          // Navega a módulos
          this.router.navigate(['/accesoModulos']);

        }

        // Usuario o contraseña incorrectos
        else { alert('Usuario o contraseña incorrectos'); }

      },

      // Error de conexión
      error: () => { alert('Error al conectar con el servidor'); }

    });

  }
  
  // Abre la ventana de cambio de contraseña.
  abrirCambioPassword() {

      alert('Cambio de contraseña en desarrollo.');

  }
  
}