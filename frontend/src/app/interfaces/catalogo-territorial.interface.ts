export interface Pais { paiId:number; empId:number; paiCod:string; paiNom:string; paiUsuMov:string; paiFecMov:string; paiAct:boolean; }
export interface Provincia { prvId:number; empId:number; paiId:number; prvCod:string; prvNom:string; prvUsuMov:string; prvFecMov:string; prvAct:boolean; }
export interface Municipio { munId:number; empId:number; prvId:number; munCod:string; munNom:string; munUsuMov:string; munFecMov:string; munAct:boolean; }
export interface CodigoPostal { copId:number; empId:number; munId:number; copCod:string; copUsuMov:string; copFecMov:string; copAct:boolean; }
export interface Via { viaId:number; empId:number; copId:number; viaTip:string; viaCod:string; viaNom:string; viaUsuMov:string; viaFecMov:string; viaAct:boolean; }
export type CatalogoTerritorial = Pais|Provincia|Municipio|CodigoPostal|Via;
