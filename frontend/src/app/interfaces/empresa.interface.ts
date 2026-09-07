export interface Empresa {

	//Datos Identificación
	empId: number;

	//Datos Empresa
  	empNom: string;
	empIma?: string;

	//Datos Movimiento
  	empUsuMov: string;
  	empFecMov: string;
	empAct: boolean;

}
