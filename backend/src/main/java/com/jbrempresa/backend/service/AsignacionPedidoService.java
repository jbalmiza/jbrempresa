package com.jbrempresa.backend.service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.exception.ReglaNegocioException;
import com.jbrempresa.backend.repository.*;

/** Planifica cada línea completa de pedido en la primera agenda capacitada disponible. */
@Service
public class AsignacionPedidoService {
    private static final int DIAS_BUSQUEDA = 30;
    private static final int PASO_MINUTOS = 5;
    private static final int DURACION_REPARTO_MINUTOS = 30;
    private final ProductoRepository productos;
    private final ServicioRepository servicios;
    private final RecursoOperativoRepository recursosOperativos;
    private final RecursoCapacidadRepository capacidades;
    private final RecursoAgendableRepository agendas;
    private final HorarioRecursoRepository horarios;
    private final ExcepcionRecursoRepository excepciones;
    private final ReservaRepository reservas;
    private final ReservaRecursoRepository asignaciones;
    private final TareaReservaRepository tareas;

    public AsignacionPedidoService(ProductoRepository productos, ServicioRepository servicios,
            RecursoOperativoRepository recursosOperativos, RecursoCapacidadRepository capacidades,
            RecursoAgendableRepository agendas, HorarioRecursoRepository horarios,
            ExcepcionRecursoRepository excepciones, ReservaRepository reservas,
            ReservaRecursoRepository asignaciones, TareaReservaRepository tareas) {
        this.productos=productos;this.servicios=servicios;this.recursosOperativos=recursosOperativos;
        this.capacidades=capacidades;this.agendas=agendas;this.horarios=horarios;this.excepciones=excepciones;
        this.reservas=reservas;this.asignaciones=asignaciones;this.tareas=tareas;
    }

    @Transactional
    public void asignar(DocumentoVenta pedido, List<DocumentoVentaDetalle> lineas) {
        int orden=1;
        for (DocumentoVentaDetalle linea : lineas) {
            if (tareas.existsByEmpIdAndDvdIdAndTarActTrue(pedido.getEmpId(),linea.getDvdId())) { orden++; continue; }
            DatosLinea datos=datos(pedido.getEmpId(),linea);
            int duracion=Math.max(1,Optional.ofNullable(linea.getDvdDurTot()).orElse(datos.duracionUnitaria()*linea.getDvdCan()));
            Hueco hueco=primerHueco(pedido.getEmpId(),datos.tipo(),datos.habilidad(),duracion);
            if (hueco==null) throw new ReglaNegocioException("PEDIDOS","No hay un empleado disponible con la habilidad "+datos.habilidad()+" en los próximos "+DIAS_BUSQUEDA+" días.");
            guardar(pedido,linea,datos,hueco,duracion,orden++);
        }
    }

    /** Genera una sola entrega por pedido cuando toda su elaboración ya ha terminado. */
    @Transactional
    public TareaReserva generarReparto(DocumentoVenta pedido) {
        Optional<TareaReserva> existente=tareas.findActivasByPedido(pedido.getEmpId(),pedido.getDovId()).stream().filter(this::esReparto).findFirst();
        if(existente.isPresent())return existente.get();
        Hueco hueco=primerHueco(pedido.getEmpId(),"SERVICIO","REPARTO",DURACION_REPARTO_MINUTOS);
        if(hueco==null)throw new ReglaNegocioException("PEDIDOS","No hay un repartidor disponible en los próximos "+DIAS_BUSQUEDA+" días.");
        Reserva reserva=new Reserva();reserva.setEmpId(pedido.getEmpId());reserva.setPerId(pedido.getPerId());reserva.setDovId(pedido.getDovId());reserva.setResIni(hueco.inicio());reserva.setResFin(hueco.fin());reserva.setResEst("PENDIENTE");reserva.setResDurMin(DURACION_REPARTO_MINUTOS);reserva.setResTit("Reparto · Pedido "+pedido.getDovNum());reserva.setResObs(pedido.getDovDirEnv());reserva.setResUsuMov("SISTEMA");reserva.setResFecMov(LocalDateTime.now());reserva.setResAct(true);reserva=reservas.save(reserva);
        guardarAsignacion(pedido.getEmpId(),reserva,hueco,"SISTEMA");
        TareaReserva tarea=new TareaReserva();tarea.setEmpId(pedido.getEmpId());tarea.setResId(reserva.getResId());tarea.setRagId(hueco.agenda().getRagId());tarea.setTarTip("SERVICIO");tarea.setTarHab("REPARTO");tarea.setTarCan(1);tarea.setTarDurUni(DURACION_REPARTO_MINUTOS);tarea.setTarIniPre(hueco.inicio());tarea.setTarFinPre(hueco.fin());tarea.setTarOrd(1);tarea.setTarTit("Reparto del pedido "+pedido.getDovNum());tarea.setTarDurMin(DURACION_REPARTO_MINUTOS);tarea.setTarEst("PENDIENTE");tarea.setTarUsuMov("SISTEMA");tarea.setTarFecMov(LocalDateTime.now());tarea.setTarAct(true);return tareas.save(tarea);
    }

    private DatosLinea datos(Long empresaId,DocumentoVentaDetalle linea) {
        if ("S".equalsIgnoreCase(linea.getDvdTipLin())) {
            Servicio s=servicios.findByEmpIdAndSerIdAndSerActTrue(empresaId,linea.getSerId()).orElseThrow(()->negocio("Servicio no disponible para planificar."));
            return new DatosLinea("SERVICIO",normalizar(s.getSerTipSer()),Math.max(1,Optional.ofNullable(s.getSerDurMin()).orElse(1)));
        }
        Producto p=productos.findByEmpIdAndProIdAndProActTrue(empresaId,linea.getProId()).orElseThrow(()->negocio("Producto no disponible para planificar."));
        return new DatosLinea("PRODUCTO",normalizar(p.getProTipPro()),Math.max(1,Optional.ofNullable(p.getProDurMin()).orElse(1)));
    }

    private Hueco primerHueco(Long empresaId,String tipo,String habilidad,int duracion) {
        List<RecursoAgendable> candidatas=recursosOperativos.findByEmpIdAndReoActTrueOrderByReoNom(empresaId).stream()
                .filter(r->"EMPLEADO".equalsIgnoreCase(r.getReoTip())&&!"B".equalsIgnoreCase(r.getReoTipMov())&&Boolean.TRUE.equals(r.getReoOpe()))
                .filter(r->capacidades.findByEmpIdAndReoIdOrderByRecOriAscRecTipAsc(empresaId,r.getReoId()).stream()
                        .anyMatch(c->Boolean.TRUE.equals(c.getRecAct())&&tipo.equalsIgnoreCase(c.getRecOri())&&habilidad.equalsIgnoreCase(normalizar(c.getRecTip()))))
                .map(r->agendas.findByEmpIdAndRagTipAndRagRefIdAndRagActTrue(empresaId,"EMPLEADO",r.getReoId()).orElse(null))
                .filter(Objects::nonNull).toList();
        Hueco mejor=null;
        for(RecursoAgendable agenda:candidatas){Hueco actual=buscarHueco(empresaId,agenda,duracion);if(actual!=null&&(mejor==null||actual.inicio().isBefore(mejor.inicio())||actual.inicio().equals(mejor.inicio())&&agenda.getRagId()<mejor.agenda().getRagId()))mejor=actual;}
        return mejor;
    }

    private Hueco buscarHueco(Long empresaId,RecursoAgendable agenda,int duracion) {
        LocalDateTime cursor=redondear(LocalDateTime.now());
        LocalDate limite=cursor.toLocalDate().plusDays(DIAS_BUSQUEDA);
        while(!cursor.toLocalDate().isAfter(limite)){
            for(HorarioRecurso horario:horarios.findByEmpIdAndRagIdAndHorActTrueOrderByHorDiaAscHorIniAsc(empresaId,agenda.getRagId())){
                if(horario.getHorDia()!=cursor.getDayOfWeek().getValue())continue;
                LocalDateTime inicio=cursor.toLocalDate().atTime(horario.getHorIni()).plusMinutes(agenda.getRagMarPre());
                if(inicio.isBefore(cursor))inicio=redondear(cursor);
                LocalDateTime ultimo=cursor.toLocalDate().atTime(horario.getHorFin()).minusMinutes(agenda.getRagMarPos()+duracion);
                while(!inicio.isAfter(ultimo)){
                    LocalDateTime fin=inicio.plusMinutes(duracion);
                    LocalDateTime ocupacionInicio=inicio.minusMinutes(agenda.getRagMarPre());
                    LocalDateTime ocupacionFin=fin.plusMinutes(agenda.getRagMarPos());
                    if(disponible(empresaId,agenda,ocupacionInicio,ocupacionFin))return new Hueco(agenda,inicio,fin);
                    inicio=inicio.plusMinutes(PASO_MINUTOS);
                }
            }
            cursor=cursor.toLocalDate().plusDays(1).atStartOfDay();
        }
        return null;
    }

    private boolean disponible(Long empresaId,RecursoAgendable agenda,LocalDateTime inicio,LocalDateTime fin){
        List<ExcepcionRecurso> lista=excepciones.findByEmpIdAndRagIdAndExrFecBetweenAndExrActTrueOrderByExrFecAscExrIniAsc(empresaId,agenda.getRagId(),inicio.toLocalDate(),fin.toLocalDate());
        if(lista.stream().anyMatch(e->!Boolean.TRUE.equals(e.getExrDis())&&solapa(e,inicio.toLocalTime(),fin.toLocalTime())))return false;
        int capacidad=lista.stream().filter(e->e.getExrCap()!=null&&solapa(e,inicio.toLocalTime(),fin.toLocalTime())).map(ExcepcionRecurso::getExrCap).findFirst().orElse(agenda.getRagCap());
        return capacidad>0&&asignaciones.contarSolapamientos(empresaId,agenda.getRagId(),inicio,fin,null)<capacidad;
    }

    private void guardar(DocumentoVenta pedido,DocumentoVentaDetalle linea,DatosLinea datos,Hueco hueco,int duracion,int orden){
        Reserva reserva=new Reserva();reserva.setEmpId(pedido.getEmpId());reserva.setPerId(pedido.getPerId());reserva.setDovId(pedido.getDovId());reserva.setResIni(hueco.inicio());reserva.setResFin(hueco.fin());reserva.setResEst("PENDIENTE");reserva.setResDurMin(duracion);reserva.setResTit("Pedido "+pedido.getDovNum()+" · "+linea.getDvdNom());reserva.setResObs(linea.getDvdObs());reserva.setResUsuMov("CATALOGO");reserva.setResFecMov(LocalDateTime.now());reserva.setResAct(true);reserva=reservas.save(reserva);
        guardarAsignacion(pedido.getEmpId(),reserva,hueco,"CATALOGO");
        TareaReserva tarea=new TareaReserva();tarea.setEmpId(pedido.getEmpId());tarea.setResId(reserva.getResId());tarea.setRagId(hueco.agenda().getRagId());tarea.setDvdId(linea.getDvdId());tarea.setTarTip(datos.tipo());tarea.setProId(linea.getProId());tarea.setSerId(linea.getSerId());tarea.setTarHab(datos.habilidad());tarea.setTarCan(linea.getDvdCan());tarea.setTarDurUni(datos.duracionUnitaria());tarea.setTarIniPre(hueco.inicio());tarea.setTarFinPre(hueco.fin());tarea.setTarOrd(orden);tarea.setTarTit(linea.getDvdNom());tarea.setTarDurMin(duracion);tarea.setTarEst("PENDIENTE");tarea.setTarUsuMov("CATALOGO");tarea.setTarFecMov(LocalDateTime.now());tarea.setTarAct(true);tareas.save(tarea);
    }

    private void guardarAsignacion(Long empresaId,Reserva reserva,Hueco hueco,String usuario){ReservaRecurso asignacion=new ReservaRecurso();asignacion.setEmpId(empresaId);asignacion.setResId(reserva.getResId());asignacion.setRagId(hueco.agenda().getRagId());asignacion.setRerIniOcu(hueco.inicio().minusMinutes(hueco.agenda().getRagMarPre()));asignacion.setRerFinOcu(hueco.fin().plusMinutes(hueco.agenda().getRagMarPos()));asignacion.setRerUsuMov(usuario);asignacion.setRerFecMov(LocalDateTime.now());asignacion.setRerAct(true);asignaciones.save(asignacion);}
    private boolean esReparto(TareaReserva tarea){return "SERVICIO".equalsIgnoreCase(tarea.getTarTip())&&"REPARTO".equalsIgnoreCase(tarea.getTarHab());}

    private LocalDateTime redondear(LocalDateTime fecha){long resto=fecha.getMinute()%PASO_MINUTOS;return fecha.truncatedTo(ChronoUnit.MINUTES).plusMinutes(resto==0?0:PASO_MINUTOS-resto);}
    private boolean solapa(ExcepcionRecurso e,LocalTime inicio,LocalTime fin){return e.getExrIni()==null||e.getExrIni().isBefore(fin)&&e.getExrFin().isAfter(inicio);}
    private String normalizar(String valor){return Optional.ofNullable(valor).orElse("").trim().toUpperCase(Locale.ROOT);}
    private ReglaNegocioException negocio(String mensaje){return new ReglaNegocioException("PEDIDOS",mensaje);}
    private record DatosLinea(String tipo,String habilidad,int duracionUnitaria){}
    private record Hueco(RecursoAgendable agenda,LocalDateTime inicio,LocalDateTime fin){}
}
