package com.jbrempresa.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jbrempresa.backend.entity.Producto;
import com.jbrempresa.backend.entity.Servicio;
import com.jbrempresa.backend.repository.ParametroRepository;
import com.jbrempresa.backend.repository.TipoArticuloRepository;

@Service
public class ImagenCatalogoService {
    public static final String CODIGO_ORIGEN = "IMAGEN_CATALOGO_ORIGEN";
    public static final String ORIGEN_TIPO = "TIPO";
    public static final String ORIGEN_REGISTRO = "REGISTRO";
    public static final String MODULO_PRODUCTOS = "PRODUCTOS";
    public static final String MODULO_SERVICIOS = "SERVICIOS";

    private final ParametroRepository parametros;
    private final TipoArticuloRepository tipos;
    private final ImagenService imagenes;

    public ImagenCatalogoService(ParametroRepository parametros, TipoArticuloRepository tipos,
            ImagenService imagenes) {
        this.parametros = parametros;
        this.tipos = tipos;
        this.imagenes = imagenes;
    }

    public Resource producto(Long empresaId, Producto producto) {
        if (usaImagenRegistro(empresaId, MODULO_PRODUCTOS)) {
            return imagenes.cargar(empresaId, MODULO_PRODUCTOS, "productos", producto.getProIma());
        }
        var tipo = tipos.findByEmpIdAndClaseAndNombreIgnoreCaseAndActivoTrue(
                empresaId, "PRODUCTO", producto.getProTipPro())
                .filter(valor -> valor.getImagen() != null && !valor.getImagen().isBlank())
                .orElseThrow(() -> noDisponible("tipo de producto"));
        return imagenes.cargar(empresaId, MODULO_PRODUCTOS, "tipos", tipo.getImagen());
    }

    public Resource servicio(Long empresaId, Servicio servicio) {
        if (usaImagenRegistro(empresaId, MODULO_SERVICIOS)) {
            return imagenes.cargar(empresaId, MODULO_SERVICIOS, "servicios", servicio.getSerIma());
        }
        var tipo = tipos.findByEmpIdAndClaseAndNombreIgnoreCaseAndActivoTrue(
                empresaId, "SERVICIO", servicio.getSerTipSer())
                .filter(valor -> valor.getImagen() != null && !valor.getImagen().isBlank())
                .orElseThrow(() -> noDisponible("tipo de servicio"));
        return imagenes.cargar(empresaId, MODULO_SERVICIOS, "tipos", tipo.getImagen());
    }

    public boolean esParametroOrigen(String modulo, String codigo) {
        return modulo != null && codigo != null
                && (MODULO_PRODUCTOS.equalsIgnoreCase(modulo.trim())
                    || MODULO_SERVICIOS.equalsIgnoreCase(modulo.trim()))
                && CODIGO_ORIGEN.equalsIgnoreCase(codigo.trim());
    }

    public boolean esOrigenValido(String valor) {
        return valor != null && (ORIGEN_TIPO.equalsIgnoreCase(valor.trim())
                || ORIGEN_REGISTRO.equalsIgnoreCase(valor.trim()));
    }

    private boolean usaImagenRegistro(Long empresaId, String modulo) {
        return parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(empresaId, modulo, CODIGO_ORIGEN)
                .filter(parametro -> Boolean.TRUE.equals(parametro.getParAct()))
                .map(parametro -> ORIGEN_REGISTRO.equalsIgnoreCase(parametro.getParVal().trim()))
                .orElse(false);
    }

    private ResponseStatusException noDisponible(String origen) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No hay una imagen principal disponible para el " + origen + ".");
    }
}
