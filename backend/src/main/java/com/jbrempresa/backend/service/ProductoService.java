package com.jbrempresa.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbrempresa.backend.entity.Producto;
import com.jbrempresa.backend.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MallaService mallaService;

    /**
     * Guarda un nuevo producto.
     */
    public Producto guardar(
            Long cliId,
            Producto producto) {

        // Comprueba que el producto pertenece al cliente
        if (!cliId.equals(producto.getCliId())) {
            throw new RuntimeException("El cliente del producto no coincide con el cliente de la operación.");
        }

        // Fecha del servidor
        producto.setProFecMov(LocalDateTime.now());

        // Guarda el producto
        producto = productoRepository.save(producto);

        // Guarda la posición en la malla
        mallaService.guardarDesdeProducto(producto);

        return producto;
    }

    /**
     * Actualiza un producto existente.
     */
    public Producto actualizar(
            Long cliId,
            Producto producto) {

        // Comprueba que el producto pertenece al cliente
        if (!cliId.equals(producto.getCliId())) {
            throw new RuntimeException("El cliente del producto no coincide con el cliente de la operación.");
        }

        // Comprueba que el producto existe para ese cliente
        productoRepository.findByCliIdAndProId(
                cliId,
                producto.getProId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        // Fecha del servidor
        producto.setProFecMov(LocalDateTime.now());

        // Guarda los cambios
        producto = productoRepository.save(producto);

        // Actualiza la posición en la malla
        mallaService.actualizarDesdeProducto(producto);

        return producto;
    }

    /**
     * Obtiene todos los productos de un cliente.
     */
    public List<Producto> obtenerProductos(Long cliId) {

        return productoRepository.findByCliId(cliId);
    }

    /**
     * Obtiene el siguiente identificador.
     */
    public Long obtenerSiguienteId() {

        return productoRepository.obtenerSiguienteId();
    }

    /**
     * Elimina un producto.
     */
    public void eliminar(
            Long cliId,
            Long proId) {

        Producto producto = productoRepository
                .findByCliIdAndProId(cliId, proId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        // Elimina primero la malla asociada
        mallaService.eliminarDesdeProducto(
                cliId,
                proId);

        // Elimina el producto
        productoRepository.delete(producto);
    }

}