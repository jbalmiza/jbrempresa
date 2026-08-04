package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Venta;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT COALESCE(MAX(v.venId),0) + 1 FROM Venta v")
    Long obtenerSiguienteId();

    // Obtiene únicamente las ventas de un cliente
    List<Venta> findByCliId(Long cliId);

    // Busca una venta concreta perteneciente a un cliente
    Optional<Venta> findByCliIdAndVenId(
            Long venId,
            Long cliId);

}