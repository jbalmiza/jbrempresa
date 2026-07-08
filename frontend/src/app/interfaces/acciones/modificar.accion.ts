import { Accion } from "../metadata/accion.model";

export const MODIFICAR: Accion = {

    id: "modificar",

    titulo: "Modificar",

    descripcion: "Modificar el registro seleccionado",

    icono: "edit",

    tooltip: "Modificar",

    requiereSeleccion: true,

    permiteMultiple: false,

    confirmar: false,

    orden: 30

};