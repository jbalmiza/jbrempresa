package com.jbrempresa.backend.shared;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Cálculo común de líneas de documentos comerciales. */
public record ImportesLinea(BigDecimal base, BigDecimal descuento, BigDecimal iva, BigDecimal total) {
    public static ImportesLinea calcular(int cantidad, BigDecimal precio, BigDecimal descuento, BigDecimal iva) {
        BigDecimal base = precio.multiply(BigDecimal.valueOf(cantidad));
        BigDecimal rebaja = base.multiply(descuento).divide(BigDecimal.valueOf(100),4,RoundingMode.HALF_UP);
        BigDecimal neto = base.subtract(rebaja);
        BigDecimal impuesto = neto.multiply(iva).divide(BigDecimal.valueOf(100),4,RoundingMode.HALF_UP);
        return new ImportesLinea(base,rebaja,impuesto,neto.add(impuesto).setScale(2,RoundingMode.HALF_UP));
    }
}
