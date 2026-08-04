export interface Accion {

    /** Identificador único */
    id: string;

    /** Texto que verá el usuario */
    titulo: string;

    /** Descripción opcional */
    descripcion?: string;

    /** Icono */
    icono?: string;

    /** Tooltip */
    tooltip?: string;

    /** Necesita un registro seleccionado */
    requiereSeleccion?: boolean;

    /** Necesita varios registros */
    permiteMultiple?: boolean;

    /** Acción peligrosa */
    confirmar?: boolean;

    /** Orden de aparición */
    orden?: number;

}
