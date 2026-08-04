// Define el paquete.
package com.jbrempresa.backend.service;

// Importa LocalDateTime.
import java.time.LocalDateTime;

// Importa Optional.
import java.util.Optional;

// Importa Autowired.
import org.springframework.beans.factory.annotation.Autowired;

// Importa Service.
import org.springframework.stereotype.Service;

// Importa Malla.
import com.jbrempresa.backend.entity.Malla;

// Importa Producto.
import com.jbrempresa.backend.entity.Producto;

// Importa MallaRepository.
import com.jbrempresa.backend.repository.MallaRepository;

// Define el servicio.
@Service
public class MallaService {

    // Repositorio de mallas.
    @Autowired
    private MallaRepository mallaRepository;

    /**
     * Guarda en la tabla mallas la posición de un producto.
     */
    public void guardarDesdeProducto(
            Producto producto) {

        // Obtiene el cliente.
        Long cliId = producto.getCliId();

        // Crea la malla.
        Malla malla = new Malla();

        // Obtiene el siguiente identificador.
        Long siguienteId = mallaRepository.obtenerSiguienteId();

        // Si no existe ningún registro.
        if (siguienteId == null) {

            // Asigna el primer identificador.
            siguienteId = 1L;

        }

        // Asigna el identificador.
        malla.setMalId(siguienteId);

        // Asigna el cliente.
        malla.setCliId(cliId);

        // Asigna la entidad.
        malla.setMalEnt("PRODUCTOS");

        // Asigna la fila.
        malla.setMalFil(producto.getProFilMal());

        // Asigna la columna.
        malla.setMalCol(producto.getProColMal());

        // Asigna el tipo.
        malla.setMalTip("PRODUCTO");

        // Asigna la referencia al producto.
        malla.setMalRefId(producto.getProId());

        // Asigna la descripción.
        malla.setMalDes(producto.getProDes());

        // Asigna el estado.
        malla.setMalAct(producto.getProAct());

        // Asigna el usuario de modificación.
        malla.setMalUsuMov(producto.getProUsuMov());

        // Asigna la fecha de modificación.
        malla.setMalFecMov(producto.getProFecMov());

        // Guarda la malla.
        mallaRepository.save(malla);

    }

    /**
     * Actualiza la posición de un producto en la tabla mallas.
     * Si la malla no existe, crea un nuevo registro.
     */
    public void actualizarDesdeProducto(
            Producto producto) {

        // Obtiene el cliente.
        Long cliId = producto.getCliId();

        // Busca la malla del producto.
        Optional<Malla> opt =
                mallaRepository.findByCliIdAndMalEntAndMalRefId(
                        cliId,
                        "PRODUCTOS",
                        producto.getProId());

        // Si existe la malla.
        if (opt.isPresent()) {

            // Obtiene la malla.
            Malla malla = opt.get();

            // Asigna la fila.
            malla.setMalFil(producto.getProFilMal());

            // Asigna la columna.
            malla.setMalCol(producto.getProColMal());

            // Asigna la descripción.
            malla.setMalDes(producto.getProDes());

            // Asigna el estado.
            malla.setMalAct(producto.getProAct());

            // Asigna el usuario de modificación.
            malla.setMalUsuMov(producto.getProUsuMov());

            // Asigna la fecha de modificación.
            malla.setMalFecMov(producto.getProFecMov());

            // Guarda los cambios.
            mallaRepository.save(malla);

        }

        // Si no existe la malla.
        else {

            // Crea la malla del producto.
            guardarDesdeProducto(producto);

        }

    }

    /**
     * Elimina la posición de un producto de la tabla mallas.
     */
    public void eliminarDesdeProducto(
            Long cliId,
            Long proId) {

        // Elimina la malla del producto.
        mallaRepository.deleteByCliIdAndMalEntAndMalRefId(
                cliId,
                "PRODUCTOS",
                proId);

    }

    /**
     * Pinta una posición de la malla.
     * Si la posición ya existe, modifica su tipo.
     * Si la posición no existe, crea un nuevo registro.
     */
    public Malla pintar(
            Long cliId,
            Malla datos) {

        // Busca la posición dentro de la malla de la entidad indicada.
        Optional<Malla> opt =
                mallaRepository.findByCliIdAndMalEntAndMalFilAndMalCol(
                        cliId,
                        datos.getMalEnt(),
                        datos.getMalFil(),
                        datos.getMalCol());

        // Si la posición ya existe.
        if (opt.isPresent()) {

            // Obtiene el registro existente.
            Malla malla = opt.get();

            // Las posiciones ocupadas por registros reales
            // no se pueden pintar desde el editor.
            if (malla.getMalRefId() != null &&
            	    malla.getMalRefId() > 0) {

                throw new RuntimeException(
                        "La posición está ocupada por un registro.");

            }

            // Modifica el color de la posición.
            malla.setMalTip(datos.getMalTip());

            // Asigna la descripción.
            malla.setMalDes(datos.getMalDes());

            // Asigna el estado.
            malla.setMalAct(true);

            // Asigna el usuario de modificación.
            malla.setMalUsuMov(datos.getMalUsuMov());

            // Asigna la fecha de modificación.
            malla.setMalFecMov(LocalDateTime.now());

            // Guarda los cambios.
            return mallaRepository.save(malla);

        }

        // Crea una nueva posición.
        Malla malla = new Malla();

        // Obtiene el siguiente identificador.
        Long siguienteId = mallaRepository.obtenerSiguienteId();

        // Si no existe ningún registro.
        if (siguienteId == null) {

            // Asigna el primer identificador.
            siguienteId = 1L;

        }

        // Asigna el identificador.
        malla.setMalId(siguienteId);

        // Asigna el cliente.
        malla.setCliId(cliId);

        // Asigna la entidad.
        malla.setMalEnt(datos.getMalEnt());

        // Asigna la fila.
        malla.setMalFil(datos.getMalFil());

        // Asigna la columna.
        malla.setMalCol(datos.getMalCol());

        // Asigna el color.
        malla.setMalTip(datos.getMalTip());

        // Una celda pintada no pertenece a ningún registro.
        malla.setMalRefId(null);

        // Asigna la descripción.
        malla.setMalDes(datos.getMalDes());

        // Asigna el estado.
        malla.setMalAct(true);

        // Asigna el usuario de modificación.
        malla.setMalUsuMov(datos.getMalUsuMov());

        // Asigna la fecha de modificación.
        malla.setMalFecMov(LocalDateTime.now());

        // Guarda y devuelve la posición.
        return mallaRepository.save(malla);

    }
    
    /**
     * Elimina una posición pintada manualmente de la malla.
     */
    public void borrarPosicion(
            Long cliId,
            String malEnt,
            Integer malFil,
            Integer malCol) {

        // Busca la posición.
        Optional<Malla> opt =
                mallaRepository.findByCliIdAndMalEntAndMalFilAndMalCol(
                        cliId,
                        malEnt,
                        malFil,
                        malCol);

        // Si la posición no existe, no hace nada.
        if (opt.isEmpty()) {

            return;

        }

        // Obtiene la malla.
        Malla malla = opt.get();

        // Comprueba si pertenece a un registro real.
        if (malla.getMalRefId() != null &&
            malla.getMalRefId() > 0) {

            throw new RuntimeException(
                    "No se puede borrar una posición ocupada por un registro.");

        }

        // Elimina la posición pintada.
        mallaRepository.delete(malla);

    }

}