package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.Parametro;

public interface ParametroRepository extends JpaRepository<Parametro, Long> {
    List<Parametro> findByEmpIdOrderByParId(Long empId);
    List<Parametro> findByEmpIdAndParModIgnoreCaseOrderByParId(Long empId, String parMod);
    List<Parametro> findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(String parMod, String parCod);
    Optional<Parametro> findByEmpIdAndParId(Long empId, Long parId);
    Optional<Parametro> findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(
            Long empId, String parMod, String parCod);
    boolean existsByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(Long empId, String parMod, String parCod);
    boolean existsByEmpIdAndParModIgnoreCaseAndParCodIgnoreCaseAndParIdNot(
            Long empId, String parMod, String parCod, Long parId);
}
