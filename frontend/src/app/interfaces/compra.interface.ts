import { CompraDetalle } from './compraDetalle.interface';
export interface Compra {
 proveedorEmpresaId?: number|null;pedidoVentaId?:number|null;pedidoVentaNumero?:string|null;proveedorNombre?:string|null;
 detalles?: CompraDetalle[];

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
	comImpPag: number;
	comImpPen: number;
	
	comFecPre: string;
	comFecPed: string;
	comFecAlb: string;
	comFecFac: string;
	comFecPag: string;
 
  	comUsuMov: string;
  	comFecMov: string;
	comAct: boolean;

}
