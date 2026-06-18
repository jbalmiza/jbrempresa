// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';

import { Supbar } from '../../../components/supbar/supbar';

import { Producto } from '../../../models/producto.interface';

import { FormsModule } from '@angular/forms';

import { ProductoService } from '../../../services/producto.service';

import { Tabla } from '../../../components/tabla/tabla';

import { ViewChild } from '@angular/core';

import { PdfService } from '../../../services/pdf.service';

// Se define la configuración del componente Angular
@Component({
  selector: 'Productos',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar, Tabla],
  templateUrl: './productos.html',
  styleUrl: './productos.css'
})

// Definición de la lógica del componente 
export class Productos {
	
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
	producto: Producto = this.crearProductoVacio();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Id Cliente',
	    proId: 'Id Producto',
	    proTipPro: 'Tipo Producto',
	    proNom: 'Nombre',
	    proAct: 'Activo',
	    usuMov: 'Usuario Mod.',
	    fecMov: 'Fecha Mod.'
	};	

	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'proId', 
		'proTipPro', 'proNom', 
		'proAct', 'usuMov', 'fecMov'

	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	productoSeleccionado: any = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private productoService: ProductoService,
	  private pdfService: PdfService
	  
	) {}

	// Este método muestra la tabla de datos
	consultar() {

		this.pestanaActiva = 'tabla';

		this.productoService.obtenerProductos().subscribe({

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
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Producto.
		this.productoService.obtenerSiguienteId().subscribe({

		  next: (id) => {


			console.log('ID recibido:', id);
		    this.producto.idProducto = id;

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
	    if (!this.productoSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.pestanaActiva = 'registro';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend pro_id a formato frontend idProducto
		this.producto = {

			idCliente: this.productoSeleccionado.cliId,			
			
			idProducto: this.productoSeleccionado.proId,

		  	tipoProducto: this.productoSeleccionado.proTipPro,

			nombre: this.productoSeleccionado.proNom,

		  	activo: this.productoSeleccionado.proAct,

			usuarioMovimiento: this.productoSeleccionado.usuMov,

		  	fechaMovimiento: this.productoSeleccionado.fecMov

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
	    if (!this.productoSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = confirm(
	      '¿Desea eliminar el producto seleccionado?'
	    );

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.productoService.eliminar(
	      this.productoSeleccionado.proId
	    ).subscribe({

	      next: () => {

	        alert('Producto eliminado correctamente');

	        // Limpia selección
	        this.productoSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        alert('Error al eliminar producto');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'productos.pdf',
			
			// Título del documento.
			'Listado de productos',

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
		  !this.producto.nombre
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const producto = {

		cliId: this.producto.idCliente,		
		
	    proId: null,

	    proTipPro: this.producto.tipoProducto,

	    proNom: this.producto.nombre,

	    proAct: this.producto.activo,

	    usuMov: this.producto.usuarioMovimiento,

	    fecMov: this.producto.fechaMovimiento,

	  };

	  this.productoService.guardar(producto).subscribe({

	    next: () => {

	      alert('Producto guardado');

	    },

	    error: (error: any) => {

	      console.error(error);

	    }

	  });

	}
	
	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/productos']);
	}	

	// Este método crea una persona vacía
 	private crearProductoVacio(): Producto {

		return {

		idCliente: Number(localStorage.getItem('clienteId')) || 0,
	  	idProducto: null,
		
	  	tipoProducto: '',
	  	nombre: '',
	  
	  	activo: true,
		usuarioMovimiento: localStorage.getItem('usuarioUsuario') || '',
		fechaMovimiento: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16)
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.producto = this.crearProductoVacio();

	}
	
}