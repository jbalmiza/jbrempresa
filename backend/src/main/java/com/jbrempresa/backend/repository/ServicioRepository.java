package com.jbrempresa.backend.repository;
import com.jbrempresa.backend.entity.Servicio;
import com.jbrempresa.backend.entity.ServicioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
public interface ServicioRepository extends JpaRepository<Servicio, ServicioId> {
 @Query("select coalesce(max(s.serId),0)+1 from Servicio s where s.empId=:empId") Long obtenerSiguienteId(@org.springframework.data.repository.query.Param("empId") Long empId);
 List<Servicio> findByEmpIdAndSerActTrueOrderBySerId(Long empId);
 List<Servicio> findByEmpIdAndSerActTrueAndSerVisCatTrueOrderBySerCatAscSerSubCatAscSerNomAsc(Long empId);
 Optional<Servicio> findByEmpIdAndSerIdAndSerActTrue(Long empId,Long serId);
 List<Servicio> findByEmpIdAndSerIdOrderBySerFecMovDesc(Long empId,Long serId);
 Optional<Servicio> findByEmpIdAndSerIdAndSerIdHis(Long empId,Long serId,Long serIdHis);
}
