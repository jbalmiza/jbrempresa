package com.jbrempresa.backend.dto;

import java.util.Collection;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import jakarta.persistence.Entity;
import tools.jackson.databind.ObjectMapper;

/** Evita que las entidades JPA heredadas se serialicen directamente en la API. */
@ControllerAdvice
public class RespuestaEntidadAdvice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper mapper;
    public RespuestaEntidadAdvice(ObjectMapper mapper){this.mapper=mapper;}
    @Override public boolean supports(MethodParameter p,Class<? extends HttpMessageConverter<?>> c){return true;}
    @Override public Object beforeBodyWrite(Object body,MethodParameter p,MediaType m,
            Class<? extends HttpMessageConverter<?>> c,ServerHttpRequest req,ServerHttpResponse res){
        if(body==null)return null;
        if(esEntidad(body))return mapper.convertValue(body,Map.class);
        if(body instanceof Collection<?> coleccion && coleccion.stream().anyMatch(this::esEntidad))
            return coleccion.stream().map(v->esEntidad(v)?mapper.convertValue(v,Map.class):v).toList();
        return body;
    }
    private boolean esEntidad(Object valor){return valor!=null&&valor.getClass().isAnnotationPresent(Entity.class);}
}
