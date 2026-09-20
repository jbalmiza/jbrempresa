package com.jbrempresa.backend.repository;
import java.util.*;import org.springframework.data.jpa.repository.*;import org.springframework.data.repository.query.Param;import com.jbrempresa.backend.entity.*;
public interface ComponenteRepository extends JpaRepository<Componente,ComponenteId>{
 @Query("select coalesce(max(i.cmpId),0)+1 from Componente i where i.empId=:empId") Long siguiente(@Param("empId")Long empId);
 List<Componente> findByEmpIdAndCmpActTrueOrderByCmpId(Long empId); List<Componente> findByCmpActTrueOrderByEmpIdAscCmpIdAsc();
 Optional<Componente> findByEmpIdAndCmpIdAndCmpActTrue(Long empId,Long id); List<Componente> findByEmpIdAndCmpIdOrderByCmpIdHisDesc(Long empId,Long id);
 Optional<Componente> findByEmpIdAndCmpIdAndCmpIdHis(Long empId,Long id,Long historico);
}
