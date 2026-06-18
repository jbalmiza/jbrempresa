// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';

import { Supbar } from '../../../components/supbar/supbar';

import { Perfil } from '../../../models/perfil.interface';

import { FormsModule } from '@angular/forms';

import { PerfilService } from '../../../services/perfil.service';

import { Tabla } from '../../../components/tabla/tabla';

import { ViewChild } from '@angular/core';

import { PdfService } from '../../../services/pdf.service';

// Se define la configuración del componente Angular
@Component({
  selector: 'Perfiles',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar, Tabla],
  templateUrl: './perfiles.html',
  styleUrl: './perfiles.css'
})

// Definición de la lógica del componente 
export class Perfiles {
	
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
	perfil: Perfil = this.crearPerfilVacio();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Id Cliente',
	    perId: 'Id Persona',
	    perNom: 'Nombre',
	    perTipPer: 'Tipo Perfil',
	    perModAdm: 'Mód. Adm.',
	    perModTer: 'Mód. Ter.',
	    perModPer: 'Mód. Per.',
	    perModPro: 'Mód. Pro.',
	    perModVen: 'Mód. Ven.',
	    perAct: 'Activo',
	    usuMov: 'Usuario Mod.',
	    fecMov: 'Fecha Mod.'
	};
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'perId', 
		'perNom', 'perTipPer',
		'perModAdm', 'perModTer', 'perModPer', 'perModPro', 'perModVen',
		'perAct', 'usuMov', 'fecMov'
	  
	];
	
	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de perfiles
	perfilesLista: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	perfilSeleccionado: any = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private perfilService: PerfilService,
		private pdfService: PdfService
		
	) {}

	// Este método muestra la tabla de datos
	consultar() {
			
		this.pestanaActiva = 'tabla';	 
		
		this.perfilService.obtenerPerfiles().subscribe({
			
			next: (respuesta) => {
	
				console.log('respuesta=', respuesta);

				this.datos = respuesta;

			},

			error: (error) => {

				console.error(error);

				alert('Error al obtener perfiles');  

			}

		});
			
	}
		
	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {

	  	this.pestanaActiva = 'registro';
		
		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Usuario.
		this.perfilService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('Id Perfil recibido:', id);

		    this.perfil.idPerfil = id;

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
	    if (!this.perfilSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.pestanaActiva = 'registro';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.perfil = {

			idCliente: this.perfilSeleccionado.cliId,			
			
			idPerfil: this.perfilSeleccionado.perId,
			

			nombre: this.perfilSeleccionado.perNom,

			tipoPerfil: this.perfilSeleccionado.perTipPer,
			

			mAdministracion: this.perfilSeleccionado.perModAdm,

			mTerritorio: this.perfilSeleccionado.perModTer,

			mPersonas: this.perfilSeleccionado.perModPer,
			
			mProductos: this.perfilSeleccionado.perModPro,

			mVentas: this.perfilSeleccionado.perModVen,
			

			activo: this.perfilSeleccionado.perAct,

			usuarioMovimiento: this.perfilSeleccionado.usuMov,

			fechaMovimiento: this.perfilSeleccionado.fecMov

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
	    if (!this.perfilSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = confirm(
	      '¿Desea eliminar el perfil seleccionado?'
	    );

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.perfilService.eliminar(
	      this.perfilSeleccionado.perId
	    ).subscribe({

	      next: () => {

	        // Limpia selección
	        this.perfilSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();
			
			alert('Perfil eliminado correctamente');
			
	      },

	      error: (error) => {

	        console.error(error);

	        alert('Error al eliminar perfil');

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
	
	// Este método guarda el contenido del formulario en base de datos
	guardar() {
		
		//Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		//Alerta para determinados campos sin valor (obligatorios)
		if (
		  !this.perfil.nombre ||
		  !this.perfil.tipoPerfil
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}


	  const perfil = {

		cliId: this.perfil.idCliente,		
		
	    perId: null,
		

	    perNom: this.perfil.nombre,

	    perTipPer: this.perfil.tipoPerfil,
		
		
		perModAdm: this.perfil.mAdministracion,

	    perModTer: this.perfil.mTerritorio,

	    perModPer: this.perfil.mPersonas,
		
		perModPro: this.perfil.mProductos,

		perModVen: this.perfil.mVentas,

		
	    perAct: this.perfil.activo,

	    usuMov: this.perfil.usuarioMovimiento,

	    fecMov: this.perfil.fechaMovimiento

	  };
	  
	  console.log('PERFIL A ENVIAR:', perfil);
	  
	  this.perfilService.guardar(perfil).subscribe({

	    next: () => {

	      alert('Perfil guardado correctamente');
		  
		  // Cambia a la pestaña registro
		  this.pestanaActiva = 'tabla';

	    },

	    error: (error: any) => {

	      console.error(error);

	      alert('Error al guardar perfil');

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

		idCliente: Number(localStorage.getItem('clienteId')) || 0,
	  	idPerfil: null,
		
	  	nombre: '',
	  	tipoPerfil: '',
		
	  	mAdministracion: false,
	  	mTerritorio: false,
		mPersonas: false,
		mProductos: false,
		mVentas: false,
		
	  	activo: true,
		usuarioMovimiento: localStorage.getItem('usuario') || '',
		fechaMovimiento: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16)

		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.perfil = this.crearPerfilVacio();
	  
	 }
	
}