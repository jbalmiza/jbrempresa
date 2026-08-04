// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { SelectorBusqueda } from '../../../components/selectorBusqueda/selectorBusqueda';

import { FechasUtil } from '../../../core/utils/fechas.util';

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
  imports: [CommonModule, FormsModule, Sidebar, Supbar,  Tabla, SelectorBusqueda],
  templateUrl: './personas.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Personas {
	
	//Esto se ejecuta al iniciar la clase y está iniciado para cualquier acción: insertar, modificar, etc.
	ngOnInit() {

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
	//Sin ViewChild, clientes.ts no sabe nada de lo que ocurre dentro de tabla.ts.
	// Permitirá acceder a los datos filtrados para exportarlos posteriormente a PDF.
	@ViewChild(Tabla)
	tabla!: Tabla;

	//Variables de la clase
	vistaActiva: 'registro' | 'tabla' = 'tabla';
	modoFormulario: 'insertar' | 'modificar' = 'insertar';
	mostrarObligatorios = false;
	
	// Se crea un objeto persona con datos vacíos
	persona: Persona = this.crearPersonaVacia();
	
	// Títulos de las columnas de la tabla
	titulosColumnas: { [key: string]: string } = {
	    cliId: 'Cliente',
	    perId: 'Persona',
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
	    perUsuMov: 'Usuario Mod.',
	    perFecMov: 'Fecha Mod.',
		perAct: 'Activo',
	};
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'perId', 
		'perTipDoc', 'perDoc', 'perNomCom',
		'perNom', 'perApe1', 'perApe2', 'perFecNac', 
		'perTel', 'perEma', 'domId', 
		'perUsuMov', 'perFecMov', 'perAct'

	];
	
	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de domicilios
	domiciliosLista: Domicilio[] = [];
	
	// Guarda el registro seleccionado de la tabla
	personaSeleccionada: Persona | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private personaService: PersonaService,
	  private domicilioService: DomicilioService,
	  private pdfService: PdfService
	  
	) {}

	// Este método muestra la tabla de datos
	consultar() {

		this.vistaActiva = 'tabla';

		this.personaService.obtenerPersonas().subscribe({

			next: (respuesta) => {

				this.datos = respuesta;
				
				//console.log(respuesta);

			},

			error: (error) => {

				console.error(error);

				alert('Error al obtener personas');

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

	  // Si estamos en la pestaña registro
	  if (this.vistaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.vistaActiva = 'tabla';

	    return;
	  }

	  // Si estamos en la pestaña tabla
	  if (this.vistaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.personaSeleccionada) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend per_id a formato frontend idPersona
		this.persona = {

			cliId: this.personaSeleccionada.cliId,			
			perId: this.personaSeleccionada.perId,

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

			perUsuMov: this.personaSeleccionada.perUsuMov,
		  	perFecMov: this.personaSeleccionada.perFecMov,
			perAct: this.personaSeleccionada.perAct

		};

	  }

	}
	
	// Este método elimina
	eliminar() {

	  // Si estamos en la pestaña registro
	  if (this.vistaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.vistaActiva = 'tabla';

	    return;

	  }

	  // Si estamos en la pestaña tabla
	  if (this.vistaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.personaSeleccionada) {

	      alert('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = confirm(
	      '¿Desea eliminar la persona seleccionada?'
	    );

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.personaService.eliminar(
	      this.personaSeleccionada.perId!
	    ).subscribe({

	      next: () => {

	        alert('Persona eliminada correctamente');

	        // Limpia selección
	        this.personaSeleccionada = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        alert('Error al eliminar persona');

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
			alert('Debe rellenar todos los campos obligatorios.');

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

			cliId: this.persona.cliId,
			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.
		    perId: 0,
	
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
	
		    perUsuMov: this.persona.perUsuMov,
		    perFecMov: this.persona.perFecMov,
			perAct: this.persona.perAct

	  	};
	  
	  //Datos en consola de la persona que se va a guardar
	  console.log('GUARDAR DATOS PERSONA:', persona);

	  this.personaService.guardar(persona).subscribe({

	    next: () => {

	      alert('Persona guardada correctamente.');
		  
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

			cliId: this.persona.cliId,
			perId: this.persona.perId,

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

			perUsuMov: this.persona.perUsuMov,
			perFecMov: this.persona.perFecMov,
			perAct: this.persona.perAct

		};

		// Datos en consola de la persona que se va a actualizar
		console.log('ACTUALIZAR DATOS PERSONA:', persona);

		this.personaService.actualizar(persona).subscribe({

			next: () => {

				alert('Persona actualizada correctamente.');

				this.limpiarFormulario();

				this.consultar();

			},

			error: (error: any) => {

				console.error(error);

				alert('Error al actualizar persona.');

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

		cliId: Number(localStorage.getItem('clienteId')) || 0,	
	  	perId: 0,
		
	  	perTipDoc: '',
	  	perDoc: '',
		perNomCom: '',
	  	perNom: '',
	  	perApe1: '',
	  	perApe2: '',
	  	perFecNac: '',
		
	  	perTel: '',
	  	perEma: '',
		
	  	domId: 0,
		
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
	
}