export interface Producto {

	empId: number;	
  	proId: number;
	proIdHis?: number;
	proTipMov?: string;
	proCauMov?: string;

  	proTipPro: string;
  	proNom: string;
	proDes: string;
	
	proCat: string;
	proSubCat: string;
	proMar: string;
	proMod: string;
	proPro: string;
	
	proPreCom: number;
	proPreVen: number;
	proPreDes: number;
	proPreIva: number;
	proPreFin: number;
	
	proStoAct: number;
	proStoMin: number;
	proUniMed: string;
	proConSto: boolean;
	proObs: string;
	proDurMin: number;
	proVisCat?: boolean;
	proIma?: string;

	proUbi: string;	
	proFilMal: number;
	proColMal: number;

  	proUsuMov: string;
  	proFecMov: string;
	proAct: boolean;

}
