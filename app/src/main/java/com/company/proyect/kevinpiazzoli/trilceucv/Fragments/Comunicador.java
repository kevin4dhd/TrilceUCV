package com.company.proyect.kevinpiazzoli.trilceucv.Fragments;

/**
 * Created by Kevin Piazzoli on 28/09/2016.
 */

public interface Comunicador {
    public void responder(String datos);
    public void notificacion(String datos);
    public void EntrarALaNoticia(String Titulo,String Descripcion, String Fecha);
}
