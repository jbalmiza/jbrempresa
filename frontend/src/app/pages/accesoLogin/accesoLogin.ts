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
import { ActivatedRoute, Router } from '@angular/router';

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
  usuario: string = localStorage.getItem('login.usuarioRecordado') || '';

  // Contraseña introducida en pantalla.
  password: string = '';

  recordar = this.usuario.length > 0;
  mostrarPassword = false;
  accediendo = false;
  mensajeError = '';
  mensajeExito = '';
  vista: 'login' | 'solicitar' | 'restablecer' = 'login';
  correoRecuperacion = '';
  tokenRecuperacion = '';
  nuevaPassword = '';
  confirmarPassword = '';
  procesandoRecuperacion = false;

  // Angular inyecta Router y UsuarioService.
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private usuarioService: UsuarioService
  ) {
    this.tokenRecuperacion = this.route.snapshot.queryParamMap.get('recuperacion') || '';
    if (this.tokenRecuperacion) this.vista = 'restablecer';
  }

  // Se define el método entrar.
  entrar() {

    this.mensajeError = '';
    if (!this.usuario.trim() || !this.password) {
      this.mensajeError = 'Introduce el usuario y la contraseña.';
      return;
    }

    this.accediendo = true;

    // Llama al backend para validar el usuario.
    this.usuarioService.login(this.usuario, this.password).subscribe({

      // Login correcto.
      next: (respuesta: any) => {

        // Si existe respuesta
        if (respuesta) {

		  this.guardarPreferenciaAcceso();
			
          // Guarda el token JWT
          localStorage.setItem( 'token', respuesta.token );

          // Fija la hora que se utilizará para el saludo durante esta sesión.
          localStorage.setItem('inicioSesion', new Date().toISOString());
		  
		  // Guarda el ID del cliente
		  localStorage.setItem( 'empresaId', respuesta.empresaId.toString() );

		  // Guarda el ID del usuario
		  localStorage.setItem( 'usuarioId', respuesta.usuarioId.toString() );

		  // Guarda el ID del perfil
		  localStorage.setItem( 'perfilId', respuesta.perfilId.toString() );

          // Guarda el cliente
          localStorage.setItem( 'empresa', respuesta.empresa );

		  // Guarda el usuario	  
		  localStorage.setItem( 'usuario', respuesta.usuario );
		  
          // Guarda el perfil
          localStorage.setItem( 'perfil', respuesta.perfil );

          // Navega a módulos
          this.router.navigate(['/accesoModulos']);

        }

        // Usuario o contraseña incorrectos
        else {
          this.accediendo = false;
          this.mensajeError = 'Usuario o contraseña incorrectos.';
        }

      },

      // Error de conexión
      error: () => {
        this.accediendo = false;
        this.mensajeError = 'No se pudo conectar con el servidor.';
      }

    });

  }

  private guardarPreferenciaAcceso(): void {

    if (!this.recordar) {
      localStorage.removeItem('login.usuarioRecordado');
      return;
    }

    localStorage.setItem('login.usuarioRecordado', this.usuario.trim());

    // Delega la contraseña al almacén seguro del navegador cuando está disponible.
    const CredencialPassword = (window as any).PasswordCredential;
    if (CredencialPassword && navigator.credentials?.store) {
      const credencial = new CredencialPassword({
        id: this.usuario.trim(),
        name: this.usuario.trim(),
        password: this.password
      });
      navigator.credentials.store(credencial).catch(() => undefined);
    }

  }
  
  // Abre la ventana de cambio de contraseña.
  abrirCambioPassword() {

      this.mensajeError = '';
      this.mensajeExito = '';
      this.vista = 'solicitar';

  }

  volverAlAcceso(): void {
    this.vista = 'login';
    this.tokenRecuperacion = '';
    this.nuevaPassword = '';
    this.confirmarPassword = '';
    this.mensajeError = '';
    this.mensajeExito = '';
    this.router.navigate(['/accesoLogin'], { replaceUrl: true });
  }

  solicitarRecuperacion(): void {
    this.mensajeError = '';
    this.mensajeExito = '';
    if (!this.usuario.trim() || !this.correoRecuperacion.trim()) {
      this.mensajeError = 'Introduce tu usuario y correo electrónico.';
      return;
    }
    this.procesandoRecuperacion = true;
    this.usuarioService.solicitarRecuperacion(
      this.usuario.trim(),
      this.correoRecuperacion.trim()
    ).subscribe({
      next: respuesta => {
        this.procesandoRecuperacion = false;
        this.mensajeExito = respuesta.mensaje;
        if (respuesta.tokenDesarrollo) {
          this.tokenRecuperacion = respuesta.tokenDesarrollo;
          this.vista = 'restablecer';
        }
      },
      error: () => {
        this.procesandoRecuperacion = false;
        this.mensajeError = 'No se pudo procesar la solicitud. Inténtalo más tarde.';
      }
    });
  }

  restablecerPassword(): void {
    this.mensajeError = '';
    this.mensajeExito = '';
    if (!this.nuevaPassword || !this.confirmarPassword) {
      this.mensajeError = 'Introduce y confirma la nueva contraseña.';
      return;
    }
    this.procesandoRecuperacion = true;
    this.usuarioService.confirmarRecuperacion(
      this.tokenRecuperacion,
      this.nuevaPassword,
      this.confirmarPassword
    ).subscribe({
      next: () => {
        this.procesandoRecuperacion = false;
        this.password = '';
        this.vista = 'login';
        this.mensajeExito = 'Contraseña actualizada. Ya puedes iniciar sesión.';
        this.router.navigate(['/accesoLogin'], { replaceUrl: true });
      },
      error: error => {
        this.procesandoRecuperacion = false;
        this.mensajeError = error?.error?.detail
          || error?.error?.message
          || 'El enlace no es válido o ha caducado.';
      }
    });
  }
  
}
