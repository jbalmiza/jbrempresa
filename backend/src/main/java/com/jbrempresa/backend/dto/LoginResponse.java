package com.jbrempresa.backend.dto;

// Respuesta del login
public class LoginResponse {

    private String token;
    
    private Long usuarioId;
    private Long clienteId;
    private Long perfilId;
    
    private String usuario;
    private String cliente;
    private String perfil;
    
    // Constructor vacío
    public LoginResponse() {

    }
    
    // Constructor de la clase
    public LoginResponse(

            String token,

            Long usuarioId,
            String usuario,

            Long clienteId,
            String cliente,

            Long perfilId,
            String perfil) {

        this.token = token;

        this.usuarioId = usuarioId;
        this.usuario = usuario;

        this.clienteId = clienteId;
        this.cliente = cliente;

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

    // Cliente Id
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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
    
    // Cliente
    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    // Perfil
    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

}