export interface Domicilio {

	empId: number;	
  	domId: number;
	domIdHis?: number;
	domTipMov?: string;
	domCauMov?: string;

	domCiv: string;
  	domTipVia: string;
  	domVia: string;
	domViaId?: number | null;
  	domNum: string;
  
  	domKm: string;
  	domEdi: string;
  	domBlo: string;
  	domPor: string;
  	domEsc: string;
  	domPla: string;
  	domPue: string;
	
	domCp: string;
	domMun: string;
	domPro: string;

  	domObs: string;
  	domDir: string;
	
	domCoX: number;
	domCoY: number;
	domHus: number;
  
  	domUsuMov: string;
  	domFecMov: string;
	domAct: boolean;
	
}
