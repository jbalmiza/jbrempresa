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
  styleUrl: './usuarios.css'
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
	pestanaActiva: 'registro' | 'tabla' = 'tabla';
	
	//Interruptor inactivo para controlar campos obligatorios
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
	    usuAct: 'Activo',
	    usuMov: 'Usuario Mod.',
	    fecMov: 'Fecha Mod.'
	};
	
	// Campos mostrados en la tabla
	columnas: string[] = [ 'cliId', 'usuId', 
		'usuUsu', 'usuCon', 'perId', 'usuNom', 'usuEma',
	  	'usuAct', 'usuMov', 'fecMov'
	  
	];

	// Datos de la tabla
	datos: any[] = [];
	
	// Lista para el selector de perfiles
	usuariosLista: any[] = [];
	
	// Guarda el registro seleccionado de la tabla
	usuarioSeleccionado: any = null;
	
	// Angular inyecta el router en modo lectura
	constructor (
		
		private readonly router: Router, 
		private usuarioService: UsuarioService,
		private pdfService: PdfService
		
	) {}

	// Este método muestra la tabla de datos
	consultar() {
			
		this.pestanaActiva = 'tabla';	 
		
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

	  	this.pestanaActiva = 'registro';
		
		this.limpiarFormulario();
		
		// Obtiene el siguiente id en la lista según los registrados en la tabla. Se utiliza para rellenar el campo Id Usuario.
		this.usuarioService.obtenerSiguienteId().subscribe({

		  next: (id) => {

			console.log('Id Usuario recibido:', id);

		    this.usuario.idUsuario = id;

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
	  if (this.pestanaActiva === 'registro') {

	    // Cambia a la pestaña tabla
	    this.pestanaActiva = 'tabla';

	    return;
	  }

	  // Si estamos en la pestaña tabla
	  if (this.pestanaActiva === 'tabla') {

	    // Comprueba si hay un usuario seleccionado
	    if (!this.usuarioSeleccionado) {

	      alert('Debe seleccionar un registro');

	      return;
	    }

	    // Cambia a la pestaña registro
	    this.pestanaActiva = 'registro';

	    // Copia los datos seleccionados al formulario
		// Convierte formtato backend usu_id a formato frontend idUsuario
		this.usuario = {

			idCliente: this.usuarioSeleccionado.cliId,			
			
			idUsuario: this.usuarioSeleccionado.usuId,

			usuario: this.usuarioSeleccionado.usuUsu,

			contrasena: this.usuarioSeleccionado.usuCon,

			idPerfil: this.usuarioSeleccionado.perId,

			nombre: this.usuarioSeleccionado.usuNom,

			email: this.usuarioSeleccionado.usuEma,

			activo: this.usuarioSeleccionado.usuAct,

			usuarioMovimiento: this.usuarioSeleccionado.usuMov,

			fechaMovimiento: this.usuarioSeleccionado.fecMov

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
	      this.usuarioSeleccionado.usu_id
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
		  !this.usuario.usuario || 
		  !this.usuario.contrasena || 
		  !this.usuario.idPerfil || 
		  !this.usuario.nombre || 
		  !this.usuario.email
		) {

		  alert('Debe rellenar todos los campos obligatorios');

		  return;

		}

	  const usuario = {

		cliId: this.usuario.idCliente,		
		
	    usuId: null,

	    usuUsu: this.usuario.usuario,

	    usuCon: this.usuario.contrasena,
		
		perId: this.usuario.idPerfil,

	    usuNom: this.usuario.nombre,

	    usuEma: this.usuario.email,

	    usuAct: this.usuario.activo,

	    usuMov: this.usuario.usuarioMovimiento,

	    fecMov: this.usuario.fechaMovimiento

	  };
	  
	  console.log(usuario);
	  
	  this.usuarioService.guardar(usuario).subscribe({

	    next: () => {

	      alert('Usuario guardado correctamente');
		  
		  // Cambia a la pestaña registro
		  this.pestanaActiva = 'tabla';

	    },

	    error: (error: any) => {

	      console.error(error);

	      alert('Error al guardar usuario');

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

		idCliente: Number(localStorage.getItem('clienteId')) || 0,
	  	idUsuario: null,
		
	  	usuario: '',
	  	contrasena: '',
	  	idPerfil: 0,
		
	  	nombre: '',
	  	email: '',
		
	  	activo: true,
		usuarioMovimiento: localStorage.getItem('usuario') || '',
		fechaMovimiento: new Date().toLocaleString('sv-SE').replace(' ', 'T').substring(0, 16)
		
		};
	}
	
	// Este método limpia los valores del formulario
	private limpiarFormulario() {

		this.usuario = this.crearUsuarioVacio();
	  
	 }
	
}