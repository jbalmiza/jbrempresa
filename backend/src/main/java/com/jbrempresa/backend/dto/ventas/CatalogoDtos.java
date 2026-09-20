package com.jbrempresa.backend.dto.ventas;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public final class CatalogoDtos {
    private CatalogoDtos() {}
    public record Configuracion(boolean publicado, boolean permitirDomicilio,
            String tokenGeneral, String alias, boolean empresaConImagen) {}
    public record ConfiguracionEntrada(boolean publicado, boolean permitirDomicilio,
            @NotBlank @Pattern(regexp="[a-z0-9]+(?:-[a-z0-9]+)*") @Size(max=80) String alias) {}
    public record ProductoPublico(String tipo, Long id, String nombre, String descripcion, String categoria,
            String subcategoria, BigDecimal precio, boolean agotado, boolean novedad, boolean mejorPrecio,
            boolean outlet, String imagenUrl, List<String> alergenos, List<Long> componentes) {}
    public record ComponentePublico(Long id, String nombre, BigDecimal precioAdicional, List<String> alergenos) {}
    public record AvisoPublico(String tipo, String titulo, String mensaje, String emisor) {}
    public record CatalogoPublico(String empresa, String empresaImagenUrl, boolean domicilioPermitido,
            String modalidad, String ubicacion, List<AvisoPublico> avisos, List<ProductoPublico> productos,
            List<ComponentePublico> componentes) {}
    public record LineaPedido(@NotBlank @Pattern(regexp="PRODUCTO|SERVICIO") String tipo, @Positive Long productoId, @Positive Integer cantidad,
            @Size(max=2000) String observaciones, List<Long> componentes) {}
    public record PedidoEntrada(@NotBlank @Size(max=100) String nombre,
            @NotBlank @Size(max=30) String telefono,
            @Size(max=150) String correo,
            @NotBlank @Pattern(regexp="EN_POSICION|DOMICILIO") String modalidad,
            @Size(max=500) String direccionEnvio, @Size(max=500) String observaciones,
            @NotEmpty List<@Valid LineaPedido> lineas) {}
    public record PedidoConfirmacion(String numero, BigDecimal total, String modalidad, String ubicacion) {}
}
