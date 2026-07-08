import { Toolbar } from './toolbar.model';
import { Columna } from './columna.model';
import { Campo } from './campo.model';

export interface Vista {

    /** Título de la ventana */
    titulo: string;

    /** Barra de acciones */
    toolbar: Toolbar;

    /** Columnas de la tabla */
    columnas: Columna[];

    /** Campos del formulario */
    campos: Campo[];

}