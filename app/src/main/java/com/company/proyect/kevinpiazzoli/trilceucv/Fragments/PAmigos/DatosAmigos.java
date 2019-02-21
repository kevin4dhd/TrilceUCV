package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.PAmigos;

/**
 * Created by KevinPiazzoli on 13/11/2016.
 */

public class DatosAmigos {

    int Foto;
    String Nombre;
    String Apellido;
    String user;
    String Amigo;

    DatosAmigos() {}

    public int getFoto() {
        return Foto;
    }

    public void setFoto(int foto) {
        Foto = foto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAmigo() {
        return Amigo;
    }

    public void setAmigo(String amigo) {
        Amigo = amigo;
    }
}
