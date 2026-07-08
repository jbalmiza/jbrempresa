import { Formulario } from '../metadata/formulario.model';
import { TipoDato } from '../enums/tipoDato.enum';

export const PERSONA_FORMULARIO: Formulario = {

    titulo: 'Persona',

    campos: [

        {
            campo: 'Nombre',
            tipo: TipoDato.TEXT
        },

        {
            campo: 'Edad',
            tipo: TipoDato.INTEGER
        }

    ]

};