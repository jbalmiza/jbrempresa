// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';

import { Supbar } from '../../../components/supbar/supbar';

import { Domicilio } from '../../../models/domicilio.interface';

import { FormsModule } from '@angular/forms';

import { DomicilioService } from '../../../services/domicilio.service';

import { Tabla } from '../../../components/tabla/tabla';

import { ViewChild } from '@angular/core';

import { PdfService } from '../../../services/pdf.service';

// Se define la configuración del componente Angular
@Component({
  selector: 'Domicilios',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar, Tabla],
  templateUrl: './domicilios.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Domicilios {
	
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
	
	// Se crea un objeto domicilio con datos vacíos
	domicilio: Domicilio = this.crearDomicilioVacio();

	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Id Cliente',
	    domId: 'Id Domicilio',
	    domTipVia: 'Tipo Vía',
	    domVia: 'Vía',
	    domNum: 'Número',
	    domKm: 'Km',
	    domEdi: 'Edificio',
	    domBlo: 'Bloque',
	    domPor: 'Portal',
	    domEsc: 'Escalera',
	    domPla: 'Planta',
	    domPue: 'Puerta',
		domCp:  'C.P.',
		domMun: 'Municipio',
		domPro: 'Provincia',
	    domObs: 'Observaciones',
	    domDir: 'Dirección Postal',
	    domUsuMov: 'Usuario Mod.',
	    domFecMov: 'Fecha Mod.',
		domAct: 'Activo',
	};	
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'domId', 
		'domTipVia', 'domVia', 'domNum', 
		'domKm', 'domEdi', 'domBlo', 'domPor', 'domEsc', 'domPla', 'domPue', 
		'domCp', 'domMun', 'domPro', 'domObs', 'domDir',
	  	'domUsuMov', 'domFecMov', 'domAct'

	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	domicilioSeleccionado: Domicilio | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private domicilioService: DomicilioService,
		private pdfService: PdfService
	
	) {}

	 // Este método muestra la tabla de datos
	 consultar() {

	 	this.vistaActiva = 'tabla';

	 	this.domicilioService.obtenerDomicilios().subscribe({

	 		next: (respuesta) => {

	 			this.datos = respuesta;

	 		},

	 		error: (error) => {

	 			console.error(error);

	 			alert('Error al obtener domicilios');

	 		}

	 	});

	 }

	 // Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {

		this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';

	  	this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Domicilio.
		this.domicilioService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('ID recibido:', id);
		    this.domicilio.domId = id;
			
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
	    if (!this.domicilioSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.domicilio = {

			cliId: this.domicilioSeleccionado.cliId,	
			
		 	domId: this.domicilioSeleccionado.domId,

		  	domTipVia: this.domicilioSeleccionado.domTipVia,

		  	domVia: this.domicilioSeleccionado.domVia,

			domNum: this.domicilioSeleccionado.domNum,

			domKm: this.domicilioSeleccionado.domKm,

			domEdi: this.domicilioSeleccionado.domEdi,

			domBlo: this.domicilioSeleccionado.domBlo,

			domPor: this.domicilioSeleccionado.domPor,

			domEsc: this.domicilioSeleccionado.domEsc,

			domPla: this.domicilioSeleccionado.domPla,

			domPue: this.domicilioSeleccionado.domPue,
			
			domCp: this.domicilioSeleccionado.domCp,
			
			domMun: this.domicilioSeleccionado.domMun,
			
			domPro: this.domicilioSeleccionado.domPro,

			domObs: this.domicilioSeleccionado.domObs,
		  
		  	domDir: this.domicilioSeleccionado.domDir,

			domUsuMov: this.domicilioSeleccionado.domUsuMov,

			domFecMov: this.domicilioSeleccionado.domFecMov,
			
			domAct: this.domicilioSeleccionado.domAct

		};
		
	  }

	}

	// Este método elimina
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
	    if (!this.domicilioSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = confirm(
	      '¿Desea eliminar el domicilio seleccionado?'
	    );

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.domicilioService.eliminar(
	      this.domicilioSeleccionado.domId!,
		  this.domicilioSeleccionado.cliId
	    ).subscribe({

	      next: () => {

	        alert('Domicilio eliminado correctamente');

	        // Limpia selección
	        this.domicilioSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        alert('Error al eliminar usuario');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'domicilios.pdf',

			// Título del documento.
			'Listado de domicilios',

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
		  !this.domicilio.domTipVia || 
		  !this.domicilio.domVia || 
		  !this.domicilio.domNum ||
		  !this.domicilio.domCp ||
		  !this.domicilio.domMun ||
		  !this.domicilio.domPro
		  
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const domicilio = {

		cliId: this.domicilio.cliId,
		
	    domId: null,

	    domTipVia: this.domicilio.domTipVia,

	    domVia: this.domicilio.domVia,

	    domNum: this.domicilio.domNum,

	    domKm: this.domicilio.domKm,

	    domEdi: this.domicilio.domEdi,
		
		domBlo: this.domicilio.domBlo,

	    domPor: this.domicilio.domPor,

	    domEsc: this.domicilio.domEsc,

	    domPla: this.domicilio.domPla,

	    domPue: this.domicilio.domPue,

		domCp:  this.domicilio.domCp,

		domMun: this.domicilio.domMun,
		
		domPro: this.domicilio.domPro,			
		
		domObs: this.domicilio.domObs,
		
		domDir: this.domicilio.domDir,

	    domUsuMov: this.domicilio.domUsuMov,

	    domFecMov: this.domicilio.domFecMov,
		
		domAct: this.domicilio.domAct

	  };
	  
	  console.log('DOMICILIO A ENVIAR:', domicilio);

	  this.domicilioService.guardar(domicilio).subscribe({

	    next: () => {

	      alert('Domicilio guardado correctamente.');
		  
		  this.limpiarFormulario();

	    },

	    error: (error: any) => {

	      console.error(error);

	      alert('Error al guardar domicilio');

	    }

	  });

	}
	
	// Este método actualiza el contenido del formulario en base de datos
	actualizar() {
		
		// Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Alerta para determinados campos sin valor (obligatorios)
		if (
		  !this.domicilio.domTipVia || 
		  !this.domicilio.domVia || 
		  !this.domicilio.domNum ||
		  !this.domicilio.domCp ||
		  !this.domicilio.domMun ||
		  !this.domicilio.domPro
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

		const domicilio = {

			cliId: this.domicilio.cliId,

		    // Mantiene el identificador del domicilio que se va a modificar
		    domId: this.domicilio.domId,

		    domTipVia: this.domicilio.domTipVia,

		    domVia: this.domicilio.domVia,

		    domNum: this.domicilio.domNum,

		    domKm: this.domicilio.domKm,

		    domEdi: this.domicilio.domEdi,
			
			domBlo: this.domicilio.domBlo,

		    domPor: this.domicilio.domPor,

		    domEsc: this.domicilio.domEsc,

		    domPla: this.domicilio.domPla,

		    domPue: this.domicilio.domPue,

			domCp: this.domicilio.domCp,

			domMun: this.domicilio.domMun,
			
			domPro: this.domicilio.domPro,

			domObs: this.domicilio.domObs,

			domDir: this.domicilio.domDir,

		    domUsuMov: this.domicilio.domUsuMov,

		    domFecMov: this.domicilio.domFecMov,

			domAct: this.domicilio.domAct

		};

		console.log('DOMICILIO A ACTUALIZAR:', domicilio);

		this.domicilioService.actualizar(domicilio).subscribe({

			next: () => {

				alert('Domicilio actualizado correctamente.');

				this.limpiarFormulario();

			},

			error: (error: any) => {

				console.error(error);

				alert('Error al actualizar domicilio.');

			}

		});

	}

	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/territorio']);
	}	
	
	// Este método crea un domicilio vacío
 	private crearDomicilioVacio(): Domicilio {

	  return {

		cliId: Number(localStorage.getItem('clienteId')) || 0,
		domId: 0,
		
	    domTipVia: '', domVia: '', domNum: '',
	    domKm: '', domEdi: '', domBlo: '', domPor: '', domEsc: '', domPla: '', domPue: '',
		domCp: '', domMun: '', domPro: '', domObs: '', domDir: '',
		
		domUsuMov: localStorage.getItem('usuario') || '',
	    domFecMov: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16),
		domAct: true

	  };

	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.domicilio = this.crearDomicilioVacio();
	  
	 }
	 
	 // Actualiza la dirección completa.
	 actualizarDireccion() {

	   const partes: string[] = [];

	   // Tipo de vía.
	   if (this.domicilio.domTipVia) { partes.push(this.domicilio.domTipVia); }

	   // Vía.
	   if (this.domicilio.domVia) { partes.push(this.domicilio.domVia); }

	   // Número.
	   if (this.domicilio.domNum) { partes.push('Nº ' + this.domicilio.domNum); }

	   // Kilómetro.
	   if (this.domicilio.domKm) { partes.push('Km: ' + this.domicilio.domKm); }

	   // Edificio.
	   if (this.domicilio.domEdi) { partes.push('Edif. ' + this.domicilio.domEdi); }

	   // Bloque.
	   if (this.domicilio.domBlo) { partes.push('Bloque ' + this.domicilio.domBlo); }

	   // Portal.
	   if (this.domicilio.domPor) { partes.push('Portal ' + this.domicilio.domPor); }

	   // Escalera.
	   if (this.domicilio.domEsc) { partes.push('Esc. ' + this.domicilio.domEsc); }

	   // Planta.
	   if (this.domicilio.domPla) { partes.push('Planta ' + this.domicilio.domEsc); }

	   // Puerta.
	   if (this.domicilio.domPue) { partes.push('Pta. ' + this.domicilio.domPue); }
	   
	   // CP.
	   if (this.domicilio.domCp) { partes.push(this.domicilio.domCp); }
	   
	   // Municipio.
	   if (this.domicilio.domMun) { partes.push(this.domicilio.domMun); }
	   
	   // Provincia.
	   if (this.domicilio.domPro) { partes.push(this.domicilio.domPro); }

	   // Observaciones.
	   if (this.domicilio.domObs) { partes.push(this.domicilio.domObs); }

	   // Construye la dirección.
	   this.domicilio.domDir = partes.join(' - ');

	 }
	
}