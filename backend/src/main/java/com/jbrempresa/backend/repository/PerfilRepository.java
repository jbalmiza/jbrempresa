package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Perfil;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    @Query("SELECT COALESCE(MAX(p.perId),0) + 1 FROM Perfil p")
    Long obtenerSiguienteId();

    // Obtiene únicamente los perfiles de un cliente
    List<Perfil> findByCliId(Long cliId);

    // Busca un perfil concreto perteneciente a un cliente
    Optional<Perfil> findByCliIdAndPerId(
            Long perId,
            Long cliId);

}