// Importa las clases necesarias de Angular
import { AfterViewInit, Component, EventEmitter, Input, Output } from '@angular/core';

// Importa Leaflet
import * as L from 'leaflet';

// Importa Observable
import { Observable } from 'rxjs';

import proj4 from 'proj4';

proj4.defs("EPSG:25829", "+proj=utm +zone=29 +ellps=GRS80 +units=m +no_defs");
proj4.defs("EPSG:25830", "+proj=utm +zone=30 +ellps=GRS80 +units=m +no_defs");
proj4.defs("EPSG:25831", "+proj=utm +zone=31 +ellps=GRS80 +units=m +no_defs");

@Component({

    // Selector HTML
    selector: 'mapaRegistros',

    // Componente independiente
    standalone: true,

    // Componentes utilizados
    imports: [],

    // Vista
    templateUrl: './mapaRegistros.html',

    // Estilos
    styleUrl: './mapaRegistros.css'

})
export class MapaRegistros implements AfterViewInit {

    // Devuelve el registro asociado al marcador seleccionado.
    @Output()
    registroSeleccionado = new EventEmitter<any>();

    // Objeto del mapa
    private mapa!: L.Map;
    private marcadorSeleccionado: L.Marker | null = null;

    // Función que obtiene los registros
    @Input()
    obtenerRegistros!: () => Observable<any[]>;

    // Ruta de navegación
    @Input()
    rutaGestion!: string;

    // Campo identificador
    @Input()
    campoId!: string;

    // Campo que se mostrará en el mapa
    @Input()
    campoTitulo!: string;

    // Campo coordenada X
    @Input()
    campoX!: string;

    // Campo coordenada Y
    @Input()
    campoY!: string;

    // Campo EPSG
    @Input()
    campoEpsg!: string;

    // Inicializa el componente
    ngAfterViewInit(): void {

        // Crea el mapa centrado en Málaga
        this.mapa = L.map('mapaRegistros').setView([36.7213, -4.4214], 13);

        // Mapa base
        L.tileLayer(
            'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
            {
                maxZoom: 19,
                attribution: '&copy; OpenStreetMap'
            }
        ).addTo(this.mapa);

        // Carga los registros
        this.cargarRegistros();

    }

    // Obtiene los registros
    private cargarRegistros(): void {

        // Comprueba que se ha recibido la función
        if (!this.obtenerRegistros) {

            console.error('No se ha recibido obtenerRegistros.');

            return;

        }

        // Obtiene los registros
        this.obtenerRegistros().subscribe(registros => {

            const posiciones: L.LatLngExpression[] = [];

            for (const registro of registros) {

                const posicion = this.crearMarcador(registro);
                if (posicion) posiciones.push(posicion);

            }

            if (posiciones.length === 1) {
                this.mapa.setView(posiciones[0], 16);
            } else if (posiciones.length > 1) {
                this.mapa.fitBounds(L.latLngBounds(posiciones), { padding: [28, 28], maxZoom: 16 });
            }

        });

    }
	
	private crearMarcador(registro: any): L.LatLngExpression | null {

		const campoMovimiento = Object.keys(registro).find(campo => campo.toLowerCase().endsWith('tipmov'));
		if (campoMovimiento && String(registro[campoMovimiento] ?? '').toUpperCase() === 'B') return null;

	    const x = registro[this.campoX];
	    const y = registro[this.campoY];
	    const epsg = registro[this.campoEpsg];

	    // Ignora registros sin coordenadas
	    if (!x || !y || !epsg) {
	        return null;
	    }

	    // Convierte UTM a Lat/Lon
	    const posicion = proj4(
	        `EPSG:${epsg}`,
	        "EPSG:4326",
	        [x, y]
	    );

	    const lon = posicion[0];
	    const lat = posicion[1];

	    // Crea el marcador
	    const coordenadas: L.LatLngExpression = [lat, lon];

	    const marcador = L.marker(coordenadas)
	        .addTo(this.mapa)
	        .bindTooltip(String(registro[this.campoTitulo] ?? 'Sin descripción'))
	        .on('click', () => this.seleccionarMarcador(marcador, registro));

	    return coordenadas;

	}

	private seleccionarMarcador(marcador: L.Marker, registro: any): void {
		if (this.marcadorSeleccionado && this.marcadorSeleccionado !== marcador) {
			this.marcadorSeleccionado.setZIndexOffset(0);
			this.marcadorSeleccionado.setOpacity(1);
			const anterior = this.marcadorSeleccionado.getElement();
			if (anterior) anterior.style.filter = '';
			this.marcadorSeleccionado.closeTooltip();
		}
		this.marcadorSeleccionado = marcador;
		marcador.setZIndexOffset(1000);
		const elemento = marcador.getElement();
		if (elemento) elemento.style.filter = 'drop-shadow(0 0 5px #1d5fa7) saturate(1.35)';
		marcador.openTooltip();
		this.registroSeleccionado.emit(registro);
	}

}
