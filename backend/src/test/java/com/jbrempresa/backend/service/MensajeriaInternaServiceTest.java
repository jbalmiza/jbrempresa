package com.jbrempresa.backend.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.Perfil;
import com.jbrempresa.backend.repository.*;
import com.jbrempresa.backend.security.AccesoPerfilService;
import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MensajeriaInternaServiceTest {
    @ParameterizedTest
    @ValueSource(strings = {"JEFE", "EMPLEADO", "CLIENTE"})
    void deniegaAdministracionSinConsultarNiModificarDatos(String rol) {
        var conversaciones = mock(ConversacionInternaRepository.class);
        var participantes = mock(ParticipanteConversacionInternaRepository.class);
        var mensajes = mock(MensajeInternoRepository.class);
        var usuarios = mock(UsuarioRepository.class);
        var perfiles = mock(PerfilRepository.class);
        var contexto = mock(ContextoOperacion.class);
        var accesos = mock(AccesoPerfilService.class);
        var perfil = new Perfil();
        perfil.setPerNom(rol);
        when(accesos.perfil(null)).thenReturn(perfil);
        var servicio = new MensajeriaInternaService(conversaciones, participantes, mensajes,
                usuarios, perfiles, contexto, accesos);
        assertThatThrownBy(servicio::bajas).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> servicio.darBaja(1L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> servicio.reactivar(1L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> servicio.eliminarDefinitivamente(1L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> servicio.eliminarHasta(LocalDate.now())).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> servicio.editar(1L, null)).isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(conversaciones, participantes, mensajes, usuarios, perfiles);
    }
}
