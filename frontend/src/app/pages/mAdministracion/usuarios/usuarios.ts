// La lógica de la pantalla (Framework Angular / Lenguaje TypeScript)

// Importa libreria para crear componentes Angular
import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

// Importa Routes para definir las rutas de navegación Angular
import { Router } from '@angular/router';

import { Sidebar } from '../../../components/sidebar/sidebar';

import { Supbar } from '../../../components/supbar/supbar';

import { Usuario } from '../../../models/usuario.interface';

import { FormsModule } from '@angular/forms';

import { UsuarioService } from '../../../services/usuario.service';

import { Tabla } from '../../../components/tabla/tabla';

import { ViewChild } from '@angular/core';

import { PdfService } from '../../../services/pdf.service';

// Se define la configuración del componente Angular
@Component({
  selector: 'Usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule, Sidebar, Supbar, Tabla],
  templateUrl: './usuarios.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})

// Definición de la lógica del componente 
export class Usuarios {
	
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
	
	// Se crea un objeto usuario con datos vacíos
	usuario: Usuario = this.crearUsuarioVacio();
	
	// Títulos de las columnas de la tabla
	titulosColumnas = {
	    cliId: 'Cliente',
	    usuId: 'Id Usuario',
	    usuUsu: 'Usuario',
	    usuCon: 'Contraseña',
	    perId: 'Perfil',
	    usuNom: 'Nombre',
	    usuEma: 'Correo Electrónico',
	    usuUsuMov: 'Usuario Mod.',
	    usuFecMov: 'Fecha Mod.',
		usuAct: 'Activo'
	};
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'usuId', 
		'usuUsu', 'usuCon', 'perId', 
		'usuNom', 'usuEma',
	  	'usuUsuMov', 'usuFecMov', 'usuAct'
	  
	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de perfiles
	usuariosLista: Usuario[] = [];
	
	// Guarda el registro seleccionado de la tabla
	usuarioSeleccionado: Usuario | null = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private usuarioService: UsuarioService,
		private pdfService: PdfService
		
	) {}

	// Este método muestra la tabla de datos
	consultar() {
			
		this.vistaActiva = 'tabla';	 
		
		this.usuarioService.obtenerUsuarios().subscribe({
			
			next: (respuesta) => {
	
				console.log('respuesta=', respuesta);

				this.datos = respuesta;

			},

			error: (error) => {

				console.error(error);

				alert('Error al obtener usuarios');  

			}

		});
			
	}
		
	// Este método muestra el formulario de registro y limpia los campos del formulario	
	insertar() {

	  	this.vistaActiva = 'registro';
		this.modoFormulario = 'insertar';
		
		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Usuario.
		this.usuarioService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('Id Usuario recibido:', id);

		    this.usuario.usuId = id;

		  },

		  error: (error) => {

		    console.error(error);

		  }
		});
		
		// Obtiene todos los usuarios registrados. Se utilizarán para rellenar el selector del campo Id Perfil.
		this.usuarioService.obtenerUsuarios().subscribe({

		  next: (respuesta) => {

			console.log('Id Perfil recibidos:', respuesta);
			
		    this.usuariosLista = respuesta;

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
	    if (!this.usuarioSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.vistaActiva = 'registro';
		this.modoFormulario = 'modificar';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.usuario = {

			cliId: this.usuarioSeleccionado.cliId,			
			usuId: this.usuarioSeleccionado.usuId,

			usuUsu: this.usuarioSeleccionado.usuUsu,
			usuCon: this.usuarioSeleccionado.usuCon,
			perId: this.usuarioSeleccionado.perId,

			usuNom: this.usuarioSeleccionado.usuNom,
			usuEma: this.usuarioSeleccionado.usuEma,

			usuUsuMov: this.usuarioSeleccionado.usuUsuMov,
			usuFecMov: this.usuarioSeleccionado.usuFecMov,
			usuAct: this.usuarioSeleccionado.usuAct

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
	    if (!this.usuarioSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;

	    }

	    // Solicita confirmación
	    const confirmado = confirm(
	      '¿Desea eliminar el usuario seleccionado?'
	    );

	    // Si cancela
	    if (!confirmado) {

	      return;

	    }

	    // Elimina el usuario
	    this.usuarioService.eliminar(
	      this.usuarioSeleccionado.usuId!,
		  this.usuarioSeleccionado.cliId
	    ).subscribe({

	      next: () => {

	        alert('Usuario eliminado correctamente');

	        // Limpia selección
	        this.usuarioSeleccionado = null;

	        // Recarga la tabla
	        this.consultar();

	      },

	      error: (error) => {

	        console.error(error);

	        alert('Error al eliminar usuario');

	      }

	    });

	  }

	}
	
	// Exporta los datos visibles de la tabla a un PDF.
	exportar() {

	    // Llama al servicio PDF.
	    this.pdfService.exportar(

	        // Nombre del fichero.
	        'usuarios.pdf',
			
			// Título del documento.
			'Listado de usuarios',

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
		  !this.usuario.usuUsu || 
		  !this.usuario.usuCon || 
		  !this.usuario.perId || 
		  !this.usuario.usuNom || 
		  !this.usuario.usuEma
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const usuario = {

		cliId: this.usuario.cliId,		
	    usuId: null,

	    usuUsu: this.usuario.usuUsu,
	    usuCon: this.usuario.usuCon,
		perId: this.usuario.perId,

	    usuNom: this.usuario.usuNom,
	    usuEma: this.usuario.usuEma,

	    usuUsuMov: this.usuario.usuUsuMov,
	    usuFecMov: this.usuario.usuFecMov,
		usuAct: this.usuario.usuAct

	  };
	  
	  console.log(usuario);
	  
	  this.usuarioService.guardar(usuario).subscribe({

	    next: () => {

	      alert('Usuario guardado correctamente');
		  
		  this.limpiarFormulario();

	    },

	    error: (error: any) => {

	      console.error(error);

	      alert('Error al guardar usuario');

	    }

	  });

	}
	
	// Este método actualiza el contenido del formulario en base de datos
	actualizar() {
		
		// Interruptor activo para controlar campos obligatorios
		this.mostrarObligatorios = true;

		// Alerta para determinados campos sin valor (obligatorios)
		if (
		  !this.usuario.usuUsu || 
		  !this.usuario.usuCon || 
		  !this.usuario.perId || 
		  !this.usuario.usuNom || 
		  !this.usuario.usuEma
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

		const usuario = {

			cliId: this.usuario.cliId,
		    usuId: this.usuario.usuId,

		    usuUsu: this.usuario.usuUsu,
		    usuCon: this.usuario.usuCon,
			perId: this.usuario.perId,

		    usuNom: this.usuario.usuNom,
		    usuEma: this.usuario.usuEma,

		    usuUsuMov: this.usuario.usuUsuMov,
		    usuFecMov: this.usuario.usuFecMov,
			usuAct: this.usuario.usuAct

		};

		console.log(usuario);

		this.usuarioService.actualizar(usuario).subscribe({

			next: () => {

				alert('Usuario actualizado correctamente.');

				this.limpiarFormulario();

			},

			error: (error: any) => {

				console.error(error);

				alert('Error al actualizar usuario.');

			}

		});

	}

	// Se define el método volver
	volver() {

		//Cambia la página a modules
		this.router.navigate(['/administracion']);
	}	
	
	// Este método crea un usuario vacío
 	private crearUsuarioVacio(): Usuario {

		return {

		cliId: Number(localStorage.getItem('clienteId')) || 0,
	  	usuId: 0,
		
	  	usuUsu: '',
	  	usuCon: '',
	  	perId: 0,
		
	  	usuNom: '',
	  	usuEma: '',
		
		usuUsuMov: localStorage.getItem('usuario') || '',
		usuFecMov: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16),
		usuAct: true
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.usuario = this.crearUsuarioVacio();
	  
	 }
	
}