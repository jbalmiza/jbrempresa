import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { SelectorBusqueda } from '../../../components/selectorBusqueda/selectorBusqueda';
import { DatosPersonaRelacion } from '../../../components/datosPersonaRelacion/datosPersonaRelacion';
import { TablaEdicion } from '../../../components/tablaEdicion/tablaEdicion';

import { FechasUtil } from '../../../shared/utils/fechas.util';

import { FormsModule } from '@angular/forms';

import { CompraService } from '../../../services/compra.service';
import { PersonaService } from '../../../services/persona.service';
import { ProductoService } from '../../../services/producto.service';
import { PdfService } from '../../../services/pdf.service';

// Las interfaces definen el modelo de datos que utiliza cada clase
import { Compra } from '../../../interfaces/compra.interface';
import { Persona } from '../../../interfaces/persona.interface';
import { Producto } from '../../../interfaces/producto.interface';
import { CompraDetalle } from '../../../interfaces/compraDetalle.interface';

// Permite acceder a un componente hijo para utilizar sus variables y métodos.
// Ejemplo acceder desde ventas a : this.tabla.datosFiltrados
import { ViewChild } from '@angular/core';

import { TablaColumna } from '../../../directives/tablaColumna/tablaColumna';

// Se define la configuración del componente Angular
@Component({
  selector: 'Compras',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, SelectorBusqueda, DatosPersonaRelacion, TablaEdicion, TablaColumna,DatosIdentificacion,DatosMovimiento,BarraAcciones],
  templateUrl: './compras.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Compras {
	
	//Esto se ejecuta al iniciar la clase y está iniciado para cualquier acción: insertar, modificar, etc.
	ngOnInit() {

		//Se obtiene la lista de personas / productos disponible en insertar y modificar
		this.personaService.obtenerPersonas().subscribe({

		  next: (respuesta) => {

		    this.personasLista = respuesta;
		    this.enriquecerCompras();

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
	//Sin ViewChild, empresas.ts no sabe nada de lo que ocurre dentro de tabla.ts.
	// Permitirá acceder a los datos filtrados para exportarlos posteriormente a PDF.
	@ViewChild(Tabla)
	tabla!: Tabla;

	//Variables de la clase
	vistaActiva: 'registro' | 'tabla' = 'tabla';
	modoFormulario: 'insertar' | 'modificar' = 'insertar';
	mostrarObligatorios = false;
	
	// Se crea un objeto producto con datos vacíos
	compra: Compra = this.crearCompraVacia();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    empId: 'Id Empresa',
	    comId: 'Id Compra',
		
	    compradorNomCom: 'Comprador',
		vendedorNomCom: 'Vendedor',		

		comImpSub: 'Importe Subtotal',
		comImpDes: 'Importe Descuento',
		comImpIva: 'Importe IVA',
		comImpTot: 'Importe Total',
		comImpCob: 'Importe Cobrado',
		comImpPen: 'Importe Pendiente',
		
		comFecPre: 'Fecha Presupuesto',
		comFecPed: 'Fecha Pedido',
		comFecAlb: 'Fecha Albarán',
		comFecFac: 'Fecha Factura',
		comFecCob: 'Fecha Cobro',
		
	    comUsuMov: 'Usuario Mod.',
	    comFecMov: 'Fecha Mod.',
		comAct: 'Activo'
	};	
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'empId', 'comId', 
		'compradorNomCom', 'vendedorNomCom', 
		'proId', 
		'comImpSub', 'comImpDes', 'comImpIva', 'comImpTot', 'comImpCob', 'comImpPen', 
		'comFecPre', 'comFecPed', 'comFecAlb', 'comFecFac', 'comFecCob', 
		'comUsuMov', 'comFecMov', 'comAct'

	];
	
	// Títulos de las columnas del detalle de la venta.
	titulosColumnasDetalle = {

	    proId: 'Producto',
		
	    comDetCan: 'Cantidad',
	    comDetPre: 'Precio',
		comDetDes: '% Descuento',
		comDetIva: '% I.V.A.',
	    comDetImp: 'Importe',
		
	    accion: 'Acción'

	};

	// Campos mostrados en la tabla de detalle.
	columnasDetalle: string[] = [

	    'proId',
		
	    'comDetCan',
	    'comDetPre',
		'comDetDes',
		'comDetIva',
	    'comDetImp',

	    'accion'

	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de personas
	personasLista: Persona[] = [];
	productosLista: Producto[] = [];

	// Lista para el selector de productos	
	compraDetalleLista: CompraDetalle[] = [];
	
	// Guarda el registro seleccionado de la tabla
	compraSeleccionada: Compra | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private compraService: CompraService,
	  private personaService: PersonaService,
	  private productoService: ProductoService,
	  private pdfService: PdfService
	  
	) {}

	// Este método muestra la tabla de datos
	private enriquecerCompras() {
		this.datos = this.datos.map(compra => ({
			...compra,
			compradorNomCom: this.personasLista.find(persona => Number(persona.perId) === Number(compra.perIdCom))?.perNomCom || '',
			vendedorNomCom: this.personasLista.find(persona => Number(persona.perId) === Number(compra.perIdVen))?.perNomCom || ''
		}));
	}

	consultar() {

		this.vistaActiva = 'tabla';

		this.compraService.obtenerCompras().subscribe({

			next: (respuesta) => {

				this.datos = respuesta;
				this.enriquecerCompras();

			},

			error: (error) => {

				console.error(error);

				avisarAplicacion('Error al obtener compras');

			}

		});

	}

	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {
		
		this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';

		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Venta.
		this.compraService.obtenerSiguienteId().subscribe({

		  next: (id) => {


			console.log('ID recibido:', id);
		    this.compra.comId = id;

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
		
		//Al pulsar insertar compra se inserta también una línea detalle por defecto.
		this.eliminarTodasCompraDetalle()
		this.insertarCompraDetalle();
		
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
	    if (!this.compraSeleccionada) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend ven_id a formato frontend idVenta
		this.compra = {

			empId: this.compraSeleccionada.empId,
		  	comId: this.compraSeleccionada.comId,

			perIdCom: this.compraSeleccionada.perIdCom,
			perIdVen: this.compraSeleccionada.perIdVen,

			
			comImpSub: this.compraSeleccionada.comImpSub,
			comImpDes: this.compraSeleccionada.comImpDes,
			comImpIva: this.compraSeleccionada.comImpIva,
			comImpTot: this.compraSeleccionada.comImpTot,
			comImpCob: this.compraSeleccionada.comImpCob,
			comImpPen: this.compraSeleccionada.comImpPen,
			
			comFecPre: this.compraSeleccionada.comFecPre,
			comFecPed: this.compraSeleccionada.comFecPed,
			comFecAlb: this.compraSeleccionada.comFecAlb,
			comFecFac: this.compraSeleccionada.comFecFac,
			comFecCob: this.compraSeleccionada.comFecCob,

			comUsuMov: this.compraSeleccionada.comUsuMov,
		  	comFecMov: this.compraSeleccionada.comFecMov,
			comAct: this.compraSeleccionada.comAct

		};

	  }

	}
	
	// Este método elimina
	async eliminar() {

	  // Si estamos en la pestaña registro
	  if (this.vistaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.vistaActiva = 'tabla';

	    return;

	  }

	  // Si estamos en la pestaña tabla
	  if (this.vistaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.compraSeleccionada) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = await confirmarAplicacion(
	      '¿Desea eliminar la compra seleccionada?'
	    ,true);

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.compraService.eliminar(
	      this.compraSeleccionada.comId!
	    ).subscribe({

	      next: () => {

	        avisarAplicacion('Compra eliminada correctamente');

	        // Limpia selección
	        this.compraSeleccionada = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        avisarAplicacion('Error al eliminar compra');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'compras.pdf',
			
			// Título del documento.
			'Listado de compras',

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
			!this.compra.perIdCom ||
			!this.compra.perIdVen 
		) {

			// Muestra el mensaje
			avisarAplicacion('Debe rellenar todos los campos obligatorios.');

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

	  	const compra = {
		
			empId: this.compra.empId,
			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.
		    comId: 0,
	
			perIdCom: this.compra.perIdCom,		
			perIdVen: this.compra.perIdVen,
			
			comImpSub: this.compra.comImpSub,
			comImpDes: this.compra.comImpDes,
			comImpIva: this.compra.comImpIva,
			comImpTot: this.compra.comImpTot,
			comImpCob: this.compra.comImpCob,
			comImpPen: this.compra.comImpPen,
			
			comFecPre: this.compra.comFecPre,
			comFecPed: this.compra.comFecPed,
			comFecAlb: this.compra.comFecAlb,
			comFecFac: this.compra.comFecFac,
			comFecCob: this.compra.comFecCob,
			
			comUsuMov: this.compra.comUsuMov,
		    comFecMov: this.compra.comFecMov,
			comAct: this.compra.comAct

	  	};

	  this.compraService.guardar(compra).subscribe({

	    next: () => {

	      avisarAplicacion('Compra guardada correctamente.');
		  
		  this.limpiarFormulario();

	    },

	    error: (error: any) => {

	      console.error(error);
		  
		  avisarAplicacion('Error al guardar compra.');

	    }

	  });

	}
	
	// Este método modifica el contenido del formulario en base de datos.
	actualizar() {

	    // Interruptor activo para controlar campos obligatorios.
	    this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

	    const compra = {

	        empId: this.compra.empId,
	        comId: this.compra.comId,

	        perIdCom: this.compra.perIdCom,
	        perIdVen: this.compra.perIdVen,

	        comImpSub: this.compra.comImpSub,
	        comImpDes: this.compra.comImpDes,
	        comImpIva: this.compra.comImpIva,
	        comImpTot: this.compra.comImpTot,
	        comImpCob: this.compra.comImpCob,
	        comImpPen: this.compra.comImpPen,

	        comFecPre: this.compra.comFecPre,
	        comFecPed: this.compra.comFecPed,
	        comFecAlb: this.compra.comFecAlb,
	        comFecFac: this.compra.comFecFac,
	        comFecCob: this.compra.comFecCob,

	        comUsuMov: this.compra.comUsuMov,
	        comFecMov: this.compra.comFecMov,
	        comAct: this.compra.comAct

	    };

	    this.compraService.actualizar(compra).subscribe({

	        next: () => {

	            avisarAplicacion('Compra actualizada correctamente.');

	            this.consultar();

	        },

	        error: (error: any) => {

	            console.error(error);
				
				avisarAplicacion('Error al actualizar compra.');

	        }

	    });

	}
	
	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/compras']);
	}	

	// Este método crea una compra vacía
 	private crearCompraVacia(): Compra {

		return {

		empId: Number(localStorage.getItem('empresaId')) || 0,
	  	comId: 0,
		
	  	perIdCom: 0,
		perIdVen: 0,
		
		comImpSub: 0,
		comImpDes: 0,
		comImpIva: 0,
		comImpTot: 0,
		comImpCob: 0,
		comImpPen: 0,
		
		comFecPre: '',
		comFecPed: '',
		comFecAlb: '',
		comFecFac: '',
		comFecCob: '',
	  
		comUsuMov: localStorage.getItem('usuario') || '',
	  	comFecMov: '',
		comAct: true
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.compra = this.crearCompraVacia();

	}
	
	// Este método crea una venta detalle vacía
	crearCompraDetalleVacia(): CompraDetalle {

	    return {

	        comDetId: 0,	
	        comId: 0,
			
	        proId: 0,
	        comDetCan: 1, 
			comDetPre: 0, 
			comDetDes: 0,
			comDetIva: 0,
			comDetImp: 0,
			
			comDetUsuMov: localStorage.getItem('usuario') || '',
			comDetFecMov: FechasUtil.formatearFechaHora(),
			comDetAct: true

	    };

	}

	// Este método inserta una nueva venta detalle en la lista
	insertarCompraDetalle() {

	    this.compraDetalleLista.push(
	       
			 this.crearCompraDetalleVacia()
	    );

	}

	// Este método elimina una nueva venta detalle de la lista	
	eliminarCompraDetalle(indice: number) {

		// Elimina una linea detalle de venta pasando el número de línea.
	    this.compraDetalleLista.splice(indice, 1);
		
		// Se recalculan los totales de la venta.
		this.calcularTotalesCompra();

	}
	
	// Elimina todas las líneas del detalle de la venta.
	eliminarTodasCompraDetalle(): void {

		// Elimina todas las lineas detalle de venta.
	    this.compraDetalleLista = [];
		
		// Se recalculan los totales de la venta.
		this.calcularTotalesCompra();

	}
	
	// Asigna el producto seleccionado a la línea del detalle y carga su precio.
	seleccionarProductoDetalle(fila: CompraDetalle, proId: number): void {

	  // Guarda el identificador del producto.
	  fila.proId = proId;

	  // Busca el producto seleccionado en la lista de productos.
	  const producto = this.productosLista.find(

	    producto => producto.proId === proId

	  );

	  // Si el producto existe...
	  if (producto) {

	    // Copia el precio de venta del producto a la línea del detalle.
	    fila.comDetPre = producto.proPreVen;
		fila.comDetDes = producto.proPreDes;
		fila.comDetIva = producto.proPreIva;
		
		// Recalcula la línea completa
		 this.actualizarLinea(fila);

	  }

	}
	
	// Este método actualiza el importe total por linea y el importe total por venta 
	actualizarLinea(fila: CompraDetalle): void {

	    this.calcularImporteLinea(fila);

	    this.calcularTotalesCompra();

	}
	
	// Este método actualiza el importe total de cada línea.
	calcularImporteLinea(fila: CompraDetalle): void {

	    const base = fila.comDetCan * fila.comDetPre;

	    const descuento = base * fila.comDetDes / 100;

	    const baseDescontada = base - descuento;

	    const iva = baseDescontada * fila.comDetIva / 100;

	    fila.comDetImp = baseDescontada + iva;

	}
	
	// Este método actualiza el importe total de la compra.
	calcularTotalesCompra(): void {

	  let subtotal = 0;
	  let descuento = 0;
	  let iva = 0;
	  let total = 0;

	  this.compraDetalleLista.forEach(fila => {

	    const base = fila.comDetCan * fila.comDetPre;

	    const impDes = base * fila.comDetDes / 100;

	    const baseDescontada = base - impDes;

	    const impIva = baseDescontada * fila.comDetIva / 100;

	    const impTotal = baseDescontada + impIva;

	    subtotal += base;
	    descuento += impDes;
	    iva += impIva;
	    total += impTotal;

	  });

	  this.compra.comImpSub = subtotal;
	  this.compra.comImpDes = descuento;
	  this.compra.comImpIva = iva;
	  this.compra.comImpTot = total;

	  // Si no hay cobros registrados, mantenemos el importe cobrado
	  // y calculamos el pendiente.
	  this.compra.comImpPen = this.compra.comImpTot - this.compra.comImpCob;

	}
	
}
