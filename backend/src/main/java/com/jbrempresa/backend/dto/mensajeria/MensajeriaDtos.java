package com.jbrempresa.backend.dto.mensajeria;
import jakarta.validation.constraints.*;import java.time.LocalDateTime;import java.util.List;
public final class MensajeriaDtos{private MensajeriaDtos(){}
 public record Destinatario(Long usuarioId,Long empresaId,String nombre,String usuario,String perfil){}
 public record NuevaConversacion(@NotBlank@Size(max=200)String asunto,@Size(max=4000)String contenido,@NotEmpty List<Long> destinatarioIds,@Pattern(regexp="NORMAL|AVISO|ALERTA")String clasificacion){}
 public record NuevoMensaje(@NotBlank@Size(max=4000)String contenido,@Pattern(regexp="NORMAL|AVISO|ALERTA")String clasificacion){}
 public record EditarMensaje(@NotBlank@Size(max=4000)String contenido){}
 public record ClasificarMensaje(@NotNull@Pattern(regexp="NORMAL|AVISO|ALERTA")String clasificacion){}
 public record Mensaje(Long id,Long conversacionId,Long remitenteId,String remitente,List<Destinatario> destinatarios,String contenido,String clasificacion,LocalDateTime fecha,LocalDateTime fechaModificacion,boolean propio){}
 public record Conversacion(Long id,String asunto,List<Destinatario> participantes,String ultimoMensaje,String ultimoEmisor,List<Destinatario> ultimoDestinatarios,String clasificacion,LocalDateTime fechaUltima,long noLeidos,boolean activa,LocalDateTime fechaBaja,String bajaPor){}
}
