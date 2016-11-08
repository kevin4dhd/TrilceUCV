package com.company.proyect.kevinpiazzoli.trilceucv.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.proyect.kevinpiazzoli.trilceucv.R;

/**
 * Created by Kevin Piazzoli on 15/10/2016.
 */

public class View_Noticia extends Fragment{

    private OnFragmentInteractionListener mListener;

    public View_Noticia() {
    }

    @Override

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String Descripcion = getArguments() != null ? getArguments().getString("Descripcion") : "No hay descripcion";
        String Fecha = getArguments() != null ? getArguments().getString("Fecha") : "No hay fecha";

        TextView Descripcion_textView = (TextView) getActivity().findViewById(R.id.Descripcion);
        TextView Fecha_textView = (TextView) getActivity().findViewById(R.id.Fecha);

        Descripcion_textView.setText(Descripcion);
        Fecha_textView.setText(Fecha);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_noticias, container, false);
    }

    public static View_Noticia newInstance(Bundle arguments){
        View_Noticia f = new View_Noticia();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Noticias.OnFragmentInteractionListener) {
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
