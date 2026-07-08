import { Injectable } from '@angular/core';

import { Entidad } from '../metadata/entidad.model';

import { ClienteService } from './cliente.service';
import { UsuarioService } from './usuario.service';
import { PerfilService } from './perfil.service';
import { PersonaService } from './persona.service';
import { DomicilioService } from './domicilio.service';
import { ProductoService } from './producto.service';
import { VentaService } from './venta.service';

@Injectable({
  providedIn: 'root'
})
export class GestionService {

  constructor(
    private clienteService: ClienteService,
    private usuarioService: UsuarioService,
    private perfilService: PerfilService,
    private personaService: PersonaService,
    private domicilioService: DomicilioService,
    private productoService: ProductoService,
    private ventaService: VentaService
  ) { }

  /**
   * Devuelve el servicio asociado a una entidad.
   */
  obtenerServicio(entidad: Entidad): any {

    switch (entidad.nombre) {

      case 'cliente':
        return this.clienteService;

      case 'usuario':
        return this.usuarioService;

      case 'perfil':
        return this.perfilService;

      case 'persona':
        return this.personaService;

      case 'domicilio':
        return this.domicilioService;

      case 'producto':
        return this.productoService;

      case 'venta':
        return this.ventaService;

      default:
        throw new Error(`No existe un servicio para la entidad '${entidad.nombre}'.`);

    }

  }

}










