import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Sidebar } from '../../components/sidebar/sidebar';
import { Supbar } from '../../components/supbar/supbar';

@Component({
  selector: 'ModuloVacio',
  standalone: true,
  imports: [Sidebar, Supbar],
  templateUrl: './moduloVacio.html',
  styleUrl: '../../styles/estiloPrincipal.css'
})
export class ModuloVacio {
  readonly modulo: string;
  readonly titulo: string;

  constructor(route: ActivatedRoute) {
    this.modulo = route.snapshot.data['modulo'] as string;
    this.titulo = route.snapshot.data['titulo'] as string;
  }
}
