import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { ChangeDetectorRef, Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { DatosPersonaRelacion } from '../../../components/datosPersonaRelacion/datosPersonaRelacion';

import { FechasUtil } from '../../../shared/utils/fechas.util';

import { Usuario } from '../../../interfaces/usuario.interface';
import { Persona } from '../../../interfaces/persona.interface';
import { Perfil } from '../../../interfaces/perfil.interface';

import { FormsModule } from '@angular/forms';

import { UsuarioService } from '../../../services/usuario.service';
import { PersonaService } from '../../../services/persona.service';
import { PerfilService } from '../../../services/perfil.service';
import { PdfService } from '../../../services/pdf.service';

import { ViewChild } from '@angular/core';
import { finalize } from 'rxjs';

// Se define la configuración del componente Angular
@Component({
  selector: 'Usuarios',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, DatosPersonaRelacion,DatosIdentificacion,DatosMovimiento,BarraAcciones],
  templateUrl: './usuarios.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Usuarios {
	ngOnInit() {
		this.personaService.obtenerPersonas().subscribe(datos => {this.personasLista = datos.filter(p => p.perTipMov !== 'B');this.datos=this.datos.map(usuario=>({...usuario,personaNomCom:this.personasLista.find(persona=>Number(persona.perId)===Number(usuario.usuPerId))?.perNomCom||''}));});
		this.perfilService.obtenerPerfiles().subscribe(datos => this.perfilesLista = datos);
	}
	
	//Busca el componente tabla en el html y guarda en una variable tabla por la cual se podrá acceder a variables y métodos dentro de tabla
	// por ejemplo a 'this.tabla.datosFiltrados' que devolverá los registros que se están mostrando en pantalla después de aplicar los filtros.
	//Sin ViewChild, empresas.ts no sabe nada de lo que ocurre dentro de tabla.ts.
	// Permitirá acceder a los datos filtrados para exportarlos posteriormente a PDF.
	@ViewChild(Tabla)
	tabla!: Tabla;

	//Variables de la clase
	vistaActiva: 'registro' | 'tabla' = 'tabla';
	modoFormulario: 'insertar' | 'modificar' = 'insertar';
	mostrarObligatorios = false;
	
	// Se crea un objeto usuario con datos vacíos
	usuario: Usuario = this.crearUsuarioVacio();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    empId: 'Empresa',
	    usuId: 'Id Usuario',
	    usuUsu: 'Usuario',
	    perId: 'Perfil',
		personaNomCom: 'Persona',
	    usuNom: 'Nombre',
		usuTel: 'Teléfono',
	    usuEma: 'Correo Electrónico',
	    usuUsuMov: 'Usuario Mod.',
	    usuFecMov: 'Fecha Mod.',
		usuAct: 'Activo'
	};
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'empId', 'usuId', 
		'usuUsu', 'perId', 'personaNomCom',
		'usuNom', 'usuTel', 'usuEma',
	  	'usuUsuMov', 'usuFecMov', 'usuAct'
	  
	];

	// Datos de la tabla
	datos: any[] = [];

	// Estado de la consulta paginada de usuarios.
	cargando = false;
	totalUsuarios = 0;
	paginaUsuarios = 1;
	
	// Lista para el selector de perfiles
	usuariosLista: Usuario[] = [];
	personasLista: Persona[] = [];
	perfilesLista: Perfil[] = [];
	
	// Guarda el registro seleccionado de la tabla
	usuarioSeleccionado: Usuario | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private usuarioService: UsuarioService,
		private personaService: PersonaService,
		private perfilService: PerfilService,
		private pdfService: PdfService,
		private readonly changeDetectorRef: ChangeDetectorRef
		
	) {}

	seleccionarPersona(perId: number): void {
		this.usuario.usuPerId = perId;
		const persona = this.personasLista.find(item => Number(item.perId) === Number(perId));
		if (persona) {
			this.usuario.usuNom = persona.perNomCom || '';
			this.usuario.usuTel = persona.perTel || '';
			this.usuario.usuEma = persona.perEma || '';
		} else {
			this.usuario.usuNom = '';
			this.usuario.usuTel = '';
			this.usuario.usuEma = '';
		}
	}

	// Este método muestra la tabla de datos
	consultar() {

		this.consultarPagina(1, this.tabla?.registrosPorPagina || 50);
			
	}

	// Consulta una página concreta usando los filtros escritos en la tabla.
	consultarPagina(pagina: number, tamanio: number) {

		this.vistaActiva = 'tabla';
		this.cargando = true;

		const filtros = this.tabla?.filtros || {};

		this.usuarioService.consultar(filtros, pagina - 1, tamanio)
			.pipe(finalize(() => {
				this.cargando = false;
				this.changeDetectorRef.markForCheck();
			}))
			.subscribe({
			
			next: (respuesta) => {
	
				this.datos = respuesta.content.map(usuario => ({...usuario, personaNomCom: this.personasLista.find(persona => Number(persona.perId) === Number(usuario.usuPerId))?.perNomCom || ''}));
				this.totalUsuarios = respuesta.totalElements;
				this.paginaUsuarios = respuesta.number + 1;
				this.usuarioSeleccionado = null;

			},

			error: (error) => {

				console.error(error);

				avisarAplicacion('Error al obtener usuarios');  

			}

			});
			
	}
		
	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {

	  	this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';
		
		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Usuario.
		this.usuarioService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('Id Usuario recibido:', id);

		    this.usuario.usuId = id;

		  },

		  error: (error) => {

		    console.error(error);

		  }
		});
		
		// Obtiene todos los usuarios registrados. Se utilizarán para rellenar el selector del campo Id Perfil.
		this.usuarioService.obtenerUsuarios().subscribe({

		  next: (respuesta) => {

			console.log('Id Perfil recibidos:', respuesta);
			
		    this.usuariosLista = respuesta;

		  },

		  error: (error) => {

		    console.error(error);

		  }

		});
	}
	
	// Este método modifica	
	modificar() {

	  // Si estamos en la pestaña registro
	  if (this.vistaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.vistaActiva = 'tabla';

	    return;
	  }

	  // Si estamos en la pestaña tabla
	  if (this.vistaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.usuarioSeleccionado) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.usuario = {

			empId: this.usuarioSeleccionado.empId,			
			usuId: this.usuarioSeleccionado.usuId,

			usuUsu: this.usuarioSeleccionado.usuUsu,
			usuCon: '',
			perId: this.usuarioSeleccionado.perId,
			usuPerId: this.usuarioSeleccionado.usuPerId,

			usuNom: this.usuarioSeleccionado.usuNom,
			usuTel: this.usuarioSeleccionado.usuTel,
			usuEma: this.usuarioSeleccionado.usuEma,

			usuUsuMov: this.usuarioSeleccionado.usuUsuMov,
			usuFecMov: this.usuarioSeleccionado.usuFecMov,
			usuAct: this.usuarioSeleccionado.usuAct

			};

		}

	}
	
	// Este método elimina
	async eliminar() {

	  // Si estamos en la pestaña registro
	  if (this.vistaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.vistaActiva = 'tabla';

	    return;

	  }

	  // Si estamos en la pestaña tabla
	  if (this.vistaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.usuarioSeleccionado) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = await confirmarAplicacion(
	      '¿Desea eliminar el usuario seleccionado?'
	    ,true);

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.usuarioService.eliminar(
	      this.usuarioSeleccionado.usuId!
	    ).subscribe({

	      next: () => {

	        avisarAplicacion('Usuario eliminado correctamente');

	        // Limpia selección
	        this.usuarioSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        avisarAplicacion('Error al eliminar usuario');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'usuarios.pdf',
			
			// Título del documento.
			'Listado de usuarios',

	        // Columnas de la tabla.
	        this.columnas,

	        // Registros filtrados actualmente visibles.
	        this.tabla.datosFiltrados,
			
			'assets/logos/logo-greensaas.png',

			'assets/logos/logo-jbrempresa.png'

	    );

	}
	
	// Comprueba que los campos obligatorios están informados
	private validarObligatorios(): boolean {

		// Comprueba los campos obligatorios
		if (
			!this.usuario.usuUsu || 
			(this.modoFormulario === 'insertar' && !this.usuario.usuCon) ||
			!this.usuario.perId || 
			!this.usuario.usuPerId ||
			!this.usuario.usuNom || 
			!this.usuario.usuEma
		) {

			// Muestra el mensaje
			avisarAplicacion('Debe rellenar todos los campos obligatorios.');

			// Indica que el formulario no es válido
			return false;

		}

		// Indica que el formulario es válido
		return true;

	}
	
	// Este método guarda el contenido del formulario en base de datos
	guardar() {
		
		//Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

	  	const usuario = {

			empId: this.usuario.empId,	
			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.	
		    usuId: 0,
	
		    usuUsu: this.usuario.usuUsu,
		    usuCon: this.usuario.usuCon,
			perId: this.usuario.perId,
			usuPerId: this.usuario.usuPerId,
	
		    usuNom: this.usuario.usuNom,
			usuTel: this.usuario.usuTel,
		    usuEma: this.usuario.usuEma,
	
		    usuUsuMov: this.usuario.usuUsuMov,
		    usuFecMov: this.usuario.usuFecMov,
			usuAct: this.usuario.usuAct

	  	};
	  
	  this.usuarioService.guardar(usuario).subscribe({

	    next: () => {

	      avisarAplicacion('Usuario guardado correctamente');
		  
		  this.limpiarFormulario();

		  this.consultar();

	    },

	    error: (error: any) => {

	      console.error(error);

	      avisarAplicacion('Error al guardar usuario');

	    }

	  });

	}
	
	// Este método actualiza el contenido del formulario en base de datos
	actualizar() {
		
		// Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

		const usuario = {

			empId: this.usuario.empId,
		    usuId: this.usuario.usuId,

		    usuUsu: this.usuario.usuUsu,
		    usuCon: this.usuario.usuCon,
			perId: this.usuario.perId,
			usuPerId: this.usuario.usuPerId,

		    usuNom: this.usuario.usuNom,
			usuTel: this.usuario.usuTel,
		    usuEma: this.usuario.usuEma,

		    usuUsuMov: this.usuario.usuUsuMov,
		    usuFecMov: this.usuario.usuFecMov,
			usuAct: this.usuario.usuAct

		};

		this.usuarioService.actualizar(usuario).subscribe({

			next: () => {

				avisarAplicacion('Usuario actualizado correctamente.');

				this.limpiarFormulario();

				this.consultar();

			},

			error: (error: any) => {

				console.error(error);

				avisarAplicacion('Error al actualizar usuario.');

			}

		});

	}

	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/administracion']);
	}	
	
	// Este método crea un usuario vacío
 	private crearUsuarioVacio(): Usuario {

		return {

		empId: Number(localStorage.getItem('empresaId')) || 0,
	  	usuId: 0,
		
	  	usuUsu: '',
	  	usuCon: '',
	  	perId: 0,
		usuPerId: 0,
		
	  	usuNom: '',
		usuTel: '',
	  	usuEma: '',
		
		usuUsuMov: localStorage.getItem('usuario') || '',
		usuFecMov: FechasUtil.formatearFechaHora(),
		usuAct: true
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.usuario = this.crearUsuarioVacio();
	  
	 }
	
}
