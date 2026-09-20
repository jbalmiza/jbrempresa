package com.jbrempresa.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.exception.ReglaNegocioException;
import com.jbrempresa.backend.repository.*;

class AsignacionPedidoServiceTest {
    ProductoRepository productos=mock(ProductoRepository.class); ServicioRepository servicios=mock(ServicioRepository.class);
    RecursoOperativoRepository operativos=mock(RecursoOperativoRepository.class); RecursoCapacidadRepository capacidades=mock(RecursoCapacidadRepository.class);
    RecursoAgendableRepository agendas=mock(RecursoAgendableRepository.class); HorarioRecursoRepository horarios=mock(HorarioRecursoRepository.class);
    ExcepcionRecursoRepository excepciones=mock(ExcepcionRecursoRepository.class); ReservaRepository reservas=mock(ReservaRepository.class);
    ReservaRecursoRepository asignaciones=mock(ReservaRecursoRepository.class); TareaReservaRepository tareas=mock(TareaReservaRepository.class);
    AsignacionPedidoService servicio=new AsignacionPedidoService(productos,servicios,operativos,capacidades,agendas,horarios,excepciones,reservas,asignaciones,tareas);

    @Test void mantieneTodaLaCantidadEnUnaTarea() {
        DocumentoVenta pedido=pedido(); DocumentoVentaDetalle linea=linea(4,5); Producto producto=new Producto();producto.setProTipPro("BOCADILLOS");producto.setProDurMin(5);
        RecursoOperativo empleado=new RecursoOperativo();empleado.setReoId(7L);empleado.setReoTip("EMPLEADO");empleado.setReoTipMov("A");empleado.setReoOpe(true);
        RecursoCapacidad capacidad=new RecursoCapacidad();capacidad.setRecOri("PRODUCTO");capacidad.setRecTip("BOCADILLOS");capacidad.setRecAct(true);
        RecursoAgendable agenda=new RecursoAgendable();agenda.setRagId(9L);agenda.setRagCap(1);agenda.setRagMarPre(0);agenda.setRagMarPos(0);
        HorarioRecurso horario=new HorarioRecurso();horario.setHorDia(LocalDate.now().getDayOfWeek().getValue());horario.setHorIni(LocalTime.MIN);horario.setHorFin(LocalTime.MAX);
        when(productos.findByEmpIdAndProIdAndProActTrue(1L,3L)).thenReturn(Optional.of(producto));when(operativos.findByEmpIdAndReoActTrueOrderByReoNom(1L)).thenReturn(List.of(empleado));
        when(capacidades.findByEmpIdAndReoIdOrderByRecOriAscRecTipAsc(1L,7L)).thenReturn(List.of(capacidad));when(agendas.findByEmpIdAndRagTipAndRagRefIdAndRagActTrue(1L,"EMPLEADO",7L)).thenReturn(Optional.of(agenda));
        when(horarios.findByEmpIdAndRagIdAndHorActTrueOrderByHorDiaAscHorIniAsc(1L,9L)).thenReturn(List.of(horario));when(excepciones.findByEmpIdAndRagIdAndExrFecBetweenAndExrActTrueOrderByExrFecAscExrIniAsc(eq(1L),eq(9L),any(),any())).thenReturn(List.of());when(asignaciones.contarSolapamientos(eq(1L),eq(9L),any(),any(),isNull())).thenReturn(0L);
        AtomicLong ids=new AtomicLong(1);when(reservas.save(any())).thenAnswer(i->{Reserva r=i.getArgument(0);r.setResId(ids.getAndIncrement());return r;});
        servicio.asignar(pedido,List.of(linea));
        ArgumentCaptor<TareaReserva> captor=ArgumentCaptor.forClass(TareaReserva.class);verify(tareas).save(captor.capture());
        assertEquals(4,captor.getValue().getTarCan());assertEquals(5,captor.getValue().getTarDurUni());assertEquals(20,captor.getValue().getTarDurMin());assertEquals(9L,captor.getValue().getRagId());
    }

    @Test void rechazaElPedidoSiNoExisteEmpleadoCapacitado() {
        DocumentoVenta pedido=pedido();DocumentoVentaDetalle linea=linea(1,5);Producto producto=new Producto();producto.setProTipPro("BOCADILLOS");producto.setProDurMin(5);
        when(productos.findByEmpIdAndProIdAndProActTrue(1L,3L)).thenReturn(Optional.of(producto));when(operativos.findByEmpIdAndReoActTrueOrderByReoNom(1L)).thenReturn(List.of());
        ReglaNegocioException error=assertThrows(ReglaNegocioException.class,()->servicio.asignar(pedido,List.of(linea)));
        assertTrue(error.getMessage().contains("BOCADILLOS"));verifyNoInteractions(reservas,asignaciones);verify(tareas,never()).save(any());
    }

    @Test void creaUnSoloRepartoPorPedido() {
        DocumentoVenta pedido=pedido();pedido.setDovMod("DOMICILIO");pedido.setDovDirEnv("Calle de prueba");
        RecursoOperativo empleado=new RecursoOperativo();empleado.setReoId(8L);empleado.setReoTip("EMPLEADO");empleado.setReoTipMov("A");empleado.setReoOpe(true);
        RecursoCapacidad capacidad=new RecursoCapacidad();capacidad.setRecOri("SERVICIO");capacidad.setRecTip("REPARTO");capacidad.setRecAct(true);
        RecursoAgendable agenda=new RecursoAgendable();agenda.setRagId(10L);agenda.setRagCap(1);agenda.setRagMarPre(0);agenda.setRagMarPos(0);
        HorarioRecurso horario=new HorarioRecurso();horario.setHorDia(LocalDate.now().getDayOfWeek().getValue());horario.setHorIni(LocalTime.MIN);horario.setHorFin(LocalTime.MAX);
        when(tareas.findActivasByPedido(1L,2L)).thenReturn(List.of());when(operativos.findByEmpIdAndReoActTrueOrderByReoNom(1L)).thenReturn(List.of(empleado));when(capacidades.findByEmpIdAndReoIdOrderByRecOriAscRecTipAsc(1L,8L)).thenReturn(List.of(capacidad));when(agendas.findByEmpIdAndRagTipAndRagRefIdAndRagActTrue(1L,"EMPLEADO",8L)).thenReturn(Optional.of(agenda));when(horarios.findByEmpIdAndRagIdAndHorActTrueOrderByHorDiaAscHorIniAsc(1L,10L)).thenReturn(List.of(horario));when(excepciones.findByEmpIdAndRagIdAndExrFecBetweenAndExrActTrueOrderByExrFecAscExrIniAsc(eq(1L),eq(10L),any(),any())).thenReturn(List.of());when(asignaciones.contarSolapamientos(eq(1L),eq(10L),any(),any(),isNull())).thenReturn(0L);when(reservas.save(any())).thenAnswer(i->{Reserva r=i.getArgument(0);r.setResId(20L);return r;});when(tareas.save(any())).thenAnswer(i->i.getArgument(0));
        TareaReserva reparto=servicio.generarReparto(pedido);
        assertEquals("SERVICIO",reparto.getTarTip());assertEquals("REPARTO",reparto.getTarHab());assertEquals(30,reparto.getTarDurMin());assertEquals(10L,reparto.getRagId());
        when(tareas.findActivasByPedido(1L,2L)).thenReturn(List.of(reparto));assertSame(reparto,servicio.generarReparto(pedido));verify(reservas,times(1)).save(any());verify(tareas,times(1)).save(any());
    }

    private DocumentoVenta pedido(){DocumentoVenta p=new DocumentoVenta();p.setEmpId(1L);p.setDovId(2L);p.setDovNum("PED-1");p.setPerId(4L);return p;}
    private DocumentoVentaDetalle linea(int cantidad,int duracion){DocumentoVentaDetalle l=new DocumentoVentaDetalle();l.setDvdId(6L);l.setDvdTipLin("P");l.setProId(3L);l.setDvdNom("Bocadillo");l.setDvdCan(cantidad);l.setDvdDurUni(duracion);l.setDvdDurTot(cantidad*duracion);return l;}
}
