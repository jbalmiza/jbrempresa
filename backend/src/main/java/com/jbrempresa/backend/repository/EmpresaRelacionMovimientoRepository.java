package com.jbrempresa.backend.repository;
import java.util.List;import org.springframework.data.jpa.repository.JpaRepository;import com.jbrempresa.backend.entity.EmpresaRelacionMovimiento;
public interface EmpresaRelacionMovimientoRepository extends JpaRepository<EmpresaRelacionMovimiento,Long>{
 List<EmpresaRelacionMovimiento> findByEmpresaIdAndRelacionIdOrderByIdDesc(Long empresaId,Long relacionId);
 void deleteByRelacionId(Long relacionId);
}
