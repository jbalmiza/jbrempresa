package com.jbrempresa.backend.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.Compra;
import com.jbrempresa.backend.repository.CompraRepository;

class CompraControllerTest {
    final CompraRepository repo = mock(CompraRepository.class);
    final ContextoOperacion contexto = mock(ContextoOperacion.class);
    final com.jbrempresa.backend.service.CompraLineasService lineas = mock(com.jbrempresa.backend.service.CompraLineasService.class);
    MockMvc mvc;

    @BeforeEach void preparar() {
        when(lineas.guardar(any(Compra.class))).thenAnswer(i -> i.getArgument(0));
        mvc = MockMvcBuilders.standaloneSetup(new CompraController(repo, contexto, lineas)).build();
        when(contexto.empresaId()).thenReturn(3L);
        when(contexto.empresaConsulta(null)).thenReturn(3L);
        when(repo.save(any(Compra.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test void consultaUsaLaEmpresaDelContexto() throws Exception {
        when(repo.findByEmpId(3L)).thenReturn(List.of());
        mvc.perform(get("/compras")).andExpect(status().isOk());
        verify(repo).findByEmpId(3L);
        verify(repo, never()).findAllByOrderByEmpIdAscComIdAsc();
    }

    @Test void consultaGlobalUsaElAmbitoAutorizadoPorContexto() throws Exception {
        when(contexto.empresaConsulta(null)).thenReturn(null);
        when(repo.findAllByOrderByEmpIdAscComIdAsc()).thenReturn(List.of());
        mvc.perform(get("/compras")).andExpect(status().isOk());
        verify(repo).findAllByOrderByEmpIdAscComIdAsc();
    }

    @Test void altaIgnoraEmpresaEnviadaEnElCuerpo() throws Exception {
        mvc.perform(post("/compras").contentType(MediaType.APPLICATION_JSON)
                .content("{\"empId\":99,\"detalles\":[{\"proId\":1,\"comDetCan\":2,\"comDetPre\":10,\"comDetDes\":0,\"comDetIva\":21}]}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.empId").value(3));
    }

    @Test void actualizarYEliminarBuscanSoloEnLaEmpresaEfectiva() throws Exception {
        Compra compra = new Compra();
        compra.setComId(7L); compra.setEmpId(3L); compra.setComAct(true);
        when(repo.findByEmpIdAndComId(3L, 7L)).thenReturn(Optional.of(compra));
        mvc.perform(put("/compras/7").contentType(MediaType.APPLICATION_JSON).content("{\"detalles\":[{\"proId\":1,\"comDetCan\":2,\"comDetPre\":10,\"comDetDes\":0,\"comDetIva\":21}]}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.empId").value(3));
        mvc.perform(delete("/compras/7")).andExpect(status().isOk());
        verify(repo, times(2)).findByEmpIdAndComId(3L, 7L);
        verify(repo).delete(compra);
    }
}
