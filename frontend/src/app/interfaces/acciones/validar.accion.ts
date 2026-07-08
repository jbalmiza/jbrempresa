export interface Validacion {

    /** Nombre interno */
    nombre: string;

    /** Título */
    titulo: string;

    /** Descripción */
    descripcion?: string;

    /** Mensaje mostrado si la validación falla */
    mensaje: string;

    /** Acción que realiza la validación */
    accion: Accion;

}