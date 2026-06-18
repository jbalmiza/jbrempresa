package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Domicilio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomicilioRepository extends JpaRepository<Domicilio, Long> {

    @Query("SELECT COALESCE(MAX(d.domId),0) + 1 FROM Domicilio d")
    Long obtenerSiguienteId();

    // Obtiene únicamente los domicilios de un cliente
    List<Domicilio> findByCliId(Long cliId);

    // Busca un domicilio concreto perteneciente a un cliente
    Optional<Domicilio> findByDomIdAndCliId(
            Long domId,
            Long cliId);

}