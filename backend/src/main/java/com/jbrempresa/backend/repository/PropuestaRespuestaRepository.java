package com.jbrempresa.backend.repository;

import com.jbrempresa.backend.entity.PropuestaRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PropuestaRespuestaRepository extends JpaRepository<PropuestaRespuesta, Long> {
    Optional<PropuestaRespuesta> findByEmpIdAndPrrId(Long empId, Long prrId);
    List<PropuestaRespuesta> findByEmpIdAndComIdOrderByPrrFecGenDesc(Long empId, Long comId);
}
