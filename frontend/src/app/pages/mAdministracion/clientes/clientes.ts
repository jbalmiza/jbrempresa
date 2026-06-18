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
  styleUrl: './clientes.css'
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
	pestanaActiva: 'registro' | 'tabla' = 'tabla';
	
	//Interruptor inactivo para controlar campos obligatorios
	mostrarObligatorios = false;
	
	// Se crea un objeto usuario con datos vacíos
	cliente: Cliente = this.crearClienteVacio();
	

	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Id Cliente',
	    cliNom: 'Nombre',
	    cliAct: 'Activo',
	    usuMov: 'Usuario Mod.',
	    fecMov: 'Fecha Mod.'
	};

	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 
		'cliNom',
		'cliAct', 'usuMov', 'fecMov'
	  
	];	
	
	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de perfiles
	clientesLista: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	clienteSeleccionado: any = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private clienteService: ClienteService,
		private pdfService: PdfService
		
	) {}

	// Este método muestra la tabla de datos
	consultar() {
			
		this.pestanaActiva = 'tabla';	 
		
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

	  	this.pestanaActiva = 'registro';
		
		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Usuario.
		this.clienteService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('Id Cliente recibido:', id);

		    this.cliente.idCliente = id;

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
	    if (!this.clienteSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.pestanaActiva = 'registro';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.cliente = {

			idCliente: this.clienteSeleccionado.cliId,			
			

			nombre: this.clienteSeleccionado.cliNom,
			
			
			activo: this.clienteSeleccionado.cliAct,

			usuarioMovimiento: this.clienteSeleccionado.usuMov,

			fechaMovimiento: this.clienteSeleccionado.fecMov

			};

		}

	}
	
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
	      this.clienteSeleccionado.cliId
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
		  !this.cliente.nombre
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const cliente = {

		cliId: null,		
		

	    cliNom: this.cliente.nombre,

		
	    cliAct: this.cliente.activo,

	    usuMov: this.cliente.usuarioMovimiento,

	    fecMov: this.cliente.fechaMovimiento

	  };
	  
	  console.log(cliente);
	  
	  this.clienteService.guardar(cliente).subscribe({

	    next: () => {

	      alert('Cliente guardado correctamente');
		  
		  // Cambia a la pestaña registro
		  this.pestanaActiva = 'tabla';

	    },

	    error: (error: any) => {

	      console.error(error);

	      alert('Error al guardar cliente');

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

	  	idCliente: null,
		
	  	nombre: '',
		
	  	activo: true,
		usuarioMovimiento: localStorage.getItem('usuario') || '',
		fechaMovimiento: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16)

		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.cliente = this.crearClienteVacio();
	  
	 }
	
}