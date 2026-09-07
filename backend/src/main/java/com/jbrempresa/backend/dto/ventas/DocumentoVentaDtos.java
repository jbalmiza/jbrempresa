package com.jbrempresa.backend.dto.ventas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.jbrempresa.backend.entity.DocumentoVenta;
import com.jbrempresa.backend.entity.DocumentoVentaDetalle;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public final class DocumentoVentaDtos {
    private DocumentoVentaDtos() {}

    public record DetalleEntrada(
            @NotNull @Pattern(regexp = "P|S") String dvdTipLin,
            Long proId,
            Long serId,
            @Size(max = 250) String dvdObs,
            @NotNull @Positive Integer dvdCan,
            @NotNull @PositiveOrZero BigDecimal dvdPre,
            @NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal dvdDes,
            @NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal dvdIva) {
        public DocumentoVentaDetalle entidad() {
            DocumentoVentaDetalle d = new DocumentoVentaDetalle();
            d.setDvdTipLin(dvdTipLin);
            d.setProId(proId);
            d.setSerId(serId);
            d.setDvdObs(dvdObs);
            d.setDvdCan(dvdCan);
            d.setDvdPre(dvdPre);
            d.setDvdDes(dvdDes);
            d.setDvdIva(dvdIva);
            return d;
        }
    }

    public record Entrada(
            @Size(max = 30) String dovNum,
            @NotNull @Positive Long perId,
            @NotNull LocalDate dovFec,
            @NotNull @Pattern(regexp = "BORRADOR|EMITIDO|PAGADO") String dovEst,
            @Size(max = 100) String dovUbi,
            @Pattern(regexp = "INTERNO|CATALOGO") String dovOri,
            @Pattern(regexp = "EN_POSICION|DOMICILIO") String dovMod,
            @Size(max = 500) String dovDirEnv,
            @Positive Integer dovFilMal,
            @Positive Integer dovColMal,
            @Size(max = 500) String dovObs,
            @NotEmpty List<@Valid DetalleEntrada> detalles) {
        public DocumentoVenta entidad() {
            DocumentoVenta d = new DocumentoVenta();
            d.setDovNum(dovNum);
            d.setPerId(perId);
            d.setDovFec(dovFec);
            d.setDovEst(dovEst);
            d.setDovUbi(dovUbi);
            d.setDovOri(dovOri);
            d.setDovMod(dovMod);
            d.setDovDirEnv(dovDirEnv);
            d.setDovFilMal(dovFilMal);
            d.setDovColMal(dovColMal);
            d.setDovObs(dovObs);
            d.setDetalles(detalles.stream().filter(detalle -> detalle != null).map(detalle -> detalle.entidad()).toList());
            return d;
        }
    }

    public record VentaRapidaEntrada(
            @Size(max = 500) String observaciones,
            @NotEmpty List<@Valid DetalleEntrada> detalles) {}

    public record DetalleSalida(
            Long dvdId, Long dovId, String dvdTipLin, Long proId, Long serId,
            String dvdNom, String dvdObs, Integer dvdCan, BigDecimal dvdPre, BigDecimal dvdDes,
            BigDecimal dvdIva, BigDecimal dvdImp, Integer dvdDurUni, Integer dvdDurTot) {
        public static DetalleSalida desde(DocumentoVentaDetalle d) {
            return new DetalleSalida(d.getDvdId(), d.getDovId(), d.getDvdTipLin(), d.getProId(), d.getSerId(),
                    d.getDvdNom(), d.getDvdObs(), d.getDvdCan(), d.getDvdPre(), d.getDvdDes(), d.getDvdIva(), d.getDvdImp(),
                    d.getDvdDurUni(), d.getDvdDurTot());
        }
    }

    public record Salida(
            Long dovId, Long empId, String dovTip, String dovNum, Long perId,
            LocalDate dovFec, String dovEst, Long dovIdOri, Long dovIdRai,
            String dovUbi, String dovOri, String dovMod, String dovDirEnv, Integer dovFilMal, Integer dovColMal,
            BigDecimal dovImpSub, BigDecimal dovImpDes, BigDecimal dovImpIva, BigDecimal dovImpTot,
            String dovObs, String dovUsuMov, LocalDateTime dovFecMov, Boolean dovAct,
            List<DetalleSalida> detalles) {
        public static Salida desde(DocumentoVenta d) {
            List<DetalleSalida> lineas = d.getDetalles() == null ? List.of()
                    : d.getDetalles().stream().map(detalle -> DetalleSalida.desde(detalle)).toList();
            return new Salida(d.getDovId(), d.getEmpId(), d.getDovTip(), d.getDovNum(), d.getPerId(),
                    d.getDovFec(), d.getDovEst(), d.getDovIdOri(), d.getDovIdRai(), d.getDovUbi(), d.getDovOri(), d.getDovMod(), d.getDovDirEnv(), d.getDovFilMal(), d.getDovColMal(), d.getDovImpSub(),
                    d.getDovImpDes(), d.getDovImpIva(), d.getDovImpTot(), d.getDovObs(), d.getDovUsuMov(),
                    d.getDovFecMov(), d.getDovAct(), lineas);
        }
    }
}
