package com.jbrempresa.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jbrempresa.backend.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Obtiene el siguiente ID disponible
    @Query("SELECT COALESCE(MAX(u.usuId),0) + 1 FROM Usuario u")
    Long obtenerSiguienteId();

    // Obtiene todos los usuarios de un cliente
    List<Usuario> findByCliId(Long cliId);

    // Busca un usuario concreto de un cliente
    Optional<Usuario> findByCliIdAndUsuId(
            Long cliId,
            Long usuId);

    // Busca un usuario por nombre de usuario
    Optional<Usuario> findByUsuUsu(
            String usuUsu);

    // Comprueba usuario y contraseña
    @Query("""
            SELECT u
            FROM Usuario u
            WHERE u.usuUsu = :usuario
            AND u.usuCon = :contrasena
            """)
    Usuario login(
            String usuario,
            String contrasena);

}