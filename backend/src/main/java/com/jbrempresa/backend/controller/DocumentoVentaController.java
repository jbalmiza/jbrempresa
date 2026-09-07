package com.jbrempresa.backend.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.dto.ventas.DocumentoVentaDtos;
import com.jbrempresa.backend.entity.DocumentoVenta;
import com.jbrempresa.backend.entity.DocumentoVentaDetalle;
import com.jbrempresa.backend.entity.DocumentoVentaMovimiento;
import com.jbrempresa.backend.entity.Persona;
import com.jbrempresa.backend.exception.RecursoNoEncontradoException;
import com.jbrempresa.backend.exception.ReglaNegocioException;
import com.jbrempresa.backend.repository.PersonaRepository;
import com.jbrempresa.backend.repository.DocumentoVentaDetalleRepository;
import com.jbrempresa.backend.repository.DocumentoVentaRepository;
import com.jbrempresa.backend.repository.DocumentoVentaMovimientoRepository;
import com.jbrempresa.backend.repository.ProductoRepository;
import com.jbrempresa.backend.repository.ServicioRepository;
import com.jbrempresa.backend.service.NumeradorDocumentoVentaService;
import com.jbrempresa.backend.service.MallaService;
import com.jbrempresa.backend.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/documentos-venta")
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentoVentaController {
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    private final DocumentoVentaRepository documentos;
    private final DocumentoVentaDetalleRepository lineas;
    private final DocumentoVentaMovimientoRepository movimientos;
    private final PersonaRepository personas;
    private final ProductoRepository productos;
    private final ServicioRepository servicios;
    private final NumeradorDocumentoVentaService numerador;
    private final ContextoOperacion contexto;
    private final MallaService mallaService;
    private final ProductoService productoService;

    public DocumentoVentaController(DocumentoVentaRepository documentos,
            DocumentoVentaDetalleRepository lineas, DocumentoVentaMovimientoRepository movimientos, PersonaRepository personas,
            ProductoRepository productos, ServicioRepository servicios,
            NumeradorDocumentoVentaService numerador, ContextoOperacion contexto, MallaService mallaService,
            ProductoService productoService) {
        this.documentos = documentos;
        this.lineas = lineas;
        this.movimientos = movimientos;
        this.personas = personas;
        this.productos = productos;
        this.servicios = servicios;
        this.numerador = numerador;
        this.contexto = contexto;
        this.mallaService = mallaService;
        this.productoService = productoService;
    }

    @GetMapping("/{tipo}")
    public List<DocumentoVentaDtos.Salida> consultar(@PathVariable String tipo,
            @RequestParam(defaultValue = "false") boolean incluirBajas) {
        List<DocumentoVenta> resultado = incluirBajas
                ? documentos.findByEmpIdAndDovTipOrderByDovFecDescDovIdDesc(contexto.empresaId(), tipo(tipo))
                : documentos.findByEmpIdAndDovTipAndDovActTrueOrderByDovFecDescDovIdDesc(contexto.empresaId(), tipo(tipo));
        return resultado
                .stream().map(this::cargar).map(DocumentoVentaDtos.Salida::desde).toList();
    }

    @PostMapping("/{tipo}")
    @Transactional
    public DocumentoVentaDtos.Salida guardar(@PathVariable String tipo,
            @Valid @RequestBody DocumentoVentaDtos.Entrada entrada) {
        DocumentoVenta documento = entrada.entidad();
        documento.setDovTip(tipo(tipo));
        prepararNuevo(documento);
        validar(documento);
        normalizarLineasYTotales(documento);
        DocumentoVenta guardado = documentos.save(documento);
        guardado.setDovIdRai(guardado.getDovId());
        guardado = documentos.save(guardado);
        if ("PED".equals(guardado.getDovTip())) mallaService.sincronizarPedido(guardado);
        guardado.setDetalles(guardarLineas(guardado, documento.getDetalles()));
        registrarMovimiento(guardado, "ALTA", "Alta del documento");
        return DocumentoVentaDtos.Salida.desde(guardado);
    }

    @PostMapping("/venta-rapida")
    @Transactional
    public DocumentoVentaDtos.Salida ventaRapida(@Valid @RequestBody DocumentoVentaDtos.VentaRapidaEntrada entrada) {
        Persona cliente = clienteGenerico();
        DocumentoVenta documento = new DocumentoVenta();
        documento.setDovTip("PED");
        documento.setPerId(cliente.getPerId());
        documento.setDovFec(LocalDate.now());
        documento.setDovEst("EMITIDO");
        documento.setDovOri("INTERNO");
        documento.setDovObs(entrada.observaciones());
        documento.setDetalles(entrada.detalles().stream().map(detalle -> detalle.entidad()).toList());
        prepararNuevo(documento);
        validar(documento);
        normalizarLineasYTotales(documento);
        DocumentoVenta guardado = documentos.save(documento);
        guardado.setDovIdRai(guardado.getDovId());
        guardado = documentos.save(guardado);
        mallaService.sincronizarPedido(guardado);
        guardado.setDetalles(guardarLineas(guardado, documento.getDetalles()));
        registrarMovimiento(guardado, "ALTA", "Venta rápida a cliente genérico");
        return DocumentoVentaDtos.Salida.desde(guardado);
    }

    @PutMapping("/{tipo}/{id}")
    @Transactional
    public DocumentoVentaDtos.Salida actualizar(@PathVariable String tipo, @PathVariable Long id,
            @Valid @RequestBody DocumentoVentaDtos.Entrada entrada) {
        DocumentoVenta actual = obtener(id);
        if (!actual.getDovTip().equals(tipo(tipo))) throw negocio("Documento no válido.");
        if (documentos.existsByEmpIdAndDovIdOri(contexto.empresaId(), id))
            throw negocio("No se puede modificar un documento que ya fue convertido.");
        DocumentoVenta documento = entrada.entidad();
        documento.setDovId(id);
        documento.setEmpId(contexto.empresaId());
        documento.setDovTip(actual.getDovTip());
        documento.setDovNum(actual.getDovNum());
        documento.setDovIdOri(actual.getDovIdOri());
        documento.setDovIdRai(actual.getDovIdRai());
        if (documento.getDovOri() == null) documento.setDovOri(actual.getDovOri());
        if (documento.getDovMod() == null) documento.setDovMod(actual.getDovMod());
        if (!"PED".equals(actual.getDovTip())) {
            documento.setDovUbi(actual.getDovUbi());
            documento.setDovFilMal(actual.getDovFilMal());
            documento.setDovColMal(actual.getDovColMal());
        }
        documento.setDovUsuMov(contexto.nombreUsuario());
        documento.setDovFecMov(contexto.fechaActual());
        documento.setDovAct(actual.getDovAct());
        validar(documento);
        normalizarLineasYTotales(documento);
        lineas.deleteByEmpIdAndDovId(contexto.empresaId(), id);
        DocumentoVenta guardado = documentos.save(documento);
        if ("PED".equals(guardado.getDovTip())) mallaService.sincronizarPedido(guardado);
        if ("PAGADO".equals(guardado.getDovEst())) liberarPosicionCadena(guardado);
        guardado.setDetalles(guardarLineas(guardado, documento.getDetalles()));
        registrarMovimiento(guardado, "MODIFICACION", "Modificación del documento");
        return DocumentoVentaDtos.Salida.desde(guardado);
    }

    @PostMapping("/{tipo}/{id}/baja")
    @Transactional
    public DocumentoVentaDtos.Salida baja(@PathVariable String tipo, @PathVariable Long id) {
        DocumentoVenta documento = obtener(id);
        if (!documento.getDovTip().equals(tipo(tipo))) throw negocio("Documento no válido.");
        if (!Boolean.TRUE.equals(documento.getDovAct())) throw negocio("El documento ya está de baja.");
        devolverStockCatalogo(documento);
        documento.setDovAct(false);
        documento.setDovUsuMov(contexto.nombreUsuario());
        documento.setDovFecMov(contexto.fechaActual());
        DocumentoVenta guardado = cargar(documentos.save(documento));
        registrarMovimiento(guardado, "BAJA", "Baja del documento");
        return DocumentoVentaDtos.Salida.desde(guardado);
    }

    @PostMapping("/{tipo}/{id}/reactivar")
    @Transactional
    public DocumentoVentaDtos.Salida reactivar(@PathVariable String tipo, @PathVariable Long id) {
        DocumentoVenta documento = obtener(id);
        if (!documento.getDovTip().equals(tipo(tipo))) throw negocio("Documento no válido.");
        if (Boolean.TRUE.equals(documento.getDovAct())) throw negocio("El documento ya está activo.");
        descontarStockCatalogo(documento);
        documento.setDovAct(true);
        documento.setDovUsuMov(contexto.nombreUsuario());
        documento.setDovFecMov(contexto.fechaActual());
        DocumentoVenta guardado = cargar(documentos.save(documento));
        if ("PED".equals(guardado.getDovTip())) mallaService.sincronizarPedido(guardado);
        registrarMovimiento(guardado, "REACTIVACION", "Reactivación del documento");
        return DocumentoVentaDtos.Salida.desde(guardado);
    }

    @GetMapping("/{tipo}/{id}/historico")
    public List<DocumentoVentaMovimiento> historico(@PathVariable String tipo, @PathVariable Long id) {
        DocumentoVenta documento = obtener(id);
        if (!documento.getDovTip().equals(tipo(tipo))) throw negocio("Documento no válido.");
        return movimientos.findByEmpIdAndDovIdOrderByDvmFecMovDescDvmIdDesc(contexto.empresaId(), id);
    }

    @DeleteMapping("/{tipo}/{id}")
    @Transactional
    public void eliminarActual(@PathVariable String tipo, @PathVariable Long id) {
        DocumentoVenta documento = obtener(id);
        if (!documento.getDovTip().equals(tipo(tipo))) throw negocio("Documento no válido.");
        if (documentos.existsByEmpIdAndDovIdOri(contexto.empresaId(), id))
            throw negocio("El documento tiene un sucesor. Use Eliminar completo.");
        devolverStockCatalogo(documento);
        lineas.deleteByEmpIdAndDovId(contexto.empresaId(), id);
        movimientos.deleteByEmpIdAndDovId(contexto.empresaId(), id);
        if ("PED".equals(documento.getDovTip())) mallaService.eliminarDesdePedido(contexto.empresaId(), id);
        documentos.delete(documento);
    }

    @DeleteMapping("/{tipo}/{id}/completo")
    @Transactional
    public void eliminarCompleto(@PathVariable String tipo, @PathVariable Long id) {
        DocumentoVenta documento = obtener(id);
        if (!documento.getDovTip().equals(tipo(tipo))) throw negocio("Documento no válido.");
        Long raiz = documento.getDovIdRai() == null ? documento.getDovId() : documento.getDovIdRai();
        List<DocumentoVenta> cadena = documentos.findByEmpIdAndDovIdRai(contexto.empresaId(), raiz);
        cadena.stream().filter(d -> "PED".equals(d.getDovTip())).findFirst().ifPresent(this::devolverStockCatalogo);
        cadena.stream().filter(d -> "PED".equals(d.getDovTip())).findFirst()
                .ifPresent(d -> mallaService.eliminarDesdePedido(contexto.empresaId(), d.getDovId()));
        cadena.forEach(d -> lineas.deleteByEmpIdAndDovId(contexto.empresaId(), d.getDovId()));
        cadena.forEach(d -> movimientos.deleteByEmpIdAndDovId(contexto.empresaId(), d.getDovId()));
        documentos.deleteAll(cadena);
    }

    @PostMapping("/{tipo}/{id}/convertir")
    @Transactional
    public DocumentoVentaDtos.Salida convertir(@PathVariable String tipo, @PathVariable Long id) {
        DocumentoVenta origen = cargar(obtener(id));
        String actual = tipo(tipo);
        if (!origen.getDovTip().equals(actual)) throw negocio("Documento no válido.");
        String siguiente = "PRE".equals(actual) ? "PED" : "PED".equals(actual) ? "ALB"
                : "ALB".equals(actual) ? "FAC" : null;
        if (siguiente == null) throw negocio("La factura es el último documento de la cadena comercial.");
        if (documentos.existsByEmpIdAndDovIdOri(contexto.empresaId(), id))
            throw negocio("El documento ya fue convertido.");

        DocumentoVenta destino = new DocumentoVenta();
        destino.setDovTip(siguiente);
        destino.setPerId(origen.getPerId());
        destino.setDovFec(LocalDate.now());
        destino.setDovEst("BORRADOR");
        destino.setDovIdOri(origen.getDovId());
        destino.setDovIdRai(origen.getDovIdRai());
        destino.setDovUbi(origen.getDovUbi());
        destino.setDovOri(origen.getDovOri());
        destino.setDovMod(origen.getDovMod());
        destino.setDovDirEnv(origen.getDovDirEnv());
        destino.setDovFilMal(origen.getDovFilMal());
        destino.setDovColMal(origen.getDovColMal());
        destino.setDovImpSub(origen.getDovImpSub());
        destino.setDovImpDes(origen.getDovImpDes());
        destino.setDovImpIva(origen.getDovImpIva());
        destino.setDovImpTot(origen.getDovImpTot());
        destino.setDovObs(origen.getDovObs());
        prepararNuevo(destino);
        DocumentoVenta guardado = documentos.save(destino);
        guardado.setDetalles(copiarLineas(guardado, origen.getDetalles()));
        origen.setDovEst("CONVERTIDO");
        origen.setDovUsuMov(contexto.nombreUsuario());
        origen.setDovFecMov(contexto.fechaActual());
        documentos.save(origen);
        registrarMovimiento(origen, "CONVERSION", "Conversión a " + siguiente);
        registrarMovimiento(guardado, "ALTA_CONVERSION", "Creado desde " + origen.getDovNum());
        return DocumentoVentaDtos.Salida.desde(guardado);
    }

    private void prepararNuevo(DocumentoVenta documento) {
        documento.setDovId(null);
        documento.setEmpId(contexto.empresaId());
        if (documento.getDovFec() == null) documento.setDovFec(LocalDate.now());
        if (vacio(documento.getDovEst())) documento.setDovEst("BORRADOR");
        documento.setDovNum(numerador.siguiente(contexto.empresaId(), documento.getDovTip()));
        documento.setDovUsuMov(contexto.nombreUsuario());
        documento.setDovFecMov(contexto.fechaActual());
        documento.setDovAct(true);
        if (vacio(documento.getDovOri())) documento.setDovOri("INTERNO");
    }

    private Persona clienteGenerico() {
        return personas.buscarCoincidenciasExactas(contexto.empresaId(), "CLIENTE-GENERICO", "", "").stream()
                .findFirst().orElseGet(() -> {
                    Persona p = new Persona();
                    p.setEmpId(contexto.empresaId());
                    p.setPerId(personas.obtenerSiguienteId(contexto.empresaId()));
                    p.setPerIdHis(1L);
                    p.setPerTipMov("A");
                    p.setPerCauMov("Alta automática para venta rápida");
                    p.setPerTipPer("FISICA");
                    p.setPerTipDoc("OTRO");
                    p.setPerDoc("CLIENTE-GENERICO");
                    p.setPerNom("Cliente");
                    p.setPerApe1("Genérico");
                    p.setPerNomCom("CLIENTE-GENERICO - Cliente Genérico");
                    p.setPerAct(true);
                    p.setPerUsuMov(contexto.nombreUsuario());
                    p.setPerFecMov(contexto.fechaActual());
                    return personas.save(p);
                });
    }

    private void validar(DocumentoVenta documento) {
        if ("PED".equals(documento.getDovTip()) && "DOMICILIO".equals(documento.getDovMod())
                && vacio(documento.getDovDirEnv())) throw negocio("La dirección de envío es obligatoria.");
        if ((documento.getDovFilMal() == null) != (documento.getDovColMal() == null))
            throw negocio("La fila y la columna de la malla deben informarse juntas.");
        personas.findByEmpIdAndPerIdAndPerActTrue(contexto.empresaId(), documento.getPerId())
                .filter(p -> !"B".equals(p.getPerTipMov()))
                .orElseThrow(() -> negocio("Persona cliente no válida."));
        if (documento.getDetalles() == null || documento.getDetalles().isEmpty())
            throw negocio("El documento debe incluir al menos una línea.");
    }

    private void normalizarLineasYTotales(DocumentoVenta documento) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal descuento = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        for (DocumentoVentaDetalle linea : documento.getDetalles()) {
            normalizarLinea(linea);
            BigDecimal base = linea.getDvdPre().multiply(BigDecimal.valueOf(linea.getDvdCan()));
            BigDecimal importeDescuento = base.multiply(linea.getDvdDes()).divide(CIEN, 4, RoundingMode.HALF_UP);
            BigDecimal neto = base.subtract(importeDescuento);
            BigDecimal importeIva = neto.multiply(linea.getDvdIva()).divide(CIEN, 4, RoundingMode.HALF_UP);
            BigDecimal importe = neto.add(importeIva).setScale(2, RoundingMode.HALF_UP);
            linea.setDvdImp(importe);
            subtotal = subtotal.add(base);
            descuento = descuento.add(importeDescuento);
            iva = iva.add(importeIva);
            total = total.add(importe);
        }
        documento.setDovImpSub(subtotal.setScale(2, RoundingMode.HALF_UP));
        documento.setDovImpDes(descuento.setScale(2, RoundingMode.HALF_UP));
        documento.setDovImpIva(iva.setScale(2, RoundingMode.HALF_UP));
        documento.setDovImpTot(total.setScale(2, RoundingMode.HALF_UP));
    }

    private void normalizarLinea(DocumentoVentaDetalle linea) {
        if ("P".equals(linea.getDvdTipLin())) {
            var producto = productos.findByEmpIdAndProIdAndProActTrue(contexto.empresaId(), linea.getProId())
                    .filter(p -> !"B".equals(p.getProTipMov()))
                    .orElseThrow(() -> negocio("Producto no válido."));
            linea.setSerId(null);
            linea.setDvdNom(producto.getProNom());
            linea.setDvdDurUni(producto.getProDurMin() == null ? 0 : producto.getProDurMin());
        } else if ("S".equals(linea.getDvdTipLin())) {
            var servicio = servicios.findByEmpIdAndSerIdAndSerActTrue(contexto.empresaId(), linea.getSerId())
                    .filter(s -> !"B".equals(s.getSerTipMov()))
                    .orElseThrow(() -> negocio("Servicio no válido."));
            linea.setProId(null);
            linea.setDvdNom(servicio.getSerNom());
            linea.setDvdDurUni(servicio.getSerDurMin());
        } else {
            throw negocio("Tipo de línea no válido.");
        }
        linea.setDvdDurTot(linea.getDvdDurUni() * linea.getDvdCan());
    }

    private List<DocumentoVentaDetalle> guardarLineas(DocumentoVenta documento,
            List<DocumentoVentaDetalle> entrada) {
        return entrada.stream().map(linea -> {
            linea.setDvdId(null);
            prepararLinea(linea, documento.getDovId());
            return lineas.save(linea);
        }).toList();
    }

    private List<DocumentoVentaDetalle> copiarLineas(DocumentoVenta documento,
            List<DocumentoVentaDetalle> entrada) {
        return entrada.stream().map(linea -> {
            DocumentoVentaDetalle copia = new DocumentoVentaDetalle();
            prepararLinea(copia, documento.getDovId());
            copia.setDvdTipLin(linea.getDvdTipLin());
            copia.setProId(linea.getProId());
            copia.setSerId(linea.getSerId());
            copia.setDvdNom(linea.getDvdNom());
            copia.setDvdObs(linea.getDvdObs());
            copia.setDvdCan(linea.getDvdCan());
            copia.setDvdPre(linea.getDvdPre());
            copia.setDvdDes(linea.getDvdDes());
            copia.setDvdIva(linea.getDvdIva());
            copia.setDvdImp(linea.getDvdImp());
            copia.setDvdDurUni(linea.getDvdDurUni());
            copia.setDvdDurTot(linea.getDvdDurTot());
            return lineas.save(copia);
        }).toList();
    }

    private void prepararLinea(DocumentoVentaDetalle linea, Long documentoId) {
        linea.setEmpId(contexto.empresaId());
        linea.setDovId(documentoId);
        linea.setDvdUsuMov(contexto.nombreUsuario());
        linea.setDvdFecMov(contexto.fechaActual());
        linea.setDvdAct(true);
    }

    private DocumentoVenta obtener(Long id) {
        return documentos.findByEmpIdAndDovId(contexto.empresaId(), id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Documento no encontrado."));
    }

    private DocumentoVenta cargar(DocumentoVenta documento) {
        documento.setDetalles(lineas.findByEmpIdAndDovIdOrderByDvdId(contexto.empresaId(), documento.getDovId()));
        return documento;
    }

    private void registrarMovimiento(DocumentoVenta documento, String tipoMovimiento, String causa) {
        DocumentoVentaMovimiento movimiento = new DocumentoVentaMovimiento();
        movimiento.setEmpId(documento.getEmpId()); movimiento.setDovId(documento.getDovId());
        movimiento.setDovTip(documento.getDovTip()); movimiento.setDovNum(documento.getDovNum());
        movimiento.setPerId(documento.getPerId()); movimiento.setDovFec(documento.getDovFec());
        movimiento.setDovEst(documento.getDovEst()); movimiento.setDovImpTot(documento.getDovImpTot());
        movimiento.setDovUbi(documento.getDovUbi());
        movimiento.setDovOri(documento.getDovOri()); movimiento.setDovMod(documento.getDovMod());
        movimiento.setDovDirEnv(documento.getDovDirEnv());
        movimiento.setDovFilMal(documento.getDovFilMal()); movimiento.setDovColMal(documento.getDovColMal());
        movimiento.setDvmTipMov(tipoMovimiento); movimiento.setDvmCauMov(causa);
        movimiento.setDvmUsuMov(contexto.nombreUsuario()); movimiento.setDvmFecMov(contexto.fechaActual());
        movimientos.save(movimiento);
    }

    private void liberarPosicionCadena(DocumentoVenta documento) {
        Long raiz = documento.getDovIdRai() == null ? documento.getDovId() : documento.getDovIdRai();
        documentos.findByEmpIdAndDovIdRai(contexto.empresaId(), raiz).stream()
                .filter(d -> "PED".equals(d.getDovTip())).findFirst()
                .ifPresent(pedido -> mallaService.eliminarDesdePedido(contexto.empresaId(), pedido.getDovId()));
    }

    private void devolverStockCatalogo(DocumentoVenta documento) {
        if (!"PED".equals(documento.getDovTip()) || !"CATALOGO".equals(documento.getDovOri())) return;
        lineas.findByEmpIdAndDovIdOrderByDvdId(contexto.empresaId(), documento.getDovId()).stream()
                .filter(l -> "P".equals(l.getDvdTipLin()) && l.getProId() != null)
                .forEach(l -> productoService.devolverStock(contexto.empresaId(), l.getProId(),
                        l.getDvdCan(), contexto.nombreUsuario()));
        mallaService.eliminarDesdePedido(contexto.empresaId(), documento.getDovId());
    }

    private void descontarStockCatalogo(DocumentoVenta documento) {
        if (!"PED".equals(documento.getDovTip()) || !"CATALOGO".equals(documento.getDovOri())) return;
        lineas.findByEmpIdAndDovIdOrderByDvdId(contexto.empresaId(), documento.getDovId()).stream()
                .filter(l -> "P".equals(l.getDvdTipLin()) && l.getProId() != null)
                .forEach(l -> productoService.descontarStock(contexto.empresaId(), l.getProId(),
                        l.getDvdCan(), contexto.nombreUsuario()));
    }

    private String tipo(String valor) {
        String normalizado = valor.toUpperCase();
        if (!List.of("PRE", "PED", "ALB", "FAC").contains(normalizado))
            throw negocio("Tipo documental no válido.");
        return normalizado;
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private ReglaNegocioException negocio(String mensaje) {
        return new ReglaNegocioException("DOCUMENTO_VENTA", mensaje);
    }
}
