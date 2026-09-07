package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.RecuperacionContrasena;

public interface RecuperacionContrasenaRepository extends JpaRepository<RecuperacionContrasena, Long> {
    Optional<RecuperacionContrasena> findByRecTokHashAndRecActTrue(String recTokHash);
    List<RecuperacionContrasena> findByUsuIdAndRecActTrue(Long usuId);
    Optional<RecuperacionContrasena> findTopByUsuIdOrderByRecFecCreDesc(Long usuId);
}
