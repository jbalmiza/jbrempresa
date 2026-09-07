package com.jbrempresa.backend.core.comunicaciones;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jbrempresa.backend.entity.ContactoCanal;
import com.jbrempresa.backend.entity.Mensaje;
import com.jbrempresa.backend.repository.ContactoCanalRepository;
import com.jbrempresa.backend.repository.MensajeRepository;

@Service
public class RecepcionMensajeService {
    private final MensajeRepository mensajes;
    private final ContactoCanalRepository contactos;

    public RecepcionMensajeService(MensajeRepository mensajes, ContactoCanalRepository contactos) {
        this.mensajes = mensajes;
        this.contactos = contactos;
    }

    @Transactional
    public Mensaje recibir(Long empresaId, Mensaje entrada, String usuarioMovimiento) {
        if (entrada.getMenIdExt() != null && !entrada.getMenIdExt().isBlank()) {
            var existente = mensajes.findByEmpIdAndMenIdExt(empresaId, entrada.getMenIdExt());
            if (existente.isPresent()) return existente.get();
        }
        if (entrada.getMenCon() == null || entrada.getMenCon().isBlank()) {
            throw new IllegalArgumentException("El contenido del mensaje es obligatorio.");
        }

        entrada.setMenId(null);
        entrada.setEmpId(empresaId);
        entrada.setComId(null);
        entrada.setMenSec(null);
        entrada.setMenDir("ENTRADA");
        entrada.setMenAut("CLIENTE");
        entrada.setMenCan(normalizarCanal(entrada.getMenCan()));

        String identidad = identidad(entrada);
        if (!identidad.isBlank()) {
            ContactoCanal contacto = contactos
                    .findByEmpIdAndCocCanAndCocIdeIgnoreCase(empresaId, entrada.getMenCan(), identidad)
                    .orElseGet(() -> nuevoContacto(empresaId, entrada.getMenCan(), identidad, usuarioMovimiento));
            entrada.setCocId(contacto.getCocId());
            entrada.setMenRem(identidad);
        }

        entrada.setPerId(null);
        clasificarPorReglas(entrada);
        entrada.setMenEst("PENDIENTE_CLASIFICAR");
        LocalDateTime ahora = LocalDateTime.now();
        entrada.setMenFecRec(entrada.getMenFecRec() == null ? ahora : entrada.getMenFecRec());
        entrada.setMenUsuMov(usuarioMovimiento == null || usuarioMovimiento.isBlank() ? "SISTEMA" : usuarioMovimiento);
        entrada.setMenFecMov(ahora);
        entrada.setMenAct(true);
        return mensajes.save(entrada);
    }

    private String normalizarCanal(String canal) {
        String valor = canal == null ? "SIMULADO" : canal.trim().toUpperCase();
        if (!Arrays.asList("SIMULADO", "EMAIL", "WHATSAPP", "INTERNO", "TELEFONO").contains(valor)) {
            throw new IllegalArgumentException("Canal no permitido.");
        }
        return valor;
    }

    private String identidad(Mensaje mensaje) {
        if (!texto(mensaje.getMenRem()).isBlank()) return texto(mensaje.getMenRem());
        if ("EMAIL".equals(mensaje.getMenCan())) return texto(mensaje.getMenEmaRem());
        if ("WHATSAPP".equals(mensaje.getMenCan()) || "TELEFONO".equals(mensaje.getMenCan())) return texto(mensaje.getMenTelRem());
        if (!texto(mensaje.getMenEmaRem()).isBlank()) return texto(mensaje.getMenEmaRem());
        if (!texto(mensaje.getMenTelRem()).isBlank()) return texto(mensaje.getMenTelRem());
        return texto(mensaje.getMenDniRem());
    }

    private ContactoCanal nuevoContacto(Long empresaId, String canal, String identidad, String usuario) {
        ContactoCanal contacto = new ContactoCanal();
        contacto.setEmpId(empresaId);
        contacto.setCocCan(canal);
        contacto.setCocIde(identidad);
        contacto.setCocUsuMov(usuario == null || usuario.isBlank() ? "SISTEMA" : usuario);
        contacto.setCocFecMov(LocalDateTime.now());
        contacto.setCocAct(true);
        return contactos.save(contacto);
    }

    private void clasificarPorReglas(Mensaje mensaje) {
        String contenido = (texto(mensaje.getMenAsu()) + " " + texto(mensaje.getMenCon())).toLowerCase();
        String intencion = "OTRO";
        double confianza = .45;
        if (contiene(contenido,"presupuesto","cotizacion","cotización")){intencion="SOLICITUD_PRESUPUESTO";confianza=.85;}
        else if (contiene(contenido,"pedido","comprar","encargo")){intencion="PEDIDO";confianza=.82;}
        else if (contiene(contenido,"cita","reserva","reservar","hora")){intencion="CITA_RESERVA";confianza=.82;}
        else if (contiene(contenido,"averia","avería","problema","reclamacion","reclamación","incidencia")){intencion="INCIDENCIA";confianza=.80;}
        else if (contiene(contenido,"producto","precio","stock","disponible","teneis","tenéis")){intencion="CONSULTA_PRODUCTO";confianza=.75;}
        else if (contiene(contenido,"informacion","información","consulta","horario")){intencion="INFORMACION_GENERAL";confianza=.68;}
        mensaje.setMenInt(intencion);
        mensaje.setMenConInt(confianza);
        mensaje.setMenOriCla("REGLAS");
        mensaje.setMenEstCla("PROPUESTA");
    }

    private boolean contiene(String texto, String... valores) { return Arrays.stream(valores).anyMatch(texto::contains); }
    private String texto(String valor) { return valor == null ? "" : valor.trim(); }
}
