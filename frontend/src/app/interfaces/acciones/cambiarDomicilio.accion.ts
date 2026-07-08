import { Accion } from "../metadata/accion.model";

export const CAMBIAR_DOMICILIO: Accion = {

    id: "cambiarDomicilio",

    titulo: "Cambiar domicilio",

    descripcion: "Cambiar el domicilio de la persona seleccionada",

    icono: "home",

    tooltip: "Cambiar domicilio",

    requiereSeleccion: true,

    permiteMultiple: false,

    confirmar: false,

    orden: 100

};