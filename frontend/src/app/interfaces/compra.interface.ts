export interface Compra {

	// Datos Identificación
	cliId: number;	
  	comId: number;

  	perIdCom: number;
	perIdVen: number;
	
	comImpSub: number;
	comImpDes: number;
	comImpIva: number;
	comImpTot: number;
	comImpCob: number;
	comImpPen: number;
	
	comFecPre: string;
	comFecPed: string;
	comFecAlb: string;
	comFecFac: string;
	comFecCob: string;
 
  	comUsuMov: string;
  	comFecMov: string;
	comAct: boolean;

}