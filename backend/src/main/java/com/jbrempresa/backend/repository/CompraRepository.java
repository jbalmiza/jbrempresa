package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Compra;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT COALESCE(MAX(c.comId),0) + 1 FROM Compra c WHERE c.empId=:empId")
    Long obtenerSiguienteId(@org.springframework.data.repository.query.Param("empId") Long empId);

    // Obtiene únicamente las ventas de un cliente
    List<Compra> findByEmpId(Long empId);

    // Busca una venta concreta perteneciente a un cliente
    Optional<Compra> findByEmpIdAndComId(
            Long empId,
            Long comId);

}
