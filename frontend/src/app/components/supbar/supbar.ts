// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa Component para crear componentes Angular.
import { Component, OnDestroy, OnInit } from '@angular/core';

// Importa CommonModule.
import { CommonModule } from '@angular/common';

// Importa Router para navegación.
import { Router } from '@angular/router';
import { AdjuntoService } from '../../services/adjunto.service';
import { BlobUrlUtil } from '../../shared/utils/blob-url.util';

// Configuración del componente.
@Component({
  selector: 'supbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './supbar.html',
  styleUrl: './supbar.css'
})

// Definición de la lógica del componente.
export class Supbar implements OnInit, OnDestroy {
	private readonly blobs=new BlobUrlUtil();

	imagenEmpresaUrl = '';
	readonly diaSemana = new Intl.DateTimeFormat('es-ES', { weekday: 'long' }).format(new Date());
	readonly fechaActual = new Intl.DateTimeFormat('es-ES', { day: 'numeric', month: 'long', year: 'numeric' }).format(new Date());

	// Evita mostrar un acceso redundante en el propio centro de módulos.
	get mostrarBotonModulos(): boolean {

		return !this.router.url.startsWith('/accesoModulos');

	}

	// Empresa del usuario conectado.
	usuarioEmpresa: string = '';	
	
  	// Nombre del usuario conectado.
  	usuarioUsuario: string = '';

	// Perfil del usuario conectado.
  	usuarioPerfil: string = '';

  	// Constructor.
	constructor(private router: Router, private adjuntoService: AdjuntoService) {

		// Obtiene el nombre del usuario.
		this.usuarioEmpresa = localStorage.getItem('empresa') || '',
		
		// Obtiene el nombre del usuario.
    	this.usuarioUsuario = localStorage.getItem('usuario') || '';

    	// Obtiene el perfil.
    	this.usuarioPerfil = localStorage.getItem('perfil') || '';

  }

  // Vuelve a módulos.
  modulos() {

    // Los módulos se abren desde el centro de trabajo en otra pestaña.
    // Al terminar, cierra esa pestaña para recuperar la portada original.
    if (window.opener && !window.opener.closed) {

      window.opener.focus();
      window.close();
      return;

    }

    // Si se accedió directamente, el navegador no permite cerrar la pestaña.
    this.router.navigate(['/accesoModulos']);

  }

	ngOnInit() { this.cargarImagenEmpresa(); }
	ngOnDestroy() { this.blobs.liberarTodas(); }
	private cargarImagenEmpresa() {
		const empresaId = Number(localStorage.getItem('empresaId')) || 0;
		if (!empresaId) return;
		this.adjuntoService.consultar('EMPRESAS', 'EMPRESA', empresaId).subscribe({ next: adjuntos => {
			const principal = adjuntos.find(a => a.adjPri && a.adjAct && a.adjMime?.startsWith('image/'));
			if (!principal) return;
			this.adjuntoService.contenido(principal.adjId, 'RUTA_DOCUMENTOS_EMPRESAS', false).subscribe({ next: imagen => {
				this.blobs.liberar(this.imagenEmpresaUrl);
				this.imagenEmpresaUrl = this.blobs.crear(imagen);
			}});
		}});
	}

  // Cierra la sesión.
  salir() {

    const usuarioRecordado = localStorage.getItem('login.usuarioRecordado');
    localStorage.clear();
    if (usuarioRecordado) localStorage.setItem('login.usuarioRecordado', usuarioRecordado);

    this.router.navigate(['/accesoLogin']);

  }

}
