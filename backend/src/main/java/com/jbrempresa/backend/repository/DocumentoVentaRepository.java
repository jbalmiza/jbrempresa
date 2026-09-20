package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.jbrempresa.backend.entity.DocumentoVenta;

public interface DocumentoVentaRepository extends JpaRepository<DocumentoVenta, Long> {
    List<DocumentoVenta> findByEmpIdAndDovTipOrderByDovFecDescDovIdDesc(Long empresaId, String tipo);
    List<DocumentoVenta> findByEmpIdAndDovTipAndDovActTrueOrderByDovFecDescDovIdDesc(Long empresaId, String tipo);
    List<DocumentoVenta> findByDovTipOrderByEmpIdAscDovFecDescDovIdDesc(String tipo);
    List<DocumentoVenta> findByDovTipAndDovActTrueOrderByEmpIdAscDovFecDescDovIdDesc(String tipo);
    Optional<DocumentoVenta> findByEmpIdAndDovId(Long empresaId, Long id);
    List<DocumentoVenta> findByEmpIdAndDovIdRai(Long empresaId, Long raiz);
    boolean existsByEmpIdAndDovIdOri(Long empresaId, Long origen);
    boolean existsByEmpIdAndDovIdOriAndDovTip(Long empresaId, Long origen, String tipo);
    long countByEmpIdAndDovTip(Long empresaId, String tipo);

    @Query("select d from DocumentoVenta d where d.empId = :empresaId and d.dovTip = 'PED' "
            + "and (:incluirBajas = true or d.dovAct = true) and exists "
            + "(select m.dvmId from DocumentoVentaMovimiento m where m.empId = d.empId "
            + "and m.dovId = d.dovId and m.dvmTipMov = 'ALTA' and m.dvmUsuMov = :usuario) "
            + "order by d.dovFec desc, d.dovId desc")
    List<DocumentoVenta> pedidosCreadosPor(@Param("empresaId") Long empresaId,
            @Param("usuario") String usuario, @Param("incluirBajas") boolean incluirBajas);

    @Query("select d from DocumentoVenta d where d.empId = :empresaId and d.dovId = :id "
            + "and d.dovTip = 'PED' and exists "
            + "(select m.dvmId from DocumentoVentaMovimiento m where m.empId = d.empId "
            + "and m.dovId = d.dovId and m.dvmTipMov = 'ALTA' and m.dvmUsuMov = :usuario)")
    Optional<DocumentoVenta> pedidoCreadoPor(@Param("empresaId") Long empresaId,
            @Param("id") Long id, @Param("usuario") String usuario);
}
