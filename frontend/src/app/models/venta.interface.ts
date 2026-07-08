export interface Venta {

	// Datos Identificación
	cliId: number;	
  	venId: number;

  	perIdVen: number;
	perIdCom: number;
	
	venImpSub: number;
	venImpDes: number;
	venImpIva: number;
	venImpTot: number;
	venImpCob: number;
	venImpPen: number;
	
	venFecPre: string;
	venFecPed: string;
	venFecAlb: string;
	venFecFac: string;
	venFecCob: string;
 
  	venUsuMov: string;
  	venFecMov: string;
	venAct: boolean;

}