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

//Importa la entidad Producto.
//Esta entidad representa la tabla VENTA de PostgreSQL.
import com.jbrempresa.backend.entity.Compra;

//Importa VentaRepository.
//El repository contiene los métodos CRUD automáticos de JPA.
import com.jbrempresa.backend.repository.CompraRepository;

import java.util.List;

//Indica que esta clase es un controlador REST.
//Un controlador REST recibe peticiones HTTP desde Angular.
@RestController

//Define la ruta base del controlador.
//Todas las URLs empezarán por:
/*
http://localhost:8080/ventas
*/
@RequestMapping("/compras")

//Permite conexiones desde Angular.
//Angular normalmente funciona en localhost:4200.
//Sin esto el navegador bloquearía las peticiones por seguridad CORS.
@CrossOrigin(origins = "http://localhost:4200")

//Define la clase PersonaController.
//Esta clase gestionará las operaciones REST de Venta.
public class CompraController {

    // @Autowired hace que Spring cree automáticamente
    // un objeto PersonaRepository y lo inyecte aquí.
    @Autowired
    
    // Variable que permitirá acceder a base de datos.
    private CompraRepository compraRepository;

    // @PostMapping indica que este método responderá
    // a peticiones HTTP POST.
    //
    // URL:
    // POST http://localhost:8080/productos
    @PostMapping
    
    // Método guardar.
    // Recibe un Producto desde Angular y devuelve el producto guardado.
    
    // @RequestBody significa:
    // convierte automáticamente el JSON recibido
    // en un objeto Venta Java.
    public Compra guardar(@RequestBody Compra compra) {

        // save() guarda automáticamente en PostgreSQL.
        //
        // Si el ID no existe:
        // INSERT
        //
        // Si el ID existe:
        // UPDATE
        return compraRepository.save(compra);

    }
    
    // @GetMapping responde a peticiones GET.
    //
    // URL:
    // GET http://localhost:8080/productos
    @GetMapping

    public List<Compra> obtenerCompras() {

        // findAll() obtiene todos los registros
        // de la tabla productos.
        return compraRepository.findAll();

    }
    
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        return compraRepository.obtenerSiguienteId();

    }
    
    // Elimina un producto.
    //
    // URL:
    // DELETE http://localhost:8080/productos/1
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {

        compraRepository.deleteById(id);

    }

}