package com.jbrempresa.backend.repository;
import java.util.*; import org.springframework.data.jpa.repository.JpaRepository; import com.jbrempresa.backend.entity.EmpresaModulo;
public interface EmpresaModuloRepository extends JpaRepository<EmpresaModulo,Long>{List<EmpresaModulo> findByEmpresaId(Long empresaId); Optional<EmpresaModulo> findByEmpresaIdAndModuloId(Long empresaId,Long moduloId);}
