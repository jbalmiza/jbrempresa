package com.jbrempresa.backend.repository;import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;import com.jbrempresa.backend.entity.TareaReserva;
public interface TareaReservaRepository extends JpaRepository<TareaReserva,Long>{List<TareaReserva> findByEmpIdAndResIdAndTarActTrueOrderByTarOrdAsc(Long empId,Long resId);void deleteByEmpIdAndResId(Long empId,Long resId);}
