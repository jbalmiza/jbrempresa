package com.jbrempresa.backend.repository;
import java.util.List; import org.springframework.data.jpa.repository.JpaRepository; import com.jbrempresa.backend.entity.UsuarioMovimiento;
public interface UsuarioMovimientoRepository extends JpaRepository<UsuarioMovimiento,Long>{List<UsuarioMovimiento> findByEmpresaIdAndUsuarioIdOrderByFechaDesc(Long empresaId,Long usuarioId);}
