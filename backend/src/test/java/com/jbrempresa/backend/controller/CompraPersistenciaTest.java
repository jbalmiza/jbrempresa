package com.jbrempresa.backend.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import jakarta.persistence.EntityManager;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.dto.compras.CompraDtos;
import com.jbrempresa.backend.entity.Producto;
import com.jbrempresa.backend.repository.*;
import com.jbrempresa.backend.service.CompraLineasService;

@DataJpaTest
@Import({CompraController.class, CompraLineasService.class})
class CompraPersistenciaTest {
    @Autowired CompraController controller;
    @Autowired CompraRepository compras;
    @Autowired CompraDetalleRepository lineas;
    @Autowired EntityManager em;
    @MockitoBean ContextoOperacion contexto;
    @MockitoBean ProductoRepository productos;

    @BeforeEach void preparar() {
        when(contexto.empresaId()).thenReturn(3L);
        when(contexto.empresaConsulta(null)).thenReturn(3L);
        when(contexto.nombreUsuario()).thenReturn("prueba");
        when(contexto.fechaActual()).thenReturn(LocalDateTime.of(2026,9,15,12,0));
        when(productos.findByEmpIdAndProIdAndProActTrue(3L,1L)).thenReturn(Optional.of(new Producto()));
    }
    CompraDtos.Entrada entrada(long producto,int cantidad) {
        return new CompraDtos.Entrada(1L,2L,null,null,null,null,new BigDecimal("999"),BigDecimal.ONE,null,
                null,null,null,null,null,List.of(new CompraDtos.DetalleEntrada(producto,cantidad,
                new BigDecimal("10"),new BigDecimal("10"),new BigDecimal("21"))));
    }
    @Test void guardaRecuperaSustituyeYEliminaElDetalleConLaCabecera() {
        var alta=controller.guardar(entrada(1L,2));
        em.flush();em.clear();
        var recuperada=controller.consultar().stream().filter(c->c.comId().equals(alta.comId())).findFirst().orElseThrow();
        assertThat(recuperada.detalles()).hasSize(1);
        assertThat(recuperada.detalles().get(0).getComDetCan()).isEqualTo(2);
        assertThat(recuperada.comImpTot()).isEqualByComparingTo("21.78");
        assertThat(recuperada.comImpPen()).isEqualByComparingTo("20.78");
        controller.actualizar(alta.comId(),entrada(1L,3));
        em.flush();em.clear();
        var detalle=lineas.findByEmpIdAndComIdOrderByComDetId(3L,alta.comId());
        assertThat(detalle).hasSize(1);
        assertThat(detalle.get(0).getComDetCan()).isEqualTo(3);
        assertThat(detalle.get(0).getComDetImp()).isEqualByComparingTo("32.67");
        controller.eliminar(alta.comId());
        em.flush();em.clear();
        assertThat(compras.findByEmpIdAndComId(3L,alta.comId())).isEmpty();
        assertThat(lineas.findByEmpIdAndComIdOrderByComDetId(3L,alta.comId())).isEmpty();
    }
    @Test void productoAjenoNoGuardaUnaCabeceraParcial() {
        long antes=compras.count();
        assertThatThrownBy(()->controller.guardar(entrada(99L,1))).isInstanceOf(IllegalArgumentException.class);
        assertThat(compras.count()).isEqualTo(antes);
    }
}
