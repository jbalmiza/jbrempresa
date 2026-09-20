package com.jbrempresa.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.DocumentoVenta;
import com.jbrempresa.backend.repository.DocumentoVentaRepository;

class DocumentoVentaEmpleadoTest {
    private final DocumentoVentaRepository documentos = mock(DocumentoVentaRepository.class);
    private final ContextoOperacion contexto = mock(ContextoOperacion.class);
    private final DocumentoVentaController controlador = new DocumentoVentaController(documentos,
            null, null, null, null, null, null, contexto, null, null, null, null, null, null, null);

    @Test
    void consultaSoloPedidosDelUsuarioAutenticado() {
        when(contexto.empleado()).thenReturn(true);
        when(contexto.empresaConsulta(null)).thenReturn(2L);
        when(contexto.nombreUsuario()).thenReturn("empleado1");
        when(documentos.pedidosCreadosPor(2L, "empleado1", false)).thenReturn(List.of());

        assertThat(controlador.consultar("PED", false)).isEmpty();
        verify(documentos).pedidosCreadosPor(2L, "empleado1", false);
    }

    @Test
    void impideConsultarOtrosTiposYModificarPedidosAjenos() {
        when(contexto.empleado()).thenReturn(true);
        when(contexto.empresaId()).thenReturn(2L);
        when(contexto.nombreUsuario()).thenReturn("empleado1");

        assertThatThrownBy(() -> controlador.consultar("FAC", false))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        error -> assertThat(error.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));
        assertThatThrownBy(() -> controlador.actualizar("PED", 3L, false, null, null))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        error -> assertThat(error.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));
        verify(documentos).pedidoCreadoPor(2L, 3L, "empleado1");
    }

    @Test
    void permiteEliminarLaCadenaDeUnPedidoPropio() {
        when(contexto.empleado()).thenReturn(true);
        when(contexto.empresaId()).thenReturn(2L);
        when(contexto.nombreUsuario()).thenReturn("empleado1");
        DocumentoVenta pedido = new DocumentoVenta();
        pedido.setDovId(3L);
        pedido.setDovTip("PED");
        when(documentos.pedidoCreadoPor(2L, 3L, "empleado1")).thenReturn(Optional.of(pedido));
        when(documentos.findByEmpIdAndDovIdRai(2L, 3L)).thenReturn(List.of());

        controlador.eliminarCompleto("PED", 3L);

        verify(documentos).deleteAll(List.of());
    }
}
