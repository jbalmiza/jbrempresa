package com.jbrempresa.backend.dto;
import java.time.LocalDateTime;import com.fasterxml.jackson.annotation.JsonFormat;import com.jbrempresa.backend.entity.Usuario;
public record UsuarioSalida(Long empId,Long usuId,String usuUsu,Long perId,Long usuPerId,String usuNom,String usuTel,String usuEma,String usuAct,String usuUsuMov,@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss") LocalDateTime usuFecMov){
 public static UsuarioSalida desde(Usuario v){return new UsuarioSalida(v.getEmpId(),v.getUsuId(),v.getUsuUsu(),v.getPerId(),v.getUsuPerId(),v.getUsuNom(),v.getUsuTel(),v.getUsuEma(),v.getUsuAct(),v.getUsuUsuMov(),v.getUsuFecMov());}
}
