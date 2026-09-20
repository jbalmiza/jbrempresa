package com.jbrempresa.backend.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.*;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.repository.*;
import com.jbrempresa.backend.security.AccesoPerfilService;

@DataJpaTest
@Import({CatalogoProveedorService.class,CompraProveedorService.class})
@Transactional(propagation=Propagation.NOT_SUPPORTED)
class CompraProveedorTransaccionTest {
    @Autowired CatalogoProveedorService servicio;
    @Autowired CompraRepository compras;
    @Autowired CompraDetalleRepository detalles;
    @Autowired DocumentoVentaRepository ventas;
    @MockitoBean ContextoOperacion contexto;
    @MockitoBean AccesoPerfilService perfiles;
    @MockitoBean EmpresaRelacionRepository relaciones;
    @MockitoBean EmpresaRepository empresas;
    @MockitoBean CatalogoService catalogos;
    boolean provocarFallo;

    @BeforeEach void preparar() {
        detalles.deleteAll();compras.deleteAll();ventas.deleteAll();
        when(contexto.empresaId()).thenReturn(1L);
        when(contexto.nombreUsuario()).thenReturn("jefe");
        when(contexto.fechaActual()).thenReturn(LocalDateTime.now());
        when(perfiles.permite(null,"PROVEEDORES")).thenReturn(true);
        EmpresaRelacion r=new EmpresaRelacion();r.setEmpId(1L);r.setEmpresaRelacionadaId(4L);r.setTipo("PROVEEDOR");
        when(relaciones.findByEmpIdAndEmpresaRelacionadaIdAndTipo(1L,4L,"PROVEEDOR")).thenReturn(Optional.of(r));
        Empresa empresa=new Empresa();empresa.setEmpNom("Proveedor renombrado");
        when(empresas.findById(4L)).thenReturn(Optional.of(empresa));
        when(catalogos.pedirDocumento(any(),any())).thenAnswer(i->venta());
    }
    DocumentoVenta venta() {
        DocumentoVenta d=new DocumentoVenta();d.setEmpId(4L);d.setDovTip("PED");d.setDovNum("PED-1");
        d.setPerId(8L);d.setDovFec(LocalDate.now());d.setDovEst("EMITIDO");d.setDovUsuMov("CATALOGO");
        d.setDovFecMov(LocalDateTime.now());d.setDovAct(true);d.setDovImpSub(new BigDecimal("20"));
        d.setDovImpDes(BigDecimal.ZERO);d.setDovImpIva(new BigDecimal("4.20"));d.setDovImpTot(new BigDecimal("24.20"));
        d=ventas.save(d);
        List<DocumentoVentaDetalle> lineas=new ArrayList<>();
        for(String tipo:List.of("P","S")){
            DocumentoVentaDetalle l=new DocumentoVentaDetalle();l.setDvdTipLin(tipo);
            if(tipo.equals("P"))l.setProId(7L);else l.setSerId(7L);
            l.setDvdNom(provocarFallo&&tipo.equals("S")?"X".repeat(201):tipo+" del proveedor");
            l.setDvdCan(1);l.setDvdPre(BigDecimal.TEN);l.setDvdDes(BigDecimal.ZERO);
            l.setDvdIva(new BigDecimal("21"));l.setDvdImp(new BigDecimal("12.10"));lineas.add(l);
        }
        d.setDetalles(lineas);return d;
    }
    @Test void creaCompraConIdsDeOrigenYLineasMixtasSinConfundirCatalogos() {
        servicio.pedir(4L,null);
        Compra c=compras.findByEmpId(1L).get(0);
        assertThat(c.getProveedorEmpresaId()).isEqualTo(4L);
        assertThat(ventas.findById(c.getPedidoVentaId())).isPresent();
        assertThat(c.getPedidoVentaNumero()).isEqualTo("PED-1");
        assertThat(c.getComImpTot()).isEqualByComparingTo("24.20");
        assertThat(compras.findByEmpId(4L)).isEmpty();
        var lineas=detalles.findByEmpIdAndComIdOrderByComDetId(1L,c.getComId());
        assertThat(lineas).hasSize(2);
        assertThat(lineas.get(0).getProId()).isEqualTo(7L);
        assertThat(lineas.get(1).getProId()).isNull();
        assertThat(lineas.get(1).getSerId()).isEqualTo(7L);
    }
    @Test void falloAlGuardarUnaLineaRevierteVentaCompraYLineas() {
        provocarFallo=true;
        assertThatThrownBy(()->servicio.pedir(4L,null)).isInstanceOf(RuntimeException.class);
        assertThat(ventas.count()).isZero();assertThat(compras.count()).isZero();assertThat(detalles.count()).isZero();
    }
}
