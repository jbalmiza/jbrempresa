import { Accion } from "../metadata/accion.model";

export const CONSULTAR: Accion = {

    id: "consultar",

    titulo: "Consultar",

    descripcion: "Consultar registros",

    icono: "search",

    tooltip: "Consultar",

    requiereSeleccion: false,

    permiteMultiple: false,

    confirmar: false,

    orden: 10

};