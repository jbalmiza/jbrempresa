import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface NodoArbolRegistro {
  id: string | number;
  padreId: string | number | null;
  etiqueta: string;
  detalle?: string;
  tipo?: 'rama' | 'hoja';
  icono?: string;
  registro?: any;
  deshabilitado?: boolean;
}

interface NodoVisible extends NodoArbolRegistro {
  nivel: number;
  tieneHijos: boolean;
}

@Component({
  selector: 'arbolRegistros',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './arbolRegistros.html',
  styleUrl: './arbolRegistros.css'
})
export class ArbolRegistros {
  private nodosInternos: NodoArbolRegistro[] = [];

  @Input()
  set nodos(valor: NodoArbolRegistro[]) {
    this.nodosInternos = valor || [];
    const idsValidos = new Set(this.nodosInternos.map(nodo => this.clave(nodo.id)));
    this.expandidos = new Set([...this.expandidos].filter(id => idsValidos.has(id)));
    if (this.expandidoInicial && !this.expansionInicialAplicada && this.nodosInternos.length) {
      this.nodosInternos.forEach(nodo => {
        if (this.nodosInternos.some(candidato => this.iguales(candidato.padreId, nodo.id))) {
          this.expandidos.add(this.clave(nodo.id));
        }
      });
      this.expansionInicialAplicada = true;
    }
    this.reconstruir();
  }
  get nodos(): NodoArbolRegistro[] { return this.nodosInternos; }
  @Input() idSeleccionado: string | number | null = null;
  @Input() mensajeVacio = 'No hay registros para mostrar.';
  @Input() expandidoInicial = true;
  @Output() nodoSeleccionado = new EventEmitter<NodoArbolRegistro>();

  nodosVisibles: NodoVisible[] = [];
  private expandidos = new Set<string>();
  private expansionInicialAplicada = false;

  seleccionar(nodo: NodoArbolRegistro): void {
    if (!nodo.deshabilitado) this.nodoSeleccionado.emit(nodo);
  }

  alternar(nodo: NodoVisible, evento: Event): void {
    evento.stopPropagation();
    const clave = this.clave(nodo.id);
    this.expandidos.has(clave) ? this.expandidos.delete(clave) : this.expandidos.add(clave);
    this.reconstruir();
  }

  expandido(id: string | number): boolean { return this.expandidos.has(this.clave(id)); }
  seleccionado(id: string | number): boolean { return this.iguales(id, this.idSeleccionado); }
  identificador(_: number, nodo: NodoVisible): string { return String(nodo.id); }

  expandirTodo(): void {
    this.nodos.forEach(nodo => this.expandidos.add(this.clave(nodo.id)));
    this.reconstruir();
  }

  contraerTodo(): void {
    this.expandidos.clear();
    this.reconstruir();
  }

  private reconstruir(): void {
    const hijos = new Map<string, NodoArbolRegistro[]>();
    const ids = new Set(this.nodos.map(nodo => this.clave(nodo.id)));
    const raices: NodoArbolRegistro[] = [];

    for (const nodo of this.nodos) {
      if (nodo.padreId === null || nodo.padreId === undefined || !ids.has(this.clave(nodo.padreId))) {
        raices.push(nodo);
      } else {
        const padre = this.clave(nodo.padreId);
        hijos.set(padre, [...(hijos.get(padre) || []), nodo]);
      }
    }

    const visibles: NodoVisible[] = [];
    const recorrer = (nodo: NodoArbolRegistro, nivel: number, ruta: Set<string>) => {
      const clave = this.clave(nodo.id);
      if (ruta.has(clave)) return;
      const descendientes = hijos.get(clave) || [];
      visibles.push({ ...nodo, nivel, tieneHijos: descendientes.length > 0 });
      if (this.expandidos.has(clave)) {
        const nuevaRuta = new Set(ruta).add(clave);
        descendientes.forEach(hijo => recorrer(hijo, nivel + 1, nuevaRuta));
      }
    };
    raices.forEach(raiz => recorrer(raiz, 0, new Set()));
    this.nodosVisibles = visibles;
  }

  private clave(valor: string | number | null): string { return String(valor); }
  private iguales(a: string | number | null, b: string | number | null): boolean {
    return a !== null && b !== null && this.clave(a) === this.clave(b);
  }
}
