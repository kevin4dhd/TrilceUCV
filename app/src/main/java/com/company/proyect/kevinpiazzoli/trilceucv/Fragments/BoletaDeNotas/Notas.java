package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.BoletaDeNotas;

import android.content.Context;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KevinPiazzoli on 05/11/2016.
 */

public class Notas {

    String Titulo;
    String Subdato;
    String Dato;
    static DecimalFormat formato = new DecimalFormat("0.00");
    static DecimalFormatSymbols custom=new DecimalFormatSymbols();

    public Notas(String titulo, String subdato, String dato) {
        this.Titulo = titulo;
        this.Subdato = subdato;
        this.Dato = dato;
    }

    static List<List<Notas>> NOTASCompletas = new ArrayList<List<Notas>>();
    static ArrayList<Notas> NOTAS = new ArrayList<Notas>();

    static void Actualizar(Context context){
        NOTASCompletas.clear();
        NOTAS.clear();
        for(int i=0;i<CursosNotas.getTitulosDeCurso().length;i++){

            NOTAS = new ArrayList<Notas>();

            String promedio1 = retornarRespuesta(
                    CursosNotas.NotasCursos[i][1],
                    CursosNotas.NotasCursos[i][2],
                    CursosNotas.NotasCursos[i][3]);

            String promedio2 = retornarRespuesta(
                    CursosNotas.NotasCursos[i][4],
                    CursosNotas.NotasCursos[i][5],
                    CursosNotas.NotasCursos[i][6]);

            String promedio3 = retornarRespuesta2(
                    CursosNotas.NotasCursos[i][7],
                    CursosNotas.NotasCursos[i][8],
                    CursosNotas.NotasCursos[i][9]);

            NOTAS.add(new Notas(context.getString(R.string.parcial) + " N°1","", ""));
            NOTAS.add(new Notas("",context.getString(R.string.nota)+ " 1", CursosNotas.NotasCursos[i][1]));
            NOTAS.add(new Notas("",context.getString(R.string.nota)+ " 2", CursosNotas.NotasCursos[i][2]));
            NOTAS.add(new Notas("",context.getString(R.string.examen_parcial), CursosNotas.NotasCursos[i][3]));
            NOTAS.add(new Notas("",context.getString(R.string.promedio), promedio1));
            NOTAS.add(new Notas("",context.getString(R.string.formula), "0.30*N1+0.30*N2+0.40*EP"));

            NOTAS.add(new Notas(context.getString(R.string.parcial)+ " N°2","", ""));
            NOTAS.add(new Notas("",context.getString(R.string.nota)+ " 1", CursosNotas.NotasCursos[i][4]));
            NOTAS.add(new Notas("",context.getString(R.string.nota)+ " 2", CursosNotas.NotasCursos[i][5]));
            NOTAS.add(new Notas("",context.getString(R.string.examen_parcial), CursosNotas.NotasCursos[i][6]));
            NOTAS.add(new Notas("",context.getString(R.string.promedio), promedio2));
            NOTAS.add(new Notas("",context.getString(R.string.formula), "0.30*N1+0.30*N2+0.40*EP"));

            NOTAS.add(new Notas(context.getString(R.string.parcial)+ " N°3","", ""));
            NOTAS.add(new Notas("",context.getString(R.string.nota)+ " 1", CursosNotas.NotasCursos[i][7]));
            NOTAS.add(new Notas("",context.getString(R.string.nota)+ " 2", CursosNotas.NotasCursos[i][8]));
            NOTAS.add(new Notas("",context.getString(R.string.examen_parcial), CursosNotas.NotasCursos[i][9]));
            NOTAS.add(new Notas("",context.getString(R.string.promedio), promedio3));
            NOTAS.add(new Notas("",context.getString(R.string.formula), "0.25*N1+0.25*N2+0.50*EP"));

            NOTAS.add(new Notas(context.getString(R.string.promedio),"", ""));
            NOTAS.add(new Notas("",context.getString(R.string.promedio_final_redondeado), RetornaRespuestaFinal2(promedio1,promedio2,promedio3)));
            NOTAS.add(new Notas("",context.getString(R.string.promedio_final_en_decimales), RetornaRespuestaFinal(promedio1,promedio2,promedio3)));
            NOTAS.add(new Notas("",context.getString(R.string.formula), "0.2*P+0.3*P+0.5*P"));
            NOTASCompletas.add(NOTAS);
        }
    }

    private static String retornarRespuesta(String uno, String dos, String tres){
        double nota1=Double.parseDouble(uno);
        double nota2=Double.parseDouble(dos);
        double nota3=Double.parseDouble(tres);
        double promedio;
        promedio = nota1*0.30 + nota2 * 0.30 + nota3 * 0.40;
        custom.setDecimalSeparator('.');
        formato.setDecimalFormatSymbols(custom);
        return formato.format(promedio);
    }

    private static String retornarRespuesta2(String uno, String dos, String tres){
            double nota1=Double.parseDouble(uno);
            double nota2=Double.parseDouble(dos);
            double nota3=Double.parseDouble(tres);
            double promedio;
            promedio = nota1*0.25 + nota2 * 0.25 + nota3 * 0.50;
            custom.setDecimalSeparator('.');
            formato.setDecimalFormatSymbols(custom);
            String respuesta = formato.format(promedio);
        return respuesta;
    }

    private static String RetornaRespuestaFinal(String uno, String dos, String tres){
        double nota1=Double.parseDouble(uno);
        double nota2=Double.parseDouble(dos);
        double nota3=Double.parseDouble(tres);
        double promedio;
        promedio = nota1*0.2 + nota2 * 0.3 + nota3 * 0.5;
        custom.setDecimalSeparator('.');
        formato.setDecimalFormatSymbols(custom);
        String respuesta = formato.format(promedio);
        return respuesta;
    }

    private static String RetornaRespuestaFinal2(String uno, String dos, String tres){
        double nota1=Double.parseDouble(uno);
        double nota2=Double.parseDouble(dos);
        double nota3=Double.parseDouble(tres);
        int promedio;
        promedio = (int) Math.rint(nota1*0.2 + nota2 * 0.3 + nota3 * 0.5);
        return Integer.toString(promedio);
    }

}
