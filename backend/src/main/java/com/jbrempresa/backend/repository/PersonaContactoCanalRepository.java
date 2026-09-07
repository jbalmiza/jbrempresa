package com.jbrempresa.backend.repository;
import java.util.*; import org.springframework.data.jpa.repository.JpaRepository; import com.jbrempresa.backend.entity.PersonaContactoCanal;
public interface PersonaContactoCanalRepository extends JpaRepository<PersonaContactoCanal,Long>{List<PersonaContactoCanal> findByEmpIdAndCocIdAndPccActTrue(Long empId,Long cocId);Optional<PersonaContactoCanal> findByEmpIdAndCocIdAndPerId(Long empId,Long cocId,Long perId);boolean existsByEmpIdAndCocIdAndPerIdAndPccActTrue(Long empId,Long cocId,Long perId);}
