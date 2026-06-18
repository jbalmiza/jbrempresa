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
  styleUrl: './domicilios.css'
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
	pestanaActiva: 'registro' | 'tabla' = 'tabla';
	
	//Interruptor inactivo para controlar campos obligatorios
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
	    domObs: 'Observaciones',
	    domDir: 'Dirección Postal',
	    domAct: 'Activo',
	    usuMov: 'Usuario Mod.',
	    fecMov: 'Fecha Mod.'
	};	
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'domId', 
		'domTipVia', 'domVia', 'domNum', 
		'domKm', 'domEdi', 'domBlo', 'domPor', 'domEsc', 'domPla', 'domPue', 
		'domObs', 'domDir',
	  	'domAct', 'usuMov', 'fecMov'

	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	domicilioSeleccionado: any = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private domicilioService: DomicilioService,
		private pdfService: PdfService
	
	) {}

	 // Este método muestra la tabla de datos
	 consultar() {

	 	this.pestanaActiva = 'tabla';

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

		this.pestanaActiva = 'registro';

	  	this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Domicilio.
		this.domicilioService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('ID recibido:', id);
		    this.domicilio.idDomicilio = id;
			
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
	    if (!this.domicilioSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.pestanaActiva = 'registro';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.domicilio = {

			idCliente: this.domicilioSeleccionado.cliId,	
			
		 	idDomicilio: this.domicilioSeleccionado.domId,

		  	tipoVia: this.domicilioSeleccionado.domTipVia,

		  	via: this.domicilioSeleccionado.domVia,

			numero: this.domicilioSeleccionado.domNum,

			km: this.domicilioSeleccionado.domKm,

			edificio: this.domicilioSeleccionado.domEdi,

			bloque: this.domicilioSeleccionado.domBlo,

			portal: this.domicilioSeleccionado.domPor,

			escalera: this.domicilioSeleccionado.domEsc,

			planta: this.domicilioSeleccionado.domPla,

			puerta: this.domicilioSeleccionado.domPue,

			observaciones: this.domicilioSeleccionado.domObs,
		  
		  	direccion: this.domicilioSeleccionado.domDir,

		  	activo: this.domicilioSeleccionado.domAct,

			usuarioMovimiento: this.domicilioSeleccionado.usuMov,

			fechaMovimiento: this.domicilioSeleccionado.fecMov

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
	      this.domicilioSeleccionado.domId
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
		  !this.domicilio.tipoVia || 
		  !this.domicilio.via || 
		  !this.domicilio.numero
		  
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const domicilio = {

		cliId: this.domicilio.idCliente,
		
	    domId: null,

	    domTipVia: this.domicilio.tipoVia,

	    domVia: this.domicilio.via,

	    domNum: this.domicilio.numero,

	    domKm: this.domicilio.km,

	    domEdi: this.domicilio.edificio,
		
		domBlo: this.domicilio.bloque,

	    domPor: this.domicilio.portal,

	    domEsc: this.domicilio.escalera,

	    domPla: this.domicilio.planta,

	    domPue: this.domicilio.puerta,
		
		domObs: this.domicilio.observaciones,
		
		domDir: this.domicilio.direccion,

	    domAct: this.domicilio.activo,

	    usuMov: this.domicilio.usuarioMovimiento,

	    fecMov: this.domicilio.fechaMovimiento,

	  };
	  
	  console.log('DOMICILIO A ENVIAR:', domicilio);

	  this.domicilioService.guardar(domicilio).subscribe({

	    next: () => {

	      alert('Domicilio guardado correctamente');

	    },

	    error: (error: any) => {

	      console.error(error);

	      alert('Error al guardar domicilio');

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

		idCliente: Number(localStorage.getItem('clienteId')) || 0,
	    idDomicilio: null,
		
	    tipoVia: '',
	    via: '',
	    numero: '',
		
	    km: '',
	    edificio: '',
	    bloque: '',
	    portal: '',
	    escalera: '',
	    planta: '',
	    puerta: '',
		
	    observaciones: '',
		direccion: '',
		
	    activo: true,
		usuarioMovimiento: localStorage.getItem('usuario') || '',
	    fechaMovimiento: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16)

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
	   if (this.domicilio.tipoVia) { partes.push(this.domicilio.tipoVia); }

	   // Vía.
	   if (this.domicilio.via) { partes.push(this.domicilio.via); }

	   // Número.
	   if (this.domicilio.numero) { partes.push('Nº ' + this.domicilio.numero); }

	   // Kilómetro.
	   if (this.domicilio.km) { partes.push('Km: ' + this.domicilio.km); }

	   // Edificio.
	   if (this.domicilio.edificio) { partes.push('Edif. ' + this.domicilio.edificio); }

	   // Bloque.
	   if (this.domicilio.bloque) { partes.push('Bloque ' + this.domicilio.bloque); }

	   // Portal.
	   if (this.domicilio.portal) { partes.push('Portal ' + this.domicilio.portal); }

	   // Escalera.
	   if (this.domicilio.escalera) { partes.push('Esc. ' + this.domicilio.escalera); }

	   // Planta.
	   if (this.domicilio.planta) { partes.push('Planta ' + this.domicilio.planta); }

	   // Puerta.
	   if (this.domicilio.puerta) { partes.push('Pta. ' + this.domicilio.puerta); }

	   // Observaciones.
	   if (this.domicilio.observaciones) { partes.push(this.domicilio.observaciones); }

	   // Construye la dirección.
	   this.domicilio.direccion = partes.join(' - ');

	 }
	
}