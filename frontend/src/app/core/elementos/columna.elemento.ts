import { Alineacion } from "../enums/tipoAlineacion.enum";
import { TipoDato } from "../enums/tipoDato.enum";

export interface Columna {

    /** Nombre del campo del objeto */
    campo: string;

    /** Texto de la cabecera */
    titulo: string;

    /** Tipo de dato */
    tipo?: TipoDato;

    /** Visible */
    visible?: boolean;

    /** Editable */
    editable?: boolean;

    /** Ordenable */
    ordenable?: boolean;

    /** Filtrable */
    filtrable?: boolean;

    /** Anchura */
    ancho?: string;

    /** Alineación */
    alineacion?: Alineacion;

}