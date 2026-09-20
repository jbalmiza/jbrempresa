import { ElementRef } from '@angular/core';
import { of } from 'rxjs';
import { AvisosVentana } from './avisosVentana';

describe('AvisosVentana', () => {
  it('mantiene en Mi agenda los avisos de la entrada de Empleados y los propios de la agenda', () => {
    const rutas: string[] = [];
    const api = { ventana: (ruta: string) => {
      rutas.push(ruta);
      return of([{ empId: 1, avisoId: rutas.length, historicoId: 1 }]);
    } };
    const componente = new AvisosVentana(
      { url: '/empleados/agenda' } as never,
      api as never,
      new ElementRef(document.createElement('div'))
    );
    componente.integrado = true;
    componente.ngOnInit();

    expect(rutas).toEqual(['/empleados', '/empleados/agenda']);
    expect(componente.avisos).toHaveLength(2);
  });
});
