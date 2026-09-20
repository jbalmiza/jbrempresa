import { of, Subject, throwError } from 'rxjs';
import { CatalogoPublico } from './catalogoPublico';

describe('Catálogo compartido de proveedores', () => {
  const datos = {empresa:'Proveedor',productos:[],avisos:[],modalidad:'DOMICILIO'};

  it('al cambiar de proveedor descarta la cesta y las respuestas del anterior', () => {
    const anterior = new Subject<any>();
    const api = {proveedor:(id:number)=>id===4?anterior:of({...datos,empresa:'Otro proveedor'})};
    const vista=new CatalogoPublico({} as never,api as never,{} as never,{} as never);
    vista.proveedorId=4;vista.ngOnChanges();
    vista.cesta=[{producto:{id:1} as never,cantidad:1,observaciones:''}];
    vista.proveedorId=5;vista.ngOnChanges();
    anterior.next(datos);
    expect(vista.catalogo?.empresa).toBe('Otro proveedor');
    expect(vista.cesta.length).toBe(0);
    vista.ngOnDestroy();
  });

  it('envía por el endpoint protegido y presenta la denegación con el diálogo común', () => {
    let destino=0,avisos=0;
    const api={pedirProveedor:(id:number)=>{destino=id;return throwError(()=>({status:403}));}};
    const vista=new CatalogoPublico({} as never,api as never,{error:()=>avisos++} as never,{} as never);
    vista.proveedorId=4;vista.catalogo=datos as never;
    vista.nombre='Empresa compradora';vista.telefono='600000000';vista.direccion='Dirección';
    vista.enviar();
    expect(destino).toBe(4);expect(avisos).toBe(1);expect(vista.confirmando).toBe(false);
  });

  it('quitar un servicio no resta unidades a un producto con el mismo identificador', () => {
    const vista=new CatalogoPublico({} as never,{} as never,{} as never,{} as never);
    const producto={id:1,tipo:'PRODUCTO'} as const;
    const servicio={id:1,tipo:'SERVICIO'} as const;
    vista.agregar(producto as never);vista.agregar(servicio as never);vista.quitar(servicio as never);
    expect(vista.cantidad(1,'PRODUCTO')).toBe(1);expect(vista.cantidad(1,'SERVICIO')).toBe(0);
  });
});
