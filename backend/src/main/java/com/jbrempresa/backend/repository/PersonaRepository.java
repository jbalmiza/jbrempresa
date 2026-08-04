package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Persona;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Query("SELECT COALESCE(MAX(p.perId),0) + 1 FROM Persona p")
    Long obtenerSiguienteId();

    // Obtiene únicamente las personas de un cliente
    List<Persona> findByCliId(Long cliId);

    // Busca una persona concreta perteneciente a un cliente
    Optional<Persona> findByCliIdAndPerId(
            Long perId,
            Long cliId);

}