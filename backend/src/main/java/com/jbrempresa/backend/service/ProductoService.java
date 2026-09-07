package com.jbrempresa.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.entity.Producto;
import com.jbrempresa.backend.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository repository;
    private final MallaService mallaService;

    public ProductoService(ProductoRepository repository, MallaService mallaService) {
        this.repository = repository;
        this.mallaService = mallaService;
    }

    public Producto obtener(Long empId, Long id) { return vigente(empId, id); }

    @Transactional
    public Producto guardar(Long empId, String usuario, Producto producto) {
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
        mallaService.guardarDesdeProducto(guardado);
        return guardado;
    }

    @Transactional
    public Producto actualizar(Long empId, String usuario, Producto producto) {
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
        mallaService.actualizarDesdeProducto(guardado);
        return guardado;
    }

    public List<Producto> obtenerProductos(Long empId) { return repository.findByEmpIdAndProActTrueOrderByProId(empId); }
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
    public List<Producto> historico(Long empId, Long id) { vigente(empId, id); return repository.findByEmpIdAndProIdOrderByProFecMovDesc(empId, id); }

    @Transactional
    public void eliminar(Long empId, Long id) {
        vigente(empId, id);
        mallaService.eliminarDesdeProducto(empId, id);
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
        return repository.save(baja);
    }

    @Transactional
    public Producto deshacer(Long empId, Long id) {
        Producto actual = vigente(empId, id);
        if (actual.getProIdHis() <= 1) conflicto("No existen movimientos anteriores para deshacer.");
        Producto anterior = repository.findByEmpIdAndProIdAndProIdHis(empId, id, actual.getProIdHis() - 1)
                .orElseThrow(() -> new RuntimeException("Movimiento anterior no encontrado."));
        repository.delete(actual);
        repository.flush();
        anterior.setProAct(true);
        Producto restaurado = repository.save(anterior);
        if ("B".equals(actual.getProTipMov())) mallaService.guardarDesdeProducto(restaurado);
        else mallaService.actualizarDesdeProducto(restaurado);
        return restaurado;
    }

    private Producto vigente(Long empId, Long id) { return repository.findByEmpIdAndProIdAndProActTrue(empId, id).orElseThrow(() -> new RuntimeException("Producto no encontrado.")); }
    private void comprobarModificable(Producto p) { if ("B".equals(p.getProTipMov())) conflicto("El producto está dado de baja. Deshaga primero la baja."); }
    private void conflicto(String mensaje) { throw new ResponseStatusException(HttpStatus.CONFLICT, mensaje); }
}
