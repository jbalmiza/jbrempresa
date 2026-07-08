import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Formulario } from '../../metadata/formulario.model';
import { TipoDato } from '../../enums/tipoDato.enum';

@Component({
	
  selector: 'formulario',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './formulario.html',
  styleUrl: '../../../styles/formulario.css'
  
})

export class Formulario {

	/** Definición del formulario a mostrar */
	@Input() formulario!: Formulario;

  	/** Enumeración de tipos de datos utilizada en la vista */
  	TipoDato = TipoDato;

}