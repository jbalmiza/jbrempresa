import { GestionAgendas } from './gestionAgendas';

describe('GestionAgendas', () => {
  it('visualiza el horario solicitado y sus márgenes', () => {
    const componente = new GestionAgendas({} as never, {} as never);
    componente.fecha = '2026-09-10'; // jueves
    componente.intervalo = 30;
    const empleado = {
      recurso: {
        ragId: 9, ragTip: 'EMPLEADO', ragRefId: 1, ragNom: 'Empleado de prueba',
        ragCap: 1, ragMarPre: 30, ragMarPos: 30, ragHorVis: '19:00'
      },
      horarios: [{ horDia: 4, horIni: '19:30', horFin: '23:30' }],
      excepciones: [], configurada: true
    } as any;
    componente.agendas = [empleado];

    expect(componente.tramos[0].etiqueta).toBe('19:00');
    expect(componente.tramos.at(-1)?.etiqueta).toBe('23:00');
    expect(componente.estado(empleado, 19 * 60)).toBe('NO_DISPONIBLE');
    expect(componente.estado(empleado, 19 * 60 + 30)).toBe('PREPARACION');
    expect(componente.estado(empleado, 20 * 60)).toBe('DISPONIBLE');
    expect(componente.estado(empleado, 22 * 60 + 30)).toBe('DISPONIBLE');
    expect(componente.estado(empleado, 23 * 60)).toBe('LIMPIEZA');
    expect(componente.estado(empleado, 23 * 60 + 30)).toBe('NO_DISPONIBLE');
  });
});
