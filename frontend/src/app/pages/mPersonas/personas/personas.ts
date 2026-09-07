import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { SelectorBusqueda } from '../../../components/selectorBusqueda/selectorBusqueda';
import { DocumentacionAdjunta } from '../../../components/documentacionAdjunta/documentacionAdjunta';
import { SelectorMapa } from '../../../components/selectorMapa/selectorMapa';
import { MapaRegistros } from '../../../components/mapaRegistros/mapaRegistros';

import { FechasUtil } from '../../../shared/utils/fechas.util';

import { Persona } from '../../../interfaces/persona.interface';
import { Domicilio } from '../../../interfaces/domicilio.interface';

import { FormsModule } from '@angular/forms';

import { PersonaService } from '../../../services/persona.service';
import { DomicilioService } from '../../../services/domicilio.service';
import { PdfService } from '../../../services/pdf.service';

import { ViewChild } from '@angular/core';

// Se define la configuración del componente Angular
@Component({
  selector: 'Personas',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, SelectorBusqueda, DocumentacionAdjunta, SelectorMapa, MapaRegistros,DatosIdentificacion,DatosMovimiento,BarraAcciones],
  templateUrl: './personas.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Personas {
	
	//Esto se ejecuta al iniciar la clase y está iniciado para cualquier acción: insertar, modificar, etc.
	ngOnInit() {
		this.modoGestion = this.route.snapshot.data['modoGestion'] === true;
		if (this.modoGestion) this.vistaActiva = 'mapa';

		//Se obtiene la lista de domicilios para disponible en insertar y modificar
		this.domicilioService.obtenerDomicilios().subscribe({

		  next: (respuesta) => {

		    this.domiciliosLista = respuesta;

		  },

		  error: (error) => {

		    console.error(error);

		  }

		});
		
	}
	
	//Busca el componente tabla en el html y guarda en una variable tabla por la cual se podrá acceder a variables y métodos dentro de tabla
	// por ejemplo a 'this.tabla.datosFiltrados' que devolverá los registros que se están mostrando en pantalla después de aplicar los filtros.
	//Sin ViewChild, empresas.ts no sabe nada de lo que ocurre dentro de tabla.ts.
	// Permitirá acceder a los datos filtrados para exportarlos posteriormente a PDF.
	@ViewChild(Tabla)
	tabla!: Tabla;

	//Variables de la clase
	vistaActiva: 'registro' | 'tabla' | 'mapa' | 'adjuntos' | 'historico' = 'tabla';
	modoGestion = false;
	obtenerPersonas = () => this.personaService.obtenerPersonas();
	modoFormulario: 'insertar' | 'modificar' | 'ver' = 'insertar';
	mostrarObligatorios = false;
	
	// Se crea un objeto persona con datos vacíos
	persona: Persona = this.crearPersonaVacia();
	
	// Títulos de las columnas de la tabla
	titulosColumnas: { [key: string]: string } = {
	    empId: 'Empresa',
	    perId: 'Persona',
		perIdHis: 'Id Histórico',
		perTipMov: 'Tipo Movimiento',
		perCauMov: 'Causa Movimiento',
		perTipPer: 'Tipo Persona',
		perRazSocCor: 'Razón Social Corta',
		perRazSocLar: 'Razón Social Larga',
	    perTipDoc: 'Tipo Documento',
	    perDoc: 'Documento',
		perNomCom: 'Nombre Completo',
	    perNom: 'Nombre',
	    perApe1: 'apellido 1',
	    perApe2: 'Apellido 2',
	    perFecNac: 'Fecha Nacimiento',
	    perTel: 'Teléfono',
	    perEma: 'Correo Electrónico',
	    domId: 'Domicilio',
		perCoX: 'Coordenada X',
		perCoY: 'Coordenada Y',
		perHus: 'Huso UTM',
	    perUsuMov: 'Usuario Mod.',
	    perFecMov: 'Fecha Mod.',
		perAct: 'Activo',
	};
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'empId', 'perId', 'perIdHis', 'perTipMov', 'perCauMov', 'perTipPer',
		'perRazSocCor', 'perRazSocLar',
		'perTipDoc', 'perDoc', 'perNomCom',
		'perNom', 'perApe1', 'perApe2', 'perFecNac', 
		'perTel', 'perEma', 'domId', 'perCoX', 'perCoY', 'perHus',
		'perUsuMov', 'perFecMov', 'perAct'

	];
	
	// Datos de la tabla
	datos: any[] = [];
	datosHistorico: Persona[] = [];
	
	// Lista para el selector de domicilios
	domiciliosLista: Domicilio[] = [];
	
	// Guarda el registro seleccionado de la tabla
	personaSeleccionada: Persona | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private readonly route: ActivatedRoute,
	  private personaService: PersonaService,
	  private domicilioService: DomicilioService,
	  private pdfService: PdfService
	  
	) {}

	mapa() { this.vistaActiva = 'mapa'; }

	abrirPersonaDesdeMapa(persona: Persona): void {
		this.personaSeleccionada = persona;
	}

	seleccionarDomicilio(domId: number) {
		this.persona.domId = domId;
		const domicilio = this.domiciliosLista.find(d => d.domId === domId);
		if (!domicilio) return;
		this.persona.perCoX = domicilio.domCoX;
		this.persona.perCoY = domicilio.domCoY;
		this.persona.perHus = domicilio.domHus;
	}

	// Este método muestra la tabla de datos
	consultar() {

		this.vistaActiva = 'tabla';
		this.personaSeleccionada = null;

		this.personaService.obtenerPersonas().subscribe({

			next: (respuesta) => {

				this.datos = respuesta;
				
				//console.log(respuesta);

			},

			error: (error) => {

				console.error(error);

				avisarAplicacion('Error al obtener personas');

			}

		});

	}

	// Muestra la documentación de la persona seleccionada.
	adjuntos() {

		if (!this.personaSeleccionada?.perId) {

			avisarAplicacion('Debe seleccionar un registro.');

			return;

		}

		this.vistaActiva = 'adjuntos';

	}

	ver(): void {
		if (!this.personaSeleccionada) return;
		if (this.vistaActiva === 'mapa') this.vistaActiva = 'tabla';
		this.modificar();
		if (this.vistaActiva === 'registro') {
			this.modoFormulario = 'ver';
			this.persona.perTipMov = this.personaSeleccionada.perTipMov;
			this.persona.perCauMov = this.personaSeleccionada.perCauMov;
		}
	}

	// Regresa a la tabla conservando la persona seleccionada.
	volverAConsulta() {

		this.vistaActiva = 'tabla';

	}

	historico() {

		if (!this.personaSeleccionada?.perId) {
			avisarAplicacion('Debe seleccionar un registro.');
			return;
		}

		this.personaService.obtenerHistorico(this.personaSeleccionada.perId).subscribe({
			next: respuesta => {
				this.datosHistorico = respuesta;
				this.personaSeleccionada = respuesta.find(persona => persona.perAct) || this.personaSeleccionada;
				this.vistaActiva = 'historico';
			},
			error: error => {
				console.error(error);
				avisarAplicacion('Error al obtener el histórico de la persona.');
			}
		});

	}

	async deshacer() {

		if (!this.personaSeleccionada || this.personaSeleccionada.perIdHis <= 1) return;
		if (!await confirmarAplicacion('¿Desea deshacer el último movimiento de la persona?')) return;

		this.personaService.deshacer(this.personaSeleccionada.perId).subscribe({
			next: persona => {
				this.personaSeleccionada = persona;
				avisarAplicacion('Movimiento deshecho correctamente.');
				this.historico();
			},
			error: error => {
				console.error(error);
				avisarAplicacion('Error al deshacer el movimiento.');
			}
		});

	}

	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {
		
		this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';

		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Persona.
		this.personaService.obtenerSiguienteId().subscribe({

		  next: (id) => {


			console.log('ID recibido:', id);
		    this.persona.perId = id;

		  },

		  error: (error) => {

		    console.error(error);

		  }
		});
		
	}
	
	// Este método modifica	
	modificar() {
	  this.modoFormulario = 'modificar';
	  if (this.vistaActiva === 'mapa') this.vistaActiva = 'tabla';

	  // Si estamos en la pestaña registro
	  if (this.vistaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.vistaActiva = 'tabla';

	    return;
	  }

	  // La selección puede proceder de la tabla o del mapa.
	  if (this.vistaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.personaSeleccionada) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;
	    }

	    if (this.personaSeleccionada.perTipMov === 'B') {
	      avisarAplicacion('Una persona dada de baja no se puede modificar. Deshaga primero la baja.');
	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend per_id a formato frontend idPersona
		this.persona = {

			empId: this.personaSeleccionada.empId,			
			perId: this.personaSeleccionada.perId,
			perIdHis: this.personaSeleccionada.perIdHis,
			perTipMov: 'M',
			perCauMov: 'Modificación del registro',
			perTipPer: this.personaSeleccionada.perTipPer || 'FISICA',
			perRazSocCor: this.personaSeleccionada.perRazSocCor || '',
			perRazSocLar: this.personaSeleccionada.perRazSocLar || '',

		  	perTipDoc: this.personaSeleccionada.perTipDoc,
		  	perDoc: this.personaSeleccionada.perDoc,
			perNomCom: this.personaSeleccionada.perNomCom,

		 	perNom: this.personaSeleccionada.perNom,
		  	perApe1: this.personaSeleccionada.perApe1,
		  	perApe2: this.personaSeleccionada.perApe2,
		 	perFecNac: this.personaSeleccionada.perFecNac,

		  	perTel: this.personaSeleccionada.perTel,
			perEma: this.personaSeleccionada.perEma,

		  	domId: this.personaSeleccionada.domId,
			perCoX: this.personaSeleccionada.perCoX,
			perCoY: this.personaSeleccionada.perCoY,
			perHus: this.personaSeleccionada.perHus,

			perUsuMov: this.personaSeleccionada.perUsuMov,
		  	perFecMov: this.personaSeleccionada.perFecMov,
			perAct: this.personaSeleccionada.perAct

		};

	  }

	}

	async baja() {
	  if (!this.personaSeleccionada?.perId || this.personaSeleccionada.perTipMov === 'B') return;
	  if (!await confirmarAplicacion('¿Desea dar de baja la persona seleccionada? El movimiento quedará registrado en el histórico.',true)) return;
	  this.personaService.baja(this.personaSeleccionada.perId).subscribe({
		next: persona => {
		  this.personaSeleccionada = persona;
		  avisarAplicacion('Persona dada de baja correctamente.');
		  this.consultar();
		},
		error: error => {
		  console.error(error);
		  avisarAplicacion(error?.error?.mensaje || error?.error?.message || 'No se pudo dar de baja la persona.');
		}
	  });
	}

	async reactivar() {
	  if (!this.personaSeleccionada || this.personaSeleccionada.perTipMov !== 'B') return;
	  if (!await confirmarAplicacion('¿Desea reactivar la persona seleccionada?')) return;
	  this.personaService.deshacer(this.personaSeleccionada.perId).subscribe({next:()=>this.consultar(),error:e=>{console.error(e);avisarAplicacion(e?.error?.mensaje||'No se pudo reactivar la persona.');}});
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
	  if (this.vistaActiva === 'tabla' || this.vistaActiva === 'mapa') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.personaSeleccionada) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = await confirmarAplicacion(
	      '¿Desea eliminar definitivamente la persona y todos sus movimientos históricos?'
	    ,true);

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.personaService.eliminar(
	      this.personaSeleccionada.perId!
	    ).subscribe({

	      next: () => {

	        avisarAplicacion('Persona e histórico eliminados correctamente');

	        // Limpia selección
	        this.personaSeleccionada = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        avisarAplicacion('Error al eliminar persona');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'personas.pdf',
			
			// Título del documento.
			'Listado de personas',

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
/*
		// Comprueba los campos obligatorios
		if (
			!this.persona.perTipDoc ||
			!this.persona.perDoc ||
			!this.persona.perNom ||
			!this.persona.perApe1 ||
			!this.persona.domId 
		) {

			// Muestra el mensaje
			avisarAplicacion('Debe rellenar todos los campos obligatorios.');

			// Indica que el formulario no es válido
			return false;

		}
*/
		// Indica que el formulario es válido
		return true;

	}

	// Este método guarda el contenido del formulario en base de datos
	guardar() {
		
		//Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

	  	const persona = {

			empId: this.persona.empId,
			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.
		    perId: 0,
			perIdHis: 1,
			perTipMov: 'A' as const,
			perCauMov: this.persona.perCauMov || 'Alta del registro',
			perTipPer: this.persona.perTipPer,
			perRazSocCor: this.persona.perRazSocCor,
			perRazSocLar: this.persona.perRazSocLar,
	
		    perTipDoc: this.persona.perTipDoc,
		    perDoc: this.persona.perDoc,
			perNomCom: this.persona.perNomCom,
	
		    perNom: this.persona.perNom,
		    perApe1: this.persona.perApe1,
		    perApe2: this.persona.perApe2,
		    perFecNac: this.persona.perFecNac,
			
		    perTel: this.persona.perTel,
		    perEma: this.persona.perEma,
	
		    domId: this.persona.domId,
			perCoX: this.persona.perCoX,
			perCoY: this.persona.perCoY,
			perHus: this.persona.perHus,
	
		    perUsuMov: this.persona.perUsuMov,
		    perFecMov: this.persona.perFecMov,
			perAct: this.persona.perAct

	  	};
	  
	  //Datos en consola de la persona que se va a guardar
	  console.log('GUARDAR DATOS PERSONA:', persona);

	  this.personaService.guardar(persona).subscribe({

	    next: () => {

	      avisarAplicacion('Persona guardada correctamente.');
		  
		  this.limpiarFormulario();

		  this.consultar();

	    },

	    error: (error: any) => {
			
	    	console.error(error);

	    }

	  });

	}
	
	// Este método actualiza el contenido del formulario en base de datos
	actualizar() {
		
		// Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

		const persona = {

			empId: this.persona.empId,
			perId: this.persona.perId,
			perIdHis: this.persona.perIdHis,
			perTipMov: 'M' as const,
			perCauMov: this.persona.perCauMov || 'Modificación del registro',
			perTipPer: this.persona.perTipPer,
			perRazSocCor: this.persona.perRazSocCor,
			perRazSocLar: this.persona.perRazSocLar,

			perTipDoc: this.persona.perTipDoc,
			perDoc: this.persona.perDoc,
			perNomCom: this.persona.perNomCom,

			perNom: this.persona.perNom,
			perApe1: this.persona.perApe1,
			perApe2: this.persona.perApe2,
			perFecNac: this.persona.perFecNac,

			perTel: this.persona.perTel,
			perEma: this.persona.perEma,

			domId: this.persona.domId,
			perCoX: this.persona.perCoX,
			perCoY: this.persona.perCoY,
			perHus: this.persona.perHus,

			perUsuMov: this.persona.perUsuMov,
			perFecMov: this.persona.perFecMov,
			perAct: this.persona.perAct

		};

		// Datos en consola de la persona que se va a actualizar
		console.log('ACTUALIZAR DATOS PERSONA:', persona);

		this.personaService.actualizar(persona).subscribe({

			next: () => {

				avisarAplicacion('Persona actualizada correctamente.');

				this.limpiarFormulario();

				this.consultar();

			},

			error: (error: any) => {

				console.error(error);

				avisarAplicacion('Error al actualizar persona.');

			}

		});

	}
	
	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/personas']);
	}	

	// Este método crea una persona vacía
 	private crearPersonaVacia(): Persona {

		return {

		empId: Number(localStorage.getItem('empresaId')) || 0,	
	  	perId: 0,
		perIdHis: 1,
		perTipMov: 'A',
		perCauMov: 'Alta del registro',
		perTipPer: 'FISICA',
		perRazSocCor: '',
		perRazSocLar: '',
		
	  	perTipDoc: 'D.N.I.',
	  	perDoc: '',
		perNomCom: '',
	  	perNom: '',
	  	perApe1: '',
	  	perApe2: '',
	  	perFecNac: '',
		
	  	perTel: '',
	  	perEma: '',
		
	  	domId: 0,
		perCoX: 0,
		perCoY: 0,
		perHus: 25830,
		
		perUsuMov: localStorage.getItem('usuario') || '',
	  	perFecMov: FechasUtil.formatearFechaHora(),
		perAct: true,
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.persona = this.crearPersonaVacia();

	}
	
	// Actualiza la dirección completa.
	actualizarNombreCompleto() {
	  if (this.persona.perTipPer === 'JURIDICA') {
		const partes = [this.persona.perDoc, this.persona.perRazSocLar].filter(Boolean);
		this.persona.perNomCom = partes.join(' - ');
		return;
	  }

	  const partes: string[] = [];

	  // Documento.
	  if (this.persona.perDoc) { partes.push(this.persona.perDoc + ' - '); }
	  
	  // Nombre.
	  if (this.persona.perNom) { partes.push(this.persona.perNom); }

	  // Apellido 1.
	  if (this.persona.perApe1) { partes.push(this.persona.perApe1); }

	  // Apellido 2.
	  if (this.persona.perApe2) { partes.push(this.persona.perApe2); }

	  // Construye el nombre completo.
	  this.persona.perNomCom = partes.join(' ');

	}

	cambiarTipoPersona() {
	  if (this.persona.perTipPer === 'JURIDICA') {
		this.persona.perTipDoc = 'C.I.F.';
		this.persona.perNom = '';
		this.persona.perApe1 = '';
		this.persona.perApe2 = '';
	  } else {
		this.persona.perTipDoc = 'D.N.I.';
		this.persona.perRazSocCor = '';
		this.persona.perRazSocLar = '';
	  }
	  this.actualizarNombreCompleto();
	}
	
}
