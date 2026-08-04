import { Accion } from '../elementos/accion.elemento';

export interface Boton {

    /** Nombre interno */
    nombre: string;

    /** Texto mostrado */
    titulo: string;

    /** Descripción */
    descripcion?: string;

    /** Icono */
    icono?: string;

    /** Tooltip */
    tooltip?: string;

    /** Visible */
    visible?: boolean;

    /** Habilitado */
    habilitado?: boolean;

    /** Requiere confirmación */
    confirmar?: boolean;

    /** Acciones a ejecutar */
    acciones: Accion[];

}