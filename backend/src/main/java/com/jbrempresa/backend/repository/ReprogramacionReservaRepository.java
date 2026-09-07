package com.jbrempresa.backend.repository;import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;import com.jbrempresa.backend.entity.ReprogramacionReserva;
public interface ReprogramacionReservaRepository extends JpaRepository<ReprogramacionReserva,Long>{List<ReprogramacionReserva> findByEmpIdAndResIdAndRprActTrueOrderByRprFecMovDesc(Long empId,Long resId);}
