package com.jbrempresa.backend.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.dto.administracion.EmpresaRelacionDtos.Opcion;
import com.jbrempresa.backend.dto.ventas.CatalogoDtos;
import com.jbrempresa.backend.entity.EmpresaRelacion;
import com.jbrempresa.backend.repository.EmpresaRelacionRepository;
import com.jbrempresa.backend.repository.EmpresaRepository;
import com.jbrempresa.backend.security.AccesoPerfilService;

@Service
public class CatalogoProveedorService {
    private final ContextoOperacion contexto;
    private final AccesoPerfilService perfiles;
    private final EmpresaRelacionRepository relaciones;
    private final EmpresaRepository empresas;
    private final CatalogoService catalogos;
    private final CompraProveedorService compras;

    public CatalogoProveedorService(ContextoOperacion contexto, AccesoPerfilService perfiles,
            EmpresaRelacionRepository relaciones, EmpresaRepository empresas, CatalogoService catalogos, CompraProveedorService compras) {
        this.contexto=contexto; this.perfiles=perfiles; this.relaciones=relaciones;
        this.empresas=empresas; this.catalogos=catalogos;this.compras=compras;
    }

    private Long empresaCompradora() {
        if (!perfiles.permite(contexto.usuarioActual(), "PROVEEDORES")) denegar();
        if (contexto.administradorGlobal() && contexto.empresaSeleccionada()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Seleccione una empresa para consultar sus proveedores.");
        return contexto.empresaId();
    }

    private boolean vigente(EmpresaRelacion r) {
        LocalDate hoy=LocalDate.now();
        return Boolean.TRUE.equals(r.getActiva()) && "PROVEEDOR".equals(r.getTipo())
            && (r.getFechaInicio()==null || !r.getFechaInicio().isAfter(hoy))
            && (r.getFechaFin()==null || !r.getFechaFin().isBefore(hoy));
    }

    public List<Opcion> proveedores() {
        return relaciones.findByEmpIdAndTipoAndActivaTrueOrderById(empresaCompradora(),"PROVEEDOR")
            .stream().filter(this::vigente).map(r->empresas.findById(r.getEmpresaRelacionadaId())
                .map(e->new Opcion(e.getEmpId(),e.getEmpNom())).orElse(null))
            .filter(java.util.Objects::nonNull).toList();
    }

    public Long autorizar(Long proveedor) {
        Long compradora=empresaCompradora();
        if (compradora.equals(proveedor) || relaciones.findByEmpIdAndEmpresaRelacionadaIdAndTipo(
                compradora,proveedor,"PROVEEDOR").filter(this::vigente).isEmpty()) denegar();
        return proveedor;
    }

    public CatalogoDtos.CatalogoPublico catalogo(Long proveedor) {
        return catalogos.catalogoEmpresa(new CatalogoService.ContextoPublico(autorizar(proveedor),null),
            "/catalogo/proveedores/"+proveedor,"CATALOGO_PROVEEDOR");
    }

    @Transactional
    public CatalogoDtos.PedidoConfirmacion pedir(Long proveedor,CatalogoDtos.PedidoEntrada entrada) {
        Long proveedorId=autorizar(proveedor);
        Long compradora=contexto.empresaId();
        var pedido=catalogos.pedirDocumento(new CatalogoService.ContextoPublico(proveedorId,null),entrada);
        compras.crear(compradora,pedido);
        return new CatalogoDtos.PedidoConfirmacion(pedido.getDovNum(),pedido.getDovImpTot(),pedido.getDovMod(),pedido.getDovUbi());
    }

    private void denegar() { throw new ResponseStatusException(HttpStatus.FORBIDDEN,
        "No tiene una relación de proveedor activa que permita acceder a este catálogo."); }
}
