import { Entidad } from './entidad.elemento';
import { Accion } from './accion.elemento';
import { Vista } from './vista.elemento';

export interface Gestion {

    /** Nombre interno */
    nombre: string;

    /** Título */
    titulo: string;

    /** Entidades que intervienen */
    entidades: Entidad[];

    /** Acciones disponibles */
    acciones: Accion[];

    /** Vista asociada */
    vista: Vista;

}