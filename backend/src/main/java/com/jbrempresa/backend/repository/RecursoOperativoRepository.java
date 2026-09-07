package com.jbrempresa.backend.repository;
import java.util.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.*;
public interface RecursoOperativoRepository extends JpaRepository<RecursoOperativo,RecursoOperativoId>{
 @Query("select coalesce(max(r.reoId),0)+1 from RecursoOperativo r where r.empId=:empId") Long obtenerSiguienteId(Long empId);
 List<RecursoOperativo> findByEmpIdAndReoActTrueOrderByReoNom(Long empId);
 Optional<RecursoOperativo> findByEmpIdAndReoIdAndReoActTrue(Long empId,Long id);
 List<RecursoOperativo> findByEmpIdAndReoIdOrderByReoIdHisDesc(Long empId,Long id);
 Optional<RecursoOperativo> findByEmpIdAndReoIdAndReoIdHis(Long empId,Long id,Long historico);
}
