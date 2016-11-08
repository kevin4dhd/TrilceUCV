package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos;

/**
 * Created by KevinPiazzoli on 06/11/2016.
 */

public class Recursos {

    String extension;
    String NombreDelArchivo;
    String Tamaño;
    String Semana;
    String SemanaVista;

    public Recursos(String extension, String nombreDelArchivo, String tamaño, String semana, String semanaVista) {
        this.extension = extension;
        NombreDelArchivo = nombreDelArchivo;
        Tamaño = tamaño;
        Semana = semana;
        SemanaVista = semanaVista;
    }
}
