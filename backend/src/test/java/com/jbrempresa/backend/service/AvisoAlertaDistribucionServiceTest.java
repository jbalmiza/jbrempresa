package com.jbrempresa.backend.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.repository.*;
import com.jbrempresa.backend.security.*;

class AvisoAlertaDistribucionServiceTest {
    final AvisoAlertaRepository avisos=mock(AvisoAlertaRepository.class);
    final AvisoAlertaLecturaRepository lecturas=mock(AvisoAlertaLecturaRepository.class);
    final EmpresaRelacionRepository relaciones=mock(EmpresaRelacionRepository.class);
    final EmpresaRepository empresas=mock(EmpresaRepository.class);
    final ContextoOperacion contexto=mock(ContextoOperacion.class);
    final AccesoPerfilService perfiles=mock(AccesoPerfilService.class);
    final AvisoAlertaDistribucionService servicio=new AvisoAlertaDistribucionService(avisos,lecturas,relaciones,empresas,contexto,perfiles);

    void usuario(String rol,long empresa){
        var jwt=new JwtUser("usuario","",9L,empresa,1L);
        var perfil=new Perfil();perfil.setPerNom(rol);
        when(contexto.usuarioActual()).thenReturn(jwt);when(perfiles.perfil(jwt)).thenReturn(perfil);
    }
    AvisoAlerta aviso(String emisor,String destino,String ubicacion,Long empresaDestino){
        var a=new AvisoAlerta();a.setEmpId(4L);a.setAviId(3L);a.setAviIdHis(1L);
        a.setAviTipo("AVISO");a.setAviTitulo("Aviso");a.setAviMensaje("Contenido");
        a.setAviEmisor(emisor);a.setAviDestinatario(destino);a.setAviUbicacion(ubicacion);
        a.setAviDestEmpId(empresaDestino);return a;
    }
    void relacionProveedor(long proveedora,long compradora){
        var r=new EmpresaRelacion();r.setEmpId(proveedora);r.setEmpresaRelacionadaId(compradora);
        r.setTipo("CLIENTE");r.setActiva(true);
        when(relaciones.findByEmpIdAndEmpresaRelacionadaIdAndTipo(proveedora,compradora,"CLIENTE"))
            .thenReturn(Optional.of(r));
    }
    @Test void proveedorSoloEnviaAlJefeDeUnaEmpresaRelacionada(){
        usuario("Jefe",4L);relacionProveedor(4,1);
        var a=aviso("PROVEEDOR","EMPRESA","MENSAJES",1L);
        when(empresas.existsById(1L)).thenReturn(true);
        servicio.preparar(a,4L,null);
        assertThat(a.getAviEmisorEmpId()).isEqualTo(4L);
        assertThat(a.getAviEmisorUsuId()).isEqualTo(9L);
        a.setAviDestEmpId(2L);
        assertThatThrownBy(()->servicio.preparar(a,4L,null))
            .isInstanceOf(IllegalArgumentException.class);
    }
    @Test void avisoDeEmpleadoSoloAdmiteMensajesParaSuJefe(){
        usuario("Empleado",2L);
        when(empresas.existsById(2L)).thenReturn(true);
        var a=aviso("EMPLEADO","JEFE","MENSAJES",2L);
        servicio.preparar(a,2L,null);
        a.setAviDestEmpId(3L);
        assertThatThrownBy(()->servicio.preparar(a,2L,null))
            .isInstanceOf(IllegalArgumentException.class);
        a.setAviDestEmpId(2L);a.setAviUbicacion("VENTANA");a.setAviDestinatario("EMPRESA");a.setAviVentana("/empleados");
        assertThatThrownBy(()->servicio.preparar(a,2L,null))
            .isInstanceOf(IllegalArgumentException.class);
    }
    @Test void administradorPuedePublicarEnTodasLasEmpresasYJefeNo(){
        usuario("Administrador",4L);
        when(contexto.administradorGlobal()).thenReturn(true);
        var a=aviso("ADMINISTRADOR","EMPRESA","VENTANA",null);
        a.setAviVentana("/comunicaciones/avisosAlertas");
        servicio.preparar(a,4L,null);
        a.setAviUbicacion("MENSAJES");a.setAviVentana(null);
        servicio.preparar(a,4L,null);
        usuario("Jefe",4L);
        when(contexto.administradorGlobal()).thenReturn(false);
        var jefe=aviso("JEFE","EMPRESA","VENTANA",null);jefe.setAviVentana("/empleados");
        assertThatThrownBy(()->servicio.preparar(jefe,4L,null))
            .isInstanceOf(IllegalArgumentException.class);
    }
    @Test void ventanaDeEmpleadosSoloEntregaEnLaEmpresaDestinataria(){
        usuario("Empleado",2L);
        when(perfiles.permite(any(),eq("EMPLEADOS"))).thenReturn(true);
        when(contexto.empresaId()).thenReturn(2L);
        var a=aviso("JEFE","EMPRESA","VENTANA",2L);a.setAviEmisorEmpId(2L);a.setAviVentana("/empleados");
        when(avisos.ventanaPara(eq("/empleados"),eq(2L),any(LocalDateTime.class))).thenReturn(List.of(a));
        assertThat(servicio.ventana("/empleados")).hasSize(1);
        when(contexto.empresaId()).thenReturn(3L);
        assertThat(servicio.ventana("/empleados")).isEmpty();
    }
    @Test void avisoParaEmpresaLlegaAlJefeYNuncaAlEmpleado(){
        var a=aviso("PROVEEDOR","EMPRESA","MENSAJES",2L);a.setAviEmisorEmpId(4L);
        when(avisos.bandejaPara(eq("JEFE"),eq(2L),any(LocalDateTime.class))).thenReturn(List.of(a));
        usuario("Jefe",2L);
        assertThat(servicio.bandeja()).hasSize(1);
        usuario("Empleado",2L);
        assertThat(servicio.bandeja()).isEmpty();
    }
}
