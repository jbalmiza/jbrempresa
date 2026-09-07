package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Persona;
import com.jbrempresa.backend.entity.PersonaId;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PersonaRepository extends JpaRepository<Persona, PersonaId> {

    @Query("SELECT COALESCE(MAX(p.perId),0) + 1 FROM Persona p WHERE p.empId=:empId")
    Long obtenerSiguienteId(@org.springframework.data.repository.query.Param("empId") Long empId);

    // Obtiene únicamente las personas de un cliente
    List<Persona> findByEmpIdAndPerActTrueOrderByPerId(Long empId);

    // Busca una persona concreta perteneciente a un cliente
    Optional<Persona> findByEmpIdAndPerIdAndPerActTrue(Long empId, Long perId);

    List<Persona> findByEmpIdAndPerIdOrderByPerFecMovDesc(Long empId, Long perId);

    Optional<Persona> findByEmpIdAndPerIdAndPerIdHis(Long empId, Long perId, Long perIdHis);

    @Query(value = """
            SELECT * FROM personas p
            WHERE p.emp_id=:empId AND p.per_act=TRUE AND p.per_tip_mov<>'B'
              AND ((:dni<>'' AND UPPER(TRIM(COALESCE(p.per_doc,'')))=UPPER(TRIM(:dni)))
                OR (:telefono<>'' AND REGEXP_REPLACE(COALESCE(p.per_tel,''),'[^0-9]','','g')=REGEXP_REPLACE(:telefono,'[^0-9]','','g'))
                OR (:correo<>'' AND LOWER(TRIM(COALESCE(p.per_ema,'')))=LOWER(TRIM(:correo))))
            ORDER BY p.per_id
            """, nativeQuery = true)
    List<Persona> buscarCoincidenciasExactas(@Param("empId") Long empId,@Param("dni") String dni,@Param("telefono") String telefono,@Param("correo") String correo);

}
