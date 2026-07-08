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
  styleUrl: '../../../styles/estiloGeneral.css'
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
	vistaActiva: 'registro' | 'tabla' = 'tabla';
	modoFormulario: 'insertar' | 'modificar' = 'insertar';
	mostrarObligatorios = false;
	
	// Se crea un objeto producto con datos vacíos
	producto: Producto = this.crearProductoVacio();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Id Cliente',
	    proId: 'Id Producto',
	    proTipPro: 'Tipo Producto',
	    proNom: 'Nombre',
		proDes: 'Descripción',
		proCat: 'Categoría',
		proSubCat: 'Subcategoría',
		proMar: 'Marca',
		porMod: 'Modelo',
		proPro: 'Proveedor',
		proPreCom: 'Precio Compra',
		proPreVen: 'Precio Venta',
		proPreDes: 'Descuento',
		proPreIva: 'I.V.A.',
		proPreFin: 'Precio Final',
		proStoAct: 'Stock Actual',
		proStoMin: 'Stock Mínimo',
		proUniMed: 'Unidad Medida',
		proConSto: 'Control Stock',
		proObs: 'Observaciones',
	    proUsuMov: 'Usuario Mod.',
	    proFecMov: 'Fecha Mod.',
		proAct: 'Activo',
	};	

	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'proId', 
		'proTipPro', 'proNom', 'proDes',
		'proCat', 'proSubCat', 'proMar', 'proMod', 'proPro',
		'proPreCom', 'proPreVen', 'proPreDes', 'proPreIva', 'proPreFin', 
		'proStoAct', 'proStoMin', 'proUniMed', 'proConSto',
		'proObs', 
		'proUsuMov', 'proFecMov','proAct'

	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	productoSeleccionado: Producto | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private productoService: ProductoService,
	  private pdfService: PdfService
	  
	) {}

	// Este método muestra la tabla de datos
	consultar() {

		this.vistaActiva = 'tabla';

		this.productoService.obtenerProductos().subscribe({

			next: (respuesta) => {

				this.datos = respuesta;

			},

			error: (error) => {

				console.error(error);

				alert('Error al obtener productos.');

			}

		});

	}

	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {
		
		this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';

		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Producto.
		this.productoService.obtenerSiguienteId().subscribe({

		  next: (id) => {


			console.log('ID recibido:', id);
		    this.producto.proId = id;

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
	    if (!this.productoSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend pro_id a formato frontend idProducto
		this.producto = {

			cliId: this.productoSeleccionado.cliId,			
			proId: this.productoSeleccionado.proId,

		  	proTipPro: this.productoSeleccionado.proTipPro,
			proNom: this.productoSeleccionado.proNom,
			proDes: this.productoSeleccionado.proDes,
			
			proCat: this.productoSeleccionado.proCat,
			proSubCat: this.productoSeleccionado.proSubCat,
			proMar: this.productoSeleccionado.proMar,
			proMod: this.productoSeleccionado.proMod,
			proPro: this.productoSeleccionado.proPro,
			
			proPreCom: this.productoSeleccionado.proPreCom,
			proPreVen: this.productoSeleccionado.proPreVen,
			proPreDes: this.productoSeleccionado.proPreDes,
			proPreIva: this.productoSeleccionado.proPreIva,
			proPreFin: this.productoSeleccionado.proPreFin,
			
			proStoAct: this.productoSeleccionado.proStoAct,
			proStoMin: this.productoSeleccionado.proStoMin,
			proUniMed: this.productoSeleccionado.proUniMed,
			proConSto: this.productoSeleccionado.proConSto,
			
			proObs: this.productoSeleccionado.proObs,
			
			proUsuMov: this.productoSeleccionado.proUsuMov,
		  	proFecMov: this.productoSeleccionado.proFecMov,
			proAct: this.productoSeleccionado.proAct

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
	      this.productoSeleccionado.proId!,
		  this.productoSeleccionado.cliId
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
			!this.producto.proTipPro ||
		  	!this.producto.proNom ||
			!this.producto.proCat ||
			!this.producto.proMar ||
			!this.producto.proPro ||
			!this.producto.proPreCom ||
			!this.producto.proPreVen ||
			!this.producto.proPreIva 
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const producto = {

		cliId: this.producto.cliId,		
	    proId: null,

	    proTipPro: this.producto.proTipPro,
	    proNom: this.producto.proNom,
		proDes: this.producto.proDes,
		
		proCat: this.producto.proCat,
		proSubCat: this.producto.proSubCat,
		proMar: this.producto.proMar,
		proMod: this.producto.proMod,
		proPro: this.producto.proPro,
		
		proPreCom: this.producto.proPreCom,
		proPreVen: this.producto.proPreVen,
		proPreDes: this.producto.proPreDes,
		proPreIva: this.producto.proPreIva,
		proPreFin: this.producto.proPreFin,
		
		proStoAct: this.producto.proStoAct,
		proStoMin: this.producto.proStoMin,
		proUniMed: this.producto.proUniMed,
		proConSto: this.producto.proConSto,
		
		proObs: this.producto.proObs,

	    usuMov: this.producto.proUsuMov,
	    fecMov: this.producto.proFecMov,
		proAct: this.producto.proAct

	  };

	  this.productoService.guardar(producto).subscribe({

	    next: () => {

	      alert('Producto guardado correctamente.');
		  
		  this.limpiarFormulario();

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

		// Alerta para determinados campos sin valor (obligatorios)
		if (
			!this.producto.proTipPro ||
			!this.producto.proNom ||
			!this.producto.proCat ||
			!this.producto.proMar ||
			!this.producto.proPro ||
			!this.producto.proPreCom ||
			!this.producto.proPreVen ||
			!this.producto.proPreIva
		) {

			alert('Debe rellenar todos los campos obligatorios');

			return;

		}

		const producto = {

			cliId: this.producto.cliId,

			// Mantiene el identificador del producto que se va a modificar
			proId: this.producto.proId,

			proTipPro: this.producto.proTipPro,
			proNom: this.producto.proNom,
			proDes: this.producto.proDes,

			proCat: this.producto.proCat,
			proSubCat: this.producto.proSubCat,
			proMar: this.producto.proMar,
			proMod: this.producto.proMod,
			proPro: this.producto.proPro,

			proPreCom: this.producto.proPreCom,
			proPreVen: this.producto.proPreVen,
			proPreDes: this.producto.proPreDes,
			proPreIva: this.producto.proPreIva,
			proPreFin: this.producto.proPreFin,

			proStoAct: this.producto.proStoAct,
			proStoMin: this.producto.proStoMin,
			proUniMed: this.producto.proUniMed,
			proConSto: this.producto.proConSto,

			proObs: this.producto.proObs,

			proUsuMov: this.producto.proUsuMov,
			proFecMov: this.producto.proFecMov,
			proAct: this.producto.proAct

		};

		console.log('PRODUCTO A ACTUALIZAR:', producto);

		this.productoService.actualizar(producto).subscribe({

			next: () => {

				alert('Producto actualizado correctamente.');

				this.limpiarFormulario();

			},

			error: (error: any) => {

				console.error(error);

				alert('Error al actualizar producto.');

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

		cliId: Number(localStorage.getItem('clienteId')) || 0,
	  	proId: 0,
		
	  	proTipPro: '',
	  	proNom: '',
		proDes: '',
		
		proCat: '',
		proSubCat: '',
		proMar: '',
		proMod: '',
		proPro: '',
		
		proPreCom: 0,
		proPreVen: 0,
		proPreDes: 0,
		proPreIva: 0,
		proPreFin: 0,
		
		proStoAct: 0,
		proStoMin: 0,
		proUniMed: '',
		proConSto: true,
		
		proObs: '',
	  
		proUsuMov: localStorage.getItem('usuario') || '',
		proFecMov: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16),
		proAct: true,
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.producto = this.crearProductoVacio();

	}
	
	// Recalcula el campo precio final
	actualizarPrecioFinal() {

	  const base =
	      (this.producto.proPreVen || 0)
	    - (this.producto.proPreDes || 0);

	  this.producto.proPreFin =
	      base + (base * (this.producto.proPreIva || 0) / 100);

	}
	
}