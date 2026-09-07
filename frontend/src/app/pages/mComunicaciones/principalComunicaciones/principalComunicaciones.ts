import { Component } from '@angular/core';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { Supbar } from '../../../components/supbar/supbar';

@Component({
  selector: 'principal-comunicaciones',
  standalone: true,
  imports: [Sidebar, Supbar],
  templateUrl: './principalComunicaciones.html',
  styleUrl: '../../../styles/estiloPrincipal.css'
})
export class PrincipalComunicaciones {}
