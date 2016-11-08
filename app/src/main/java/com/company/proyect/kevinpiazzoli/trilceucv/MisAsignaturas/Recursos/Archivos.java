package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Recursos;

/**
 * Created by KevinPiazzoli on 04/11/2016.
 */

public class Archivos {

    boolean estadoDescarga;
    String NombreDelArchivo;
    String NombreDelArchivoFile;
    String AbrirFileRutaArchive;
    String Semana;
    String Tamaño;
    String RutaArchivo;
    String URLArchivo;
    String extension;

    public Archivos(boolean estadoDescarga,
                    String nombreDelArchivo,
                    String nombreDelArchivoFile,
                    String abrirFileRutaArchive,
                    String semana,
                    String tamaño,
                    String rutaArchivo,
                    String URLArchivo,
                    String extension) {
        this.estadoDescarga = estadoDescarga;
        NombreDelArchivo = nombreDelArchivo;
        NombreDelArchivoFile = nombreDelArchivoFile;
        AbrirFileRutaArchive = abrirFileRutaArchive;
        Semana = semana;
        Tamaño = tamaño;
        RutaArchivo = rutaArchivo;
        this.URLArchivo = URLArchivo;
        this.extension = extension;
    }
}
