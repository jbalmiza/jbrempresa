package com.jbrempresa.backend.core.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.jbrempresa.backend.repository.EmpresaRepository;

@Component
public class ParametrosProductosIniciales implements ApplicationRunner {
    private final EmpresaRepository empresas;
    private final ConfiguracionBeneficioProductosService beneficio;
    public ParametrosProductosIniciales(EmpresaRepository empresas, ConfiguracionBeneficioProductosService beneficio) {
        this.empresas = empresas; this.beneficio = beneficio;
    }
    @Override public void run(ApplicationArguments args) {
        empresas.findAll().forEach(e -> beneficio.garantizar(e.getEmpId()));
    }
}
