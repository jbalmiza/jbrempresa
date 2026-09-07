package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Perfil;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    @Query("SELECT COALESCE(MAX(p.perId),0) + 1 FROM Perfil p WHERE p.empId=:empId")
    Long obtenerSiguienteId(@org.springframework.data.repository.query.Param("empId") Long empId);

    // Obtiene únicamente los perfiles de un cliente
    List<Perfil> findByEmpId(Long empId);

    // Busca un perfil concreto perteneciente a un cliente
    Optional<Perfil> findByEmpIdAndPerId(
            Long perId,
            Long empId);

}
