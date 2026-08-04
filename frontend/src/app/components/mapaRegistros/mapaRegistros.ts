// Importa las clases necesarias de Angular
import { AfterViewInit, Component, Input } from '@angular/core';

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

    // Objeto del mapa
    private mapa!: L.Map;

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

            console.log('Registros recibidos:', registros);

            for (const registro of registros) {

                this.crearMarcador(registro);

            }

        });

    }
	
	private crearMarcador(registro: any): void {

	    const x = registro[this.campoX];
	    const y = registro[this.campoY];
	    const epsg = registro[this.campoEpsg];

	    // Ignora registros sin coordenadas
	    if (!x || !y || !epsg) {
	        return;
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
	    L.marker([lat, lon])
	        .addTo(this.mapa)
	        .bindPopup(registro[this.campoTitulo]);

	}

}