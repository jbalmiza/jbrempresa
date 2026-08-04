package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Malla;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MallaRepository extends JpaRepository<Malla, Long> {

    @Query("SELECT COALESCE(MAX(m.malId),0) + 1 FROM Malla m")
    Long obtenerSiguienteId();

    // Obtiene únicamente las mallas de un cliente
    List<Malla> findByCliId(Long cliId);

    // Busca una malla concreta perteneciente a un cliente
    Optional<Malla> findByMalIdAndCliId(
            Long malId,
            Long cliId);
    
 // Busca una posición concreta de la malla
    Optional<Malla> findByCliIdAndMalEntAndMalFilAndMalCol(
            Long cliId,
            String malEnt,
            Integer malFil,
            Integer malCol);

    // Obtiene todos los registros de una entidad (PRODUCTOS, USUARIOS...)
    List<Malla> findByCliIdAndMalEnt(
            Long cliId,
            String malEnt);

    // Busca el registro asociado a una entidad concreta
    Optional<Malla> findByCliIdAndMalEntAndMalRefId(
            Long cliId,
            String malEnt,
            Long malRefId);
    
    void deleteByCliIdAndMalEntAndMalRefId(
            Long cliId,
            String malEnt,
            Long malRefId);

}