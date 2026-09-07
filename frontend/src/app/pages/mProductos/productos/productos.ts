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
import { DocumentacionAdjunta } from '../../../components/documentacionAdjunta/documentacionAdjunta';

import { SelectorMalla } from '../../../components/selectorMalla/selectorMalla';

import { FechasUtil } from '../../../shared/utils/fechas.util';

import { Producto } from '../../../interfaces/producto.interface';

import { FormsModule } from '@angular/forms';

import { ProductoService } from '../../../services/producto.service';
import { TipoArticulo, TipoArticuloService } from '../../../services/tipo-articulo.service';
import { PdfService } from '../../../services/pdf.service';

import { ViewChild } from '@angular/core';

// Se define la configuración del componente Angular
@Component({
  selector: 'Productos',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, SelectorMalla, DocumentacionAdjunta,DatosIdentificacion,DatosMovimiento,BarraAcciones],
  templateUrl: './productos.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Productos {
	
	//Busca el componente tabla en el html y guarda en una variable tabla por la cual se podrá acceder a variables y métodos dentro de tabla
	// por ejemplo a 'this.tabla.datosFiltrados' que devolverá los registros que se están mostrando en pantalla después de aplicar los filtros.
	//Sin ViewChild, empresas.ts no sabe nada de lo que ocurre dentro de tabla.ts.
	// Permitirá acceder a los datos filtrados para exportarlos posteriormente a PDF.
	@ViewChild(Tabla)
	tabla!: Tabla;

	//Variables de la clase
	vistaActiva: 'malla' | 'registro' | 'tabla' | 'adjuntos' | 'historico' = 'tabla';
	modoFormulario: 'insertar' | 'modificar' = 'insertar';
	mostrarObligatorios = false;
	
	// Se crea un objeto producto con datos vacíos
	producto: Producto = this.crearProductoVacio();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    empId: 'Id Empresa',
	    proId: 'Id Producto',
		proIdHis: 'Id Histórico',
		proTipMov: 'Tipo Movimiento',
		proCauMov: 'Causa Movimiento',
	    proTipPro: 'Tipo Producto',
	    proNom: 'Nombre',
		proDes: 'Descripción',
		proCat: 'Categoría',
		proSubCat: 'Subcategoría',
		proMar: 'Marca',
		proMod: 'Modelo',
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
		proDurMin: 'Duración (min)',
		proUbi: 'Ubicación',
		proFilMal: 'Fila Malla',
		proColMal: 'Columna Malla',
	    proUsuMov: 'Usuario Mod.',
	    proFecMov: 'Fecha Mod.',
		proAct: 'Activo',
	};	

	// Campos mostrados en la tabla
	columnas: string[] = [ 'empId', 'proId', 'proIdHis', 'proTipMov', 'proCauMov',
		'proTipPro', 'proNom', 'proDes',
		'proCat', 'proSubCat', 'proMar', 'proMod', 'proPro',
		'proPreCom', 'proPreVen', 'proPreDes', 'proPreIva', 'proPreFin', 
		'proStoAct', 'proStoMin', 'proUniMed', 'proConSto',
		'proObs', 'proDurMin',
		'proUbi', 'proFilMal', 'proColMal',
		'proUsuMov', 'proFecMov','proAct'

	];

	// Datos de la tabla
	datos: any[] = [];
	datosHistorico: Producto[] = [];
	tiposProducto: TipoArticulo[] = [];
	
	// Guarda el registro seleccionado de la tabla
	productoSeleccionado: Producto | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
	  private readonly router: Router,
	  private productoService: ProductoService,
	  private pdfService: PdfService,
	  private tipoArticuloService: TipoArticuloService
	  
	) { this.tipoArticuloService.listar('PRODUCTO').subscribe({next: tipos => this.tiposProducto = tipos, error: () => avisarAplicacion('No se pudieron cargar los tipos de producto.')}); }
	
	// Este método muestra el mapa de datos
	malla() {

		this.vistaActiva = 'malla';
		
	}

	// Este método muestra la tabla de datos
	consultar() {

		this.vistaActiva = 'tabla';
		this.productoSeleccionado = null;

		this.productoService.obtenerProductos().subscribe({

			next: (respuesta) => {

				this.datos = respuesta;

			},

			error: (error) => {

				console.error(error);

				avisarAplicacion('Error al obtener productos.');

			}

		});

	}

	adjuntos() { if (this.productoSeleccionado?.proId) this.vistaActiva = 'adjuntos'; }
	volverAConsulta() { this.vistaActiva = 'tabla'; }
	historico() {
		if (!this.productoSeleccionado?.proId) return;
		this.productoService.obtenerHistorico(this.productoSeleccionado.proId).subscribe({
			next: datos => { this.datosHistorico = datos; this.productoSeleccionado = datos.find(p => p.proAct) || this.productoSeleccionado; this.vistaActiva = 'historico'; },
			error: error => { console.error(error); avisarAplicacion('Error al obtener el histórico del producto.'); }
		});
	}
	async deshacer() {
		if (!this.productoSeleccionado || (this.productoSeleccionado.proIdHis || 1) <= 1) return;
		if (!await confirmarAplicacion('¿Desea deshacer el último movimiento del producto?')) return;
		this.productoService.deshacer(this.productoSeleccionado.proId).subscribe({
			next: producto => { this.productoSeleccionado = producto; avisarAplicacion('Movimiento deshecho correctamente.'); this.historico(); },
			error: error => { console.error(error); avisarAplicacion('Error al deshacer el movimiento.'); }
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
	  this.modoFormulario = 'modificar';

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

	      avisarAplicacion('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend pro_id a formato frontend idProducto
		this.producto = {

			empId: this.productoSeleccionado.empId,			
			proId: this.productoSeleccionado.proId,
			proIdHis: this.productoSeleccionado.proIdHis,
			proTipMov: 'M',
			proCauMov: 'Modificación del registro',

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
			proDurMin: this.productoSeleccionado.proDurMin || 0,
			proVisCat: this.productoSeleccionado.proVisCat || false,
			proIma: this.productoSeleccionado.proIma || '',
			
			proFilMal: this.productoSeleccionado.proFilMal,
			proColMal: this.productoSeleccionado.proColMal,
			proUbi: this.productoSeleccionado.proUbi,

			proUsuMov: this.productoSeleccionado.proUsuMov,
		  	proFecMov: this.productoSeleccionado.proFecMov,
			proAct: this.productoSeleccionado.proAct

		};

	  }

	}
	
	async baja() {
	  if (!this.productoSeleccionado || this.productoSeleccionado.proTipMov === 'B') return;
	  if (!await confirmarAplicacion('¿Desea dar de baja el producto seleccionado? El movimiento quedará en el histórico.',true)) return;
	  this.productoService.baja(this.productoSeleccionado.proId).subscribe({next:()=>this.consultar(),error:e=>{console.error(e);avisarAplicacion(e?.error?.mensaje||'No se pudo dar de baja el producto.');}});
	}

	async reactivar() {
	  if (!this.productoSeleccionado || this.productoSeleccionado.proTipMov !== 'B') return;
	  if (!await confirmarAplicacion('¿Desea reactivar el producto seleccionado?')) return;
	  this.productoService.deshacer(this.productoSeleccionado.proId).subscribe({next:()=>this.consultar(),error:e=>{console.error(e);avisarAplicacion(e?.error?.mensaje||'No se pudo reactivar el producto.');}});
	}

	// Elimina el registro completo y todas sus versiones.
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
	    if (!this.productoSeleccionado) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = await confirmarAplicacion(
	      '¿Desea eliminar definitivamente el producto y todos sus movimientos históricos?'
	    ,true);

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.productoService.eliminar(
	      this.productoSeleccionado.proId!
	    ).subscribe({

	      next: () => {

	        avisarAplicacion('Producto e histórico eliminados correctamente');

	        // Limpia selección
	        this.productoSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        avisarAplicacion('Error al eliminar producto');

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
	
	// Comprueba que los campos obligatorios están informados
	private validarObligatorios(): boolean {

		// Comprueba los campos obligatorios
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

	  	const producto = {

			empId: this.producto.empId,
			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.		
		    proId: 0,
			proIdHis: 1,
			proTipMov: 'A',
			proCauMov: this.producto.proCauMov || 'Alta del registro',
	
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
			proDurMin: this.producto.proDurMin || 0,
			proVisCat: this.producto.proVisCat || false,
			proIma: this.producto.proIma || '',
			
			proFilMal: this.producto.proFilMal,
			proColMal: this.producto.proColMal,
			proUbi: this.producto.proUbi,
	
		    proUsuMov: this.producto.proUsuMov,
		    proFecMov: this.producto.proFecMov,
			proAct: this.producto.proAct

	  	};

	  this.productoService.guardar(producto).subscribe({

	    next: (guardado) => {


	      avisarAplicacion('Producto guardado correctamente.');
		  
		  this.limpiarFormulario();

		  this.consultar();

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

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

		const producto = {

			empId: this.producto.empId,

			// Mantiene el identificador del producto que se va a modificar
			proId: this.producto.proId,
			proIdHis: this.producto.proIdHis,
			proTipMov: 'M',
			proCauMov: this.producto.proCauMov || 'Modificación del registro',

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
			proDurMin: this.producto.proDurMin || 0,
			proVisCat: this.producto.proVisCat || false,
			proIma: this.producto.proIma || '',
			
			proFilMal: this.producto.proFilMal,
			proColMal: this.producto.proColMal,
			proUbi: this.producto.proUbi,

			proUsuMov: this.producto.proUsuMov,
			proFecMov: this.producto.proFecMov,
			proAct: this.producto.proAct

		};

		console.log('PRODUCTO A ACTUALIZAR:', producto);

		this.productoService.actualizar(producto).subscribe({

			next: (actualizado) => {


				avisarAplicacion('Producto actualizado correctamente.');

				this.limpiarFormulario();

				this.consultar();

			},

			error: (error: any) => {

				console.error(error);

				avisarAplicacion('Error al actualizar producto.');

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

		empId: Number(localStorage.getItem('empresaId')) || 0,
	  	proId: 0,
		proIdHis: 1,
		proTipMov: 'A',
		proCauMov: 'Alta del registro',
		
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
		proDurMin: 0,
		proVisCat: true,
		proIma: 'producto-predeterminado.png',
		
		proFilMal: 0,
		proColMal: 0,
		proUbi: '',
	  
		proUsuMov: localStorage.getItem('usuario') || '',
		proFecMov: FechasUtil.formatearFechaHora(),
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
