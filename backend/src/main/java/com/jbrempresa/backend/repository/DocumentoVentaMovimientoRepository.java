package com.jbrempresa.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.DocumentoVentaMovimiento;

public interface DocumentoVentaMovimientoRepository extends JpaRepository<DocumentoVentaMovimiento, Long> {
    List<DocumentoVentaMovimiento> findByEmpIdAndDovIdOrderByDvmFecMovDescDvmIdDesc(Long empId, Long dovId);
    void deleteByEmpIdAndDovId(Long empId, Long dovId);
}
