package com.jbrempresa.backend.repository;

import com.jbrempresa.backend.entity.AvisoAlertaLectura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisoAlertaLecturaRepository extends JpaRepository<AvisoAlertaLectura,Long> {
    boolean existsByEmpIdAndAviIdAndAviIdHisAndUsuarioId(Long empId,Long aviId,Long aviIdHis,Long usuarioId);
    void deleteByEmpIdAndAviId(Long empId,Long aviId);
}
