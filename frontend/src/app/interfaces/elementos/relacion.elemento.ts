import { TipoCardinalidad } from '../enums/tipoCardinalidad.enum';
import { TipoRelacion } from '../enums/tipoRelacion.enum';

export interface Relacion {

    /** Nombre de la relación */
    nombre: string;

    /** Entidad relacionada */
    entidad: string;

    /** Tipo de relación */
    tipo: TipoRelacion;

    /** Cardinalidad */
    cardinalidad: TipoCardinalidad;

}