package com.jbrempresa.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.repository.*;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.shared.ImportesLinea;

@Service
public class CompraLineasService {
    private final CompraDetalleRepository lineas;
    private final ProductoRepository productos;
    private final ContextoOperacion contexto;
    public CompraLineasService(CompraDetalleRepository lineas,ProductoRepository productos,ContextoOperacion contexto){
        this.lineas=lineas;this.productos=productos;this.contexto=contexto;
    }
    public void preparar(Compra compra) {
        if(compra.getDetalles()==null||compra.getDetalles().isEmpty()) throw new IllegalArgumentException("La compra debe contener al menos una línea.");
        BigDecimal subtotal=BigDecimal.ZERO,descuento=BigDecimal.ZERO,iva=BigDecimal.ZERO,total=BigDecimal.ZERO;
        for(CompraDetalle linea:compra.getDetalles()){
            productos.findByEmpIdAndProIdAndProActTrue(compra.getEmpId(),linea.getProId())
                .filter(p->!"B".equals(p.getProTipMov()))
                .orElseThrow(()->new IllegalArgumentException("Producto no válido para la empresa de la compra."));
            var importe=ImportesLinea.calcular(linea.getComDetCan(),linea.getComDetPre(),linea.getComDetDes(),linea.getComDetIva());
            linea.setComDetImp(importe.total());
            subtotal=subtotal.add(importe.base());descuento=descuento.add(importe.descuento());iva=iva.add(importe.iva());total=total.add(importe.total());
        }
        compra.setComImpSub(subtotal.setScale(2,RoundingMode.HALF_UP));
        compra.setComImpDes(descuento.setScale(2,RoundingMode.HALF_UP));
        compra.setComImpIva(iva.setScale(2,RoundingMode.HALF_UP));
        compra.setComImpTot(total.setScale(2,RoundingMode.HALF_UP));
        BigDecimal pagado=compra.getComImpPag()==null?BigDecimal.ZERO:compra.getComImpPag();
        compra.setComImpPag(pagado);
        compra.setComImpPen(compra.getComImpTot().subtract(pagado));
    }
    public Compra guardar(Compra compra) {
        lineas.deleteByEmpIdAndComId(compra.getEmpId(),compra.getComId());
        compra.setDetalles(compra.getDetalles().stream().map(linea->{
            linea.setComDetId(null);linea.setEmpId(compra.getEmpId());linea.setComId(compra.getComId());
            linea.setComDetUsuMov(contexto.nombreUsuario());linea.setComDetFecMov(contexto.fechaActual());linea.setComDetAct(true);
            return lineas.save(linea);
        }).toList());
        return compra;
    }
    public Compra cargar(Compra compra){
        compra.setDetalles(lineas.findByEmpIdAndComIdOrderByComDetId(compra.getEmpId(),compra.getComId()));
        return compra;
    }
    public void eliminar(Compra compra){lineas.deleteByEmpIdAndComId(compra.getEmpId(),compra.getComId());}
}
