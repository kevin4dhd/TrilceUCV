package com.company.proyect.kevinpiazzoli.trilceucv.Fragments.Asig_matriculadas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.company.proyect.kevinpiazzoli.trilceucv.BaseDeDatos.BaseDeDatosUCV;
import com.company.proyect.kevinpiazzoli.trilceucv.MisAsignaturas.MisAsignaturas_Activity;
import com.company.proyect.kevinpiazzoli.trilceucv.R;

import java.util.ArrayList;

public class Asignaturas_Matriculadas extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView ListaDeCursos;
    private String[][] DatosAsignaturas;
    private BaseDeDatosUCV UCVdb;
    private ArrayList<Subdatos_Asignaturas_Matriculadas> ArrayDatos;
    private Asignaturas_Adapter adaptador;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UCVdb = new BaseDeDatosUCV(getActivity());
        ListaDeCursos = (ListView) getActivity().findViewById(R.id.ListaDeCursos);
        DatosAsignaturas = UCVdb.ObtenerAsignaturasAdapter(0);


        ArrayDatos = new ArrayList<>();
        adaptador = new Asignaturas_Adapter(getActivity(),ArrayDatos);
        ListaDeCursos.setAdapter(adaptador);

        for(int i=0;i<DatosAsignaturas.length;i++) {
            ArrayDatos.add(new Subdatos_Asignaturas_Matriculadas(DatosAsignaturas[i][0], DatosAsignaturas[i][1], DatosAsignaturas[i][2]));
        }

        ListaDeCursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),MisAsignaturas_Activity.class);
                intent.putExtra("key_curso", DatosAsignaturas[position][3]);
                startActivity(intent);
            }
        });

    }

    public Asignaturas_Matriculadas() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asignaturas__matriculadas, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
