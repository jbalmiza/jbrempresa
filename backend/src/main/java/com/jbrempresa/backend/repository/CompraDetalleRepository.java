package com.jbrempresa.backend.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.CompraDetalle;
public interface CompraDetalleRepository extends JpaRepository<CompraDetalle,Long> {
    List<CompraDetalle> findByEmpIdAndComIdOrderByComDetId(Long empId,Long comId);
    void deleteByEmpIdAndComId(Long empId,Long comId);
}
