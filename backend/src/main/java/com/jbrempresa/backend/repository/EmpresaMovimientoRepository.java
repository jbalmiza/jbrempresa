package com.jbrempresa.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.EmpresaMovimiento;

public interface EmpresaMovimientoRepository extends JpaRepository<EmpresaMovimiento, Long> {
    List<EmpresaMovimiento> findByEmpresaIdOrderByFechaDesc(Long empresaId);
}
