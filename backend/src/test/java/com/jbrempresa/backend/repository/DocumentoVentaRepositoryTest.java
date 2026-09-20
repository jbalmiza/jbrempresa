package com.jbrempresa.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import com.jbrempresa.backend.entity.DocumentoVenta;
import com.jbrempresa.backend.entity.DocumentoVentaMovimiento;

@DataJpaTest
class DocumentoVentaRepositoryTest {
    @Autowired private DocumentoVentaRepository documentos;
    @Autowired private DocumentoVentaMovimientoRepository movimientos;

    @Test
    void filtraPorEmpresaYUsuarioDeAltaAunqueCambieElUsuarioDeMovimiento() {
        DocumentoVenta propio = pedido(1L, "PED-1", "jefe");
        alta(propio, "empleado1");
        alta(pedido(1L, "PED-2", "empleado2"), "empleado2");
        alta(pedido(2L, "PED-3", "empleado1"), "empleado1");

        assertThat(documentos.pedidosCreadosPor(1L, "empleado1", false))
                .extracting(DocumentoVenta::getDovId).containsExactly(propio.getDovId());
        assertThat(documentos.pedidoCreadoPor(1L, propio.getDovId(), "empleado1")).isPresent();
        assertThat(documentos.pedidoCreadoPor(1L, propio.getDovId(), "empleado2")).isEmpty();
    }

    private DocumentoVenta pedido(Long empresa, String numero, String usuarioMovimiento) {
        DocumentoVenta d = new DocumentoVenta();
        d.setEmpId(empresa);
        d.setDovTip("PED");
        d.setDovNum(numero);
        d.setPerId(1L);
        d.setDovFec(LocalDate.of(2026, 9, 18));
        d.setDovEst("EMITIDO");
        d.setDovUsuMov(usuarioMovimiento);
        d.setDovFecMov(LocalDateTime.of(2026, 9, 18, 10, 0));
        d.setDovAct(true);
        return documentos.saveAndFlush(d);
    }

    private void alta(DocumentoVenta d, String creador) {
        DocumentoVentaMovimiento m = new DocumentoVentaMovimiento();
        m.setEmpId(d.getEmpId());
        m.setDovId(d.getDovId());
        m.setDovTip(d.getDovTip());
        m.setDovNum(d.getDovNum());
        m.setPerId(d.getPerId());
        m.setDovFec(d.getDovFec());
        m.setDovEst(d.getDovEst());
        m.setDvmTipMov("ALTA");
        m.setDvmUsuMov(creador);
        m.setDvmFecMov(LocalDateTime.of(2026, 9, 18, 10, 0));
        movimientos.saveAndFlush(m);
    }
}
