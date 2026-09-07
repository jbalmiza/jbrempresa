// Importa las clases necesarias de Angular
import {
    AfterViewInit,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    Output,
    ViewChild
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { ViewEncapsulation } from '@angular/core';

import { MallaService } from '../../services/malla.service';

import { Malla } from '../../interfaces/malla.interface';

@Component({

    // Nombre del componente
    selector: 'selectorMalla',

    // Componente independiente
    standalone: true,

    // Componentes utilizados
    imports: [CommonModule],

    // Vista HTML
    templateUrl: './selectorMalla.html',

    // Hoja de estilos
    styleUrl: './selectorMalla.css',
	
	encapsulation: ViewEncapsulation.None

})
export class SelectorMalla implements AfterViewInit {

    // Muestra u oculta la ventana
    mostrarMalla = false;

    // Contenedor donde se dibuja la malla
    @ViewChild('contenedor')
    contenedor!: ElementRef<HTMLDivElement>;

    // Número de filas de la malla
    private filasMalla = 50;

    // Número de columnas de la malla
    private columnasMalla = 100;

    // Fila seleccionada
    filaSeleccionada = 1;

    // Columna seleccionada
    columnaSeleccionada = 1;
	
	// Celda actualmente seleccionada
	private celdaSeleccionada?: HTMLDivElement;
	
	// Entidad de Malla
	@Input()
	entidad = "";

    // Fila del producto
    @Input() fila = 0;

    // Columna del producto
    @Input() columna = 0;

    // Devuelve la fila seleccionada
    @Output() filaChange = new EventEmitter<number>();

    // Devuelve la columna seleccionada
    @Output() columnaChange = new EventEmitter<number>();

    // Se ejecuta al crear el componente
    ngAfterViewInit(): void {
    }
	
	constructor(
	    private mallaService: MallaService
	) {}
	
	// Colección de registros de la malla.
	//
	// Contiene todas las posiciones ocupadas obtenidas
	// desde el backend.
	mallas: Malla[] = [];

	// Abre la ventana
	abrirMalla(): void {

	    // Obtiene las mallas del cliente
	    this.mallaService.obtenerMallas().subscribe({

			next: (mallas) => {

			    // Conserva únicamente los registros de la entidad seleccionada.
			    this.mallas = mallas.filter(
			        m => m.malEnt === this.entidad
			    );

			    this.mostrarMalla = true;

			    setTimeout(() => {

			        // Si el producto no tiene posición, empieza en 1,1
			        this.filaSeleccionada = this.fila > 0 ? this.fila : 1;
			        this.columnaSeleccionada = this.columna > 0 ? this.columna : 1;

			        this.crearMalla();

			    }, 100);

			},

	        error: (error) => {

	            console.error('Error al cargar la malla:', error);

	        }

	    });

	}

	// Construye la malla
	crearMalla(): void {
		
		if (!this.contenedor) {
		    return;
		}


	    const malla = this.contenedor.nativeElement;

	    // Borra el contenido anterior
	    malla.innerHTML = '';

	    // Configura la rejilla
	    malla.style.display = 'grid';

	    malla.style.gridTemplateColumns =
	        `repeat(${this.columnasMalla}, 30px)`;

	    const posicionesOcupadas = new Map(
	        this.mallas.map(registro => [
	            `${registro.malFil}-${registro.malCol}`,
	            registro
	        ])
	    );

	    // Crea todas las celdas
	    for (let fila = 1; fila <= this.filasMalla; fila++) {

	        for (let columna = 1; columna <= this.columnasMalla; columna++) {

	            // Crea la celda
	            const celda = document.createElement('div');

	            // Asigna el identificador de la celda
	            celda.id = `celda-${fila}-${columna}`;

	            // Asigna la clase base
	            celda.classList.add('celda');

	            // Comprueba si la posición está ocupada
	            const registro = posicionesOcupadas.get(`${fila}-${columna}`);
	            const esSeleccionActual =
	                fila === this.filaSeleccionada &&
	                columna === this.columnaSeleccionada;

	            // Si existe un registro en esa posición,
	            // marca la celda como ocupada
	            if (registro) {

	                celda.classList.add('ocupada');

	            }

	            // Si es la posición seleccionada,
	            // la marca automáticamente
	            if (esSeleccionActual) {

	                celda.classList.add('celdaSeleccionada');

	                this.celdaSeleccionada = celda;

	            }

	            // Texto mostrado al pasar el ratón
	            celda.title = registro && !esSeleccionActual
	                ? `Fila ${fila} - Columna ${columna} · Posición ocupada`
	                : `Fila ${fila} - Columna ${columna}`;

	            // Evento de selección de la celda
	            if (!registro || esSeleccionActual) {
	                celda.onclick = () => this.seleccionarCelda(
	                    celda,
	                    fila,
	                    columna
	                );
	            }

	            // Añade la celda a la malla
	            malla.appendChild(celda);

	        }

	    }

	    // Sitúa la vista sobre la celda seleccionada
	    if (this.celdaSeleccionada) {

	        this.celdaSeleccionada.scrollIntoView({

	            behavior: 'auto',
	            block: 'center',
	            inline: 'center'

	        });

	    }

	}
	
	// Selecciona una celda
	seleccionarCelda(
	    celda: HTMLDivElement,
	    fila: number,
	    columna: number
	): void {

	    // Quita la selección anterior
	    if (this.celdaSeleccionada) {

	        this.celdaSeleccionada.classList.remove('celdaSeleccionada');

	    }

	    // Marca la nueva
	    celda.classList.add('celdaSeleccionada');

	    this.celdaSeleccionada = celda;

	    // Guarda la posición
	    this.filaSeleccionada = fila;
	    this.columnaSeleccionada = columna;

	}

    // Guarda la selección
	aceptarMalla(): void {

	    this.fila = this.filaSeleccionada;
	    this.columna = this.columnaSeleccionada;

	    this.filaChange.emit(this.fila);
	    this.columnaChange.emit(this.columna);

	    this.cerrarMalla();

	}

    // Cierra la ventana
    cerrarMalla(): void {

        this.mostrarMalla = false;

    }

}
