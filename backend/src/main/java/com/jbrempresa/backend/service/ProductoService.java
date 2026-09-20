package com.jbrempresa.backend.service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.entity.Producto;
import com.jbrempresa.backend.repository.ProductoRepository;
import com.jbrempresa.backend.repository.ProductoComponenteRepository;
import com.jbrempresa.backend.repository.ComponenteRepository;
import com.jbrempresa.backend.entity.ProductoComponente;

@Service
public class ProductoService {
    private final ProductoRepository repository;
    private final MallaService mallaService;
    private final ProductoComponenteRepository componentesProducto;
    private final ComponenteRepository componentes;

    public ProductoService(ProductoRepository repository, MallaService mallaService, ProductoComponenteRepository componentesProducto, ComponenteRepository componentes) {
        this.repository = repository;
        this.mallaService = mallaService;
        this.componentesProducto = componentesProducto;
        this.componentes = componentes;
    }

    public Producto obtener(Long empId, Long id) { return vigente(empId, id); }

    @Transactional
    public Producto guardar(Long empId, String usuario, Producto producto) {
        actualizarTotalCompra(producto);
        if (producto.getProDurMin() == null) producto.setProDurMin(0);
        if (producto.getProVisCat() == null) producto.setProVisCat(true);
        if (producto.getProIma() == null || producto.getProIma().isBlank()) producto.setProIma("producto-predeterminado.png");
        producto.setEmpId(empId);
        producto.setProId(repository.obtenerSiguienteId(empId));
        producto.setProIdHis(1L);
        producto.setProTipMov("A");
        if (producto.getProCauMov() == null || producto.getProCauMov().isBlank()) producto.setProCauMov("Alta del registro");
        producto.setProAct(true);
        producto.setProUsuMov(usuario);
        producto.setProFecMov(LocalDateTime.now());
        Producto guardado = repository.save(producto);
        guardarComponentes(guardado);
        mallaService.guardarDesdeProducto(guardado);
        return guardado;
    }

    @Transactional
    public Producto actualizar(Long empId, String usuario, Producto producto) {
        actualizarTotalCompra(producto);
        if (producto.getProDurMin() == null) producto.setProDurMin(0);
        Producto anterior = vigente(empId, producto.getProId());
        if (producto.getProVisCat() == null) producto.setProVisCat(anterior.getProVisCat());
        if (producto.getProIma() == null) producto.setProIma(anterior.getProIma());
        comprobarModificable(anterior);
        anterior.setProAct(false);
        repository.saveAndFlush(anterior);
        producto.setEmpId(empId);
        producto.setProIdHis(anterior.getProIdHis() + 1);
        producto.setProTipMov("M");
        if (producto.getProCauMov() == null || producto.getProCauMov().isBlank()) producto.setProCauMov("Modificación del registro");
        producto.setProAct(true);
        producto.setProUsuMov(usuario);
        producto.setProFecMov(LocalDateTime.now());
        Producto guardado = repository.save(producto);
        guardarComponentes(guardado);
        mallaService.actualizarDesdeProducto(guardado);
        return guardado;
    }

    public List<Producto> obtenerProductos(Long empId) { return repository.findByEmpIdAndProActTrueOrderByProId(empId).stream().map(this::cargarComponentes).toList(); }
    public List<Producto> obtenerProductosGlobales() { return repository.findByProActTrueOrderByEmpIdAscProIdAsc().stream().map(this::cargarComponentes).toList(); }
    public Long obtenerSiguienteId(Long empId) { return repository.obtenerSiguienteId(empId); }

    @Transactional
    public Producto asociarImagen(Long empId, Long id, String ruta) {
        Producto producto = vigente(empId, id);
        producto.setProIma(ruta);
        return repository.save(producto);
    }

    @Transactional
    public void descontarStock(Long empId, Long id, int cantidad, String usuario) {
        Producto actual = vigente(empId, id);
        if (!Boolean.TRUE.equals(actual.getProConSto())) return;
        int stock = actual.getProStoAct() == null ? 0 : actual.getProStoAct().intValue();
        if (cantidad > stock) conflicto("No hay stock suficiente para " + actual.getProNom() + ".");
        Producto actualizado = new Producto();
        BeanUtils.copyProperties(actual, actualizado);
        actualizado.setProStoAct(stock - cantidad);
        actualizado.setProCauMov("Salida por pedido de catálogo");
        actualizar(empId, usuario, actualizado);
    }

    @Transactional
    public void devolverStock(Long empId, Long id, int cantidad, String usuario) {
        Producto actual = vigente(empId, id);
        if (!Boolean.TRUE.equals(actual.getProConSto())) return;
        Producto actualizado = new Producto();
        BeanUtils.copyProperties(actual, actualizado);
        actualizado.setProStoAct((actual.getProStoAct() == null ? 0 : actual.getProStoAct()) + cantidad);
        actualizado.setProCauMov("Devolución por anulación de pedido de catálogo");
        actualizar(empId, usuario, actualizado);
    }
    public List<Producto> historico(Long empId, Long id) { vigente(empId, id); return repository.findByEmpIdAndProIdOrderByProFecMovDesc(empId, id).stream().map(this::cargarComponentes).toList(); }

    @Transactional
    public void eliminar(Long empId, Long id) {
        vigente(empId, id);
        mallaService.eliminarDesdeProducto(empId, id);
        componentesProducto.deleteByEmpIdAndProId(empId, id);
        repository.deleteAll(repository.findByEmpIdAndProIdOrderByProFecMovDesc(empId, id));
    }

    @Transactional
    public Producto baja(Long empId, Long id, String usuario) {
        Producto anterior = vigente(empId, id);
        comprobarModificable(anterior);
        anterior.setProAct(false);
        repository.saveAndFlush(anterior);
        Producto baja = new Producto();
        BeanUtils.copyProperties(anterior, baja);
        baja.setProIdHis(anterior.getProIdHis() + 1);
        baja.setProTipMov("B");
        baja.setProCauMov("Baja del registro");
        baja.setProAct(true);
        baja.setProUsuMov(usuario);
        baja.setProFecMov(LocalDateTime.now());
        mallaService.eliminarDesdeProducto(empId, id);
        Producto guardado = repository.save(baja);
        guardarComponentes(guardado);
        return guardado;
    }

    @Transactional
    public Producto deshacer(Long empId, Long id) {
        Producto actual = vigente(empId, id);
        if (actual.getProIdHis() <= 1) conflicto("No existen movimientos anteriores para deshacer.");
        Producto anterior = repository.findByEmpIdAndProIdAndProIdHis(empId, id, actual.getProIdHis() - 1)
                .orElseThrow(() -> new RuntimeException("Movimiento anterior no encontrado."));
        componentesProducto.deleteByEmpIdAndProIdAndProIdHis(empId, id, actual.getProIdHis());
        repository.delete(actual);
        repository.flush();
        anterior.setProAct(true);
        Producto restaurado = repository.save(anterior);
        if ("B".equals(actual.getProTipMov())) mallaService.guardarDesdeProducto(restaurado);
        else mallaService.actualizarDesdeProducto(restaurado);
        return cargarComponentes(restaurado);
    }

    private Producto vigente(Long empId, Long id) { return cargarComponentes(repository.findByEmpIdAndProIdAndProActTrue(empId, id).orElseThrow(() -> new RuntimeException("Producto no encontrado."))); }
    private Producto cargarComponentes(Producto producto) { producto.setComponentes(componentesProducto.findByEmpIdAndProIdAndProIdHisOrderByPrcId(producto.getEmpId(),producto.getProId(),producto.getProIdHis())); return producto; }
    private void guardarComponentes(Producto producto) {
        componentesProducto.deleteByEmpIdAndProIdAndProIdHis(producto.getEmpId(),producto.getProId(),producto.getProIdHis());
        if (producto.getComponentes()==null) return;
        java.util.Set<Long> usados=new java.util.HashSet<>();
        for (ProductoComponente linea:producto.getComponentes()) {
            if(linea.getCmpId()==null||linea.getPrcCan()==null||linea.getPrcCan().signum()<=0) conflicto("Seleccione un componente e informe una cantidad mayor que cero.");
            if(!usados.add(linea.getCmpId())) conflicto("Un componente no puede repetirse en el producto.");
            componentes.findByEmpIdAndCmpIdAndCmpActTrue(producto.getEmpId(),linea.getCmpId()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Componente no válido para la empresa."));
            linea.setPrcId(null);linea.setEmpId(producto.getEmpId());linea.setProId(producto.getProId());linea.setProIdHis(producto.getProIdHis());componentesProducto.save(linea);
        }
    }
    private void actualizarTotalCompra(Producto producto) {
        if (producto.getProPreCom() == null) return;
        BigDecimal descuento = producto.getProDesCom() == null ? BigDecimal.ZERO : producto.getProDesCom();
        BigDecimal iva = producto.getProIvaCom() == null ? BigDecimal.ZERO : producto.getProIvaCom();
        producto.setProTotCom(producto.getProPreCom()
                .multiply(BigDecimal.ONE.subtract(descuento.movePointLeft(2)))
                .multiply(BigDecimal.ONE.add(iva.movePointLeft(2)))
                .setScale(2, RoundingMode.HALF_UP));
    }
    private void comprobarModificable(Producto p) { if ("B".equals(p.getProTipMov())) conflicto("El producto está dado de baja. Deshaga primero la baja."); }
    private void conflicto(String mensaje) { throw new ResponseStatusException(HttpStatus.CONFLICT, mensaje); }
}
