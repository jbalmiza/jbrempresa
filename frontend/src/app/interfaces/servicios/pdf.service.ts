// Permite que este servicio pueda ser utilizado
// desde cualquier componente Angular.
import { Injectable } from '@angular/core';

// Librería para generar documentos PDF.
import jsPDF from 'jspdf';

// Librería para generar tablas automáticamente
// dentro del PDF.
import autoTable from 'jspdf-autotable';

// Indica que este servicio estará disponible
// en toda la aplicación.
@Injectable({
  providedIn: 'root'
})

// Servicio encargado de generar PDFs.
export class PdfService {
	
	// Exporta los datos recibidos a un documento PDF.
	async exportar(

	    // Nombre del fichero PDF.
	    nombreFichero: string,

		// Título del documento PDF.		
		titulo: string,

	    // Columnas que se mostrarán en la tabla.
	    columnas: string[],

	    // Datos que se exportarán.
	    datos: any[],
		
		// Logo del cliente.
		logoCliente?: string,

		// Logo de la aplicación.
		logoAplicacion?: string

	) {

		// Crea un nuevo documento PDF en orientación horizontal.
		const documento = new jsPDF({

		    orientation: 'landscape'

		});
		
		// Carga el logotipo del cliente.
		const imagenCliente = logoCliente ?
		    await this.cargarImagen(logoCliente) : null;

		// Carga el logotipo de la aplicación.
		const imagenAplicacion = logoAplicacion ?
		    await this.cargarImagen(logoAplicacion) : null;
			
			// Inserta el logotipo del cliente.
			if (imagenCliente) {

			    documento.addImage( imagenCliente, 'PNG', 20, 10, 30, 10 );

			}
			
			// Inserta el logotipo de la aplicación.
			if (imagenAplicacion) {

			    documento.addImage( imagenAplicacion, 'PNG',240, 10, 30, 10 );

			}
		
		// Establece el tamaño de letra del título.
		documento.setFontSize(18);

		// Escribe el título del documento.
		documento.text(titulo, 140, 20, {

		    // Centra el texto horizontalmente.
		    align: 'center'

		});

	    // Genera una tabla automáticamente.
	    autoTable(documento, {

			startY: 30,
			
	        // Cabecera de la tabla.
	        head: [columnas],

	        // Filas de la tabla.
	        body: datos.map(fila =>

	            columnas.map(columna =>

	                fila[columna]

	            )

	        )

	    });
		
		// Obtiene el número total de páginas generadas.
		const totalPaginas = documento.getNumberOfPages();

		// Recorre todas las páginas del PDF.
		for (let i = 1; i <= totalPaginas; i++) {

		    // Selecciona la página actual.
		    documento.setPage(i);

		    // Establece un tamaño de letra más pequeño
		    // para el pie de página.
		    documento.setFontSize(10);

		    // Escribe el número de página en la parte
		    // inferior derecha del documento.
		    documento.text(

		        // Texto que se mostrará.
		        `Página ${i} de ${totalPaginas}`,

		        // Posición horizontal.
		        documento.internal.pageSize.getWidth() - 50,

		        // Posición vertical.
		        documento.internal.pageSize.getHeight() - 10

		    );

		}

	    // Descarga el PDF generado.
	    documento.save(nombreFichero);

	}
	
	// Convierte una imagen en formato Base64
	// para poder insertarla en el PDF.
	private cargarImagen(ruta: string): Promise<string> {

		return new Promise((resolve, reject) => {

			// Crea una nueva imagen.
			const imagen = new Image();

			// Se ejecuta cuando la imagen termina de cargarse.
			imagen.onload = () => {

				// Crea un canvas temporal.
				const canvas = document.createElement('canvas');

				canvas.width = imagen.width;
				canvas.height = imagen.height;

				// Obtiene el contexto de dibujo.
				const contexto = canvas.getContext('2d');

				// Dibuja la imagen sobre el canvas.
				contexto?.drawImage(imagen, 0, 0);

				// Devuelve la imagen convertida a Base64.
				resolve(canvas.toDataURL('image/png'));

			};

			// Si ocurre un error al cargar la imagen.
			imagen.onerror = reject;

			// Indica la ruta de la imagen.
			imagen.src = ruta;

		});

	}

}