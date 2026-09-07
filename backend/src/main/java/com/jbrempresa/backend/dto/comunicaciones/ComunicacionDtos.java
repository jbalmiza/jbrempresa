package com.jbrempresa.backend.dto.comunicaciones;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jbrempresa.backend.entity.Comunicacion;
import com.jbrempresa.backend.entity.ContactoCanal;
import com.jbrempresa.backend.entity.Mensaje;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class ComunicacionDtos {
    private ComunicacionDtos() {}

    public record ConversacionEntrada(
            @Size(max=20) String comTip,
            @Size(max=10) String comOri,
            @Size(max=20) String comCan,
            @Size(max=30) String comEst,
            @NotBlank @Size(max=200) String comAsu,
            @Size(max=30) String comInt,
            Long perId, Long usuId, Long areId) {
        public Comunicacion entidad() {
            Comunicacion v=new Comunicacion();v.setComTip(comTip);v.setComOri(comOri);v.setComCan(comCan);
            v.setComEst(comEst);v.setComAsu(comAsu);v.setComInt(comInt);v.setPerId(perId);v.setUsuId(usuId);v.setAreId(areId);return v;
        }
    }

    public record ConversacionSalida(Long comId,Long empId,String comTip,String comOri,String comCan,String comEst,
            String comAsu,String comInt,Long perId,Long usuId,Long areId,@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") LocalDateTime comFecUlt,
            String comUsuMov,@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") LocalDateTime comFecMov,Boolean comAct) {
        public static ConversacionSalida desde(Comunicacion v){return new ConversacionSalida(v.getComId(),v.getEmpId(),v.getComTip(),v.getComOri(),v.getComCan(),v.getComEst(),v.getComAsu(),v.getComInt(),v.getPerId(),v.getUsuId(),v.getAreId(),v.getComFecUlt(),v.getComUsuMov(),v.getComFecMov(),v.getComAct());}
    }

    public record MensajeEntrada(@Size(max=20) String menCan,@Size(max=200) String menIdExt,
            @Size(max=200) String menRem,@Size(max=200) String menAsu,@NotBlank String menCon,
            @Size(max=150) String menNomRem,@Size(max=30) String menDniRem,
            @Size(max=50) String menTelRem,@Size(max=150) String menEmaRem,LocalDateTime menFecRec) {
        public Mensaje entidad(){Mensaje v=new Mensaje();v.setMenCan(menCan);v.setMenIdExt(menIdExt);v.setMenRem(menRem);v.setMenAsu(menAsu);v.setMenCon(menCon);v.setMenNomRem(menNomRem);v.setMenDniRem(menDniRem);v.setMenTelRem(menTelRem);v.setMenEmaRem(menEmaRem);v.setMenFecRec(menFecRec);return v;}
    }

    public record RespuestaEntrada(@Size(max=10) String menDir,@Size(max=15) String menAut,
            @NotBlank String menCon,@Size(max=30) String menEst,@Size(max=200) String menIdExt) {
        public Mensaje entidad(){Mensaje v=new Mensaje();v.setMenDir(menDir);v.setMenAut(menAut);v.setMenCon(menCon);v.setMenEst(menEst);v.setMenIdExt(menIdExt);return v;}
    }

    public record ClasificacionEntrada(@NotBlank @Size(max=30) String menInt) {}
    public record ContactoEntrada(@NotBlank @Size(max=20) String cocCan,@NotBlank @Size(max=200) String cocIde) {}

    public record MensajeSalida(Long menId,Long empId,Long comId,Long menSec,String menDir,String menAut,String menCon,
            String menAsu,String menEst,String menInt,Double menConInt,String menOriCla,String menEstCla,String menIdExt,
            String menRem,String menDes,Long cocId,String menCan,Long perId,String menNomRem,String menDniRem,
            String menTelRem,String menEmaRem,@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") LocalDateTime menFecRec,@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") LocalDateTime menFecPro,Double menConPer,
            String menUsuMov,@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") LocalDateTime menFecMov,Boolean menAct) {
        public static MensajeSalida desde(Mensaje v){return new MensajeSalida(v.getMenId(),v.getEmpId(),v.getComId(),v.getMenSec(),v.getMenDir(),v.getMenAut(),v.getMenCon(),v.getMenAsu(),v.getMenEst(),v.getMenInt(),v.getMenConInt(),v.getMenOriCla(),v.getMenEstCla(),v.getMenIdExt(),v.getMenRem(),v.getMenDes(),v.getCocId(),v.getMenCan(),v.getPerId(),v.getMenNomRem(),v.getMenDniRem(),v.getMenTelRem(),v.getMenEmaRem(),v.getMenFecRec(),v.getMenFecPro(),v.getMenConPer(),v.getMenUsuMov(),v.getMenFecMov(),v.getMenAct());}
    }

    public record ContactoSalida(Long cocId,Long empId,String cocCan,String cocIde,String cocUsuMov,@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") LocalDateTime cocFecMov,Boolean cocAct){
        public static ContactoSalida desde(ContactoCanal v){return new ContactoSalida(v.getCocId(),v.getEmpId(),v.getCocCan(),v.getCocIde(),v.getCocUsuMov(),v.getCocFecMov(),v.getCocAct());}
    }
}
