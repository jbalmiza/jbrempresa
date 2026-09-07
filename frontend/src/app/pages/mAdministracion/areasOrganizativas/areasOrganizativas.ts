import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ArbolRegistros, NodoArbolRegistro } from '../../../components/arbolRegistros/arbolRegistros';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { ContextoSesionService } from '../../../core/session/contexto-sesion.service';
import { AreaOrganizativa } from '../../../interfaces/area-organizativa.interface';
import { AreaOrganizativaService } from '../../../services/area-organizativa.service';

@Component({
  selector: 'AreasOrganizativas',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, ArbolRegistros, DatosIdentificacion, DatosMovimiento,BarraAcciones],
  templateUrl: './areasOrganizativas.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})
export class AreasOrganizativas {
  vistaActiva: 'arbol' | 'tabla' | 'registro' = 'arbol';
  private vistaRetorno: 'arbol' | 'tabla' = 'arbol';
  modo: 'insertar' | 'modificar' = 'insertar';
  datos: AreaOrganizativa[] = [];
  areas: AreaOrganizativa[] = [];
  nodosArbol: NodoArbolRegistro[] = [];
  seleccionada: AreaOrganizativa | null = null;
  nodoSeleccionadoId: string | number | null = null;
  area: AreaOrganizativa;

  columnas = ['empId', 'areId', 'areCod', 'areNom', 'areIdPad', 'areUsuMov', 'areFecMov', 'areAct'];
  titulosColumnas: any = {
    empId: 'Empresa', areId: 'Id Área', areCod: 'Código', areNom: 'Nombre',
    areIdPad: 'Área Superior', areUsuMov: 'Usuario',
    areFecMov: 'Fecha', areAct: 'Activo'
  };

  constructor(
    private service: AreaOrganizativaService,
    private sesion: ContextoSesionService,
    private router: Router
  ) {
    this.area = this.vacia();
    this.cargar('arbol');
  }

  arbol(): void { this.cargar('arbol'); }
  consultar(): void { this.cargar('tabla'); }

  seleccionarNodo(nodo: NodoArbolRegistro): void {
    this.nodoSeleccionadoId = nodo.id;
    this.seleccionada = nodo.registro as AreaOrganizativa;
  }

  seleccionarFila(area: AreaOrganizativa): void {
    this.seleccionada = area;
    this.nodoSeleccionadoId = `area:${area.areId}`;
  }

  insertar(): void {
    this.vistaRetorno = this.vistaActiva === 'tabla' ? 'tabla' : 'arbol';
    this.modo = 'insertar';
    this.area = this.vacia();
    if (this.seleccionada?.areAct) this.area.areIdPad = this.seleccionada.areId;
    this.vistaActiva = 'registro';
  }

  modificar(): void {
    if (!this.seleccionada) return;
    this.vistaRetorno = this.vistaActiva === 'tabla' ? 'tabla' : 'arbol';
    this.modo = 'modificar';
    this.area = { ...this.seleccionada };
    this.vistaActiva = 'registro';
  }

  guardar(): void {
    if (!this.area.areCod.trim() || !this.area.areNom.trim()) {
      avisarAplicacion('Código y nombre son obligatorios.');
      return;
    }
    const operacion = this.modo === 'insertar'
      ? this.service.guardar(this.area)
      : this.service.actualizar(this.area);
    operacion.subscribe({
      next: () => {
        avisarAplicacion('Área guardada correctamente.');
        this.cargar(this.vistaRetorno);
      },
      error: error => this.mostrarError(error)
    });
  }

  async eliminar(): Promise<void> {
    if (!this.seleccionada || !await confirmarAplicacion('¿Desea eliminar el área seleccionada?',true)) return;
    this.service.eliminar(this.seleccionada.areId).subscribe({
      next: () => this.cargar(this.vistaActiva === 'tabla' ? 'tabla' : 'arbol'),
      error: error => this.mostrarError(error)
    });
  }

  cancelar(): void { this.cargar(this.vistaRetorno); }
  volver(): void { this.router.navigate(['/administracion']); }

  private cargar(vista: 'arbol' | 'tabla'): void {
    this.vistaActiva = vista;
    this.seleccionada = null;
    this.nodoSeleccionadoId = null;
    this.service.consultar().subscribe({
      next: datos => {
        this.datos = datos;
        this.areas = datos.filter(area => area.areAct);
        this.nodosArbol = this.areas.map(area => ({
          id: `area:${area.areId}`,
          padreId: area.areIdPad ? `area:${area.areIdPad}` : null,
          etiqueta: area.areNom,
          detalle: area.areCod,
          tipo: 'rama',
          icono: '▦',
          registro: area
        }));
      },
      error: error => this.mostrarError(error)
    });
  }

  private vacia(): AreaOrganizativa {
    return {
      areId: 0, empId: this.sesion.empresaId, areCod: '', areNom: '',
      areIdPad: null, areUsuMov: this.sesion.usuarioNombre, areFecMov: '', areAct: true
    };
  }

  private mostrarError(error: any): void {
    console.error(error);
    avisarAplicacion(error?.error?.detail || error?.error?.message || 'No se pudo completar la operación.');
  }
}
