package com.company.proyect.kevinpiazzoli.trilceucv.Tablas;

/**
 * Created by KevinPiazzoli on 28/10/2016.
 */

public class Tabla_datos {

    private String dato1;
    private String dato2;
    private String Titulo;

    public Tabla_datos(String Titulo, String dato1, String dato2) {
        this.Titulo=Titulo;
        this.dato1 = dato1;
        this.dato2 = dato2;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDato1() {
        return dato1;
    }

    public void setDato1(String dato1) {
        this.dato1 = dato1;
    }

    public String getDato2() {
        return dato2;
    }

    public void setDato2(String dato2) {
        this.dato2 = dato2;
    }
}
