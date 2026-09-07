package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Malla;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MallaRepository extends JpaRepository<Malla, Long> {

    @Query("SELECT COALESCE(MAX(m.malId),0) + 1 FROM Malla m WHERE m.empId=:empId")
    Long obtenerSiguienteId(@org.springframework.data.repository.query.Param("empId") Long empId);

    // Obtiene únicamente las mallas de un cliente
    List<Malla> findByEmpId(Long empId);

    // Busca una malla concreta perteneciente a un cliente
    Optional<Malla> findByMalIdAndEmpId(
            Long malId,
            Long empId);
    
 // Busca una posición concreta de la malla
    Optional<Malla> findByEmpIdAndMalEntAndMalFilAndMalCol(
            Long empId,
            String malEnt,
            Integer malFil,
            Integer malCol);

    // Obtiene todos los registros de una entidad (PRODUCTOS, USUARIOS...)
    List<Malla> findByEmpIdAndMalEnt(
            Long empId,
            String malEnt);

    // Busca el registro asociado a una entidad concreta
    Optional<Malla> findByEmpIdAndMalEntAndMalRefId(
            Long empId,
            String malEnt,
            Long malRefId);
    
    void deleteByEmpIdAndMalEntAndMalRefId(
            Long empId,
            String malEnt,
            Long malRefId);

}
