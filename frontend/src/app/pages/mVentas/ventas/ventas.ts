// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { SelectorBusqueda } from '../../../components/selectorBusqueda/selectorBusqueda';
import { TablaEdicion } from '../../../components/tablaEdicion/tablaEdicion';

import { FechasUtil } from '../../../core/utils/fechas.util';

import { FormsModule } from '@angular/forms';

import { VentaService } from '../../../services/venta.service';
import { PersonaService } from '../../../services/persona.service';
import { ProductoService } from '../../../services/producto.service';
import { PdfService } from '../../../services/pdf.service';

import { Venta } from '../../../interfaces/venta.interface';
import { Persona } from '../../../interfaces/persona.interface';
import { Producto } from '../../../interfaces/producto.interface';
import { VentaDetalle } from '../../../interfaces/ventaDetalle.interface';

// Permite acceder a un componente hijo para utilizar sus variables y métodos.
// Ejemplo acceder desde ventas a : this.tabla.datosFiltrados
import { ViewChild } from '@angular/core';

import { TablaColumna } from '../../../directives/tablaColumna/tablaColumna';

// Se define la configuración del componente Angular
@Component({
  selector: 'Ventas',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar, Tabla, SelectorBusqueda, TablaEdicion, TablaColumna],
  templateUrl: './ventas.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Ventas {
	
	//Esto se ejecuta al iniciar la clase y está iniciado para cualquier acción: insertar, modificar, etc.
	ngOnInit() {

		//Se obtiene la lista de personas / productos disponible en insertar y modificar
		this.personaService.obtenerPersonas().subscribe({

		  next: (respuesta) => {

		    this.personasLista = respuesta;

		  },

		  error: (error) => {

		    console.error(error);

		  }

		});
		
		this.productoService.obtenerProductos().subscribe({

		    next: (respuesta) => {

		        this.productosLista = respuesta;

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
	
	// Se crea un objeto producto con datos vacíos
	venta: Venta = this.crearVentaVacia();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Id Cliente',
	    venId: 'Id Venta',
		
	    perIdVen: 'Id Vendedor',
		perIdCom: 'Id Comprador',		

		venImpSub: 'Importe Subtotal',
		venImpDes: 'Importe Descuento',
		venImpIva: 'Importe IVA',
		venImpTot: 'Importe Total',
		venImpCob: 'Importe Cobrado',
		venImpPen: 'Importe Pendiente',
		
		venFecPre: 'Fecha Presupuesto',
		venFecPed: 'Fecha Pedido',
		venFecAlb: 'Fecha Albarán',
		venFecFac: 'Fecha Factura',
		venFecCob: 'Fecha Cobro',
		
	    venUsuMov: 'Usuario Mod.',
	    venFecMov: 'Fecha Mod.',
		venAct: 'Activo'
	};	
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'venId', 
		'perIdVen', 'perIdCom', 
		'venImpSub', 'venImpDes', 'venImpIva', 'venImpTot', 'venImpCob', 'venImpPen', 
		'venFecPre', 'venFecPed', 'venFecAlb', 'venFecFac', 'venFecCob', 
		'venUsuMov', 'venFecMov', 'venAct'

	];
	
	// Títulos de las columnas del detalle de la venta.
	titulosColumnasDetalle = {

	    proId: 'Producto',
		
	    venDetCan: 'Cantidad',
	    venDetPre: 'Precio',
		venDetDes: '% Descuento',
		venDetIva: '% I.V.A.',
	    venDetImp: 'Importe',
		
	    accion: 'Acción'

	};

	// Campos mostrados en la tabla de detalle.
	columnasDetalle: string[] = [

	    'proId',
		
	    'venDetCan',
	    'venDetPre',
		'venDetDes',
		'venDetIva',
	    'venDetImp',

	    'accion'

	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de personas
	personasLista: Persona[] = [];
	productosLista: Producto[] = [];

	// Lista para el selector de productos	
	ventaDetalleLista: VentaDetalle[] = [];
	
	// Guarda el registro seleccionado de la tabla
	ventaSeleccionada: Venta | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private ventaService: VentaService,
	  private personaService: PersonaService,
	  private productoService: ProductoService,
	  private pdfService: PdfService
	  
	) {}

	// Este método muestra la tabla de datos
	consultar() {

		this.vistaActiva = 'tabla';

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
		
		this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';

		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Venta.
		this.ventaService.obtenerSiguienteId().subscribe({

		  next: (id) => {


			console.log('ID recibido:', id);
		    this.venta.venId = id;

		  },

		  error: (error) => {

		    console.error(error);

		  }
		});
		
		// Obtiene todas las personas registradas. Se utiliza para rellenar el selector del campo Id Persona.
		this.personaService.obtenerPersonas().subscribe({

		  next: (respuesta) => {

		    this.personasLista = respuesta;
			
			console.log('LISTA DE PERSONAS:', this.personasLista);

		  },

		  error: (error) => {

		    console.error(error);

		  }

		});
		
		//Al pulsar insertar venta se inserta también una línea detalle por defecto.
		this.eliminarTodasVentaDetalle()
		this.insertarVentaDetalle();
		
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
	    if (!this.ventaSeleccionada) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend ven_id a formato frontend idVenta
		this.venta = {

			cliId: this.ventaSeleccionada.cliId,
		  	venId: this.ventaSeleccionada.venId,

			perIdVen: this.ventaSeleccionada.perIdVen,
			perIdCom: this.ventaSeleccionada.perIdCom,

			
			venImpSub: this.ventaSeleccionada.venImpSub,
			venImpDes: this.ventaSeleccionada.venImpDes,
			venImpIva: this.ventaSeleccionada.venImpIva,
			venImpTot: this.ventaSeleccionada.venImpTot,
			venImpCob: this.ventaSeleccionada.venImpCob,
			venImpPen: this.ventaSeleccionada.venImpPen,
			
			venFecPre: this.ventaSeleccionada.venFecPre,
			venFecPed: this.ventaSeleccionada.venFecPed,
			venFecAlb: this.ventaSeleccionada.venFecAlb,
			venFecFac: this.ventaSeleccionada.venFecFac,
			venFecCob: this.ventaSeleccionada.venFecCob,

			venUsuMov: this.ventaSeleccionada.venUsuMov,
		  	venFecMov: this.ventaSeleccionada.venFecMov,
			venAct: this.ventaSeleccionada.venAct

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
	      this.ventaSeleccionada.venId!
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
	
	// Comprueba que los campos obligatorios están informados
	private validarObligatorios(): boolean {

		// Comprueba los campos obligatorios
		if (
			!this.venta.perIdCom ||
			!this.venta.perIdVen 
		) {

			// Muestra el mensaje
			alert('Debe rellenar todos los campos obligatorios.');

			// Indica que el formulario no es válido
			return false;

		}

		// Indica que el formulario es válido
		return true;

	}

	// Este método guarda el contenido del formulario en base de datos
	guardar() {
		
		//Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

	  	const venta = {
		
			cliId: this.venta.cliId,
			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.
		    venId: 0,
	
			perIdVen: this.venta.perIdVen,		
			perIdCom: this.venta.perIdCom,
			
			venImpSub: this.venta.venImpSub,
			venImpDes: this.venta.venImpDes,
			venImpIva: this.venta.venImpIva,
			venImpTot: this.venta.venImpTot,
			venImpCob: this.venta.venImpCob,
			venImpPen: this.venta.venImpPen,
			
			venFecPre: this.venta.venFecPre,
			venFecPed: this.venta.venFecPed,
			venFecAlb: this.venta.venFecAlb,
			venFecFac: this.venta.venFecFac,
			venFecCob: this.venta.venFecCob,
			
			venUsuMov: this.venta.venUsuMov,
		    venFecMov: this.venta.venFecMov,
			venAct: this.venta.venAct

	  	};

	  this.ventaService.guardar(venta).subscribe({

	    next: () => {

	      alert('Venta guardada correctamente.');

		  this.limpiarFormulario();

		  this.consultar();

	    },

	    error: (error: any) => {

	      console.error(error);
		  
		  alert('Error al guardar venta.');

	    }

	  });

	}
	
	// Este método modifica el contenido del formulario en base de datos.
	actualizar() {

	    // Interruptor activo para controlar campos obligatorios.
	    this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

	    const venta = {

	        cliId: this.venta.cliId,
	        venId: this.venta.venId,

	        perIdVen: this.venta.perIdVen,
	        perIdCom: this.venta.perIdCom,

	        venImpSub: this.venta.venImpSub,
	        venImpDes: this.venta.venImpDes,
	        venImpIva: this.venta.venImpIva,
	        venImpTot: this.venta.venImpTot,
	        venImpCob: this.venta.venImpCob,
	        venImpPen: this.venta.venImpPen,

	        venFecPre: this.venta.venFecPre,
	        venFecPed: this.venta.venFecPed,
	        venFecAlb: this.venta.venFecAlb,
	        venFecFac: this.venta.venFecFac,
	        venFecCob: this.venta.venFecCob,

	        venUsuMov: this.venta.venUsuMov,
	        venFecMov: this.venta.venFecMov,
	        venAct: this.venta.venAct

	    };

	    this.ventaService.actualizar(venta).subscribe({

	        next: () => {

	            alert('Venta actualizada correctamente.');
				
				this.limpiarFormulario();

	            this.consultar();

	        },

	        error: (error: any) => {

	            console.error(error);
				
				alert('Error al actualizar venta.');

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

		cliId: Number(localStorage.getItem('clienteId')) || 0,
	  	venId: 0,
		
	  	perIdVen: 0,
		perIdCom: 0,
		
		venImpSub: 0,
		venImpDes: 0,
		venImpIva: 0,
		venImpTot: 0,
		venImpCob: 0,
		venImpPen: 0,
		
		venFecPre: '',
		venFecPed: '',
		venFecAlb: '',
		venFecFac: '',
		venFecCob: '',
	  
		venUsuMov: localStorage.getItem('usuario') || '',
	  	venFecMov: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16),
		venAct: true
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.venta = this.crearVentaVacia();

	}
	
	// Este método crea una venta detalle vacía
	crearVentaDetalleVacia(): VentaDetalle {

	    return {

	        venDetId: 0,	
	        venId: 0,
			
	        proId: 0,
	        venDetCan: 1, 
			venDetPre: 0, 
			venDetDes: 0,
			venDetIva: 0,
			venDetImp: 0,
			
			venDetUsuMov: localStorage.getItem('usuario') || '',
			venDetFecMov: FechasUtil.formatearFechaHora(),
			venDetAct: true

	    };

	}

	// Este método inserta una nueva venta detalle en la lista
	insertarVentaDetalle() {

	    this.ventaDetalleLista.push(
	       
			 this.crearVentaDetalleVacia()
	    );

	}

	// Este método elimina una nueva venta detalle de la lista	
	eliminarVentaDetalle(indice: number) {

		// Elimina una linea detalle de venta pasando el número de línea.
	    this.ventaDetalleLista.splice(indice, 1);
		
		// Se recalculan los totales de la venta.
		this.calcularTotalesVenta();

	}
	
	// Elimina todas las líneas del detalle de la venta.
	eliminarTodasVentaDetalle(): void {

		// Elimina todas las lineas detalle de venta.
	    this.ventaDetalleLista = [];
		
		// Se recalculan los totales de la venta.
		this.calcularTotalesVenta();

	}
	
	// Asigna el producto seleccionado a la línea del detalle y carga su precio.
	seleccionarProductoDetalle(fila: VentaDetalle, proId: number): void {

	  // Guarda el identificador del producto.
	  fila.proId = proId;

	  // Busca el producto seleccionado en la lista de productos.
	  const producto = this.productosLista.find(

	    producto => producto.proId === proId

	  );

	  // Si el producto existe...
	  if (producto) {

	    // Copia el precio de venta del producto a la línea del detalle.
	    fila.venDetPre = producto.proPreVen;
		fila.venDetDes = producto.proPreDes;
		fila.venDetIva = producto.proPreIva;
		
		// Recalcula la línea completa
		 this.actualizarLinea(fila);

	  }

	}
	
	// Este método actualiza el importe total por linea y el importe total por venta 
	actualizarLinea(fila: VentaDetalle): void {

	    this.calcularImporteLinea(fila);

	    this.calcularTotalesVenta();

	}
	
	// Este método actualiza el importe total de cada línea.
	calcularImporteLinea(fila: VentaDetalle): void {

	    const base = fila.venDetCan * fila.venDetPre;

	    const descuento = base * fila.venDetDes / 100;

	    const baseDescontada = base - descuento;

	    const iva = baseDescontada * fila.venDetIva / 100;

	    fila.venDetImp = baseDescontada + iva;

	}
	
	// Este método actualiza el importe total de la venta.
	calcularTotalesVenta(): void {

	  let subtotal = 0;
	  let descuento = 0;
	  let iva = 0;
	  let total = 0;

	  this.ventaDetalleLista.forEach(fila => {

	    const base = fila.venDetCan * fila.venDetPre;

	    const impDes = base * fila.venDetDes / 100;

	    const baseDescontada = base - impDes;

	    const impIva = baseDescontada * fila.venDetIva / 100;

	    const impTotal = baseDescontada + impIva;

	    subtotal += base;
	    descuento += impDes;
	    iva += impIva;
	    total += impTotal;

	  });

	  this.venta.venImpSub = subtotal;
	  this.venta.venImpDes = descuento;
	  this.venta.venImpIva = iva;
	  this.venta.venImpTot = total;

	  // Si no hay cobros registrados, mantenemos el importe cobrado
	  // y calculamos el pendiente.
	  this.venta.venImpPen = this.venta.venImpTot - this.venta.venImpCob;

	}
	
}