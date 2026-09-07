package com.jbrempresa.backend.repository;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jbrempresa.backend.entity.RecursoCapacidad;
public interface RecursoCapacidadRepository extends JpaRepository<RecursoCapacidad,Long>{
 List<RecursoCapacidad> findByEmpIdAndReoIdOrderByRecOriAscRecTipAsc(Long empId,Long recursoId);
 void deleteByEmpIdAndReoId(Long empId,Long recursoId);
}
