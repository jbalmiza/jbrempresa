package com.jbrempresa.backend.dto;

// Respuesta del login
public class LoginResponse {

    private String token;
    
    private Long usuarioId;
    private Long empresaId;
    private Long perfilId;
    
    private String usuario;
    private String empresa;
    private String perfil;
    
    // Constructor vacío
    public LoginResponse() {

    }
    
    // Constructor de la clase
    public LoginResponse(

            String token,

            Long usuarioId,
            String usuario,

            Long empresaId,
            String empresa,

            Long perfilId,
            String perfil) {

        this.token = token;

        this.usuarioId = usuarioId;
        this.usuario = usuario;

        this.empresaId = empresaId;
        this.empresa = empresa;

        this.perfilId = perfilId;
        this.perfil = perfil;

    }

    // Token JWT
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    // Usuario Id
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    // Empresa Id
    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    // Perfil Id
    public Long getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(Long perfilId) {
        this.perfilId = perfilId;
    }

    // Usuario
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    // Empresa
    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    // Perfil
    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

}