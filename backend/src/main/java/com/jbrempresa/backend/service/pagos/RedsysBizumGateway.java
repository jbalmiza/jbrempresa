package com.jbrempresa.backend.service.pagos;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.core.config.ConfiguracionRedsysBizumService;

@Service
public class RedsysBizumGateway implements PasarelaPago {
    private final ConfiguracionRedsysBizumService configuracion;

    public RedsysBizumGateway(ConfiguracionRedsysBizumService configuracion) {
        this.configuracion = configuracion;
    }

    @Override
    public InicioPago iniciar(SolicitudPago solicitud) {
        var estado = configuracion.estado(solicitud.empresaId());
        if (!estado.preparada()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Bizum mediante Redsys no está configurado para esta empresa.");
        }
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                "La conexión real con Redsys se habilitará al disponer de credenciales y documentación del terminal.");
    }

    @Override
    public ResultadoPago confirmar(Map<String, String> parametrosFirmados) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED,
                "La validación de firma Redsys todavía no está habilitada.");
    }
}
