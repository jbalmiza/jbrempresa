package com.jbrempresa.backend.dto;
import com.jbrempresa.backend.entity.Usuario;import jakarta.validation.constraints.*;
public record UsuarioEntrada(@NotBlank @Size(max=100) String usuUsu,@Size(max=200) String usuCon,@NotNull Long perId,@NotNull Long usuPerId,@NotBlank @Size(max=150) String usuNom,@Size(max=30) String usuTel,@Email @Size(max=150) String usuEma,String usuAct){
 public Usuario entidad(){Usuario v=new Usuario();v.setUsuUsu(usuUsu);v.setUsuCon(usuCon);v.setPerId(perId);v.setUsuPerId(usuPerId);v.setUsu_nom(usuNom);v.setUsuTel(usuTel);v.setUsuEma(usuEma);v.setUsu_act(usuAct);return v;}
}
