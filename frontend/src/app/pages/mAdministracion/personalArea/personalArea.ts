import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { ArbolRegistros, NodoArbolRegistro } from '../../../components/arbolRegistros/arbolRegistros';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { DatosPersonaRelacion } from '../../../components/datosPersonaRelacion/datosPersonaRelacion';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { ContextoSesionService } from '../../../core/session/contexto-sesion.service';
import { AreaOrganizativa } from '../../../interfaces/area-organizativa.interface';
import { Persona } from '../../../interfaces/persona.interface';
import { PersonalArea } from '../../../interfaces/personal-area.interface';
import { AreaOrganizativaService } from '../../../services/area-organizativa.service';
import { PersonaService } from '../../../services/persona.service';
import { PersonalAreaService } from '../../../services/personal-area.service';

@Component({
  selector: 'PersonalArea',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, ArbolRegistros, DatosPersonaRelacion, DatosIdentificacion, DatosMovimiento,BarraAcciones],
  templateUrl: './personalArea.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})
export class PersonalAreaPage {
  vistaActiva: 'arbol' | 'tabla' | 'registro' = 'arbol';
  private vistaRetorno: 'arbol' | 'tabla' = 'arbol';
  modo: 'insertar' | 'modificar' = 'insertar';
  datos: PersonalArea[] = [];
  areas: AreaOrganizativa[] = [];
  personas: Persona[] = [];
  nodosArbol: NodoArbolRegistro[] = [];
  seleccionada: PersonalArea | null = null;
  areaSeleccionada: AreaOrganizativa | null = null;
  nodoSeleccionadoId: string | number | null = null;
  asignacion: PersonalArea;

  columnas = ['empId', 'peaId', 'areId', 'personaNomCom', 'peaCar', 'peaRes', 'peaPri', 'peaFecDes', 'peaFecHas', 'peaUsuMov', 'peaFecMov', 'peaAct'];
  titulosColumnas: any = {
    empId: 'Empresa', peaId: 'Id Asignación', areId: 'Área', personaNomCom: 'Persona',
    peaCar: 'Cargo', peaRes: 'Responsable', peaPri: 'Principal', peaFecDes: 'Desde',
    peaFecHas: 'Hasta', peaUsuMov: 'Usuario', peaFecMov: 'Fecha', peaAct: 'Activo'
  };

  constructor(
    private service: PersonalAreaService,
    private areaService: AreaOrganizativaService,
    private personaService: PersonaService,
    private sesion: ContextoSesionService,
    private router: Router
  ) {
    this.asignacion = this.vacia();
    this.cargar('arbol');
  }

  arbol(): void { this.cargar('arbol'); }
  consultar(): void { this.cargar('tabla'); }

  seleccionarNodo(nodo: NodoArbolRegistro): void {
    this.nodoSeleccionadoId = nodo.id;
    if (nodo.tipo === 'hoja') {
      this.seleccionada = nodo.registro as PersonalArea;
      this.areaSeleccionada = this.areas.find(area => area.areId === this.seleccionada?.areId) || null;
    } else {
      this.areaSeleccionada = nodo.registro as AreaOrganizativa;
      this.seleccionada = null;
    }
  }

  seleccionarFila(asignacion: PersonalArea): void {
    this.seleccionada = asignacion;
    this.areaSeleccionada = this.areas.find(area => area.areId === asignacion.areId) || null;
    this.nodoSeleccionadoId = `personal:${asignacion.peaId}`;
  }

  insertar(): void {
    this.vistaRetorno = this.vistaActiva === 'tabla' ? 'tabla' : 'arbol';
    this.modo = 'insertar';
    this.asignacion = this.vacia();
    if (this.areaSeleccionada) this.asignacion.areId = this.areaSeleccionada.areId;
    this.vistaActiva = 'registro';
  }

  modificar(): void {
    if (!this.seleccionada) return;
    this.vistaRetorno = this.vistaActiva === 'tabla' ? 'tabla' : 'arbol';
    this.modo = 'modificar';
    this.asignacion = { ...this.seleccionada };
    this.vistaActiva = 'registro';
  }

  guardar(): void {
    if (!this.asignacion.areId || !this.asignacion.perId) {
      avisarAplicacion('Área y persona son obligatorias.');
      return;
    }
    const operacion = this.modo === 'insertar'
      ? this.service.guardar(this.asignacion)
      : this.service.actualizar(this.asignacion);
    operacion.subscribe({
      next: () => {
        avisarAplicacion('Asignación guardada correctamente.');
        this.cargar(this.vistaRetorno);
      },
      error: error => this.mostrarError(error)
    });
  }

  async eliminar(): Promise<void> {
    if (!this.seleccionada || !await confirmarAplicacion('¿Desea eliminar la asignación seleccionada?',true)) return;
    this.service.eliminar(this.seleccionada.peaId).subscribe({
      next: () => this.cargar(this.vistaActiva === 'tabla' ? 'tabla' : 'arbol'),
      error: error => this.mostrarError(error)
    });
  }

  cancelar(): void { this.cargar(this.vistaRetorno); }
  volver(): void { this.router.navigate(['/administracion']); }
  nombreArea(id: number): string | number { return this.areas.find(area => area.areId === id)?.areNom || id; }
  nombrePersona(id: number): string | number { return this.personas.find(persona => persona.perId === id)?.perNomCom || id; }

  private cargar(vista: 'arbol' | 'tabla'): void {
    this.vistaActiva = vista;
    this.seleccionada = null;
    this.areaSeleccionada = null;
    this.nodoSeleccionadoId = null;
    forkJoin({
      asignaciones: this.service.consultar(),
      areas: this.areaService.consultar(),
      personas: this.personaService.obtenerPersonas()
    }).subscribe({
      next: ({ asignaciones, areas, personas }) => {
        this.datos = asignaciones.map(asignacion => ({...asignacion, personaNomCom: personas.find(persona => persona.perId === asignacion.perId)?.perNomCom || ''}));
        this.areas = areas.filter(area => area.areAct);
        this.personas = personas.filter(persona => persona.perTipMov !== 'B');
        this.construirArbol();
      },
      error: error => this.mostrarError(error)
    });
  }

  private construirArbol(): void {
    const nodosArea: NodoArbolRegistro[] = this.areas.map(area => ({
      id: `area:${area.areId}`,
      padreId: area.areIdPad ? `area:${area.areIdPad}` : null,
      etiqueta: area.areNom,
      detalle: area.areCod,
      tipo: 'rama',
      icono: '▦',
      registro: area
    }));
    const nodosPersonal: NodoArbolRegistro[] = this.datos
      .filter(asignacion => asignacion.peaAct)
      .map(asignacion => {
        const persona = this.personas.find(item => item.perId === asignacion.perId);
        const distintivos = [
          asignacion.peaCar,
          asignacion.peaRes ? 'Responsable' : '',
          asignacion.peaPri ? 'Área principal' : ''
        ].filter(Boolean).join(' · ');
        return {
          id: `personal:${asignacion.peaId}`,
          padreId: `area:${asignacion.areId}`,
          etiqueta: persona?.perNomCom || `Persona ${asignacion.perId}`,
          detalle: distintivos || 'Personal asignado',
          tipo: 'hoja',
          icono: '●',
          registro: asignacion
        };
      });
    this.nodosArbol = [...nodosArea, ...nodosPersonal];
  }

  private vacia(): PersonalArea {
    return {
      peaId: 0, empId: this.sesion.empresaId, areId: 0, perId: 0, peaCar: '',
      peaRes: false, peaPri: false, peaFecDes: '', peaFecHas: '',
      peaUsuMov: this.sesion.usuarioNombre, peaFecMov: '', peaAct: true
    };
  }

  private mostrarError(error: any): void {
    console.error(error);
    avisarAplicacion(error?.error?.detail || error?.error?.message || 'No se pudo completar la operación.');
  }
}
