// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa Autowired.

// Importa LocalDateTime.
import java.time.LocalDateTime;

// Importa List.
import java.util.List;

// Importa Authentication.
import org.springframework.security.core.Authentication;

// Importa SecurityContextHolder.
import org.springframework.security.core.context.SecurityContextHolder;

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Importa Persona.
import com.jbrempresa.backend.entity.Persona;

// Importa PersonaRepository.
import com.jbrempresa.backend.repository.PersonaRepository;

// Importa DomicilioRepository.
import com.jbrempresa.backend.repository.DomicilioRepository;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/personas")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class PersonaController {

    // Repositorio de personas.
    private final PersonaRepository personaRepository;

    // Repositorio de domicilios.
    private final DomicilioRepository domicilioRepository;

    public PersonaController(
            PersonaRepository personaRepository,
            DomicilioRepository domicilioRepository) {
        this.personaRepository = personaRepository;
        this.domicilioRepository = domicilioRepository;
    }

    // Obtiene el cliente autenticado.
    private Long obtenerEmpresa() {

        // Obtiene la autenticación.
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // Obtiene el usuario.
        JwtUser usuario =
                (JwtUser) authentication.getPrincipal();

        // Devuelve el cliente.
        return usuario.getEmpresaId();

    }

    // Comprueba los datos obligatorios de la persona.
    private void validarPersona(
            Persona persona) {

        if (esTextoVacio(persona.getPerTipPer())) persona.setPerTipPer("FISICA");
        if ("JURIDICA".equals(persona.getPerTipPer())) {
            if (esTextoVacio(persona.getPerTipDoc()) || esTextoVacio(persona.getPerDoc())
                    || esTextoVacio(persona.getPerRazSocCor()) || esTextoVacio(persona.getPerRazSocLar())) {
                throw new IllegalArgumentException("Debe informar el documento y las razones sociales de la persona jurídica.");
            }
        } else if (esTextoVacio(persona.getPerTipDoc()) ||
                esTextoVacio(persona.getPerDoc()) ||
                esTextoVacio(persona.getPerNom()) ||
                esTextoVacio(persona.getPerApe1())) {

            throw new IllegalArgumentException(
                    "Debe informar los campos obligatorios de la persona.");

        }

        persona.setPerNomCom(construirNombreCompleto(persona));

    }

    private String construirNombreCompleto(Persona persona) {
        String denominacion;
        if ("JURIDICA".equals(persona.getPerTipPer())) {
            denominacion = persona.getPerRazSocLar().trim();
        } else {
            denominacion = String.join(" ",
                    persona.getPerNom().trim(),
                    persona.getPerApe1().trim(),
                    esTextoVacio(persona.getPerApe2()) ? "" : persona.getPerApe2().trim())
                    .replaceAll("\\s+", " ")
                    .trim();
        }
        return persona.getPerDoc().trim() + " - " + denominacion;
    }

    // Comprueba si un texto esta vacio.
    private boolean esTextoVacio(
            String texto) {

        return texto == null || texto.isBlank();

    }

    // Comprueba que el domicilio pertenece al cliente.
    private void comprobarDomicilio(
            Long empId,
            Long domId) {

        // Permite personas sin domicilio asociado.
        if (domId == null || domId == 0) {
            return;
        }

        domicilioRepository
                .findByEmpIdAndDomIdAndDomActTrue(
                        empId,
                        domId)
                .orElseThrow(() -> new RuntimeException(
                        "Domicilio no encontrado para el cliente."));

    }

    // Obtiene el usuario autenticado.
    private String obtenerUsuario() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        JwtUser usuario =
                (JwtUser) authentication.getPrincipal();

        return usuario.getUsername();

    }

    // Guarda una persona.
    @PostMapping
    @Transactional
    public Persona guardar(
            @RequestBody Persona persona) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Comprueba los campos obligatorios.
        validarPersona(persona);

        // Asigna el cliente.
        persona.setEmpId(empId);

        // Comprueba el domicilio asociado.
        comprobarDomicilio(
                empId,
                persona.getDomId());

        // Asigna el usuario de modificacion.
        persona.setPerUsuMov(obtenerUsuario());

        // El identificador de persona se mantiene entre todas sus versiones.
        persona.setPerId(personaRepository.obtenerSiguienteId(obtenerEmpresa()));
        persona.setPerIdHis(1L);
        persona.setPerTipMov("A");
        if (persona.getPerCauMov() == null || persona.getPerCauMov().isBlank()) persona.setPerCauMov("Alta del registro");
        persona.setPerAct(true);

        // Asigna la fecha.
        persona.setPerFecMov(LocalDateTime.now());

        // Guarda el registro.
        return personaRepository.save(persona);

    }

    // Actualiza una persona.
    @PutMapping("/{id}")
    @Transactional
    public Persona actualizar(
            @PathVariable("id") Long id,
            @RequestBody Persona persona) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Comprueba los campos obligatorios.
        validarPersona(persona);

        // Obtiene la versión vigente.
        Persona anterior = personaRepository.findByEmpIdAndPerIdAndPerActTrue(empId, id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada."));

        if ("B".equals(anterior.getPerTipMov())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Una persona dada de baja no se puede modificar. Deshaga primero la baja.");
        }

        anterior.setPerAct(false);
        personaRepository.save(anterior);

        // Asigna el identificador.
        persona.setPerId(id);
        persona.setPerIdHis(anterior.getPerIdHis() + 1);
        persona.setPerTipMov("M");
        if (persona.getPerCauMov() == null || persona.getPerCauMov().isBlank()) persona.setPerCauMov("Modificación del registro");
        persona.setPerAct(true);

        // Asigna el cliente.
        persona.setEmpId(empId);

        // Comprueba el domicilio asociado.
        comprobarDomicilio(
                empId,
                persona.getDomId());

        // Asigna el usuario de modificacion.
        persona.setPerUsuMov(obtenerUsuario());

        // Asigna la fecha.
        persona.setPerFecMov(LocalDateTime.now());

        // Guarda el registro.
        return personaRepository.save(persona);

    }

    // Obtiene las personas.
    @GetMapping
    public List<Persona> obtenerPersonas() {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Devuelve los registros.
        return personaRepository.findByEmpIdAndPerActTrueOrderByPerId(empId);

    }

    // Obtiene el siguiente ID.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve el identificador.
        return personaRepository.obtenerSiguienteId(obtenerEmpresa());

    }

    // Elimina definitivamente una persona que todavía no tiene histórico.
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminar(
            @PathVariable("id") Long id) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Busca la persona.
        personaRepository
                        .findByEmpIdAndPerIdAndPerActTrue(empId, id)
                        .orElseThrow(() -> new RuntimeException("Persona no encontrada."));

        personaRepository.deleteAll(personaRepository.findByEmpIdAndPerIdOrderByPerFecMovDesc(empId, id));
    }

    // Maestro único de Personas para los selectores del resto de módulos.
    @GetMapping("/selector")
    public List<Persona> obtenerPersonasSelector() {
        Long empId = obtenerEmpresa();
        return personaRepository.findByEmpIdAndPerActTrueOrderByPerId(empId).stream()
                .filter(persona -> !"B".equals(persona.getPerTipMov()))
                .toList();
    }

    // Registra una baja lógica conservando todas las versiones anteriores.
    @PostMapping("/{id}/baja")
    @Transactional
    public Persona baja(@PathVariable("id") Long id) {
        Long empId = obtenerEmpresa();
        Persona anterior = personaRepository.findByEmpIdAndPerIdAndPerActTrue(empId, id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada."));
        if ("B".equals(anterior.getPerTipMov())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La persona ya está dada de baja.");
        }

        anterior.setPerAct(false);
        personaRepository.save(anterior);

        Persona baja = copiarPersona(anterior);
        baja.setPerIdHis(anterior.getPerIdHis() + 1);
        baja.setPerTipMov("B");
        baja.setPerCauMov("Baja del registro");
        baja.setPerUsuMov(obtenerUsuario());
        baja.setPerFecMov(LocalDateTime.now());
        baja.setPerAct(true);
        return personaRepository.save(baja);
    }

    @GetMapping("/{id}/historico")
    public List<Persona> obtenerHistorico(@PathVariable("id") Long id) {
        Long empId = obtenerEmpresa();
        personaRepository.findByEmpIdAndPerIdAndPerActTrue(empId, id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada."));
        return personaRepository.findByEmpIdAndPerIdOrderByPerFecMovDesc(empId, id);
    }

    @PostMapping("/{id}/deshacer")
    @Transactional
    public Persona deshacer(@PathVariable("id") Long id) {
        Long empId = obtenerEmpresa();
        Persona actual = personaRepository.findByEmpIdAndPerIdAndPerActTrue(empId, id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada."));

        if (actual.getPerIdHis() <= 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No existen movimientos anteriores para deshacer.");
        }

        Persona anterior = personaRepository
                .findByEmpIdAndPerIdAndPerIdHis(empId, id, actual.getPerIdHis() - 1)
                .orElseThrow(() -> new RuntimeException("Movimiento histórico anterior no encontrado."));

        personaRepository.delete(actual);
        anterior.setPerAct(true);
        return personaRepository.save(anterior);
    }

    private Persona copiarPersona(Persona origen) {
        Persona copia = new Persona();
        copia.setEmpId(origen.getEmpId());
        copia.setPerId(origen.getPerId());
        copia.setPerTipPer(origen.getPerTipPer());
        copia.setPerRazSocCor(origen.getPerRazSocCor());
        copia.setPerRazSocLar(origen.getPerRazSocLar());
        copia.setPerCauMov(origen.getPerCauMov());
        copia.setPerTipDoc(origen.getPerTipDoc());
        copia.setPerDoc(origen.getPerDoc());
        copia.setPerNomCom(origen.getPerNomCom());
        copia.setPerNom(origen.getPerNom());
        copia.setPerApe1(origen.getPerApe1());
        copia.setPerApe2(origen.getPerApe2());
        copia.setPerFecNac(origen.getPerFecNac());
        copia.setPerTel(origen.getPerTel());
        copia.setPerEma(origen.getPerEma());
        copia.setDomId(origen.getDomId());
        copia.setPerCoX(origen.getPerCoX());
        copia.setPerCoY(origen.getPerCoY());
        copia.setPerHus(origen.getPerHus());
        return copia;
    }

}
