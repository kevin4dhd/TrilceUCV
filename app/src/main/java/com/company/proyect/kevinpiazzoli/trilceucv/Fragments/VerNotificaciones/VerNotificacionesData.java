package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.VerNotificaciones;

/**
 * Created by KevinPiazzoli on 29/11/2016.
 */

public class VerNotificacionesData {
    private int FotoNotificacion;
    private String Titulo;
    private String Fecha;
    private String Descripcion;

    VerNotificacionesData(){}

    public int getFotoNotificacion() {
        return FotoNotificacion;
    }

    public void setFotoNotificacion(int fotoNotificacion) {
        FotoNotificacion = fotoNotificacion;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
