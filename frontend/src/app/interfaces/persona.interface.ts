export interface Persona {

	empId: number;	
  	perId: number;
   perIdHis: number;
   perTipMov: 'A' | 'M' | 'B';
   perCauMov: string;
   perTipPer: 'FISICA' | 'JURIDICA';
   perRazSocCor: string;
   perRazSocLar: string;
  

  	perTipDoc: string;
  	perDoc: string;
	perNomCom: string;

  	perNom: string;
  	perApe1: string;
  	perApe2: string;
	perFecNac: string;

  	perTel: string;
  	perEma: string;

	domId: number;
	perCoX: number;
	perCoY: number;
	perHus: number;

  	perUsuMov: string;
  	perFecMov: string;
	perAct: boolean;

}
