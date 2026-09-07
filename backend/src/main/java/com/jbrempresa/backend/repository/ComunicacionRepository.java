package com.jbrempresa.backend.repository;
import java.util.*; import org.springframework.data.jpa.repository.JpaRepository; import com.jbrempresa.backend.entity.Comunicacion;
public interface ComunicacionRepository extends JpaRepository<Comunicacion,Long>{List<Comunicacion> findByEmpIdAndComActTrueOrderByComFecUltDesc(Long empId);List<Comunicacion> findByEmpIdAndPerIdAndComActTrueOrderByComFecUltDesc(Long empId,Long perId);Optional<Comunicacion> findByEmpIdAndComIdAndComActTrue(Long empId,Long comId);}
