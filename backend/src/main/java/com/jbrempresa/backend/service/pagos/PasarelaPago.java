package com.jbrempresa.backend.service.pagos;

import java.math.BigDecimal;
import java.util.Map;

/** Contrato que aislará Ventas de la implementación concreta de Redsys. */
public interface PasarelaPago {
    InicioPago iniciar(SolicitudPago solicitud);
    ResultadoPago confirmar(Map<String, String> parametrosFirmados);

    record SolicitudPago(Long empresaId, Long pedidoId, String numeroPedido, BigDecimal importe,
            String moneda, String urlRetornoCorrecto, String urlRetornoCancelado, String urlNotificacion) {}
    record InicioPago(String referencia, String url, Map<String, String> campos) {}
    record ResultadoPago(String referencia, String estado, String codigoRespuesta) {}
}
