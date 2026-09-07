// Define el paquete.
package com.jbrempresa.backend.controller;

// Importa LocalDateTime.
import java.time.LocalDateTime;

// Importa List.
import java.util.List;

// Importa Autowired.

// Importa Authentication.
import org.springframework.security.core.Authentication;

// Importa SecurityContextHolder.
import org.springframework.security.core.context.SecurityContextHolder;

// Importa las anotaciones REST.
import org.springframework.web.bind.annotation.*;

// Importa Malla.
import com.jbrempresa.backend.entity.Malla;

// Importa MallaRepository.
import com.jbrempresa.backend.repository.MallaRepository;

// Importa MallaService.
import com.jbrempresa.backend.service.MallaService;

// Importa JwtUser.
import com.jbrempresa.backend.security.JwtUser;

// Define el controlador.
@RestController

// Define la ruta base.
@RequestMapping("/mallas")

// Permite peticiones desde Angular.
@CrossOrigin(origins = "http://localhost:4200")
public class MallaController {

    // Repositorio de mallas.
    private final MallaRepository mallaRepository;

    // Servicio de mallas.
    private final MallaService mallaService;

    public MallaController(MallaRepository mallaRepository, MallaService mallaService) {
        this.mallaRepository = mallaRepository;
        this.mallaService = mallaService;
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

    // Guarda una malla.
    @PostMapping
    public Malla guardar(
            @RequestBody Malla malla) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Asigna el cliente.
        malla.setEmpId(empId);

        // Asigna el usuario de modificacion.
        malla.setMalUsuMov(obtenerUsuario());

        // Si el identificador es 0, se trata de un registro nuevo.
        if (malla.getMalId() != null && malla.getMalId() == 0) {

            // Elimina el identificador recibido.
            malla.setMalId(null);

        }

        // Asigna la fecha.
        malla.setMalFecMov(LocalDateTime.now());

        // Guarda el registro.
        return mallaRepository.save(malla);

    }

    // Actualiza una malla.
    @PutMapping("/{id}")
    public Malla actualizar(
            @PathVariable Long id,
            @RequestBody Malla malla) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Comprueba que la malla pertenece al cliente.
        mallaRepository.findByMalIdAndEmpId(
                id,
                empId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Malla no encontrada."));

        // Asigna el identificador.
        malla.setMalId(id);

        // Asigna el cliente.
        malla.setEmpId(empId);

        // Asigna el usuario de modificacion.
        malla.setMalUsuMov(obtenerUsuario());

        // Asigna la fecha.
        malla.setMalFecMov(LocalDateTime.now());

        // Guarda el registro.
        return mallaRepository.save(malla);

    }

    // Obtiene las mallas.
    @GetMapping
    public List<Malla> obtenerMallas() {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Devuelve los registros del cliente.
        return mallaRepository.findByEmpId(empId);

    }

    // Obtiene el siguiente ID orientativo.
    @GetMapping("/siguiente-id")
    public Long obtenerSiguienteId() {

        // Devuelve un identificador solo para mostrarlo en pantalla.
        return mallaRepository.obtenerSiguienteId(obtenerEmpresa());

    }

    // Pinta una posición de la malla.
    @PostMapping("/pintar")
    public Malla pintar(
            @RequestBody Malla malla) {

        // Obtiene el cliente autenticado.
        Long empId = obtenerEmpresa();

        // Asigna el usuario autenticado.
        malla.setMalUsuMov(obtenerUsuario());

        // Pinta la posición utilizando el servicio.
        return mallaService.pintar(
                empId,
                malla);

    }
    
 // Borra una posición pintada de la malla.
    @DeleteMapping("/posicion/{entidad}/{fila}/{columna}")
    public void borrarPosicion(
            @PathVariable String entidad,
            @PathVariable Integer fila,
            @PathVariable Integer columna) {

        // Obtiene el cliente autenticado.
        Long empId = obtenerEmpresa();

        // Elimina la posición.
        mallaService.borrarPosicion(
                empId,
                entidad,
                fila,
                columna);

    }

    // Elimina una malla.
    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        // Obtiene el cliente.
        Long empId = obtenerEmpresa();

        // Busca la malla.
        Malla malla =
                mallaRepository
                        .findByMalIdAndEmpId(
                                id,
                                empId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Malla no encontrada."));

        // Elimina el registro.
        mallaRepository.delete(malla);

    }

}
