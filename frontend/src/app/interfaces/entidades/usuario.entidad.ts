import { Entidad } from "../metadata/entidad.model";

export const USUARIO: Entidad = {

    nombre: 'usuario',

    titulo: 'Usuario',

    tabla: 'usuarios',

    clave: 'usuId',

    campos: [

        

    ],

    relaciones: [

        

    ],

    gestiones: [

		CAMBIAR_CONTRASENA,
		CAMBIAR_PERFIL,
		CAMBIAR_ESTADO

    ]

};


