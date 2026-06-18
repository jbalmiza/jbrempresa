// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';

import { Supbar } from '../../../components/supbar/supbar';

import { Venta } from '../../../models/venta.interface';

import { FormsModule } from '@angular/forms';

import { VentaService } from '../../../services/venta.service';

import { Tabla } from '../../../components/tabla/tabla';

import { ViewChild } from '@angular/core';

import { PdfService } from '../../../services/pdf.service';

// Se define la configuración del componente Angular
@Component({
  selector: 'Ventas',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar, Tabla],
  templateUrl: './ventas.html',
  styleUrl: './ventas.css'
})

// Definición de la lógica del componente 
export class Ventas {
	
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
	
	// Se crea un objeto producto con datos vacíos
	venta: Venta = this.crearVentaVacia();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Id Cliente',
	    venId: 'Id Venta',
	    perId: 'Id Persona',
	    proId: 'Id Producto',
	    venAct: 'Activo',
	    usuMov: 'Usuario Mod.',
	    fecMov: 'Fecha Mod.'
	};	
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'venId', 
		'perId', 'proId', 
		'venAct', 'usuMov', 'fecMov'

	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	ventaSeleccionada: any = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private ventaService: VentaService,
	  private pdfService: PdfService
	  
	) {}

	// Este método muestra la tabla de datos
	consultar() {

		this.pestanaActiva = 'tabla';

		this.ventaService.obtenerVentas().subscribe({

			next: (respuesta) => {

				this.datos = respuesta;

			},

			error: (error) => {

				console.error(error);

				alert('Error al obtener ventas');

			}

		});

	}

	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {
		
		this.pestanaActiva = 'registro';

		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Venta.
		this.ventaService.obtenerSiguienteId().subscribe({

		  next: (id) => {


			console.log('ID recibido:', id);
		    this.venta.idVenta = id;

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
	    if (!this.ventaSeleccionada) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.pestanaActiva = 'registro';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend ven_id a formato frontend idVenta
		this.venta = {

			idCliente: this.ventaSeleccionada.cliId,
			
		  	idVenta: this.ventaSeleccionada.venId,

		  
			idPersona: this.ventaSeleccionada.perId,

		  	idProducto: this.ventaSeleccionada.proId,

		  
			activo: this.ventaSeleccionada.venAct,

			usuarioMovimiento: this.ventaSeleccionada.usuMov,

		  	fechaMovimiento: this.ventaSeleccionada.fecMov

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
	    if (!this.ventaSeleccionada) {

	      alert('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = confirm(
	      '¿Desea eliminar la venta seleccionada?'
	    );

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.ventaService.eliminar(
	      this.ventaSeleccionada.venId
	    ).subscribe({

	      next: () => {

	        alert('Venta eliminada correctamente');

	        // Limpia selección
	        this.ventaSeleccionada = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        alert('Error al eliminar venta');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'ventas.pdf',
			
			// Título del documento.
			'Listado de ventas',

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
		  !this.venta.idPersona || 
		  !this.venta.idProducto
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const venta = {

		
		cliId: this.venta.idCliente,
		
	    venId: null,

	    
		perId: this.venta.idPersona,

	    proId: this.venta.idProducto,

	   
		venAct: this.venta.activo,

	    usuMov: this.venta.usuarioMovimiento,

	    fecMov: this.venta.fechaMovimiento,

	  };

	  this.ventaService.guardar(venta).subscribe({

	    next: () => {

	      alert('Venta guardada');

	    },

	    error: (error: any) => {

	      console.error(error);

	    }

	  });

	}
	
	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/ventas']);
	}	

	// Este método crea una venta vacía
 	private crearVentaVacia(): Venta {

		return {

		idCliente: Number(localStorage.getItem('clienteId')) || 0,
	  	idVenta: null,
		
	  	idPersona: 0,
	  	idProducto: 0,
	  
	  	activo: true,
		usuarioMovimiento: localStorage.getItem('usuario') || '',
	  	fechaMovimiento: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16)
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.venta = this.crearVentaVacia();

	}
	
}