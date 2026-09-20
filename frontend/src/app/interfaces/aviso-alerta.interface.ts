export type EmisorAviso='ADMINISTRADOR'|'JEFE'|'PROVEEDOR'|'EMPLEADO';
export type DestinatarioAviso='CLIENTE'|'EMPRESA'|'ADMINISTRADOR'|'JEFE'|'EMPLEADO';
export type UbicacionAviso='CATALOGO_CLIENTE'|'CATALOGO_PROVEEDOR'|'VENTANA'|'MENSAJES';
export interface AvisoAlerta { empId:number; aviId:number|null; aviIdHis:number; aviTipMov:string; aviCauMov:string; aviTipo:'AVISO'|'ALERTA'; aviTitulo:string; aviMensaje:string; aviEmisor:EmisorAviso; aviEmisorEmpId:number|null; aviEmisorUsuId:number|null; aviDestinatario:DestinatarioAviso; aviDestEmpId:number|null; aviUbicacion:UbicacionAviso; aviVentana:string|null; aviFecIni:string|null; aviFecFin:string|null; aviUsuMov:string; aviFecMov:string|null; aviAct:boolean; }
export interface AvisoEntregado { empId:number; avisoId:number; historicoId:number; tipo:'AVISO'|'ALERTA'; titulo:string; mensaje:string; emisor:EmisorAviso; empresaEmisora:string; fecha:string; leido:boolean; }
