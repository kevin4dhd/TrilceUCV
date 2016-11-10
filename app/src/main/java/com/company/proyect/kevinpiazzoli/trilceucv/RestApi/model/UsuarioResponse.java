package com.company.proyect.kevinpiazzoli.trilceucv.RestApi.model;

/**
 * Created by KevinPiazzoli on 08/11/2016.
 */

public class UsuarioResponse {

    private String id;
    private String token;
    private String user;

    public UsuarioResponse() {
    }

    public UsuarioResponse(String id, String token, String user) {
        this.id = id;
        this.token = token;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getusuario() {
        return user;
    }

    public void setusuario(String user) {
        this.user = user;
    }

}
