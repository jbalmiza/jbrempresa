package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.Adjunto;

public interface AdjuntoRepository extends JpaRepository<Adjunto, Long> {
    List<Adjunto> findByEmpIdAndAdjModIgnoreCaseAndAdjTipRegIgnoreCaseAndAdjRegIdOrderByAdjId(
            Long empId, String adjMod, String adjTipReg, Long adjRegId);
    Optional<Adjunto> findByEmpIdAndAdjId(Long empId, Long adjId);
}
