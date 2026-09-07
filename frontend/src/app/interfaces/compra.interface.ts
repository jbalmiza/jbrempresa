export interface Compra {

	// Datos Identificación
	empId: number;	
  	comId: number;

  	perIdCom: number;
	perIdVen: number;
	compradorNomCom?: string;
	vendedorNomCom?: string;
	
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
