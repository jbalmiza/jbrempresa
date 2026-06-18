// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';

import { Supbar } from '../../../components/supbar/supbar';

import { Persona } from '../../../models/persona.interface';

import { FormsModule } from '@angular/forms';

import { PersonaService } from '../../../services/persona.service';

// Permite utilizar los servicios de domicilio, para obtener la lista de domicilios en el selector.
import { DomicilioService } from '../../../services/domicilio.service';

import { Tabla } from '../../../components/tabla/tabla';

import { ViewChild } from '@angular/core';

import { PdfService } from '../../../services/pdf.service';

import { SelectorBusqueda } from '../../../components/selectorBusqueda/selectorBusqueda';

// Se define la configuración del componente Angular
@Component({
  selector: 'Personas',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar,  Tabla, SelectorBusqueda],
  templateUrl: './personas.html',
  styleUrl: './personas.css'
})

// Definición de la lógica del componente 
export class Personas {
	
	//Busca el componente tabla en el html y guarda en una variable tabla por la cual se podrá acceder a variables y métodos dentro de tabla
	// por ejemplo a 'this.tabla.datosFiltrados' que devolverá los registros que se están mostrando en pantalla después de aplicar los filtros.
	//Sin ViewChild, clientes.ts no sabe nada de lo que ocurre dentro de tabla.ts.
	// Permitirá acceder a los datos filtrados para exportarlos posteriormente a PDF.
	@ViewChild(Tabla)
	tabla!: Tabla;

	//Variables de la clase
	pestanaActiva: 'registro' | 'tabla' = 'tabla';
	
	//Interruptor inactivo para controlar campos obligatorios
	mostrarObligatorios = false;
	
	// Se crea un objeto persona con datos vacíos
	persona: Persona = this.crearPersonaVacia();
	
	// Títulos de las columnas de la tabla
	titulosColumnas: { [key: string]: string } = {
	    cliId: 'Cliente',
	    perId: 'Persona',
	    perTipDoc: 'Tipo Documento',
	    perDoc: 'Documento',
	    perNom: 'Nombre',
	    perApe1: 'apellido 1',
	    perApe2: 'Apellido 2',
	    perFecNac: 'Fecha Nacimiento',
	    perTel: 'Teléfono',
	    perEma: 'Correo Electrónico',
	    domId: 'Domicilio',
	    perAct: 'Activo',
	    usuMov: 'Usuario Mod.',
	    fecMov: 'Fecha Mod.'
	};
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'perId', 
		'perTipDoc', 'perDoc', 
		'perNom', 'perApe1', 'perApe2', 'perFecNac', 
		'perTel', 'perEma', 'domId', 
		'perAct', 'usuMov', 'fecMov'

	];
	
	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de domicilios
	domiciliosLista: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	personaSeleccionada: any = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private personaService: PersonaService,
	  private domicilioService: DomicilioService,
	  private pdfService: PdfService
	  
	) {}

	// Este método muestra la tabla de datos
	consultar() {

		this.pestanaActiva = 'tabla';

		this.personaService.obtenerPersonas().subscribe({

			next: (respuesta) => {

				this.datos = respuesta;

			},

			error: (error) => {

				console.error(error);

				alert('Error al obtener personas');

			}

		});

	}

	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {
		
		this.pestanaActiva = 'registro';

		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Persona.
		this.personaService.obtenerSiguienteId().subscribe({

		  next: (id) => {


			console.log('ID recibido:', id);
		    this.persona.idPersona = id;

		  },

		  error: (error) => {

		    console.error(error);

		  }
		});
		
		// Obtiene todos los domicilios registrados. Se utilizarán para rellenar el selector del campo Id Domicilio.
		this.domicilioService.obtenerDomicilios().subscribe({

		  next: (respuesta) => {

		    this.domiciliosLista = respuesta;
			
			console.log('LISTA DE DOMICILIOS:', this.domiciliosLista);

		  },

		  error: (error) => {

		    console.error(error);

		  }

		});
		
	}
	
	// Este método modifica	
	modificar() {

	  // Si estamos en la pestaña registro
	  if (this.pestanaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.pestanaActiva = 'tabla';

	    return;
	  }

	  // Si estamos en la pestaña tabla
	  if (this.pestanaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.personaSeleccionada) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.pestanaActiva = 'registro';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend per_id a formato frontend idPersona
		this.persona = {

			idCliente: this.personaSeleccionada.cliId,			
			
			idPersona: this.personaSeleccionada.perId,

		  	tipoDocumento: this.personaSeleccionada.perTipDoc,

		  	documento: this.personaSeleccionada.perDoc,

		 	nombre: this.personaSeleccionada.perNom,

		  	apellido1: this.personaSeleccionada.perApe1,

		  	apellido2: this.personaSeleccionada.perApe2,

		 	fechaNacimiento: this.personaSeleccionada.perFecNac,

		  	telefono: this.personaSeleccionada.perTel,

			email: this.personaSeleccionada.perEma,

		  	idDomicilio: this.personaSeleccionada.domId,

		  	activo: this.personaSeleccionada.perAct,

			usuarioMovimiento: this.personaSeleccionada.usuMov,

		  	fechaMovimiento: this.personaSeleccionada.fecMov

		};

	  }

	}
	
	// Este método elimina
	// Este método elimina
	eliminar() {

	  // Si estamos en la pestaña registro
	  if (this.pestanaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.pestanaActiva = 'tabla';

	    return;

	  }

	  // Si estamos en la pestaña tabla
	  if (this.pestanaActiva === 'tabla') {

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
	      this.personaSeleccionada.perId
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

	// Este método guarda el contenido del formulario en base de datos
	guardar() {
		
		//Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		//Alerta para determinados campos sin valor (obligatorios)
		if (
		  !this.persona.tipoDocumento ||
		  !this.persona.documento ||
		  !this.persona.nombre ||
		  !this.persona.apellido1 ||
		  !this.persona.idDomicilio ||
		  !this.persona.telefono
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const persona = {

		cliId: this.persona.idCliente,
		
	    perId: null,

	    perTipDoc: this.persona.tipoDocumento,

	    perDoc: this.persona.documento,

	    perNom: this.persona.nombre,

	    perApe1: this.persona.apellido1,

	    perApe2: this.persona.apellido2,

	    perFecNac: this.persona.fechaNacimiento,

	    perTel: this.persona.telefono,

	    perEma: this.persona.email,

	    domId: this.persona.idDomicilio,

	    perAct: this.persona.activo,

	    usuMov: this.persona.usuarioMovimiento,

	    fecMov: this.persona.fechaMovimiento,

	  };
	  
	  //Datos en consola de la persona que se va a guardar
	  console.log('GUARDAR DATOS PERSONA:', persona);

	  this.personaService.guardar(persona).subscribe({

	    next: () => {

	      alert('Persona guardada');

	    },

	    error: (error: any) => {
			
	    	console.error(error);

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

		idCliente: Number(localStorage.getItem('clienteId')) || 0,	
	  	idPersona: null,
		
	  	tipoDocumento: '',
	  	documento: '',
	  	nombre: '',
	  	apellido1: '',
	  	apellido2: '',
	  	fechaNacimiento: '',
		
	  	telefono: '',
	  	email: '',
		
	  	idDomicilio: 0,
		
	  	activo: true,
		usuarioMovimiento: localStorage.getItem('usuario') || '',
	  	fechaMovimiento: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16)
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.persona = this.crearPersonaVacia();

	}
	
}