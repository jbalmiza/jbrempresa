import { AgendaRegistros } from './agendaRegistros';

describe('AgendaRegistros', () => {
  it('respeta exactamente la hora inicial de visualización en modo múltiple', () => {
    const componente = new AgendaRegistros({} as never);
    componente.zoomMinutos = 30;
    componente.fechaActual = new Date(2026, 8, 14);
    componente.agendasMultiples = [{
      recurso: { ragId: 1, ragTip: 'EMPLEADO', ragRefId: 1, ragNom: 'Empleado', ragCap: 1, ragMarPre: 0, ragMarPos: 0, ragHorVis: '19:15:00', ragIntVis: 10 },
      horarios: [{ horDia: 1, horIni: '09:00:00', horFin: '23:00:00' }],
      excepciones: [],
      configurada: true
    }];

    componente.ngOnChanges({ agendasMultiples: { currentValue: componente.agendasMultiples, previousValue: [], firstChange: true, isFirstChange: () => true } });
    expect(componente.zoomMinutos).toBe(10);
    componente.seleccionarVista('semana');
    expect(componente.vista).toBe('semana');
    expect(componente.zoomMinutos).toBe(10);
    componente.zoomMinutos = 15;
    expect(componente.vista).toBe('semana');
    componente.seleccionarVista('dia');
    expect(componente.vista).toBe('dia');
    expect(componente.zoomMinutos).toBe(15);

    expect(componente.tramosMultiple[0].etiqueta).toBe('19:15');
    componente.zoomMinutos = 10;
    expect(componente.tramosMultiple[1].etiqueta).toBe('19:25');
  });
});
