package com.company.proyect.kevinpiazzoli.trilceucv.UltimasNoticias;

/**
 * Created by Kevin Piazzoli on 15/10/2016.
 */

public class Card {
    private long id;
    private String Titulo;
    private String Descripcion;
    private String Fecha;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}