package com.jbrempresa.backend.core.security;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import jakarta.persistence.Entity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

/**
 * Cinturón de seguridad para controladores antiguos pendientes de DTO tipado.
 * Impide que campos multiempresa o de auditoría enviados por el navegador
 * lleguen a considerarse fiables. Los controladores deben rellenarlos desde
 * ContextoOperacion. Los nuevos endpoints deben usar DTO y no depender de este guard.
 */
@ControllerAdvice
public class ProteccionEntidadEntradaAdvice extends RequestBodyAdviceAdapter {
    private final Validator validator;

    public ProteccionEntidadEntradaAdvice(Validator validator) {
        this.validator = validator;
    }
    private static final String[] CAMPOS_PROTEGIDOS = {
            "EmpId", "UsuMov", "FecMov", "Act"
    };

    @Override
    public boolean supports(MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return parameter.getParameterType().isAnnotationPresent(Entity.class);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        for (Method metodo : body.getClass().getMethods()) {
            if (metodo.getParameterCount() != 1 || !metodo.getName().startsWith("set")) continue;
            String propiedad = metodo.getName().substring(3);
            if (!protegido(propiedad) || metodo.getParameterTypes()[0].isPrimitive()) continue;
            try { metodo.invoke(body, new Object[]{null}); }
            catch (ReflectiveOperationException ex) { throw new IllegalStateException("No se pudo proteger el contrato de entrada.", ex); }
        }
        var violaciones = validator.validate(body);
        if (!violaciones.isEmpty()) {
            throw new ConstraintViolationException(
                    "La solicitud contiene campos no válidos.",
                    Set.<ConstraintViolation<?>>copyOf(violaciones));
        }
        return body;
    }

    private boolean protegido(String propiedad) {
        if ("EmpId".equals(propiedad)) return true;
        for (String sufijo : CAMPOS_PROTEGIDOS) if (!"EmpId".equals(sufijo) && propiedad.endsWith(sufijo)) return true;
        return false;
    }
}
