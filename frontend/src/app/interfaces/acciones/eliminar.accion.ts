import { Accion } from "../elementos/accion.elemento";

export const ELIMINAR: Accion = {

    id: "eliminar",

    titulo: "Eliminar",

    descripcion: "Eliminar el registro seleccionado",

    icono: "delete",

    tooltip: "Eliminar",

    requiereSeleccion: true,

    permiteMultiple: false,

    confirmar: true,

    orden: 40

};