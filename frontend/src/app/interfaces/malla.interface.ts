export interface Malla {

    // Identificador
    malId: number;

    // Empresa
    empId: number;

    // Entidad de malla (PRODUCTOS, USUARIOS, etc.)
    malEnt: string;

    // Fila de la malla
    malFil: number;

    // Columna de la malla
    malCol: number;

    // Tipo de registro (PRODUCTO, PASILLO, MACETA...)
    malTip: string;

    // Id del elemento relacionado
    malRefId: number;

    // Descripción
    malDes: string;

    // Activo
    malAct: boolean;

    // Usuario de modificación
    malUsuMov: string;

    // Fecha de modificación
    malFecMov: string | null;

}
