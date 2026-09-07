package com.jbrempresa.backend.repository;

import java.util.Optional;

import com.jbrempresa.backend.entity.Empresa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query("SELECT COALESCE(MAX(c.empId),0) + 1 FROM Empresa c")
    Long obtenerSiguienteId();

    // Busca un empresa por su ID
    Optional<Empresa> findByEmpId(
            Long empId);

}
