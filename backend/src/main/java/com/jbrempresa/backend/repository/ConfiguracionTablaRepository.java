package com.jbrempresa.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.ConfiguracionTabla;

public interface ConfiguracionTablaRepository extends JpaRepository<ConfiguracionTabla,Long> {
    Optional<ConfiguracionTabla> findByEmpIdAndUsuIdAndCotClaAndCotActTrue(Long empId,Long usuId,String cotCla);
}
