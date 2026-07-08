import { Gestion } from "../metadata/gestion.model";
import { PERSONA } from "../entidades/persona.entidad";
import { DOMICILIO } from "../entidades/domicilio.entidad";

export const CAMBIAR_DOMICILIO: Gestion = {

    nombre: "cambiarDomicilio",

    titulo: "Cambiar Domicilio",

    entidades: [
        PERSONA,
        DOMICILIO
    ],

    acciones: []

};