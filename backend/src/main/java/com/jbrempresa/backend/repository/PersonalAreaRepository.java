package com.jbrempresa.backend.repository;
import java.util.List; import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.PersonalArea;
public interface PersonalAreaRepository extends JpaRepository<PersonalArea,Long> {
 List<PersonalArea> findByEmpIdOrderByPeaId(Long empId);
 Optional<PersonalArea> findByEmpIdAndPeaId(Long empId,Long peaId);
 boolean existsByEmpIdAndAreId(Long empId,Long areId);
 boolean existsByEmpIdAndAreIdAndPerIdAndPeaActTrue(Long empId,Long areId,Long perId);
 boolean existsByEmpIdAndAreIdAndPerIdAndPeaActTrueAndPeaIdNot(Long empId,Long areId,Long perId,Long peaId);
 boolean existsByEmpIdAndPerIdAndPeaPriTrueAndPeaActTrue(Long empId,Long perId);
 boolean existsByEmpIdAndPerIdAndPeaPriTrueAndPeaActTrueAndPeaIdNot(Long empId,Long perId,Long peaId);
}
