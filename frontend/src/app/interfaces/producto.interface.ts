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
	proIvaCom?: number;
	proDesCom?: number;
	proTotCom?: number;
	proPreComEst?: boolean;
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
	proNov?: boolean;
	proMejPre?: boolean;
	proOut?: boolean;
	proDisLun?: boolean;
	proDisMar?: boolean;
	proDisMie?: boolean;
	proDisJue?: boolean;
	proDisVie?: boolean;
	proDisSab?: boolean;
	proDisDom?: boolean;
	proIma?: string;

	proUbi: string;	
	proFilMal: number;
	proColMal: number;

  	proUsuMov: string;
  	proFecMov: string;
	proAct: boolean;
	componentes?: ProductoComponente[];

}

export interface ProductoComponente { prcId?: number; empId?: number; proId?: number; proIdHis?: number; cmpId: number | null; prcCan: number; }
