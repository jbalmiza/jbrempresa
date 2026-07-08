import { Campo } from './campo.elemento';
import { Toolbar } from './toolbar.elemento';
import { Validacion } from './validacion.elemento';

export interface Formulario {

    /** Nombre interno */
    nombre: string;

    /** Título */
    titulo: string;

    /** Descripción */
    descripcion?: string;

    /** Icono */
    icono?: string;

    /** Barra superior */
    toolbarSuperior?: Toolbar;

    /** Campos */
    campos: Campo[];

    /** Barra inferior */
    toolbarInferior?: Toolbar;

    /** Validaciones */
    validaciones?: Validacion[];

}