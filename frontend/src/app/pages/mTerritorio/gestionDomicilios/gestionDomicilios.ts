import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';

import { SelectorMapa } from '../../../components/selectorMapa/selectorMapa';
import { MapaRegistros } from '../../../components/mapaRegistros/mapaRegistros';
import { AgendaRegistros } from '../../../components/agendaRegistros/agendaRegistros';
import { DocumentacionAdjunta } from '../../../components/documentacionAdjunta/documentacionAdjunta';

import { FechasUtil } from '../../../shared/utils/fechas.util';

import { Domicilio } from '../../../interfaces/domicilio.interface';

import { DomicilioService } from '../../../services/domicilio.service';
import { PdfService } from '../../../services/pdf.service';
import { CatalogoTerritorialService } from '../../../services/catalogo-territorial.service';
import { Via, CodigoPostal, Municipio, Provincia } from '../../../interfaces/catalogo-territorial.interface';

import { ViewChild } from '@angular/core';

// Se define la configuración del componente Angular
@Component({
  selector: 'GestionDomicilios',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, SelectorMapa, MapaRegistros, AgendaRegistros, DocumentacionAdjunta,DatosIdentificacion,DatosMovimiento,BarraAcciones],
  templateUrl: './gestionDomicilios.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class GestionDomicilios {

	readonly opcionesCinco: string[] = ['1', '2', '3', '4', '5'];
	vias: Via[] = []; codigosPostales: CodigoPostal[] = []; municipios: Municipio[] = []; provincias: Provincia[] = [];
	
	//Busca el componente tabla en el html y guarda en una variable tabla por la cual se podrá acceder a variables y métodos dentro de tabla
	// por ejemplo a 'this.tabla.datosFiltrados' que devolverá los registros que se están mostrando en pantalla después de aplicar los filtros.
	//Sin ViewChild, empresas.ts no sabe nada de lo que ocurre dentro de tabla.ts.
	// Permitirá acceder a los datos filtrados para exportarlos posteriormente a PDF.
	@ViewChild(Tabla)
	tabla!: Tabla;

	//Variables de la clase
	vistaActiva: 'mapa' | 'registro' | 'tabla' | 'agenda' | 'adjuntos' | 'historico' = 'mapa';
	modoFormulario: 'insertar' | 'modificar' | 'ver' = 'insertar';
	mostrarObligatorios = false;
	
	obtenerDomicilios = () => this.domicilioService.obtenerDomicilios();
	
	// Se crea un objeto domicilio con datos vacíos
	domicilio: Domicilio = this.crearDomicilioVacio();

	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    empId: 'Id Empresa',
	    domId: 'Id Domicilio',
		domIdHis: 'Id Histórico',
		domTipMov: 'Tipo Movimiento',
		domCauMov: 'Causa Movimiento',
		domCiv: 'C.I.V.',
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
		domCoX: 'Coordenada X',
		domCoY: 'Coordenada Y',
		domHus: 'Huso UTM',
	    domUsuMov: 'Usuario Mod.',
	    domFecMov: 'Fecha Mod.',
		domAct: 'Activo',
	};	
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'empId', 'domId', 'domIdHis', 'domTipMov', 'domCauMov',
		'domCiv',
		'domTipVia', 'domVia', 'domNum', 
		'domKm', 'domEdi', 'domBlo', 'domPor', 'domEsc', 'domPla', 'domPue', 
		'domCp', 'domMun', 'domPro', 'domObs', 
		'domDir',
		'domCoX', 'domCoY', 'domHus',
	  	'domUsuMov', 'domFecMov', 'domAct'

	];

	// Datos de la tabla
	datos: any[] = [];
	datosHistorico: Domicilio[] = [];
	
	// Guarda el registro seleccionado de la tabla
	domicilioSeleccionado: Domicilio | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private domicilioService: DomicilioService,
		private pdfService: PdfService,
		private catalogoTerritorial: CatalogoTerritorialService
	
	) { this.cargarCatalogosTerritoriales(); }

	private cargarCatalogosTerritoriales() {
		this.catalogoTerritorial.vias().subscribe(v => this.vias = v.filter(x => x.viaAct)); this.catalogoTerritorial.codigos().subscribe(v => this.codigosPostales = v.filter(x => x.copAct)); this.catalogoTerritorial.municipios().subscribe(v => this.municipios = v.filter(x => x.munAct)); this.catalogoTerritorial.provincias().subscribe(v => this.provincias = v.filter(x => x.prvAct));
	}
	seleccionarVia() { const via=this.vias.find(v=>v.viaId===Number(this.domicilio.domViaId));if(!via)return;const cp=this.codigosPostales.find(v=>v.copId===via.copId);const mun=this.municipios.find(v=>v.munId===cp?.munId);const pro=this.provincias.find(v=>v.prvId===mun?.prvId);this.domicilio.domTipVia=via.viaTip;this.domicilio.domVia=via.viaNom;this.domicilio.domCp=cp?.copCod||'';this.domicilio.domMun=mun?.munNom||'';this.domicilio.domPro=pro?.prvNom||'';this.actualizarDireccion();}
	
	// Este método muestra el mapa de datos
	mapa() {

		this.domicilioSeleccionado = null;
		this.vistaActiva = 'mapa';
		
	}

	abrirDomicilioDesdeMapa(domicilio: Domicilio): void {

		this.domicilioSeleccionado = domicilio;

	}

	ver(): void {
		if (!this.domicilioSeleccionado) return;
		this.vistaActiva = 'tabla';
		this.modificar();
		this.modoFormulario = 'ver';
	}
	agenda(): void { if(this.domicilioSeleccionado)this.vistaActiva='agenda'; }
	adjuntos(): void { if (this.domicilioSeleccionado?.domId) this.vistaActiva = 'adjuntos'; }
	volverAConsulta(): void { this.vistaActiva = 'tabla'; }
	historico(): void {
		if (!this.domicilioSeleccionado?.domId) return;
		this.domicilioService.obtenerHistorico(this.domicilioSeleccionado.domId).subscribe({
			next: datos => { this.datosHistorico = datos; this.domicilioSeleccionado = datos.find(d => d.domAct) || this.domicilioSeleccionado; this.vistaActiva = 'historico'; },
			error: error => { console.error(error); avisarAplicacion('Error al obtener el histórico del domicilio.'); }
		});
	}
	async deshacer(): Promise<void> {
		if (!this.domicilioSeleccionado || (this.domicilioSeleccionado.domIdHis || 1) <= 1) return;
		if (!await confirmarAplicacion('¿Desea deshacer el último movimiento del domicilio?')) return;
		this.domicilioService.deshacer(this.domicilioSeleccionado.domId).subscribe({
			next: domicilio => { this.domicilioSeleccionado = domicilio; avisarAplicacion('Movimiento deshecho correctamente.'); this.historico(); },
			error: error => { console.error(error); avisarAplicacion('Error al deshacer el movimiento.'); }
		});
	}

	seleccionarDomicilio(domicilio: Domicilio): void {

		this.domicilioSeleccionado = domicilio;

	}

	 // Este método muestra la tabla de datos
	 consultar() {
		
	 	this.vistaActiva = 'tabla';
		this.domicilioSeleccionado = null;

	 	this.domicilioService.obtenerDomicilios().subscribe({

	 		next: (respuesta) => {
				
	 			this.datos = respuesta;

	 		},

	 		error: (error) => {

	 			console.error(error);

	 			avisarAplicacion('Error al obtener domicilios');

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
	  this.modoFormulario = 'modificar';
	  if (this.vistaActiva === 'mapa') this.vistaActiva = 'tabla';

	  // El formulario ya muestra el domicilio seleccionado.
	  if (this.vistaActiva === 'registro') return;

	  // Si estamos en la pestaña tabla
	  if (this.vistaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.domicilioSeleccionado) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.domicilio = {

			empId: this.domicilioSeleccionado.empId,	
		 	domId: this.domicilioSeleccionado.domId,
			domIdHis: this.domicilioSeleccionado.domIdHis,
			domTipMov: this.domicilioSeleccionado.domTipMov,
			
			domCiv: this.domicilioSeleccionado.domCiv,
		  	domTipVia: this.domicilioSeleccionado.domTipVia,
		  	domVia: this.domicilioSeleccionado.domVia,
			domViaId: this.domicilioSeleccionado.domViaId,
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
			
			domCoX: this.domicilioSeleccionado.domCoX,
			domCoY: this.domicilioSeleccionado.domCoY,
			domHus: this.domicilioSeleccionado.domHus,

			domUsuMov: this.domicilioSeleccionado.domUsuMov,
			domFecMov: this.domicilioSeleccionado.domFecMov,
			domAct: this.domicilioSeleccionado.domAct

		};
		
	  }

	}

	async baja() {
		if (!this.domicilioSeleccionado || this.domicilioSeleccionado.domTipMov === 'B') return;
		if (!await confirmarAplicacion('¿Desea dar de baja el domicilio seleccionado? El movimiento quedará en el histórico.',true)) return;
		this.domicilioService.baja(this.domicilioSeleccionado.domId).subscribe({next:()=>this.consultar(),error:e=>{console.error(e);avisarAplicacion(e?.error?.mensaje||'No se pudo dar de baja el domicilio.');}});
	}

	// Elimina el registro completo y todas sus versiones.
	async eliminar() {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.domicilioSeleccionado || this.domicilioSeleccionado.domTipMov === 'B') {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = await confirmarAplicacion(
	      '¿Desea eliminar definitivamente el domicilio y todos sus movimientos históricos?'
	    ,true);

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el domicilio
	    this.domicilioService.eliminar(
	      this.domicilioSeleccionado.domId!
	    ).subscribe({

	      next: () => {

	        avisarAplicacion('Domicilio e histórico eliminados correctamente.');

	        // Limpia selección
	        this.domicilioSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        avisarAplicacion('Error al eliminar domicilio.');

	      }

	    });

	}

	async reactivar(): Promise<void> {

		if (!this.domicilioSeleccionado || this.domicilioSeleccionado.domTipMov !== 'B') return;
		if (!await confirmarAplicacion('¿Desea reactivar el domicilio seleccionado?')) return;

		this.domicilioService.deshacer(this.domicilioSeleccionado.domId).subscribe({
			next: () => {
				this.domicilioSeleccionado = null;
				this.consultar();
			},
			error: error => {
				console.error(error);
				avisarAplicacion(error?.error?.message || error?.error?.detail || 'No se pudo reactivar el domicilio.');
			}
		});

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
	
	// Comprueba que los campos obligatorios están informados
	private validarObligatorios(): boolean {
/*
		// Comprueba los campos obligatorios
		if (
			!this.domicilio.domCiv ||
			!this.domicilio.domTipVia ||
			!this.domicilio.domVia ||
			!this.domicilio.domNum ||
			!this.domicilio.domCp ||
			!this.domicilio.domMun ||
			!this.domicilio.domPro
		) {

			// Muestra el mensaje
			avisarAplicacion('Debe rellenar todos los campos obligatorios.');

			// Indica que el formulario no es válido
			return false;

		}
*/
		// Indica que el formulario es válido
		return true;

	}
	
	// Este método guarda el contenido del formulario en base de datos
	guardar() {
		
		//Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

	  	const domicilio = {

			empId: this.domicilio.empId,
			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.
		    domId: 0,
			
			domCiv: this.domicilio.domCiv,
		    domTipVia: this.domicilio.domTipVia,
		    domVia: this.domicilio.domVia,
			domViaId: this.domicilio.domViaId,
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
			
			domCoX: this.domicilio.domCoX,			
			domCoY: this.domicilio.domCoY,
			domHus: this.domicilio.domHus,
	
		    domUsuMov: this.domicilio.domUsuMov,
		    domFecMov: this.domicilio.domFecMov,			
			domAct: this.domicilio.domAct

	  	};
	  
	  console.log('DOMICILIO A ENVIAR:', domicilio);

	  this.domicilioService.guardar(domicilio).subscribe({

		next: () => {
			
			avisarAplicacion('Domicilio guardado correctamente.');
		
		  	this.limpiarFormulario();

		 	this.consultar();

	    	},

	    error: (error: any) => {

	      console.error(error);

	      avisarAplicacion('Error al guardar domicilio.');

	    }

	  });

	}
	
	// Este método actualiza el contenido del formulario en base de datos
	actualizar() {
		
		// Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

		const domicilio = {

			empId: this.domicilio.empId,

		    // Mantiene el identificador del domicilio que se va a modificar
		    domId: this.domicilio.domId,
			
			domCiv: this.domicilio.domCiv,

		    domTipVia: this.domicilio.domTipVia,

		    domVia: this.domicilio.domVia,
			domViaId: this.domicilio.domViaId,

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
			
			domCoX: this.domicilio.domCoX,

			domCoY: this.domicilio.domCoY,

			domHus: this.domicilio.domHus,

		    domUsuMov: this.domicilio.domUsuMov,

		    domFecMov: this.domicilio.domFecMov,

			domAct: this.domicilio.domAct

		};

		console.log('DOMICILIO A ACTUALIZAR:', domicilio);

		this.domicilioService.actualizar(domicilio).subscribe({

			next: () => {

				avisarAplicacion('Domicilio actualizado correctamente.');

				this.limpiarFormulario();

				this.consultar();
				
			},

			error: (error: any) => {

				console.error(error);

				avisarAplicacion('Error al actualizar domicilio.');

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

		empId: Number(localStorage.getItem('empresaId')) || 0,
		domId: 0,
		domIdHis: 1,
		domTipMov: 'A',
		
		domCiv: '',
	    domTipVia: '', domVia: '', domViaId: null, domNum: '',
	    domKm: '', domEdi: '', domBlo: '', domPor: '', domEsc: '', domPla: '', domPue: '',
		domCp: '', domMun: '', domPro: '', domObs: '', domDir: '',
		
		domCoX: 0, domCoY: 0, domHus: 25830,
		domUsuMov: localStorage.getItem('usuario') || '',
	    domFecMov: FechasUtil.formatearFechaHora(),
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
	   if (this.domicilio.domPla) { partes.push('Planta ' + this.domicilio.domPla); }

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






