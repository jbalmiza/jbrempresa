import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ContextoSesionService {
  private readonly empleadoAgendaSubject = new BehaviorSubject<number>(
    Number(localStorage.getItem('contexto.empleadoAgendaId')) || 0
  );
  readonly empleadoAgendaId$ = this.empleadoAgendaSubject.asObservable();
  get token(): string { return localStorage.getItem('token') || ''; }
  get empresaId(): number { return Number(localStorage.getItem('empresaId')) || 0; }
  get empresaNombre(): string { return localStorage.getItem('empresa') || ''; }
  get usuarioId(): number { return Number(localStorage.getItem('usuarioId')) || 0; }
  get usuarioNombre(): string { return localStorage.getItem('usuario') || ''; }
  get perfilId(): number { return Number(localStorage.getItem('perfilId')) || 0; }
  get perfilNombre(): string { return localStorage.getItem('perfil') || ''; }
  get empleadoAgendaId(): number { return this.empleadoAgendaSubject.value; }
  seleccionarEmpleadoAgenda(id: number): void {
    const empleadoId = Number(id) || 0;
    if (empleadoId) localStorage.setItem('contexto.empleadoAgendaId', String(empleadoId));
    else localStorage.removeItem('contexto.empleadoAgendaId');
    this.empleadoAgendaSubject.next(empleadoId);
  }
  cerrarSesion(): void {
    const usuarioRecordado = localStorage.getItem('login.usuarioRecordado');
    localStorage.clear();
    if (usuarioRecordado) localStorage.setItem('login.usuarioRecordado', usuarioRecordado);
  }
}
