package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Compra;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT COALESCE(MAX(c.comId),0) + 1 FROM Compra c")
    Long obtenerSiguienteId();

    // Obtiene únicamente las ventas de un cliente
    List<Compra> findByCliId(Long cliId);

    // Busca una venta concreta perteneciente a un cliente
    Optional<Compra> findByCliIdAndComId(
            Long cliId,
            Long comId);

}