import { Producto } from '../../interfaces/producto.interface';

/** Los precios se introducen sin IVA y ambos totales incluyen su IVA. */
export class PreciosProductoUtil {
  static redondear(valor: number): number { return Math.round((valor + Number.EPSILON) * 100) / 100; }

  static ventaConIva(producto: Producto): number {
    return this.redondear(Number(producto.proPreVen || 0) * (1 + Number(producto.proPreIva || 0) / 100));
  }

  static actualizarFinal(producto: Producto): void {
    const descuento = Number(producto.proPreDes || 0);
    const iva = Number(producto.proPreIva || 0);
    producto.proPreFin = this.redondear(Number(producto.proPreVen || 0) * (1 - descuento / 100) * (1 + iva / 100));
  }

  static actualizarCompra(producto: Producto): void {
    producto.proTotCom = this.redondear(Number(producto.proPreCom || 0) * (1 - Number(producto.proDesCom || 0) / 100) * (1 + Number(producto.proIvaCom || 0) / 100));
  }

  static desdeCompra(producto: Producto, beneficio: number): void {
    producto.proPreVen = this.redondear(Number(producto.proPreCom || 0) * (1 + beneficio / 100));
    this.actualizarFinal(producto);
  }

  static desdeVenta(producto: Producto, beneficio: number): void {
    producto.proPreCom = this.redondear(Number(producto.proPreVen || 0) * (1 - beneficio / 100));
    this.actualizarCompra(producto);
    producto.proPreComEst = true;
  }
}
