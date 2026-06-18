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
import com.jbrempresa.backend.entity.Persona;

//Importa PersonaRepository.
//El repository contiene los métodos CRUD automáticos de JPA.
import com.jbrempresa.backend.repository.PersonaRepository;

import java.util.List;

//Indica que esta clase es un controlador REST.
//Un controlador REST recibe peticiones HTTP desde Angular.
@RestController

//Define la ruta base del controlador.
//Todas las URLs empezarán por:
/*
http://localhost:8080/personas
*/
@RequestMapping("/personas")

//Permite conexiones desde Angular.
//Angular normalmente funciona en localhost:4200.
//Sin esto el navegador bloquearía las peticiones por seguridad CORS.
@CrossOrigin(origins = "http://localhost:4200")

//Define la clase PersonaController.
//Esta clase gestionará las operaciones REST de Persona.
public class PersonaController {

    // @Autowired hace que Spring cree automáticamente
    // un objeto PersonaRepository y lo inyecte aquí.
    @Autowired
    
    // Variable que permitirá acceder a base de datos.
    private PersonaRepository personaRepository;

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
    public Persona guardar(@RequestBody Persona persona) {

        // save() guarda automáticamente en PostgreSQL.
        //
        // Si el ID no existe:
        // INSERT
        //
        // Si el ID existe:
        // UPDATE
        return personaRepository.save(persona);

    }
    
    // @GetMapping responde a peticiones GET.
    //
    // URL:
    // GET http://localhost:8080/personas
    @GetMapping

    public List<Persona> obtenerPersonas() {

        // findAll() obtiene todos los registros
        // de la tabla personas.
        return personaRepository.findAll();

    }
    
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        return personaRepository.obtenerSiguienteId();

    }
    
    // Elimina una persona.
    //
    // URL:
    // DELETE http://localhost:8080/personas/1
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {

        personaRepository.deleteById(id);

    }

}