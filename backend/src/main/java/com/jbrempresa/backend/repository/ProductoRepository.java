package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Producto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT COALESCE(MAX(p.proId),0) + 1 FROM Producto p")
    Long obtenerSiguienteId();

    // Obtiene únicamente los productos de un cliente
    List<Producto> findByCliId(Long cliId);

    // Busca un producto concreto perteneciente a un cliente
    Optional<Producto> findByCliIdAndProId(
            Long proId,
            Long cliId);

}