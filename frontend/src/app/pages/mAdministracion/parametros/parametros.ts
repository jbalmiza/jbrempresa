import {BarraAcciones} from '../../../directives/barraAcciones/barraAcciones';
import {avisarAplicacion,confirmarAplicacion} from '../../../core/interaccion/dialogos.service';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';
import { Tabla } from '../../../components/tabla/tabla';
import { DatosIdentificacion } from '../../../components/datosIdentificacion/datosIdentificacion';
import { DatosMovimiento } from '../../../components/datosMovimiento/datosMovimiento';
import { Parametro } from '../../../interfaces/parametro.interface';
import { ParametroService } from '../../../services/parametro.service';
import { FechasUtil } from '../../../shared/utils/fechas.util';

@Component({
  selector: 'Parametros',
  standalone: true,
  imports:[CommonModule, FormsModule, Sidebar, Supbar, Tabla, DatosIdentificacion, DatosMovimiento,BarraAcciones],
  templateUrl: './parametros.html',
  styleUrl: '../../../styles/estiloGeneral.css'
})
export class Parametros {
  vistaActiva: 'registro' | 'tabla' = 'tabla';
  modoFormulario: 'insertar' | 'modificar' = 'insertar';
  mostrarObligatorios = false;
  parametro: Parametro = this.crearParametroVacio();
  parametroSeleccionado: Parametro | null = null;
  datos: Parametro[] = [];
  readonly modulos = ['ADMINISTRACION', 'TERRITORIO', 'PERSONAS', 'PRODUCTOS', 'SERVICIOS', 'VENTAS', 'COMPRAS', 'COMUNICACIONES', 'RECURSOS'];
  moduloContexto = '';
  rutaVolver = '';
  columnas = ['empId', 'parId', 'parMod', 'parCod', 'parDes', 'parVal', 'parUsuMov', 'parFecMov', 'parAct'];
  titulosColumnas = {
    empId: 'Empresa',
    parMod: 'Módulo',
    parId: 'Id Parámetro',
    parCod: 'Código',
    parDes: 'Descripción',
    parVal: 'Valor'
    ,parUsuMov: 'Usuario Mod.'
    ,parFecMov: 'Fecha Mod.'
    ,parAct: 'Activo'
  };

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly parametroService: ParametroService
  ) {
    this.moduloContexto = this.route.snapshot.data['moduloParametros'] || '';
    this.rutaVolver = this.route.snapshot.data['rutaVolver'] || '';
    this.parametro = this.crearParametroVacio();
  }

  consultar() {
    this.vistaActiva = 'tabla';
    this.parametroService.consultar(this.moduloContexto || undefined).subscribe({
      next: respuesta => { this.datos = respuesta; this.parametroSeleccionado = null; },
      error: error => this.mostrarError(error, 'Error al obtener parámetros.')
    });
  }

  insertar() {
    this.vistaActiva = 'registro';
    this.modoFormulario = 'insertar';
    this.mostrarObligatorios = false;
    this.parametro = this.crearParametroVacio();
  }

  modificar() {
    if (this.vistaActiva === 'registro') { this.vistaActiva = 'tabla'; return; }
    if (!this.parametroSeleccionado) { avisarAplicacion('Debe seleccionar un registro.'); return; }
    this.modoFormulario = 'modificar';
    this.mostrarObligatorios = false;
    this.parametro = { ...this.parametroSeleccionado };
    this.vistaActiva = 'registro';
  }

  guardar() {
    this.mostrarObligatorios = true;
    if (!this.esValido()) return;
    this.parametroService.guardar(this.parametro).subscribe({
      next: () => { avisarAplicacion('Parámetro guardado correctamente.'); this.consultar(); },
      error: error => this.mostrarError(error, 'Error al guardar el parámetro.')
    });
  }

  actualizar() {
    this.mostrarObligatorios = true;
    if (!this.esValido()) return;
    this.parametroService.actualizar(this.parametro).subscribe({
      next: () => { avisarAplicacion('Parámetro actualizado correctamente.'); this.consultar(); },
      error: error => this.mostrarError(error, 'Error al actualizar el parámetro.')
    });
  }

  async eliminar() {
    if (!this.parametroSeleccionado) { avisarAplicacion('Debe seleccionar un registro.'); return; }
    if (!await confirmarAplicacion('¿Desea eliminar el parámetro seleccionado?',true)) return;
    this.parametroService.eliminar(this.parametroSeleccionado.parId).subscribe({
      next: () => { avisarAplicacion('Parámetro eliminado correctamente.'); this.consultar(); },
      error: error => this.mostrarError(error, 'Error al eliminar el parámetro.')
    });
  }

  cancelar() { this.vistaActiva = 'tabla'; this.mostrarObligatorios = false; }
  esParametroSensible() {
    return this.parametro.parMod === 'ADMINISTRACION'
      && this.parametro.parCod?.trim().toUpperCase() === 'SMTP_PASSWORD';
  }
  volver() {
    this.router.navigate([this.rutaVolver || (this.moduloContexto ? `/${this.moduloContexto.toLowerCase()}` : '/administracion')]);
  }
  private esValido() {
    return Boolean(
      this.parametro.parMod?.trim()
      && this.parametro.parCod?.trim()
      && this.parametro.parDes?.trim()
      && (this.esParametroSensible() || this.parametro.parVal?.trim())
    );
  }
  private crearParametroVacio(): Parametro {
    return {
      empId: Number(localStorage.getItem('empresaId')) || 0,
      parId: 0,
      parMod: this.moduloContexto,
      parCod: '',
      parDes: '',
      parVal: ''
      ,parUsuMov: localStorage.getItem('usuario') || ''
      ,parFecMov: FechasUtil.formatearFechaHora()
      ,parAct: true
    };
  }
  private mostrarError(error: any, mensaje: string) {
    console.error(error);
    avisarAplicacion(error?.error?.detail || error?.error?.message || mensaje);
  }
}
