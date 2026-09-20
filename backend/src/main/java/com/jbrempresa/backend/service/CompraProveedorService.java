package com.jbrempresa.backend.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.repository.*;

@Service
public class CompraProveedorService {
    private final CompraRepository compras;
    private final CompraDetalleRepository detalles;
    private final EmpresaRepository empresas;
    private final ContextoOperacion contexto;
    public CompraProveedorService(CompraRepository compras, CompraDetalleRepository detalles,
            EmpresaRepository empresas, ContextoOperacion contexto) {
        this.compras=compras;this.detalles=detalles;this.empresas=empresas;this.contexto=contexto;
    }

    @Transactional(propagation=Propagation.MANDATORY)
    public Compra crear(Long compradora, DocumentoVenta pedido) {
        if (!contexto.empresaId().equals(compradora) || compradora.equals(pedido.getEmpId())
                || pedido.getDovId()==null || !"PED".equals(pedido.getDovTip()) || pedido.getDetalles().isEmpty())
            throw new IllegalArgumentException("Pedido de proveedor no válido.");
        Empresa proveedor=empresas.findById(pedido.getEmpId()).orElseThrow();
        Compra compra=new Compra();
        compra.setEmpId(compradora);compra.setProveedorEmpresaId(pedido.getEmpId());
        compra.setPedidoVentaId(pedido.getDovId());compra.setPedidoVentaNumero(pedido.getDovNum());
        compra.setProveedorNombre(proveedor.getEmpNom());compra.setComFecPed(pedido.getDovFec());
        compra.setComImpSub(pedido.getDovImpSub());compra.setComImpDes(pedido.getDovImpDes());
        compra.setComImpIva(pedido.getDovImpIva());compra.setComImpTot(pedido.getDovImpTot());
        compra.setComImpPag(BigDecimal.ZERO);compra.setComImpPen(pedido.getDovImpTot());
        compra.setComAct(true);compra.setComUsuMov(contexto.nombreUsuario());compra.setComFecMov(contexto.fechaActual());
        compra=compras.save(compra);
        for(DocumentoVentaDetalle origen:pedido.getDetalles()) {
            CompraDetalle linea=new CompraDetalle();
            linea.setEmpId(compradora);linea.setComId(compra.getComId());
            // Estos IDs pertenecen a proveedorEmpresaId, nunca al catálogo de la compradora.
            linea.setProId(origen.getProId());linea.setSerId(origen.getSerId());
            linea.setNombre(origen.getDvdNom());linea.setObservaciones(origen.getDvdObs());
            linea.setComDetCan(origen.getDvdCan());linea.setComDetPre(origen.getDvdPre());
            linea.setComDetDes(origen.getDvdDes());linea.setComDetIva(origen.getDvdIva());linea.setComDetImp(origen.getDvdImp());
            linea.setComDetUsuMov(compra.getComUsuMov());linea.setComDetFecMov(compra.getComFecMov());linea.setComDetAct(true);
            detalles.save(linea);
        }
        return compra;
    }
}
