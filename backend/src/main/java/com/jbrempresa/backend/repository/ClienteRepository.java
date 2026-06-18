package com.jbrempresa.backend.repository;

import java.util.Optional;

import com.jbrempresa.backend.entity.Cliente;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT COALESCE(MAX(c.cliId),0) + 1 FROM Cliente c")
    Long obtenerSiguienteId();

    // Busca un cliente por su ID
    Optional<Cliente> findByCliId(
            Long cliId);

}