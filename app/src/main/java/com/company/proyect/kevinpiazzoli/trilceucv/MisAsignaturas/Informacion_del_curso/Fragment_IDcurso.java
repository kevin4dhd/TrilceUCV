package com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.Informacion_del_curso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Comunicador;
import com.company.proyect.kevinpiazzoli.trilceucv.R;
import com.company.proyect.kevinpiazzoli.trilceucv.Tablas.DatosAdapter;
import com.company.proyect.kevinpiazzoli.trilceucv.Tablas.Tabla_datos;

import java.util.ArrayList;

/**
 * Created by KevinPiazzoli on 26/10/2016.
 */

public class Fragment_IDcurso extends Fragment{

    private Comunicador comunicacion;
    private String keys[] ={
            "escuela",
            "periodo",
            "curriculo",
            "codigo",
            "asignatura",
            "seccion",
            "docente",
            "creditos",
            "ciclo",
            "horas_teorias",
            "horas_practicas"};
    private ArrayList<Tabla_datos> ArrayDatos;
    private ListView lista_datos;
    private DatosAdapter adaptador;
    private String InformacionID_curso[][];
    private BaseDeDatosUCV UCVdb;
    private String CodigoCurso;

    public Fragment_IDcurso(){
    }

    public static Fragment_IDcurso newInstance() {
        return new Fragment_IDcurso();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_misasignaturas, container, false);

        lista_datos = (ListView) view.findViewById(R.id.listView);

        ArrayDatos = new ArrayList<>();
        adaptador = new DatosAdapter(getActivity(),ArrayDatos);
        lista_datos.setAdapter(adaptador);

        Intent extras = getActivity().getIntent();
        Bundle bundle = extras.getExtras();

        if(bundle!=null){
            UCVdb = new BaseDeDatosUCV(getActivity());
            CodigoCurso = bundle.getString("key_curso");
            InformacionID_curso = UCVdb.ObtenerInformacionAsignaturaMatricuolada(0,CodigoCurso,keys);
            ActualziarTablas();
        }

        return view;
    }

    public void ActualziarTablas(){
        ArrayDatos.clear();
        String datos[] = {
                "Escuela",
                "Periodo",
                "Currículo",
                "Código",
                "Asignatura",
                "Sección",
                "Docente",
                "Créditos",
                "Ciclo",
                "Horas de Teoría",
                "Horas de Práctica"
        };
        ArrayDatos.add(new Tabla_datos("Información de la Asignatura","",""));
        for(int i = 0; i < keys.length; i++) {
            ArrayDatos.add(new Tabla_datos("",datos[i],InformacionID_curso[i][0]));
        }
        lista_datos.setAdapter(adaptador);
    }
}
