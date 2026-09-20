package com.jbrempresa.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.entity.DocumentoVenta;
import com.jbrempresa.backend.entity.DocumentoVentaDetalle;
import com.jbrempresa.backend.repository.DocumentoVentaDetalleRepository;
import com.jbrempresa.backend.repository.DocumentoVentaRepository;

@Service
public class FacturacionPedidoService {
    private final DocumentoVentaRepository documentos;
    private final DocumentoVentaDetalleRepository detalles;
    private final NumeradorDocumentoVentaService numerador;
    private final ConfiguracionDocumentosVentaService configuracion;
    public FacturacionPedidoService(DocumentoVentaRepository documentos, DocumentoVentaDetalleRepository detalles,
            NumeradorDocumentoVentaService numerador, ConfiguracionDocumentosVentaService configuracion) {
        this.documentos=documentos; this.detalles=detalles; this.numerador=numerador; this.configuracion=configuracion;
    }
    public DocumentoVenta generar(DocumentoVenta pedido, List<DocumentoVentaDetalle> lineas, String usuario) {
        if (!"PED".equals(pedido.getDovTip())
                || documentos.existsByEmpIdAndDovIdOriAndDovTip(pedido.getEmpId(), pedido.getDovId(), "FAC")) return null;
        DocumentoVenta factura=new DocumentoVenta(); factura.setEmpId(pedido.getEmpId()); factura.setDovTip("FAC");
        factura.setDovTipFac(configuracion.tipoFactura(pedido.getEmpId()));
        factura.setDovNum(numerador.siguiente(pedido.getEmpId(),"FAC")); factura.setPerId(pedido.getPerId());
        factura.setDovFec(LocalDate.now()); factura.setDovEst("SIMPLIFICADA".equals(factura.getDovTipFac())?"EMITIDO":"BORRADOR");
        factura.setDovIdOri(pedido.getDovId()); factura.setDovIdRai(pedido.getDovIdRai()); factura.setDovOri(pedido.getDovOri());
        factura.setDovMod(pedido.getDovMod()); factura.setDovUbi(pedido.getDovUbi()); factura.setDovDirEnv(pedido.getDovDirEnv());
        factura.setDovFilMal(pedido.getDovFilMal()); factura.setDovColMal(pedido.getDovColMal());
        factura.setDovImpSub(pedido.getDovImpSub()); factura.setDovImpDes(pedido.getDovImpDes());
        factura.setDovImpIva(pedido.getDovImpIva()); factura.setDovImpTot(pedido.getDovImpTot()); factura.setDovObs(pedido.getDovObs());
        factura.setDovPag(Boolean.TRUE.equals(pedido.getDovPag())); factura.setDovUsuMov(usuario);
        factura.setDovFecMov(LocalDateTime.now()); factura.setDovAct(true); factura=documentos.save(factura);
        for(DocumentoVentaDetalle origen:lineas){DocumentoVentaDetalle copia=new DocumentoVentaDetalle();copia.setEmpId(factura.getEmpId());copia.setDovId(factura.getDovId());copia.setDvdTipLin(origen.getDvdTipLin());copia.setProId(origen.getProId());copia.setSerId(origen.getSerId());copia.setDvdNom(origen.getDvdNom());copia.setDvdObs(origen.getDvdObs());copia.setDvdCan(origen.getDvdCan());copia.setDvdPre(origen.getDvdPre());copia.setDvdDes(origen.getDvdDes());copia.setDvdIva(origen.getDvdIva());copia.setDvdImp(origen.getDvdImp());copia.setDvdDurUni(origen.getDvdDurUni());copia.setDvdDurTot(origen.getDvdDurTot());copia.setDvdUsuMov(usuario);copia.setDvdFecMov(LocalDateTime.now());copia.setDvdAct(true);detalles.save(copia);}
        return factura;
    }
}
