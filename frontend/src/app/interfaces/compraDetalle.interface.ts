export interface CompraDetalle {
 serId?:number|null;nombre?:string|null;observaciones?:string|null;

	// Datos Identificación
	comDetId: number;
	comId: number;

  	proId: number;
  	comDetCan: number;
  	comDetPre: number;
	comDetDes: number;
	comDetIva: number;
  	comDetImp: number;
	
	comDetUsuMov: string;
	comDetFecMov: string;
	comDetAct: boolean;

}