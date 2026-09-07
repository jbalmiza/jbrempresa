import {avisarAplicacion,confirmarAplicacion} from '../../core/interaccion/dialogos.service';
import { Component, Input, OnChanges, OnDestroy, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Adjunto, TipoAdjunto } from '../../interfaces/adjunto.interface';
import { AdjuntoService } from '../../services/adjunto.service';
import { BlobUrlUtil } from '../../shared/utils/blob-url.util';

@Component({
  selector: 'documentacionAdjunta',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './documentacionAdjunta.html',
  styleUrl: './documentacionAdjunta.css'
})
export class DocumentacionAdjunta implements OnChanges, OnDestroy {
  private readonly blobs=new BlobUrlUtil();
  @Input() modulo = '';
  @Input() tipoRegistro = '';
  @Input() registroId = 0;
  @Input() codigoRuta = '';
  @Input() desactivado = false;
  @Input() permitirImagenPrincipal = false;

  adjuntos: Adjunto[] = [];
  nombre = '';
  tipo: TipoAdjunto = 'ORIGINAL';
  archivo: File | null = null;
  cargando = false;
  mensajeError = '';
  readonly formatos = '.pdf,.doc,.docx,.xls,.xlsx,.jpg,.jpeg,.png';
  readonly tamanioMaximo = 10 * 1024 * 1024;
  vistasImagen = new Map<number, string>();

  constructor(private readonly adjuntoService: AdjuntoService) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes['registroId'] || changes['desactivado']) {
      if (!this.desactivado && this.registroId > 0) this.consultar();
      else this.adjuntos = [];
    }
  }

  consultar() {
    this.cargando = true;
    this.mensajeError = '';
    this.adjuntoService.consultar(this.modulo, this.tipoRegistro, this.registroId).subscribe({
      next: adjuntos => { this.adjuntos = adjuntos; this.cargando = false; this.cargarVistasImagen(); },
      error: error => { this.cargando = false; this.mostrarError(error, 'No se pudo consultar la documentación.'); }
    });
  }

  seleccionarArchivo(evento: Event) {
    const input = evento.target as HTMLInputElement;
    const archivo = input.files?.[0] || null;
    this.mensajeError = '';
    if (archivo && archivo.size > this.tamanioMaximo) {
      this.archivo = null;
      input.value = '';
      this.mensajeError = 'El archivo supera el máximo permitido de 10 MB.';
      return;
    }
    this.archivo = archivo;
  }

  incorporar(inputArchivo: HTMLInputElement) {
    if (!this.nombre.trim() || !this.archivo) {
      this.mensajeError = 'Debe indicar un nombre y seleccionar un archivo.';
      return;
    }
    this.cargando = true;
    this.mensajeError = '';
    this.adjuntoService.guardar({
      modulo: this.modulo,
      tipoRegistro: this.tipoRegistro,
      registroId: this.registroId,
      codigoRuta: this.codigoRuta,
      nombre: this.nombre.trim(),
      tipo: this.tipo,
      archivo: this.archivo
    }).subscribe({
      next: () => {
        this.nombre = '';
        this.tipo = 'ORIGINAL';
        this.archivo = null;
        inputArchivo.value = '';
        this.consultar();
      },
      error: error => { this.cargando = false; this.mostrarError(error, 'No se pudo incorporar el archivo.'); }
    });
  }

  visualizar(adjunto: Adjunto) { this.abrirContenido(adjunto, false); }

  descargar(adjunto: Adjunto) { this.abrirContenido(adjunto, true); }

  async eliminar(adjunto: Adjunto) {
    if (!await confirmarAplicacion(`¿Desea eliminar el adjunto "${adjunto.adjNom}"?`,true)) return;
    this.cargando = true;
    this.adjuntoService.eliminar(adjunto.adjId, this.codigoRuta).subscribe({
      next: () => this.consultar(),
      error: error => { this.cargando = false; this.mostrarError(error, 'No se pudo eliminar el adjunto.'); }
    });
  }

  marcarPrincipal(adjunto: Adjunto) {
    if (adjunto.adjPri || !this.esImagen(adjunto)) return;
    this.cargando = true;
    this.mensajeError = '';
    this.adjuntoService.marcarPrincipal(adjunto.adjId, this.codigoRuta).subscribe({
      next: () => this.consultar(),
      error: error => { this.cargando = false; this.mostrarError(error, 'No se pudo establecer la imagen principal.'); }
    });
  }

  esImagen(adjunto: Adjunto): boolean { return adjunto.adjMime?.startsWith('image/'); }

  ngOnDestroy() { this.blobs.liberarTodas();this.vistasImagen.clear(); }

  formatearTamanio(bytes: number): string {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / 1024 / 1024).toFixed(1)} MB`;
  }

  private abrirContenido(adjunto: Adjunto, descargar: boolean) {
    this.adjuntoService.contenido(adjunto.adjId, this.codigoRuta, descargar).subscribe({
      next: contenido => {
        if (descargar) {
          this.blobs.descargar(contenido,adjunto.adjNomArc);
        } else {
          const url=this.blobs.crear(contenido);
          window.open(url, '_blank', 'noopener');
          setTimeout(() => this.blobs.liberar(url), 60000);
        }
      },
      error: error => this.mostrarError(error, 'No se pudo abrir el archivo.')
    });
  }

  private mostrarError(error: any, mensaje: string) {
    console.error(error);
    this.mensajeError = error?.error?.detail || error?.error?.message || mensaje;
  }

  private cargarVistasImagen() {
    this.liberarVistas();
    if (!this.permitirImagenPrincipal) return;
    this.adjuntos.filter(adjunto => this.esImagen(adjunto)).forEach(adjunto => {
      this.adjuntoService.contenido(adjunto.adjId, this.codigoRuta, false).subscribe({
        next: contenido => this.vistasImagen.set(adjunto.adjId, this.blobs.crear(contenido))
      });
    });
  }

  private liberarVistas() {
    this.vistasImagen.forEach(url => this.blobs.liberar(url));
    this.vistasImagen.clear();
  }
}
