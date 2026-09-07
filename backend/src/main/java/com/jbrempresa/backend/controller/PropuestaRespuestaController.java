package com.jbrempresa.backend.controller;

import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.*;

@RestController
@RequestMapping("/comunicaciones/{comunicacionId}/propuestas-respuesta")
public class PropuestaRespuestaController {
    private final ComunicacionRepository comunicaciones;
    private final MensajeRepository mensajes;
    private final ProductoRepository productos;
    private final PropuestaRespuestaRepository propuestas;
    private final ContextoOperacion contexto;

    public PropuestaRespuestaController(ComunicacionRepository comunicaciones, MensajeRepository mensajes,
            ProductoRepository productos, PropuestaRespuestaRepository propuestas, ContextoOperacion contexto) {
        this.comunicaciones = comunicaciones; this.mensajes = mensajes; this.productos = productos;
        this.propuestas = propuestas; this.contexto = contexto;
    }

    @GetMapping
    public List<PropuestaRespuesta> consultar(@PathVariable Long comunicacionId) {
        conversacion(comunicacionId);
        return propuestas.findByEmpIdAndComIdOrderByPrrFecGenDesc(cli(), comunicacionId);
    }

    @PostMapping
    @Transactional
    public PropuestaRespuesta generar(@PathVariable Long comunicacionId) {
        Comunicacion comunicacion = conversacion(comunicacionId);
        if (!"CONSULTA_PRODUCTO".equals(comunicacion.getComInt()))
            throw error("La primera versión solo genera respuestas para consultas de producto.");
        Mensaje ultimoCliente = mensajes.findByEmpIdAndComIdAndMenActTrueOrderByMenSec(cli(), comunicacionId).stream()
                .filter(m -> "CLIENTE".equals(m.getMenAut()) && "ENTRADA".equals(m.getMenDir()))
                .reduce((a, b) -> b).orElseThrow(() -> error("La conversación no contiene un mensaje de cliente."));
        PropuestaRespuesta p = new PropuestaRespuesta();
        p.setEmpId(cli()); p.setComId(comunicacionId); p.setPrrCon(respuestaCatalogo(ultimoCliente.getMenCon()));
        p.setPrrOri("CATALOGO_REGLAS"); p.setPrrEst("PENDIENTE"); p.setPrrUsuGen(contexto.nombreUsuario());
        p.setPrrFecGen(contexto.fechaActual());
        return propuestas.save(p);
    }

    @PostMapping("/{propuestaId}/aprobar")
    @Transactional
    public Mensaje aprobar(@PathVariable Long comunicacionId, @PathVariable Long propuestaId,
            @RequestBody Map<String, String> datos) {
        Comunicacion c = conversacion(comunicacionId);
        PropuestaRespuesta p = propuesta(comunicacionId, propuestaId);
        if (!"PENDIENTE".equals(p.getPrrEst())) throw error("La propuesta ya fue procesada.");
        String contenido = texto(datos.get("contenido"));
        if (contenido.isBlank()) throw error("La respuesta aprobada no puede estar vacía.");
        p.setPrrConApr(contenido); p.setPrrEst("APROBADA"); p.setPrrUsuApr(contexto.nombreUsuario());
        p.setPrrFecApr(contexto.fechaActual()); propuestas.save(p);
        Mensaje m = new Mensaje(); m.setEmpId(cli()); m.setComId(comunicacionId);
        m.setMenSec(mensajes.siguienteSecuencia(cli(), comunicacionId)); m.setMenDir("SALIDA"); m.setMenAut("IA");
        m.setMenCon(contenido); m.setMenEst("PENDIENTE_ENVIO"); m.setMenUsuMov(contexto.nombreUsuario());
        m.setMenFecMov(contexto.fechaActual()); m.setMenAct(true); Mensaje guardado = mensajes.save(m);
        c.setComEst("PENDIENTE_CLIENTE"); c.setComFecUlt(contexto.fechaActual());
        c.setComUsuMov(contexto.nombreUsuario()); c.setComFecMov(contexto.fechaActual()); comunicaciones.save(c);
        return guardado;
    }

    @PostMapping("/{propuestaId}/rechazar")
    @Transactional
    public PropuestaRespuesta rechazar(@PathVariable Long comunicacionId, @PathVariable Long propuestaId) {
        conversacion(comunicacionId); PropuestaRespuesta p = propuesta(comunicacionId, propuestaId);
        if (!"PENDIENTE".equals(p.getPrrEst())) throw error("La propuesta ya fue procesada.");
        p.setPrrEst("RECHAZADA"); p.setPrrUsuApr(contexto.nombreUsuario()); p.setPrrFecApr(contexto.fechaActual());
        return propuestas.save(p);
    }

    private String respuestaCatalogo(String consulta) {
        Set<String> terminos = new HashSet<>();
        Arrays.stream(normalizar(consulta).split("\\s+")).map(this::raiz).forEach(terminos::add);
        terminos.removeIf(t -> t.length() < 3 || Set.of("que","los","las","una","uno","para","con","tiene","teneis","hola","buena","cuanto","valen","precio").contains(t));
        List<Producto> encontrados = productos.findByEmpIdAndProActTrueOrderByProId(cli()).stream()
                .filter(p -> !"B".equals(p.getProTipMov()))
                .sorted(Comparator.comparingInt((Producto p) -> coincidencias(p, terminos)).reversed())
                .filter(p -> coincidencias(p, terminos) > 0).limit(5).toList();
        if (encontrados.isEmpty()) return "Gracias por tu consulta. No he encontrado una coincidencia clara en el catálogo. ¿Puedes indicarnos el producto, marca o modelo que necesitas?";
        StringBuilder r = new StringBuilder("Gracias por tu consulta. He encontrado estas opciones en nuestro catálogo:\n");
        for (Producto p : encontrados) {
            r.append("• ").append(p.getProNom());
            if (p.getProMar()!=null&&!p.getProMar().isBlank()) r.append(" · ").append(p.getProMar());
            if (p.getProMod()!=null&&!p.getProMod().isBlank()) r.append(" ").append(p.getProMod());
            if (p.getProPreFin()!=null) r.append(" · ").append(p.getProPreFin().setScale(2, RoundingMode.HALF_UP)).append(" € precio final");
            else if (p.getProPreVen()!=null) r.append(" · ").append(p.getProPreVen().setScale(2, RoundingMode.HALF_UP)).append(" €");
            if (Boolean.TRUE.equals(p.getProConSto())) r.append(p.getProStoAct()!=null&&p.getProStoAct()>0?" · Disponible":" · Sin stock");
            r.append("\n");
        }
        return r.append("¿Quieres información más detallada o que preparemos un presupuesto?").toString();
    }

    private int coincidencias(Producto p, Set<String> terminos) {
        Set<String> ficha = new HashSet<>();
        Arrays.stream(normalizar(texto(p.getProNom())+" "+texto(p.getProDes())+" "+texto(p.getProMar())+" "+texto(p.getProMod())+" "+texto(p.getProCat())+" "+texto(p.getProSubCat())).split("\\s+"))
                .map(this::raiz).forEach(ficha::add);
        return (int) terminos.stream().filter(ficha::contains).count();
    }
    private String raiz(String palabra){if(palabra.length()>5&&palabra.endsWith("es"))return palabra.substring(0,palabra.length()-2);if(palabra.length()>4&&palabra.endsWith("s"))return palabra.substring(0,palabra.length()-1);return palabra;}
    private String normalizar(String v){return Normalizer.normalize(texto(v).toLowerCase(),Normalizer.Form.NFD).replaceAll("\\p{M}","").replaceAll("[^a-z0-9]+"," ").trim();}
    private String texto(String v){return v==null?"":v.trim();}
    private Long cli(){return contexto.empresaId();}
    private Comunicacion conversacion(Long id){return comunicaciones.findByEmpIdAndComIdAndComActTrue(cli(),id).orElseThrow(()->error("Conversación no encontrada."));}
    private PropuestaRespuesta propuesta(Long comId,Long id){return propuestas.findByEmpIdAndPrrId(cli(),id).filter(p->comId.equals(p.getComId())).orElseThrow(()->error("Propuesta no encontrada."));}
    private IllegalArgumentException error(String mensaje){return new IllegalArgumentException(mensaje);}
}
