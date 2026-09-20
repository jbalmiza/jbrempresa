package com.jbrempresa.backend.repository;
import java.time.LocalDateTime; import java.util.*;
import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;
import com.jbrempresa.backend.entity.*;
public interface AvisoAlertaRepository extends JpaRepository<AvisoAlerta,AvisoAlertaId>{
 @Query("select coalesce(max(a.aviId),0)+1 from AvisoAlerta a where a.empId=:empId") Long siguienteId(@Param("empId") Long empId);
 List<AvisoAlerta> findByEmpIdAndAviActTrueOrderByAviId(Long empId);
 List<AvisoAlerta> findByAviActTrueOrderByEmpIdAscAviIdAsc();
 Optional<AvisoAlerta> findByEmpIdAndAviIdAndAviActTrue(Long empId,Long aviId);
 List<AvisoAlerta> findByEmpIdAndAviIdOrderByAviFecMovDesc(Long empId,Long aviId);
 @Query("select a from AvisoAlerta a where a.empId=:empId and a.aviAct=true and a.aviTipMov<>'B' and a.aviUbicacion=:ubicacion and (a.aviFecIni is null or a.aviFecIni<=:ahora) and (a.aviFecFin is null or a.aviFecFin>=:ahora) order by a.aviTipo desc,a.aviId")
 List<AvisoAlerta> visibles(@Param("empId") Long empId,@Param("ubicacion") String ubicacion,@Param("ahora") LocalDateTime ahora);
 @Query("select a from AvisoAlerta a where a.aviAct=true and a.aviTipMov<>'B' and a.aviUbicacion='MENSAJES' and (a.aviFecIni is null or a.aviFecIni<=:ahora) and (a.aviFecFin is null or a.aviFecFin>=:ahora) and ((:rol='ADMINISTRADOR' and a.aviDestinatario='ADMINISTRADOR') or (:rol='JEFE' and a.aviDestinatario in ('JEFE','EMPRESA') and (a.aviDestEmpId=:empresa or a.aviEmisor='ADMINISTRADOR' and a.aviDestinatario='EMPRESA' and a.aviDestEmpId is null)) or (:rol='EMPLEADO' and a.aviDestinatario='EMPLEADO' and a.aviDestEmpId=:empresa)) order by a.aviFecMov desc")
 List<AvisoAlerta> bandejaPara(@Param("rol") String rol,@Param("empresa") Long empresa,@Param("ahora") LocalDateTime ahora);
 @Query("select a from AvisoAlerta a where a.aviAct=true and a.aviTipMov<>'B' and a.aviUbicacion='VENTANA' and a.aviVentana=:ruta and (a.aviDestEmpId=:empresa or a.aviEmisor='ADMINISTRADOR' and a.aviDestinatario='EMPRESA' and a.aviDestEmpId is null) and (a.aviFecIni is null or a.aviFecIni<=:ahora) and (a.aviFecFin is null or a.aviFecFin>=:ahora) order by a.aviFecMov desc")
 List<AvisoAlerta> ventanaPara(@Param("ruta") String ruta,@Param("empresa") Long empresa,@Param("ahora") LocalDateTime ahora);
}
