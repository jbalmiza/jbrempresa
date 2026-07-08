import { Accion } from '../elementos/accion.elemento';
import { Boton } from '../elementos/boton.elemento';
import { Campo } from '../elementos/campo.elemento';
import { Validacion } from '../elementos/validacion.elemento';

export interface Formulario {

    /** Nombre interno */
    nombre: string;

    /** Título */
    titulo: string;

    /** Descripción */
    descripcion?: string;

    /** Icono */
    icono?: string;

    /** Campos */
    campos: Campo[];

    /** Botones disponibles */
    botones?: Boton[];

    /** Acciones asociadas */
    acciones?: Accion[];

    /** Validaciones del formulario */
    validaciones?: Validacion[];

    /** Solo lectura */
    soloLectura?: boolean;

    /** Permite edición */
    editable?: boolean;

    /** Visible */
    visible?: boolean;

}