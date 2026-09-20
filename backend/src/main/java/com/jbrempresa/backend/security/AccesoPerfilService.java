package com.jbrempresa.backend.security;

import java.util.Set;
import org.springframework.stereotype.Service;
import com.jbrempresa.backend.entity.Perfil;
import com.jbrempresa.backend.repository.PerfilRepository;

@Service
public class AccesoPerfilService {
    private static final Set<String> TODOS = Set.of("ADMINISTRACION","COMUNICACIONES","TERRITORIO","PERSONAS","CLIENTES","EMPLEADOS","PROVEEDORES","PRODUCTOS","SERVICIOS","COMPRAS","VENTAS","RECURSOS","CAJA");
    private final PerfilRepository perfiles;
    public AccesoPerfilService(PerfilRepository perfiles){this.perfiles=perfiles;}
    public Perfil perfil(JwtUser usuario){return perfiles.findByEmpIdAndPerId(usuario.getEmpresaId(),usuario.getPerfilId()).orElseThrow();}
    public boolean permite(JwtUser usuario,String modulo){
        String nombre=perfil(usuario).getPerNom().trim().toUpperCase();
        if(nombre.equals("ADMINISTRADOR")||nombre.equals("JEFE"))return TODOS.contains(modulo);
        if(nombre.equals("CLIENTE"))return modulo.equals("CLIENTES");
        if(nombre.equals("EMPLEADO"))return modulo.equals("EMPLEADOS");
        return false;
    }
    public boolean administradorGlobal(JwtUser usuario){return perfil(usuario).getPerNom().equalsIgnoreCase("Administrador");}
}
