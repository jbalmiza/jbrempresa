// Importa las clases necesarias de Angular.
import {
    AfterViewInit,
    Component,
    ElementRef,
    Input,
    ViewChild
} from '@angular/core';

// Importa Observable.
import { Observable } from 'rxjs';

// Importa ViewEncapsulation.
import { ViewEncapsulation } from '@angular/core';

// Importa MallaService.
import { MallaService } from '../../services/malla.service';

// Importa la interfaz Malla.
import { Malla } from '../../interfaces/malla.interface';

// Define el componente.
@Component({

    // Selector HTML.
    selector: 'mallaRegistros',

    // Componente independiente.
    standalone: true,

    // Componentes utilizados.
    imports: [],

    // Vista HTML.
    templateUrl: './mallaRegistros.html',

    // Hoja de estilos.
    styleUrl: './mallaRegistros.css',

    // Permite aplicar los estilos a las celdas creadas dinámicamente.
    encapsulation: ViewEncapsulation.None

})
export class MallaRegistros implements AfterViewInit {

    // Contenedor donde se dibuja la malla.
    @ViewChild('contenedor')
    contenedor!: ElementRef<HTMLDivElement>;

    // Número de filas de la malla.
    private filasMalla = 50;

    // Número de columnas de la malla.
    private columnasMalla = 100;

    // Función que obtiene los registros.
    @Input()
    obtenerRegistros!: () => Observable<any[]>;

    // Campo identificador.
    @Input()
    campoId!: string;

    // Campo que se mostrará en la malla.
    @Input()
    campoTitulo!: string;

    // Campo que contiene la entidad de malla.
    @Input()
    entidad = '';

    // Campo que contiene la fila.
    @Input()
    campoFila!: string;

    // Campo que contiene la columna.
    @Input()
    campoColumna!: string;

    // Color actualmente seleccionado.
    colorSeleccionado = 'NEGRO';

    // Constructor.
    constructor(
        private mallaService: MallaService
    ) {}

    // Inicializa el componente.
    ngAfterViewInit(): void {

        // Carga los registros.
        this.cargarRegistros();

    }

    // Obtiene los registros.
    private cargarRegistros(): void {

        // Comprueba que se ha recibido la función.
        if (!this.obtenerRegistros) {

            console.error('No se ha recibido obtenerRegistros.');

            return;

        }

        // Obtiene los registros.
        this.obtenerRegistros().subscribe({

            next: (registros) => {

                // Conserva únicamente los registros
                // de la entidad seleccionada.
                const registrosEntidad =
                    registros.filter(
                        registro =>
                            registro.malEnt === this.entidad
                    );

                // Construye la malla con los registros recibidos.
                this.crearMalla(registrosEntidad);

            },

            // Si ocurre un error.
            error: (error) => {

                // Muestra el error.
                console.error(
                    'Error al cargar los registros de la malla:',
                    error
                );

            }

        });

    }

    // Construye la malla.
    private crearMalla(registros: any[]): void {

        // Comprueba que existe el contenedor.
        if (!this.contenedor) {

            console.error('No existe el contenedor de la malla.');

            return;

        }

        // Obtiene el elemento HTML.
        const malla = this.contenedor.nativeElement;

        // Borra el contenido anterior.
        malla.innerHTML = '';

        // Configura la rejilla.
        malla.style.display = 'grid';

        // Define el número y tamaño de las columnas.
        malla.style.gridTemplateColumns =
            `repeat(${this.columnasMalla}, 21px)`;

        // Recorre las filas.
        for (
            let fila = 1;
            fila <= this.filasMalla;
            fila++
        ) {

            // Recorre las columnas.
            for (
                let columna = 1;
                columna <= this.columnasMalla;
                columna++
            ) {

                // Crea la celda.
                const celda =
                    document.createElement('div');

                // Asigna el identificador de la celda.
                celda.id =
                    `celda-${fila}-${columna}`;

                // Asigna la clase base.
                celda.classList.add('celda');

                // Busca un registro situado en esta posición.
                const registro = registros.find(
                    registro =>
                        Number(
                            registro[this.campoFila]
                        ) === fila &&
                        Number(
                            registro[this.campoColumna]
                        ) === columna
                );

                // Si existe un registro en esta posición.
                if (registro) {

                    // Pinta la celda según el tipo almacenado.
                    switch (registro.malTip) {

                        // Posición ocupada por un producto.
                        case 'PRODUCTO':

                            celda.classList.add(
                                'producto'
                            );

                            break;

                        // Color negro.
                        case 'NEGRO':

                            celda.classList.add(
                                'negro'
                            );

                            break;

                        // Color verde.
                        case 'VERDE':

                            celda.classList.add(
                                'verde'
                            );

                            break;

                        // Color rojo.
                        case 'ROJO':

                            celda.classList.add(
                                'rojo'
                            );

                            break;

                        // Color amarillo.
                        case 'AMARILLO':

                            celda.classList.add(
                                'amarillo'
                            );

                            break;

                        // Cualquier otro valor.
                        default:

                            celda.classList.add(
                                'ocupada'
                            );

                            break;

                    }

                    // Obtiene el título del registro.
                    const titulo =
                        registro[this.campoTitulo] ?? '';

                    // Obtiene el identificador del registro.
                    const id =
                        registro[this.campoId] ?? '';

                    // Muestra la información al pasar el ratón.
                    celda.title =
                        `${titulo}\n` +
                        `ID: ${id}\n` +
                        `Fila: ${fila}\n` +
                        `Columna: ${columna}`;

                }

                // Si la posición está libre.
                else {

                    // Muestra la posición al pasar el ratón.
                    celda.title =
                        `Fila ${fila} - Columna ${columna}`;

                }

                // Permite actuar sobre la celda al hacer clic.
                celda.onclick = () => {

                    // Procesa la celda pulsada.
                    this.pulsarCelda(
                        fila,
                        columna
                    );

                };

                // Añade la celda a la malla.
                malla.appendChild(celda);

            }

        }

    }

    // Selecciona el color con el que se pintará la malla.
    seleccionarColor(
            color: string): void {

        // Guarda el color seleccionado.
        this.colorSeleccionado = color;

    }

    // Se ejecuta al pulsar una celda.
    private pulsarCelda(
            fila: number,
            columna: number): void {

        // Si está seleccionado el color blanco.
        if (this.colorSeleccionado === 'BLANCO') {

            // Borra la posición pintada.
            this.mallaService.borrarPosicion(
                this.entidad,
                fila,
                columna
            ).subscribe({

                // Si se elimina correctamente.
                next: () => {

                    // Vuelve a cargar la malla.
                    this.cargarRegistros();

                },

                // Si ocurre un error.
                error: (error) => {

                    // Muestra el error.
                    console.error(
                        'Error al borrar la posición de la malla:',
                        error
                    );

                }

            });

            // Termina el método para no pintar la celda.
            return;

        }

        // Crea los datos de la posición que se quiere pintar.
        const malla = {

            // Identificador nuevo.
            malId: 0,

            // El cliente lo asignará el backend.
            cliId: 0,

            // Entidad a la que pertenece esta malla.
            malEnt: this.entidad,

            // Fila seleccionada.
            malFil: fila,

            // Columna seleccionada.
            malCol: columna,

            // Color seleccionado.
            malTip: this.colorSeleccionado,

            // Una celda pintada manualmente
            // no está asociada a ningún registro.
            malRefId: 0,

            // Descripción de la celda.
            malDes: this.colorSeleccionado,

            // La posición queda activa.
            malAct: true,

            // Usuario de modificación.
            malUsuMov:
                localStorage.getItem('usuario') ?? '',

            // La fecha la asignará el backend.
            malFecMov: ''

        } as Malla;

        // Envía la posición al backend.
        this.mallaService.pintar(malla).subscribe({

            // Si se guarda correctamente.
            next: () => {

                // Vuelve a cargar la malla para mostrar
                // inmediatamente el cambio realizado.
                this.cargarRegistros();

            },

            // Si ocurre un error.
            error: (error) => {

                // Muestra el error.
                console.error(
                    'Error al pintar la posición de la malla:',
                    error
                );

            }

        });

    }

}