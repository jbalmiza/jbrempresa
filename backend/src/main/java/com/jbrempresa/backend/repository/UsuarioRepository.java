package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import com.jbrempresa.backend.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Obtiene el siguiente ID disponible
    @Query("SELECT COALESCE(MAX(u.usuId),0) + 1 FROM Usuario u WHERE u.empId=:empId")
    Long obtenerSiguienteId(@org.springframework.data.repository.query.Param("empId") Long empId);

    // Obtiene todos los usuarios de un cliente
    List<Usuario> findByEmpId(Long empId);

    // Busca usuarios del cliente autenticado, con filtros y paginación en base de datos.
    @Query(value = """
            SELECT * FROM usuarios u
            WHERE u.emp_id = :empresaId
              AND (:empId IS NULL OR CAST(u.emp_id AS TEXT) ILIKE CONCAT('%', :empId, '%'))
              AND (:usuId IS NULL OR CAST(u.usu_id AS TEXT) ILIKE CONCAT('%', :usuId, '%'))
              AND (:usuUsu IS NULL OR u.usu_usu ILIKE CONCAT('%', :usuUsu, '%'))
              AND (:perId IS NULL OR CAST(u.per_id AS TEXT) ILIKE CONCAT('%', :perId, '%'))
              AND (:usuNom IS NULL OR u.usu_nom ILIKE CONCAT('%', :usuNom, '%'))
              AND (:usuEma IS NULL OR u.usu_ema ILIKE CONCAT('%', :usuEma, '%'))
              AND (:usuUsuMov IS NULL OR u.usu_usu_mov ILIKE CONCAT('%', :usuUsuMov, '%'))
              AND (:usuFecMov IS NULL OR TO_CHAR(u.usu_fec_mov, 'DD/MM/YYYY HH24:MI:SS') ILIKE CONCAT('%', :usuFecMov, '%'))
              AND (:usuAct IS NULL OR CAST(u.usu_act AS TEXT) ILIKE CONCAT('%', :usuAct, '%'))
            """,
            countQuery = """
            SELECT COUNT(*) FROM usuarios u
            WHERE u.emp_id = :empresaId
              AND (:empId IS NULL OR CAST(u.emp_id AS TEXT) ILIKE CONCAT('%', :empId, '%'))
              AND (:usuId IS NULL OR CAST(u.usu_id AS TEXT) ILIKE CONCAT('%', :usuId, '%'))
              AND (:usuUsu IS NULL OR u.usu_usu ILIKE CONCAT('%', :usuUsu, '%'))
              AND (:perId IS NULL OR CAST(u.per_id AS TEXT) ILIKE CONCAT('%', :perId, '%'))
              AND (:usuNom IS NULL OR u.usu_nom ILIKE CONCAT('%', :usuNom, '%'))
              AND (:usuEma IS NULL OR u.usu_ema ILIKE CONCAT('%', :usuEma, '%'))
              AND (:usuUsuMov IS NULL OR u.usu_usu_mov ILIKE CONCAT('%', :usuUsuMov, '%'))
              AND (:usuFecMov IS NULL OR TO_CHAR(u.usu_fec_mov, 'DD/MM/YYYY HH24:MI:SS') ILIKE CONCAT('%', :usuFecMov, '%'))
              AND (:usuAct IS NULL OR CAST(u.usu_act AS TEXT) ILIKE CONCAT('%', :usuAct, '%'))
            """,
            nativeQuery = true)
    Page<Usuario> buscarPorEmpresa(
            @Param("empresaId") Long empresaId,
            @Param("empId") String empId,
            @Param("usuId") String usuId,
            @Param("usuUsu") String usuUsu,
            @Param("perId") String perId,
            @Param("usuNom") String usuNom,
            @Param("usuEma") String usuEma,
            @Param("usuUsuMov") String usuUsuMov,
            @Param("usuFecMov") String usuFecMov,
            @Param("usuAct") String usuAct,
            Pageable pageable);

    // Busca un usuario concreto de un cliente
    Optional<Usuario> findByEmpIdAndUsuId(
            Long empId,
            Long usuId);

    // Busca un usuario por nombre de usuario
    Optional<Usuario> findByUsuUsu(
            String usuUsu);

    Optional<Usuario> findByUsuUsuAndUsuEmaIgnoreCase(
            String usuUsu,
            String usuEma);

    // Comprueba si existe un nombre de usuario.
    boolean existsByUsuUsu(
            String usuUsu);

    // Comprueba si existe un nombre de usuario en otro registro.
    boolean existsByUsuUsuAndUsuIdNot(
            String usuUsu,
            Long usuId);

    boolean existsByEmpIdAndUsuPerId(Long empId, Long usuPerId);
    boolean existsByEmpIdAndUsuPerIdAndUsuIdNot(Long empId, Long usuPerId, Long usuId);

    // Comprueba usuario y contraseña
}
