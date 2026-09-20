// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa Component para crear componentes Angular.
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';

// Importa CommonModule.
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Importa Router para navegación.
import { Router } from '@angular/router';
import { AdjuntoService } from '../../services/adjunto.service';
import { BlobUrlUtil } from '../../shared/utils/blob-url.util';
import { EmpresaService } from '../../services/empresa.service';
import { Empresa } from '../../interfaces/empresa.interface';
import { AgendaService } from '../../services/agenda.service';
import { RecursoAgenda } from '../../interfaces/agenda.interface';
import { ContextoSesionService } from '../../core/session/contexto-sesion.service';
import { ChatInterno } from '../chatInterno/chatInterno';
import { MensajeriaInternaService } from '../../services/mensajeria-interna.service';
import { AvisoAlertaService } from '../../services/aviso-alerta.service';
import { forkJoin } from 'rxjs';
import { AvisosVentana } from '../avisosVentana/avisosVentana';
import {navegarModuloEnMismaPestana} from '../../shared/utils/navegacion-modulos.util';

// Configuración del componente.
@Component({
  selector: 'supbar',
  standalone: true,
  imports: [CommonModule, FormsModule, ChatInterno, AvisosVentana],
  templateUrl: './supbar.html',
  styleUrl: './supbar.css'
})

// Definición de la lógica del componente.
export class Supbar implements OnInit, OnDestroy {
	private readonly blobs=new BlobUrlUtil();

	imagenEmpresaUrl = '';
	contadorMensajes = 0;
	private refrescoMensajes?: ReturnType<typeof setInterval>;
	desplegable: 'usuario'|'mensajes'|null = null;
	readonly diaSemana = new Intl.DateTimeFormat('es-ES', { weekday: 'long' }).format(new Date());
	readonly fechaActual = new Intl.DateTimeFormat('es-ES', { day: 'numeric', month: 'long', year: 'numeric' }).format(new Date());

	// Evita mostrar un acceso redundante en el propio centro de módulos.
	get mostrarBotonModulos(): boolean {

		return !this.router.url.startsWith('/accesoModulos') && this.usuarioPerfil.trim().toUpperCase() !== 'EMPLEADO';

	}

	// Empresa del usuario conectado.
	usuarioEmpresa: string = '';	
	
	// Nombre del usuario conectado.
	usuarioUsuario: string = '';
	usuarioAcceso: string = '';

	// Perfil del usuario conectado.
	usuarioPerfil: string = '';
	esAdministrador = false;
	empresas: Empresa[] = [];
	empresaSeleccionada = 0;
	empleados: RecursoAgenda[] = [];
	empleadoSeleccionado = 0;

  	// Constructor.
	constructor(private router: Router, private adjuntoService: AdjuntoService, private empresasService: EmpresaService, private agendaService: AgendaService, private contexto: ContextoSesionService, private mensajeriaInterna: MensajeriaInternaService,private avisosApi:AvisoAlertaService) {

		// Obtiene el nombre del usuario.
		this.usuarioEmpresa = localStorage.getItem('empresa') || '',
		
		// Obtiene el nombre del usuario.
    	this.usuarioUsuario = localStorage.getItem('usuario') || '';
		this.usuarioAcceso = this.nombreAccesoToken() || localStorage.getItem('login.usuarioRecordado') || this.usuarioUsuario;

    	// Obtiene el perfil.
    	this.usuarioPerfil = localStorage.getItem('perfil') || '';
		this.esAdministrador = this.usuarioPerfil.trim().toUpperCase() === 'ADMINISTRADOR';
		if (this.esAdministrador) {
			this.empresaSeleccionada = Number(localStorage.getItem('contexto.empresaId')) || 0;
			this.usuarioEmpresa = localStorage.getItem('contexto.empresaNombre') || 'Todas las empresas';
			this.empleadoSeleccionado = this.contexto.empleadoAgendaId;
		}

  }

  // Vuelve a módulos.
  modulos() {

    // Los módulos se abren desde el centro de trabajo en otra pestaña.
    // Al terminar, cierra esa pestaña para recuperar la portada original.
    if (!navegarModuloEnMismaPestana() && window.opener && !window.opener.closed) {

      window.opener.focus();
      window.close();
      return;

    }

    // Si se accedió directamente, el navegador no permite cerrar la pestaña.
    this.router.navigate(['/accesoModulos']);

  }
	private nombreAccesoToken():string{try{const token=localStorage.getItem('token')||'';const cuerpo=token.split('.')[1];if(!cuerpo)return '';const base64=cuerpo.replace(/-/g,'+').replace(/_/g,'/');return String(JSON.parse(decodeURIComponent(escape(atob(base64)))).sub||'')}catch{return ''}}

	ngOnInit() { if(this.esAdministrador){this.cargarEmpresas();this.cargarEmpleados();}this.cargarImagenEmpresa();this.cargarMensajes();this.refrescoMensajes=setInterval(()=>this.cargarMensajes(),15000); }
	ngOnDestroy() { this.blobs.liberarTodas();if(this.refrescoMensajes)clearInterval(this.refrescoMensajes); }
	private cargarImagenEmpresa() {
		const empresaId = this.esAdministrador && this.empresaSeleccionada ? this.empresaSeleccionada : Number(localStorage.getItem('empresaId')) || 0;
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
	private cargarEmpresas(){this.empresasService.obtenerEmpresas().subscribe({next:empresas=>this.empresas=empresas.filter(e=>String(e.empAct).toLowerCase()==='true')})}
	private cargarEmpleados(){if(!this.empresaSeleccionada){this.empleados=[];this.contexto.seleccionarEmpleadoAgenda(0);return;}this.agendaService.recursos().subscribe({next:recursos=>{this.empleados=recursos.filter(recurso=>recurso.ragTip==='EMPLEADO');if(!this.empleados.some(recurso=>recurso.ragId===this.empleadoSeleccionado)){this.empleadoSeleccionado=0;this.contexto.seleccionarEmpleadoAgenda(0);}}})}
	cambiarEmpresa(){const empresa=this.empresas.find(e=>Number(e.empId)===Number(this.empresaSeleccionada));this.contexto.seleccionarEmpleadoAgenda(0);localStorage.setItem('contexto.empresaId',String(this.empresaSeleccionada));localStorage.setItem('contexto.empresaNombre',empresa?.empNom||'Todas las empresas');window.location.reload();}
	cambiarEmpleado(){this.contexto.seleccionarEmpleadoAgenda(this.empleadoSeleccionado);}
	private cargarMensajes(){forkJoin({conversaciones:this.mensajeriaInterna.noLeidos(),avisos:this.avisosApi.bandeja()}).subscribe({next:r=>this.contadorMensajes=r.conversaciones.total+r.avisos.filter(a=>!a.leido).length,error:()=>this.contadorMensajes=0})}
	toggle(desplegable:'usuario'|'mensajes',evento:Event){evento.stopPropagation();this.desplegable=this.desplegable===desplegable?null:desplegable;}
	@HostListener('document:click') cerrarDesplegables(){this.desplegable=null;}

  // Cierra la sesión.
  salir() {

    const usuarioRecordado = localStorage.getItem('login.usuarioRecordado');
    localStorage.clear();
    if (usuarioRecordado) localStorage.setItem('login.usuarioRecordado', usuarioRecordado);

    this.router.navigate(['/accesoLogin']);

  }

}
