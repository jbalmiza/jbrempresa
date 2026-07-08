import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Toolbar } from '../../elementos/toolbar.elemento';

@Component({
  selector: 'toolbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toolbar.html',
  styleUrls: ['./toolbar.css']
})
export class Toolbar {

  /** Barra de herramientas a representar */
  @Input() toolbar!: Toolbar;

}