// Define el paquete donde está ubicado este archivo Java
package com.jbrempresa.backend.controller;

//Importa la anotación @Autowired.
//Sirve para que Spring inyecte automáticamente objetos necesarios.
import org.springframework.beans.factory.annotation.Autowired;

//Importa todas las anotaciones REST de Spring:
//@RestController
//@RequestMapping
//@PostMapping
//@RequestBody
//@CrossOrigin
import org.springframework.web.bind.annotation.*;

//Importa la entidad Persona.
//Esta entidad representa la tabla PERSONA de PostgreSQL.
import com.jbrempresa.backend.entity.Domicilio;

//Importa PersonaRepository.
//El repository contiene los métodos CRUD automáticos de JPA.
import com.jbrempresa.backend.repository.DomicilioRepository;

import java.util.List;

//Indica que esta clase es un controlador REST.
//Un controlador REST recibe peticiones HTTP desde Angular.
@RestController

//Define la ruta base del controlador.
//Todas las URLs empezarán por:
/*
http://localhost:8080/personas
*/
@RequestMapping("/domicilio")

//Permite conexiones desde Angular.
//Angular normalmente funciona en localhost:4200.
//Sin esto el navegador bloquearía las peticiones por seguridad CORS.
@CrossOrigin(origins = "http://localhost:4200")

//Define la clase PersonaController.
//Esta clase gestionará las operaciones REST de Persona.
public class DomicilioController {

    // @Autowired hace que Spring cree automáticamente
    // un objeto PersonaRepository y lo inyecte aquí.
    @Autowired
    
    // Variable que permitirá acceder a base de datos.
    private DomicilioRepository domicilioRepository;

    // @PostMapping indica que este método responderá
    // a peticiones HTTP POST.
    //
    // URL:
    // POST http://localhost:8080/personas
    @PostMapping
    
    // Método guardar.
    // Recibe una Persona desde Angular y devuelve la Persona guardada.
    
    // @RequestBody significa:
    // convierte automáticamente el JSON recibido
    // en un objeto Persona Java.
    public Domicilio guardar(@RequestBody Domicilio domicilio) {

        // save() guarda automáticamente en PostgreSQL.
        //
        // Si el ID no existe:
        // INSERT
        //
        // Si el ID existe:
        // UPDATE
        return domicilioRepository.save(domicilio);

    }
    
    // @GetMapping responde a peticiones GET.
    //
    // URL:
    // GET http://localhost:8080/domicilios
    @GetMapping

    public List<Domicilio> obtenerDomicilios() {

        // findAll() obtiene todos los registros
        // de la tabla domicilios.
        return domicilioRepository.findAll();

    }
    
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        return domicilioRepository.obtenerSiguienteId();

    }
    
    // Elimina un domicilio.
    //
    // URL:
    // DELETE http://localhost:8080/usuarios/1
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {

        domicilioRepository.deleteById(id);

    }

}