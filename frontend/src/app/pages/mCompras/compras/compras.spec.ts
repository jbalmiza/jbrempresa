import { of } from 'rxjs';
import { Compras } from './compras';
import { CompraService } from '../../../services/compra.service';
import { Compra } from '../../../interfaces/compra.interface';

describe('Líneas de Compras', () => {
  const linea = {proId:1,comDetCan:2,comDetPre:10,comDetDes:0,comDetIva:21,comDetImp:24.2};
  it('recupera las líneas al editar sin modificar la copia consultada', () => {
    const vista = new Compras({} as never,{} as never,{} as never,{} as never,{} as never);
    vista.compraSeleccionada = {...vista.compra, detalles:[linea as never]};
    vista.modificar();
    expect(vista.compraDetalleLista.length).toBe(1);
    vista.compraDetalleLista[0].comDetCan=5;
    expect(vista.compraSeleccionada.detalles![0].comDetCan).toBe(2);
  });
  it('envía líneas tanto en alta como modificación y normaliza fechas vacías', () => {
    const cuerpos: any[]=[];
    const http={post:(_url:string,body:unknown)=>{cuerpos.push(body);return of(body);},put:(_url:string,body:unknown)=>{cuerpos.push(body);return of(body);}};
    const api=new CompraService(http as never);
    const compra={comId:7,comFecPag:'',detalles:[linea]} as Compra;
    api.guardar(compra).subscribe();
    api.actualizar(compra).subscribe();
    for(const cuerpo of cuerpos){
      expect(cuerpo.detalles[0].comDetCan).toBe(2);
      expect(cuerpo.detalles[0].comDetImp).toBeUndefined();
      expect(cuerpo.comFecPag).toBeNull();
    }
  });
});
