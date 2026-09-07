import {CommonModule} from '@angular/common';import {Component,EventEmitter,Input,Output} from '@angular/core';
@Component({selector:'teclado-tactil',standalone:true,imports:[CommonModule],templateUrl:'./tecladoTactil.html',styleUrl:'./tecladoTactil.css'}) export class TecladoTactil{
 @Input() campo:HTMLInputElement|HTMLTextAreaElement|null=null;@Output() cerrar=new EventEmitter<void>();mayus=false;
 readonly filas=[['1','2','3','4','5','6','7','8','9','0'],['Q','W','E','R','T','Y','U','I','O','P'],['A','S','D','F','G','H','J','K','L','Ñ'],['Z','X','C','V','B','N','M',',','.','-']];
 escribir(tecla:string){if(!this.campo)return;const valor=this.mayus?tecla:tecla.toLocaleLowerCase('es-ES');this.insertar(valor)}
 espacio(){this.insertar(' ')}enter(){if(this.campo instanceof HTMLTextAreaElement)this.insertar('\n');else this.cerrar.emit()}
 borrar(){const c=this.campo;if(!c)return;const inicio=c.selectionStart??c.value.length,fin=c.selectionEnd??inicio;if(inicio!==fin)c.setRangeText('',inicio,fin,'end');else if(inicio>0)c.setRangeText('',inicio-1,inicio,'end');this.actualizar()}
 private insertar(texto:string){const c=this.campo;if(!c)return;const inicio=c.selectionStart??c.value.length,fin=c.selectionEnd??inicio;if(c.maxLength>0&&c.value.length-(fin-inicio)+texto.length>c.maxLength)return;c.setRangeText(texto,inicio,fin,'end');this.actualizar()}
 private actualizar(){this.campo?.dispatchEvent(new Event('input',{bubbles:true}));this.campo?.focus()}
}
