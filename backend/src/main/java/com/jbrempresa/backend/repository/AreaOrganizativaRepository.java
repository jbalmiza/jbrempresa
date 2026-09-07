package com.jbrempresa.backend.repository;
import java.util.List; import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.AreaOrganizativa;
public interface AreaOrganizativaRepository extends JpaRepository<AreaOrganizativa,Long> {
 List<AreaOrganizativa> findByEmpIdOrderByAreNom(Long empId);
 Optional<AreaOrganizativa> findByEmpIdAndAreId(Long empId,Long areId);
 boolean existsByEmpIdAndAreCodIgnoreCase(Long empId,String areCod);
 boolean existsByEmpIdAndAreCodIgnoreCaseAndAreIdNot(Long empId,String areCod,Long areId);
 boolean existsByEmpIdAndAreIdPad(Long empId,Long areIdPad);
}
