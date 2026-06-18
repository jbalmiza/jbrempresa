// 4º El backend que recibe peticiones y devuelve la respuesta (Framework Spring Boot / Lenguaje java)

// Define el paquete donde está la clase.
package com.jbrempresa.backend.controller;

// Permite que Angular pueda llamar al backend.
import org.springframework.web.bind.annotation.CrossOrigin;

// Sirve para crear endpoints HTTP GET.
import org.springframework.web.bind.annotation.GetMapping;

// Sirve para definir una ruta base.
import org.springframework.web.bind.annotation.RequestMapping;

// Indica que esta clase es un controlador REST.
import org.springframework.web.bind.annotation.RestController;

// Le dice a Spring Boot: esta clase responderá peticiones HTTP
@RestController

// Define la ruta base:
@RequestMapping("/api")

// Permite que Angular (localhost:4200) acceda al backend. Sin esto el navegador bloquea la petición.
@CrossOrigin(origins = "http://localhost:4200")

// Define la clase del controlador.
public class HelloController {

// Define el endpoint: /api/hello usando HTTP GET
    @GetMapping("/hello")
    
// Método que se ejecuta cuando Angular llama:
    public String hello() {
    	
// Devuelve texto al frontend Angular.
        return "Backend funcionando";
    }
}