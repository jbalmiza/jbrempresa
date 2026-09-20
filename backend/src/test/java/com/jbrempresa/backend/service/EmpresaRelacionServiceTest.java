package com.jbrempresa.backend.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.EmpresaRelacion;
import com.jbrempresa.backend.repository.*;

class EmpresaRelacionServiceTest {
    final EmpresaRelacionRepository repo = mock(EmpresaRelacionRepository.class);
    final ContextoOperacion contexto = mock(ContextoOperacion.class);
    final EmpresaRelacionMovimientoRepository movimientos = mock(EmpresaRelacionMovimientoRepository.class);
    final EmpresaRelacionService servicio = new EmpresaRelacionService(repo, mock(EmpresaRepository.class), contexto, movimientos);

    EmpresaRelacion relacion(long id, long empresa, long relacionada, long pareja, String tipo) {
        EmpresaRelacion r = spy(new EmpresaRelacion());
        when(r.getId()).thenReturn(id);
        r.setEmpId(empresa); r.setEmpresaRelacionadaId(relacionada); r.setParejaId(pareja); r.setTipo(tipo);
        when(repo.findById(id)).thenReturn(Optional.of(r));
        return r;
    }

    @Test void eliminaLasDosRelacionesLiberandoPrimeroSusEnlaces() {
        when(contexto.empresaId()).thenReturn(1L);
        var a=relacion(10,1,4,11,"PROVEEDOR");
        var b=relacion(11,4,1,10,"CLIENTE");
        servicio.eliminar(10L);
        assertThat(a.getParejaId()).isNull();
        assertThat(b.getParejaId()).isNull();
        var orden=inOrder(repo);
        orden.verify(repo).saveAll(List.of(a,b));
        orden.verify(repo).flush();
        orden.verify(repo).deleteAll(List.of(a,b));
    }

    @Test void noPermiteEliminarDesdeOtraEmpresa() {
        when(contexto.empresaId()).thenReturn(3L);
        relacion(10,1,4,11,"PROVEEDOR");
        assertThatThrownBy(()->servicio.eliminar(10L)).isInstanceOf(IllegalArgumentException.class);
        verify(repo,never()).deleteAll(anyList());
        verify(repo,never()).saveAll(anyList());
    }

    @Test void parejaIncoherenteNoModificaNingunaRelacion() {
        when(contexto.empresaId()).thenReturn(1L);
        var a=relacion(10,1,4,11,"PROVEEDOR");
        relacion(11,4,3,10,"CLIENTE");
        assertThatThrownBy(()->servicio.eliminar(10L)).isInstanceOf(IllegalArgumentException.class);
        assertThat(a.getParejaId()).isEqualTo(11L);
        verify(repo,never()).deleteAll(anyList());
        verify(repo,never()).saveAll(anyList());
    }

    @Test void bajaYReactivacionRegistranAmbosSentidosConCausaYAutor() {
        when(contexto.empresaId()).thenReturn(1L);
        when(contexto.nombreUsuario()).thenReturn("administrador");
        var fecha=java.time.LocalDateTime.of(2026,9,15,12,0);
        when(contexto.fechaActual()).thenReturn(fecha);
        var a=relacion(10,1,4,11,"PROVEEDOR");var b=relacion(11,4,1,10,"CLIENTE");
        servicio.estado(10L,false,"  Fin temporal  ");
        assertThat(a.getActiva()).isFalse();assertThat(b.getActiva()).isFalse();
        assertThat(a.getTipoMovimiento()).isEqualTo("B");
        servicio.estado(10L,true,"Reanudación");
        assertThat(a.getActiva()).isTrue();assertThat(b.getActiva()).isTrue();
        var captor=org.mockito.ArgumentCaptor.forClass(com.jbrempresa.backend.entity.EmpresaRelacionMovimiento.class);
        verify(movimientos,times(4)).save(captor.capture());
        assertThat(captor.getAllValues()).extracting(m->m.getTipo()).containsExactly("B","B","R","R");
        assertThat(captor.getAllValues().get(0).getCausa()).isEqualTo("Fin temporal");
        assertThat(captor.getAllValues()).allSatisfy(m->{assertThat(m.getUsuario()).isEqualTo("administrador");assertThat(m.getFecha()).isEqualTo(fecha);});
    }
    @Test void causaVaciaNoCambiaNingunaRelacion() {
        assertThatThrownBy(()->servicio.estado(10L,false," ")).isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repo,movimientos);
    }
    @Test void historicoDeOtraEmpresaNoSePuedeConsultar() {
        when(contexto.empresaId()).thenReturn(3L);relacion(10,1,4,11,"PROVEEDOR");
        assertThatThrownBy(()->servicio.historico(10L)).isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(movimientos);
    }
}
