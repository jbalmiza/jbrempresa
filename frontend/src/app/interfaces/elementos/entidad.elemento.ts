import { Campo } from './campo.model';
import { Relacion } from './relacion.model';
import { Accion } from './accion.model';

export interface Entidad {

    /** Nombre interno */
    nombre: string;
	
    /** Título visible */
    titulo: string;

    /** Tabla de la base de datos */
    tabla: string;

    /** Campo clave */
    clave: string;

    /** Definición de campos */
    campos: Campo[];

    /** Relaciones */
    relaciones?: Relacion[];

    /** Gestiones disponibles */
    gestiones?: Gestion[];

}