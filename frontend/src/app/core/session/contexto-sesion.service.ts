import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ContextoSesionService {
  get token(): string { return localStorage.getItem('token') || ''; }
  get empresaId(): number { return Number(localStorage.getItem('empresaId')) || 0; }
  get empresaNombre(): string { return localStorage.getItem('empresa') || ''; }
  get usuarioId(): number { return Number(localStorage.getItem('usuarioId')) || 0; }
  get usuarioNombre(): string { return localStorage.getItem('usuario') || ''; }
  get perfilId(): number { return Number(localStorage.getItem('perfilId')) || 0; }
  get perfilNombre(): string { return localStorage.getItem('perfil') || ''; }
  cerrarSesion(): void {
    const usuarioRecordado = localStorage.getItem('login.usuarioRecordado');
    localStorage.clear();
    if (usuarioRecordado) localStorage.setItem('login.usuarioRecordado', usuarioRecordado);
  }
}
