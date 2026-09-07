export interface DocumentoVentaDetalle {
  dvdId: number;
  dovId: number;
  dvdTipLin: '' | 'P' | 'S';
  proId: number | null;
  serId: number | null;
  dvdNom: string;
  dvdObs?: string;
  dvdCan: number;
  dvdPre: number;
  dvdDes: number;
  dvdIva: number;
  dvdImp: number;
  dvdDurUni: number;
  dvdDurTot: number;
  referencia?: number;
}
export interface DocumentoVenta {
  dovId: number;
  empId: number;
  dovTip: 'PRE' | 'PED' | 'ALB' | 'FAC';
  dovNum: string;
  perId: number;
  personaNomCom?: string;
  dovFec: string;
  dovEst: string;
  dovIdOri: number | null;
  dovIdRai: number | null;
  dovUbi: string;
  dovOri?: 'INTERNO' | 'CATALOGO';
  dovMod?: 'EN_POSICION' | 'DOMICILIO';
  dovDirEnv?: string;
  dovFilMal: number | null;
  dovColMal: number | null;
  dovImpSub: number;
  dovImpDes: number;
  dovImpIva: number;
  dovImpTot: number;
  dovObs: string;
  dovUsuMov: string;
  dovFecMov: string;
  dovAct: boolean;
  detalles: DocumentoVentaDetalle[];
}

export interface DocumentoVentaMovimiento {
  dvmId: number;
  empId: number;
  dovId: number;
  dovTip: string;
  dovNum: string;
  perId: number;
  dovFec: string;
  dovEst: string;
  dovUbi: string;
  dovFilMal: number | null;
  dovColMal: number | null;
  dovImpTot: number;
  dvmTipMov: string;
  dvmCauMov: string;
  dvmUsuMov: string;
  dvmFecMov: string;
}
