// Importa las clases necesarias de Angular.
import {
    AfterViewInit,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    OnDestroy,
    Output,
    ViewChild
} from '@angular/core';

// Importa Observable.
import { forkJoin, Observable, of } from 'rxjs';

// Importa ViewEncapsulation.
import { ViewEncapsulation } from '@angular/core';

// Importa MallaService.
import { MallaService } from '../../services/malla.service';
import { BlobUrlUtil } from '../../shared/utils/blob-url.util';

// Importa la interfaz Malla.
import { Malla } from '../../interfaces/malla.interface';
import { CommonModule } from '@angular/common';

// Define el componente.
@Component({

    // Selector HTML.
    selector: 'mallaRegistros',

    // Componente independiente.
    standalone: true,

    // Componentes utilizados.
    imports: [CommonModule],

    // Vista HTML.
    templateUrl: './mallaRegistros.html',

    // Hoja de estilos.
    styleUrl: './mallaRegistros.css',

    // Permite aplicar los estilos a las celdas creadas dinámicamente.
    encapsulation: ViewEncapsulation.None

})
export class MallaRegistros implements AfterViewInit, OnDestroy {
    private readonly blobs=new BlobUrlUtil();

    registrosPendientes: Array<{ registro: any; etiqueta: string }> = [];
    posicionPendiente = '';
    vistaPreviaVisible = false;
    vistaPreviaNombre = '';
    vistaPreviaInformacion: string[] = [];
    vistaPreviaImagen = '';
    vistaPreviaX = 0;
    vistaPreviaY = 0;

    // Devuelve el registro asociado a una posición ocupada.
    @Output()
    registroSeleccionado = new EventEmitter<any>();

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

    // Función opcional para enriquecer la información de registros vinculados.
    @Input()
    obtenerDetalles?: () => Observable<any[]>;

    // Identificador del registro de detalle.
    @Input()
    campoIdDetalle = '';

    // Formatea la información mostrada al pasar el cursor.
    @Input()
    formatearDetalle?: (detalle: any) => string;

    @Input()
    obtenerImagenDetalle?: (detalle: any) => Observable<Blob>;

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

        // Obtiene la malla y, si se ha configurado, sus registros asociados.
        forkJoin({
            registros: this.obtenerRegistros(),
            detalles: this.obtenerDetalles ? this.obtenerDetalles() : of([])
        }).subscribe({

            next: ({ registros, detalles }) => {

                // Conserva únicamente los registros
                // de la entidad seleccionada.
                const registrosEntidad =
                    registros.filter(
                        registro =>
                            registro.malEnt === this.entidad
                    );

                // Construye la malla con los registros recibidos.
                this.crearMalla(registrosEntidad, detalles);

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
    private crearMalla(registros: any[], detalles: any[]): void {

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
            `32px repeat(${this.columnasMalla}, 21px)`;

        const detallesPorId = new Map(
            detalles.map(detalle => [Number(detalle[this.campoIdDetalle]), detalle])
        );

        const esquina = document.createElement('div');
        esquina.classList.add('cabecera-malla', 'esquina-malla');
        malla.appendChild(esquina);

        for (let columna = 1; columna <= this.columnasMalla; columna++) {
            const cabecera = document.createElement('div');
            cabecera.classList.add('cabecera-malla', 'cabecera-columna');
            cabecera.textContent = String(columna);
            malla.appendChild(cabecera);
        }

        const registrosPorPosicion = new Map<string, any[]>();
        for (const registro of registros) {
            const clave = `${Number(registro[this.campoFila])}-${Number(registro[this.campoColumna])}`;
            const agrupados = registrosPorPosicion.get(clave) ?? [];
            agrupados.push(registro);
            registrosPorPosicion.set(clave, agrupados);
        }

        // Recorre las filas.
        for (
            let fila = 1;
            fila <= this.filasMalla;
            fila++
        ) {

            const cabeceraFila = document.createElement('div');
            cabeceraFila.classList.add('cabecera-malla', 'cabecera-fila');
            cabeceraFila.textContent = String(fila);
            malla.appendChild(cabeceraFila);

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
                const registrosCelda = registrosPorPosicion.get(`${fila}-${columna}`) ?? [];
                const registro = registrosCelda[0];

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

                    // Muestra la información al pasar el ratón.
                    const detalle = detallesPorId.get(Number(registro.malRefId));
                    const informacion = detalle && this.formatearDetalle
                        ? this.formatearDetalle(detalle)
                        : titulo;

                    // Las posiciones ocupadas utilizan exclusivamente la vista previa enriquecida.
                    celda.removeAttribute('title');
                    celda.onmouseenter = () => this.mostrarVistaPrevia(celda, detalle, informacion, fila, columna);
                    celda.onmouseleave = () => this.ocultarVistaPrevia();

                    if (registrosCelda.length > 1) {
                        const contador = document.createElement('span');
                        contador.classList.add('contador-registros');
                        contador.textContent = String(registrosCelda.length);
                        celda.appendChild(contador);
                        celda.removeAttribute('title');
                    }

                }

                // Si la posición está libre.
                else {

                    // Muestra la posición al pasar el ratón.
                    celda.title =
                        `Fila ${fila} - Columna ${columna}`;

                }

                // Permite actuar sobre la celda al hacer clic.
                celda.onclick = () => {

                    // Una posición vinculada a un registro abre su gestión.
                    if (registrosCelda.some(item => item?.malRefId)) {
                        const vinculados = registrosCelda.filter(item => item?.malRefId);
                        if (vinculados.length === 1) {
                            this.registroSeleccionado.emit(vinculados[0]);
                            return;
                        }
                        this.posicionPendiente = `Fila ${fila} · Columna ${columna}`;
                        this.registrosPendientes = vinculados.map(item => {
                            const detalle = detallesPorId.get(Number(item.malRefId));
                            const etiqueta = detalle && this.formatearDetalle
                                ? this.formatearDetalle(detalle)
                                : item[this.campoTitulo] || `ID ${item.malRefId}`;
                            return { registro: item, etiqueta };
                        });
                        return;

                    }

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
    seleccionarRegistro(registro: any): void {
        this.cerrarSelector();
        this.registroSeleccionado.emit(registro);
    }

    private mostrarVistaPrevia(celda: HTMLElement, detalle: any, informacion: string, fila: number, columna: number): void {
        if (!detalle) return;
        const posicion = celda.getBoundingClientRect();
        const lineas = informacion.split('\n').filter(Boolean);
        this.vistaPreviaNombre = lineas[0] || 'Registro';
        this.vistaPreviaInformacion = [...lineas.slice(1), `Fila: ${fila} | Columna: ${columna}`];
        this.vistaPreviaX = Math.min(posicion.right + 10, window.innerWidth - 210);
        this.vistaPreviaY = Math.min(posicion.top, window.innerHeight - 180);
        this.vistaPreviaVisible = true;
        if (!this.obtenerImagenDetalle) return;
        this.obtenerImagenDetalle(detalle).subscribe({
            next: imagen => {
                this.blobs.liberar(this.vistaPreviaImagen);
                this.vistaPreviaImagen = this.blobs.crear(imagen);
            },
            error: () => { this.vistaPreviaImagen = ''; }
        });
    }

    private ocultarVistaPrevia(): void {
        this.vistaPreviaVisible = false;
        this.vistaPreviaInformacion = [];
        this.blobs.liberar(this.vistaPreviaImagen);
        this.vistaPreviaImagen = '';
    }

    ngOnDestroy(): void { this.ocultarVistaPrevia(); }

    cerrarSelector(): void {
        this.registrosPendientes = [];
        this.posicionPendiente = '';
    }

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
            empId: 0,

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
            malFecMov: null

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
