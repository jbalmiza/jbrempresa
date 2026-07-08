// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';

import { Supbar } from '../../../components/supbar/supbar';

import { Cliente } from '../../../models/cliente.interface';

import { FormsModule } from '@angular/forms';

import { ClienteService } from '../../../services/cliente.service';

import { Tabla } from '../../../components/tabla/tabla';

import { ViewChild } from '@angular/core';

import { PdfService } from '../../../services/pdf.service';

// Se define la configuración del componente Angular
@Component({
  selector: 'Clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar, Tabla],
  templateUrl: './clientes.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Clientes {
	
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
	
	// Se crea un objeto usuario con datos vacíos
	cliente: Cliente = this.crearClienteVacio();
	

	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Id Cliente',
	    cliNom: 'Nombre',
	    cliUsuMov: 'Usuario Mod.',
	    cliFecMov: 'Fecha Mod.',
		cliAct: 'Activo'
	};

	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 
		'cliNom', 
		'cliUsuMov', 'cliFecMov', 'cliAct' ];	
	
	// Datos de la tabla
	datos: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	clienteSeleccionado: Cliente | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private clienteService: ClienteService,
		private pdfService: PdfService
		
	) {}

	// Este método muestra la tabla de datos
	consultar() {
			
		this.vistaActiva = 'tabla';	 
		
		this.clienteService.obtenerClientes().subscribe({
			
			next: (respuesta) => {
	
				console.log('respuesta=', respuesta);

				this.datos = respuesta;

			},

			error: (error) => {

				console.error(error);

				alert('Error al obtener clientes');  

			}

		});
			
	}
		
	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {

	  	this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';
		
		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Usuario.
		this.clienteService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('Id Cliente recibido:', id);

		    this.cliente.cliId = id;

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
	    if (!this.clienteSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.cliente = {

			cliId: this.clienteSeleccionado.cliId,			
			
			cliNom: this.clienteSeleccionado.cliNom,

			cliUsuMov: this.clienteSeleccionado.cliUsuMov,

			cliFecMov: this.clienteSeleccionado.cliFecMov,
			
			cliAct: this.clienteSeleccionado.cliAct,

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
	    if (!this.clienteSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = confirm(
	      '¿Desea eliminar el cliente seleccionado?'
	    );

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.clienteService.eliminar(
	      this.clienteSeleccionado.cliId!
	    ).subscribe({

	      next: () => {

	        alert('Cliente eliminado correctamente');

	        // Limpia selección
	        this.clienteSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        alert('Error al eliminar cliente');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'clientes.pdf',
			
			// Título del documento.
			'Listado de clientes',

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
		  !this.cliente.cliNom
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const cliente = {

		cliId: null,		
		
	    cliNom: this.cliente.cliNom,

	    cliUsuMov: this.cliente.cliUsuMov,

	    cliFecMov: this.cliente.cliFecMov,
		
		cliAct: this.cliente.cliAct,

	  };
	  
	  console.log(cliente);
	  
	  this.clienteService.guardar(cliente).subscribe({

	    next: () => {

	      alert('Cliente guardado correctamente');
		  
		  this.limpiarFormulario();

	    },

	    error: (error: any) => {

	      console.error(error);

	      alert('Error al guardar cliente');

	    }

	  });

	}
	
	// Este método actualiza el contenido del formulario en base de datos
	actualizar() {
		
		// Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Alerta para determinados campos sin valor (obligatorios)
		if (
		  !this.cliente.cliNom
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

		const cliente = {

			// Mantiene el identificador del cliente que se va a modificar
			cliId: this.cliente.cliId,

		    cliNom: this.cliente.cliNom,

		    cliUsuMov: this.cliente.cliUsuMov,

		    cliFecMov: this.cliente.cliFecMov,

			cliAct: this.cliente.cliAct

		};

		console.log('CLIENTE A ACTUALIZAR:', cliente);

		this.clienteService.actualizar(cliente).subscribe({

			next: () => {

				alert('Cliente actualizado correctamente.');

				this.limpiarFormulario();

			},

			error: (error: any) => {

				console.error(error);

				alert('Error al actualizar cliente.');

			}

		});

	}

	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/administracion']);
	}	
	
	// Este método crea un usuario vacío
 	private crearClienteVacio(): Cliente {

		return {

	  	cliId: 0,
		
	  	cliNom: '',
		
		cliUsuMov: localStorage.getItem('usuario') || '',
		cliFecMov: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16),
		cliAct: true

		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.cliente = this.crearClienteVacio();
	  
	 }
	
}