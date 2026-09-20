export type TipoRecursoAgenda='EMPLEADO'|'DOMICILIO';
export type EstadoReserva='PENDIENTE'|'CONFIRMADA'|'EN_CURSO'|'TERMINADA'|'CANCELADA'|'AUSENCIA';
export type IntervaloAgenda=5|10|15|30|60;
export const INTERVALOS_AGENDA=[{minutos:5,etiqueta:'5 min'},{minutos:10,etiqueta:'10 min'},{minutos:15,etiqueta:'15 min'},{minutos:30,etiqueta:'30 min'},{minutos:60,etiqueta:'1 hora'}] as const;
export interface RecursoAgenda{ragId:number;ragTip:TipoRecursoAgenda;ragRefId:number;ragNom:string;ragCap:number;ragMarPre:number;ragMarPos:number;ragHorVis:string;ragIntVis:IntervaloAgenda;}
export interface RecursoAgendaEntrada{ragTip:TipoRecursoAgenda;ragRefId:number;ragNom:string;ragCap:number;ragMarPre:number;ragMarPos:number;ragHorVis:string;ragIntVis:IntervaloAgenda;}
export interface HorarioAgenda{horId?:number;horDia:number;horIni:string;horFin:string;}
export interface ExcepcionAgenda{exrId?:number;exrFec:string;exrIni:string|null;exrFin:string|null;exrDis:boolean;exrCap:number|null;exrMot:string;}
export type TipoTarea='PRODUCTO'|'SERVICIO';
export type EstadoTarea='PENDIENTE'|'EN_CURSO'|'FINALIZADO';
export interface TareaReserva{tarId?:number;ragId:number|null;dvdId?:number|null;tarTip?:TipoTarea;proId?:number|null;serId?:number|null;tarHab?:string;tarCan?:number;tarDurUni?:number;tarOrd:number;tarTit:string;tarDurMin:number;tarEst?:EstadoTarea;tarIniPre?:string|null;tarFinPre?:string|null;tarIniRea?:string|null;tarFinRea?:string|null;}
export interface ReservaAgenda{resId:number;perId:number|null;dovId:number|null;comId:number|null;resIni:string;resFin:string;resEst:EstadoReserva;resDurMin:number;resTit:string;resObs:string;ragId:number;pagado:boolean|null;tareas:TareaReserva[];}
export interface ReservaAgendaEntrada{perId:number|null;dovId:number|null;comId:number|null;resIni:string;resFin:string;resTit:string;resObs:string;ragId:number;tareas:TareaReserva[];}
export interface ReprogramacionAgenda{rprId:number;rprIniAnt:string;rprFinAnt:string;rprIniNue:string;rprFinNue:string;rprMot:string;rprUsuMov:string;rprFecMov:string;}
export interface TareaAgendaEmpleado{tarId:number;resId:number;pedidoId:number|null;dvdId:number|null;pagado:boolean;tipo:TipoTarea;habilidad:string;titulo:string;cantidad:number;duracionUnitaria:number;duracionTotal:number;estado:EstadoTarea;inicioPrevisto:string|null;finPrevisto:string|null;inicioReal:string|null;finReal:string|null;}
export interface AgendaEmpleado{ragId:number;empleado:string;tareas:TareaAgendaEmpleado[];}
