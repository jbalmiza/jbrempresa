package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import com.jbrempresa.backend.entity.Producto;
import com.jbrempresa.backend.entity.ProductoId;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductoRepository extends JpaRepository<Producto, ProductoId> {

    @Query("SELECT COALESCE(MAX(p.proId),0) + 1 FROM Producto p WHERE p.empId=:empId")
    Long obtenerSiguienteId(@org.springframework.data.repository.query.Param("empId") Long empId);

    // Obtiene únicamente los productos de un cliente
    List<Producto> findByEmpIdAndProActTrueOrderByProId(Long empId);
    List<Producto> findByEmpIdAndProActTrueAndProVisCatTrueOrderByProCatAscProSubCatAscProNomAsc(Long empId);

    // Busca un producto concreto perteneciente a un cliente
    Optional<Producto> findByEmpIdAndProIdAndProActTrue(Long empId, Long proId);

    List<Producto> findByEmpIdAndProIdOrderByProFecMovDesc(Long empId, Long proId);

    Optional<Producto> findByEmpIdAndProIdAndProIdHis(Long empId, Long proId, Long proIdHis);

}
