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
            String tokenGeneral, boolean empresaConImagen) {}
    public record ConfiguracionEntrada(boolean publicado, boolean permitirDomicilio) {}
    public record ProductoPublico(String tipo, Long id, String nombre, String descripcion, String categoria,
            String subcategoria, BigDecimal precio, boolean agotado, String imagenUrl) {}
    public record CatalogoPublico(String empresa, String empresaImagenUrl, boolean domicilioPermitido,
            String modalidad, String ubicacion, List<ProductoPublico> productos) {}
    public record LineaPedido(@NotBlank @Pattern(regexp="PRODUCTO|SERVICIO") String tipo, @Positive Long productoId, @Positive Integer cantidad,
            @Size(max=250) String observaciones) {}
    public record PedidoEntrada(@NotBlank @Size(max=100) String nombre,
            @NotBlank @Size(max=30) String telefono,
            @NotBlank @Pattern(regexp="EN_POSICION|DOMICILIO") String modalidad,
            @Size(max=500) String direccionEnvio, @Size(max=500) String observaciones,
            @NotEmpty List<@Valid LineaPedido> lineas) {}
    public record PedidoConfirmacion(String numero, BigDecimal total, String modalidad, String ubicacion) {}
}
