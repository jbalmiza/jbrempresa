package com.jbrempresa.backend.repository;
import java.util.*; import org.springframework.data.jpa.repository.JpaRepository; import com.jbrempresa.backend.entity.ModuloAplicacion;
public interface ModuloAplicacionRepository extends JpaRepository<ModuloAplicacion,Long>{List<ModuloAplicacion> findByActivoTrueOrderByPosicionAscIdAsc(); Optional<ModuloAplicacion> findByCodigoIgnoreCase(String codigo);}
