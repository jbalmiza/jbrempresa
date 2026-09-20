package com.jbrempresa.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import com.jbrempresa.backend.entity.Parametro;
import com.jbrempresa.backend.entity.Producto;
import com.jbrempresa.backend.entity.Servicio;
import com.jbrempresa.backend.entity.TipoArticulo;
import com.jbrempresa.backend.repository.ParametroRepository;
import com.jbrempresa.backend.repository.TipoArticuloRepository;

class ImagenCatalogoServiceTest {
    private final ParametroRepository parametros = mock(ParametroRepository.class);
    private final TipoArticuloRepository tipos = mock(TipoArticuloRepository.class);
    private final ImagenService imagenes = mock(ImagenService.class);
    private final ImagenCatalogoService servicio = new ImagenCatalogoService(parametros, tipos, imagenes);

    @Test
    void productoUsaImagenDelTipoPorDefecto() {
        Producto producto = new Producto();
        producto.setProTipPro("PIZZA");
        TipoArticulo tipo = new TipoArticulo();
        tipo.setImagen("pizza.svg");
        Resource recurso = mock(Resource.class);
        when(parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(
                1L, "PRODUCTOS", "IMAGEN_CATALOGO_ORIGEN")).thenReturn(Optional.empty());
        when(tipos.findByEmpIdAndClaseAndNombreIgnoreCaseAndActivoTrue(1L, "PRODUCTO", "PIZZA"))
                .thenReturn(Optional.of(tipo));
        when(imagenes.cargar(1L, "PRODUCTOS", "tipos", "pizza.svg")).thenReturn(recurso);

        assertThat(servicio.producto(1L, producto)).isSameAs(recurso);
        verify(imagenes).cargar(1L, "PRODUCTOS", "tipos", "pizza.svg");
    }

    @Test
    void servicioUsaAdjuntoPrincipalCuandoElParametroIndicaRegistro() {
        Parametro parametro = new Parametro();
        parametro.setParVal("REGISTRO");
        parametro.setParAct(true);
        Servicio registro = new Servicio();
        registro.setSerIma("servicio-7.webp");
        Resource recurso = mock(Resource.class);
        when(parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(
                2L, "SERVICIOS", "IMAGEN_CATALOGO_ORIGEN")).thenReturn(Optional.of(parametro));
        when(imagenes.cargar(2L, "SERVICIOS", "servicios", "servicio-7.webp")).thenReturn(recurso);

        assertThat(servicio.servicio(2L, registro)).isSameAs(recurso);
        verify(imagenes).cargar(2L, "SERVICIOS", "servicios", "servicio-7.webp");
    }

    @Test
    void soloAceptaLosDosOrigenesDefinidos() {
        assertThat(servicio.esOrigenValido("TIPO")).isTrue();
        assertThat(servicio.esOrigenValido("registro")).isTrue();
        assertThat(servicio.esOrigenValido("OTRO")).isFalse();
    }
}
