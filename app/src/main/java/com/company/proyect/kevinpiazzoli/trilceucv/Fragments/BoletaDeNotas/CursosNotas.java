package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas;

import android.content.Context;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;

import java.util.List;

/**
 * Created by KevinPiazzoli on 05/11/2016.
 */

public class CursosNotas {

    private BaseDeDatosUCV UCVdb;
    static String TitulosDeCurso[];
    static String NotasCursos[][];

    public CursosNotas(Context context) {
        UCVdb = new BaseDeDatosUCV(context);
        List<ArreglosdeArreglos> DatosCursos = UCVdb.obtenerCursosyCodeyNotas(0);
        TitulosDeCurso = DatosCursos.get(0).Array1;
        NotasCursos = DatosCursos.get(0).Array3;
    }

    static String[] getTitulosDeCurso() {
        return TitulosDeCurso;
    }
}
