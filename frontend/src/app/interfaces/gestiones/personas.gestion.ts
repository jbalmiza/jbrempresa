import { Gestion } from "../metadata/gestion.model";

import { PERSONA } from "../entidades/persona.entidad";

import { PERSONAS_VISTA } from "../vistas/personas.vista";

import { CONSULTAR } from "../acciones/consultar.accion";
import { INSERTAR } from "../acciones/insertar.accion";
import { MODIFICAR } from "../acciones/modificar.accion";
import { ELIMINAR } from "../acciones/eliminar.accion";

import { CAMBIAR_DOMICILIO } from "../acciones/cambiarDomicilio.accion";

export const PERSONAS: Gestion = {

    nombre: "personas",

    titulo: "Personas",

    entidades: [
        PERSONA
    ],

    acciones: [
        CONSULTAR,
        INSERTAR,
        MODIFICAR,
        ELIMINAR,
        CAMBIAR_DOMICILIO
    ],

    //vista: PERSONAS_VISTA

};