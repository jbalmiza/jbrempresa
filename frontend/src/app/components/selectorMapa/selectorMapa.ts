// Importa las clases necesarias de Angular
import { AfterViewInit, Component, Input, Output, EventEmitter } from '@angular/core';

// Importa la librería Leaflet para mostrar el mapa
import * as L from 'leaflet';

// Importa Proj4 para convertir coordenadas
import proj4 from 'proj4';

// Configura las imágenes del marcador por defecto
delete (L.Icon.Default.prototype as any)._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: '/assets/utils/marker-icon-2x.png',
    iconUrl: '/assets/utils/marker-icon.png',
    shadowUrl: '/assets/utils/marker-shadow.png'
});

// Registra los sistemas de coordenadas UTM ETRS89
proj4.defs("EPSG:25829", "+proj=utm +zone=29 +ellps=GRS80 +units=m +no_defs");
proj4.defs("EPSG:25830", "+proj=utm +zone=30 +ellps=GRS80 +units=m +no_defs");
proj4.defs("EPSG:25831", "+proj=utm +zone=31 +ellps=GRS80 +units=m +no_defs");

@Component({

    // Nombre del componente
    selector: 'selectorMapa',

    // Componente independiente
    standalone: true,

    // Componentes utilizados
    imports: [],

    // Vista HTML
    templateUrl: './selectorMapa.html',

    // Hoja de estilos
    styleUrl: './selectorMapa.css'

})
export class SelectorMapa implements AfterViewInit {

    // Muestra u oculta la ventana
    mostrarMapa = false;

    // Objeto del mapa
    private mapa!: L.Map;

    // Marcador de la posición
    private marcador!: L.Marker;

    // Última latitud seleccionada
    private lat = 0;

    // Última longitud seleccionada
    private lon = 0;

    // Coordenada UTM X
    @Input() x = 0;

    // Coordenada UTM Y
    @Input() y = 0;

    // Huso UTM (25829, 25830, 25831...)
    @Input() epsg = 25830;

    // Devuelve la nueva coordenada X
    @Output() xChange = new EventEmitter<number>();

    // Devuelve la nueva coordenada Y
    @Output() yChange = new EventEmitter<number>();

    // Devuelve el huso utilizado
    @Output() epsgChange = new EventEmitter<number>();

    // Se ejecuta al crear el componente
    ngAfterViewInit(): void {
    }
	
	// Abre la ventana del mapa
	abrirMapa(): void {

	    // Muestra la ventana
	    this.mostrarMapa = true;

	    // Espera a que Angular dibuje el HTML
	    setTimeout(() => {

	        // Posición inicial (Málaga)
	        let lat = 36.7213;
	        let lon = -4.4214;

	        // Zoom inicial
	        let zoom = 13;

	        // Utiliza el huso recibido o el 25830 por defecto
	        const epsg = this.epsg || 25830;
			
	        // Si existen coordenadas las convierte a Latitud/Longitud
	        if (this.x > 0 && this.y > 0) {

	            const posicion = proj4(
	                `EPSG:${epsg}`,
	                "EPSG:4326",
	                [this.x, this.y]
	            );

	            lon = posicion[0];
	            lat = posicion[1];

	            // Acerca el mapa
	            zoom = 16;

	        }

	        // Guarda la posición actual
	        this.lat = lat;
	        this.lon = lon;

	        // Si el mapa todavía no existe lo crea
	        if (!this.mapa) {

	            // Crea el mapa
	            this.mapa = L.map('mapa').setView([lat, lon], zoom);

	            // Añade el mapa base
	            L.tileLayer(
	                'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
	                {
	                    maxZoom: 19,
	                    attribution: '&copy; OpenStreetMap'
	                }
	            ).addTo(this.mapa);

	            // Crea el marcador
	            this.marcador = L.marker(
	                [lat, lon],
	                {
	                    draggable: true
	                }
	            ).addTo(this.mapa);

	            // Actualiza la posición al arrastrar el marcador
	            this.marcador.on('dragend', () => {

	                const posicion = this.marcador.getLatLng();

	                this.lat = posicion.lat;
	                this.lon = posicion.lng;

	            });

	            // Permite seleccionar una posición haciendo clic
	            this.mapa.on('click', (e: L.LeafletMouseEvent) => {

	                this.lat = e.latlng.lat;
	                this.lon = e.latlng.lng;

	                this.marcador.setLatLng(e.latlng);

	            });

	        } else {

	            // Centra el mapa
	            this.mapa.setView([lat, lon], zoom);

	            // Mueve el marcador
	            this.marcador.setLatLng([lat, lon]);

	            // Recalcula el tamaño del mapa
	            this.mapa.invalidateSize();

	        }

	    }, 100);

	}
	
	// Guarda la posición seleccionada
	aceptarMapa(): void {

	    // Utiliza el huso recibido o el 25830 por defecto
	    const epsg = this.epsg || 25830;

	    // Convierte Latitud/Longitud a UTM
	    const utm = proj4(
	        "EPSG:4326",
	        `EPSG:${epsg}`,
	        [this.lon, this.lat]
	    );

	    // Devuelve la coordenada X
	    this.xChange.emit(Number(utm[0].toFixed(2)));

	    // Devuelve la coordenada Y
	    this.yChange.emit(Number(utm[1].toFixed(2)));

	    // Devuelve el huso utilizado
	    this.epsgChange.emit(epsg);

	    // Actualiza también los valores internos del componente
	    this.x = Number(utm[0].toFixed(2));
	    this.y = Number(utm[1].toFixed(2));
	    this.epsg = epsg;

	    // Cierra la ventana
	    this.cerrarMapa();

	}

	// Cierra la ventana del mapa
	cerrarMapa(): void {

	    // Oculta la ventana
	    this.mostrarMapa = false;

	}
	
}