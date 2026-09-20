package com.jbrempresa.backend.core.config;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.Time;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

/** Mantiene los perfiles funcionales y usuarios de demostración acordados. */
@Component
@Order(0)
public class PerfilesIniciales implements ApplicationRunner {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;

    public PerfilesIniciales(JdbcTemplate jdbc, PasswordEncoder passwordEncoder) {
        this.jdbc = jdbc;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        sincronizarSecuencia("perfiles", "per_id");
        sincronizarSecuencia("usuarios", "usu_id");
        List<Long> empresas = jdbc.queryForList(
                "SELECT emp_id FROM empresas WHERE LOWER(emp_act) IN ('true','s','a','1') ORDER BY emp_id",
                Long.class);
        if (empresas.isEmpty()) return;

        Long empresaAdministradora = empresas.getFirst();
        List<String> usuariosPermitidos = new ArrayList<>();
        usuariosPermitidos.add("jackalblue");
        jdbc.update("UPDATE usuarios SET usu_usu='jackalblue' WHERE LOWER(usu_usu)='jackablue' AND NOT EXISTS (SELECT 1 FROM usuarios WHERE LOWER(usu_usu)='jackalblue')");
        for (Long empresaId : empresas) {
            if (empresaId.equals(empresaAdministradora)) {
                Long perfil = perfil(empresaId, "Administrador", "GLOBAL");
                usuario(empresaId, perfil, "jackalblue", "jackalblue", "Administrador global");
            }
            Long jefe = perfil(empresaId, "Jefe", "EMPRESA");
            Long empleado = perfil(empresaId, "Empleado", "EMPLEADO");
            usuario(empresaId, jefe, "jefe" + empresaId, "jefe", "Jefe empresa " + empresaId);
            prepararAgendaJefe(empresaId, "jefe" + empresaId);
            usuario(empresaId, empleado, "empleado" + empresaId, "empleado", "Empleado empresa " + empresaId);
            prepararRepartidor(empresaId, "empleado" + empresaId);
            usuariosPermitidos.add("jefe" + empresaId);
            usuariosPermitidos.add("empleado" + empresaId);
            asegurarUsuariosEmpleados(empresaId, empleado, usuariosPermitidos);
        }
        eliminarUsuariosNoDocumentados(usuariosPermitidos);
        normalizarPerfiles(empresaAdministradora);
    }

    private void eliminarUsuariosNoDocumentados(List<String> permitidos) {
        String marcas = String.join(",", java.util.Collections.nCopies(permitidos.size(), "?"));
        Object[] valores = permitidos.toArray();
        String seleccion = "SELECT usu_id FROM usuarios WHERE LOWER(usu_usu) NOT IN (" + marcas + ")";
        jdbc.update("DELETE FROM recuperaciones_contrasena WHERE usu_id IN (" + seleccion + ")", valores);
        jdbc.update("DELETE FROM usuarios_movimientos WHERE usu_id IN (" + seleccion + ")", valores);
        jdbc.update("DELETE FROM usuarios WHERE LOWER(usu_usu) NOT IN (" + marcas + ")", valores);
    }

    /** Garantiza que cada recurso empleado activo tenga una cuenta vinculada a su Persona. */
    private void asegurarUsuariosEmpleados(Long empresaId, Long perfilEmpleado, List<String> permitidos) {
        record Empleado(Long recursoId, Long personaId, String nombre) {}
        List<Empleado> empleados = jdbc.query("""
                SELECT r.reo_id,r.per_id,r.reo_nom
                  FROM recursos_operativos r
                 WHERE r.emp_id=? AND r.reo_tip='EMPLEADO' AND r.reo_act=TRUE
                   AND r.reo_tip_mov<>'B' AND r.reo_ope=TRUE AND r.per_id IS NOT NULL
                   AND r.reo_id_his=(SELECT MAX(r2.reo_id_his) FROM recursos_operativos r2
                                      WHERE r2.emp_id=r.emp_id AND r2.reo_id=r.reo_id)
                 ORDER BY r.reo_id
                """, (rs, fila) -> new Empleado(rs.getLong(1), rs.getLong(2), rs.getString(3)), empresaId);
        for (Empleado empleado : empleados) {
            List<String> accesos = jdbc.queryForList(
                    "SELECT usu_usu FROM usuarios WHERE emp_id=? AND usu_per_id=? ORDER BY usu_id",
                    String.class, empresaId, empleado.personaId());
            if (!accesos.isEmpty()) {
                jdbc.update("UPDATE usuarios SET usu_act='true',usu_usu_mov='SISTEMA',usu_fec_mov=? WHERE emp_id=? AND usu_per_id=?",
                        Timestamp.valueOf(LocalDateTime.now()), empresaId, empleado.personaId());
                accesos.stream().map(v -> v.toLowerCase(Locale.ROOT)).forEach(permitidos::add);
                continue;
            }
            String acceso = "recurso" + empleado.recursoId();
            jdbc.update("INSERT INTO usuarios(emp_id,usu_usu,usu_con,per_id,usu_per_id,usu_nom,usu_ema,usu_act,usu_usu_mov,usu_fec_mov) VALUES(?,?,?,?,?,?,?,'true','SISTEMA',?)",
                    empresaId, acceso, passwordEncoder.encode("empleado"), perfilEmpleado, empleado.personaId(), empleado.nombre(),
                    acceso + "@pruebas.local", Timestamp.valueOf(LocalDateTime.now()));
            permitidos.add(acceso);
        }
    }

    private void sincronizarSecuencia(String tabla, String columna) {
        try (var conexion = java.util.Objects.requireNonNull(jdbc.getDataSource()).getConnection()) {
            if (!"PostgreSQL".equalsIgnoreCase(conexion.getMetaData().getDatabaseProductName())) return;
        } catch (java.sql.SQLException e) {
            throw new IllegalStateException("No se pudo identificar la base de datos para sincronizar sus secuencias.", e);
        }
        jdbc.execute("SELECT setval(pg_get_serial_sequence('" + tabla + "','" + columna
                + "'),COALESCE((SELECT MAX(" + columna + ") FROM " + tabla + "),1),TRUE)");
    }

    private void normalizarPerfiles(Long empresaAdministradora) {
        Long administrador = jdbc.queryForObject("SELECT per_id FROM perfiles WHERE emp_id=? AND UPPER(per_nom)='ADMINISTRADOR' ORDER BY per_id LIMIT 1", Long.class, empresaAdministradora);
        jdbc.update("UPDATE usuarios SET per_id=(SELECT per_id FROM perfiles p WHERE p.emp_id=usuarios.emp_id AND UPPER(p.per_nom)='JEFE' ORDER BY p.per_id LIMIT 1) WHERE usu_usu<> 'jackalblue' AND per_id IN (SELECT per_id FROM perfiles WHERE UPPER(per_nom)='ADMINISTRADOR')");
        jdbc.update("UPDATE usuarios SET per_id=? WHERE usu_usu='jackalblue'", administrador);
        jdbc.update("DELETE FROM perfiles p WHERE UPPER(p.per_nom) NOT IN ('ADMINISTRADOR','JEFE','CLIENTE','EMPLEADO') AND NOT EXISTS (SELECT 1 FROM usuarios u WHERE u.per_id=p.per_id)");
        jdbc.update("DELETE FROM perfiles p WHERE UPPER(p.per_nom)='ADMINISTRADOR' AND p.emp_id<>? AND NOT EXISTS (SELECT 1 FROM usuarios u WHERE u.per_id=p.per_id)", empresaAdministradora);
    }

    private Long perfil(Long empresaId, String nombre, String alcance) {
        List<Long> ids = jdbc.queryForList(
                "SELECT per_id FROM perfiles WHERE emp_id=? AND UPPER(per_nom)=? ORDER BY per_id LIMIT 1",
                Long.class, empresaId, nombre.toUpperCase(Locale.ROOT));
        if (!ids.isEmpty()) {
            jdbc.update("UPDATE perfiles SET per_tip_per=?,per_mod_adm=?,per_mod_ter=?,per_mod_per=?,per_mod_pro=?,per_mod_ven=?,per_act='true',per_usu_mov='SISTEMA',per_fec_mov=? WHERE per_id=?",
                    alcance, permiso(alcance, "ADMINISTRACION"), permiso(alcance, "TERRITORIO"), permiso(alcance, "PERSONAS"), permiso(alcance, "PRODUCTOS"), permiso(alcance, "VENTAS"), Timestamp.valueOf(LocalDateTime.now()), ids.getFirst());
            return ids.getFirst();
        }
        return jdbc.queryForObject("INSERT INTO perfiles(emp_id,per_nom,per_tip_per,per_mod_adm,per_mod_ter,per_mod_per,per_mod_pro,per_mod_ven,per_act,per_usu_mov,per_fec_mov) VALUES(?,?,?,?,?,?,?,?,?,?,?) RETURNING per_id",
                Long.class, empresaId, nombre, alcance, permiso(alcance, "ADMINISTRACION"), permiso(alcance, "TERRITORIO"), permiso(alcance, "PERSONAS"), permiso(alcance, "PRODUCTOS"), permiso(alcance, "VENTAS"), "true", "SISTEMA", Timestamp.valueOf(LocalDateTime.now()));
    }

    private String permiso(String alcance, String modulo) {
        if (alcance.equals("GLOBAL") || alcance.equals("EMPRESA")) return "true";
        return Boolean.toString((alcance.equals("CLIENTE") && modulo.equals("CLIENTES"))
                || (alcance.equals("EMPLEADO") && modulo.equals("EMPLEADOS")));
    }

    private void usuario(Long empresaId, Long perfilId, String acceso, String clave, String nombre) {
        List<Long> existentes = jdbc.queryForList("SELECT usu_id FROM usuarios WHERE LOWER(usu_usu)=LOWER(?)", Long.class, acceso);
        if (!existentes.isEmpty()) {
            jdbc.update("UPDATE usuarios SET emp_id=?,per_id=?,usu_con=?,usu_nom=?,usu_act='true',usu_usu_mov='SISTEMA',usu_fec_mov=? WHERE usu_id=?",
                    empresaId, perfilId, passwordEncoder.encode(clave), nombre, Timestamp.valueOf(LocalDateTime.now()), existentes.getFirst());
            return;
        }
        Long personaId = jdbc.queryForObject("SELECT COALESCE(MAX(per_id),0)+1 FROM personas", Long.class);
        jdbc.update("INSERT INTO personas(per_id,per_id_his,per_tip_mov,emp_id,per_tip_per,per_tip_doc,per_doc,per_nom_com,per_nom,per_ape1,per_ema,per_act,per_usu_mov,per_fec_mov) VALUES(?,1,'A',?,'FISICA','INT',?,?,?,'Usuario',?,TRUE,'SISTEMA',?)",
                personaId, empresaId, "USUARIO-" + acceso.toUpperCase(Locale.ROOT), nombre, nombre, acceso + "@pruebas.local", Timestamp.valueOf(LocalDateTime.now()));
        jdbc.update("INSERT INTO usuarios(emp_id,usu_usu,usu_con,per_id,usu_per_id,usu_nom,usu_ema,usu_act,usu_usu_mov,usu_fec_mov) VALUES(?,?,?,?,?,?,?,'true','SISTEMA',?)",
                empresaId, acceso, passwordEncoder.encode(clave), perfilId, personaId, nombre, acceso + "@pruebas.local", Timestamp.valueOf(LocalDateTime.now()));
    }

    /** Convierte el empleado de demostración en trabajador de reparto con capacidad y agenda propias. */
    private void prepararRepartidor(Long empresaId, String acceso) {
        Long personaId = jdbc.queryForObject(
                "SELECT usu_per_id FROM usuarios WHERE emp_id=? AND LOWER(usu_usu)=LOWER(?)",
                Long.class, empresaId, acceso);
        String nombre = "Repartidor empresa " + empresaId;
        List<Long> recursos = jdbc.queryForList(
                "SELECT reo_id FROM recursos_operativos WHERE emp_id=? AND per_id=? AND reo_act=TRUE AND reo_tip_mov<>'B' ORDER BY reo_id_his DESC LIMIT 1",
                Long.class, empresaId, personaId);
        Long recursoId;
        if (recursos.isEmpty()) {
            // reo_id forma parte de una clave primaria global junto con el histórico;
            // no puede reiniciarse su numeración dentro de cada empresa.
            recursoId = jdbc.queryForObject("SELECT COALESCE(MAX(reo_id),0)+1 FROM recursos_operativos", Long.class);
            jdbc.update("INSERT INTO recursos_operativos(reo_id,reo_id_his,emp_id,reo_nom,reo_tip,per_id,reo_des,reo_tip_mov,reo_cau_mov,reo_ope,reo_act,reo_usu_mov,reo_fec_mov) VALUES(?,1,?,?, 'EMPLEADO',?,'Trabajador con habilidad de reparto','A','Alta automática de repartidor',TRUE,TRUE,'SISTEMA',?)",
                    recursoId, empresaId, nombre, personaId, Timestamp.valueOf(LocalDateTime.now()));
        } else {
            recursoId = recursos.getFirst();
        }
        Integer capacidades = jdbc.queryForObject(
                "SELECT COUNT(*) FROM recursos_capacidades WHERE emp_id=? AND reo_id=? AND rec_ori='SERVICIO' AND UPPER(rec_tip)='REPARTO'",
                Integer.class, empresaId, recursoId);
        if (capacidades == 0) jdbc.update(
                "INSERT INTO recursos_capacidades(emp_id,reo_id,rec_ori,rec_tip,rec_act) VALUES(?,?,'SERVICIO','REPARTO',TRUE)",
                empresaId, recursoId);
        else jdbc.update("UPDATE recursos_capacidades SET rec_act=TRUE WHERE emp_id=? AND reo_id=? AND rec_ori='SERVICIO' AND UPPER(rec_tip)='REPARTO'", empresaId, recursoId);

        List<Long> agendas = jdbc.queryForList(
                "SELECT rag_id FROM recursos_agendables WHERE emp_id=? AND rag_tip='EMPLEADO' AND rag_ref_id=? ORDER BY rag_id LIMIT 1",
                Long.class, empresaId, recursoId);
        Long agendaId;
        if (agendas.isEmpty()) {
            agendaId = jdbc.queryForObject("INSERT INTO recursos_agendables(emp_id,rag_tip,rag_ref_id,rag_nom,rag_cap,rag_mar_pre,rag_mar_pos,rag_hor_vis,rag_usu_mov,rag_fec_mov,rag_act) VALUES(?,'EMPLEADO',?,?,1,0,0,?,'SISTEMA',?,TRUE) RETURNING rag_id",
                    Long.class, empresaId, recursoId, nombre, Time.valueOf("09:00:00"), Timestamp.valueOf(LocalDateTime.now()));
        } else {
            agendaId = agendas.getFirst();
            jdbc.update("UPDATE recursos_agendables SET rag_act=TRUE WHERE rag_id=?", agendaId);
        }
        Integer horarios = jdbc.queryForObject("SELECT COUNT(*) FROM horarios_recurso WHERE emp_id=? AND rag_id=? AND hor_act=TRUE", Integer.class, empresaId, agendaId);
        if (horarios == 0) for (int dia = 1; dia <= 7; dia++) jdbc.update(
                "INSERT INTO horarios_recurso(emp_id,rag_id,hor_dia,hor_ini,hor_fin,hor_usu_mov,hor_fec_mov,hor_act) VALUES(?,?,?,?,?,'SISTEMA',?,TRUE)",
                empresaId, agendaId, dia, Time.valueOf("09:00:00"), Time.valueOf("23:00:00"), Timestamp.valueOf(LocalDateTime.now()));
    }

    /** El Jefe también es trabajador y dispone de agenda propia, sin capacidad productiva predeterminada. */
    private void prepararAgendaJefe(Long empresaId, String acceso) {
        Long personaId = jdbc.queryForObject("SELECT usu_per_id FROM usuarios WHERE emp_id=? AND LOWER(usu_usu)=LOWER(?)", Long.class, empresaId, acceso);
        String nombre = "Jefe empresa " + empresaId;
        List<Long> recursos = jdbc.queryForList("SELECT reo_id FROM recursos_operativos WHERE emp_id=? AND per_id=? AND reo_act=TRUE AND reo_tip_mov<>'B' ORDER BY reo_id_his DESC LIMIT 1", Long.class, empresaId, personaId);
        Long recursoId;
        if (recursos.isEmpty()) {
            recursoId = jdbc.queryForObject("SELECT COALESCE(MAX(reo_id),0)+1 FROM recursos_operativos", Long.class);
            jdbc.update("INSERT INTO recursos_operativos(reo_id,reo_id_his,emp_id,reo_nom,reo_tip,per_id,reo_des,reo_tip_mov,reo_cau_mov,reo_ope,reo_act,reo_usu_mov,reo_fec_mov) VALUES(?,1,?,?,'EMPLEADO',?,'Jefe con agenda de trabajo','A','Alta automática de agenda del jefe',TRUE,TRUE,'SISTEMA',?)", recursoId, empresaId, nombre, personaId, Timestamp.valueOf(LocalDateTime.now()));
        } else recursoId = recursos.getFirst();
        List<Long> agendas = jdbc.queryForList("SELECT rag_id FROM recursos_agendables WHERE emp_id=? AND rag_tip='EMPLEADO' AND rag_ref_id=? ORDER BY rag_id LIMIT 1", Long.class, empresaId, recursoId);
        Long agendaId;
        if (agendas.isEmpty()) agendaId = jdbc.queryForObject("INSERT INTO recursos_agendables(emp_id,rag_tip,rag_ref_id,rag_nom,rag_cap,rag_mar_pre,rag_mar_pos,rag_hor_vis,rag_usu_mov,rag_fec_mov,rag_act) VALUES(?,'EMPLEADO',?,?,1,0,0,?,'SISTEMA',?,TRUE) RETURNING rag_id", Long.class, empresaId, recursoId, nombre, Time.valueOf("09:00:00"), Timestamp.valueOf(LocalDateTime.now()));
        else { agendaId = agendas.getFirst(); jdbc.update("UPDATE recursos_agendables SET rag_act=TRUE WHERE rag_id=?", agendaId); }
        Integer horarios = jdbc.queryForObject("SELECT COUNT(*) FROM horarios_recurso WHERE emp_id=? AND rag_id=? AND hor_act=TRUE", Integer.class, empresaId, agendaId);
        if (horarios == 0) for (int dia = 1; dia <= 5; dia++) jdbc.update("INSERT INTO horarios_recurso(emp_id,rag_id,hor_dia,hor_ini,hor_fin,hor_usu_mov,hor_fec_mov,hor_act) VALUES(?,?,?,?,?,'SISTEMA',?,TRUE)", empresaId, agendaId, dia, Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), Timestamp.valueOf(LocalDateTime.now()));
    }
}
