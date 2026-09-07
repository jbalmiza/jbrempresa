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

import { FechasUtil } from '../../../shared/utils/fechas.util';

import { Perfil } from '../../../interfaces/perfil.interface';

import { FormsModule } from '@angular/forms';

import { PerfilService } from '../../../services/perfil.service';
import { PdfService } from '../../../services/pdf.service';

import { ViewChild } from '@angular/core';

// Se define la configuración del componente Angular
@Component({
  selector: 'Perfiles',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla,DatosIdentificacion,DatosMovimiento,BarraAcciones],
  templateUrl: './perfiles.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Perfiles {
	
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
	
	// Se crea un objeto usuario con datos vacíos
	perfil: Perfil = this.crearPerfilVacio();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    empId: 'Id Empresa',
	    perId: 'Id Persona',
	    perNom: 'Nombre',
	    perTipPer: 'Tipo Perfil',
	    perModAdm: 'Mód. Adm.',
	    perModTer: 'Mód. Ter.',
	    perModPer: 'Mód. Per.',
	    perModPro: 'Mód. Pro.',
	    perModVen: 'Mód. Ven.',
	    perUsuMov: 'Usuario Mod.',
	    perFecMov: 'Fecha Mod.',
		perAct: 'Activo'
	};
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'empId', 'perId', 
		'perNom', 'perTipPer',
		'perModAdm', 'perModTer', 'perModPer', 'perModPro', 'perModVen',
		'perUsuMov', 'perFecMov','perAct'
	  
	];
	
	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de perfiles
	perfilesLista: Perfil[] = [];
	
	// Guarda el registro seleccionado de la tabla
	perfilSeleccionado: Perfil | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private perfilService: PerfilService,
		private pdfService: PdfService
		
	) {}

	// Este método muestra la tabla de datos
	consultar() {
			
		this.vistaActiva = 'tabla';	 
		
		this.perfilService.obtenerPerfiles().subscribe({
			
			next: (respuesta) => {
	
				console.log('respuesta=', respuesta);

				this.datos = respuesta;

			},

			error: (error) => {

				console.error(error);

				avisarAplicacion('Error al obtener perfiles');  

			}

		});
			
	}
		
	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {

	  	this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';
		
		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Usuario.
		this.perfilService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('Id Perfil recibido:', id);

		    this.perfil.perId = id;

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
	    if (!this.perfilSeleccionado) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.perfil = {

			empId: this.perfilSeleccionado.empId,			
			
			perId: this.perfilSeleccionado.perId,
			
			perNom: this.perfilSeleccionado.perNom,

			perTipPer: this.perfilSeleccionado.perTipPer,
			
			perModAdm: this.perfilSeleccionado.perModAdm,

			perModTer: this.perfilSeleccionado.perModTer,

			perModPer: this.perfilSeleccionado.perModPer,
			
			perModPro: this.perfilSeleccionado.perModPro,

			perModVen: this.perfilSeleccionado.perModVen,

			perUsuMov: this.perfilSeleccionado.perUsuMov,

			perFecMov: this.perfilSeleccionado.perFecMov,
			
			perAct: this.perfilSeleccionado.perAct

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
	    if (!this.perfilSeleccionado) {

	      avisarAplicacion('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = await confirmarAplicacion(
	      '¿Desea eliminar el perfil seleccionado?'
	    ,true);

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.perfilService.eliminar(
	      this.perfilSeleccionado.perId!
	    ).subscribe({

	      next: () => {

	        // Limpia selección
	        this.perfilSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();
			
			avisarAplicacion('Perfil eliminado correctamente');
			
	      },

	      error: (error) => {

	        console.error(error);

	        avisarAplicacion('Error al eliminar perfil');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'perfiles.pdf',
			
			// Título del documento.
			'Listado de perfiles',

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
			!this.perfil.perNom ||
			!this.perfil.perTipPer
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


	  	const perfil = {

			empId: this.perfil.empId,		
			// Se envía 0 porque la interfaz utiliza 'number' y no admite null.
			// El backend interpreta este registro como nuevo e ignora este valor,
			// dejando que la base de datos asigne automáticamente el identificador definitivo.
		    perId: 0,
			
		    perNom: this.perfil.perNom,
	
		    perTipPer: this.perfil.perTipPer,
					
			perModAdm: this.perfil.perModAdm,
	
		    perModTer: this.perfil.perModTer,
	
		    perModPer: this.perfil.perModPer,
			
			perModPro: this.perfil.perModPro,
	
			perModVen: this.perfil.perModVen,
	
		    perUsuMov: this.perfil.perUsuMov,
	
		    perFecMov: this.perfil.perFecMov,
			
			perAct: this.perfil.perAct

	  	};
	  
	  console.log('PERFIL A ENVIAR:', perfil);
	  
	  this.perfilService.guardar(perfil).subscribe({

	    next: () => {

	      avisarAplicacion('Perfil guardado correctamente.');
		  
		  this.limpiarFormulario();

		  this.consultar();

	    },

	    error: (error: any) => {

	      console.error(error);

	      avisarAplicacion('Error al guardar perfil');

	    }

	  });

	}
	
	// Este método actualiza el contenido del formulario en base de datos
	actualizar() {
		
		// Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Comprueba los campos obligatorios
		if (!this.validarObligatorios()) { return; }

		const perfil = {

			empId: this.perfil.empId,
		    perId: this.perfil.perId,

		    perNom: this.perfil.perNom,

		    perTipPer: this.perfil.perTipPer,

			perModAdm: this.perfil.perModAdm,

		    perModTer: this.perfil.perModTer,

		    perModPer: this.perfil.perModPer,

			perModPro: this.perfil.perModPro,

			perModVen: this.perfil.perModVen,

		    perUsuMov: this.perfil.perUsuMov,

		    perFecMov: this.perfil.perFecMov,

			perAct: this.perfil.perAct

		};

		console.log('PERFIL A ACTUALIZAR:', perfil);

		this.perfilService.actualizar(perfil).subscribe({

			next: () => {

				avisarAplicacion('Perfil actualizado correctamente.');

				this.limpiarFormulario();

				this.consultar();

			},

			error: (error: any) => {

				console.error(error);

				avisarAplicacion('Error al actualizar perfil.');

			}

		});

	}

	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/administracion']);
	}	
	
	// Este método crea un usuario vacío
 	private crearPerfilVacio(): Perfil {

		return {

		empId: Number(localStorage.getItem('empresaId')) || 0,
	  	perId: 0,
		
	  	perNom: '',
	  	perTipPer: '',
		
	  	perModAdm: false,
	  	perModTer: false,
		perModPer: false,
		perModPro: false,
		perModVen: false,
		
		perUsuMov: localStorage.getItem('usuario') || '',
		perFecMov: '',
		perAct: true,

		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.perfil = this.crearPerfilVacio();
	  
	 }
	
}
