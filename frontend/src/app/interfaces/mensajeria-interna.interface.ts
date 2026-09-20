export type ClasificacionMensaje='NORMAL'|'AVISO'|'ALERTA';
export interface DestinatarioMensaje{usuarioId:number;empresaId:number;nombre:string;usuario:string;perfil:string}
export interface ConversacionInterna{id:number;asunto:string;participantes:DestinatarioMensaje[];ultimoMensaje:string;ultimoEmisor:string;ultimoDestinatarios:DestinatarioMensaje[];clasificacion:ClasificacionMensaje;fechaUltima:string;noLeidos:number;activa:boolean;fechaBaja:string|null;bajaPor:string|null}
export interface MensajeInterno{id:number;conversacionId:number;remitenteId:number;remitente:string;destinatarios:DestinatarioMensaje[];contenido:string;clasificacion:ClasificacionMensaje;fecha:string;fechaModificacion:string|null;propio:boolean}
