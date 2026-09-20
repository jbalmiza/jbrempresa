import { ChatInterno } from './chatInterno';
import { of } from 'rxjs';

describe('Permisos de mensajería por contexto', () => {
  afterEach(() => localStorage.clear());

  for (const perfil of ['ADMINISTRADOR', 'JEFE', 'EMPLEADO']) {
    it(`la cabecera no permite administrar conversaciones para ${perfil}`, async () => {
      localStorage.setItem('perfil', perfil);
      const chat = new ChatInterno({} as never, {} as never, {} as never);
      expect(chat.puedeConsultarBajas).toBe(false);
      expect(chat.puedeEliminar).toBe(false);
      expect(chat.puedeGestionar).toBe(false);
      chat.verBajas();
      await chat.darBaja();
      await chat.purgarHasta();
      await chat.reactivar({ id: 1 } as never, new Event('click'));
      await chat.eliminarDefinitivamente({ id: 1 } as never, new Event('click'));
      expect(chat.vista).toBe('LISTA');
    });
  }

  it('separa eliminación en Registro y reactivación en Gestión', () => {
    localStorage.setItem('perfil', 'ADMINISTRADOR');
    const chat = new ChatInterno({} as never, {} as never, {} as never);
    chat.modo = 'REGISTRO';
    expect(chat.puedeConsultarBajas).toBe(true);
    expect(chat.puedeEliminar).toBe(true);
    expect(chat.puedeGestionar).toBe(false);
    chat.modo = 'GESTION';
    expect(chat.puedeConsultarBajas).toBe(true);
    expect(chat.puedeEliminar).toBe(false);
    expect(chat.puedeGestionar).toBe(true);
  });

  it('deniega acciones administrativas a otros perfiles incluso fuera de la cabecera', () => {
    localStorage.setItem('perfil', 'JEFE');
    const chat = new ChatInterno({} as never, {} as never, {} as never);
    for (const modo of ['REGISTRO', 'GESTION'] as const) {
      chat.modo = modo;
      expect(chat.puedeConsultarBajas).toBe(false);
      expect(chat.puedeEliminar).toBe(false);
      expect(chat.puedeGestionar).toBe(false);
    }
  });

  it('marca un aviso informativo como leído sin abrir una conversación', () => {
    let marcado=0;
    const avisosApi={marcarLeido:()=>{marcado++;return of(undefined)}};
    const chat=new ChatInterno({} as never,{} as never,avisosApi as never);
    const aviso={empId:1,avisoId:2,historicoId:1,tipo:'AVISO',titulo:'Aviso',mensaje:'Contenido',emisor:'JEFE',empresaEmisora:'Empresa',fecha:'2026-09-17T10:00:00',leido:false} as const;
    chat.avisos=[{...aviso}];
    chat.leerAviso(chat.avisos[0]);
    expect(marcado).toBe(1);
    expect(chat.avisos[0].leido).toBe(true);
    expect(chat.vista).toBe('LISTA');
  });
});
