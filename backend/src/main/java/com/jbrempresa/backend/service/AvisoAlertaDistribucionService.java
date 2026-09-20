package com.jbrempresa.backend.service;

import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.AvisoAlerta;
import com.jbrempresa.backend.entity.AvisoAlertaLectura;
import com.jbrempresa.backend.entity.EmpresaRelacion;
import com.jbrempresa.backend.repository.AvisoAlertaLecturaRepository;
import com.jbrempresa.backend.repository.AvisoAlertaRepository;
import com.jbrempresa.backend.repository.EmpresaRelacionRepository;
import com.jbrempresa.backend.repository.EmpresaRepository;
import com.jbrempresa.backend.security.AccesoPerfilService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class AvisoAlertaDistribucionService {
    public record Entrega(Long empId,Long avisoId,Long historicoId,String tipo,String titulo,String mensaje,
                          String emisor,String empresaEmisora,LocalDateTime fecha,boolean leido) {}
    private static final Set<String> EMISORES=Set.of("ADMINISTRADOR","JEFE","PROVEEDOR","EMPLEADO");
    private static final Set<String> UBICACIONES=Set.of("CATALOGO_CLIENTE","CATALOGO_PROVEEDOR","VENTANA","MENSAJES");
    private static final Set<String> DESTINATARIOS=Set.of("CLIENTE","EMPRESA","ADMINISTRADOR","JEFE","EMPLEADO");
    private final AvisoAlertaRepository avisos;
    private final AvisoAlertaLecturaRepository lecturas;
    private final EmpresaRelacionRepository relaciones;
    private final EmpresaRepository empresas;
    private final ContextoOperacion contexto;
    private final AccesoPerfilService perfiles;

    public AvisoAlertaDistribucionService(AvisoAlertaRepository avisos,AvisoAlertaLecturaRepository lecturas,
            EmpresaRelacionRepository relaciones,EmpresaRepository empresas,ContextoOperacion contexto,
            AccesoPerfilService perfiles){
        this.avisos=avisos;this.lecturas=lecturas;this.relaciones=relaciones;this.empresas=empresas;
        this.contexto=contexto;this.perfiles=perfiles;
    }

    public void preparar(AvisoAlerta a,Long empresaRegistro,AvisoAlerta anterior){
        if(!EMISORES.contains(a.getAviEmisor())||!UBICACIONES.contains(a.getAviUbicacion())||
                !DESTINATARIOS.contains(a.getAviDestinatario()))
            throw new IllegalArgumentException("Emisor, destinatario o ubicación no válidos.");
        String rol=rol();Long propia=contexto.usuarioActual().getEmpresaId();
        if(!contexto.administradorGlobal()&&!propia.equals(empresaRegistro))
            throw new IllegalArgumentException("Empresa no permitida.");
        if(anterior==null){
            if(!((rol.equals("ADMINISTRADOR")&&a.getAviEmisor().equals("ADMINISTRADOR"))
                ||(rol.equals("JEFE")&&Set.of("JEFE","PROVEEDOR").contains(a.getAviEmisor()))
                ||(rol.equals("EMPLEADO")&&a.getAviEmisor().equals("EMPLEADO"))))
                throw new IllegalArgumentException("El emisor no corresponde al usuario autenticado.");
            a.setAviEmisorEmpId(propia);a.setAviEmisorUsuId(contexto.usuarioActual().getUsuarioId());
        }else{
            if(!contexto.administradorGlobal()&&!contexto.usuarioActual().getUsuarioId().equals(anterior.getAviEmisorUsuId()))
                throw new IllegalArgumentException("Solo el emisor puede modificar el aviso o alerta.");
            a.setAviEmisor(anterior.getAviEmisor());
            a.setAviEmisorEmpId(anterior.getAviEmisorEmpId());a.setAviEmisorUsuId(anterior.getAviEmisorUsuId());
        }
        if(a.getAviEmisor().equals("PROVEEDOR")&&!proveedorDe(a.getAviEmisorEmpId(),
            a.getAviUbicacion().equals("MENSAJES")&&a.getAviDestinatario().equals("EMPRESA")?a.getAviDestEmpId():null))
            throw new IllegalArgumentException("La empresa emisora no tiene una relación de proveedor vigente.");
        if(a.getAviDestinatario().equals("EMPRESA")&&a.getAviDestEmpId()==null&&
            !(a.getAviEmisor().equals("ADMINISTRADOR")&&contexto.administradorGlobal()))
            throw new IllegalArgumentException("Solo el Administrador puede enviar a todas las empresas.");
        switch(a.getAviUbicacion()){
            case "CATALOGO_CLIENTE" -> {
                if(!empresaRegistro.equals(a.getAviDestEmpId())||!a.getAviDestinatario().equals("CLIENTE")||
                        a.getAviEmisor().equals("EMPLEADO")||a.getAviEmisor().equals("PROVEEDOR"))
                    throw new IllegalArgumentException("Destino del catálogo de clientes no válido.");
            }
            case "CATALOGO_PROVEEDOR" -> {
                if(!empresaRegistro.equals(a.getAviDestEmpId())||!a.getAviDestinatario().equals("EMPRESA")||
                        !a.getAviEmisor().equals("PROVEEDOR"))
                    throw new IllegalArgumentException("Destino del catálogo de proveedores no válido.");
            }
            case "VENTANA" -> {
                if(!a.getAviDestinatario().equals("EMPRESA")||
                    a.getAviVentana()==null||!a.getAviVentana().matches("/[a-zA-Z]+(?:/[a-zA-Z]+)?")||
                    Set.of("EMPLEADO","PROVEEDOR").contains(a.getAviEmisor()))
                    throw new IllegalArgumentException("Destino de ventana no válido.");
                if(a.getAviEmisor().equals("JEFE")&&(!propia.equals(a.getAviDestEmpId())||
                    !a.getAviVentana().equals("/empleados")))
                    throw new IllegalArgumentException("El Jefe solo puede publicar en el módulo Empleados.");
                if(a.getAviDestEmpId()!=null&&!empresas.existsById(a.getAviDestEmpId()))
                    throw new IllegalArgumentException("Empresa destinataria no encontrada.");
            }
            case "MENSAJES" -> {
                boolean valido=switch(a.getAviEmisor()){
                    case "EMPLEADO" -> a.getAviDestinatario().equals("JEFE")&&propia.equals(a.getAviDestEmpId());
                    case "JEFE" -> a.getAviDestinatario().equals("ADMINISTRADOR");
                    case "PROVEEDOR" -> a.getAviDestinatario().equals("ADMINISTRADOR")||
                        a.getAviDestinatario().equals("EMPRESA")&&proveedorDe(propia,a.getAviDestEmpId());
                    default -> a.getAviDestinatario().equals("EMPRESA");
                };
                if(!valido)throw new IllegalArgumentException("El emisor no puede enviar al destinatario indicado.");
                if(a.getAviDestEmpId()!=null&&!empresas.existsById(a.getAviDestEmpId()))
                    throw new IllegalArgumentException("Empresa destinataria no encontrada.");
            }
            default -> throw new IllegalArgumentException("Ubicación no válida.");
        }
        if(!a.getAviUbicacion().equals("VENTANA"))a.setAviVentana(null);
    }

    public List<AvisoAlerta> filtrarConsulta(List<AvisoAlerta> registros){
        if(!rol().equals("EMPLEADO"))return registros;
        Long usuario=contexto.usuarioActual().getUsuarioId();
        return registros.stream().filter(a->usuario.equals(a.getAviEmisorUsuId())).toList();
    }
    public void exigirGestion(AvisoAlerta a){
        if(contexto.administradorGlobal())return;
        if(!contexto.usuarioActual().getEmpresaId().equals(a.getEmpId())||
                !contexto.usuarioActual().getUsuarioId().equals(a.getAviEmisorUsuId()))
            throw new IllegalArgumentException("Solo el emisor puede gestionar este aviso o alerta.");
    }

    public List<Entrega> bandeja(){
        String rol=rol();Long empresa=contexto.usuarioActual().getEmpresaId();
        return avisos.bandejaPara(rol,empresa,LocalDateTime.now()).stream().map(this::entrega).toList();
    }
    public List<Entrega> ventana(String ruta){
        if(ruta==null||!ruta.matches("/[a-zA-Z]+(?:/[a-zA-Z]+)?"))
            throw new IllegalArgumentException("Ventana no válida.");
        String modulo=ruta.substring(1).split("/")[0].toUpperCase();
        if(!perfiles.permite(contexto.usuarioActual(),modulo)||
            ruta.startsWith("/administracion/")&&!contexto.administradorGlobal()&&
                Set.of("empresas","gestionEmpresas","perfiles","modulos","gestionModulos","mensajes","gestionMensajes")
                    .contains(ruta.substring(ruta.lastIndexOf('/')+1)))
            throw new IllegalArgumentException("No tiene acceso a esta ventana.");
        Long empresa=contexto.empresaId();
        return avisos.ventanaPara(ruta,empresa,LocalDateTime.now()).stream().map(this::entrega).toList();
    }
    @Transactional public void marcarLeido(Long empresa,Long id){
        Entrega visible=bandeja().stream().filter(a->a.empId().equals(empresa)&&a.avisoId().equals(id))
            .findFirst().orElseThrow(()->new IllegalArgumentException("Aviso no accesible."));
        Long usuario=contexto.usuarioActual().getUsuarioId();
        if(visible.leido())return;
        AvisoAlertaLectura lectura=new AvisoAlertaLectura();lectura.setEmpId(empresa);lectura.setAviId(id);
        lectura.setAviIdHis(visible.historicoId());lectura.setUsuarioId(usuario);
        lectura.setFechaLectura(LocalDateTime.now());lecturas.save(lectura);
    }
    @Transactional public void eliminarLecturas(Long empresa,Long id){lecturas.deleteByEmpIdAndAviId(empresa,id);}
    private Entrega entrega(AvisoAlerta a){
        boolean leido=lecturas.existsByEmpIdAndAviIdAndAviIdHisAndUsuarioId(a.getEmpId(),a.getAviId(),
            a.getAviIdHis(),contexto.usuarioActual().getUsuarioId());
        String empresa="ADMINISTRADOR".equals(a.getAviEmisor())||a.getAviEmisorEmpId()==null?"":
            empresas.findById(a.getAviEmisorEmpId()).map(e->e.getEmpNom()).orElse("");
        return new Entrega(a.getEmpId(),a.getAviId(),a.getAviIdHis(),a.getAviTipo(),a.getAviTitulo(),
            a.getAviMensaje(),a.getAviEmisor(),empresa,a.getAviFecMov(),leido);
    }
    private String rol(){return perfiles.perfil(contexto.usuarioActual()).getPerNom().trim().toUpperCase();}
    private boolean proveedorDe(Long proveedora,Long compradora){
        if(proveedora==null)return false;
        if(compradora==null)return relaciones.findByEmpIdAndTipoAndActivaTrueOrderById(proveedora,"CLIENTE")
            .stream().anyMatch(this::vigente);
        return relaciones.findByEmpIdAndEmpresaRelacionadaIdAndTipo(proveedora,compradora,"CLIENTE")
            .filter(this::vigente).isPresent();
    }
    private boolean vigente(EmpresaRelacion r){LocalDate hoy=LocalDate.now();return Boolean.TRUE.equals(r.getActiva())
        &&(r.getFechaInicio()==null||!r.getFechaInicio().isAfter(hoy))
        &&(r.getFechaFin()==null||!r.getFechaFin().isBefore(hoy));}
}
