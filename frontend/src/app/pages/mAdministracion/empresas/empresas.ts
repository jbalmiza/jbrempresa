import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { ActivatedRoute, Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { DocumentacionAdjunta } from '../../../components/documentacionAdjunta/documentacionAdjunta';
import { SelectorBusqueda } from '../../../components/selectorBusqueda/selectorBusqueda';

import { FechasUtil } from '../../../shared/utils/fechas.util';

import { Empresa } from '../../../interfaces/empresa.interface';

import { FormsModule } from '@angular/forms';

import { EmpresaService, MovimientoEmpresa } from '../../../services/empresa.service';
import { DomicilioService } from '../../../services/domicilio.service';
import { PdfService } from '../../../services/pdf.service';

import { ViewChild } from '@angular/core';

// Se define la configuración del componente Angular
@Component({
  selector: 'Empresas',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, DocumentacionAdjunta, SelectorBusqueda,DatosIdentificacion,DatosMovimiento,BarraAcciones],
  templateUrl: './empresas.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Empresas {
	
	//Busca el componente tabla en el html y guarda en una variable tabla por la cual se podrá acceder a variables y métodos dentro de tabla
	// por ejemplo a 'this.tabla.datosFiltrados' que devolverá los registros que se están mostrando en pantalla después de aplicar los filtros.
	//Sin ViewChild, empresas.ts no sabe nada de lo que ocurre dentro de tabla.ts.
	// Permitirá acceder a los datos filtrados para exportarlos posteriormente a PDF.
	@ViewChild(Tabla)
	tabla!: Tabla;

	//Variables de la clase
	gestion = false;
	vistaActiva: 'registro' | 'tabla' | 'adjuntos' | 'movimiento' | 'historico' = 'tabla';
	modoFormulario: 'insertar' | 'modificar' | 'ver' = 'insertar';
	tipoMovimiento: 'B' | 'R' = 'B'; causaMovimiento = '';
	historicoDatos: MovimientoEmpresa[] = [];
	historicoColumnas = ['id','empresaId','tipo','causa','usuario','fecha','activo'];
	historicoTitulos = {id:'Id Histórico',empresaId:'Empresa',tipo:'Tipo Movimiento',causa:'Causa Movimiento',usuario:'Usuario Mod.',fecha:'Fecha Mod.',activo:'Activo'};
	mostrarObligatorios = false;
	domicilios: any[] = [];
	get domiciliosEmpresa(): any[] { return this.domicilios.filter(d => Number(d.empId) === Number(this.empresa.empId)); }
	
	// Se crea un objeto usuario con datos vacíos
	empresa: Empresa = this.crearEmpresaVacio();
	

	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    empId: 'Id Empresa',
	    empNom: 'Nombre comercial',
		empRazSoc: 'Razón social',
		empNif: 'NIF/CIF',
		empActEco: 'Actividad',
		empTel: 'Teléfono',
		empEma: 'Correo electrónico',
		empWeb: 'Página web',
		domDirFiscal: 'Domicilio fiscal',
		empIma: 'Imagen',
		empTipMov: 'Tipo Movimiento',
		empCauMov: 'Causa Movimiento',
	    empUsuMov: 'Usuario Mod.',
	    empFecMov: 'Fecha Mod.',
		empAct: 'Activo'
	};

	// Campos mostrados en la tabla
	columnas: string[] = ['empId','empNom','empRazSoc','empNif','empActEco','empTel','empEma','empWeb',
		'domDirFiscal','empIma','empTipMov','empCauMov','empUsuMov','empFecMov','empAct'];
	columnasOcultasPorDefecto = ['empIma','empTipMov','empCauMov'];
	
	// Datos de la tabla
	datos: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	empresaSeleccionado: Empresa | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		route: ActivatedRoute,
		private empresaService: EmpresaService,
		private pdfService: PdfService,
		private domicilioService: DomicilioService
		
	) { this.gestion = !!route.snapshot.data['gestion']; this.domicilioService.obtenerDomicilios().subscribe({next: datos => {this.domicilios = datos; this.datos = this.enriquecerEmpresas(this.datos);}}); }

	// Este método muestra la tabla de datos
	consultar() {
			
		this.vistaActiva = 'tabla';
		this.empresaSeleccionado = null;
		const incluirBajas = !!this.tabla?.filtros?.['empAct'];
		
		this.empresaService.obtenerEmpresas(incluirBajas).subscribe({
			
			next: (respuesta) => {
	
				console.log('respuesta=', respuesta);

				this.datos = this.enriquecerEmpresas(respuesta);

			},

			error: (error) => {

				console.error(error);

				avisarAplicacion('Error al obtener empresas');  

			}

		});
			
	}

	ver() {
		if (!this.empresaSeleccionado) return;
		this.empresa = { ...this.empresaSeleccionado };
		this.modoFormulario = 'ver'; this.vistaActiva = 'registro';
	}

	esActiva(empresa: Empresa | null): boolean {
		return !!empresa && !['false','b','0','n'].includes(String(empresa.empAct).trim().toLowerCase());
	}

	prepararMovimiento(tipo: 'B' | 'R') {
		if (!this.empresaSeleccionado) return;
		this.tipoMovimiento = tipo; this.causaMovimiento = ''; this.vistaActiva = 'movimiento';
	}

	guardarMovimiento() {
		if (!this.empresaSeleccionado || !this.causaMovimiento.trim()) { avisarAplicacion('Debe informar la causa del movimiento.'); return; }
		const peticion = this.tipoMovimiento === 'B'
			? this.empresaService.baja(this.empresaSeleccionado.empId, this.causaMovimiento)
			: this.empresaService.reactivar(this.empresaSeleccionado.empId, this.causaMovimiento);
		peticion.subscribe({next:()=>{avisarAplicacion(this.tipoMovimiento === 'B' ? 'Empresa dada de baja.' : 'Empresa reactivada.');this.consultar();},error:error=>avisarAplicacion(error?.error?.detail || error?.error?.message || 'No se pudo realizar la operación.')});
	}

	verHistorico() {
		if (!this.empresaSeleccionado) return;
		this.empresaService.historico(this.empresaSeleccionado.empId).subscribe({next:datos=>{this.historicoDatos=datos;this.vistaActiva='historico';},error:()=>avisarAplicacion('No se pudo consultar el histórico.')});
	}
		
	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {

	  	this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';
		
		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Usuario.
		this.empresaService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('Id Empresa recibido:', id);

		    this.empresa.empId = id;

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
	    if (!this.empresaSeleccionado) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    this.empresa = { ...this.empresaSeleccionado };

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
	    if (!this.empresaSeleccionado) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = await confirmarAplicacion(
	      '¿Desea eliminar el empresa seleccionado?'
	    ,true);

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.empresaService.eliminar(
	      this.empresaSeleccionado.empId!
	    ).subscribe({

	      next: () => {

	        avisarAplicacion('Empresa eliminado correctamente');

	        // Limpia selección
	        this.empresaSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        avisarAplicacion('Error al eliminar empresa');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'empresas.pdf',
			
			// Título del documento.
			'Listado de empresas',

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
		  	!this.empresa.empNom
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

	 
		const empresa = {

			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.
			empId: 0,	
				
		    empNom: this.empresa.empNom,
			empIma: this.empresa.empIma,
			empRazSoc: this.empresa.empRazSoc, empNif: this.empresa.empNif, empActEco: this.empresa.empActEco,
			empTel: this.empresa.empTel, empEma: this.empresa.empEma, empWeb: this.empresa.empWeb, domId: this.empresa.domId,
	
		    empUsuMov: this.empresa.empUsuMov,
		    empFecMov: this.empresa.empFecMov,	
			empAct: this.empresa.empAct,

	  	};
	  
	  console.log(empresa);
	  
	  this.empresaService.guardar(empresa).subscribe({

	    next: (guardada: any) => {


	      avisarAplicacion('Empresa guardado correctamente');
		  
		  this.limpiarFormulario();

		  this.consultar();

	    },

	    error: (error: any) => {

	      console.error(error);

	      avisarAplicacion('Error al guardar empresa');

	    }

	  });

	}
	
	// Este método actualiza el contenido del formulario en base de datos
	actualizar() {
		
		// Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }
		
		
		const empresa = {

			// Mantiene el identificador del empresa que se va a modificar
			empId: this.empresa.empId,
		    empNom: this.empresa.empNom,
			empIma: this.empresa.empIma,
			empRazSoc: this.empresa.empRazSoc, empNif: this.empresa.empNif, empActEco: this.empresa.empActEco,
			empTel: this.empresa.empTel, empEma: this.empresa.empEma, empWeb: this.empresa.empWeb, domId: this.empresa.domId,

		    empUsuMov: this.empresa.empUsuMov,
		    empFecMov: this.empresa.empFecMov,
			empAct: this.empresa.empAct

		};

		console.log('EMPRESA A ACTUALIZAR:', empresa);

		this.empresaService.actualizar(empresa).subscribe({

			next: (actualizada) => {


				avisarAplicacion('Empresa actualizado correctamente.');

				this.limpiarFormulario();

				this.consultar();

			},

			error: (error: any) => {

				console.error(error);

				avisarAplicacion('Error al actualizar empresa.');

			}

		});

	}

	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/administracion']);
	}	
	
	// Este método crea un usuario vacío
 	private crearEmpresaVacio(): Empresa {

		return {

	  	empId: 0,
		
	  	empNom: '',
		empIma: '',
		empRazSoc: '', empNif: '', empActEco: '', empTel: '', empEma: '', empWeb: '', domId: null,
		
		empUsuMov: localStorage.getItem('usuario') || '',
		empFecMov: FechasUtil.formatearFechaHora(),
		empAct: true,
		empTipMov: 'A', empCauMov: ''

		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.empresa = this.crearEmpresaVacio();
	  
	 }


	adjuntos() { if (this.empresaSeleccionado) this.vistaActiva = 'adjuntos'; }

	private enriquecerEmpresas(empresas: any[]): any[] {
		return (empresas || []).map(empresa => ({
			...empresa,
			domDirFiscal: this.domicilios.find(d => Number(d.empId) === Number(empresa.empId) && Number(d.domId) === Number(empresa.domId))?.domDir || ''
		}));
	}
	
}
