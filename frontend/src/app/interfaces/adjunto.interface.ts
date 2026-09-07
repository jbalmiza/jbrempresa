export type TipoAdjunto = 'ORIGINAL' | 'COPIA' | 'OTRO';

export interface Adjunto {
  adjId: number;
  empId: number;
  adjMod: string;
  adjTipReg: string;
  adjRegId: number;
  adjNom: string;
  adjTip: TipoAdjunto;
  adjNomArc: string;
  adjRutRel: string;
  adjMime: string;
  adjTam: number;
  adjUsuMov: string;
  adjFecMov: string;
  adjAct: boolean;
  adjPri: boolean;
}
