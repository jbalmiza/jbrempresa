package com.jbrempresa.backend.service;

import static com.jbrempresa.backend.dto.agenda.AgendaEmpleadoDtos.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.exception.ReglaNegocioException;
import com.jbrempresa.backend.repository.*;
import com.jbrempresa.backend.security.AccesoPerfilService;

@Service
public class AgendaEmpleadoService {
    private final ContextoOperacion contexto;
    private final AccesoPerfilService accesos;
    private final UsuarioRepository usuarios;
    private final RecursoOperativoRepository recursos;
    private final RecursoAgendableRepository agendas;
    private final TareaReservaRepository tareas;
    private final ReservaRepository reservas;
    private final DocumentoVentaRepository pedidos;
    private final AsignacionPedidoService asignacionPedidos;

    public AgendaEmpleadoService(ContextoOperacion contexto, AccesoPerfilService accesos, UsuarioRepository usuarios,
            RecursoOperativoRepository recursos, RecursoAgendableRepository agendas, TareaReservaRepository tareas,
            ReservaRepository reservas, DocumentoVentaRepository pedidos, AsignacionPedidoService asignacionPedidos) {
        this.contexto = contexto; this.accesos = accesos; this.usuarios = usuarios;
        this.recursos = recursos; this.agendas = agendas; this.tareas = tareas;
        this.reservas=reservas;this.pedidos=pedidos;this.asignacionPedidos=asignacionPedidos;
    }

    public AgendaEmpleadoSalida consultar(Long recursoAgendaId) {
        RecursoAgendable agenda = agendaConsulta(recursoAgendaId);
        List<TareaEmpleadoSalida> salida = tareas
                .findByEmpIdAndRagIdAndTarActTrueOrderByTarIniPreAscTarOrdAsc(contexto.empresaId(), agenda.getRagId())
                .stream().map(this::salida).toList();
        return new AgendaEmpleadoSalida(agenda.getRagId(), agenda.getRagNom(), salida);
    }

    private RecursoAgendable agendaConsulta(Long recursoAgendaId) {
        String perfil = accesos.perfil(contexto.usuarioActual()).getPerNom();
        boolean administrador = "ADMINISTRADOR".equalsIgnoreCase(perfil);
        boolean jefe = "JEFE".equalsIgnoreCase(perfil);
        if (recursoAgendaId == null) {
            if (administrador) throw negocio("Seleccione un empleado para consultar su agenda.");
            return agendaPropia();
        }
        if (!administrador && !jefe) {
            RecursoAgendable propia = agendaPropia();
            if (!recursoAgendaId.equals(propia.getRagId()))
                throw new AccessDeniedException("No puede consultar la agenda de otro empleado.");
            return propia;
        }
        RecursoAgendable agenda = agendas.findByEmpIdAndRagIdAndRagActTrue(contexto.empresaId(), recursoAgendaId)
                .filter(a -> "EMPLEADO".equalsIgnoreCase(a.getRagTip()))
                .orElseThrow(() -> negocio("La agenda no pertenece a un empleado de la empresa seleccionada."));
        recursos.findByEmpIdAndReoIdAndReoActTrue(contexto.empresaId(), agenda.getRagRefId())
                .filter(r -> "EMPLEADO".equalsIgnoreCase(r.getReoTip()) && !"B".equalsIgnoreCase(r.getReoTipMov())
                        && Boolean.TRUE.equals(r.getReoOpe()))
                .orElseThrow(() -> negocio("El empleado seleccionado no está activo y operativo."));
        return agenda;
    }

    @Transactional
    public TareaEmpleadoSalida cambiarEstado(Long tareaId, String estado) {
        TareaReserva tarea = tareaAccesible(tareaId);
        if ("PENDIENTE".equals(estado) && List.of("EN_CURSO", "FINALIZADO").contains(tarea.getTarEst())) {
            tarea.setTarEst(estado);
            tarea.setTarIniRea(null);
            tarea.setTarFinRea(null);
        } else if ("EN_CURSO".equals(estado) && "PENDIENTE".equals(tarea.getTarEst())) {
            tarea.setTarEst(estado);
            tarea.setTarIniRea(contexto.fechaActual());
        } else if ("FINALIZADO".equals(estado) && "EN_CURSO".equals(tarea.getTarEst())) {
            tarea.setTarEst(estado);
            tarea.setTarFinRea(contexto.fechaActual());
        } else {
            throw negocio("El estado de la tarea solo puede avanzar de Pendiente a En curso y después a Finalizado.");
        }
        tarea.setTarUsuMov(contexto.nombreUsuario());
        tarea.setTarFecMov(contexto.fechaActual());
        tarea=tareas.save(tarea);
        sincronizarFlujo(tarea);
        return salida(tarea);
    }

    @Transactional public TareaEmpleadoSalida marcarPagado(Long tareaId, Boolean pagado){
        TareaReserva tarea=tareaAccesible(tareaId);
        Reserva reserva=reservas.findByEmpIdAndResIdAndResActTrue(contexto.empresaId(),tarea.getResId()).orElseThrow(()->negocio("No se encontró la reserva de la tarea."));if(reserva.getDovId()==null)throw negocio("La tarea no pertenece a un pedido.");
        DocumentoVenta pedido=pedidos.findByEmpIdAndDovId(contexto.empresaId(),reserva.getDovId()).orElseThrow(()->negocio("No se encontró el pedido de la tarea."));pedido.setDovPag(Boolean.TRUE.equals(pagado));auditar(pedido);pedidos.save(pedido);return salida(tarea);
    }

    private void sincronizarFlujo(TareaReserva tarea){
        Reserva reserva=reservas.findByEmpIdAndResIdAndResActTrue(contexto.empresaId(),tarea.getResId()).orElseThrow(()->negocio("No se encontró la reserva de la tarea."));
        reserva.setResEst("FINALIZADO".equals(tarea.getTarEst())?"TERMINADA":"PENDIENTE".equals(tarea.getTarEst())?"PENDIENTE":"EN_CURSO");reserva.setResUsuMov(contexto.nombreUsuario());reserva.setResFecMov(contexto.fechaActual());reservas.save(reserva);
        if(reserva.getDovId()==null)return;
        DocumentoVenta pedido=pedidos.findByEmpIdAndDovId(contexto.empresaId(),reserva.getDovId()).orElseThrow(()->negocio("No se encontró el pedido de la tarea."));
        if(esReparto(tarea)){
            pedido.setDovEst("FINALIZADO".equals(tarea.getTarEst())?"ENTREGADO":"PENDIENTE".equals(tarea.getTarEst())?"FINALIZADO":"EN_REPARTO");auditar(pedido);pedidos.save(pedido);return;
        }
        if("PENDIENTE".equals(tarea.getTarEst())){List<TareaReserva> tareasPedido=tareas.findActivasByPedido(contexto.empresaId(),pedido.getDovId());boolean algunaEnCurso=tareasPedido.stream().filter(t->!esReparto(t)).anyMatch(t->"EN_CURSO".equals(t.getTarEst()));pedido.setDovEst(algunaEnCurso?"EN_CURSO":"EMITIDO");auditar(pedido);pedidos.save(pedido);return;}
        if("EN_CURSO".equals(tarea.getTarEst())&&"EMITIDO".equals(pedido.getDovEst())){pedido.setDovEst("EN_CURSO");auditar(pedido);pedidos.save(pedido);return;}
        if("FINALIZADO".equals(tarea.getTarEst())){
            List<TareaReserva> tareasPedido=tareas.findActivasByPedido(contexto.empresaId(),pedido.getDovId());
            boolean elaboracionTerminada=tareasPedido.stream().filter(t->!esReparto(t)).allMatch(t->"FINALIZADO".equals(t.getTarEst()));
            if(elaboracionTerminada){pedido.setDovEst("FINALIZADO");auditar(pedido);pedidos.save(pedido);if("DOMICILIO".equalsIgnoreCase(pedido.getDovMod()))asignacionPedidos.generarReparto(pedido);}
        }
    }
    private boolean esReparto(TareaReserva tarea){return "SERVICIO".equalsIgnoreCase(tarea.getTarTip())&&"REPARTO".equalsIgnoreCase(tarea.getTarHab());}
    private void auditar(DocumentoVenta pedido){pedido.setDovUsuMov(contexto.nombreUsuario());pedido.setDovFecMov(contexto.fechaActual());}

    private RecursoAgendable agendaPropia() {
        var autenticado = contexto.usuarioActual();
        String perfil = accesos.perfil(autenticado).getPerNom();
        if (!"EMPLEADO".equalsIgnoreCase(perfil) && !"JEFE".equalsIgnoreCase(perfil)
                && !"ADMINISTRADOR".equalsIgnoreCase(perfil))
            throw new AccessDeniedException("La agenda personal solo está disponible para empleados, jefes y administradores.");
        Usuario usuario = usuarios.findByEmpIdAndUsuId(contexto.empresaId(), autenticado.getUsuarioId())
                .orElseThrow(() -> negocio("No se encontró el usuario autenticado."));
        RecursoOperativo recurso = recursos.findByEmpIdAndPerIdAndReoActTrueOrderByReoIdHisDesc(
                contexto.empresaId(), usuario.getUsuPerId()).stream()
                .filter(r -> "EMPLEADO".equals(r.getReoTip()) && !"B".equals(r.getReoTipMov()) && Boolean.TRUE.equals(r.getReoOpe()))
                .findFirst().orElseThrow(() -> negocio("El usuario no tiene un recurso operativo de empleado activo."));
        return agendas.findByEmpIdAndRagTipAndRagRefIdAndRagActTrue(contexto.empresaId(), "EMPLEADO", recurso.getReoId())
                .orElseThrow(() -> negocio("El empleado no tiene una agenda configurada."));
    }

    private TareaReserva tareaAccesible(Long tareaId) {
        TareaReserva tarea = tareas.findByEmpIdAndTarIdAndTarActTrue(contexto.empresaId(), tareaId)
                .orElseThrow(() -> negocio("No se encontró la tarea en la empresa activa."));
        String perfil = accesos.perfil(contexto.usuarioActual()).getPerNom();
        if ("ADMINISTRADOR".equalsIgnoreCase(perfil) || "JEFE".equalsIgnoreCase(perfil)) {
            agendas.findByEmpIdAndRagIdAndRagActTrue(contexto.empresaId(), tarea.getRagId())
                    .filter(a -> "EMPLEADO".equalsIgnoreCase(a.getRagTip()))
                    .orElseThrow(() -> negocio("La tarea no pertenece a una agenda de empleado accesible."));
            return tarea;
        }
        RecursoAgendable propia = agendaPropia();
        if (!propia.getRagId().equals(tarea.getRagId()))
            throw new AccessDeniedException("No puede actuar sobre la agenda de otro empleado.");
        return tarea;
    }

    private TareaEmpleadoSalida salida(TareaReserva t) {
        Reserva reserva=reservas.findByEmpIdAndResIdAndResActTrue(contexto.empresaId(),t.getResId()).orElse(null);DocumentoVenta pedido=reserva==null||reserva.getDovId()==null?null:pedidos.findByEmpIdAndDovId(contexto.empresaId(),reserva.getDovId()).orElse(null);
        return new TareaEmpleadoSalida(t.getTarId(), t.getResId(), reserva==null?null:reserva.getDovId(), t.getDvdId(), pedido!=null&&Boolean.TRUE.equals(pedido.getDovPag()), t.getTarTip(), t.getTarHab(),
                t.getTarTit(), t.getTarCan(), t.getTarDurUni(), t.getTarDurMin(), t.getTarEst(),
                t.getTarIniPre(), t.getTarFinPre(), t.getTarIniRea(), t.getTarFinRea());
    }
    private ReglaNegocioException negocio(String mensaje) { return new ReglaNegocioException("AGENDA_EMPLEADO", mensaje); }
}
