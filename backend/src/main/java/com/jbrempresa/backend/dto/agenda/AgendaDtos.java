package com.jbrempresa.backend.dto.agenda;
import java.time.*;import java.util.List;import jakarta.validation.Valid;import jakarta.validation.constraints.*;
public final class AgendaDtos{private AgendaDtos(){}
 public record RecursoEntrada(@NotNull @Pattern(regexp="EMPLEADO|DOMICILIO")String ragTip,@NotNull @Positive Long ragRefId,@NotBlank @Size(max=200)String ragNom,@NotNull @Positive Integer ragCap,@NotNull @PositiveOrZero Integer ragMarPre,@NotNull @PositiveOrZero Integer ragMarPos,@NotNull LocalTime ragHorVis,@NotNull Integer ragIntVis){}
 public record RecursoSalida(Long ragId,String ragTip,Long ragRefId,String ragNom,Integer ragCap,Integer ragMarPre,Integer ragMarPos,LocalTime ragHorVis,Integer ragIntVis){}
 public record HorarioEntrada(@NotNull @Min(1)@Max(7)Integer horDia,@NotNull LocalTime horIni,@NotNull LocalTime horFin){}
 public record HorarioSalida(Long horId,Integer horDia,LocalTime horIni,LocalTime horFin){}
 public record ExcepcionEntrada(@NotNull LocalDate exrFec,LocalTime exrIni,LocalTime exrFin,@NotNull Boolean exrDis,@Positive Integer exrCap,@Size(max=300)String exrMot){}
 public record ExcepcionSalida(Long exrId,LocalDate exrFec,LocalTime exrIni,LocalTime exrFin,Boolean exrDis,Integer exrCap,String exrMot){}
 public record TareaEntrada(@NotNull @Positive Integer tarOrd,@NotBlank @Size(max=200)String tarTit,@NotNull @Positive Integer tarDurMin,@Positive Long ragId,@Positive Long dvdId,@Pattern(regexp="PRODUCTO|SERVICIO")String tarTip,@Positive Long proId,@Positive Long serId,@Size(max=100)String tarHab,@Positive Integer tarCan,@Positive Integer tarDurUni,LocalDateTime tarIniPre,LocalDateTime tarFinPre){}
 public record TareaSalida(Long tarId,Long ragId,Long dvdId,String tarTip,Long proId,Long serId,String tarHab,Integer tarCan,Integer tarDurUni,Integer tarOrd,String tarTit,Integer tarDurMin,String tarEst,LocalDateTime tarIniPre,LocalDateTime tarFinPre,LocalDateTime tarIniRea,LocalDateTime tarFinRea){}
 public record ReservaEntrada(@Positive Long perId,@Positive Long dovId,@Positive Long comId,@NotNull LocalDateTime resIni,@NotNull LocalDateTime resFin,@NotBlank @Size(max=200)String resTit,@Size(max=500)String resObs,@NotNull @Positive Long ragId,@NotNull List<@Valid TareaEntrada> tareas){}
 public record ReservaSalida(Long resId,Long perId,Long dovId,Long comId,LocalDateTime resIni,LocalDateTime resFin,String resEst,Integer resDurMin,String resTit,String resObs,Long ragId,Boolean pagado,List<TareaSalida> tareas){}
 public record ReprogramacionEntrada(@NotNull LocalDateTime resIni,@NotNull LocalDateTime resFin,@Size(max=300)String motivo){}
 public record ReprogramacionSalida(Long rprId,LocalDateTime rprIniAnt,LocalDateTime rprFinAnt,LocalDateTime rprIniNue,LocalDateTime rprFinNue,String rprMot,String rprUsuMov,LocalDateTime rprFecMov){}
 public record EstadoEntrada(@NotNull @Pattern(regexp="PENDIENTE|CONFIRMADA|EN_CURSO|TERMINADA|CANCELADA|AUSENCIA")String estado){}
}
