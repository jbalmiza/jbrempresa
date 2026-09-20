package com.jbrempresa.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtFilterTest {
    private final JwtService jwt = mock(JwtService.class);
    private final CustomUserDetailsService usuarios = mock(CustomUserDetailsService.class);
    private final JwtFilter filtro = new JwtFilter(jwt, usuarios);

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void consultaPeriodicaNoRenuevaSesion() throws Exception {
        prepararUsuario();
        MockHttpServletResponse respuesta = ejecutar("/mensajeria-interna/no-leidos");

        assertThat(respuesta.getHeader("X-Refresh-Token")).isNull();
        verify(jwt, never()).generarToken("empleado", 1L, 2L, 3L);
    }

    @Test
    void actividadRealRenuevaToken() throws Exception {
        prepararUsuario();
        when(jwt.generarToken("empleado", 1L, 2L, 3L)).thenReturn("renovado");

        MockHttpServletResponse respuesta = ejecutar("/usuarios/actividad");

        assertThat(respuesta.getHeader("X-Refresh-Token")).isEqualTo("renovado");
    }

    private void prepararUsuario() {
        when(jwt.obtenerUsuario("token")).thenReturn("empleado");
        when(usuarios.loadUserByUsername("empleado"))
                .thenReturn(new JwtUser("empleado", "", 1L, 2L, 3L));
    }

    private MockHttpServletResponse ejecutar(String ruta) throws Exception {
        MockHttpServletRequest peticion = new MockHttpServletRequest("GET", ruta);
        peticion.setServletPath(ruta);
        peticion.addHeader("Authorization", "Bearer token");
        MockHttpServletResponse respuesta = new MockHttpServletResponse();
        filtro.doFilter(peticion, respuesta, new MockFilterChain());
        return respuesta;
    }
}
