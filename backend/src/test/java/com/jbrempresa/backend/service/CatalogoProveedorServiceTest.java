package com.jbrempresa.backend.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.EmpresaRelacion;
import com.jbrempresa.backend.repository.*;
import com.jbrempresa.backend.security.AccesoPerfilService;

class CatalogoProveedorServiceTest {
    final ContextoOperacion contexto=mock(ContextoOperacion.class);
    final AccesoPerfilService perfiles=mock(AccesoPerfilService.class);
    final EmpresaRelacionRepository relaciones=mock(EmpresaRelacionRepository.class);
    final CatalogoService catalogos=mock(CatalogoService.class);
    final CompraProveedorService compras=mock(CompraProveedorService.class);
    final CatalogoProveedorService servicio=new CatalogoProveedorService(contexto,perfiles,relaciones,mock(EmpresaRepository.class),catalogos,compras);
    @BeforeEach void configurar(){
        when(contexto.empresaId()).thenReturn(1L);
        when(perfiles.permite(null,"PROVEEDORES")).thenReturn(true);
    }
    EmpresaRelacion relacion(){
        var r=new EmpresaRelacion();r.setEmpId(1L);r.setEmpresaRelacionadaId(4L);r.setTipo("PROVEEDOR");
        when(relaciones.findByEmpIdAndEmpresaRelacionadaIdAndTipo(1L,4L,"PROVEEDOR")).thenReturn(Optional.of(r));return r;
    }
    @Test void relacionVigenteUsaElCatalogoYLosPedidosDelProveedor(){
        var pedido=new com.jbrempresa.backend.entity.DocumentoVenta();
        when(catalogos.pedirDocumento(any(),any())).thenReturn(pedido);
        relacion();servicio.catalogo(4L);servicio.pedir(4L,null);
        verify(compras).crear(1L,pedido);
        verify(catalogos).catalogoEmpresa(new CatalogoService.ContextoPublico(4L,null),"/catalogo/proveedores/4","CATALOGO_PROVEEDOR");
        verify(catalogos).pedirDocumento(new CatalogoService.ContextoPublico(4L,null),null);
    }
    @Test void sinRelacionRechazaCatalogoPedidoEImagenes(){
        assertThatThrownBy(()->servicio.catalogo(4L)).isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(()->servicio.pedir(4L,null)).isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(()->servicio.autorizar(4L)).isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(catalogos);
    }
    @Test void vuelveAValidarAlEnviarTrasDarDeBajaLaRelacion(){
        var r=relacion();servicio.catalogo(4L);r.setActiva(false);
        assertThatThrownBy(()->servicio.pedir(4L,null)).isInstanceOf(ResponseStatusException.class);
        verify(catalogos,never()).pedirDocumento(any(),any());
    }
    @Test void relacionCaducadaOFuturaNoAutoriza(){
        var r=relacion();r.setFechaFin(LocalDate.now().minusDays(1));
        assertThatThrownBy(()->servicio.autorizar(4L)).isInstanceOf(ResponseStatusException.class);
        r.setFechaFin(null);r.setFechaInicio(LocalDate.now().plusDays(1));
        assertThatThrownBy(()->servicio.autorizar(4L)).isInstanceOf(ResponseStatusException.class);
    }
    @Test void administradorDebeElegirEmpresaYSigueNecesitandoRelacion(){
        when(contexto.administradorGlobal()).thenReturn(true);
        when(contexto.empresaSeleccionada()).thenReturn(null);
        assertThatThrownBy(()->servicio.proveedores()).isInstanceOf(ResponseStatusException.class);
        when(contexto.empresaSeleccionada()).thenReturn(1L);
        assertThatThrownBy(()->servicio.catalogo(4L)).isInstanceOf(ResponseStatusException.class);
    }
    @Test void perfilSinAccesoAlModuloNoConsultaRelaciones(){
        when(perfiles.permite(null,"PROVEEDORES")).thenReturn(false);
        assertThatThrownBy(()->servicio.proveedores()).isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(relaciones);
    }
}
