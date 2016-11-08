package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas;

/**
 * Created by KevinPiazzoli on 26/10/2016.
 */

public class AddTablayout {

    private final static int ELEMENTOS = 2;
    private final static String INFORMACION_DEL_CURSO = "Información del Curso";
    private final static String RECURSOS = "Recursos";

    public static String[] TabAll(){
        String[] tabAll = new String[ELEMENTOS];
        tabAll[0]=INFORMACION_DEL_CURSO;
        tabAll[1]=RECURSOS;
        return tabAll;
    }

    public static int getELEMENTOS() {
        return ELEMENTOS;
    }

    public static String getInformacionDelCurso() {
        return INFORMACION_DEL_CURSO;
    }

    public static String getRECURSOS() {
        return RECURSOS;
    }
}
