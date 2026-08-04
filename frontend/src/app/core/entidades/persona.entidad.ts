import { Entidad } from "../elementos/entidad.elemento";
import { TipoDato } from "../enums/tipoDato.enum";

export const PERSONA: Entidad = {

    nombre: "persona",

    titulo: "Persona",

    tabla: "personas",

    clave: "perId",

    campos: [

        {
            campo: "perId",
            descripcion: "Id. Persona",
            tipo: TipoDato.NUMERO
        },

        {
            campo: "cliId",
            descripcion: "Id. Cliente",
            tipo: TipoDato.NUMERO
        },

        {
            campo: "perTipDoc",
            descripcion: "Tipo de documento",
            tipo: TipoDato.TEXTO,
            longitud: 3
        },

        {
            campo: "perDoc",
            descripcion: "Documento",
            tipo: TipoDato.TEXTO,
            longitud: 30
        },

        {
            campo: "perNom",
            descripcion: "Nombre",
            tipo: TipoDato.TEXTO,
            longitud: 100
        },

        {
            campo: "perApe1",
            descripcion: "Primer apellido",
            tipo: TipoDato.TEXTO,
            longitud: 100
        },

        {
            campo: "perApe2",
            descripcion: "Segundo apellido",
            tipo: TipoDato.TEXTO,
            longitud: 100
        },

        {
            campo: "perFecNac",
            descripcion: "Fecha de nacimiento",
            tipo: TipoDato.FECHA
        },

        {
            campo: "perTel",
            descripcion: "Teléfono",
            tipo: TipoDato.TELEFONO,
            longitud: 20
        },

        {
            campo: "perEma",
            descripcion: "Correo electrónico",
            tipo: TipoDato.EMAIL,
            longitud: 150
        },

        {
            campo: "domId",
            descripcion: "Id. Domicilio",
            tipo: TipoDato.NUMERO
        },

        {
            campo: "perUsuMov",
            descripcion: "Usuario modificación",
            tipo: TipoDato.NUMERO
        },

        {
            campo: "perFecMov",
            descripcion: "Fecha modificación",
            tipo: TipoDato.FECHA
        },

        {
            campo: "perAct",
            descripcion: "Activo",
            tipo: TipoDato.BOOLEAN
        }

    ],

};

