export interface Empresa {

	//Datos Identificación
	empId: number;

	//Datos Empresa
  	empNom: string;
	empIma?: string;
	empRazSoc?: string;
	empNif?: string;
	empActEco?: string;
	empTel?: string;
	empEma?: string;
	empWeb?: string;
	domId?: number | null;

	//Datos Movimiento
  	empUsuMov: string;
  	empFecMov: string;
	empAct: boolean;
	empTipMov?: string;
	empCauMov?: string;

}
