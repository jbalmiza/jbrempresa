import { Directive, ElementRef, Input, OnChanges, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { BlobUrlUtil } from '../../shared/utils/blob-url.util';

/** Carga imágenes mediante HttpClient para aplicar el contexto y la autenticación comunes. */
@Directive({selector:'img[imagenHttp]',standalone:true})
export class ImagenHttp implements OnChanges, OnDestroy {
  @Input() imagenHttp = '';
  private peticion?: Subscription;
  private blobs = new BlobUrlUtil();
  constructor(private http:HttpClient,private elemento:ElementRef<HTMLImageElement>) {}
  ngOnChanges() {
    this.peticion?.unsubscribe(); this.blobs.liberarTodas();
    this.elemento.nativeElement.removeAttribute('src');
    if (!this.imagenHttp) return;
    this.peticion=this.http.get(this.imagenHttp,{responseType:'blob'}).subscribe({
      next:blob=>this.elemento.nativeElement.src=this.blobs.crear(blob),
      error:()=>this.elemento.nativeElement.removeAttribute('src')
    });
  }
  ngOnDestroy(){this.peticion?.unsubscribe();this.blobs.liberarTodas();}
}
