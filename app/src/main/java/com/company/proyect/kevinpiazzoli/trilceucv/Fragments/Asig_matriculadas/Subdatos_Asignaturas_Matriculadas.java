package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas;

/**
 * Created by KevinPiazzoli on 01/11/2016.
 */

public class Subdatos_Asignaturas_Matriculadas {
    private String Curso;
    private String Docente;
    private String Ciclo;

    public Subdatos_Asignaturas_Matriculadas(String curso, String docente, String ciclo) {
        Curso = curso;
        Docente = docente;
        Ciclo = ciclo;
    }

    public String getCurso() {
        return Curso;
    }

    public String getDocente() {
        return Docente;
    }

    public String getCiclo() {
        return Ciclo;
    }
}
