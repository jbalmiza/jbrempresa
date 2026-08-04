import { TipoDato } from "../enums/tipoDato.enum";

export interface Campo {

    /** Nombre del campo */
    campo: string;

	/** Descripción */
	descripcion?: string;
	
    /** Tipo de dato */
    tipo: TipoDato;

    /** Longitud */
    longitud?: number;

    /** Decimales */
    decimales?: number;

    /** Obligatorio */
    obligatorio?: boolean;

    /** Editable */
    editable?: boolean;

    /** Valor por defecto */
    valorDefecto?: any;

    /** Calculado */
    calculado?: boolean;

}