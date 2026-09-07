/** Gestiona URLs temporales de Blob y garantiza su liberación. */
export class BlobUrlUtil {
  private readonly urls = new Set<string>();
  crear(blob: Blob): string { const url = URL.createObjectURL(blob); this.urls.add(url); return url; }
  liberar(url: string | null | undefined): void { if (!url || !this.urls.delete(url)) return; URL.revokeObjectURL(url); }
  liberarTodas(): void { this.urls.forEach((url) => URL.revokeObjectURL(url)); this.urls.clear(); }
  descargar(blob: Blob, nombre: string): void {
    const url = this.crear(blob);
    const enlace = document.createElement('a');
    enlace.href = url;
    enlace.download = nombre;
    enlace.click();
    this.liberar(url);
  }
}
