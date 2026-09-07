package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Domicilio;
import com.jbrempresa.backend.entity.DomicilioId;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DomicilioRepository extends JpaRepository<Domicilio, DomicilioId> {
	boolean existsByEmpIdAndDomViaId(Long empId, Long domViaId);

    @Query("SELECT COALESCE(MAX(d.domId),0) + 1 FROM Domicilio d WHERE d.empId=:empId")
    Long obtenerSiguienteId(@org.springframework.data.repository.query.Param("empId") Long empId);

    // Obtiene únicamente los domicilios de un cliente
    List<Domicilio> findByEmpIdAndDomActTrueOrderByDomId(Long empId);

    // Busca un domicilio concreto perteneciente a un cliente
    Optional<Domicilio> findByEmpIdAndDomIdAndDomActTrue(Long empId, Long domId);

    List<Domicilio> findByEmpIdAndDomIdOrderByDomFecMovDesc(Long empId, Long domId);

    Optional<Domicilio> findByEmpIdAndDomIdAndDomIdHis(Long empId, Long domId, Long domIdHis);

}
